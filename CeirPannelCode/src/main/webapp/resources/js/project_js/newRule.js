	var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});

		
		function getFeature(current){

			$.ajax({
				url: './getFeatureName?ruleName='+current.value,
				type: 'POST',
				processData: false,
				contentType: false,
				async : false,
				success: function (data, textStatus, jqXHR) {

					$('#Feature').empty();
					var html='<option value="">Feature Name</option>';
					$('#Feature').append(html);
			    	for (i = 0; i < data.length; i++) {
			    		$('<option>').val(data[i]).text(data[i]).appendTo('#Feature,#editFeature');
			    	}
					
				
				
				},
				error: function (jqXHR, textStatus, errorThrown) {

				}
			});

		}
		
$.getJSON('./registrationUserType?type=2', function(data) {
	for (i = 0; i < data.length; i++) {
		$('<option>').val(data[i].usertypeName).text(data[i].usertypeName)
		.appendTo('#User');
	}
});
$.getJSON('./ruleName', function(data) {
	for (i = 0; i < data.length; i++) {
		$('<option>').val(data[i].name).text(data[i].name)
		.appendTo('#Rule');
	}
});

/*$.getJSON('./getDropdownList/PERIOD_ACTION', function(data) {
	for (i = 0; i < data.length; i++) {
		$('<option>').val(data[i].interp).text(data[i].interp)
		.appendTo('#GracePeriod,#PostGracePeriod');
	}
});
*/


function getGrace(current){
	var rule=$('#Rule').val();
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


$.getJSON('./getDropdownList/MOVE_TO_NEXT', function(data) {
	for (i = 0; i < data.length; i++) {
		$('<option>').val(data[i].interp).text(data[i].interp)
		.appendTo('#MoveToGracePeriod,#MoveToPostGracePeriod');
	}
});


function save(){
	var name=$('#editName').val();
	var description=$('#editDescription').val();
	var state=$('#editState').val();
	var newRule={
			"failedRuleActionGrace": $('#MoveToGracePeriod').val(),
			"failedRuleActionPostGrace":$('#MoveToPostGracePeriod').val(),
			"feature": $('#Feature').val(),
			"graceAction": $('#GracePeriod').val(),
			"name": $('#Rule').val(),
			"postGraceAction": $('#PostGracePeriod').val(),
			"ruleOrder":parseInt($('#order').val()),
			"userType":$('#User').val(),
			"output": $('#output').val(),
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

		url : "./save",
		data : JSON.stringify(newRule),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		type : 'POST',
		success: function (data, textStatus, jqXHR) {
			$("#consignmentSubbmitButton").prop('disabled', true);
			$("#successModal").openModal({
				dismissible:false
			});


		},
		error: function (jqXHR, textStatus, errorThrown) {

		}
	});

	return false;
}