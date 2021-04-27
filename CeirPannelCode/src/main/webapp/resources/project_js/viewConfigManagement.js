var featureId = 16;
var roleType = $("body").attr("data-roleType");
var userId = $("body").attr("data-userID");
var currentRoleType = $("body").attr("data-selected-roleType"); 
var startdate=$('#startDate').val(); 
var endDate=$('#endDate').val();
var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';
$(document).ready(function(){
	configManagementDatatable();
	pageRendering();
	
});

var role = currentRoleType == null ? roleType : currentRoleType;


//**************************************************Config Detail table**********************************************

function configManagementDatatable(){
	var ellipsis = "...";
	var filterRequest={
			"endDate":$('#endDate').val(),
			"startDate":$('#startDate').val(),
			"tag":$('#parametername').val(),
			"type" : parseInt($('#type').val()),
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"username" : $("body").attr("data-selected-username"),
			"description":$('#descriptionID').val(),
			"value":$('#valueID').val(),
			"userName" : $("body").attr("data-selected-username"),
			"roleType":$("body").attr("data-roleType")
	}
	if(lang=='en'){
		var langFile='./resources/i18n/english_datatable.json';
	}
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: 'headers?type=adminConfigMessage',
		type: 'POST',
		dataType: "json",
		success: function(result){
			/*console.log("Url-------" +url+"--------"+ "dataUrl-------" +dataUrl);*/
			var table=	$("#configLibraryTable").removeAttr('width').DataTable({
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
					url : 'adminConfigData',
					type: 'POST',
					dataType: "json",
					data : function(d) {
						d.filter = JSON.stringify(filterRequest); 
						//console.log(JSON.stringify(filterRequest));
					}

				},
				"columns": result,
				fixedColumns: true,
				columnDefs: [
					{
					render: function ( data, type, row ) {
					    return data.length > 80 ?
					        data.substr( 0, 80 ) + ellipsis : data;
					},
					targets: 3,
				},
		            { width: 100, targets: result.length - 1 },
		            { orderable: false, targets: -1 }
		        ]
			});
			
		   
			$('div#initialloader').delay(300).fadeOut('slow');
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			//console.log("error in ajax");
		}
	});
	
	
}



//**************************************************viewConfig page buttons**********************************************

function pageRendering(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: 'configManagement/pageRendering',
		type: 'POST',
		dataType: "json",
		success: function(data){
			var elem='<p class="PageHeading">'+data.pageTitle+'</p>';		
			$("#pageHeader").append(elem);
			var button=data.buttonList;

			var date=data.inputTypeDateList;
			
			for(i=0; i<date.length; i++){
				if(date[i].type === "date"){
					$("#configTableDiv").append("<div class='input-field responsiveDiv'>"+
							"<div id='enddatepicker' class='input-group date'>"+
							"<label for='TotalPrice'>"+date[i].title
							+"</label>"+"<input class='form-control datepicker' type='text' id="+date[i].id+" autocomplete='off'>"+
							"<span	class='input-group-addon' style='color: #ff4081'>"+
							"<i	class='fa fa-calendar' aria-hidden='true' style='float: right; margin-top: -37px;'>"+"</i>"+"</span>");
					$( "#"+date[i].id ).datepicker({
						dateFormat: "yy-mm-dd",
						 maxDate: new Date()
			        }); 	
				}
					else if(date[i].type === "text"){
						$("#configTableDiv").append("<div class='input-field'><input type="+date[i].type+" maxlength="+date[i].className+" id="+date[i].id+"><label for='parametername' class='center-align'>"+date[i].title+"</label></div>");
						
					}
					else if(date[i].type === "select"){

						var dropdownDiv=
							$("#configTableDiv").append("<div class='selectDropdwn'>"+
									
									"<div class='select-wrapper select2  initialized'>"+
									"<span class='caret'>"+"</span>"+
									"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

									"<select id="+date[i].id+" class='select2 initialized'>"+
									"<option>"+date[i].title+
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
					$("#configTableDiv").append("<div class='selectDropdwn'>"+
							
							"<div class='select-wrapper select2 form-control boxBorder boxHeight initialized'>"+
							"<span class='caret'>"+"</span>"+
							"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

							"<select id="+dropdown[i].id+" class='select2 form-control boxBorder boxHeight initialized'>"+
							"<option value='null' selected>"+dropdown[i].title+
							"</option>"+
							"</select>"+
							"</div>"+
					"</div>");
			}*/

			
			
			$("#configTableDiv").append("<div class='filter_btn'><button class='btn primary botton' type='button' id='submitFilter'></button></div>");
			$("#configTableDiv").append("<div class='filter_btn'><button type='button'  class='btn primary botton' id='clearFilter'>Clear all filters</button></div>");
			$("#configTableDiv").append("<div class='filter_btn'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportData()'>Export<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");
			$('#clearFilter').attr("onclick", "filterResetSystemAdmin('viewFilter')");
			for(i=0; i<button.length; i++){
				$('#'+button[i].id).text(button[i].buttonTitle);
				$('#'+button[i].id).attr("onclick", button[i].buttonURL);
			
			}
			
			
		}

	}); 
	
	//status-----------dropdown
	$.getJSON('./getDropdownList/CONFIG_TYPE', function(data) {
		for (i = 0; i < data.length; i++) {
			$('<option>').val(data[i].value).text(data[i].interp)
			.appendTo('#type');
		}
	});
};


$('.datepicker').on('mousedown',function(event){
	event.preventDefault();
});


function viewDetails(tag){
	$("#viewAdminSystemModel").openModal({
        dismissible:false
    });
	var RequestData = {
			"tag" : tag,
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"username" : $("body").attr("data-selected-username"),
			"userName" : $("body").attr("data-selected-username"),
			"roleType":$("body").attr("data-roleType")
	} 
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url : "./system/viewTag",
		data :	JSON.stringify(RequestData),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		success : function(data) {
			//console.log(data);
			setViewPopupData(data);
			$("label[class='center-align']").addClass('active');
		},
		error : function() {
			//alert("Failed");
		}
	});
}

function setViewPopupData(data){
	data.tag=="" || data.tag==null ? $("#viewTag").val('NA') : $("#viewTag").val(data.tag);
	data.value=="" || data.value==null ? $("#viewValue").val('NA') : $("#viewValue").val(data.value);
	data.typeInterp=="" || data.typeInterp==null ? $("#viewtype").val('NA') : $("#viewtype").val(data.typeInterp);
	data.description=="" || data.description==null ? $("#description").val('NA') : $("#description").val(data.description); 
	data.remark=="" || data.remark==null ? $("#remarks").val('NA') : $("#remarks").val(data.remark); 
	data.modifiedBy =="" || data.modifiedBy==null ?  $("#viewModifiedBy").val('NA'): $("#viewModifiedBy").val(data.modifiedBy);
	
}


function updateDetails(tag){
	$("#editAdminSystemModel").openModal({
        dismissible:false
    });
	var RequestData = {
			"tag" : tag,
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"username" : $("body").attr("data-selected-username"),
			"userName" : $("body").attr("data-selected-username"),
			"roleType":$("body").attr("data-roleType")
	} 
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url : "./system/viewTag",
		data :	JSON.stringify(RequestData),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		success : function(data) {
			//console.log(data);
			setEditPopupData(data);
			$("label[class='center-align']").addClass('active');
		},
		error : function() {
			//alert("Failed");
		}
	});
}


function setEditPopupData(data){
	$("#EditId").val(data.id);
	$("#editTag").val(data.tag);
	$("#editValue").val(data.value);
	$("#edittype").val(data.typeInterp);
	$("#editdescription").val(data.description);
	$("#editremarks").val(data.remark);
	data.modifiedBy =="" || data.modifiedBy==null ?  $("#editModifiedBy").val('NA'): $("#editModifiedBy").val(data.modifiedBy);
}

function updateSystem(){
var updateRequest = {
		"id" :  parseInt($("#EditId").val()),
 		"tag" : $("#editTag").val(),
		"description": $("#editdescription").val(),
		"remark": $("#editremarks").val(),
		"value": $("#editValue").val(),
		"type" : parseInt($("#edittype").val()),
		"userId":parseInt(userId),
		"featureId":parseInt(featureId),
		"userTypeId": parseInt($("body").attr("data-userTypeID")),
		"userType":$("body").attr("data-roleType"),
		"username" : $("body").attr("data-selected-username"),
		"userName" : $("body").attr("data-selected-username"),
		"roleType":$("body").attr("data-roleType"),
		
}
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$.ajaxSetup({
	headers:
	{ 'X-CSRF-TOKEN': token }
});

$.ajax({
	url : "./system/update",
	data :	JSON.stringify(updateRequest),
	dataType : 'json',
	contentType : 'application/json; charset=utf-8',
	type : 'PUT',
	success : function(data) {
			//console.log("Updated data---->" +JSON.stringify(data));
			
			$("#editAdminSystemModel").closeModal();	
			$("#confirmedUpdatedSystem").openModal({
		        dismissible:false
		    });
			$.i18n().locale = window.parent.$('#langlist').val();
			
			$.i18n().load({
				'en' : './resources/i18n/en.json',
				'km' : './resources/i18n/km.json'
			}).done(function() {
				//$('#updateFieldMessage').text($.i18n(data.tag));
				if(data.errorCode==200){
					//alert($.i18n('System_configuration_update'));
					$('#updateFieldMessage').text('');
					$('#updateFieldMessage').text($.i18n('System_configuration_update'));
				}else if(data.errorCode==201){
					$('#updateFieldMessage').text('');
					$('#updateFieldMessage').text($.i18n('System_configuration_failed'));
				}
			});
			
		
		
		//console.log("updateRequest---------->" +JSON.stringify(updateRequest));
		
	},
	error : function() {
		//alert("Failed");
	}
});

return false;
	
}

function confirmModel(){
$("#editAdminSystemModel").closeModal();
setTimeout(function(){$('#confirmedUpdatedSystem').openModal({
    dismissible:false
});},200);
}


function exportData(){
	var roleType = $("body").attr("data-roleType");
	var currentRoleType = $("body").attr("data-stolenselected-roleType");
	var table = $('#configLibraryTable').DataTable();
	var info = table.page.info(); 
	var pageNo=info.page;
	var pageSize =info.length;

	var filterRequest={
			"endDate":$('#endDate').val(),
			"startDate":$('#startDate').val(),
			"tag":$('#parametername').val(),
			"type" : parseInt($('#type').val()),
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"username" : $("body").attr("data-selected-username"),
			"pageNo":parseInt(pageNo),
			"pageSize":parseInt(pageSize),
			"userName" : $("body").attr("data-selected-username"),
			"roleType":$("body").attr("data-roleType"),
			"description":$('#descriptionID').val(),
			"value":$('#valueID').val(),
			
	}
	//console.log(JSON.stringify(filterRequest))
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: './exportSystemConfigData',
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


function filterResetSystemAdmin(formID){
	$('#'+formID).trigger('reset');
	$("label").removeClass('active');
	configManagementDatatable();
}




