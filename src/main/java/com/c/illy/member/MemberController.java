package com.c.illy.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.servlet.ModelAndView;

import com.c.illy.address.AddressRepository;
import com.c.illy.address.AddressService;
import com.c.illy.address.AddressVO;
import com.c.illy.cart.CartService;
import com.c.illy.cart.CartVO;
import com.c.illy.coupon.CouponService;
import com.c.illy.coupon.CouponVO;
import com.c.illy.payment.PaymentVO;
import com.c.illy.qna.QnaService;
import com.c.illy.qna.QnaVO;
import com.c.illy.util.Pager;

@Controller
@RequestMapping("member/**")
public class MemberController {
	@Autowired
	private MemberService memberService;

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private CartService cartService;
	@Autowired
	private CouponService couponService;

	//--다영 
	@Autowired
	private QnaService qnaService; 
	
	@GetMapping("join_agreement")
	public ModelAndView join_agreement() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("member/join_agreement");
		return mv;
	}

	@GetMapping("join_agreement_detail")
	public ModelAndView join_agreement_detail() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("member/join_agreement_detail");
		return mv;
	}

	@GetMapping("join_agreement_detail2")
	public ModelAndView join_agreement_detail2() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("member/join_agreement_detail2");
		return mv;
	}

	@GetMapping("join")
	public String join(Model model, HttpSession httpSession) {
		AddressVO addressVO = new AddressVO();
		model.addAttribute("addressVO", addressVO);
		return "/member/join";
	}

	// 회원가입 form 검증
	@PostMapping("join")
	public String join(@Valid AddressVO addressVO, BindingResult bindingResult, HttpServletRequest request)
			throws Exception {
		if (bindingResult.hasErrors()) {
			return "/member/join";
		}

		memberService.setInsert(addressVO);

		// api로 받아온 우편번호, 주소, 참고항목, 상세정보를 address 변수에 합침
		addressService.setAddress(addressVO, request);
		return "redirect:/";
	}

	// Ajax 아이디 중복검사
	@GetMapping("checkId")
	public ModelAndView checkId(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		MemberVO memberVO = new MemberVO();
		memberVO.setUsername(request.getParameter("username"));
		memberVO = memberService.usernameSelect(memberVO);
		mv.setViewName("member/common/checkId");
		mv.addObject("checkId", memberVO);
		return mv;
	}

	@GetMapping("login")
	public String login() {
		return "member/login";
	}

	@PostMapping("login")
	public String login(HttpServletRequest httpServletRequest) {

		return "member/login";
	}
	@GetMapping("findId")
	public String findId() {
		return "member/find_id";
	}


	@GetMapping("find_id")
	public ModelAndView findId(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		MemberVO memberVO = new MemberVO();
		
		if(request.getParameter("member_phone") == null) {
			memberVO.setMember_name(request.getParameter("member_name"));
			memberVO.setMember_email(request.getParameter("member_email"));	
		
		}
		else if (request.getParameter("member_email") == null) {
			memberVO.setMember_name(request.getParameter("member_name"));
			memberVO.setMember_phone(request.getParameter("member_phone"));
		}
		memberVO = memberService.find_id(memberVO);
		mv.addObject("findId", memberVO);
		mv.setViewName("member/common/Find_id");
		return mv;
	}

	@GetMapping("findPw")
	public ModelAndView findPw() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("member/find_pw");
		return mv;
	}
	
	@GetMapping("changeMemberPassword")
	public ModelAndView changeMemberPassword() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("member/change_member_password");
		return mv;
	}
	
	@GetMapping("changeMember")
	public ModelAndView changeMember(@Valid AddressVO addressVO, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("member/change_member");
		return mv;
	}

// ----------------------------------------------------- ijy ----------------------------------------------------	
	
	/* my page - 주문목록/배송조회 */
	@GetMapping("myPage/myPageOrder")
	public String getMyPageOrder(@AuthenticationPrincipal MemberVO memberVO, Model model) throws Exception {
		
		model.addAttribute("member", memberVO);
		return "member/myPageOrder/myPageOrder";
	}
	@GetMapping("myPage/myPageOrderPager")
	public String getMyPageOrderPager(PaymentVO paymentVO, CartVO cartVO, Pager pager, Model model) throws Exception {
		
		 List<PaymentVO> list = cartService.getMyPageOrderPager(paymentVO, cartVO, pager);
	
			
		PaymentVO paymentVO2 = new PaymentVO();
			
		for(Integer i=0;i<list.size();i++) {
			paymentVO2.setPayment_id(list.get(i).getPayment_id());
			cartService.setPaymentDelivery(paymentVO2); //배송중으로 변경
			cartService.setPaymentDone(paymentVO2); //배송완료로 변경
		}
			
		list = cartService.getMyPageOrderPager(paymentVO, cartVO, pager);
		  
		
		model.addAttribute("list", list);
		model.addAttribute("count", cartService.getMyPageOrderCount(paymentVO, cartVO));
		model.addAttribute("pager", pager);
		return "member/myPageOrder/myPageOrderAjax";
	}
	
	
	/* my page - 주문목록/배송조회 : 상세페이지 */
	@GetMapping("myPage/myPageOrderDetail")
	public String getMyPageOrderDetail(@AuthenticationPrincipal MemberVO memberVO, PaymentVO paymentVO, Model model) throws Exception {
		paymentVO = cartService.getMyPageOrderDetail(paymentVO);
		AddressVO addressDefault = addressService.getJoinAddress(memberVO);
		AddressVO addressOrder = addressService.getAddressOrder(paymentVO);
		
		model.addAttribute("list", paymentVO);
		model.addAttribute("addressDefault", addressDefault); //주문자 정보 배송지 가져오기
		model.addAttribute("addressOrder", addressOrder); //배송지 정보 배송지 가져오기
		
		return "member/myPageOrder/myPageOrderDetail";
	}
	
	/* my page - 취소/반품/교환 내역 */
	@GetMapping("myPage/myPageCancel")
	public String getMyPageCancel(@AuthenticationPrincipal MemberVO memberVO, Model model) throws Exception {
		
		model.addAttribute("member", memberVO);
		return "member/myPageOrder/myPageCancel";
	}
	
	/* my page - 취소/반품 처리 현황 tab */
	@GetMapping("myPage/myPageCancelPager")
	public String getMyPageCancelPager(PaymentVO paymentVO, CartVO cartVO, Pager pager, Model model) throws Exception {
		
		 List<PaymentVO> list = cartService.getMyPageOrderPager(paymentVO, cartVO, pager);
		  
		
		model.addAttribute("list", list);
		model.addAttribute("count", cartService.getMyPageOrderCount(paymentVO, cartVO));
		model.addAttribute("pager", pager);
		return "member/myPageOrder/myPageCancelAjax";
	}
	/* my page - 취소/반품 신청 내역 tab */
	@GetMapping("myPage/myPageCancelNone")
	public String getMyPageCancelNone() throws Exception {
		return "member/myPageOrder/myPageCancelNone";
	}
	
	
	/* my page - 환불/입금 내역 */
	@GetMapping("myPage/myPageRefund")
	public String getMyPageRefund(@AuthenticationPrincipal MemberVO memberVO, Model model) {
		
		model.addAttribute("member", memberVO);
		return "member/myPageOrder/myPageRefund";
	}
	
	/* my page - 환불 처리 현황 tab */
	@GetMapping("myPage/myPageRefundPager")
	public String getMyPageRefundPager(PaymentVO paymentVO, CartVO cartVO, Pager pager, Model model) throws Exception {
		
		 List<PaymentVO> list = cartService.getMyPageOrderPager(paymentVO, cartVO, pager);
		  
		
		model.addAttribute("list", list);
		model.addAttribute("count", cartService.getMyPageOrderCount(paymentVO, cartVO));
		model.addAttribute("pager", pager);
		return "member/myPageOrder/myPageRefundAjax";
	}
	
	/* my page - 환불/입금 신청 내역 tab */
	@GetMapping("myPage/myPageRefundNone")
	public String getMyPageRefundNone() throws Exception {
		return "member/myPageOrder/myPageRefundNone";
	}
	
	
	/* my page - 쿠폰 */
	@GetMapping("myPage/myPageCoupon")
	public String getMyPageCoupon(@AuthenticationPrincipal MemberVO memberVO, Model model) throws Exception {
		couponService.setDeadlineState(); //쿠폰 사용기간 만료 update
		memberVO = memberService.getSelect(memberVO);

		model.addAttribute("couponSize", couponService.getCouponList(memberVO).size());
		model.addAttribute("member", memberVO);
		return "member/myPageCoupon/myPageCoupon";
	}
	
	/* my page - 쿠폰 사용가능 tab */
	@GetMapping("myPage/myPageCouponPager")
	public String getMyPageCouponPager(Pager pager, CouponVO couponVO, Model model) throws Exception {
		
		List<CouponVO> list = couponService.getCouponPager(pager, couponVO);
		  
		
		model.addAttribute("list", list);
		model.addAttribute("count", couponService.getCouponCount(couponVO));
		model.addAttribute("pager", pager);
		return "member/myPageCoupon/myPageCouponAjax";
	}
	/* my page - 쿠폰 사용불가 tab */
	@GetMapping("myPage/myPageCouponNone")
	public String getMyPageCouponNone(Pager pager, CouponVO couponVO, Model model) throws Exception {
		
		List<CouponVO> list = couponService.getCouponPager(pager, couponVO);
		  
		
		model.addAttribute("list", list);
		model.addAttribute("count", couponService.getCouponCount(couponVO));
		model.addAttribute("pager", pager);
		return "member/myPageCoupon/myPageCouponNone";
	}
// ----------------------------------------------------- ijy end ------------------------------------------------

	

	//----------------------------------------------------------------------------myPage_다영 추가 start
	@GetMapping("myPage")
	public String getmyPage()throws Exception{
		return"member/myPage";
	}
	
	//--1:1 문의 페이지
	@GetMapping("qnaList")
	public String getQnaList(ModelAndView mv)throws Exception{
		System.out.println("저길 오나");
		return "board/qnaList"; 
	}
	
	//--1:1 문의 ajax
	@GetMapping("qnaListDate")
	public ModelAndView getQnaListDate(ModelAndView mv,QnaVO qnaVO,Pager pager)throws Exception{
		System.out.println("여길 오나");
		List<QnaVO> ar = qnaService.getQnaList(pager, qnaVO);
		System.out.println(ar.size());
		mv.setViewName("board/qnaListajax");
		mv.addObject("QList", ar);
		mv.addObject("pager", pager);
		return mv; 
	}
	
	//1:1문의 작성하기 
	@GetMapping("addQna")
	public String addQna()throws Exception{
		return "board/addQna";
	}
	
	@PostMapping("addQnaList")
	public void setAddQna()throws Exception{
		
	}
	
	
	@ResponseBody
	@RequestMapping(value = "VerifyRecaptcha", method = RequestMethod.POST)
	public int VerifyRecaptcha(HttpServletRequest request) {
	    com.c.illy.util.VerifyRecaptcha.setSecretKey("시크릿 코드");
	    String gRecaptchaResponse = request.getParameter("recaptcha");
	    try {
	       if(com.c.illy.util.VerifyRecaptcha.verify(gRecaptchaResponse))
	          return 0; // 성공
	       else return 1; // 실패
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1; //에러
	    }
	}
	
	
	
	//----------------------------------------------------------------------------myPage_다영 추가 end
	
	
	
	
}
