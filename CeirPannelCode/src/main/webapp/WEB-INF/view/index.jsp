<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head><title>DMC Portal</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="msapplication-tap-highlight" content="no">
<meta name="description"
content="Materialize is a Material Design Admin Template,It's modern, responsive and based on Material Design by Google. ">
<meta name="keywords"
content="materialize, admin template, dashboard template, flat admin template, responsive admin template,">
<!--<title>Main</title>-->

<link href="${context}/resources/js/plugins/data-tables/css/jquery.dataTables.min.css" type="text/css" rel="stylesheet"
media="screen,projection">

<!-- Favicons-->
<link rel="icon" href="${context}/resources/images/DMC-Logo.png" sizes="32x32">

<!-- For Windows Phone -->
<link rel="stylesheet" href="${context}/resources/font/font-awesome/css/font-awesome.min.css">

<!-- CORE CSS-->
<link href="${context}/resources/css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="${context}/resources/css/style.css" type="text/css" rel="stylesheet" media="screen,projection">

<!-- INCLUDED PLUGIN CSS ON THIS PAGE -->
<link href="${context}/resources/js/plugins/prism/prism.css" type="text/css" rel="stylesheet" media="screen,projection">
<link href="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.css" type="text/css" rel="stylesheet"
media="screen,projection">
<link href="${context}/resources/js/plugins/chartist-js/chartist.min.css" type="text/css" rel="stylesheet" media="screen,projection">

<style>
ul li {
display: inline-flex;
}

li {
padding: 7px 15px;
border: solid 1px #c9c9c9;
border-radius: 5px;
margin-right: 10px;
}
select{
height: 1.5rem;
border: none;
}
footer {
padding-left: 0;
}
</style>
<script>
var contextpath = "${context}";
</script>

</head>

<body>

<!-- //////////////////////////////////////////////////////////////////////////// -->

<!-- START CONTENT -->
<section id="content" id="mainPage">
<!--start container-->
<div class="container">
<div class="section">
<div class="row">
<div class="col s12 m12 l12">
<h1 style="text-align: center;"><spring:message code="welcome.title" /></h1>
<%-- <h5> <spring:message code="table.creationDate" /></h5> --%>
<%-- <img src="${context}/resources/images/TELECOMMUNICATIONS-4.jpg" alt="" class="responsive-img"> --%>
</div>
</div>

<div class="row">
<div class="col s12 m12 l12">

<ul class="haed-btn">
<a href="${context}/login" style="color: #000;">
<li class="haed-btn-style"><spring:message code="registration.login" /></li></a>
<li class="haed-btn-style" style="padding: 0;">
<select id="usertypes" class="browser-default" onchange="openRegistrationPage(this.value)" style="height: 35px; width: 150px;">
<option value="" disabled selected><spring:message code="select.registration" /></option>
<option value="Importer">Importer</option>
<option value="Distributor">Distributor</option>

<option value="Retailer">Retailer</option>
<option value="Custom">Custom</option>
<option value="Operator">Operator</option>
<option value="TRC">TRC</option>
<option value="Manufacturer">Manufacturer</option>
<option value="Lawful Agency">Lawful Agency</option>
<option value="Immigration">Immigration</option>
<option value="DRT">DRT</option>
</select>
</li>

<li class="haed-btn-style" style="padding: 0;">
<select id="usertypes" class="browser-default" onchange="openEndUserGrievancePage(this)" style="height: 35px; width: 150px;">
<option value="" disabled selected><spring:message code="select.registerGrievance" /></option>
<option value="0">Report Grievance</option>
<option value="1">Track Grievance</option>
</select>
</li>
<li class="haed-btn-style" style="padding: 0;">
<select id="usertypes" class="browser-default" onchange="openEndUserStockPage(this)" style="height: 35px; width: 150px;">
<option value="" disabled selected><spring:message code="select.stock" /></option>
<option value="0">Upload Stock</option>
<option value="1">Check Upload Status</option>
</select>
</li>
<a href="./checkDeviceslogin" style="color: #000;" >
<li class="haed-btn-style"><spring:message code="registration.checkdevice" /></li></a>
    <a onclick="selfRegisterDevice()" style="color: #000;"> <li class="haed-btn-style">Register
                                    Device</li></a>
                         <a onclick="updateVisaValidity()" style="color: #000;">   <li class="haed-btn-style">Update
                                    Visa Validity</li></a>
<!-- <li class="haed-btn-style"><a href="#" style="color: #000;">Report
Grievance</a></li> -->
<!-- <li class="haed-btn-style">
<a class='dropdown-trigger btn' href='#' data-target='dropdown1'>Select user</a>
<ul id='dropdown1' class='dropdown-content'>
<li><a href="#!">one</a></li>
<li><a href="#!">two</a></li>
<li class="divider" tabindex="-1"></li>
</ul>
</li> -->
</ul>

</div>
</div>
</div>
</div>
<!--end container-->
</section>
<!-- END CONTENT -->



<!-- //////////////////////////////////////////////////////////////////////////// -->

<!-- START FOOTER -->
<footer class="page-footer" style="position: fixed; bottom: 0; width: 100%;">
<div class="footer-copyright">
<div class="container">
<span class="right"><spring:message code="registration.copyright" /></span>
</div>
</div>
</footer>
<!-- END FOOTER -->





<!-- START CONTENT -->
<section id="content" id="loginForm" style="display: none;">
<!--start container-->
<div class="container">
<div class="section">
<div class="row card-panel" style="width: 50%; height: 65vh; margin: auto; margin-top: 10vh;">
<div class="col s12 m12 l12">
<div class="row">
<h5 style="text-align: -webkit-center;"><spring:message code="registration.login" /></h5> <hr style="margin-bottom: 30px;">

<!-- <div class="col s1"><i class="fa fa-lock" aria-hidden="true" style="font-size: 30px; margin-top: 12px; color: #ff4081;"></i></div> -->
<div class="input-field col s12">
<select class="browser-default">
<option value="" disabled selected><spring:message code="select.userType" /></option>
<option value="Air">Importer</option>
<option value="Land">Distributor</option>
<option value="Water">Retailer</option>
</select>
</div>

<!-- <div class="col s1">
<span class="fa-passwd-reset fa-stack" style="margin-top: 12px; color: #ff4081;">
<i class="fa fa-undo fa-stack-2x"></i>
<i class="fa fa-lock fa-stack-1x"></i>
</span></div> -->
<div class="input-field col s12">

<label for="newPassword" style="color: #000; font-size: 12px;"><spring:message code="registration.username" /></label>
<input type="text" id="newPassword" class="" maxlength="10" />
</div>

<!-- <div class="col s1"><i class="fa fa-check-square-o" aria-hidden="true" style="font-size: 28px; margin-top: 12px; color: #ff4081;;"></i></div> -->
<div class="input-field col s12">

<label for="confirmPassword" style="color: #000; font-size: 12px;"><spring:message code="registration.password" /></label>
<input type="text" class="" id="confirmPassword" maxlength="10" />
</div>
</div>
<div class="row" style="margin-top: 30px;">
<div class="input-field col s12 m12 l12 center">
<a href="index.html" class="btn" type="button" id="save" style="width: 108px;"><spring:message code="registration.login" /></a>
</div>
</div>

</div>
</div>
</div>
</div>
<!--end container-->
</section>
<!-- END CONTENT -->





<!-- ================================================
Scripts
================================================ -->
<!-- jQuery Library -->
<script src="${context}/resources/custom_js/jquery.min.js"></script>
<!-- ajax js -->
<script type="text/javascript" src="${context}/resources/ajax/Registration.js?version=<%= (int) (Math.random() * 10) %>"></script>
<!--materialize js-->
<script type="text/javascript" src="${context}/resources/js/materialize.js"></script>

<script type="text/javascript" src="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
<script type="text/javascript" src="${context}/resources/js/plugins.js"></script>

</body></html>