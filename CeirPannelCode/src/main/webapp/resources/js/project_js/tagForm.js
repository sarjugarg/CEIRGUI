/*
		window.parent.$('#langlist').on('change', function() {
			var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';
			window.location.assign("./uploadPaidStatus?lang="+lang);
		}); */
		
		
	$('#btnLink').css({"display":"none"});	
	var roleType = $("body").attr("data-roleType");
	var userId = $("body").attr("data-userID");
	var roleType = $("body").attr("data-roleType"); 


	
	
	function hide() {
		var tagId = $('#tagId').val();
		
		if(tagId.length == 0){
			//////console.log("please field input");
		}else{
			//sessionStorage.setItem("roleType",roleType);
		sessionStorage.setItem("tagId", tagId);
		window.location.replace("./fieldManagement?via=other&tagId="+tagId);
		}
	}
	
	
	
	var request ={
			  "userId" : parseInt($("body").attr("data-userID"))
		}
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers:
		{ 'X-CSRF-TOKEN': token }
	});

	
	$.ajax({
		url: './getSystemTags',
		type: 'POST',
		data : JSON.stringify(request),
		dataType : 'json',
		contentType : 'application/json; charset=utf-8',
		success: function (data, textStatus, jqXHR) {
			var result = data.data;
			for (i = 0; i < result.length; i++){
				$('<option>').val(result[i].tag).text(result[i].displayName).appendTo('#tagId');
			}
			
		},
		error: function (jqXHR, textStatus, errorThrown) {
			//////console.log("error in ajax")
		}
	});