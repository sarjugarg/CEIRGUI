<%@ page import="java.util.Date"%>
<%
	response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache-Control", "no-store");
response.setDateHeader("Expires", 0);
response.setHeader("Pragma", "no-cache");

/*   //200 secs
 session.setAttribute("usertype", null);  */
/* 	 session.setMaxInactiveInterval(10); */
int timeout = session.getMaxInactiveInterval();

long accessTime = session.getLastAccessedTime();
long currentTime = new Date().getTime();
long dfd = accessTime + timeout;
if (currentTime < dfd) {
	/*  response.setHeader("Refresh", timeout + "; URL = ../login");
	 System.out.println("timeout========"+timeout); 
	if (session.getAttribute("usertype") != null) { */
%>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<!-- Security Tags -->
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:csrfMetaTags />
<!-- Security Tags -->
<%@ taglib prefix='spring' uri='http://www.springframework.org/tags'%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en" class="no-js">
<head><title>CEIR Portal</title>
<!--<title>Dashboard</title>-->
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta content="" name="description" />
<meta content="" name="author" />

<!-- Security Tags -->
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />
<!-- Security Tags -->


<script type="text/javascript"
	src="${context}/resources/js/plugins/jquery-1.11.2.min.js"></script>
	<script
		src="${context}/resources/custom_js/bootstrap.min.js"></script>
	
<link rel="shortcut icon" href="">
<!-- CORE CSS-->
<link href="${context}/resources/css/materialize.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link href="${context}/resources/css/style.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<!-- Custome CSS-->

<!-- Favicons-->
<link rel="icon" href="${context}/resources/images/DMC-Logo.png"
	sizes="32x32">
<!-- INCLUDED PLUGIN CSS ON THIS PAGE -->
<link href="${context}/resources/js/plugins/prism/prism.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/font/font-awesome/css/font-awesome.min.css"
	type="text/css" rel="stylesheet" media="screen,projection">


<link rel="stylesheet"
	href="${context}/resources/custom_js/jquery-ui.css">
<%-- <script src="${context}/resources/custom_js/1.12.1_jquery-ui.min.js"></script>
 --%>
<script src="${context}/resources/custom_js/jquery.blockUI.js"></script>

<!------------------------------------------- Dragable Model---------------------------------->
<script src="${context}/resources/custom_js/1.12.1_jquery-ui.min.js"></script>


<script>
	var contextpath = "${context}";
<%String usertype = (String) session.getAttribute("usertype");
String name = (String) session.getAttribute("name");
Integer usertypeId = (Integer) session.getAttribute("usertypeId");
if (usertypeId == null) {
	usertypeId = 0;
}

Integer selfRegister = (Integer) session.getAttribute("selfRegister");
if (selfRegister == null) {
	selfRegister = 0;
}%>
	
</script>

</head>
<style>
.fa-eye-slash, .fa-eye {
	position: absolute;
	right: 10px;
	top: 10px;
}

div#modalMessageBody {
	text-align: center;
	margin-top: 12px;
}

div#error_Modal {
	width: 550px;
	height: 50px;
	margin-top: 14%;
}

</style>

<body data-lang="${language}" data-usertype="${usertype}"
	data-roleType="${usertype}" data-userTypeID="${usertypeId}"
	data-userID="${userid}" data-selected-roleType="${selectedUserTypeId}"
	data-stolenselected-roleType="${stolenselectedUserTypeId}"
	data-selected-consignmentTxnId="${consignmentTxnId}"
	data-operatorTypeId="${operatorTypeId}"
	data-selected-consignmentStatus="${consignmentStatus}"
	data-selected-username="${username}"
	data-defaultLink="${defaultLink}"
	data-currentTime=" <%=currentTime%>"
	data-dfd=" <%=dfd%>"
	data-user-state="${userStatusValue}">
	<!-- Start Page Loading -->
	<div id="loader-wrapper">
		<div id="initialloader"></div>
		<div class="loader-section section-left"></div>
		<div class="loader-section section-right"></div>
	</div>
	<!-- End Page Loading -->
	<!-- //////////////////////////////////////////////////////////////////////////// -->
	<!-- START HEADER -->
	<header id="header" class="page-topbar">
		<!-- start header nav-->
		<div class="navbar-fixed">
			<nav class="navbar-color">
				<div class="nav-wrapper">
					<ul class="left">
						<li>
							<div class="offset-md-1 text-right px-0 ml-3 my-auto">
								<a href="javascript:void(0)" rel="noopener noreferrer"> <img
									src="./resources/images/DMC-Logo.png" id="header-img"
									class="darken-1 my-2" style="height: 56px;"></a>
							</div>
						</li>
						<li>
							<h1 class="logo-wrapper">
								<a href="#" class="brand-logo darken-1"><spring:message
										code="page.ceir" /> <span id="cierRoletype"> <spring:message
											code="roletype.${fn:replace(sessionScope.usertype, ' ', '_')}" />
								</span> <spring:message code="page.portal" /> <%
 	if ("Operator".equalsIgnoreCase(usertype)) {
 %> - <%=session.getAttribute("operatorTypeName")%> <%
 	} else {
 }
 %> </a> <span class="logo-text"><spring:message
										code="registration.materialize" /></span>
							</h1>
						</li>
					</ul>
					<ul id="chat-out" class="right hide-on-med-and-down"
						style="overflow: inherit !important;">
						<li>
						<form id="manualDownload" autocomplete="off" action="./Consignment/ManualFileDownload" method="post">
						<input type="hidden" name="userTypeId" value="${usertypeId}">
						<input type="hidden" name="${_csrf.parameterName}"value="${_csrf.token}" />
						<input type="hidden" name="contextName">
						<a href="javascript:{}" style="color: white; cursor: pointer;" onclick="document.getElementById('manualDownload').submit();">
						<i
								class="fa fa-download download-icon" aria-hidden="true"
								title="<spring:message code="title.manual" />" style="color: #fff; line-height: 3;"></i></a>
							</form>	
						<li>
							<div id="divLang" style="display: flex; margin: 8px 6px;"
								class="darken-1">
								<div id="iconLable" class="darken-1">
									<i class="fa fa-globe fa-6" aria-hidden="true"
										style="line-height: 4"></i>
								</div>
								<div style="width: 80px !important;">
									<select class="darken-1" id="langlist"
										style="border-bottom: none; height: 42px; background: #00bcd4; line-height: 1; border: 1px solid #00bcd4 !important;">
										<option value="en" style="color: #444;">English</option>
										<option value="km" style="color: #444;"><spring:message
												code="lang.khmer" /></option>
									</select>
								</div>
							</div>
						</li>
						<%-- 						<li>
                             <a href="javascript:void(0)"  
							 style="color:rgba(0, 0, 0, 0.3);; cursor: pointer;"><spring:message
									code="registration.home" /></a></li> --%>
						<li class="profileInfo"><a
							class="btn-flat dropdown-button waves-effect waves-light white-text profile-btn"
							href="#" data-activates="profile-dropdown" style="height: 40px;"><i
								class="mdi-action-account-circle"
								style="color: #fff; font-size: 40px;"></i></a>
							<ul id="profile-dropdown" class="dropdown-content">
								<%
									if (selfRegister != 0) {
								%>
								<li><a id="editLink" href="javascript:void(0)"
									target="mainArea"><i class="fa fa-pencil dropdownColor"
										style="float: left;"></i><span style="float: left"
										class="dropdownColor"><spring:message
												code="registration.editinfo" /></span></a></li>
					<li class="divider"></li>
								<li><a onclick="manageAccountPopup();"
									href="javascript:void(0)"><i
										class="mdi-action-settings dropdownColor"></i> <span
										class="dropdownColor"> <spring:message
												code="registration.activate/deactivateaccount" /></span></a></li>
								<li class="divider"></li>

								<%
									}
								%>
								
								<%
									if (selfRegister == 0) {
								%>
								<li><a id="viewProfile" href="./userManagement?type=user_view"
									target="mainArea"><i class="fa fa-eye dropdownColor"
										style="float: left;position: inherit;"></i><span style="float: left"
										class="dropdownColor"><spring:message
												code="registration.viewProfile" /></span></a></li>
					<li class="divider"></li>
								

								<%
									}
								%>
								
								<li><a href="javascript:void(0)"
									onclick="changePasswordPopup()"><i
										class="fa fa-key dropdownColor" style="float: left"></i><span
										style="float: left" class="dropdownColor"><spring:message
												code="registration.changepassword" /></span></a></li>
								<li class="divider"></li>

								<li><a href="javascript:void(0)" onclick="openLogout()"
									style="cursor: pointer;" id=""><i style="float: left;"
										class="mdi-hardware-keyboard-tab dropdownColor"></i> <span
										class="dropdownColor"> <spring:message
												code="registration.logout" /></span></a></li>

							</ul></li>
					</ul>
				</div>
			</nav>
		</div>

		<!-- end header nav-->
	</header>
	<!-- END HEADER -->

	<!-- //////////////////////////////////////////////////////////////////////////// -->

	<!-- START MAIN -->
	<div id="main">
		<!-- START WRAPPER -->
		<div class="wrapper">

			<!-- START LEFT SIDEBAR NAV-->
			<aside id="left-sidebar-nav">
				<ul id="slide-out" class="side-nav fixed leftside-navigation">
					<li class="user-details cyan darken-2">
						<div class="row">
						
							<div class="col col s4 m4 l4">
								<!--  <img src="images/avatar.jpg" alt="" class="circle responsive-img valign profile-image"> -->
								<p
									style="width: 180px; text-align: center; color: #fff; font-size: 16px; margin-top: 2px;">
									<spring:message code="page.welcome" />
									<%=name%>
									(<%=(String) session.getAttribute("username")%>)
											<br><span id="userState"></span>
								
								</p>
								
							</div>
						</div>
					
					</li>
				
				
					<li>
						<ul class="navData">
							<c:forEach items="${features}" var="feature">
								<li class="bold"><a
									href="${feature.link}?FeatureId=${feature.id}"
									target="mainArea" class="waves-effect waves-cyan"
									data-featureID="${feature.id}"><i class="${feature.logo}"></i>
										<spring:message
											code="sidebar.${fn:replace(feature.name, ' ', '_')}" /> </a></li>
							</c:forEach>
						</ul>
					</li>
				</ul>
				<a href="#" data-activates="slide-out"
					class="sidebar-collapse btn-floating btn-medium waves-effect waves-light hide-on-large-only cyan"><i
					class="mdi-navigation-menu" id="rightSideToggle"></i></a>
			</aside>
			<!-- END LEFT SIDEBAR NAV-->

			<!-- //////////////////////////////////////////////////////////////////////////// -->

			<!-- START CONTENT -->
			<section id="content">

				<!--breadcrumbs start-->

				<!--breadcrumbs end-->


				<!--start container-->
				<div class="container">
					<div class="section" id="section">
						<!-- 
						<iframe name="mainArea" class="embed-responsive-item" id="mainArea"
							scrolling="yes" frameBorder="0" src="./Home" width="100%"
							height="700px"></iframe> -->
					</div>
					<!-- Floating Action Button -->

					<!-- Floating Action Button -->
				</div>
				<!--end container-->
			</section>
			<!-- END CONTENT -->

			<!-- //////////////////////////////////////////////////////////////////////////// -->
			<!-- START RIGHT SIDEBAR NAV-->
			<aside id="right-sidebar-nav"></aside>
			<!-- LEFT RIGHT SIDEBAR NAV-->

		</div>
		<!-- END WRAPPER -->

	</div>
	<!-- END MAIN -->



	<!-- //////////////////////////////////////////////////////////////////////////// -->

	<!-- START FOOTER -->
	<footer class="page-footer">
		<div class="footer-copyright">
			<div class="container">

				<span id="copyrightText" class="right"><spring:message
						code="registration.copyright" /></span>

			</div>
		</div>
	</footer>

	<!-- END FOOTER -->
	<div id="manageAccount" class="modal">
		<h6 class="modal-header">
			<spring:message code="registration.manageaccount" />
		</h6>

		<div class="modal-content">
			<form id="userStatusForm" onsubmit="return passwordPopup()">
				<span style="text-align: center; color: red;" id="errorMsg"></span>

				<p>
					<spring:message code="registration.requestceiradminto" />
				</p>
				<div class="row" style="height: 30px;">

					<p>
						<label style="margin-right: 50px"> <input type="radio"
							name="status" value="Deactivate" id="deac" oninput="InvalidRadioMsg('radio');"
					 oninvalid="InvalidRadioMsg('radio');"
							required> <span> <spring:message
									code="registration.deactivate" />
						</span></label>
						<spring:message code="registration.permanentlydeleteportal" />
					</p>
				</div>
				<%
					//String status = (String) session.getAttribute("userStatus");
				Integer statusValue = (Integer) session.getAttribute("userStatusValue");
				
				%>
		<%
					if (statusValue == 3) {
				%>
				<div class="row" style="height: 30px;">
					<p>
						<label style="margin-right: 67px"> <input type="radio"
							value="Disable" name="status"
							title=""required  > <span> <spring:message
									code="registration.disable" />
						</span></label>
						<spring:message code="registration.alltheactionwillbe" />
					</p>
				</div>
				<%
					} else if (statusValue == 5) {
				%>
				<div class="row" style="height: 30px;">
					<p>
						<label style="margin-right: 67px"> <input type="radio"
							value="Approved" name="status" required="required"><span>
								<spring:message code="registration.enable" />
						</span></label>
						<spring:message code="registration.allactionable" />
					</p>
				</div>

				<%
					} else {
				}
				%>
				<div class="input-field col s12 center">
					<button class="btn" id="updateStatusBtn">
						<spring:message code="button.submit" />
					</button>
					<button type="button" class="btn modal-close"
						style="margin-left: 10px;">
						<spring:message code="modal.cancel" />
					</button>
				</div>
			</form>
		</div>
	</div>
	<!-- Modal End -->

	<!-- Modal 4 start   -->

	<div id="manageAccountSubmit" class="modal">
		<h6 class="modal-header">
			<spring:message code="registration.manageaccount" />
		</h6>
		<div class="modal-content">
			<h6 id="mgAccount">
				<!-- The request has been successfully registered with CEIR
				Admin. Please find confirmation over registered mail in 2 to 3
				working days. -->
			</h6>

			<div class="input-field col s12 center">
			
				<form action="./login?isExpired=yes" method="post">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />

						<button type="submit" class="btn">
							<spring:message code="modal.ok" />
						</button>
				
					</form>
				
			</div>
		</div>
	</div>
	<!-- Modal End -->

	<!-- Modal 4 start   -->

	<div id="changePassword" class="modal" style="width: 40%;">
		<h6 class="modal-header">
			<spring:message code="registration.changepassword" />
		</h6>
		<div class="modal-content">
			<form id="changePassForm" onsubmit="return changePassword();">
				<div class="row">
					<span style="text-align: center; color: red;" id="errorMsg"></span>
					<div class="col s1">
						<i class="fa fa-lock" aria-hidden="true"
							style="font-size: 30px; margin-top: 12px; color: #ff4081;"></i>
					</div>

					<div class="input-field col s11">
						<input type="password" id="oldPassword" class="password" autocomplete="off"
							pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,10}$"
							maxlength="10" min="8"
							oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
							oninvalid="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
							required /> <label for="oldPassword" class="center-align"
							style="color: #000; font-size: 12px;"> <spring:message
								code="registration.oldpassword" />
						</label>
						<div class="input-field-addon">
							<i class="fa fa-eye-slash teal-text toggle-password"
								aria-hidden="true"></i>
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

						<label for="newPassword" style="color: #000; font-size: 12px;"><spring:message
								code="registration.newpassword" /></label> <input type="password" autocomplete="off"
							pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$"
							maxlength="10" min="8"
							oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
							oninvalid="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
							required id="password" class="password2" />
						<div class="input-field-addon">
							<i class="fa fa-eye-slash teal-text toggle-password2"
								aria-hidden="true"></i>
						</div>
					</div>

					<div class="col s1">
						<i class="fa fa-check-square-o" aria-hidden="true"
							style="font-size: 28px; margin-top: 12px; color: #ff4081;"></i>
					</div>
					<div class="input-field col s11">

						<label for="confirm_password"
							style="color: #000; font-size: 12px;"><spring:message
								code="registration.confirmpassword" /></label> <input type="password" autocomplete="off"
							class="password3" id="confirm_password"
							pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$"
							maxlength="10" min="8"
							oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
							oninvalid="InvalidMsg(this,'input','<spring:message code="validation.password" />');"
							title="<spring:message code="validation.minumum8length" />"
							required>
						<div class="input-field-addon">
							<i class="fa fa-eye-slash teal-text toggle-password3"
								aria-hidden="true"></i>
						</div>
					</div>
				</div>
				<div class="row" style="margin-top: 30px;">
					<div class="input-field col s12 center">
						<%-- <button class="btn" type="submit" id="changePassBtn">
							<spring:message code="button.submit" />
						</button> --%>
						<button class="btn" id="changePassBtn" type="submit"
							style="margin-left: 10px;">
							<spring:message code="button.submit" />
						</button>
						<button type="button" class="btn modal-close"
							style="margin-left: 10px;">
							<spring:message code="modal.cancel" />
						</button>
					</div>

				</div>
			</form>
		</div>
	</div>
	<!-- Modal End -->


	<!-- Modal 2 start   -->


	<!-- Modal 2 start -->

	<div id="submitActivateDeactivate" class="modal">
		<button type="button"
			class=" modal-action modal-close waves-effect waves-green btn-flat right"
			data-dismiss="modal">&times;</button>
		<div class="modal-content">

			<div class="row">
				<h6>
					<spring:message code="registration.therequesthasbee" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<div class="input-field col s12 center">
						<button class="modal-close waves-effect waves-light btn"
							style="margin-left: 10px;" type="submit" name="add_user"
							id="add_user">
							<spring:message code="modal.cancel" />
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal End -->

	<!-- Modal 2 start -->

	<div id="cancelActivateDeactivate" class="modal">
		<button type="button"
			class=" modal-action modal-close waves-effect waves-green btn-flat right"
			data-dismiss="modal">&times;</button>
		<div class="modal-content">

			<div class="row">
				<h6>
					<spring:message code="registration.cancelRequest" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<div class="input-field col s12 center">
						<a href="index.html" class="btn" type="submit" name="add_user"
							id="add_user"><spring:message code="modal.yes" /></a> <a
							href="#activateDeactivate" class="modal-close modal-trigger btn"
							style="margin-left: 10px;"><spring:message code="modal.no" /></a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal End -->

	<!-- modal start -->
	<div id="changePasswordMessage" class="modal" style="width: 40%;">
		<h6 class="modal-header">
			<spring:message code="registration.changepassword" />
		</h6>
		<div class="modal-content">
			<div class="row">
				<h6 id="cPassSucessMsg"></h6>
			</div>
			<div class="row">
				<div class="center">

					<%
						String userLatestLang = (String) session.getAttribute("updatedLanguage");
					%>
					<%
						if (userLatestLang != null) {
					%>
					<a href="./?lang=<%=userLatestLang%>" class="btn modal-close"><spring:message
							code="modal.ok" /></a>
					<%
						} else {
					%>
					<a href="./?lang=<%=session.getAttribute("language")%>"
						class="btn modal-close"><spring:message code="modal.ok" /></a>
					<%
						}
					%>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal start -->

	<div id="goToHome" class="modal modal-small" style="width: 40%;">
		<!-- <button type="button" class=" modal-action modal-close waves-effect waves-green btn-flat right"
data-dismiss="modal">&times;</button> -->
		<h6 class="modal-header">
			<spring:message code="registration.homepage" />
		</h6>
		<div class="modal-content">
			<div class="row">
				<h6>
					<spring:message code="registration.pagewillredirectpanel" />
				</h6>
			</div>
			<div class="input-field col s12 center">
				<div class="input-field col s12 center">
					<a href="${context}/homePage" class="btn" type="submit"
						name="add_user" id="home_Links"><spring:message
							code="modal.yes" /></a> <a href="#" class="modal-close btn"
						style="margin-left: 10px;"><spring:message code="modal.no" /></a>
				</div>
			</div>
		</div>
	</div>





	<div id="goToLogout" class="modal modal-small" style="width: 40%;">

		<!-- <button type="button" class=" modal-action modal-close waves-effect waves-green btn-flat right"
data-dismiss="modal">&times;</button> -->
		<h6 class="modal-header">
			<spring:message code="logout.page" />
		</h6>
		<div class="modal-content">
			<div class="row">
				<h6>
					<spring:message code="logout.msg" />
				</h6>
			</div>
			
			<div class="input-field col s12 center">
				<div class="input-field col s12 center">
					<%-- <a href="./logout" class="btn" type="submit" name="add_user"
						id="add_user"><spring:message code="modal.yes" /></a> --%>


					<form action="./login?isExpired=yes" method="post" id="logoutForm">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />

						<button type="button" onclick="sessionLogOut(<%=session.getLastAccessedTime()+timeout %>, <%=new Date().getTime()  %>)" class="btn">

							<spring:message code="modal.yes" />
						</button>
						<a href="#" class="modal-close btn" style="margin-left: 10px;"><spring:message
								code="modal.no" /></a>
					</form>

				</div>
			</div>
		</div>
	</div>

	<!-- Modal End -->

	<!-- Modal End -->
	<!-- Modal End -->

	<!-- File Related Modal  -->
	<div id="fileFormateModal" class="modal">
		<h6 class="modal-header">
			<spring:message code="fileValidationModalHeader" />
		</h6>
		<div class="modal-content">
			<div class="row">
				<h6 id="fileErrormessage">
					<spring:message code="fileValidationName" />
					<br> <br>
					<spring:message code="fileValidationFormate" />
					<br>
					<br>
					<spring:message code="fileValidationSize" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<div class="input-field col s12 center">
						<button class="modal-close waves-effect waves-light btn"
							onclick="document.getElementById('mainArea').contentWindow.clearFileName();"
							style="margin-left: 10px;">
							<spring:message code="modal.ok" />
						</button>
					</div>
					
				</div>
			</div>
		</div>
	</div>
	<!-- Modal End -->
	<!-- ================================================
    Scripts
    ================================================ -->
	<!-- 	Error Modal -->
	<div class="modal" id="error_Modal" role="dialog">
		<div class="modal-dialog">
			<div class="row" id="modalMessageBody" style="text-align: center;"></div>

		</div>
	</div>


	<!-- 	password Modal -->
         <div id="passwordModal"  class="modal" style="width: 40%; z-index: 1003;  opacity: 1; transform: scaleX(1); top: 10%;">
             <h6 class="modal-header"><spring:message code="registration.pleaseenteryourpassword" /></h6>
            <div class="modal-content" >
<form  onsubmit="return updateUserStatusModal()">
                    <div class="row">
                        
                           <div class="input-field col s12">

                                <label for="confirmPassword" style="color: #000; font-size: 12px;"><spring:message code="registration.password" /></label>
                                <input required="required"  type="password" class="password" id="confirmPassword" pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,10}$"  maxlength="10"
										oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.password" />');" >
                                	<div class="input-field-addon">
							<i class="fa fa-eye-slash teal-text toggle-password"
								aria-hidden="true"></i>
						</div>
                            </div>
                        
                    </div>
                    <div class="row">
                        <div class="input-field col s12 center">
                            <button type="submit" id="passwordBtn" class="btn"><spring:message code="button.submit" /></button>
                            <button type="button" class="btn modal-close" style="margin-left: 10px;"><spring:message code="button.cancel" /></button>
                        </div>
                    </div>
                    </form>
                </div>
                            </div>
                            
                            	<!-- Modal End -->
<div class="modal" id="error_Modal_reg" role="dialog">
		<div class="modal-dialog">
			<div class="row" id="modalMessageBodyReg"
					style="text-align: center;"></div>
			
		</div>
		
	</div>
               
               
               
               <div id="500ErrorModal" class="modal" style="height: 150px !important;">
		<h6 class="modal-header"><spring:message code="modal.errorContent" /></h6>
		<div class="modal-content">
			<div class="row">
				<div class="row" id="msgDialog" style="text-align: center;color:red;"></div>


				<div class="row input_fields_wrap">
				<div class="row input_fields_wrap">
					<div class="col s12 m12 center" style="margin-top: 10px;">
					<button class="btn modal-close" style="margin-left: 10px;"><spring:message code="modal.close" /></button>
				</div>

	</div></div></div></div></div>
	
                            	<!-- 	password Modal -->
	<!-- jQuery Library -->

	<!--materialize js-->
	<script type="text/javascript"
		src="${context}/resources/js/materialize.js"></script>
	
	<script type="text/javascript"
		src="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	
		<script type="text/javascript"
		src="${context}/resources/js/plugins/data-tables/js/jquery.dataTables.min.js"></script>  

	
	
	<!-- i18n library -->
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
	
	
	
	<!-- Custom js -->

	
	
	<script type="text/javascript"
		src="${context}/resources/project_js/backbutton.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/dragableModal.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/enterKey.js?version=<%= (int) (Math.random() * 10) %>"></script>

	
	<script type="text/javascript"
		src="${context}/resources/ajax/Password.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/ajax/Login.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<%-- 	<script type="text/javascript"
		src="${context}/resources/project_js/disable_inspectElement.js"></script> --%>
	</script>
	<script type="text/javascript"
		src="${context}/resources/ajax/Profile.js?version=<%= (int) (Math.random() * 10) %>"></script>

	<script type="text/javascript"
		src="${context}/resources/project_js/dashboard.js?version=<%= (int) (Math.random() * 10) %>"></script>
	

	
	<script type="text/javascript"
		src="${context}/resources/project_js/globalVariables.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript">

		$(document).ready(function() {
			
			<%if (usertypeId == 13 || usertypeId == 20 || usertypeId == 23) {%>
		//	$("#langlist").val('en');
			$("#langlist").prop("disabled", true);
			$("#divLang").hide();
	<%}%>
		openEditPage(
	<%=usertypeId%>
		)
		});
		
		
	</script>
	<script type="text/javascript">
		/*  function sessionLogOut(){
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers : {
					'X-CSRF-TOKEN' : token
				}
			});
			$.ajax({
				type : 'POST',
				url : './login?isExpired=yes',
				processData : false,
				contentType : false,
			    success : function(data) {
					alert("SUCCESS=="+data);
					}, 
				
				error : function(xhr, ajaxOptions, thrownError) {
					alert("error");
				}
			}); 
		 
	}  */
	 function sessionLogOut(timeOut,currentTime){
		$('#logoutForm').submit();    
		/*  if(currentTime > timeOut){
				$('#logoutForm').submit();
			}
			else{
				 window.location.href = "./login";
			}  */
}		
		</script>
<script type="text/javascript">$( document ).ready(function() {if($("body").attr("data-roleType") == '' || ($("body").attr("data-roleType") != window.parent.$("body").attr("data-roleType"))){window.top.location.href = "./login?isExpired=yes";} var timeoutTime = <%=session.getLastAccessedTime()%>;var timeout = <%=session.getMaxInactiveInterval()%>;timeoutTime += timeout;var currentTime;$("body").click(function(e) {$.ajaxSetup({headers:{ 'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content") }});$.ajax({url: './serverTime',type: 'GET',async: false,success: function (data, textStatus, jqXHR) {currentTime = data;},error: function (jqXHR, textStatus, errorThrown) {}});if( currentTime > timeoutTime ){window.top.location.href = "./login?isExpired=yes";}else{timeoutTime = currentTime + timeout;}});});</script>
<script type="text/javascript"
		src="${context}/resources/ajax/keyBoardShortcut.js?version=<%= (int) (Math.random() * 10) %>"></script>
</body></html>
<%
	} else {
	request.setAttribute("msg", "  *Session has been expired");
	request.getRequestDispatcher("./login.jsp").forward(request, response);
}
%>