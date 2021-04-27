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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- Security Tags -->
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:csrfMetaTags />
<!-- Security Tags -->
<c:set var="context" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html class="no-js" lang="en" dir="ltr">
<head><title>CEIR Portal</title>
<!--<title>Currency Management</title>-->
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
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<!-- Security Tags -->

<script type="text/javascript"
	src="${context}/resources/js/plugins/jquery-1.11.2.min.js"></script>

<!-- Favicons-->
<link rel="icon" href="${context}/resources/images/DMC-Logo.png" sizes="32x32">
<!-- CORE CSS-->
<link href="${context}/resources/css/materialize.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<link href="${context}/resources/css/style.css" type="text/css"
	rel="stylesheet" media="screen,projection">
<script type="text/javascript"
	src="${context}/resources/js/plugins/data-tables/js/jquery.dataTables.min.js"></script>
<link
	href="${context}/resources/js/plugins/data-tables/css/jquery.dataTables.min.css"
	type="text/css" rel="stylesheet" media="screen,projection">

<!-- Custome CSS-->
<link href="" type="text/css"
	rel="stylesheet" media="screen,projection">
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
<script src="${context}/resources/custom_js/1.12.1_jquery-ui.min.js"></script>

<script src="${context}/resources/custom_js/jquery.blockUI.js"></script>



</head>
<body data-id="53"
	data-roleType="${usertype}" data-userTypeID="${usertypeId}"
	data-userID="${userid}" data-selected-roleType="${selectedUserTypeId}"
	data-stolenselected-roleType="${stolenselectedUserTypeId}"
	data-selected-consignmentTxnId="${consignmentTxnId}"
	data-selected-consignmentStatus="${consignmentStatus}"
	data-selected-username="${username}"
	session-value="en"
	session-valueTxnID="${not empty param.txnID ? param.txnID : 'null'}">

	<%-- session-value="${not empty param.NID ? param.NID : 'null'}" --%>

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
							<form action="${context}/addressManagement" id="viewFilter"
								method="post">
								<div class="registrationTableDiv_box" id="AddressTableDiv"
									style="padding-bottom: 5px; background-color: #e2edef52;">
									<div id="filterBtnDiv"></div>
								</div>
							</form>
							<table id="AddressManagementLibraryTable"
								class="responsive-table striped display"></table>

						</div>

					</div>
				</div>
				<div id="footerBtn"></div>
			</div>
		</div>
		
	
		<!--end container-->
	</section>
	
	<div id="addAddressModal" class="modal">
           <h6 class="modal-header"><spring:message code="sidebar.Address_Management" /></h6>
           <div class="modal-content">
            <div class="row">
              
                   <h5 class="center">
						<label> <input name="group1" type="radio" value="0"
							onclick="AddSystemAddress('province');"/>
							<span class="checkboxFont"> <spring:message code="input.province" /></span></label>
									
						 <label> <input name="group2" type="radio" value="1"
							onclick="AddSystemAddress('district')"/>
							<span class="checkboxFont"> <spring:message code="input.district" /></span>
						</label> 
						
						<label> <input name="group3" type="radio" value="2"
							onclick="AddSystemAddress('commune')"/>
							<span class="checkboxFont"> <spring:message code="input.commune" /></span>
						</label> 
						
						<label> <input name="group4" type="radio" value="3"
							onclick="AddSystemAddress('village')"/>
							<span class="checkboxFont"> <spring:message code="input.village" /></span>
						</label> 
					</h5>
							  
            </div>
            <div class="row">
                <div class="input-field col s12 center">
                    <button class="btn modal-close" onclick = "resetButtons()"style="margin-left: 10px;"><spring:message code="button.cancel" /></button>
                </div>
            </div>
        </div>
    </div>
    
    <div id="addProvinceModal" class="modal">
               <form action="" onsubmit="return saveProvince()" method="POST"
								enctype="multipart/form-data" >
								  <div class="row" id="singleInput">
								  <h6 class="modal-header "> Add Province</h6>
                                <div class="col s12 m12 l12">
                   	   
                                   <div class="row"  style="margin-top: 10px">
                                        	<div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="table.country" /> <!-- <span
										class="star">*</span> --></label> <select id="country"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required disabled></select>
										</div> 
                                       		
                                       
										
									<div class="input-field col s12 m6" style="margin-top: 22px;">
										<input type="text" name="province" id="addProvince"
										pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"
							placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
						 required/> <label for="addProvince"
							class="center-align"><spring:message
								code="input.province" /> <span class="star">*</span></label>
								</div>		
                          </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.submit" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div> 
        
         <div id="addDistrictModal" class="modal">
               <form action="" onsubmit="return saveDistrict()" method="POST"
								enctype="multipart/form-data" >
								  <div class="row" id="singleInput">
								  <h6 class="modal-header ">Add District</h6>
                                <div class="col s12 m12 l12">
                   	   
                                   <div class="row"  style="margin-top: 10px">
                                        	<%-- <div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="table.country" /> <span
										class="star">*</span></label> <select id="country1"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required></select>
										</div>  --%>
                                        	
                                        	
                                        	<div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="input.province" /> <span
										class="star">*</span></label> <select id="provinceForDistrict"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required></select>
										</div> 
                                       		
                                       
										
									<div class="input-field col s12 m6" style="margin-top: 22px;">
										<input type="text" name="district" id="addDistrict"
										pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"
							placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
							  required/> <label for="addDistrict"
							class="center-align"><spring:message
								code="input.district" /> <span class="star">*</span></label>
								</div>		
                          </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.submit" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div> 
        
          <div id="addCommuneModal" class="modal">
               <form action="" onsubmit="return saveCommune()" method="POST"
								enctype="multipart/form-data" >
								  <div class="row" id="singleInput">
								  <h6 class="modal-header "> Add Commune</h6>
                                <div class="col s12 m12 l12">
                   	   
                                   <div class="row"  style="margin-top: 10px">
                                   
                                   	<div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="input.province" /> <span
										class="star">*</span></label> <select id="provinceForCommune"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required></select>
										</div> 
										
                                   
                                        	<div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="input.district" /> <span
										class="star">*</span></label> <select id="districtForCommune"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required>
										<option value=""><spring:message code="select.district" /></option>
										</select>
										</div> 
                                       		
                                       
										
									<div class="input-field col s12 m6" style="margin-top: 22px;">
										<input type="text" name="commune" id="addCommune"
										pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"
							placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
							maxlength="50" required/> <label for="addCommune"
							class="center-align"><spring:message
								code="input.commune" /> <span class="star">*</span></label>
								</div>		
                          </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.submit" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div> 
		
		<div id="addVillageModal" class="modal">
               <form action="" onsubmit="return saveVillage()" method="POST"
								enctype="multipart/form-data" >
								  <div class="row" id="singleInput">
								  <h6 class="modal-header "> Add Village</h6>
                                <div class="col s12 m12 l12">
                   	   					
                                   <div class="row"  style="margin-top: 10px">
                                   <div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="input.province" /> <span
										class="star">*</span></label> <select id="provinceForVillage"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required></select>
									</div> 
                                   
                                   			<div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="input.district" /> <span
										class="star">*</span></label> <select id="districtForVillage"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required >
										<option value=""><spring:message code="select.district" /></option></select>
										</div> 
                                   
                                        	<div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label><spring:message code="input.commune" /> <span
										class="star">*</span></label> <select id="communeForVillage"
										class="browser-default" class="mySelect"
										onchange="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										oninvalid="InvalidMsg(this,'select','<spring:message code="validation.selectFieldMsg" />');"
										style="padding-left: 0;" required>
										<option value=""><spring:message code="select.commune" /></option></select>
										</div> 
                                       		
                                       
										
									<div class="input-field col s12 m6" style="margin-top: 22px;">
										<input type="text" name="village" id="addVillage"
							pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"										
							placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
							maxlength="50" required/> <label for="addVillage"
							class="center-align"><spring:message
								code="input.village" /> <span class="star">*</span></label>
								</div>		
                          </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.submit" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div> 
        
        <div id="confirmSaveModal" class="modal">
         <h6 class="modal-header"><spring:message code="sidebar.Address_Management" /></h6>
          <div class="modal-content">
            <div class="row">
                <form action="">
                  
                    <h6 id ="successMessage"></h6>
                </form>
            </div>
            <div class="row">
                <div class="input-field col s12 center">
                    <a href="./addressManagement?FeatureId=53" class="btn modal-close" ><spring:message code="modal.ok" /></a>
                    <%-- <a onclick="closeConfirmantionModel()"
						class="modal-close modal-trigger btn" type="submit"><spring:message code="modal.ok" /></a> --%>
                </div>
            </div>
        </div>
    	</div>
	<div id="DeleteAddressModal" class="modal">
		<h6 class="modal-header"><spring:message code="modal.header.deleteAddress" /></h6>
		<div class="modal-content">
		<div class="row">
				<h6><spring:message code="modal.message.Address.delete" /></h6>
			</div> 
			<input type="text" id="deleteFieldId" hidden>
			<div class="row">
				<div class="input-field col s12 center">
					<a onclick="confirmantiondelete()"
						class="modal-close modal-trigger btn" type="submit"><spring:message code="modal.yes" /></a>
					<button class="modal-close btn" style="margin-left: 10px;"><spring:message code="modal.no" /></button>
				</div>
			</div>
		</div>
	</div>	
	
	<div id="closeDeleteModal" class="modal">
			<h6 class="modal-header"><spring:message code="modal.header.deleteAddress" /></h6>
			<div class="modal-content">
		
			
			<div class="row">

				<h6 id="tacModalText"><spring:message code="modal.message.AddressDeleted" /> </h6>
			</div>
			<div class="row">
				<div class="input-field col s12 center">
					<a href="" class="modal-close btn"
						style="margin-left: 10px;">OK</a>
				</div>
			</div>
		</div>
	</div>
	
		<div id="editAddressModal" class="modal">
           <h6 class="modal-header">Update Address</h6>
           <div class="modal-content">
            <div class="row">
              
                   <h5 class="center">
						<label> <input name="group1" type="radio" value="0"
							onclick="EditSystemAddress('province');"/>
							<span class="checkboxFont"> <spring:message code="input.province" /></span></label>
						
						<input type="text" name="localityId" id="localityId" hidden="hidden">
									
						 <label> <input name="group2" type="radio" value="1"
							onclick="EditSystemAddress('district')"/>
							<span class="checkboxFont"> <spring:message code="input.district" /></span>
						</label> 
						
						<label> <input name="group3" type="radio" value="2"
							onclick="EditSystemAddress('commune')"/>
							<span class="checkboxFont"> <spring:message code="input.commune" /></span>
						</label> 
						
						<label> <input name="group4" type="radio" value="3"
							onclick="EditSystemAddress('village')"/>
							<span class="checkboxFont"> <spring:message code="input.village" /></span>
						</label> 
					</h5>
							  
            </div>
            <div class="row">
                <div class="input-field col s12 center">
                    <button class="btn modal-close" onclick = "resetButtons()"style="margin-left: 10px;"><spring:message code="button.cancel" /></button>
                </div>
            </div>
        </div>
    </div>
	
	<div id="editProvinceModal" class="modal">
               <form action="" onsubmit="return updateProvince()" method="POST"
								enctype="multipart/form-data" >
								  <div class="row" id="singleInput">
								  <h6 class="modal-header "> Update Province</h6>
                                <div class="col s12 m12 l12">
                   	   
                                <div class="row"  style="margin-top: 10px">


						<div class="input-field col s12 m6" style="margin-top: 10px">
							<label for="editCountry" class="center-align"><spring:message
							code="table.country" /> </label>
							<input type="text" name="editCountry" id="editCountry"
								oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
								oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
								maxlength="50" required disabled/> 
						</div>

						<input type="text" name=oldProvince id="oldProvince" hidden="hidden">
						
						<div class="input-field col s12 m6" style="margin-top: 10px;">
							<label for="editProvince"
							class="center-align"><spring:message
							code="input.province" /> <span class="star">*</span></label>
							<input type="text" name="editProvince" id="editProvince"
							pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"
							placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
							maxlength="50" required/> 
						</div>	
						
						 <div class="input-field col s12 m6" style="margin-top: 22px;">
                        	<input type="text" id="editProvinceModifiedBy" name="value" disabled>
                        	<label for="editProvinceModifiedBy" class="center-align">Modified By </label>
                    	</div>
						
							
                      </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.update" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div> 
	
	<div id="editDistrictModal" class="modal">
               <form action="" onsubmit="return updateDistrict()" method="POST"
								enctype="multipart/form-data" >
						<div class="row" id="singleInput">
								  <h6 class="modal-header ">Update District</h6>
                                <div class="col s12 m12 l12">
                   	   
                                <div class="row"  style="margin-top: 10px">
                                        <div class="input-field col s12 m6" style="margin-top: 10px">
                                        <label for="editProvinceforDistrict"
										class="center-align"><spring:message
										code="input.province" /> </label>
										<input type="text" name="editProvinceforDistrict" id="editProvinceforDistrict"
										placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
										maxlength="50" required disabled /> 
								</div> 
                                       
                                 <input type="text" name=oldDistrict id="oldDistrict" hidden="hidden">
                                       		
                                 <div class="input-field col s12 m6" style="margin-top: 10px;">
                                 		<label for="editDistrict"
										class="center-align"><spring:message
										code="input.district" /> <span class="star">*</span></label>
										<input type="text" name="editDistrict" id="editDistrict"
										pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"
										placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
										maxlength="50" required/> 
								</div>
								
								<div class="input-field col s12 m6" style="margin-top: 22px;">
                        			<input type="text" id="editDistrictModifiedBy" name="value" disabled>
                        			<label for="editDistrictModifiedBy" class="center-align">Modified By </label>
                    		</div>		
                        	  </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.update" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div> 
        
      <div id="editCommuneModal" class="modal">
               <form action="" onsubmit="return updateCommune()" method="POST"
								enctype="multipart/form-data" >
								  <div class="row" id="singleInput">
								  <h6 class="modal-header "> Update Commune</h6>
                                <div class="col s12 m12 l12">
                   	   
                                   <div class="row"  style="margin-top: 10px">
                                   <div class="col s12 m6 l6" style="margin-bottom: 5px;">
                                   	<label for="editProvinceForCommune"
											class="center-align"><spring:message
											code="input.province" /> </label>
											<input type="text" name="commune" id="editProvinceForCommune"
											placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
											maxlength="50" required disabled/> 
									</div> 
									
									<input type="text" name=oldcommune id="oldcommune" hidden="hidden">
									
										
									<div class="col s12 m6 l6" style="margin-bottom: 5px;">
											<label for="editDistrictForCommune"
											class="center-align"><spring:message
											code="input.district" /> </label>
											<input type="text" name="editDistrictForCommune" id="editDistrictForCommune"
											placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
											maxlength="50" required disabled/> 
									</div> 
                                       		
                                     <div class="input-field col s12 m6" style="margin-top: 22px;">
                                      		<label for="editCommune"
											class="center-align"><spring:message
											code="input.commune" /> <span class="star">*</span></label>	
											<input type="text" name="editCommune" id="editCommune"
											pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"
											placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
											maxlength="50" required/> 
									</div>		
									
									<div class="input-field col s12 m6" style="margin-top: 22px;">
                        			<input type="text" id="editCommuneModifiedBy" name="value" disabled>
                        			<label for="editCommuneModifiedBy" class="center-align">Modified By </label>
                    			</div>	
                          </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.update" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div>   
        
       <div id="editVillageModal" class="modal">
               <form action="" onsubmit="return updateVillage()" method="POST"
								enctype="multipart/form-data" >
								  <div class="row" id="singleInput">
								  <h6 class="modal-header "> Update Village</h6>
                                <div class="col s12 m12 l12">
                   	   					
                                   <div class="row"  style="margin-top: 10px">
                                   <div class="col s12 m6 l6" style="margin-bottom: 5px;">
                                   		<label for="editProvinceForVillage"
										class="center-align"><spring:message
										code="input.province" /> </label>
										<input type="text" name="editProvinceForVillage" id="editProvinceForVillage"
										placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
										maxlength="50" required disabled/> 
									</div> 
                                  
                                   
                                   <input type="text" name=oldVillage id="oldVillage" hidden="hidden">
                                   
                                   
                                   
                                   <div class="col s12 m6 l6" style="margin-bottom: 5px;">
                                   		<label for="editDistrictForVillage"
										class="center-align"><spring:message
										code="input.district" /> </label>
										<input type="text" name="editDistrictForVillage" id="editDistrictForVillage"
										placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
										maxlength="50" required disabled/> 
									</div> 
                                   
                                   <div class="col s12 m6 l6" style="margin-bottom: 5px;">
                                   		 <label for="editCommuneForVillage"
										class="center-align"><spring:message
										code="input.commune" /> </label>
										<input type="text" name="editCommuneForVillage" id="editCommuneForVillage"
										placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
										maxlength="50" required disabled/>
										
								  </div> 
                                       		
                                  <div class="input-field col s12 m6" style="margin-top: 18px;">
                                  		<label for="editVillage"
										class="center-align"><spring:message
										code="input.village" /> <span class="star">*</span></label>
										<input type="text" name="editVillage" id="editVillage"
										pattern="<spring:eval expression="@environment.getProperty('pattern.province')" />"
										placeholder="" oninput="InvalidMsg(this,'input','<spring:message code="validation.50character" />');" oninvalid="InvalidMsg(this,'input','<spring:message code="validation.50character" />');"
										maxlength="50" required/> 
								</div>	
								
								<div class="input-field col s12 m6" style="margin-top: 18px;">
                        			<input type="text" id="editVillageModifiedBy" name="value" disabled>
                        			<label for="editVillageModifiedBy" class="center-align">Modified By </label>
                    			</div>		
                          </div>
                               <div class="row">
                                            <div class="input-field col s12 center" style="padding: 20px 0;">
                                                <!-- <a href="#submitIMEI" class="btn modal-trigger">Submit</a>  -->
                                                 <button class=" btn" type="submit"><spring:message code="button.update" /></button>
                                               	<button type="button" onclick = "resetButtons()" class="btn modal-close" style="margin-left: 10px;" title=" "><spring:message code="button.cancel" /></button>
                                            </div>
									</div>	
                                  </div>
                         </div></form>
        </div>  
        
        <div id="updateFieldsSuccess" class="modal">
     <h6 class="modal-header" style="margin:0px;">Update Address</h6>
        <div class="modal-content">
            
            <div class="row">
                <h6 id="updateFieldMessage"> </h6>
            </div>
            <div class="row">
                <div class="input-field col s12 center">
                    <a href="" class="modal-close btn"><spring:message code="modal.ok" /></a>
                </div>
            </div>
        </div>
    </div>
<!--materialize js-->
	<script type="text/javascript"
		src="${context}/resources/js/materialize.js"></script>

	<script type="text/javascript"
		src="${context}/resources/js/plugins/data-tables/js/jquery.dataTables.min.js"></script>



	<!--plugins.js - Some Specific JS codes for Plugin Settings-->
	<script
		src="${context}/resources/custom_js/bootstrap.min.js"></script>

	
	<!--custom-script.js - Add your own theme custom JS-->
	<script type="text/javascript" src="${context}/resources/js/plugins.js"></script>

	
	<!--scrollbar-->
	<script type="text/javascript"
		src="${context}/resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	
	<script type="text/javascript"
		src="${context}/resources/js/countries.js"></script>
	<script type="text/javascript"
		src="${context}/resources/js/SystemCountry.js"></script>
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
		src="${context}/resources/project_js/viewAddressManagement.js?version=<%= (int) (Math.random() * 10) %>"></script>
<%-- 	<script type="text/javascript"
		src="${context}/resources/ajax/Registration.js?version=<%= (int) (Math.random() * 10) %>"></script> --%>
			<script type="text/javascript"
		src="${context}/resources/project_js/validationMsg.js?version=<%= (int) (Math.random() * 10) %>"></script>
	
			<script type="text/javascript"
		src="" async></script>
<script type="text/javascript">$( document ).ready(function() {if($("body").attr("data-roleType") == '' || ($("body").attr("data-roleType") != window.parent.$("body").attr("data-roleType"))){window.top.location.href = "./login?isExpired=yes";} var timeoutTime = <%=session.getLastAccessedTime()%>;var timeout = <%=session.getMaxInactiveInterval()%>;timeoutTime += timeout;var currentTime;$("body").click(function(e) {$.ajaxSetup({headers:{ 'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content") }});$.ajax({url: './serverTime',type: 'GET',async: false,success: function (data, textStatus, jqXHR) {currentTime = data;},error: function (jqXHR, textStatus, errorThrown) {}});if( currentTime > timeoutTime ){window.top.location.href = "./login?isExpired=yes";}else{timeoutTime = currentTime + timeout;}});});</script>
<script type="text/javascript"
		src="${context}/resources/ajax/keyBoardShortcut.js?version=<%= (int) (Math.random() * 10) %>"></script>
</body></html>
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
<%
	}
%>


