var featureId = 15;
var roleType = $("body").attr("data-roleType");
var userId = $("body").attr("data-userID");
var currentRoleType = $("body").attr("data-selected-roleType"); 
var startdate=$('#startDate').val(); 
var endDate=$('#endDate').val();

$(document).ready(function(){
	messageManagementDatatable();
	pageRendering();
	
});

var role = currentRoleType == null ? roleType : currentRoleType;


//**************************************************Message Detail table**********************************************

function messageManagementDatatable(){
	var feature=  $("#feature").val() == "" || $("#feature").val() == undefined ? null : $("#feature").val();
	var filterRequest={
			"endDate":$('#endDate').val(),
			"startDate":$('#startDate').val(),
			"tag":$('#parametername').val(),
			"channel" : parseInt($('#channel').val()),
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"userName" : $("body").attr("data-selected-username"),
			"username" : $("body").attr("data-selected-username"),
			"roleType":$("body").attr("data-roleType"),
			"featureName" : feature
	}
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: 'headers?type=adminSystemMessage',
		type: 'POST',
		dataType: "json",
		success: function(result){
			/*////console.log("Url-------" +url+"--------"+ "dataUrl-------" +dataUrl);*/
			var table=	$("#messageLibraryTable").removeAttr('width').DataTable({
				destroy:true,
				"serverSide": true,
				orderCellsTop : true,
				"ordering" : false,
				"bPaginate" : true,
				"bFilter" : true,
				"bInfo" : true,
				"bSearchable" : true,
				"oLanguage": {
					"sEmptyTable": "No records found in the system"
			    },
				initComplete: function() {
			 		$('.dataTables_filter input')
   .off().on('keyup', function(event) {
	   if (event.keyCode === 13) {
			 table.search(this.value.trim(), false, false).draw();
		}
      
   });
   },
				ajax: {
					url : 'adminMessageData',
					type: 'POST',
					dataType: "json",
					data : function(d) {
						d.filter = JSON.stringify(filterRequest); 
						//////console.log(JSON.stringify(filterRequest));
					}

				},
				"columns": result,
				fixedColumns: true,
				columnDefs: [
		            { width: 100, targets: result.length - 1 },
		            { width: 125, targets: 0 },
		            { width: 125, targets: 1 }

		        ]
			});
			$('div#initialloader').delay(300).fadeOut('slow');
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			//////console.log("error in ajax");
		}
	});
}



//**************************************************MessageManagement page buttons**********************************************

function pageRendering(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: 'messageManagement/pageRendering',
		type: 'POST',
		dataType: "json",
		success: function(data){
			var elem='<p class="PageHeading">'+data.pageTitle+'</p>';		
			$("#pageHeader").append(elem);
			var button=data.buttonList;

			var date=data.inputTypeDateList;
			for(i=0; i<date.length; i++){
				if(date[i].type === "text"){
					$("#messageTableDiv").append("<div class='input-field col s6 m2' style='margin-top: 22px;'><input type="+date[i].type+" id="+date[i].id+"><label for='parametername' class='center-align'>"+date[i].title+"</label></div>");
				}
				
			} 
			
			// dynamic dropdown portion
			var dropdown=data.dropdownList;
			for(i=0; i<dropdown.length; i++){
				var dropdownDiv=
					$("#messageTableDiv").append("<div class='col s6 m2 l2 selectDropdwn'>"+
							"<div class='select-wrapper select2 form-control boxBorder boxHeight initialized'>"+
							"<span class='caret'>"+"</span>"+
							"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

							"<select id="+dropdown[i].id+" class='select2 form-control boxBorder boxHeight initialized'>"+
							"<option value='' selected>"+dropdown[i].title+
							"</option>"+
							"</select>"+
							"</div>"+
					"</div>");
			}
			
			
			$("#messageTableDiv").append("<div class='col s12 m2 l2'><button class='btn primary botton' type='button' id='submitFilter'></button></div>");
			$("#messageTableDiv").append("<div class=' col s3 m1'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportData()'>Export<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");
			for(i=0; i<button.length; i++){
				$('#'+button[i].id).text(button[i].buttonTitle);
				$('#'+button[i].id).attr("onclick", button[i].buttonURL);
			
			}
			
			$('.datepicker').datepicker({
				dateFormat: "yy-mm-dd"
				});
		}

	}); 
	setAllDropdown();
};

function setAllDropdown(){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	//status-----------dropdown
	$.getJSON('./getDropdownList/CHANNEL', function(data) {
		for (i = 0; i < data.length; i++) {
			$('<option>').val(data[i].value).text(data[i].interp)
			.appendTo('#channel');
		}
	});
	
	$.getJSON('./getAllfeatures', function(data) {
		for (i = 0; i < data.length; i++) {
		$('<option>').val(data[i].name).text(data[i].name).appendTo('#feature');
		}
	});
}


$('.datepicker').on('mousedown',function(event){
	event.preventDefault();
});



function viewDetails(tag){
	$("#viewMessageModel").openModal({
        dismissible:false
    });
	var RequestData = {
			"tag" : tag,
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"userName" : $("body").attr("data-selected-username"),
			"username" : $("body").attr("data-selected-username")
	} 
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url : "./message/viewTag",
		data :	JSON.stringify(RequestData),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		success : function(data) {
			//////console.log(data);
			setViewPopupData(data);
		},
		error : function() {
			//alert("Failed");
		}
	});
}

function setViewPopupData(data){
	data.tag=="" || data.tag==null ? $("#viewTag").val('NA') : $("#viewTag").val(data.tag);
	data.value=="" || data.value==null ? $("#viewValue").val('NA') : $("#viewValue").val(data.value);
	data.description=="" || data.description==null ? $("#description").val('NA') : $("#description").val(data.description);
	data.channelInterp=="" || data.channelInterp==null ? $("#viewChannel").val('NA') : $('#viewChannel').val(data.channelInterp);
	
	$("label[for='viewTag']").addClass('active');
	$("label[for='viewValue']").addClass('active');
	$("label[for='description']").addClass('active');
	$("label[for='viewChannel']").addClass('active');
	

}

function updateDetails(tag){
	$("#editMessageModel").openModal({
        dismissible:false
    });
	
	var RequestData = {
			"tag" : tag,
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"userName" : $("body").attr("data-selected-username"),
			"username" : $("body").attr("data-selected-username")
	} 
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url : "./message/viewTag",
		data :	JSON.stringify(RequestData),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		success : function(data) {
			//////console.log(data);
			setEditData(data);
		},
		error : function() {
			//alert("Failed");
		}
	});
}

function setEditData(data){
	$("#Edittag").val(data.tag);
	$("#EditId").val(data.id);
	$("#editValue").val(data.value);
	$("#editdescription").val(data.description);
	$("#editChannel").val(data.channelInterp);
}


function updateMessage(){
	 var updateRequest = {
			 "id" :  parseInt($("#EditId").val()),
		 	 "tag" : $("#Edittag").val(),
			 "value" : $("#editValue").val(),
			 "description" : $("#editdescription").val(),
			 "channel" : parseInt($("#editChannel").val()),
			 "userId":parseInt(userId),
			 "featureId":parseInt(featureId),
			 "userTypeId": parseInt($("body").attr("data-userTypeID")),
			 "userType":$("body").attr("data-roleType"),
			 "userName" : $("body").attr("data-selected-username"),
			"roleType":$("body").attr("data-roleType"),
			"username" : $("body").attr("data-selected-username")
	}
	 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});
	 $.ajax({
			url : "./message/update",
			data :	JSON.stringify(updateRequest),
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			type : 'PUT',
			success : function(data) {
				//////console.log("updateRequest---------->" +JSON.stringify(updateRequest));
				confirmModel()
			},
			error : function() {
				//alert("Failed");
			}
		});
	 return false;
}


function confirmModel(){
	$("#editMessageModel").closeModal();
	setTimeout(function(){$('#confirmedUpdatedMessage').openModal({
        dismissible:false
    });},200);
}

function exportData(){
	var roleType = $("body").attr("data-roleType");
	var currentRoleType = $("body").attr("data-stolenselected-roleType");
	var table = $('#messageLibraryTable').DataTable();
	var info = table.page.info(); 
	var pageNo=info.page;
	var pageSize =info.length;
	var feature=  $("#feature").val() == "" || $("#feature").val() == undefined ? null : $("#feature").val();

	var filterRequest={
			"endDate":$('#endDate').val(),
			"startDate":$('#startDate').val(),
			"tag":$('#parametername').val(),
			"channel" : parseInt($('#channel').val()),
			"userId":parseInt(userId),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userType":$("body").attr("data-roleType"),
			"userName" : $("body").attr("data-selected-username"),
			"featureName" : feature,
			"pageNo":parseInt(pageNo),
			"pageSize":parseInt(pageSize)
			
	}
	//////console.log(JSON.stringify(filterRequest))
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: './exportMessageConfigData',
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

