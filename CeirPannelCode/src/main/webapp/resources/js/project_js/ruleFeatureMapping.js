
var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';


$.i18n().locale = lang;	

$.i18n().load( {
	'en': './resources/i18n/en.json',
	'km': './resources/i18n/km.json'
} ).done( function() { 
});

$(window).load(function(){
	$('div#initialloader').fadeIn('fast');
	filter(lang);
	pageRendering();
});



function filter(lang)
{       	
	table('./headers?lang='+lang+'&type=ruleFeatureMapping','./ruleFeatureMappingListData');
}

//**************************************************filter table**********************************************

function table(url,dataUrl){
	var Feature=  $("#Feature").val() =='null' ? null : $("#Feature").val();
	var Rule= $("#Rule").val() =='null' ? null : $("#Rule").val();
	var User= $("#User").val() =='null' ? null : $("#User").val();



	var filterRequest={
			
			"featureName": Feature,

			"ruleName": Rule,

			"userType": User,
			"userId":parseInt($("body").attr("data-userID")),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userName":$("body").attr("data-username"),
			"roleType":$("body").attr("data-roleType")
	}
	if(lang=='km'){
		var langFile='./resources/i18n/khmer_datatable.json';
	}
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: url,
		/*	headers: {"Accept-Language": "en"},*/
		type: 'POST',
		dataType: "json",
		success: function(result){
			var table=	$("#table").DataTable({
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
					url : dataUrl,
					type: 'POST',
					dataType: "json",
					data : function(d) {
						d.filter = JSON.stringify(filterRequest); 

					}
				},
				"columns": result
			});

			$('div#initialloader').delay(300).fadeOut('slow');
	
		},
		error: function (jqXHR, textStatus, errorThrown) {

		}
	});
}



function pageRendering(){
	pageButtons('./ruleFeatureMapping/pageRendering');

}


function pageButtons(Url){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: Url,
		type: 'POST',
		dataType: "json",
		success: function(data){
			data.userStatus == "Disable" ? $('#btnLink').addClass( "eventNone" ) : $('#btnLink').removeClass( "eventNone" );

			var elem='<p class="PageHeading">'+data.pageTitle+'</p>';		
			$("#pageHeader").append(elem);
			var button=data.buttonList;
			/*			var date=data.inputTypeDateList;
					for(i=0; i<date.length; i++){
						if(date[i].type === "date"){
							$("#FieldTableDiv").append("<div class='input-field col s6 m2'>"+
									"<div id='enddatepicker' class='input-group'>"+
									"<input class='form-control datepicker' type='text' id="+date[i].id+" autocomplete='off' onchange='checkDate(startDate,endDate)'>"+
									"<label for="+date[i].id+">"+date[i].title
									+"</label>"+
									"<span	class='input-group-addon' style='color: #ff4081'>"+
									"<i	class='fa fa-calendar' aria-hidden='true' style='float: right; margin-top: -37px;'>"+"</i>"+"</span>");
							$( "#"+date[i].id ).datepicker({
								dateFormat: "yy-mm-dd",
								 maxDate: new Date()
					        });
						}else if(date[i].type === "text"){
							$("#FieldTableDiv").append("<div class='input-field col s6 m2' ><input type="+date[i].type+" id="+date[i].id+" maxlength='19' /><label for="+date[i].id+" class='center-align'>"+date[i].title+"</label></div>");
						}

					} */

			// dynamic dropdown portion
			var dropdown=data.dropdownList;
			for(i=0; i<dropdown.length; i++){
				var dropdownDiv=
					$("#FieldTableDiv").append("<div class='col s6 m2 selectDropdwn'>"+

							"<div class='select-wrapper select2  initialized'>"+
							"<span class='caret'>"+"</span>"+
							"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

							"<select id="+dropdown[i].id+" class='select2 initialized'>"+
							"<option value='null' selected>"+dropdown[i].title+
							"</option>"+
							"</select>"+
							"</div>"+
					"</div>");
			}

			$("#FieldTableDiv").append("<div class=' col s3 m2 l1'><button type='button' class='btn primary botton' id='submitFilter'/></div>");

			for(i=0; i<button.length; i++){
				$('#'+button[i].id).text(button[i].buttonTitle);
				if(button[i].type === "HeaderButton"){
					$('#'+button[i].id).attr("href", button[i].buttonURL);
				}
				else{
					$('#'+button[i].id).attr("onclick", button[i].buttonURL);
				}
			}


			$.getJSON('./getAllfeatures', function(data) {
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].name).text(data[i].name).appendTo('#Feature');
				}
			});
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			
			
			$.getJSON('./registrationUserType?type=2', function(data) {
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].usertypeName).text(data[i].usertypeName)
					.appendTo('#User,#editUser');
				}
			});
			
			$.getJSON('./ruleName', function(data) {
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].name).text(data[i].name)
					.appendTo('#Rule,#editRule');
				}
			});

		/*	$.getJSON('./getDropdownList/PERIOD_ACTION', function(data) {
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].interp).text(data[i].interp)
					.appendTo('#GracePeriod,#PostGracePeriod');
				}
			});
*/

			$.getJSON('./getDropdownList/MOVE_TO_NEXT', function(data) {
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].interp).text(data[i].interp)
					.appendTo('#MoveToGracePeriod,#MoveToPostGracePeriod');
				}
			});



		}
	}); 



}




function getDetailBy(id){

	window.xid=id;
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url : "./getBy/"+id,
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'GET',
		success : function(data) {
			var result=JSON.stringify(data);
			$("#editModel").openModal({
				dismissible:false
			});

			setData(JSON.parse(result));
		},
		error : function() {
			//alert("Failed");
		}
	});	
}


function setData(result){
console.log("result is : "+JSON.stringify(result))
	$("#editRule").val(result.name).change();
	
	$("#editUser").val(result.userType);
	$("#order").val(result.ruleOrder);
	$("#GracePeriod").val(result.graceAction);
	$("#PostGracePeriod").val(result.postGraceAction);
	$("#MoveToGracePeriod").val(result.failedRuleActionGrace);
	$("#MoveToPostGracePeriod").val(result.failedRuleActionPostGrace);
	$("#editOutput").val(result.output);
	
	
	
	var rule=$('#editRule').val();
	var feature=$('#editFeature').val();
	$.ajax({
		url: './ruleFeatureActionMapping?&ruleName='+rule+'&featureName='+feature,
		type: 'POST',
		processData: false,
		contentType: false,
		async : false,
		success: function (data, textStatus, jqXHR) {
			$('#GracePeriod,#PostGracePeriod').empty();
			
	    for (i = 0; i < data.length; i++) {
	    		$('<option>').val(data[i].actions).text(data[i].actions).appendTo('#GracePeriod,#PostGracePeriod');
	    	}
		},
		error: function (jqXHR, textStatus, errorThrown) {

		}
	});

	$.ajax({
		url: './getFeatureName?ruleName='+rule,
		type: 'POST',
		processData: false,
		contentType: false,
		async : false,
		success: function (data, textStatus, jqXHR) {

			$('#editFeature').empty();
			var html='<option value="">Feature Name</option>';
			$('#editFeature').append(html);
	    	for (i = 0; i < data.length; i++) {
	    		$('<option>').val(data[i]).text(data[i]).appendTo('#editFeature');
	    	}
			
	    	$("#editFeature").val(result.feature).change();
		
		},
		error: function (jqXHR, textStatus, errorThrown) {

		}
	});

}








function update(){

	var name=$('#editName').val();
	var description=$('#editDescription').val();
	var state=$('#editState').val();
	var newRule={
			"failedRuleActionGrace": $("#MoveToGracePeriod").val(),
			"failedRuleActionPostGrace": $("#MoveToPostGracePeriod").val(),
			"feature": $("#editFeature").val(),
			"graceAction": $("#GracePeriod").val(),
			"id": parseInt(window.xid),
			"name": $("#editRule").val(),
			"postGraceAction": $("#PostGracePeriod").val(),
			"ruleOrder":parseInt($("#order").val()),
			"userType": $("#editUser").val(),
			"output":  $("#editOutput").val(),
			"userId":parseInt($("body").attr("data-userID")),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userName":$("body").attr("data-username"),
			"roleType":$("body").attr("data-roleType")
	}	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({

		url : "./updateRuleMapping",
		data : JSON.stringify(newRule),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		success: function (data, textStatus, jqXHR) {
			$('#editModel').closeModal();
			$("#updateFieldsSuccess").openModal({
				dismissible:false
			});

		},
		error: function (jqXHR, textStatus, errorThrown) {

		}
	});

	return false;
}



function DeleteByID(id){
	window.id=id;

	$("#DeleteFieldModal").openModal({
		dismissible:false
	});

}
function deleteModal(){
	var newRule ={
			"id" : parseInt(window.id),
			"userId":parseInt($("body").attr("data-userID")),
			"featureId":parseInt(featureId),
			"userTypeId": parseInt($("body").attr("data-userTypeID")),
			"userName":$("body").attr("data-username"),
			"roleType":$("body").attr("data-roleType")
	}
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url : "./deleteRuleMapping",
		data : JSON.stringify(newRule),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		success : function(data, textStatus, xhr) {
			$("#DeleteFieldModal").closeModal();
			$("#closeDeleteModal").openModal({
				dismissible:false
			});
		},
		error : function() {

		}
	});


	return false;


}












function getGrace(current){
	var rule=$('#editRule').val();
	$.ajax({
		url: './ruleFeatureActionMapping?&ruleName='+rule+'&featureName='+current.value,
		type: 'POST',
		processData: false,
		contentType: false,
		async : false,
		success: function (data, textStatus, jqXHR) {
			$('#GracePeriod,#PostGracePeriod').empty();
			
	    for (i = 0; i < data.length; i++) {
	    		$('<option>').val(data[i].actions).text(data[i].actions).appendTo('#GracePeriod,#PostGracePeriod');
	    	}
		},
		error: function (jqXHR, textStatus, errorThrown) {

		}
	});

}




function getFeature(current){

	$.ajax({
		url: './getFeatureName?ruleName='+current.value,
		type: 'POST',
		processData: false,
		contentType: false,
		async : false,
		success: function (data, textStatus, jqXHR) {
			$('#editFeature').empty();
			var html='<option value="">Feature Name</option>';
			$('#editFeature').append(html);
	    	for (i = 0; i < data.length; i++) {
	    		$('<option>').val(data[i]).text(data[i]).appendTo('#editFeature');
	    	}
			
		
		
		},
		error: function (jqXHR, textStatus, errorThrown) {

		}
	});

}
