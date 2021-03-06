<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

	<link rel="stylesheet" href="/css/common.css">
	<link rel="stylesheet" href="/css/admin/insertBoard.css">
	<!-- include summernote css/js -->
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
	
	<style type="text/css">
		#right_content{width:1000px;float:left;padding-left:40px;}
		.board_zone_tit h2{font-size:24px; color:#333333;padding-bottom:10px; margin-bottom:9px;    }
		.board_zone_cont{border-top:1px solid #999; width:100%; }
		.faq_write tbody tr th{font-size:12px; line-height:1.5; color:#333; margin:0; border-bottom:1px solid #dbdbdb; padding:13px 0 13px 25px; font-weight:bold; background-color:#fbfbfb;text-align:left; vertical-align:top;  }
		.faq_write tbody tr td{font-size:12px; line-height:1.5; color:#333;height:32px; margin:0; padding:7px 0 11px 15px; border-bottom:1px solid #dbdbdb; background-color:#fff;  }
		.faq_write tbody tr input{width:320px; height:31px; padding:0 10px; border:1px solid #d6d6d6; color:#333; line-height:31px;  }
		#faq_type_code{width:127px; height:31px; color:#717171; vertical-align:top; outline:none; font-size:12px; line-height:1.5; border:1px solid #dbdbdb;  }
		.btn_wrap{position:relative; text-align:center; margin-top:30px;  }
		.boardBtn{display:inline-block; font-weight:bold;  background-color:#fff; width:100px; height:42px; padding:0 5px; color:#3e3d3c; font-size:14px; border:1px solid #ccc; text-align:center; cursor:pointer; }
		.boardBtn.upload{ background-color:#000; color:#fff; min-width:90px; padding:0 10px;  border-color:1px solid #000; margin-left:6px; }
	</style>


<title>???????????? ?????????</title>
</head>
<body>
	<div id="wrapper">
		<c:import url="/WEB-INF/views/admin/adNavbar.jsp"></c:import>
		<div id="container" style="padding-bottom: 155px; margin-bottom: -155px;">
			<div id="contents">
				<div class="left_cont">
					<c:import url="/WEB-INF/views/admin/adLeftMenu.jsp"></c:import>		
				</div>
	
				<div class="right_cont">			
					<h2>UPDATE FAQ</h2>
					<form action="./updateFaq" id="faqAdFrm" method="post">
						<table class="faq_write">
							<colgroup>
								<col style="width:143px">
								<col style="width:808px">
							</colgroup>
							<tbody>
								<tr>
									<th scope="row">?????? ??????</th>
									<td>
										<select id="faq_type_code" name="faq_type_code" value="${faqVO.faq_type_code}">
											<option class="pickType" value="f_1">????????????/????????????</option>
											<option class="pickType" value="f_2">??????/??????/??????</option>
											<option class="pickType" value="f_3">??????/??????/??????/??????</option>
											<option class="pickType" value="f_4">????????????</option>
											<option class="pickType" value="f_5">????????????(Y1.1)</option>
											<option class="pickType" value="f_6">????????????(Y3/Y3.2/Y3.3)</option>
											<option class="pickType" value="f_7">????????????(X7/X7.1)</option>
											<option class="pickType" value="f_8">AS??????</option>
											<option class="pickType" value="f_9">????????????</option>
										</select>	
										<input hidden="" id="faq_type" name="faq_type" value="" >									
									</td>
								</tr>
								<tr>
									<th scope="row">??????</th>
									<td>
										<input hidden="" name="faq_id" value="${faqVO.faq_id}">
										<input type="text" style="width:80%;" class="faq_title" name="faq_title" value="${faqVO.faq_title}">
									</td>
								</tr>
								<tr>
									<th scope="row">??????</th>
									<td>
										<textarea class="summernote" name="faq_contents" id="faq_contents" >
											${faqVO.faq_contents}
										</textarea>
									</td>
								</tr>
							</tbody>
					
						</table>
						<div class="btn_wrap">
							<button type="button" onclick="location.href='./adFaqList'" class="goback boardBtn">??????</button>
							<button type="button" class="upload boardBtn">??????</button>
						</div>
					</form>
					
				</div>
				
			</div>	
			
		</div>
		<c:import url="/WEB-INF/views/admin/adFooter.jsp"></c:import>
	</div>
	
	
	
	
	<script type="text/javascript">

	$('.summernote').summernote({
		tabsize: 2,
		height: 500,
		toolbar: [
				['style', ['style']],
				['font', ['bold', 'underline', 'clear']],
				['color', ['color']],
				['para', ['ul', 'ol', 'paragraph']],
				['table', ['table']],
				['insert', ['link', 'picture', 'video']],
				['view', ['fullscreen', 'codeview', 'help']]
			['view', ['codeview']]
		]
	});
	
	//??????????????? ?????? ?????????????????? ?????? ???????????? ?????? ????????? ?????? ??????????????????
	const faq_type_code='${faqVO.faq_type_code}';
	$('.pickType').each(function(){
		const value=$(this).val();
		if(value==faq_type_code){
			$(this).prop("selected",true);
		}
	});	
	
	
	
	//faq_type_code??? ????????? faq_type ??? db?????? ????????? ?????? ???????????? form submit ?????????  
	$(".upload").click(function(){
		var code=$("#faq_type_code").val();
		
		if(code=="f_1"){
			$("#faq_type").val("????????????/????????????");
		}else if(code=="f_2"){
			$("#faq_type").val("??????/??????/??????");
		}else if(code=="f_3"){
			$("#faq_type").val("??????/??????/??????/??????");
		}else if(code=="f_4"){
			$("#faq_type").val("????????????");
		}else if(code=="f_5"){
			$("#faq_type").val("????????????(Y1.1)");
		}else if(code=="f_6"){
			$("#faq_type").val("????????????(Y3/Y3.2/Y3.3)");
		}else if(code=="f_7"){
			$("#faq_type").val("????????????(X7/X7.1)");
		}else if(code=="f_8"){
			$("#faq_type").val("AS??????");
		}else{
			$("#faq_type").val("????????????");
		}
		
		$("#faqAdFrm").submit();
	});
	
	
	
</script>
	

	
</body>
</html>