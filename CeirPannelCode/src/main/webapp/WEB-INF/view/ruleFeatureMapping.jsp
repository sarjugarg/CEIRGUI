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
<!-- Security Tags -->
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:csrfMetaTags />
<!-- Security Tags -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html class="no-js" lang="en" dir="ltr">
<head>
<title>CEIR Portal</title>
<!--<title>Rule List</title>-->
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='-1'>
<meta http-equiv='pragma' content='no-cache'>
<meta name="fragment" content="!">
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

<!-- Favicons-->
<link rel="icon" href="${context}/resources/images/DMC-Logo.png"
	sizes="32x32">
<!-- CORE CSS-->
<link href="${context}/resources/css/materialize.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link href="${context}/resources/css/style.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/js/plugins/data-tables/css/jquery.dataTables.min.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/font/font-awesome/css/font-awesome.min.css"
	type="text/css" rel="stylesheet" media="screen,projection">

<!-- INCLUDED PLUGIN CSS ON THIS PAGE -->
<link href="${context}/resources/js/plugins/prism/prism.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link rel="stylesheet"
	href="${context}/resources/project_css/viewConsignment.css">
<link rel="stylesheet"
	href="${context}/resources/project_css/iconStates.css">

<link rel="stylesheet"
	href="${context}/resources/custom_js/jquery-ui.css">

<script src="${context}/resources/custom_js/jquery.blockUI.js"></script>

<!------------------------------------------- Dragable Model---------------------------------->
<script src="${context}/resources/custom_js/1.12.1_jquery-ui.min.js"></script>

</head>
<body data-id="30" data-roleType="${usertype}"
	data-userTypeID="${usertypeId}" data-userID="${userid}"
	data-selected-roleType="${selectedUserTypeId}"
	data-stolenselected-roleType="${stolenselectedUserTypeId}"
	data-selected-consignmentTxnId="${consignmentTxnId}"
	data-selected-consignmentStatus="${consignmentStatus}"
	session-value="en"
	session-valueTxnID="${not empty param.txnID ? param.txnID : 'null'}"
	data-username="${username}">

	<!-- START CONTENT -->
	<!-- START CONTENT -->
	<section id="content">
		<div id="initialloader"></div>

		<!--start container-->
		<div class="container">
			<div class="section">
				<div class="row">
					<div class="col s12 m12 l12">
						<div class="row card-panel">
							<div class="container-fluid pageHeader" id="pageHeader">

								<a class="boton right" id="btnLink"></a>
							</div>
							<form action="${context}/ruleListMav" id="viewfilter" method="post">
								<div class="registrationTableDiv_box" id="FieldTableDiv"
									style="padding-bottom: 5px; background-color: #e2edef52;">
									<div id="filterBtnDiv"></div>
								</div>
							</form>
							<table id="table" class="responsive-table striped display"></table>

						</div>

					</div>
				</div>
				<div id="footerBtn"></div>
			</div>
		</div>






		<div id="editModel" class="modal">

			<div class="header-fixed header-fixed-style">
				<h6 class="modal-header">
					<spring:message code="modal.EditRuleFeatureMapping" />
				</h6>
			</div>

			<div class="scrollDivHeight"></div>

			<div class="modal-content modal-content-style">
				<form action="" onsubmit="return update()" method="POST"
					enctype="multipart/form-data" id="register">

					<div class="row myRow">
						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.ruleName" />
								<span class="star">*</span>
							</p>
							<select id="editRule" name="rule" class="browser-default"
								class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');getFeature(this);"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required>
								<option value="null" selected="">Rule Name</option>
							</select>
						</div>


						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.featureName" />
								<span class="star">*</span>
							</p>
							<select id="editFeature" name="organisationcountry"
								class="browser-default" class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');getGrace(this);"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required>
								<option value="null" selected="">Feature Name</option>
							</select>
						</div>
					</div>

					<div class="row myRow">
						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.userType" />
								<span class="star">*</span>
							</p>
							<select id="editUser" name="organisationcountry"
								class="browser-default" class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required></select>
						</div>

						<div class="input-field col s12 m6">
							<input type="text" name="order" id="order"
							pattern="<spring:eval expression="@environment.getProperty('pattern.order')" />"
								maxlength="7" 
								oninput="InvalidMsg(this,'input','<spring:message code="validation.7character" />');"
								oninvalid="InvalidMsg(this,'input','<spring:message code="validation.7character" />');"
								required /> <label for="quantity" class="center-align"><spring:message
									code="table.order" /> <span class="star">*</span></label>
						</div>

					</div>



					<div class="row myRow">
						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.gracePeriod" />
								<span class="star">*</span>
							</p>
							<select id="GracePeriod" name="organisationcountry"
								class="browser-default" class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required></select>
						</div>


						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.postGracePeriod" />
								<span class="star">*</span>
							</p>
							<select id="PostGracePeriod" name="organisationcountry"
								class="browser-default" class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required></select>
						</div>
					</div>



					<div class="row myRow">
						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.moveToGracePeriod" />
								<span class="star">*</span>
							</p>
							<select id="MoveToGracePeriod" name="organisationcountry"
								class="browser-default" class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required></select>
						</div>


						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.moveToPostGracePeriod" />
								<span class="star">*</span>
							</p>
							<select id="MoveToPostGracePeriod" name="organisationcountry"
								class="browser-default" class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required></select>
						</div>
					</div>



					<div class="row myRow">
						<div class=" col s12 m6">
							<p style="margin: 0; font-size: 12px;">
								<spring:message code="table.expectedOutput" />
								<span class="star">*</span>
							</p>
							<select id="editOutput" name="editOutput" class="browser-default"
								class="mySelect"
								onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
								style="padding-left: 0;" required>
								<option value="Yes">Yes</option>
								<option value="No">No</option>
							</select>
						</div>
						
						<div class="input-field col s12 m6" style="margin-top: 16px;">
                        <input type="text" id="editModifiedBy"  disabled>
                        <label for="editModifiedBy" class="center-align">Modified By </label>
                    </div>
					</div>




					<div class="row">
						<div class="input-field col s12 center">

							<button class="btn " type="submit" style="margin-left: 10px;">
								<spring:message code="button.update" />
							</button>
							<button class="modal-close btn" type="button"
								style="margin-left: 10px;">
								<spring:message code="button.cancel" />
							</button>


						</div>

					</div>
				</form>
			</div>
		</div>


		<!--end container-->
	</section>


	<div id="updateFieldsSuccess" class="modal">
		<h6 class="modal-header" style="margin: 0px;">
			<spring:message code="button.update" />
		</h6>
		<div class="modal-content">

			<div class="row">
				<h6 id="updateFieldMessage">
					<spring:message code="input.requestupdated" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<a href="" class="modal-close btn"><spring:message
							code="modal.ok" /></a>
				</div>
			</div>
		</div>
	</div>




	<div id="DeleteFieldModal" class="modal">
		<h6 class="modal-header">
			<spring:message code="modal.header.deleteRuleFeature" />
		</h6>
		<div class="modal-content">
			<div class="row">
				<h6>
					<spring:message code="modal.message.ruleFeature.delete" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<a onclick="deleteModal()" class="modal-close modal-trigger btn"
						type="submit"><spring:message code="modal.yes" /></a>
					<button class="modal-close btn" style="margin-left: 10px;">
						<spring:message code="modal.no" />
					</button>
				</div>
			</div>
		</div>
	</div>



	<div id="closeDeleteModal" class="modal">
		<h6 class="modal-header">
			<spring:message code="modal.message.ruleFeature.delete" />
		</h6>
		<div class="modal-content">


			<div class="row">

				<h6 id="tacModalText">
					<spring:message code="modal.message.ruleFeatureDeleted" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<a href="" class="modal-close btn" style="margin-left: 10px;"><spring:message
							code="modal.ok" /></a>
				</div>
			</div>
		</div>
	</div>



	<div id="viewModel" class="modal">

		<div class="header-fixed header-fixed-style">
			<h6 class="modal-header">
				<spring:message code="modal.ViewRuleFeatureMapping" />
			</h6>
		</div>

		<div class="scrollDivHeight"></div>

		<div class="modal-content modal-content-style">
			<div class="row myRow">


				<div class="input-field col s12 m6">
					<input type="text" name="viewRule" id="viewRule" /> <label
						for="viewRule" class="center-align"><spring:message
							code="table.ruleName" /></label>
				</div>


				<div class="input-field col s12 m6">
					<input type="text" name="viewFeature" id="viewFeature" /> <label
						for="viewFeature" class="center-align"><spring:message
							code="table.featureName" /> </label>
				</div>

			</div>

			<div class="row myRow">


				<div class="input-field col s12 m6">
					<input type="text" name="viewUser" id="viewUser" /> <label
						for="viewUser" class="center-align"><spring:message
							code="table.userType" /> </label>
				</div>



				<div class="input-field col s12 m6">
					<input type="text" name="vieworder" id="vieworder" /> <label
						for="vieworder" class="center-align"><spring:message
							code="table.order" /> </label>
				</div>

			</div>



			<div class="row myRow">

				<div class="input-field col s12 m6">
					<input type="text" name="viewGracePeriod" id="viewGracePeriod" />
					<label for="viewGracePeriod" class="center-align"><spring:message
							code="table.gracePeriod" /> </label>
				</div>


				<div class="input-field col s12 m6">
					<input type="text" name="viewPostGracePeriod"
						id="viewPostGracePeriod" /> <label for="viewPostGracePeriod"
						class="center-align"><spring:message
							code="table.postGracePeriod" /> </label>
				</div>

			</div>



			<div class="row myRow">
				<div class="input-field col s12 m6">
					<input type="text" name="viewMoveToGracePeriod"
						id="viewMoveToGracePeriod" /> <label for="viewMoveToGracePeriod"
						class="center-align"><spring:message
							code="table.moveToGracePeriod" /> </label>
				</div>

				<div class="input-field col s12 m6">
					<input type="text" name="viewMoveToPostGracePeriod"
						id="viewMoveToPostGracePeriod" /> <label
						for="viewMoveToPostGracePeriod" class="center-align"><spring:message
							code="table.moveToPostGracePeriod" /> </label>
				</div>

			</div>



			<div class="row myRow">

				<div class="input-field col s12 m6">
					<input type="text" name="viewOutput" id="viewOutput" /> <label
						for="viewOutput" class="center-align"><spring:message
							code="table.expectedOutput" /> </label>
				</div>
				
				<div class="input-field col s12 m6" style="margin-top: 22px;">
                        <input type="text" id="viewModifiedBy"  disabled>
                        <label for="viewModifiedBy" class="center-align">Modified By </label>
                    </div>
			</div>




			<div class="row">
				<div class="input-field col s12 center">

					<button class="btn modal-close" style="margin-left: 10px;">
						<spring:message code="modal.close" />
					</button>

				</div>

			</div>
		</div>
	</div>

	<!--materialize js-->
	<script type="text/javascript"
		src="${context}/resources/js/materialize.js"></script>
	<script src="${context}/resources/custom_js/bootstrap.min.js"></script>

	<script type="text/javascript"
		src="${context}/resources/js/plugins/data-tables/js/jquery.dataTables.min.js"></script>



	<script type="text/javascript" src="${context}/resources/js/plugins.js"></script>

	<script type="text/javascript"
		src="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	<script type="text/javascript"
		src="${context}/resources/js/countries.js"></script>
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
	<script type="text/javascript"
		src="${context}/resources/project_js/globalVariables.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/backbutton.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/dragableModal.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/enterKey.js?version=<%= (int) (Math.random() * 10) %>"></script>

	<script type="text/javascript"
		src="${context}/resources/project_js/validationMsg.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/_dateFunction.js?version=<%= (int) (Math.random() * 10) %>"
		async></script>
	<script type="text/javascript" src="" async></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/ruleFeatureMapping.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript">
		$(document)
				.ready(
						function() {
							if ($("body").attr("data-roleType") == '' || ($("body").attr("data-roleType") != window.parent.$("body").attr("data-roleType"))) {
								window.top.location.href = "./login?isExpired=yes";
							}
							var timeoutTime =
	<%=session.getLastAccessedTime()%>
		;
							var timeout =
	<%=session.getMaxInactiveInterval()%>
		;
							timeoutTime += timeout;
							var currentTime;
							$("body")
									.click(
											function(e) {
												$
														.ajaxSetup({
															headers : {
																'X-CSRF-TOKEN' : $(
																		"meta[name='_csrf']")
																		.attr(
																				"content")
															}
														});
												$.ajax({
													url : './serverTime',
													type : 'GET',
													async : false,
													success : function(data,
															textStatus, jqXHR) {
														currentTime = data;
													},
													error : function(jqXHR,
															textStatus,
															errorThrown) {
													}
												});
												if (currentTime > timeoutTime) {
													window.top.location.href = "./login?isExpired=yes";
												} else {
													timeoutTime = currentTime
															+ timeout;
												}
											});
						});
	</script>
	<script type="text/javascript"
		src="${context}/resources/ajax/keyBoardShortcut.js?version=<%= (int) (Math.random() * 10) %>"></script>
</body>
</html>
<%
	} else {
	/*  request.setAttribute("msg", "  *Please login first");
	request.getRequestDispatcher("./index.jsp").forward(request, response); */
%>
<script language="JavaScript">
	sessionStorage.setItem("loginMsg", "*Session has been expired");
	window.top.location.href = "./login";
</script>
<%
	}
%>


