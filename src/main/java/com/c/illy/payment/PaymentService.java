package com.c.illy.payment;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.illy.cart.CartProductVO;
import com.c.illy.cart.CartVO;
import com.c.illy.util.Pager;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	public int setPayment(PaymentVO paymentVO) throws Exception {
		return paymentRepository.setPayment(paymentVO);
	}
	
	public PaymentVO getPaymentOne() throws Exception {
		return paymentRepository.getPaymentOne();
	}
	
	public List<CartProductVO> getPaymentCart(PaymentVO paymentVO) throws Exception {
		return paymentRepository.getPaymentCart(paymentVO);
	}

	public List<PaymentVO> getMyPageOrderPager(PaymentVO paymentVO, CartVO cartVO, Pager pager) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("payment", paymentVO);
		map.put("cart", cartVO);
		
		pager.setPerPage(10);
		pager.makeRow();

		Long totalCount = paymentRepository.getMyPageOrderCount(map);
		pager.makeNum(totalCount);

		map.put("pager", pager);

		return paymentRepository.getMyPageOrderPager(map);
	}
	
	public Long getMyPageOrderCount(PaymentVO paymentVO, CartVO cartVO) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("payment", paymentVO);
		map.put("cart", cartVO);

		return paymentRepository.getMyPageOrderCount(map);
	}
	
	public Long getMyPageTotalCount(PaymentVO paymentVO, CartVO cartVO) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("payment", paymentVO);
		map.put("cart", cartVO);
		
		return paymentRepository.getMyPageTotalCount(map);
	}
	
	public PaymentVO getMyPageOrderDetail(PaymentVO paymentVO) throws Exception {
		return paymentRepository.getMyPageOrderDetail(paymentVO);
	}

	//작성가능한리뷰리스트-다영
	public List<PaymentVO> myReviewList(PaymentVO paymentVO, CartVO cartVO, Pager pager) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("payment", paymentVO);
		map.put("cart", cartVO);
		map.put("pager", pager);
		
		pager.setPerPage(10);
		pager.makeRow();

		Long totalCount = paymentRepository.getMyReviewCount(map);
		pager.makeNum(totalCount);
		List<PaymentVO> ar=paymentRepository.myReviewList(map);
		
		System.out.println(ar.size()+"-------------------");
		
		map.put("pager", pager);
		System.out.println("========");
		System.out.println("member_id:==== " + paymentVO.getMember_id());
		System.out.println("pn: ====" + pager.getPn());
		System.out.println("totalCount:=== " + totalCount);

		return paymentRepository.myReviewList(map);
	}
	public Long getMyReviewCount(PaymentVO paymentVO, CartVO cartVO) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("payment", paymentVO);
		map.put("cart", cartVO);

		return paymentRepository.getMyReviewCount(map);
	}

}
