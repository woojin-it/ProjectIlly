package com.c.illy.payment;

import java.net.HttpURLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.c.illy.address.AddressService;
import com.c.illy.address.AddressVO;
import com.c.illy.cart.CartProductVO;
import com.c.illy.cart.CartService;
import com.c.illy.cart.CartVO;
import com.c.illy.coupon.CouponService;
import com.c.illy.coupon.CouponVO;
import com.c.illy.member.MemberService;
import com.c.illy.member.MemberVO;
import com.c.illy.product.ProductService;
import com.c.illy.product.ProductVO;
import com.c.illy.util.Pager;

@Controller
@RequestMapping("/payment/**")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private CartService cartService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ProductService productService;
	
	// 장바구니 통해서 온 목록
	@GetMapping("paymentList")
	public void getPaymentList(@AuthenticationPrincipal MemberVO memberVO, Model model) throws Exception {
		couponService.setDeadlineState(); //쿠폰상태 - 사용기간만료
		
		List<CartProductVO> ar = cartService.getCartListCheck(memberVO); //선택 상품 List
		List<CouponVO> coupon = couponService.getCouponList(memberVO); //modal - 쿠폰적용
		List<AddressVO> ar2 = addressService.getAddressList(memberVO); //modal _ 배송지관리
		AddressVO addressVO = addressService.getAddressLatest(memberVO); //최근배송지
		AddressVO addressVO2 = addressService.getDefaultAddress(memberVO); //기본배송지
		AddressVO addressVO3 = addressService.getJoinAddress(memberVO); //내 정보에 있는 주소
		
		
		model.addAttribute("addressJoin", addressVO3);
		model.addAttribute("addressDefault", addressVO2);
		model.addAttribute("addressVO", addressVO);
		model.addAttribute("memberVO", memberService.usernameSelect(memberVO));
		model.addAttribute("paymentList", ar);
		model.addAttribute("addressList", ar2);
		model.addAttribute("coupon", coupon);
	}
	
	// 바로구매를 통해서 온 목록
	@GetMapping("directPayment")
	public String getDirectPayment(@AuthenticationPrincipal MemberVO memberVO, CartVO cartVO, Model model) throws Exception {
		int result = cartService.setPaymentCart(cartVO, memberVO);
		
		couponService.setDeadlineState(); //쿠폰상태 - 사용기간만료
		
		List<CartProductVO> ar = cartService.getDirectPayment(memberVO); //선택 상품 List
		List<CouponVO> coupon = couponService.getCouponList(memberVO); //modal - 쿠폰적용
		List<AddressVO> ar2 = addressService.getAddressList(memberVO); //modal _ 배송지관리
		AddressVO addressVO = addressService.getAddressLatest(memberVO); //최근배송지
		AddressVO addressVO2 = addressService.getDefaultAddress(memberVO); //기본배송지
		AddressVO addressVO3 = addressService.getJoinAddress(memberVO); //내 정보에 있는 주소
		

		model.addAttribute("addressJoin", addressVO3);
		model.addAttribute("addressDefault", addressVO2);
		model.addAttribute("addressVO", addressVO);
		model.addAttribute("memberVO", memberService.usernameSelect(memberVO));
		model.addAttribute("paymentList", ar);
		model.addAttribute("addressList", ar2);
		model.addAttribute("coupon", coupon);
		
		return "payment/paymentList";
	}
	
	// 결제 완료 - product 기계 구매했을 때 시리얼넘버 생성하기
	@RequestMapping(value = "insertPayment",  method = RequestMethod.POST)
	@ResponseBody
	public Integer setPaymentInsert(HttpServletRequest request, CouponVO couponVO, PaymentVO paymentVO, MemberVO memberVO, 
			Model model, AddressVO addressVO, CartVO cartVO) throws Exception {
		
		addressVO.setMember_id(paymentVO.getMember_id());
		int result = addressService.setPaymentAddress(addressVO); //배송받을 주소 insert
		
		addressVO = addressService.getAddressOne();
		paymentVO.setAddress_id(addressVO.getAddress_id());
		result = paymentService.setPayment(paymentVO); //결제완료
		
		paymentVO = paymentService.getPaymentOne();
		
		result = cartService.setPaymentID(paymentVO, cartVO, request); //결제상태 update, serialNumber 생성
		result = memberService.setAddBean(memberVO, paymentVO); //결제 후 포인트 적립
		
		if(couponVO.getCoupon_id() != 0) {
			//쿠폰 사용 시 쿠폰 상태 update
			couponVO.setPayment_id(paymentVO.getPayment_id());
			result = couponService.setUseState(couponVO);
		}
		
		return paymentVO.getPayment_id();
	}
	
	//결제 완료
	@GetMapping("paymentEnd")
	public void getPaymentEnd(Model model, PaymentVO paymentVO) throws Exception {

		List<CartProductVO> ar = paymentService.getPaymentCart(paymentVO); //결제완료 상품 list
		
		model.addAttribute("cartList", ar);
		model.addAttribute("addressVO", addressService.getAddressOne());
		model.addAttribute("paymentVO", paymentService.getPaymentOne());
	}
	
	//주문취소 - point 감소, 쿠폰사용 취소
	@GetMapping("setPaymentCancel")
	public String setPaymentCancel(@AuthenticationPrincipal MemberVO memberVO, Model model, PaymentVO paymentVO, CartVO cartVO, Pager pager) throws Exception {
		int result = cartService.setPaymentCancel(paymentVO, memberVO);
		
		List<PaymentVO> list = paymentService.getMyPageOrderPager(paymentVO, cartVO, pager); 
		
		model.addAttribute("list", list);
		model.addAttribute("count", paymentService.getMyPageOrderCount(paymentVO, cartVO));
		model.addAttribute("pager", pager);
		return "member/myPageOrder/myPageOrderAjax";
	}
	
	//환불 - point 감소, point 감소, 쿠폰사용 취소
	@GetMapping("setPaymentRefund")
	public String setPaymentRefund(@AuthenticationPrincipal MemberVO memberVO, Model model, PaymentVO paymentVO, CartVO cartVO, Pager pager) throws Exception {
		int result = cartService.setPaymentRefund(paymentVO, memberVO);
		
		List<PaymentVO> list = paymentService.getMyPageOrderPager(paymentVO, cartVO, pager);
		
		model.addAttribute("list", list);
		model.addAttribute("count", paymentService.getMyPageOrderCount(paymentVO, cartVO));
		model.addAttribute("pager", pager);
		return "member/myPageOrder/myPageOrderAjax";
	}
	
	//주문취소 - point 감소, 쿠폰사용 취소 (상세페이지에서)
	@RequestMapping("setPaymentCancelDetail")
	@ResponseBody
	public String setPaymentCancelDetail(@AuthenticationPrincipal MemberVO memberVO, Model model, PaymentVO paymentVO) throws Exception {
		int result = cartService.setPaymentCancel(paymentVO, memberVO);
		
		return "주문취소가 정상처리 되었습니다.";
	}
	
	//환불 - point 감소, point 감소, 쿠폰사용 취소 (상세페이지에서)
	@RequestMapping("setPaymentRefundDetail")
	@ResponseBody
	public String setPaymentRefundDetail(@AuthenticationPrincipal MemberVO memberVO, Model model, PaymentVO paymentVO) throws Exception {
		int result = cartService.setPaymentRefund(paymentVO, memberVO);
		
		return "환불처리가 정상처리 되었습니다.";
	}
	
	
	
	
	
	// 네이버페이로 구매했을 경우 - 일리랑 상관 X
	@GetMapping("naverpay")
	public void naverpayPopup(Model model, ProductVO productVO, Integer cnt) throws Exception {
		model.addAttribute("productVO", productService.getSelectProductOne(productVO));
		model.addAttribute("cnt", cnt);
	}
	@GetMapping("naverpayResultTest")
	public void naverpayResultTest(String paymentId) throws Exception {
		HttpURLConnection httpURLConnection = null;
//		URL url = "https://dev.apis.naver.com/naverpay-partner/naverpay/payments/v2/apply/payment";
		System.out.println(paymentId);
	}
}
