<%@ page import="java.util.Date" %>
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
	 long currentTime= new Date().getTime(); 
	 long dfd= accessTime +timeout;
	 if( currentTime< dfd){
	/*  response.setHeader("Refresh", timeout + "; URL = ../login");
	 System.out.println("timeout========"+timeout); 
	if (session.getAttribute("usertype") != null) { */
%>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!-- Security Tags -->
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:csrfMetaTags />
<!-- Security Tags -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en" class="no-js">
<head><title>CEIR Portal</title>
<!--<title>Home</title>-->
<link
	href="https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,800&display=swap"
	rel="stylesheet">
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

<!-- Favicons-->
<link rel="icon" href="${context}/resources/images/DMC-Logo.png"
	sizes="32x32">
<script type="text/javascript"
	src="${context}/resources/js/plugins/jquery-1.11.2.min.js"></script>
		<script type="text/javascript" src="${context}/resources/graph_js/chart.min.js"></script>
	
<link rel="shortcut icon" href="">
<!-- CORE CSS-->
<link href="${context}/resources/css/materialize.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link href="${context}/resources/css/style.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<!-- Custome CSS-->

<link
	href="${context}/resources/js/plugins/data-tables/css/jquery.dataTables.min.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<!-- INCLUDED PLUGIN CSS ON THIS PAGE -->
<link href="${context}/resources/js/plugins/prism/prism.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.css"
	type="text/css" rel="stylesheet" media="screen,projection">
<link href="${context}/resources/css/dashboard.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link
	href="${context}/resources/font/font-awesome/css/font-awesome.min.css"
	type="text/css" rel="stylesheet" media="screen,projection">


<link rel="stylesheet"
	href="${context}/resources/custom_js/jquery-ui.css">

<script src="${context}/resources/custom_js/jquery.blockUI.js"></script>
<!------------------------------------------- Dragable Model---------------------------------->
<script src="${context}/resources/custom_js/1.12.1_jquery-ui.min.js"></script>
<script type="text/javascript"
	src="${context}/resources/graph_js/html2canvas.js?version=<%= (int) (Math.random() * 10) %>"></script>


<link rel="stylesheet"
	href="${context}/resources/project_css/iconStates.css">

<style>
.row {
	margin-bottom: 0;
	margin-top: 0;
}

/* input[type=text] {
      height: 35px; 
      margin: 0 0 5px 0;
    } */
input
[
type
=
text
]
:not
 
(
.browser-default
 
)
{
font-size
:
 
13
px
;
/* height: 30px; */


}
input[type=date] {
	font-size: 0.8rem;
}

textarea.materialize-textarea {
	padding: 0;
	padding-left: 10px;
}

.btn-flat {
	height: auto;
}

#starColor {
	color: red;
}



section {
	margin-top: 10px;
}

input#expectedArrivaldate {
    height: 6vh;
}
input#quantity {
    height: 7vh;
}



@media (min-width: 1200px)
.col-xl-8 {
    flex: 0 0 66.66667%;
    max-width: 66.66667%;
}
.col, .col-1, .col-10, .col-11, .col-12, .col-2, .col-3, .col-4, .col-5, .col-6, .col-7, .col-8, .col-9, .col-auto, .col-lg, .col-lg-1, .col-lg-10, .col-lg-11, .col-lg-12, .col-lg-2, .col-lg-3, .col-lg-4, .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-lg-auto, .col-md, .col-md-1, .col-md-10, .col-md-11, .col-md-12, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6, .col-md-7, .col-md-8, .col-md-9, .col-md-auto, .col-sm, .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12, .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8, .col-sm-9, .col-sm-auto, .col-xl, .col-xl-1, .col-xl-10, .col-xl-11, .col-xl-12, .col-xl-2, .col-xl-3, .col-xl-4, .col-xl-5, .col-xl-6, .col-xl-7, .col-xl-8, .col-xl-9, .col-xl-auto {
    position: relative;
    width: 100%;
    padding-right: .75rem;
}
.card {
    position: relative;
    display: flex;
    flex-direction: column;
    min-width: 0;
    word-wrap: break-word;
    background-color: #fff;
    background-clip: border-box;
    border: 1px solid #e3e6f0;
    border-radius: .35rem;
}

.shadow {
    box-shadow: 0 .15rem 1.75rem 0 rgba(58,59,69,.15)!important;
}
.mb-4, .my-4 {
    margin-bottom: 1.5rem!important;
}

.card-header:first-child {
    border-radius: calc(.35rem - 1px) calc(.35rem - 1px) 0 0;
}
.pb-3, .py-3 {
    padding-bottom: 1rem!important;
}
.pt-3, .py-3 {
    padding-top: 1rem!important;
}
.align-items-center {
    align-items: center!important;
}
.justify-content-between {
    justify-content: space-between!important;
}
.flex-row {
    flex-direction: row!important;
}
.d-flex {
    display: flex!important;
}
.card-header {
    padding: .75rem 1.25rem;
    margin-bottom: 0;
    background-color: #f8f9fc;
    border-bottom: 1px solid #e3e6f0;
}

.text-primary {
    color: #4e73df!important;
}

.font-weight-bold {
    font-weight: 700!important;
}
.m-0 {
    margin: 0!important;
}
.h6, h6 {
    font-size: 1rem;
}

.card-body {
    flex: 1 1 auto;
    padding: 1.25rem;
    overflow-x: scroll;
}


.highcharts-credits {
display: none !important;
}

.split {
    display: grid;
}
.icon-div {
    width: 39px;
    height: 34px;
    position: absolute;
    background-color: #009688;
    border-radius: 5px;
    margin: -20px 0 0 10px;
}
.round-circle {
    overflow-wrap: break-word;
    width: 200px;
    height: 84px;
    border: solid 1px #444;
    border-radius: 5px;
    margin-top: 30px;
    background-color: #fff;
    align-items: center;
}
.test-icon {
    font-size: 22px !important;
    color: #fff;
    margin-top: 6px;
}
th {
    background: #529dba !important;
    color: #fff;
     border-bottom: 1px solid #fff; 
}
</style>
</head>
<body data-id="46" data-roleType="${usertype}" data-userID="${userid}">
	<!-- START MAIN -->
		<div id="initialloader"></div>
	<!-- START WRAPPER -->
	<div class="wrapper">

		<!-- START CONTENT -->
		<section id="content">
			<!--start container-->
			<div class="container">
			
				<div class="section">
						
					<div class="row">
			
						<div class="col s12 m12 l12">
							<div class="row card-panel">
									<a id="wholePageImage" href="javascript:void(0);" onclick="graphImageDownload('Operator_dashboard')">Download</a>
								<div class="row card-panel responsive-page" id="endUserRaiseGrievance" style="display:block !important">
                            <h6 class="fixPage-modal-header ">
                            <spring:message code="sidebar.Operator_Dashboard" />
                            				</h6>
                            				<div id="wrapperPage">
                            				<h6 class="m-0 font-weight-bold text-primary" id="dateVal"></h6>
                            				 <div class="split">
                            						<div class="col s12 m12 info-div center" id="infoBox"></div>
                           	
                           <div class="cgraph_box">
                            
                               <div class="graph_info">
                               <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                 <h6 class="m-0 font-weight-bold text-primary">Operator Wise Blocked IMEI Graph</h6>
                 <div> <a href="javascript:void(0);" id="expblockedIMEI">Export</a> | 
                      <a id="blockedIMEIImg" download="Blocked_IMEI.jpg">Download</a></div>
                </div>
                <!-- Card Body -->
                <div class="card-body">
                   <canvas class="chart-area" id="blockIMEIGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
    </div></div>
<!--               <div class="card shadow mb-4">
                Card Header - Dropdown
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">Operator Wise IMEIs</h6>
                <div>
															<a id="expOperatorWiseIMEI">Export</a> | <a id="operatorWiseImage">Download</a>
														</div>
                </div>
                Card Body
                <div class="card-body" style = "height: 456px;">
                 
                      	      <table class="responsive-table striped datatable" id="activeDeviceTable">
                             	<thead>
				<tr>
				<th>Operator</th>
				<th>Count</th>
				</tr>
				</thead>
                                <tr>
                                  <td>QB</td>
                                  <td id="firstTD"></td>
                                  </tr>
                                  <tr>
                                  <td>Seatel</td>
                                  <td id="secondTD"></td>
                                </tr>
                                <tr>
                                  <td>Smart</td>
                                  <td id="thirdTD"></td>
                                </tr>
                                <tr>
                                  <td>Cellcard</td>
                                  <td id="fourthTD"></td>
                                </tr>
                                <tr>
                                  <td>Metfone</td>
                                  <td id="fifthTD"></td>
                                </tr>
  							  
                            </table>
                 
                </div>
              </div> -->
            </div>
            
            
                            <div class="graph_info">
              <%-- <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">Operator Wise Trend</h6>
              
                </div>
                <!-- Card Body -->
                <div class="card-body">
                 
                    <canvas class="chart-area" id="horizontalBarGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
    </div></div> --%>
    <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">Operator Wise Trend</h6>
                 <div> <a href="javascript:void(0);" id="exp">Export</a> | 
                      <a id="lineImage" download="Operator_Wise_Trend.jpg">Download</a></div>
                </div>
                <!-- Card Body -->
                <div class="card-body">
                   <canvas class="chart-area" id="lineGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
                </div>
              </div>
    
    </div>
                      
             </div>               
              </div> </div>
              
                                       <div class="cgraph_box">
<%--                                           <div class="col-xl-8 col-lg-7">
    <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">Pairing Types</h6>
                 <div> <a id="expPairingType">Export</a> | 
                      <a id="pairingTypesImage" download="Pairing_Types.jpg">Download</a></div>
                </div>
                <!-- Card Body -->
                <div class="card-body" id="noData">
                   <canvas class="chart-area" id="gaugeGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
                </div>
              </div>
    
    </div> --%>
    
               <div class="graph_info">
    <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">Operator's Active Pair</h6>
                 <div> <a href="javascript:void(0);" id="expOperatorActivePair">Export</a> | 
                      <a id="OperatorActiveImage" download="Operator's_Active_Pair.jpg">Download</a></div>
                </div>
                <!-- Card Body -->
                <div class="card-body" id="noData">
                   <canvas class="chart-area" id="pieGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
                </div>
              </div>
    
    </div>
            </div>       
              
   <!-- --------------------------------------------------- blocked IMEI graph----------------------------------------------------- -->                
                    
                    <div class="split">
                            						<!-- <div class="col s12 m12 info-div center" id="infoBox"></div> -->
                           	
                           <%-- <div style="display:flex; margin-left: 12px;">
                            
                             
            
            
                            <div class="col-xl-8 col-lg-7">
              <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">Operator Wise Trend</h6>
              
                </div>
                <!-- Card Body -->
                <div class="card-body">
                 
                    <canvas class="chart-area" id="horizontalBarGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
    </div></div>
    <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">Blocked IMEI Graph</h6>
                 <div> <a id="expblockedIMEI">Export</a> | 
                      <a id="blockedIMEIImg" download="Blocked_IMEI.jpg">Download</a></div>
                </div>
                <!-- Card Body -->
                <div class="card-body">
                   <canvas class="chart-area" id="blockIMEIGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
                </div>
              </div>
    
    </div>
                      
             </div>      --%>
             
            
             
                       
              </div> 
    <!-- ---------------------------------------------------end blocked IMEI graph-------------------------------------------- -->              
     
 <%--     <div style="display:flex">
                            <div class="col-xl-8 col-lg-7">
              <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">User Login Graph</h6>
                 
                </div>
                <!-- Card Body -->
                <div class="card-body">
                   <canvas class="chart-area" id="gaugeGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
                </div>
              </div>
            </div>
             
             
             
             
             <div class="col-xl-8 col-lg-7">
              <div class="card shadow mb-4">
                <!-- Card Header - Dropdown -->
                <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                  <h6 class="m-0 font-weight-bold text-primary">User Login Graph</h6>
              
                </div>
                <!-- Card Body -->
                <div class="card-body">
                 
                    <canvas class="chart-area" id="horizontalBarGraph" style = "width: 550px; height: 400px; margin: 0 auto">
                  </canvas>
    </div></div></div></div> --%>
    </div></div></div></div></div></div></section></div>



	<!--end container-->



	<!-- END MAIN -->


</body>
<script type="text/javascript" src="${context}/resources/js/materialize.js"></script>		
<script type="text/javascript" src="${context}/resources/custom_js/bootstrap.min.js"></script>

<script type="text/javascript" src="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	<script type="text/javascript"
		src="${context}/resources/js/plugins/data-tables/js/jquery.dataTables.min.js"></script>

<script src="${context}/resources/graph_js/chartjs-plugin-datalabel.js"></script>
<script type="text/javascript"
	src="${context}/resources/graph_js/toImage.js?version=<%= (int) (Math.random() * 10) %>"></script>
	<script type="text/javascript"
		src="${context}/resources/graph_js/jsonToCSV.js?version=<%= (int) (Math.random() * 10) %>"></script>		
<script type="text/javascript"
		src="${context}/resources/graph_js/OperatorDashboard.js?version=<%= (int) (Math.random() * 10) %>"></script>

	<!-- chartist -->
<script type="text/javascript">$( document ).ready(function() {activeDeviceGraph(); if($("body").attr("data-roleType") == '' || ($("body").attr("data-roleType") != window.parent.$("body").attr("data-roleType"))){window.top.location.href = "./login?isExpired=yes";} var timeoutTime = <%=session.getLastAccessedTime()%>;var timeout = <%=session.getMaxInactiveInterval()%>;timeoutTime += timeout;var currentTime;$("body").click(function(e) {$.ajaxSetup({headers:{ 'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content") }});$.ajax({url: './serverTime',type: 'GET',async: false,success: function (data, textStatus, jqXHR) {currentTime = data;},error: function (jqXHR, textStatus, errorThrown) {}});if( currentTime > timeoutTime ){window.top.location.href = "./login?isExpired=yes";}else{timeoutTime = currentTime + timeout;}});});</script>
<script type="text/javascript"
		src="${context}/resources/ajax/keyBoardShortcut.js?version=<%= (int) (Math.random() * 10) %>"></script>
</html>
<%
	} else {
		/*  request.setAttribute("msg", "  *Please login first");
		request.getRequestDispatcher("./index.jsp").forward(request, response); */
%>
<script language="JavaScript">
	sessionStorage.setItem("loginMsg",
			"*Session has been expired");
	window.top.location.href = "./login";
</script>
<!-- <script language="JavaScript">
window.onload = function() {
setInterval(function() {
window.location.replace("../login");
}, 10);
}
 --></script>
<%
	}
%>


