<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
			
			<c:choose>
				<c:when test="${not empty parameterCateCode}">	
					<c:if test="${requestor eq 'product'}">
											
						<c:choose>
							<c:when test="${parameterCateCode eq 001}"><h2>COFFEE</h2></c:when>
							<c:when test="${parameterCateCode eq 002}"><h2>MACHINES</h2></c:when>
						</c:choose>
						
						<c:forEach var="categoryCnt" items="${categoryCnt}">
							<c:if test="${parameterCateCode eq categoryCnt.code}">
								<h2>${categoryCnt.name}</h2>
							</c:if>
						</c:forEach>
						
						<img class="listMainImg" alt="" src="/images/product/list/productList_${parameterCateCode}.jpg">
										
						<div class="productCategory">
							<a href="/product/list?product_categoryCode=${categoryAllCnt.code}">${categoryAllCnt.name} <span>(${categoryAllCnt.count})</span></a>
							<c:forEach var="categoryCnt" items="${categoryCnt}">
								<a href="/product/list?product_categoryCode=${categoryCnt.code}">${categoryCnt.name} <span>(${categoryCnt.count})</span></a>
							</c:forEach>
						</div>
							
					</c:if>				
					
					<c:if test="${requestor eq 'admin'}">
						<h2 style="font-size: 28px; color: #222; width: 100%; font-weight: bold; text-align: left; padding-bottom: 20px; border-bottom: 1px solid #dbdbdb; margin-bottom: 70px;">
						PRODUCT LIST</h2>
							
						<div class="productCategory">
							<a href="/admin/list?product_categoryCode=${categoryAllCnt.code}">${categoryAllCnt.name} <span>(${categoryAllCnt.count})</span></a>
							<c:forEach var="categoryCnt" items="${categoryCnt}">
								<a href="/admin/list?product_categoryCode=${categoryCnt.code}">${categoryCnt.name} <span>(${categoryCnt.count})</span></a>
							</c:forEach>
						</div>
					</c:if>
				</c:when>
				<c:otherwise>
					<h2>ALL PRODUCT</h2>
					<img class="mainImg" alt="" src="/images/product/list/ALL_PRODUCT.jpg">
				</c:otherwise>
			</c:choose>

				<div class="productPickList">
					
					<c:if test="${
						empty parameterCateCode
						|| parameterCateCode eq '001'
						|| parameterCateCode eq '002'
					}">
						<span class="pickListNum">?????? <strong>${categoryAllCnt.count}</strong> ???</span>
					</c:if>
						
					<c:forEach var="categoryCnt" items="${categoryCnt}">
						<c:if test="${parameterCateCode eq categoryCnt.code}">
							<span class="pickListNum">?????? <strong>${categoryCnt.count}</strong> ???</span>
						</c:if>
					</c:forEach>
					
					<ul class="pickListBox">
						<li>
							<input type="radio" id="sort1" name="product_id DESC" onclick="javascript:getAjaxList(1, 'product_id DESC')">
							<label for="sort1" class="sortLabel">????????????</label>
						</li>
						<li>
							<input type="radio" id="sort2" name="review_star DESC" onclick="javascript:getAjaxList(1, 'review_star DESC')">
							<label for="sort2" class="sortLabel">????????????</label>
						</li>
						<li>
							<input type="radio" id="sort3" name="product_price ASC" onclick="javascript:getAjaxList(1, 'product_price ASC')">
							<label for="sort3" class="sortLabel">???????????????</label>
						</li>
						<li>
							<input type="radio" id="sort4" name="product_price DESC" onclick="javascript:getAjaxList(1, 'product_price DESC')">
							<label for="sort4" class="sortLabel">???????????????</label>
						</li>
					</ul>
				</div>
				
				<ul class="productList">
				<c:forEach var="productVO" items="${listAllProduct}">
					<li>
						<div class="productWrap">
							<a href="/product/select?product_categoryCode=${productVO.product_categoryCode}&product_id=${productVO.product_id}">
								<div class="productImgWrap">
									<img alt="" src="/upload/product/${productVO.productFileVOs[0].productFile_name}">
								</div>
								<span class="productName">${productVO.product_name}</span>
							</a>
							<div class="iconBox"></div>
							<span class="productPrice pricePattern">${productVO.product_price}</span>
							<c:if test="${requestor eq 'product'}">
								<button class="btnDefault red insertCart" data-product-id="${productVO.product_id}" data-product-name="${productVO.product_name}">????????????</button>
								<button class="btnDefault red directPayment" data-product-id="${productVO.product_id}">????????????</button>
							</c:if>
							<c:if test="${requestor eq 'admin'}">
								<c:choose>
									<c:when test="${categoryAllCnt.code eq 001}">
										<button class="btnDefault red" onclick="location.href='/admin/updateCoffee?product_id=${productVO.product_id}'">????????????</button>
										<button class="btnDefault red" onclick="location.href='/admin/updateProductState?product_id=${productVO.product_id}&product_categoryCode=001'">????????????</button>
									</c:when>
									<c:when test="${categoryAllCnt.code eq 002}">
										<button class="btnDefault red" onclick="location.href='/admin/updateMachine?product_id=${productVO.product_id}'">????????????</button>
										<button class="btnDefault red" onclick="location.href='/admin/updateProductState?product_id=${productVO.product_id}&product_categoryCode=002'">????????????</button>
									</c:when>
									<c:when test="${categoryAllCnt.code eq 003}">
										<button class="btnDefault red" onclick="location.href='/admin/updateAccessories?product_id=${productVO.product_id}'">????????????</button>
									</c:when>
								</c:choose>
							</c:if>
						</div>
					</li>
				</c:forEach>
				
				<!-- ???????????? ?????? ??? modal -->
				<div id="addCartLayer" class="modal_wrap" style="display: none;">
					<div class="add_cart_layer">
						<div class="add_cart_layerView">
				            <h2>???????????? ??????</h2>
				            <div>
				                <p id="productAddcartImages"><strong>????????? ??????????????? ???????????????.</strong><br>?????? ?????????????????????????</p>
				            </div>
				            <div class="add_cart_btn_box">
				                <button class="add_cart_btn_cancel close_a_btn"><span>??????</span></button>
				                <button class="add_cart_btn_move"><span>??????</span></button>
				            </div>
				            <a class="add_cart_top_cancel close_a_btn"><img src="/images/cart/btn_layer_close.png" alt="??????"></a>
						</div> <!-- add_cart_layerView end -->
					</div>	<!-- add_cart_layer end -->			
				</div> <!-- addCartLayer end --> <!-- ???????????? ?????? ??? modal end -->
				</ul>
				
				<div class="pagination">
					<ul>						
					<c:forEach begin="${pager.startNum}" end="${pager.lastNum}" varStatus="i">
						<c:if test="${pager.pn eq pager.startNum + (i.count-1)}">
							<li><span>${pager.startNum + (i.count-1)}</span></li>
						</c:if>
						<c:if test="${pager.pn ne pager.startNum + (i.count-1)}">
							<li><a href="javascript:getAjaxList(${pager.startNum + (i.count-1)}, '${pager.sort}')">${pager.startNum + (i.count-1)}</a></li>
						</c:if>
					</c:forEach>
					</ul>
				</div>
				
			</div>
			
			
<script type="text/javascript">

	$().ready(function(){
		setPricePattern();
		sortRadioBoxCheck();
	});

	function setPricePattern() {
		$('.pricePattern').each(function(){
			$(this).html(Number($(this).html()).toLocaleString() + '???');
		})			
	}
	
	function sortRadioBoxCheck() {
		$('input[type=radio]').each(function(){
			if($(this).attr('name') == '${pager.sort}') {
				$(this).prop('checked', true);
				$(this).siblings('label').addClass('on');
			}
		});
	}

	$('input[type=radio]').change(function(){
		$('.sortLabel').each(function(){ $(this).removeClass('on')})
		$(this).next().addClass('on');
	}); /********** ?????? ????????? ????????? **********/
	
	$('.productWrap').hover(function(){
		$(this).find('button').fadeIn( "fast" );
	}, function(){
		$(this).find('button').css('display', 'none');
	}); /********** ?????? ????????? ??? ?????????, ?????? ?????? **********/
	
	/* ijy - ?????? ?????????????????? */
	$('.directPayment').click(function(){
		console.log('product_id: '+$(this).attr('data-product-id'));
		
		location.href="/payment/directPayment?cart_cnt=1&product_id="+$(this).attr('data-product-id');
	});
	
	//???????????? ??????
	$('.insertCart').click(function(){
		if($('#member_id').val() == ''){
// 			alert('???????????? ???????????????.');
			location.href="/member/login";
		} else {
			$.ajax({
				type:"GET",
				url:"/cart/setCart",
				data: {
					product_id: $(this).attr('data-product-id'),
					cart_cnt: 1
				},
				success: function(result) {
					$('#addCartLayer').css("display", "block");//modal ?????????
					$("body").css("overflow","hidden");//body ???????????? ?????????
				},
				error : function(xhr, status, error){
					console.log(error);				
				}
			
			});
			
		}
	});
	
	$('.add_cart_btn_move').click(function(){
		location.href="/cart/normalBasket";
	}); //???????????? ?????? ??? ??????????????? ??????
	$('.close_a_btn').click(function(){
		$('#addCartLayer').css("display", "none");//modal ?????????
		$("body").css("overflow","auto");//body ???????????? ??????
	}); //?????? ?????? X ?????? ?????? ??? ??????????????? ??????
	
</script>