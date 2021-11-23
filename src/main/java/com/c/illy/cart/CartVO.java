package com.c.illy.cart;

import java.util.List;

import com.c.illy.member.MemberVO;
import com.c.illy.product.ProductVO;

import lombok.Data;

@Data
public class CartVO {
	
	private int cart_id;
	private int member_id;
	private String product_id;
	private int cart_cnt;
	private String cart_state;
}