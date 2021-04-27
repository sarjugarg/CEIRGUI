var featureId = 17;
var roleType = $("body").attr("data-roleType");
var userId = $("body").attr("data-userID");
var currentRoleType = $("body").attr("data-selected-roleType"); 
var startdate=$('#startDate').val(); 
var endDate=$('#endDate').val();
var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';

$(document).ready(function(){
	pageRendering();
	setTimeout(function(){ auditManagementDatatable(); }, 200);
	
	
});

var role = currentRoleType == null ? roleType : currentRoleType;


//**************************************************Audit Management table**********************************************

function auditManagementDatatable(){
	
	var userType = $('#userType').val() == 'null' ? null : $("#userType").val();
	var featureName = $('#feature').val() == 'null' ? null : $("#feature").val();
	var subFeature = $('#subfeatureFilter').val() == 'null' ? null : $("#subfeatureFilter").val();
	var roleType = $('#roleType').val() == 'null' ? null : $("#roleType").val();
	var filterRequest={
			
			//"userId":parseInt(userId),
			//"featureId":parseInt(featureId),
			//"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType": userType,
			"featureId": parseInt(featureId),
			"startDate" : $("#startDate").val(),
			"endDate" : $("#endDate").val(),
			"txnId" : $("#transactionID").val(),
			"featureName" : featureName,
			"subFeatureName" : subFeature,
			"userName" : $("#userName").val(),
			"roleType" : roleType,
			"publicIp" : $("#publicIpFilter").val(),
			"browser" : $("#browserFilter").val()
		
			
			
	}
	if(lang=='km'){
		var langFile="./resources/i18n/khmer_datatable.json";
	}else if(lang=='en'){
		var langFile='./resources/i18n/english_datatable.json';
	}	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});

	//////console.log("filterRequest-->" +JSON.stringify(filterRequest));
	$.ajax({
		url: 'headers?type=auditManagement',
		type: 'POST',
		dataType: "json",
		success: function(result){
			var table=	$("#auditLibraryTable").removeAttr('width').DataTable({
				destroy:true,
				"serverSide": true,
				orderCellsTop : true,
				"ordering" : true,
				"bPaginate" : true,
				"bFilter" : false,
				"bInfo" : true,
			"bSearchable" : true,
				"oLanguage": {
			        "sEmptyTable": "No records found in the system"
			    },
			    "aaSorting": [],
				
			    initComplete: function() {
			 		$('.dataTables_filter input')
   .off().on('keyup', function(event) {
	   if (event.keyCode === 13) {
			 table.search(this.value.trim(), false, false).draw();
		}
      
   });
   },
				ajax: {
					url : 'auditManagementData',
					type: 'POST',
					dataType: "json",
					data : function(d) {
						d.filter = JSON.stringify(filterRequest); 
					//	////console.log(JSON.stringify(filterRequest));
					}

				},
				"columns": result,
				fixedColumns: true,
				columnDefs: [
		            { width: 50, targets: result.length - 1 },
		            { width: 125, targets: 0 },
		            { width: 150, targets: 1 },
		            { orderable: false, targets: -1 }
		        ]
			});
			$('div#initialloader').delay(300).fadeOut('slow');
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			//////console.log("error in ajax");
		}
	});
}



//**************************************************viewAudit page buttons**********************************************

function pageRendering(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});

	$.ajax({
		url: 'audit/pageRendering',
		type: 'POST',
		dataType: "json",
		success: function(data){
			data.userStatus == "Disable" ? $('#btnLink').addClass( "eventNone" ) : $('#btnLink').removeClass( "eventNone" );
			
			var elem='<p class="PageHeading">'+data.pageTitle+'</p>';		
			$("#pageHeader").append(elem);
			var button=data.buttonList;
			var date=data.inputTypeDateList;
			for(i=0; i<date.length; i++){
				if(date[i].type === "date"){
					$("#auditTableDiv").append("<div class='input-field'>"+
							"<div id='enddatepicker' class='input-group date'>"+
							"<input class='form-control datepicker' onchange='checkDate(startDate,endDate)' type='text' id="+date[i].id+" autocomplete='off'>"+
							"<label for="+date[i].id+">"+date[i].title
							+"</label>"+
							"<span	class='input-group-addon' style='color: #ff4081'>"+
							"<i	class='fa fa-calendar' aria-hidden='true' style='float: right; margin-top: -37px;'>"+"</i>"+"</span>");
					$( "#"+date[i].id ).datepicker({
						dateFormat: "yy-mm-dd",
						 maxDate: new Date()
			        }); 
				}else if(date[i].type === "text"){
					$("#auditTableDiv").append("<div class='input-field' ><input type="+date[i].type+" id="+date[i].id+" maxlength="+date[i].className+" /><label for="+date[i].id+" class='center-align'>"+date[i].title+"</label></div>");
				}
				else if(date[i].type === "select"){

					var dropdownDiv=
						$("#auditTableDiv").append("<div class='selectDropdwn'>"+
								
								"<div class='select-wrapper select2  initialized'>"+
								"<span class='caret'>"+"</span>"+
								"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

								"<select id="+date[i].id+" class='select2 initialized'>"+
								"<option value=''>"+date[i].title+
								"</option>"+
								"</select>"+
								"</div>"+
						"</div>");
				
				}
				
			} 
		
		// dynamic dropdown portion
		/*	var dropdown=data.dropdownList;
			for(i=0; i<dropdown.length; i++){
				var dropdownDiv=
					$("#auditTableDiv").append("<div class='col s6 m2 selectDropdwn'>"+
						
							"<div class='select-wrapper select2  initialized'>"+
							"<span class='caret'>"+"</span>"+
							"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

							"<select id="+dropdown[i].id+" class='select2 initialized'>"+
							"<option value=null selected>"+dropdown[i].title+
							"</option>"+
							"</select>"+
							"</div>"+
					"</div>");
			}*/

				$("#auditTableDiv").append("<div class='filter_btn'><button type='button' class='btn primary botton' id='submitFilter'/></div>");
				$("#auditTableDiv").append("<div class='filter_btn'><button type='button'  class='btn primary botton' id='clearFilter'>"+$.i18n('clearFilter')+"</button></div>");
				$("#auditTableDiv").append("<div class='filter_btn'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportAuditData()'>Export<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");
				$('#clearFilter').attr("onclick", "filterResetAudit('viewFilter')");	
				for(i=0; i<button.length; i++){
					$('#'+button[i].id).text(button[i].buttonTitle);
					$('#'+button[i].id).attr("onclick", button[i].buttonURL);
				}

			
		


		}
	});
	
	setAllDropdown()
	
}


function setAllDropdown(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});

	$.getJSON('./registrationUserType?type=2', function(data) {
		for (i = 0; i < data.length; i++) {
			$('<option>').val(data[i].usertypeName).text(data[i].usertypeName)
			.appendTo('#userType');
		}
	});
	
	$.getJSON('./registrationUserType?type=2', function(data) {
		for (i = 0; i < data.length; i++) {
			$('<option>').val(data[i].usertypeName).text(data[i].usertypeName)
			.appendTo('#roleType');
		}
	});
	
	
	$.getJSON('./getAllfeatures', function(data) {
		for (i = 0; i < data.length; i++) {
		$('<option>').val(data[i].name).text(data[i].name).appendTo('#feature');
		}
	});
	
	$.getJSON('./getsubfeatures', function(data) {
		for (i = 0; i < data.length; i++) {
		$('<option>').val(data[i].name).text(data[i].name).appendTo('#subfeatureFilter');
		}
	});
}






$('.datepicker').on('mousedown',function(event){
	event.preventDefault();
});


function viewDetails(Id){
	$("#viewAuditModel").openModal({
        dismissible:false
    });
	 var Id = parseInt(Id);
	 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});

	$.ajax({
		url : './audit/view/'+Id,
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'GET',
		success : function(data) {
			setViewPopupData(data);
		},
		error : function() {
			//alert("Failed");
		}
	});
}


function setViewPopupData(data){
	data.userId=="" || data.userId==null ? $("#viewUserId").val('NA') : $("#viewUserId").val(data.userId);
	data.txnId=="" || data.txnId==null ? $("#viewTxnId").val('NA') : $("#viewTxnId").val(data.txnId);
	data.userName=="" || data.userName==null ? $("#viewUserName").val('NA') : $("#viewUserName").val(data.userName);
	data.userType=="" || data.userType==null ? $("#viewUserType").val('NA') : $("#viewUserType").val(data.userType);
	data.roleType=="" || data.roleType==null ? $("#viewRoleType").val('NA') : $("#viewRoleType").val(data.roleType);
	data.featureName=="" || data.featureName==null ? $("#viewFeature").val('NA') : $("#viewFeature").val(data.featureName);
	data.subFeature=="" || data.subFeature==null ? $("#viewSubFeature").val('NA') : $("#viewSubFeature").val(data.subFeature);
	data.publicIp=="" || data.publicIp==null ? $("#viewPublicIp").val('NA') : $("#viewPublicIp").val(data.publicIp);
	data.browser=="" || data.browser==null ? $("#viewBrowser").val('NA') : $("#viewBrowser").val(data.browser);
}


/*function exportAuditData(){
	var table = $('#auditLibraryTable').DataTable();
	var info = table.page.info(); 
	var pageNo=info.page;
	var pageSize =info.length;
	window.location.href="./exportAuditData?pageSize="+pageSize+"&pageNo="+pageNo;
	
}*/


function exportAuditData()
{	
	var table = $('#auditLibraryTable').DataTable();
	var info = table.page.info(); 
	var pageNo=info.page;
	var pageSize =info.length;
	var userType = $('#userType').val() == 'null' ? null : $("#userType").val();
	var featureName = $('#feature').val() == 'null' ? null : $("#feature").val();
	var subFeature = $('#subfeatureFilter').val() == 'null' ? null : $("#subfeatureFilter").val();
	var roleType = $('#roleType').val() == 'null' ? null : $("#roleType").val();
	
	var filterRequest={
			
			//"userId":parseInt(userId),
			//"featureId":parseInt(featureId),
			//"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType": userType,
			"featureId": parseInt(featureId),
			"startDate" : $("#startDate").val(),
			"endDate" : $("#endDate").val(),
			"txnId" : $("#transactionID").val(),
			"featureName" : featureName,
			"subFeatureName" : subFeature,
			"userName" : $("#userName").val(),
			"roleType" : roleType,
			"pageNo":parseInt(pageNo),
			"pageSize":parseInt(pageSize),
			"publicIp" : $("#publicIpFilter").val(),
			"browser" : $("#browserFilter").val()
			
			
	}
	
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	
	$.ajax({
		url: './exportAuditData',
		type: 'POST',
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(filterRequest),
		success: function (data, textStatus, jqXHR) {
			  window.location.href = data.url;

		},
		error: function (jqXHR, textStatus, errorThrown) {
			
		}
	});

}

function filterResetAudit(formID){
	$('#'+formID).trigger('reset');
	$("label").removeClass('active');
	auditManagementDatatable(lang,null);
}
