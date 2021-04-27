		var cierRoletype =$("body").attr("data-roleType");	
		var startdate=$('#startDate').val(); 
		var endDate=$('#endDate').val();
		var taxStatus=$('#taxPaidStatus').val();
		var txnId=$('#transactionID').val();
		var consignmentStatus=$('#filterConsignmentStatus').val();
		var userId = $("body").attr("data-userID");
		var userType=$("body").attr("data-roleType");
		var featureId="31";
		var rejectedMsg,consignmentApproved,errorMsg,havingTxnID,updateMsg,hasBeenUpdated;
		var consignmentDeleted,deleteInProgress;
		var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';


		$.i18n().locale = lang;	
		
		$.i18n().load( {
			'en': './resources/i18n/en.json',
			'km': './resources/i18n/km.json'
		} ).done( function() { 
			rejectedMsg=$.i18n('rejectedMsg');
			consignmentApproved=$.i18n('consignmentApproved');
			errorMsg=$.i18n('errorMsg');
			havingTxnID=$.i18n('havingTxnID');
			updateMsg=$.i18n('updateMsg');
			hasBeenUpdated=$.i18n('hasBeenUpdated');
			consignmentDeleted=$.i18n('consignmentDeleted');
			deleteInProgress=$.i18n('deleteInProgress');
		});

         $(window).load(function(){
			$('div#initialloader').fadeIn('fast');
			alertFieldTable(lang);
			sessionStorage.removeItem("session-value");
			pageRendering();
			
		 });
		

		$('.datepick').datepicker({
			dateFormat: "yy-mm-dd"
		});


		var sourceType =localStorage.getItem("sourceType");
		var TagId = sessionStorage.getItem("tagId");
		
		
		//**************************************************filter table**********************************************
		
		function alertFieldTable(lang,source){
			var source__val;
			if(source == 'filter') {
				source__val= source;
				$("body").attr("data-session-source","filter");
			}
			else{
				source__val = $("body").attr("data-session-source");
				
			}
			var alertId = $("#alertId").val() == "-1" || $("#alertId").val() == undefined ? "" : $("#alertId option:selected").text();
			var feature = $("#filterfeature").val() == "-1" || $("#filterfeature").val() == undefined ? null : $("#filterfeature option:selected").text();
			var description = $("#description").val() == "" || $("#description").val() == undefined ? null : $("#description").val();
			var filterRequest={
					"endDate":$('#endDate').val(),
					"startDate":$('#startDate').val(),
					"userId":parseInt(userId),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username"),
					"alertId" : alertId,
					"feature" : feature,
					"description" : description
					
					
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
			$.ajax({
				url: 'headers?type=alertManagementHeaders&lang='+lang,
				/*	headers: {"Accept-Language": "en"},*/
				type: 'POST',
				dataType: "json",
				success: function(result){
					var table=	$("#alertManagementLibraryTable").removeAttr('width').DataTable({
						destroy:true,
						"serverSide": true,
						orderCellsTop : true,
						"ordering" : true,
						"bPaginate" : true,
						"bFilter" : false,
						"bInfo" : true,
						"bSearchable" : true,
						"oLanguage": {  
							"sUrl": langFile  
						},
						"aaSorting": [],
						columnDefs: [
							   { orderable: false, targets: -1 }
							],
						initComplete: function() {
					 		$('.dataTables_filter input')
	       .off().on('keyup', function(event) {
	    	   if (event.keyCode === 13) {
	    			 table.search(this.value.trim(), false, false).draw();
	    		}
	          
	       });
		   },
						ajax: {
							url : 'alertManagementData?source='+source__val,
							type: 'POST',
							dataType: "json",
							data : function(d) {
								d.filter = JSON.stringify(filterRequest); 

							},error: function (jqXHR, textStatus, errorThrown,data) {
								
								 window.parent.$('#msgDialog').text($.i18n('500ErrorMsg'));
								 // messageWindow(jqXHR['responseJSON']['message']);
								 window.parent.$('#500ErrorModal').openModal({
								 dismissible:false
								 });
								
							}
						},
						"columns": result,
						fixedColumns: true,
						columnDefs: [
							{ width: 120, targets: 0 },
							{ width: 120, targets: 1 },
							{ orderable: false, targets: -1 }
						]
					});

					$('div#initialloader').delay(300).fadeOut('slow');
					
				},
				error: function (jqXHR, textStatus, errorThrown) {
					
				}
			});
			
		
		}

		$('.datepicker').on('mousedown',function(event){
			event.preventDefault();
		});



		function pageRendering(){
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: 'alertManagement/pageRendering',
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
							$("#alertTableDiv").append("<div class='input-field'>"+
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
						$("#alertTableDiv").append("<div class='input-field' ><input type="+date[i].type+" id="+date[i].id+" maxlength="+date[i].className+" /><label for="+date[i].id+" class='center-align'>"+date[i].title+"</label></div>");
					}
					else if(date[i].type === "select"){

							var dropdownDiv=
								$("#alertTableDiv").append("<div class='selectDropdwn'>"+
										
										"<div class='select-wrapper select2  initialized'>"+
										"<span class='caret'>"+"</span>"+
										"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

										"<select id="+date[i].id+" class='select2 initialized'>"+
										"<option value='-1' selected>"+date[i].title+
										"</option>"+
										"</select>"+
										"</div>"+
								"</div>");
						
						}
					}
				
				// dynamic dropdown portion
					/*var dropdown=data.dropdownList;
					for(i=0; i<dropdown.length; i++){
						var dropdownDiv=
							$("#alertTableDiv").append("<div class='col s6 m2 selectDropdwn'>"+
								
									"<div class='select-wrapper select2  initialized'>"+
									"<span class='caret'>"+"</span>"+
									"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

									"<select id="+dropdown[i].id+" class='select2 initialized'>"+
									"<option value='-1' selected>"+dropdown[i].title+
									"</option>"+
									"</select>"+
									"</div>"+
							"</div>");
					}*/
						var viewFilter="viewFilter";
						$("#alertTableDiv").append("<div class='filter_btn'><button type='button' class='btn primary botton' id='submitFilter'/></div>");
						$("#alertTableDiv").append("<div class='filter_btn'><button type='button'  class='btn primary botton' id='clearFilter'>"+$.i18n('clearFilter')+"</button></div>");
						$("#alertTableDiv").append("<div class='filter_btn'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportAlertData()'>"+$.i18n('Export')+"<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");
						$('#clearFilter').attr("onclick", "Resetfilter('viewFilter')");
						for(i=0; i<button.length; i++){
							$('#'+button[i].id).text(button[i].buttonTitle);
							$('#'+button[i].id).attr("onclick", button[i].buttonURL);
						}

				}
			}); 
			
			setAllDropdown();
		
	}
		
		function setAllDropdown(){
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.getJSON('./getAllAlerts', function(data) {
			for (i = 0; i < data.length; i++) {
			$('<option>').val(data[i].id).text(data[i].alertId).appendTo('#alertId');
			}
		});
			
			$.getJSON('./getAllfeatures', function(data) {
				for (i = 0; i < data.length; i++) {
				$('<option>').val(data[i].id).text(data[i].name).appendTo('#filterfeature');
				}
			});
			
		}	



		
		
	

		//**********************************************************Export Excel file************************************************************************
		function exportAlertData()
		{
			var alertId = $("#alertId").val() == "-1" || $("#alertId").val() == undefined ? "" : $("#alertId option:selected").text();
			var feature = $("#filterfeature").val() == "-1" || $("#filterfeature").val() == undefined ? null : $("#filterfeature option:selected").text();
			var description = $("#description").val() == "" || $("#description").val() == undefined ? null : $("#description").val();
			
			
			var roleType = $("body").attr("data-roleType");
			var currentRoleType = $("body").attr("data-stolenselected-roleType");
			var table = $('#alertManagementLibraryTable').DataTable();
			var info = table.page.info(); 
			var pageNo=info.page;
			var pageSize =info.length;
			
			var filterRequest={
					"endDate":$('#endDate').val(),
					"startDate":$('#startDate').val(),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"pageNo":parseInt(pageNo),
					"pageSize":parseInt(pageSize),
					"userId" : parseInt($("body").attr("data-userID")),
					"alertId" : alertId,
					"username" : $("body").attr("data-selected-username"),
					"feature" : feature,
					"description" : description
					
					
			}
			//////console.log(JSON.stringify(filterRequest))
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: './exportAlertData?source=ViewExport',
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
			
		
		function alertViewByID(id){
			
			var request = {
				"dataId" :  parseInt(id),	
				"featureId":parseInt(featureId),
				"userId" :  parseInt($("body").attr("data-userID")),
			   "userType":  $("body").attr("data-roleType"),
			 "userTypeId" : parseInt($("body").attr("data-userTypeID")),
			   "username" : $("body").attr("data-selected-username"),
			}
			
			//////console.log("request--------->" +JSON.stringify(request));
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
					url: './alertViewByID',
					type: 'POST',
					data : JSON.stringify(request),
					dataType : 'json',
					contentType : 'application/json; charset=utf-8',
					success: function (data, textStatus, jqXHR) {
							var result = data.data
							$("#editAlertModal").openModal({
						        dismissible:false
						    });
							AlertEditPopupData(result);
							//////console.log(result)
					},
					error: function (jqXHR, textStatus, errorThrown) {
						//////console.log("error in ajax")
					}
				});	
			}

		function AlertEditPopupData(result){
			$("#editAlertId").val(result.alertId);
			$("#editfeature").val(result.feature);
			$("#editdescription").val(result.description);
			$("#editId").val(result.id);
			
			$("label[for='editAlertId']").addClass('active');
			$("label[for='editfeature']").addClass('active');
			$("label[for='editdescription']").addClass('active');
			
		}
		
		
		/*---------------------------------- Update Field-------------------------------------*/
		
		
		function updatedAlert(){
			
			var request ={ 
					 "id" : parseInt($("#editId").val()),
					 "alertId":  $('#editAlertId').val(),
					 "description": $('#editdescription').val(),
					 "featureId": parseInt(featureId),
					 "userId" : parseInt($("body").attr("data-userID")),
					 "userType":$("body").attr("data-roleType"),
					 "userTypeId": parseInt($("body").attr("data-userTypeID")),
					 "username" : $("body").attr("data-selected-username"),
					 
			}
			
			//////console.log("request--->" +JSON.stringify(request))
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: './updateAlert',
				type: 'POST',
				data : JSON.stringify(request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				success: function (data, textStatus, jqXHR) {
				
					//////console.log("Updated data---->" +data)
					$("#editAlertModal").closeModal();	
					$("#updateAlertSuccess").openModal({
				        dismissible:false
				    });
					
				},
				error: function (jqXHR, textStatus, errorThrown) {
					//////console.log("error in ajax")
				}
			});	
			
			return false
		}
		
		function Resetfilter(formID){
			$('#'+formID).trigger('reset');
			$("label").removeClass('active');
			alertFieldTable(lang)
			
		}	
		

		function saveIPLog() {
		var obj = {
		username : $("body").attr("data-selected-username"),
		password : "",
		captcha : ""
		}

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");

		if(sessionStorage.getItem("isSessionActive") == "Y"){
		$.ajaxSetup({
		headers : {
		'X-CSRF-TOKEN' : token
		}
		});

		$.ajax({
		type : 'POST',
		url : contextpath + '/ipLogInfo',
		contentType : "application/json",
		data : JSON.stringify(obj),
		success : function(data) {
		// console.log("successfully saved");

		},
		error : function(xhr, ajaxOptions, thrownError) {

		}
		});
		}

		sessionStorage.removeItem("isSessionActive");
		}	
		