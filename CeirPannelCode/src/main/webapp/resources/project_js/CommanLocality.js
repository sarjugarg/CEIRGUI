
/*
function getDistrict(current,districtID,communeID) {
	var request = {
		"province" : current.value
	}
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers : {
			'X-CSRF-TOKEN' : token
		}
	});
	$.ajax({
				url : './getallDistrict',
				type : 'POST',
				data : JSON.stringify(request),
				dataType : 'json',
				async: false,
				contentType : 'application/json; charset=utf-8',
				success : function(data, textStatus, jqXHR) {
					var result = data;
					$("#"+districtID).empty();
					$("#"+communeID).empty();
					//var html='<option value="">Select District</option>';
					$('#'+districtID).append(html);	
					 for (i = 0; i < result.length; i++) {
						
						var html='<option value="'+result[i].id+'">'+result[i].district+'</option>';
						$('#'+districtID).append(html);	
					} 

				},
				error : function(jqXHR, textStatus, errorThrown) {
					//////console.log("error in ajax")
				}
			});
}




function getCommune(current,communeID,villageID) {
	var request = {
		"districtID" : parseInt(current.value)
	}
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers : {
			'X-CSRF-TOKEN' : token
		}
	});
	$.ajax({
				url : './getallCommune',
				type : 'POST',
				data : JSON.stringify(request),
				dataType : 'json',
				async: false,
				contentType : 'application/json; charset=utf-8',
				success : function(data, textStatus, jqXHR) {
					var result = data;
					$('#'+communeID).empty();
					$('#'+villageID).empty();
					var html='<option value="">Select Commune</option>';
					$('#'+communeID).append(html);	
					 for (i = 0; i < result.length; i++) {
						
						var html='<option value="'+result[i].id+'">'+result[i].commune+'</option>';
						$('#'+communeID).append(html);	
					} 

				},
				error : function(jqXHR, textStatus, errorThrown) {
					//////console.log("error in ajax")
				}
			});
}



function getVillage(current,villageID) {
	var request = {
		"communeID" : parseInt(current.value)
	}
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers : {
			'X-CSRF-TOKEN' : token
		}
	});
	$.ajax({
				url : './getallVillage',
				type : 'POST',
				data : JSON.stringify(request),
				async: false,
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				success : function(data, textStatus, jqXHR) {
					var result = data;
					$('#'+villageID).empty();
					var html='<option value="">Select Village</option>';
					$('#'+villageID).append(html);	
					 for (i = 0; i < result.length; i++) {
						
						var html='<option value="'+result[i].id+'">'+result[i].village+'</option>';
						$('#'+villageID).append(html);	
					} 

				},
				error : function(jqXHR, textStatus, errorThrown) {
					//////console.log("error in ajax")
				}
			});
}
*/


function getDistrict(current,districtID,communeID) {
var request = {
"province" : current.value
}
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$.ajaxSetup({
headers : {
'X-CSRF-TOKEN' : token
}
});
$.ajax({
url : './getallDistrict',
type : 'POST',
data : JSON.stringify(request),
dataType : 'json',
async: false,
contentType : 'application/json; charset=utf-8',
success : function(data, textStatus, jqXHR) {
var result = data;
//$('#district,#commune').empty();
$("#"+districtID).empty();
$("#"+communeID).empty();
var html='<option value="">Select Commune</option>';
$('#'+communeID).append(html);
var html='<option value="">Select District</option>';
$('#'+districtID).append(html);	
for (i = 0; i < result.length; i++) {

var html='<option value="'+result[i].id+'">'+result[i].district+'</option>';
$('#'+districtID).append(html);	
} 

},
error : function(jqXHR, textStatus, errorThrown) {
//////console.log("error in ajax")
}
});
}




function getCommune(current,communeID,villageID) {
var request = {
"districtID" : parseInt(current.value)
}
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$.ajaxSetup({
headers : {
'X-CSRF-TOKEN' : token
}
});
$.ajax({
url : './getallCommune',
type : 'POST',
data : JSON.stringify(request),
dataType : 'json',
async: false,
contentType : 'application/json; charset=utf-8',
success : function(data, textStatus, jqXHR) {
var result = data;
//$('#commune,#village').empty();
$('#'+communeID).empty();
$('#'+villageID).empty();
var html='<option value="">Select Village</option>';
$('#'+villageID).append(html);	
var html='<option value="">Select Commune</option>';
$('#'+communeID).append(html);	
for (i = 0; i < result.length; i++) {

var html='<option value="'+result[i].id+'">'+result[i].commune+'</option>';
$('#'+communeID).append(html);	
} 

},
error : function(jqXHR, textStatus, errorThrown) {
//////console.log("error in ajax")
}
});
}



function getVillage(current,villageID) {
var request = {
"communeID" : parseInt(current.value)
}
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$.ajaxSetup({
headers : {
'X-CSRF-TOKEN' : token
}
});
$.ajax({
url : './getallVillage',
type : 'POST',
data : JSON.stringify(request),
async: false,
dataType : 'json',
contentType : 'application/json; charset=utf-8',
success : function(data, textStatus, jqXHR) {
var result = data;
//$('#village').empty();
$('#'+villageID).empty();
var html='<option value="">Select Village</option>';
$('#'+villageID).append(html);	
for (i = 0; i < result.length; i++) {

var html='<option value="'+result[i].id+'">'+result[i].village+'</option>';
$('#'+villageID).append(html);	
} 

},
error : function(jqXHR, textStatus, errorThrown) {
//////console.log("error in ajax")
}
});
}