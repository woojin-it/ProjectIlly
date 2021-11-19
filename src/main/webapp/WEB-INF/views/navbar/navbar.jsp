<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

	<link rel="stylesheet" href="/css/navbar.css">

</head>
<body>

	<div class="gnbHeadWrap">
		
		<div class="gnbTop">	
				
			<div class="slideWrap">
			
				<span class="slideBtn prev">◀</span>
			
				<div class="slideTrack" data-slide_content_cnt="4">
					<c:forEach begin="0" end="3" varStatus="i">
						<div class="slideContent">
							<a href="javascript:;">Slider Content Index : ${i.count}</a>
						</div>
					</c:forEach>
					<c:forEach begin="0" end="1" varStatus="i">
						<div class="slideContent">
							<a href="javascript:;">Slider Content Index : ${i.count}</a>
						</div>
					</c:forEach>
				</div>
				
				<span class="slideBtn next">▶</span>
				
			</div>
			
			<a class="tempBtn">illy CAFFE</a>
						
		</div>
		
		<div class="gnbBottom">
		
			<div class="logo">
				<a href="/"><img alt="" src="/images/gnb/logo50.jpg"></a>
			</div>
			
			<div class="menuWrap">
				<ul>
					<li><a href="javascript:;">ALL PRODUCT</a></li>
					<li><a href="javascript:;">COFFEE</a></li>
					<li><a href="javascript:;">MACHINES</a></li>
					<li><a href="javascript:;">ILLY ART COLLECTION</a></li>
					<li><a href="javascript:;">ACCESSORIES</a></li>
					<li><a href="javascript:;">SPECIALLY CURATED SET</a></li>
				</ul>
			</div>
			
			<div class="rightBox">
				<div class="searchWrap">
					<input type="text">
					<img class="searchImg" alt="" src="/images/gnb/new-search.png">
				</div>
				<div class="memberWrap">
					<img class="userImg" alt="" src="/images/gnb/new-user.png">
					<img class="cartImg" alt="" src="/images/gnb/new-cart.png">
				</div>
			</div>
			
		</div>
		
	</div>

</body>
<!-- ===== ===== ===== jquery START ===== ===== ===== -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<script src="/js/trackSlider.js"></script>
	
	<script type="text/javascript">
		/* 우클릭 방지 */
		$("body").contextmenu(function() { return false; });
		
		let slideTrack = $('.slideTrack');
		let slideContent = $('.slideContent');
		
		$().ready(function(){
			navInterval = setInterval(function(){slide(slideTrack, slideContent, 1300);}, 3300);
		})
		
		$('.slideBtn').click(function(){
			/* 슬라이드 작동중 일 경우 종료 */
			if(slideTrack.hasClass('slideActive')) { return; }
			
			/* 슬라이드 연속 방지를 위해 clearInterval후 새롭게 setInterval */
			clearInterval(navInterval);
			navInterval = setInterval(function(){slide(slideTrack, slideContent, 1300);}, 3300);
			
			slide(slideTrack, slideContent, 1300, $(this));
		});		
		
		/* ===== hover event START ===== */
		$('.searchImg').hover(
			function(){	$(this).attr('src', '/images/gnb/new-search-hover.png'); }
			,function(){ $(this).attr('src', '/images/gnb/new-search.png'); }
		);
		$('.userImg').hover(
			function(){	$(this).attr('src', '/images/gnb/new-user-hover.png'); }
			,function(){ $(this).attr('src', '/images/gnb/new-user.png'); }
		);
		$('.cartImg').hover(
			function(){ $(this).attr('src', '/images/gnb/new-cart-hover.png'); }
			,function(){ $(this).attr('src', '/images/gnb/new-cart.png'); }
		);
		/* ===== hover event END ===== */
		
	</script>
<!-- ===== ===== ===== jquery END ===== ===== ===== -->
</html>