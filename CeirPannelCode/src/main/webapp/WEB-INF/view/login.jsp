 <%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Security Tags -->
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:csrfMetaTags />
<!-- Security Tags -->
<%@page import="java.util.Locale"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">

<head><title>CEIR Portal</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="msapplication-tap-highlight" content="no">
<meta name="description"
	content="Materialize is a Material Design Admin Template,It's modern, responsive and based on Material Design by Google. ">
<meta name="keywords"
	content="materialize, admin template, dashboard template, flat admin template, responsive admin template,">
	<!-- Security Tags -->
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<!-- Security Tags -->
<!--<title>CEIR Portal</title>-->


<!-- Favicons-->
<link rel="icon" href="${context}/resources/images/DMC-Logo.png" sizes="32x32">

<!-- For Windows Phone -->
<link rel="stylesheet"
	href="${context}/resources/font/font-awesome/css/font-awesome.min.css">

<!-- CORE CSS-->
<link href="${context}/resources/css/materialize.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link href="${context}/resources/css/style.css" type="text/css"
	rel="stylesheet" media="screen,projection">

<!-- INCLUDED PLUGIN CSS ON THIS PAGE -->
<link href="${context}/resources/js/plugins/prism/prism.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/js/plugins/chartist-js/chartist.min.css"
	type="text/css" rel="stylesheet" media="screen,projection">

<style>
.boton {
	color: #2979a5;
	float: right;
	border: solid 1px rgba(33, 167, 201, 0.774);
	padding: 4px 10px;
	border-radius: 7px;
	font-size: 14px;
	background-color: #fff;
}

.row {
	margin-bottom: 0;
	margin-top: 0;
}

/* @media only screen and (min-width: 601px) .row .col.m6 {
            margin-top: 0;
            margin-bottom: 0;
            height: 40px;
        } */
input[type=text] {
	margin: 0 0 5px 0;
}

.fa-eye-slash, .fa-eye {
	position: absolute;
	right: 10px;
	top: 10px;
}

a#newUserLink {
	padding-right: 10px;
	line-height: 13px;
}

.forgotPassword {
	padding-left: 10px;
	border-left: solid 2px #9e9e9e;
	line-height: 13px;
}
</style>
<script>
	var contextpath = "${context}";
</script>
</head>

<body data-msg="${msg}" data-lang-param="${pageContext.response.locale}" data-userID="${userid}">
<c:remove var="userid" scope="session"/>
	<!-- //////////////////////////////////////////////////////////////////////////// -->
<div id="initialloader" style="display:none"></div>


	<!-- //////////////////////////////////////////////////////////////////////////// -->

	<!-- START MAIN -->
	<div id="">
		<!-- START WRAPPER -->
		<div class="wrapper">



			<!-- //////////////////////////////////////////////////////////////////////////// -->

			<!-- START CONTENT -->
			<section id="content">
				<!--start container-->
				<div class="container">
					<div class="section">
						<div class="row card-panel login-card-panel">
							<form id="loginForm" onsubmit="return login()">
								<div class="col s12 m12 l12">
									<div class="row">
<div class="col s9 m10 select-lang-lable">
										<i class="fa fa-globe fa-6" aria-hidden="true"></i>
										</div>
										<div class="col s3 m2 right" style="padding: 0;">
											<select class="browser-default select-lang-drpdwn"
												id="langlist">
												<option value="en">English</option>
												<option value="km"><spring:message code="lang.khmer" /></option>
											</select>
										</div>
										<div class="col s12 m12">
											<h5 style="text-align: -webkit-center;">
												<spring:message code="registration.login" />
											</h5>
											<span id="errorMsg" style="color: red;"></span>
											<hr>


										</div>



										<div class="input-field col s12">
											<input type="text" name="username"
												id="username" class="" 
												oninput="setCustomValidity('')"  oninvalid="this.setCustomValidity('<spring:message code="validation.requiredMsg" />')"
												title= "<spring:message code="validation.requiredMsg" />" required  maxlength="10" /> <label
												for="username"><spring:message code="registration.username" /></label>
										</div>

										<div  class="input-field col s12" id="show_hide_password">
											<input type="password"   class="password"autocomplete="off"
												name="password" id="password" 
												oninput="setCustomValidity('')" oninvalid="this.setCustomValidity('<spring:message code="validation.requiredMsg" />')"
												title= "<spring:message code="validation.requiredMsg" />" required maxlength="10"
												oncopy="return false" onpaste="return false" /> <label
												for="password"> <spring:message code="registration.password" /></label>
											<div class="input-field-addon">
												<i   class="fa fa-eye-slash teal-text toggle-password" aria-hidden="true"></i>
											</div>

										</div>

										<div class="form-group form-actions col s12 m12">
											<span class="input-icon"> <img id="captchaImage"
												src="${context}/captcha">
												<button
													style="background: none; border: none; outline: none; color: black;"
													type="button" onclick="refreshCaptcha('captchaImage')">
													<i class="fa fa-refresh"></i>
												</button> <%-- <img src="${context}/captcha"" id="captchaImage">
						 <br>
                           <input type="button" onclick="refreshCaptcha('captchaImage')"> --%>
												<div class="input-field">
													<input autocomplete="off" type="text" name="captcha"
														class="form-control boxBorder boxHeight" id="captcha"
														oninput="setCustomValidity('')" oninvalid="this.setCustomValidity('<spring:message code="validation.requiredMsg" />')"
												title= "<spring:message code="validation.requiredMsg" />" required> <label for="captcha"
														style="left: 0.01rem;"><spring:message
															code="registration.enteryourcaptcha" /><span
														class="star">*</span> </label>
												</div>

											</span>
										</div>




										<div class="row" style="margin: 30px 0 20px 0;">
											<div class="input-field col s12 m12 l12 center">
												<%--     <a href="${context}/importerDashboard" class="btn" type="button" id="save" style="width: 100%;">Login</a> --%>
												<button type="submit" class="btn" id="save"
													style="width: 100%;" value="Login">
													<spring:message code="registration.login" />
												</button>
											</div>
										</div>

										<a href="${context}/forgotPassword"
											class="right forgotPassword"><spring:message
												code="registration.forgotpassword" /></a> <span><a
											href="JavaScript:Void(0);" id="newUserLink" class="right"><spring:message
													code="registration.newUser" /></a></span>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				<!--end container-->
			</section>
			<!-- END CONTENT -->

		</div>
		<!-- END WRAPPER -->

	</div>
	<!-- END MAIN -->


<div id="changePasswordMessage" class="modal" style="width: 40%;">
<h6 class="modal-header"><spring:message code="registration.changepassword" /></h6>
<div class="modal-content">
<div class="row">
<h6 id="cPassSucessMsg"></h6>
</div>
<div class="row">
<div class="center">
<a href="" class="btn"><spring:message code="modal.ok" /></a>
</div>
</div>
</div>
</div>


	<div id="changePassword" class="modal" style="width: 40%;">
		<h6 class="modal-header"><spring:message code="registration.changepassword" /></h6>
		<div class="modal-content">
			<form onsubmit="return changeExpiryPassword()">
				<div class="row">

                   <input type="hidden" id="userId" >
					<span style="text-align: center; color: red;" id="errorMsg"></span>
					<div class="col s1">
						<i class="fa fa-lock" aria-hidden="true"
							style="font-size: 30px; margin-top: 12px; color: #ff4081;"></i>
					</div>
					<div class="input-field col s11">
						<input type="password" id="oldPassword" class="password2" autocomplete="off"
							pattern="<spring:eval expression="@environment.getProperty('pattern.password')" />"
							 oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
						    
							oninvalid="this.setCustomValidity('<spring:message code="validation.requiredMsg" />')"
							title="Please enter atleast one numeric char, one alphabet, one special character and must be of minumum 8 length"
							required /> <label for="oldPassword"
							class="center-align" style="color: #000; font-size: 12px;">
							 <spring:message code="registration.oldpassword" /></label>
							<div class="input-field-addon">
												<i  class="fa fa-eye-slash teal-text toggle-password2" aria-hidden="true"></i>
											</div>
					</div>

					<div class="col s1">
						<span class="fa-passwd-reset fa-stack"
							style="margin-top: 12px; color: #ff4081;"> <i
							class="fa fa-undo fa-stack-2x"></i> <i
							class="fa fa-lock fa-stack-1x"></i>
						</span>
					</div>
					<div class="input-field col s11">

						<label for="newPassword" style="color: #000; font-size: 12px;"><spring:message code="registration.newpassword" /></label> <input type="password"
							pattern="<spring:eval expression="@environment.getProperty('pattern.password')" />" autocomplete="off"
							
							oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
						<%-- oninvalid="InvalidMsg(this,'input','<spring:message code="validation.password" />');" --%>
						oninvalid="this.setCustomValidity('<spring:message code="validation.requiredMsg" />')"
							required id="password2" class="password3" />
				<div class="input-field-addon">
		<i  class="fa fa-eye-slash teal-text toggle-password3" aria-hidden="true"></i>
											</div>				
					</div>

					<div class="col s1">
						<i class="fa fa-check-square-o" aria-hidden="true"
							style="font-size: 28px; margin-top: 12px; color: #ff4081;"></i>
					</div>
					<div class="input-field col s11">

						<label for="confirm_password"
							style="color: #000; font-size: 12px;"><spring:message code="registration.confirmpassword" /></label> <input 
							type="password" class="password4" id="confirm_password" autocomplete="off"
							pattern="<spring:eval expression="@environment.getProperty('pattern.password')" />"
							oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
						    oninvalid="this.setCustomValidity('<spring:message code="validation.requiredMsg" />')"
							required />
						<div class="input-field-addon">
				<i  class="fa fa-eye-slash teal-text toggle-password4" aria-hidden="true"></i>
											</div>		
					</div>
				</div>
				<div class="row" style="margin-top: 30px;">
					<div class="input-field col s12 center">
						<button class="btn" id="updateStatusBtn" type="submit"><spring:message code="button.submit" /></button>
						<button type="button" class="btn modal-close"
							style="margin-left: 10px;"><spring:message code="button.cancel" /></button>
					</div>

				</div>
			</form>
		</div>
	</div>


	<!-- //////////////////////////////////////////////////////////////////////////// -->





	<!-- ================================================
    Scripts
    ================================================ -->
	<!-- jQuery Library -->
	<script src="${context}/resources/custom_js/jquery.min.js"></script>
	<script type="text/javascript"
		src="${context}/resources/js/materialize.js"></script>
<script
		src="${context}/resources/custom_js/bootstrap.min.js"></script>
	

<script type="text/javascript" src="${context}/resources/js/plugins.js"></script>

	<script type="text/javascript"
		src="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	
	
	<%-- <script type="text/javascript"
		src="${context}/resources/js/plugins/data-tables/data-tables-script.js"></script>

 --%>				<!-- i18n library -->
	<script type="text/javascript"
		src="${context}/resources/project_js/CLDRPluralRuleParser.js"></script>
	<script type="text/javascript"
		src="${context}/resources/i18n_library/i18n.js"></script>
	<script type="text/javascript"
		src="${context}/resources/i18n_library/messagestore.js"></script>

	<script type="text/javascript"
		src="${context}/resources/i18n_library/fallbacks.js"></script>

	<script type="text/javascript"
		src="${context}/resources/i18n_library/language.js"></script>

	<script type="text/javascript"
		src="${context}/resources/i18n_library/parser.js"></script>


	<script type="text/javascript"
		src="${context}/resources/i18n_library/emitter.js"></script>


	<script type="text/javascript"
		src="${context}/resources/i18n_library/bidi.js"></script>

	<script type="text/javascript"
		src="${context}/resources/i18n_library/history.js"></script>

	<script type="text/javascript"
		src="${context}/resources/i18n_library/min.js"></script>
		<script type="text/javascript"
		src="${context}/resources/ajax/Registration.js?version=<%= (int) (Math.random() * 10) %>"></script>
	
	<script type="text/javascript"
		src="${context}/resources/project_js/globalVariables.js?version=<%= (int) (Math.random() * 10) %>"></script>
	
	<script type="text/javascript" src="${context}/resources/ajax/clearSession.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript" src="${context}/resources/ajax/Login.js?version=<%= (int) (Math.random() * 10) %>"></script>
	 <script type="text/javascript" src="${context}/resources/ajax/Password.js?version=<%= (int) (Math.random() * 10) %>"></script>
<script type="text/javascript" src="${context}/resources/project_js/login.js?version=<%= (int) (Math.random() * 10) %>"></script>
  <script type="text/javascript" src="${context}/resources/js/plugins.js"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/ValidationFileOutsidePortal.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript">
	 if(window.location.href.includes('isExpired=yes') && location.href.indexOf('reload')  ==-1)   
	    {
	         location.href=location.href+'&reload';
	    }
</script>
	<script type="text/javascript"
		src="${context}/resources/ajax/keyBoardShortcut.js?version=<%= (int) (Math.random() * 10) %>"></script>
</body></html>

 