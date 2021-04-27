/*
		window.parent.$('#langlist').on('change', function() {
			var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';
			window.location.assign("./uploadPaidStatus?lang="+lang);
		}); */
		
		
	$('#btnLink').css({"display":"none"});	
	
	var userId = $("body").attr("data-userID");
	var userType = $("body").attr("data-roleType"); 
	var featureId = 39;

	
	
	function hide() {
		var tableName = $('#tableId').val();
		
		if(tableName.length == 0){
			//////console.log("please field input");
		}else{
			//sessionStorage.setItem("roleType",roleType);
		sessionStorage.setItem("tableName", tableName);
		window.location.replace("./dbTables?via=other&tableName="+tableName);
		}
	}
	
	
	var dbName = "ceirconfig" ;
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});
	$.ajax({
		url: './getallTables?dbName='+dbName+'&featureId='+featureId+'&userId='+userId+'&userType='+userType,
		type: 'POST',
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		success: function (data, textStatus, jqXHR) {
			var result = data.tableNames;
			for (i = 0; i < result.length; i++){
				$('<option>').val(result[i]).text(result[i]).appendTo('#tableId');
			}
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			//////console.log("error in ajax")
		}
	});