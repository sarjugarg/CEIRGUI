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
<html lang="en" class="no-js">
<head><title>CEIR Portal</title>


<!--<title>Grievance</title>-->

<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='-1'>
<meta http-equiv='pragma' content='no-cache'>


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

<script type="text/javascript">
	var path = "${context}";
</script>
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
<%--  <link href="${context}/resources/js/plugins/chartist-js/chartist.min.css" type="text/css" rel="stylesheet" media="screen,projection"> --%>
<link rel="stylesheet"
	href="${context}/resources/project_css/viewConsignment.css">
<link rel="stylesheet"
	href="${context}/resources/project_css/iconStates.css">
<link rel="stylesheet" href="${context}/resources/css/grievance.css">


<link rel="stylesheet"
	href="${context}/resources/custom_js/jquery-ui.css">
<script src="${context}/resources/custom_js/1.12.1_jquery-ui.min.js"></script>


<style type="text/css">
button.modal-action.modal-close.waves-effect.waves-green.btn-flat.right
	{
	height: 36px;
	font-size: 31px
}

.header-fixed-style {
	width: inherit;
	z-index: 1003;
	position: fixed;
}
</style>


</head>

<body data-id="6" data-roleType="${usertype}"
	data-userTypeID="${usertypeId}" data-userID="${userid}"
	data-selected-roleType="${selectedUserTypeId}"
	data-stolenselected-roleType="${stolenselectedUserTypeId}"
	data-grievanceTxnId="${grievanceTxnId}"
	data-grievanceId="${grievanceId}" data-userName="${userName}"
	data-grievanceStatus="${grievanceStatus}"
	session-valueTxnID="${not empty param.txnID ? param.txnID : 'null'}"
	data-session-source="${not empty param.source ? param.source : 'menu'}">


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

								<a href="" class="boton right" id="btnLink"></a>
							</div>
							<form action="${context}/Grievance/grievanceManagement" id="viewFilter"
								method="post">
								<div class="registrationTableDiv_box" id="greivanceTableDiv"
									style="padding-bottom: 5px; background-color: #e2edef52;">
									<div id="filterBtnDiv"></div>
								</div>
							</form>
							<table id="grivanceLibraryTable"
								class="responsive-table striped display"></table>

						</div>

					</div>
				</div>
				<div id="footerBtn"></div>
			</div>
		</div>
		<!--end container-->
	</section>

	<div id="replyModal" class="modal">


		<div class="header-fixed header-fixed-style">
			<button
				class="modal-action modal-close waves-effect waves-green btn-flat right"
				onclick="cleanReplyPopUp()">&times;</button>
			<h6 class="modal-header">
				<spring:message code="input.reply" />
			</h6>


		</div>

		<div class="scrollDivHeight"></div>

		<div class="modal-content modal-content-style">

			<form id="replymessageForm" onsubmit="return saveGrievanceReply()"
				method="POST" enctype="multipart/form-data">
				<div class="row">
					<div class="col s12 m12">
						<h6 style="font-weight: bold;">
							<spring:message code="input.grievID" />
							<span id="grievanceIdToSave"></span>
						</h6>
						<span id="grievanceTxnId" style="display: none;"></span>
						<hr>
					</div>
					<input type="text" id="existingGrievanceID" style="display: none;">
					<div class="col s12 m12" id="viewPreviousMessage">
						<!--  <h6 style="float: left; font-weight: bold;" id="mesageUserType"> </h6>
                    <h6 style="float: left;"></h6>
                        <span style="float:right;"></span> -->
					</div>

					<div class="col s12 m12">
						<label for="replyRemark" style="margin-top: 7px"><spring:message
								code="input.remarks" /><span class="star">*</span></label>
						<textarea id="replyRemark" class="materialize-textarea"
							maxlength="200" style="min-height: 8rem"
							oninput="InvalidMsg(this,'input','<spring:message code="validation.200characters" />');"
							oninvalid="InvalidMsg(this,'input','<spring:message code="validation.200characters" />');"
							required></textarea>

						<input type="text" style="display: none" id="grievanceUserid">
						<!-- <h6 style="color: #000;">Upload Supporting Document </h6> -->

					</div>
					<!--   <div class="file-field col s12 m12">
                    <div class="btn"><span>Select File</span><input id="replyFile" type="file" accept=".csv" ></div>
                    <div class="file-path-wrapper"><input class="file-path validate" type="text"
                            placeholder="">
                        <div>
                            <p id="myFiles"></p>
                        </div>
                    </div>
                </div> -->

					<div id="mainDiv" class="mainDiv">
						<div id="filediv" class="fileDiv">
							<div class="row">

								<div class="col s12 m6 l6" style="margin-top: 8px;">
									<label for="Category"><spring:message
											code="input.documenttype" /></label> <select class="browser-default"
										id="docTypetag1" onchange="enableSelectFile()"
										oninput="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');">
										<option value="" disabled selected><spring:message
												code="select.documenttype" />
										</option>

									</select>

								</div>
								<div class="file-field col s12 m6"  id="removestar">
									<h6 id="supportingdocumentFile" style="color: #000;">
										<spring:message code="input.supportingdocument" />
									</h6>
									<div class="btn">
										<span><spring:message code="input.selectfile" /></span> <input
											type="file" name="files[]" id="docTypeFile1"
											onchange="enableAddMore('docTypeFile1','filediv')" disabled="disabled"
											oninput="InvalidMsg(this,'fileType','<spring:message code="validation.NoChosen" />');"
											oninvalid="InvalidMsg(this,'fileType','<spring:message code="validation.NoChosen" />');">
									</div>
									<div class="file-path-wrapper">
										<input class="file-path validate" type="text" id="filetextField"
											placeholder="<spring:message code="grievanceFileMessage" />">
										<div>
											<p id="myFiles"></p>
										</div>
									</div>
								</div>
							</div>


						</div>

					</div>
					<div class="col s12 m6 right">
						<button class="btn right add_field_button" disabled="disabled" >
							<span style="font-size: 20px;">+</span>
							<spring:message code="input.addmorefile" />
						</button>
					</div>
					<div class="col s12 m12">
						<p>
							<spring:message code="input.requiredfields" />
							<span class="star">*</span>
						</p>
						<p id="closeTicketCheckbox" style="display: none;">
							<label><span><spring:message
										code="modal.message.griev.closeticket" /></span><input
								type="checkbox" id="closeTicketCheck" /></label>
						</p>
						<!-- <a href="./Consignment/sampleFileDownload/filetype=sample">Download Sample Format</a><br> -->
					</div>
					<div class="col s12 m12 center">
						<%-- <p id="closeTicketCheckbox" style="float: left; display: none;">
							<label> <span><spring:message
										code="modal.message.griev.closeticket" /></span> <input
								type="checkbox" id="closeTicketCheck" />
							</label>
						</p> --%>
						<button class="right btn" type="submit" id="grievanceReplyButton">
							<spring:message code="input.reply" />
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div id="replyMsg" class="modal">
		<h6 class="modal-header">
			<spring:message code="modal.header.grievancereply" />
		</h6>
		<div class="modal-content">

			<div class="row">
				<h6 id="showReplyResponse">
					<spring:message code="modal.message.grievance.reply" />
					<span id="replyGrievanceId"> </span>
					<spring:message code="modal.issuccessful" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<div class="input-field col s12 center">
						<a href="./grievanceManagement" class="modal-close btn"><spring:message
								code="modal.ok" /></a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="manageAccount" class="modal">

		<div class="header-fixed header-fixed-style">
			<button
				class="modal-action modal-close waves-effect waves-green btn-flat right"
				data-dismiss="modal">&times;</button>
			<h6 class="modal-header">
				<spring:message code="modal.header.grievancehistory" />
			</h6>
		</div>
		<div class="scrollDivHeight"></div>
		<div class="modal-content modal-content-style">
			<div id="live-chat">
				<div class="chat">
					<div class="chat-history">
						<h6>
							<spring:message code="input.grievID" />
							<span id="viewGrievanceId"></span>
						</h6>
						<div class="chat-message clearfix" id="chatMsg"></div>
						<!-- end chat-message -->


					</div>
				</div>
			</div>
		</div>
	</div>

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
			<input type="text" id='removeFileId' style="display: none;">
			<input type="text" id='removeFileInput' style="display: none">
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<div class="input-field col s12 center">
						<button class=" btn" onclick="clearFileName()"
							style="margin-left: 10px;">
							<spring:message code="modal.ok" />
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="viewuplodedModel" class="modal" style="overflow: hidden">
		<a href="#!" class="modal-close waves-effect waves-green btn-flat">&times;</a>
		<div class="modal-content">
			<div class="row">
				<img src="" id="fileSource" width="400" height="400">
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
		src="${context}/resources/project_js/grievanceManagement.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/dragableModal.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/enterKey.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src=""
		async></script>
	<script type="text/javascript"
		src="${context}/resources/project_js/_dateFunction.js?version=<%= (int) (Math.random() * 10) %>"
		async></script>

	<script type="text/javascript"
		src="${context}/resources/project_js/validationMsg.js?version=<%= (int) (Math.random() * 10) %>"></script>
<script type="text/javascript">$( document ).ready(function() {if($("body").attr("data-roleType") == '' || ($("body").attr("data-roleType") != window.parent.$("body").attr("data-roleType"))){window.top.location.href = "./login?isExpired=yes";} var timeoutTime = <%=session.getLastAccessedTime()%>;var timeout = <%=session.getMaxInactiveInterval()%>;timeoutTime += timeout;var currentTime;$("body").click(function(e) {$.ajaxSetup({headers:{ 'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content") }});$.ajax({url: './serverTime',type: 'GET',async: false,success: function (data, textStatus, jqXHR) {currentTime = data;},error: function (jqXHR, textStatus, errorThrown) {}});if( currentTime > timeoutTime ){window.top.location.href = "./login?isExpired=yes";}else{timeoutTime = currentTime + timeout;}});});</script>
<script type="text/javascript"
		src="${context}/resources/ajax/keyBoardShortcut.js?version=<%= (int) (Math.random() * 10) %>"></script>
</body></html>
<%
	} else {
%>
<script language="JavaScript">
sessionStorage.setItem("loginMsg",
"*Session has been expired");
window.top.location.href = "./login";
</script>
<%
}
%>