<%
Integer statusCode=(Integer)session.getAttribute("statusCode");
%>
<%
if(statusCode==200){
%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Security Tags -->
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page import="java.io.InputStream" %>
<%@page import="java.util.Properties" %>

<sec:csrfMetaTags />
<!-- Security Tags -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
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
	<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='expires' content='-1'>
<meta http-equiv='pragma' content='no-cache'>
<!-- Security Tags -->
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<!-- Security Tags -->

<!--<title>CEIR | Importer Portal</title>-->

<link rel="apple-touch-icon-precomposed"
	href="${context}/resources/images/favicon/apple-touch-icon-152x152.png">
<!-- For iPhone -->
<meta name="msapplication-TileColor" content="#00bcd4">
<!-- For Windows Phone -->
<link rel="stylesheet"
	href="${context}/resources/font/font-awesome/css/font-awesome.min.css">
<!-- CORE CSS-->
<!-- Favicons-->
<link rel="icon" href="${context}/resources/images/DMC-Logo.png" sizes="32x32">


<link rel="stylesheet"
	href="${context}/resources/custom_js/materialize.min.css">
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
	<link href="${context}/resources/project_css/leanOverlay.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<!-- Country -->

<style>
input[type="checkbox"] {
	display: none;
}

footer {
	padding-left: 0;
}

.btn-flat {
	height: auto;
}

.star {
	color: red;
}

.dropdown-content li>a, .dropdown-content li>span {
	color: #444;
}

.input-field>label {
	color: #444 !important;
}

[type="radio"]:not (:checked ), [type="radio"]:checked {
	opacity: 0;
}

input[type=text], input[type=password], input[type=email], input[type=url],
	input[type=time], input[type=date], input[type=datetime-local], input[type=tel],
	input[type=number], input[type=search], textarea.materialize-textarea {
	background-color: transparent !important;
	border: none !important;
	border-bottom: 1px solid #9e9e9e !important;
	border-radius: 0 !important;
	outline: none !important;
	height: 2.6rem !important;
	width: 100% !important;
	font-size: 1rem !important;
	margin: 0 0 5px 0 !important;
	padding: 0 !important;
	box-shadow: none !important;
	-webkit-box-sizing: content-box !important;
	-moz-box-sizing: content-box !important;
	box-sizing: content-box !important;
	transition: all .3s !important;
}

input
[
type
=
text
]
:focus
:not
 
(
[
readonly
]
 
)
{
border-bottom:1px
 
1
px
 
solid
 
#ff4081
 
!
important
;

	
box-shadow
:
 
0
1
px
 
0
0
#ff4081
 
!
important
;


}
input[type=text]:focus:not ([readonly] )+label {
	color: #ff4081 !important;
	background-color: transparent !important;
}

.input-field {
	position: relative;
	margin-top: 1rem;
	margin-bottom: 0;
}

.row {
	margin-left: auto;
	margin-right: auto;
	margin-bottom: 0;
}

.btn {
	background-color: #ff4081 !important;
}

select {
	background-color: transparent;
	border: none;
	border-bottom: 1px solid #9e9e9e;
	padding: 0;
	margin-top: 0;
	height: 2.6rem;
}

[type="checkbox"]:not (:checked ), [type="checkbox"]:checked {
	position: inherit;
	opacity: 1;
	pointer-events: none;
}

[type="checkbox"]+span:not (.lever ):before, [type="checkbox"]:not (.filled-in
	 )+span:not (.lever ):after {
	display: none;
}

input[type="checkbox"] {
	display: block;
}

[type="checkbox"]:not (:checked ), [type="checkbox"]:checked {
	float: left;
	margin-top: 5px;
}
.fa-eye-slash, .fa-eye {
	position: absolute;
	right: 10px;
	top: 10px;
}

.upload-file-label {
	margin: 0;
	
}

.file-field .btn {
	line-height: 2.4rem;
	height: 2.4rem;
}
.section .registration-form {
padding-top: 1rem;
padding-bottom: 1rem;
width: 90%;
margin: auto;
/* border: solid 2px #444; */
}


</style>
<script>
var contextpath = "${context}";
</script>
</head>

<body data-lang-param="${pageContext.response.locale}">
	<%String name=request.getParameter("type");%>

	<!--  Scripts
    ================================================ -->
	<!-- jQuery Library -->
	<script type="text/javascript"
		src="${context}/resources/js/plugins/jquery-1.11.2.min.js"></script>
	<!-- ajax js -->
	<script type="text/javascript"
		src="${context}/resources/ajax/Registration.js"></script>
	<script type="text/javascript"
		src="${context}/resources/ajax/Profile.js"></script>

	<script type="text/javascript"
		src="${context}/resources/js/materialize.js"></script>
	<script type="text/javascript" src="${context}/resources/js/country.js"></script>
	<!--prism
    <script type="text/javascript" src="js/prism/prism.js"></script>-->
	<!--scrollbar-->
	<script type="text/javascript"
		src="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	

	<script type="text/javascript"
		src="${context}/resources/ajax/Password.js?version=<%= (int) (Math.random() * 10) %>"></script>
	

	<!-- //////////////////////////////////////////////////////////////////////////// -->

	<!-- START CONTENT -->
	<section id="content" id="mainPage">
		<!--start container-->
		<div class="container">
			<div class="section">
				<form id="registrationForm" autocomplete="off"
					onsubmit="return saveOperatorRegistration()">
	<div class="row card-panel registration-form">
									<h5 class="fixPage-modal-header-reg ">
									<spring:message code="operator.reg" />
													<div class="col s10 m11 select-lang-lable-reg">
								
								<i class="fa fa-globe fa-6" aria-hidden="true"></i>
							</div>
							<div class="col s2 m1 right" style="padding: 0;">
								<select class="browser-default select-lang-drpdwn-reg" id="langlist">
									<option value="en" class="fontBlack">English</option>
									<option value="km" class="fontBlack"><spring:message code="lang.khmer" /></option>
								</select>
							</div> 
								</h5>
								
								
						<div class="row">
						
							<div class="col s12 m12">
							
								<span id="msg" style="color: red;">${msg}</span>
						
							</div>

							<input type="hidden" id="usertypeId" value="${usertypeId}">
							<input type="hidden" id="usertypeName" value="<%=name%>">
							<input type="hidden" id="type" value="1">
							<div class="row">
								<div class="input-field col s12 m4 l4">
									<input type="text" name="firstName" id="firstName" pattern="<spring:eval expression="@environment.getProperty('pattern.name')" />"  maxlength="20"
										oninput="InvalidMsg(this,'input','<spring:message code="validation.20Character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.20Character" />');" 
										title="" required />
									<label for="firstName" class="center-align"><spring:message code="input.firstName" /> <span class="star">*</span> </label>
								</div>

								<div class="input-field col s12 m4 l4">
									<input type="text" name="middleName" id="middleName" pattern="<spring:eval expression="@environment.getProperty('pattern.name')" />" maxlength="20"
										oninput="InvalidMsg(this,'input','<spring:message code="validation.20Character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.20Character" />');" 
										title="<spring:message code="validation.20Character" />" />
									<label for="middleName"><spring:message code="input.middleName" /></label>
								</div>


								<div class="input-field col s12 m4 l4">
									<input type="text" name="lastName" id="lastName" pattern="<spring:eval expression="@environment.getProperty('pattern.name')" />" maxlength="20"
										oninput="InvalidMsg(this,'input','<spring:message code="validation.20Character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.20Character" />');" 
										title=""  />
									<label for="lastName"><spring:message code="input.lastName" /></label>
								</div>
								<div class="col s12 m6 l6">
									<label><spring:message code="input.OperatorType" /> <span class="star">*</span></label> 
									<select class="browser-default" id="operatorType" title="" 
										 oninput="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" required>
										<option value="" disabled selected><spring:message code="select.select" /></option>
									
									</select>
								</div>
							</div>

												<div class="row">
								<div class="input-field col s12 m12 l12">
									<input type="text" maxlength="200"  pattern="<spring:eval expression="@environment.getProperty('pattern.address')" />"  name="propertyLocation" id="propertyLocation"
										oninput="InvalidMsg(this,'input','<spring:message code="validation.address20characters" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.address20characters" />');" title="" required /> 
										 <label for="propertyLocation"><spring:message code="input.address" /> <span class="star">*</span></label>
								</div>

		<div class="col s12 m6 l6">
									<label><spring:message code="table.country" /> <span class="star">*</span></label>
									<select id="country" class="browser-default" class="mySelect" disabled
									oninput="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" style="padding-left: 0;" required></select>
								</div>

								<div class="col s12 m6 l6">
									<label><spring:message code="input.province" /> <span
										class="star">*</span></label> <select id="state"
										class="browser-default" class="mySelect"
										onchange="getDistrict(this);InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required></select>
								</div>
								
								
								<div class="col s12 m6 l6">
									<label><spring:message code="input.district" /><span
										class="star">*</span></label> <select
										id="district" class="browser-default" class="mySelect"
										onchange="getCommune(this);InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required>
										<option value=""><spring:message code="select.district" /></option></select>
								</div>


								<div class="col s12 m6 l6">
									<label><spring:message code="input.commune" /><span
										class="star">*</span></label> <select
										id="commune" class="browser-default" class="mySelect"
										onchange="getVillage(this);InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required>
										<option value=""><spring:message code="select.commune" /></option></select></select>
								</div>
								<div class="col s12 m6 l6">
									<label><spring:message code="input.village" /></label> <select
										id="village" class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" >
										<option value=""><spring:message code="select.village" /></option></select>


								</div>
								
								
								<div class="input-field col s12 m6 l6">
									<input type="text" name="street" maxlength="20" id="street" pattern="<spring:eval expression="@environment.getProperty('pattern.street')" />"
									oninput="InvalidMsg(this,'input','<spring:message code="validation.streetNumber" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.streetNumber" />');" title="" required />
									<label for="street"><spring:message code="input.streetNumber" /> <span class="star">*</span> </label>
								</div>
							
							</div>

<div class="row">

								<div class="input-field col s12 m6 l6">
									<input type="text" name="locality" maxlength="30" id="locality" pattern="<spring:eval expression="@environment.getProperty('pattern.locality')" />"
									oninput="InvalidMsg(this,'input','<spring:message code="validation.address30characters" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.address30characters" />');" title="">
									<label for="locality"><spring:message code="input.locality" /> </label>
								</div>

								
								<div class="input-field col s12 m6 l6">
									<input type="text"  name="postalCode" maxlength="6" id="postalCode" pattern="<spring:eval expression="@environment.getProperty('pattern.postal')" />"
									oninput="InvalidMsg(this,'input','<spring:message code="validation.postalcode" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.postalcode" />');" title="">
									<label for="postalCode"><spring:message code="input.postalCode" /></label>
								</div>
								</div>
							<div class="row">

								<div class="input-field col s12 m6 l6">
									<input type="text" name="passportNo" required="required" id="passportNo" maxlength="13" pattern="<spring:eval expression="@environment.getProperty('pattern.operatorNid')" />" 
									 oninput="InvalidMsg(this,'input','<spring:message code="validation.14Character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.14Character" />');" title="" required/>
									<label for="passportNo"><spring:message code="registration.nationalid" /> <span class="star">*</span></label>
								</div>

								<div class="file-field col s12 m6 l6">
								<h6 class="file-label"><spring:message code="registration.uploadnationalid" /> <span class="star">*</span></h6>
									<div class="btn">
										<span><spring:message code="registration.uploadnationalid" /></span>
										<input onchange="isImageValid('NationalIdImage')" type="file" id="NationalIdImage" placeholder="Upload National ID Image"
										oninput="InvalidMsg(this,'fileType','<spring:message code="validation.file" />');" oninvalid="InvalidMsg(this,'fileType','<spring:message code="validation.file" />');" title="" required />
									</div>
									<div class="file-path-wrapper">
										<input class="file-path validate" id="NIdImageText" type="text" placeholder="">
									</div>
								</div>

								<div class="file-field col s12 m6 l6">
								<h6 class="file-label"><spring:message code="registration.uploadphoto" /> <span class="star">*</span></h6>
									<div class="btn">
										<span><spring:message code="registration.uploadphoto" /></span>
										<input id="photo" type="file" onchange="isImageValid('photo')" placeholder="" oninput="InvalidMsg(this,'fileType','<spring:message code="validation.file" />');" oninvalid="InvalidMsg(this,'fileType','<spring:message code="validation.file" />');"
										 title="" required />
									</div>
									<div class="file-path-wrapper">
										<input id="photoTxt" class="file-path validate" type="text" placeholder="">
									</div>
								</div>

								<div class="input-field col s12 m6 l6" style="margin-top: 22px;">
									<input type="text" name="employeeID"  id="employeeId" maxlength="30"  pattern="<spring:eval expression="@environment.getProperty('pattern.employeeID')" />"    
									oninput="InvalidMsg(this,'input','<spring:message code="validation.address30characterswithoutSpecial" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.address30characterswithoutSpecial" />');" title="" required> 
									<label for="employeeId"><spring:message code="registration.employeeid" /> <span class="star">*</span></label>
								</div>


							</div>
<div class="row">
								<div class="file-field col s12 m6 l6">
								<h6 class="file-label"><spring:message code="operator.uploadidcard" /> <span class="star">*</span></h6>
									<div class="btn">
										<span><spring:message code="operator.uploadidcard" /></span>
										<input onchange="isImageValid('idCard')" id="idCard" type="file" placeholder="" oninput="InvalidMsg(this,'fileType','<spring:message code="validation.file" />');" oninvalid="InvalidMsg(this,'fileType','<spring:message code="validation.file" />');"
										 title="" required />
									</div>
									<div class="file-path-wrapper">
										<input id="idCardTxt" class="file-path validate" type="text" placeholder="">
									</div>
								</div>

								<div class="col s12 m6 l6" style="margin-top: 8px;">
									<label><spring:message code="registration.natureofemployment" /> <span class="star">*</span></label>
									<select id="natureOfEmployment" class="browser-default" title="" 
										 oninput="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" required>
										<option value="" disabled selected><spring:message code="registration.natureofemployment" /></option>
									<!-- 	<option value="Permanent">Permanent</option>
										<option value="Temporary">Temporary</option>
										<option value="Contract">Contract</option> -->
									</select>
								</div>
								</div>
							<div class="row">

								<div class="input-field col s12 m6 l6">
									<input type="text" name="designation" required="required" id="designation" maxlength="30" 
								pattern="<spring:eval expression="@environment.getProperty('pattern.designation/authorityName')" />" oninput="InvalidMsg(this,'input','<spring:message code="validation.30alphab" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.30alphab" />');" title="" required /> 
									<label for="designation"><spring:message code="registration.designationandtitle" /> <span class="star">*</span></label>
								</div>

								<div class="input-field col s12 m6 l6">
									<input type="text" name="reportingAuthorityName" id="authorityName" maxlength="30"  pattern="<spring:eval expression="@environment.getProperty('pattern.designation/authorityName')" />" 
									oninput="InvalidMsg(this,'input','<spring:message code="validation.30alphab" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.30alphab" />');" title=""> 
									<label for="authorityName"><spring:message code="registration.reportingauthorityname" /></label>
								</div>

								<div class="input-field col s12 m6 l6">
									<input type="text" name="authorityEmail" maxlength="280" id="authorityEmail" pattern="<spring:eval expression="@environment.getProperty('pattern.mail')" />" 
									oninput="InvalidMsg(this,'email','<spring:message code="validation.email" />');" oninvalid="InvalidMsg(this,'email','<spring:message code="validation.email" />');" title=""> 
										<label for="authorityEmail"><spring:message code="registration.ReportingAuthorityEmailid" /></label>
								</div>
								<div class="input-field col s12 m6 l6">
									<input type="tel" name="authorityPhoneNo"  id="authorityPhoneNo" maxlength="10" pattern="<spring:eval expression="@environment.getProperty('pattern.contactNo')" />" 
										oninput="InvalidMsg(this,'input','<spring:message code="validation.contact" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.contact" />');" title="">
									<label for="authorityPhoneNo"><spring:message code="registration.reportingauthoritycontactnumber" /></label>
								</div>
								<div class="input-field col s12 m6 l6">
									<input type="text" name="email" required="required" id="email" maxlength="280"  pattern="<spring:eval expression="@environment.getProperty('pattern.mail')" />"
									oninput="InvalidMsg(this,'email','<spring:message code="validation.email" />');" oninvalid="InvalidMsg(this,'email','<spring:message code="validation.email" />');" title="" required/> 
										<label for="email"><spring:message code="input.email" /> <span class="star">*</span></label>
								</div>

								<div class="input-field col s12 m6 l6">
									<input type="tel" name="phoneNo" maxlength="15" id="phoneNo" pattern="<spring:eval expression="@environment.getProperty('pattern.contactNo')" />" 
									oninput="InvalidMsg(this,'input','<spring:message code="validation.contact" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.contact" />');" title="" required /> 
										<label for="phoneNo"><spring:message code="input.contactNum" /> <span class="star">*</span></label>
								</div>


							</div>





							<div class="row">
								<div class="input-field col s12 m6 l6">
									<input type="password" name="password" class="password" id="password" maxlength="250"  pattern="<spring:eval expression="@environment.getProperty('pattern.password')" />"  min="8"  oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.password" />');" title=""
										required /> 
										<label for="password"><spring:message code="registration.password" /> <span class="star">*</span> </label>
									<div class="input-field-addon">
						<i class="fa fa-eye-slash teal-text toggle-password" aria-hidden="true"></i>
									</div>
								</div>

								<div class="input-field col s12 m6 l6">
									<input type="password" name="rePassword" class="password2" maxlength="250"  id="confirm_password" pattern="<spring:eval expression="@environment.getProperty('pattern.password')" />"  min="8" oninput="InvalidMsg(this,'input','<spring:message code="validation.password" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.password" />');" title="" required /> 
										<label for="confirm_password"><spring:message code="registration.retypepassword" /> <span class="star">*</span> </label>
									<div class="input-field-addon">
			<i class="fa fa-eye-slash teal-text toggle-password2" aria-hidden="true"></i>
									</div>
								</div>
							</div>


							<div class="row securityQuestionDiv">
								<div class="col s12 m6 l6">
									<label><spring:message code="registration.securityQuestion1" /><span class="star">*</span></label>
									<input type="hidden" class="id" id="id0"> <select class="browser-default questionId" id="questionId0" name="questionId"
									title="" 
										 oninput="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" required>
										<option value="" disabled selected><spring:message code="registration.securityQuestion1" /></option>
									
									</select>
								</div>

								<div class="input-field col s12 m6 l6">
									<input type="text" name="answer" class="answer" id="answer0" pattern="<spring:eval expression="@environment.getProperty('pattern.securityAnswer')" />" 
										maxlength="50" oninput="InvalidMsg(this,'input','<spring:message code="validation.50alphanumeric" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50alphanumeric" />');" title="" required/>
									<label><spring:message code="registration.answer1" /> <span class="star">*</span> </label>
								</div>
							</div>
							<div class="row securityQuestionDiv">
								<div class="col s12 m6 l6">
									<label><spring:message code="registration.securityQuestion2" /><span class="star">*</span></label>
									<input type="hidden" class="id" id="id1" /> <select name="questionId" id="questionId1" class="browser-default questionId"
									title="" 
										 oninput="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" required>
										<option value="" disabled selected><spring:message code="registration.securityQuestion2" /></option>
										<%--<c:forEach items="${questions}" var="question"> 		
										<c:if test="${question.category=='2'}">
									<option value="${question.id}">${question.question}</option>
									</c:if>
									</c:forEach> --%>
									</select>
								</div>

								<div class="input-field col s12 m6 l6">
									<input type="text" name="answer" class="answer" id="answer1" pattern="<spring:eval expression="@environment.getProperty('pattern.securityAnswer')" />"  maxlength="50"
										oninput="InvalidMsg(this,'input','<spring:message code="validation.50alphanumeric" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50alphanumeric" />');" title="" required/> 
										<label><spring:message code="registration.answer2" /> <span class="star">*</span> </label>
								</div>
							</div>

							<div class="row securityQuestionDiv">
								<div class="col s12 m6 l6">
									<label><spring:message code="registration.securityQuestion3" /><span class="star">*</span></label>
									<input type="hidden" class="id" id="id2" /> <select name="questionId" id="questionId2" class="browser-default questionId"
									title="" 
										 oninput="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');" required>
										<option value="" disabled selected><spring:message code="registration.securityQuestion3" /></option>
									</select>
								</div>

								<div class="input-field col s12 m6 l6">
									<input type="text" name="answer" class="answer" id="answer2" oninput="InvalidMsg(this,'input','<spring:message code="validation.50alphanumeric" />');" 
									oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50alphanumeric" />');" title="" 
										maxlength="50" pattern="<spring:eval expression="@environment.getProperty('pattern.securityAnswer')" />"  required="required"> 
										<label><spring:message code="registration.answer3" /> <span class="star">*</span> </label>
								</div>
							</div>

							<div class="form-group form-actions">
								<span class="input-icon"> <img id="captchaImage" src="${context}/captcha">
									<button style="background: none; border: none; outline: none;" type="button" onclick="refreshCaptcha('captchaImage')">
										<i class="fa fa-refresh"></i> </button> <%-- <img src="${context}/captcha"" id="captchaImage">
						 <br>
                           <input type="button" onclick="refreshCaptcha('captchaImage')"> --%>
									<div class="input-field col s12 m6 l12">
										<input autocomplete="off" type="text" name="captcha" id="captcha"
										oninput="InvalidMsg(this,'input','<spring:message code="validation.captcha" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.captcha" />');" title="" required/> 
										<label for="captcha"> <spring:message code="registration.enteryourcaptcha" /> <span class="star">*</span> </label>
									</div>

								</span>
							</div>

							<p>
								<label style="color: black !important;"> <input name="disclamer" id="disclamer" type="checkbox" 
								oninput="InvalidMsg(this,'checkbox','<spring:message code="validation.checkbox" />');" oninvalid="InvalidMsg(this,'checkbox','<spring:message code="validation.checkbox" />');" 
								required /> <span>
										<span class="star">*</span> <spring:message code="registration.certifyMsg" /> </span>
								</label>
							</p>
						</div>

						<div class="row">
							<span><spring:message code="input.requiredfields" /> <span class="star">*</span></span>
							<div class="input-field col s12 center">
								<button disabled="disabled" class="btn" id="btnSave" type="submit" style="margin-left: 10px;"> <spring:message code="button.submit" /> </button>
								<a href="${context}/DMC" class="btn" style="margin-left: 10px;"><spring:message code="button.cancel" /></a>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--end container-->
	</section>
	<!-- END CONTENT -->



	<!-- //////////////////////////////////////////////////////////////////////////// -->



	<!-- Modal 1 start   -->


	<!-- Modal End -->

	<!-- Modal 2 start   -->

	<div id="submitActivateDeactivate" class="modal">
		<button type="button"
			class=" modal-action modal-close waves-effect waves-green btn-flat right"
			data-dismiss="modal">&times;</button>
		<div class="modal-content">

			<div class="row">
				<h6>
					<spring:message code="operator.request" />
				</h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<div class="input-field col s12 center">
						<button class="modal-close waves-effect waves-light btn"
							style="margin-left: 10px;" type="submit" name="add_user"
							id="add_user">
							<spring:message code="button.cancel" />
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal End -->

	<!-- Modal 2 start   -->

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
						<a href="userRegistration.html" class="btn" type="submit"
							name="add_user" id="add_user"><spring:message
								code="modal.yes" /></a> <a href="#activateDeactivate"
							class="modal-close modal-trigger btn" style="margin-left: 10px;"><spring:message
								code="modal.no" /></a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- START MAIN -->
<div id="otpMessage" class="modal">
		<h6 class="modal-header" style="font-size: 1.15rem;">
			<spring:message code="registration.verifyotp" />
		</h6>
		<div class="modal-content">
			<h6 id="otpResponse" style="    font-size: 1.15rem;line-height: 110%;margin: .7666666667rem 0 .46rem 0;"></h6>
			<div class="row">
				<div class="input-field col s12 center">
					<a href="${context}/login" class="btn"><spring:message
							code="modal.ok" /></a>
				</div>
			</div>
		</div>
	</div>
	

<div id="otpMsgModal" class="modal"
		style="width: 40%; margin-left: 30%; margin-top: 10vh;">
		<h6 class="modal-header">
			<spring:message code="registration.verifyotp" />
		</h6>
		<div class="modal-content">
			<!-- <h4 class="header2 pb-2">User Info</h4> -->

			<p style="padding: 10px;" class="center" id="otpMsg"></p>

			<a href="javascript:void(0)" onclick="openOtpPopup()" class="btn"
				style="width: 100%; margin-top: 20px; margin-bottom: 20px;"><spring:message
					code="registration.verifyotp" /></a>
		</div>
	</div>




	<!-- modal start -->

	<div id="otpVerification" class="modal" style="width: 40%;">
		<!-- <button type="button" class=" modal-action modal-close waves-effect waves-green btn-flat right"
            data-dismiss="modal">&times;</button> -->
		<h6 class="modal-header"><spring:message code="registration.otp" /></h6>
		<div class="modal-content">
			<form id="verifyOtpForm" onsubmit="return verifyOtp()">
				<p class="center" id="verifyOtpResp"></p>
				<input type="hidden" id="userid" name="userid" value="${userId}">
				<div class="row">
					<div class="input-field col s12 m12">
						<input type="text" placeholder="<spring:message code="placeholder.emailotp" />"name="emailOtp" maxlength="6" id="emailOtp"
						pattern="<spring:eval expression="@environment.getProperty('pattern.otp')" />"  oninput="InvalidMsg(this,'input','<spring:message code="validation.6Character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.6Character" />');" 
							title="" required />
					</div>
					<div class="input-field col s12 m12">
						<input placeholder="<spring:message code="placeholder.optphone" />" type="text"name="phoneOtp" maxlength="6" pattern="<spring:eval expression="@environment.getProperty('pattern.otp')" />" 
						 id="phoneOtp" oninput="InvalidMsg(this,'input','<spring:message code="validation.6Character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.6Character" />');" 
							title="" required />
					</div>
				</div>
				<a href="javascript:void(0)"
					onclick="resendOtp();"
					class="right"><spring:message code="registration.resendotp" /></a>
				<button type="submit" id="otpVerifyBtn" class="btn"
					style="width: 100%; margin-top: 20px; margin-bottom: 20px;">
					<spring:message code="registration.done" />
				</button>
			</form>
		</div>
	</div>

	<!-- Modal End -->
	<!-- Modal End -->

		<div class="modal" id="error_Modal_reg" role="dialog">
		<div class="modal-dialog">
			<div class="row" id="modalMessageBodyReg"
					style="text-align: center;"></div>
			
		</div>
	</div>
	
			<div id="fileFormateModal" class="modal">
		<h6 class="modal-header"><spring:message code="fileValidationModalHeader" /></h6>
		<div class="modal-content">
			<div class="row">
				<h6 id="fileErrormessage"><spring:message code="fileValidationName" /><br> <br> <spring:message code="fileValidationFormate" /> <br><br> <spring:message code="fileValidationSize" /> </h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<div class="input-field col s12 center">
					<input type="hidden" id="FilefieldId">
						<button class="modal-close waves-effect waves-light btn" onclick="clearFilesName('FilefieldId')"
							style="margin-left: 10px;"><spring:message code="modal.ok" /></button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- ================================================
    Scripts
    ================================================ -->
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
    
    		<script type="text/javascript" src="${context}/resources/project_js/globalVariables.js?version=<%= (int) (Math.random() * 10) %>"></script>
<script type="text/javascript"
		src="${context}/resources/project_js/backbutton.js?version=<%= (int) (Math.random() * 10) %>"></script>
	
			<script type="text/javascript"
		src="${context}/resources/project_js/provinceDropdown.js?version=<%= (int) (Math.random() * 10) %>"></script>
    
<script type="text/javascript"
		src="${context}/resources/project_js/ValidationFileOutsidePortal.js?version=<%= (int) (Math.random() * 10) %>"></script>
		
		<script>
		</script>
	
	
	<!-- Modal End -->
	<script type="text/javascript">
	$('#langlist').on('change', function() {
		window.lang=$('#langlist').val() == 'km' ? 'km' : 'en';
		var url_string = window.location.href;
		var url = new URL(url_string);
		var type = url.searchParams.get("type");
		window.location.assign("registration?type="+type+"&lang="+window.lang);			
		}); 
		
		//var langParam=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';


		
		
	        $(document).ready(function () {
	        	 checkBoxClick();
	        	$('#langlist').val(data_lang_param);
	        	$.i18n().locale = data_lang_param;	
	            			
	            			$.i18n().load( {
	            				'en': './resources/i18n/en.json',
	            				'km': './resources/i18n/km.json'
	            			} ).done( function() { 
	            			});
	        
            questionDataByCategory();
            //operatorList();
            systemConfigList('operatorType','OPERATORS');
            systemConfigList('natureOfEmployment','Nature_Of_Employment');
            
            $('.dropdown-trigger').dropdown();

            /*  $(document).ready(function () {
                 $('select').material_select();
             }); */
             var password = document.getElementById("password")
             , confirm_password = document.getElementById("confirm_password");

           function validatePassword(){
             if(password.value != confirm_password.value) {
             	 confirm_password.setCustomValidity("<spring:message code='registration.passnotmatch' />");
             } else {
               confirm_password.setCustomValidity('');
             }
           }

           password.onchange = validatePassword;
           confirm_password.onkeyup = validatePassword;
        
           populateCountries( "country",    "state");
           
           $("#country").val("Cambodia");
           setDropdown();
        //   populateStates( "country","state" );

          
        });   

	         function clearFilesName(id)
	        {
	       		var fieldId=$('#'+id).val();
	       		 if(fieldId=='NationalIdImage')
    		    	{
    		    	$('#'+fieldId).val('');
        		    $("#NIdImageText").val('');
    		    	}
    		    else if(fieldId=='photo')
    		    {
    		    	$('#'+fieldId).val('');
        		    $("#photoTxt").val('');
    		    	
    		    }
    		    else if(fieldId=='idCard')
    		    {
    		    	$('#'+fieldId).val('');
        		    $("#idCardTxt").val('');	
    		    }
    		    else{}
	        }
    </script>
    <script type="text/javascript"
		src="${context}/resources/ajax/keyBoardShortcut.js?version=<%= (int) (Math.random() * 10) %>"></script>
</body></html>
<%}
else{
%>
<%@include file="registrationPopup.jsp" %>
<%}%>