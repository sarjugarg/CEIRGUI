		var cierRoletype =$("body").attr("data-roleType");	
		var startdate=$('#startDate').val(); 
		var endDate=$('#endDate').val();
		var taxStatus=$('#taxPaidStatus').val();
		var txnId=$('#transactionID').val();
		var consignmentStatus=$('#filterConsignmentStatus').val();
		var userId = $("body").attr("data-userID");
		var userType=$("body").attr("data-roleType");
		var featureId="23";
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
			filterFieldTable(lang);
			sessionStorage.removeItem("session-value");
			pageRendering();
			
		 });
		

		$('.datepick').datepicker({
			dateFormat: "yy-mm-dd"
		});


		var sourceType =localStorage.getItem("sourceType");
		var TagId = sessionStorage.getItem("tagId");
		
		
		//**************************************************filter table**********************************************
		
		function filterFieldTable(lang){
			window.tag_val= $('#filterTagId').val() == undefined ? TagId : $('#filterTagId').val();
			var filterRequest={
					"endDate":$('#endDate').val(),
					"startDate":$('#startDate').val(),
					"userId":parseInt(userId),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"tag": window.tag_val,
					"userId":parseInt(userId),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username")
			}				
			if(lang=='km'){
				var langFile="./resources/i18n/khmer_datatable.json";
			}
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			
			$.ajax({
				url: 'headers?type=fieldManagement&lang='+lang,
				/*	headers: {"Accept-Language": "en"},*/
				type: 'POST',
				dataType: "json",
				success: function(result){
					var table=	$("#fieldManagementLibraryTable").DataTable({
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
							url : 'fieldManagementData',
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
				url: 'fieldManagement/pageRendering',
				type: 'POST',
				dataType: "json",
				success: function(data){
					data.userStatus == "Disable" ? $('#btnLink').addClass( "eventNone" ) : $('#btnLink').removeClass( "eventNone" );
					
					var elem='<p class="PageHeading">'+data.pageTitle+'</p>';		
					$("#pageHeader").append(elem);
					var button=data.buttonList;
					var date=data.inputTypeDateList;
					/*for(i=0; i<date.length; i++){
						if(date[i].type === "date"){
							$("#FieldTableDiv").append("<div class='input-field col s6 m2'>"+
			[[						"<div id='enddatepicker' class='input-group'>"+
									"<input class='form-control datepicker' type='text' id="+date[i].id+" autocomplete='off' onchange='checkDate(startDate,endDate)'>"+
									"<label for="+date[i].id+">"+date[i].title
									+"</label>"+
									"<span	class='input-group-addon' style='color: #ff4081'>"+
									"<i	class='fa fa-calendar' aria-hidden='true' style='float: right; margin-top: -37px;'>"+"</i>"+"</span>");

						}else if(date[i].type === "text"){
							$("#FieldTableDiv").append("<div class='input-field col s6 m2' ><input type="+date[i].type+" id="+date[i].id+" maxlength='19' /><label for="+date[i].id+" class='center-align'>"+date[i].title+"</label></div>");
						}
					} 
				*/
					// dynamic dropdown portion
					var dropdown=data.dropdownList;
					for(i=0; i<dropdown.length; i++){
						var dropdownDiv=
							$("#FieldTableDiv").append("<div class='col s6 m2 selectDropdwn'>"+
								
									"<div class='select-wrapper select2  initialized'>"+
									"<span class='caret'>"+"</span>"+
									"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

									"<select id="+dropdown[i].id+" class='select2 initialized'>"+
									"<option value=null selected>"+dropdown[i].title+
									"</option>"+
									"</select>"+
									"</div>"+
							"</div>");
					}

						$("#FieldTableDiv").append("<div class=' col s3 m2 l1'><button type='button' class='btn primary botton' id='submitFilter'/></div>");
						//$("#FieldTableDiv").append("<div class=' col s3 m2 l7'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportConsignmentData()'>"+$.i18n('Export')+"<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");
						for(i=0; i<button.length; i++){
							$('#'+button[i].id).text(button[i].buttonTitle);
							$('#'+button[i].id).attr("onclick", button[i].buttonURL);
						}

					/*	for(i=0; i<button.length; i++){
							$('#'+button[i].id).text(button[i].buttonTitle);
							if(button[i].type === "HeaderButton"){
								$('#'+button[i].id).attr("onclick", button[i].buttonURL);
							}
							
						}*/

				
					
				$('.datepicker').datepicker({
						dateFormat: "yy-mm-dd"
					});
				}
			}); 
			
			
			setDropdown();
	}


		
		//**********************************************************Export Field file************************************************************************
		function exportConsignmentData()
		{
			var consignmentStartDate=$('#startDate').val();
			var consignmentEndDate=$('#endDate').val();
			var consignmentTxnId=$('#transactionID').val();
			var filterConsignmentStatus=parseInt($('#filterConsignmentStatus').val());
			var consignmentTaxPaidStatus=parseInt($('#taxPaidStatus').val());
			
			var table = $('#fieldManagementLibraryTable').DataTable();
			var info = table.page.info(); 
			var pageNo=info.page;
			var pageSize =info.length;
			window.location.href="./exportConsignmnet?consignmentStartDate="+consignmentStartDate+"&consignmentEndDate="+consignmentEndDate+"&consignmentTxnId="+consignmentTxnId+"&filterConsignmentStatus="+filterConsignmentStatus+"&consignmentTaxPaidStatus="+consignmentTaxPaidStatus+"&pageSize="+pageSize+"&pageNo="+pageNo;
		}

		
	function setDropdown(){
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
					$('<option>').val(result[i].tag).text(result[i].displayName).appendTo('#tag,#filterTagId,#Edittag');
				}
				
			},
			error: function (jqXHR, textStatus, errorThrown) {
				////console.log("error in ajax")
			}
		});	
	}

		function AddField(){
			$('#addTags').openModal({
		        dismissible:false
		    });
			$('#tag').val(window.tag_val);	
			var tagDropDown =  document.getElementById("tag");
			var displayName = tagDropDown.options[tagDropDown.selectedIndex].text;
			$('#displayName').val(displayName);	
		}
		
	
	/*----------------------------------- Save Field ----------------------------------------- */
		
	function submitTag(){
		
		var request={
					  "displayName" : $('#displayName').val(),		
					  "interp": $('#addInterp').val(), 
					  "tag":   $('#tag').val(),
					  "tagId": $('#tagId').val(),
					  "value": $('#addValue').val(),
					  "description" : $('#description').val(),
					  "userId":parseInt(userId),
					  "featureId":parseInt(featureId),
					  "userTypeId": parseInt($("body").attr("data-userTypeID")),
					  "userType":$("body").attr("data-roleType"),
					  "username" : $("body").attr("data-selected-username")
				}
		
		//////console.log("request------------->" +JSON.stringify(request))
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});
		$.ajax({
			url : './add-Field',
			data : JSON.stringify(request),
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			type : 'POST',
			success : function(data, textStatus, jqXHR) {
					////console.log(JSON.stringify(data));
					$("#confirmField").openModal({
				        dismissible:false
				    });
			},
			error : function(jqXHR, textStatus, errorThrown) {
				////console.log("error in ajax")
			}
		});
			
			return false
	}


  
	/*--------------------------------- Edit Model View -----------------------------------*/
	
	
	function FieldViewByID(tag,id){
			var Id = parseInt(id);
		
			var request ={
					  "id" : parseInt(Id),
					  "userId":parseInt(userId),
					  "userId":parseInt(userId),
					  "featureId":parseInt(featureId),
					  "userTypeId": parseInt($("body").attr("data-userTypeID")),
					  "userType":$("body").attr("data-roleType"),
					  "username" : $("body").attr("data-selected-username")
				}
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: './fieldViewByID',
				type: 'POST',
				data : JSON.stringify(request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				success: function (data, textStatus, jqXHR) {
						var result = data.data
						$("#editTags").openModal({
					        dismissible:false
					    });
						FieldEditPopupData(result);
						////console.log(result)
				},
				error: function (jqXHR, textStatus, errorThrown) {
					////console.log("error in ajax")
				}
			});	
		}
	
	
	function FieldEditPopupData(result){
		$("#editId").val(result.id);
		$("#Edittag").val(result.tag);
		$("#editdescription").val(result.description);
		$("#editInterp").val(result.interp);
		$("#editFieldId").val(result.tagId);
		$("#editdisplayName").val(result.displayName);
		
		$("label[for='editdescription']").addClass('active');
		$("label[for='editInterp']").addClass('active');
		$("label[for='editFieldId']").addClass('active');
		
	}
	
	
	/*---------------------------------- Update Field-------------------------------------*/
	
	
	function updatedTag(){
	
		var request ={ 
		  "description": $("#editdescription").val(),
		  "displayName": $("#editdisplayName").val(),
		  "id": parseInt($("#editId").val()),
		  "interp": $("#editInterp").val(),
		  "tag": $("#Edittag").val(),
		  "tagId": $("#editFieldId").val(),
		  "userId":parseInt(userId),
		  "featureId":parseInt(featureId),
		  "userTypeId": parseInt($("body").attr("data-userTypeID")),
		  "userType":$("body").attr("data-roleType"),
		  "username" : $("body").attr("data-selected-username")
		  //"value":$("#editValue").val()
		}
		
		//////console.log("request--->" +JSON.stringify(request))
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});
		$.ajax({
			url: './updateSystemTags',
			type: 'PUT',
			data : JSON.stringify(request),
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			success: function (data, textStatus, jqXHR) {
			
				////console.log("Updated data---->" +data)
				$("#editTags").closeModal();	
				$("#updateFieldsSuccess").openModal({
			        dismissible:false
			    });
				
			},
			error: function (jqXHR, textStatus, errorThrown) {
				////console.log("error in ajax")
			}
		});	
		
		return false
	}

	
	
  /*------------------------------------ Delete Field -----------------------------------*/
	
	
	function DeleteFieldRecord(id){
		$("#DeleteFieldModal").openModal({
	        dismissible:false
	    });
		$("#deleteFieldId").val(id);
		
	}	
	
	
	
	function confirmantiondelete(){
		var request ={
				"userId" : $("body").attr("data-userID"),
				"id"  : parseInt($("#deleteFieldId").val()),
				"userId":parseInt(userId),
				"featureId":parseInt(featureId),
				"userTypeId": parseInt($("body").attr("data-userTypeID")),
				"userType":$("body").attr("data-roleType"),
				"username" : $("body").attr("data-selected-username")
		}
		//////console.log(JSON.stringify(request));
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});
		
		$.ajax({
			url : './deleteField',
			data : JSON.stringify(request),
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			type : 'DELETE',
			success : function(data, textStatus, xhr) {
				////console.log(data);
				$("#DeleteFieldModal").closeModal();
				$("#closeDeleteModal").openModal({
			        dismissible:false
			    });
				
				$("#materialize-lean-overlay-3").css("display","none");
			},
			error : function() {
				////console.log("Error");
			}
		});
	}
	
	function resetFields(){
		$('#tagId').val("");
		$('#addInterp').val("");
		$('#description').val("");
		$("label[for='addFieldId']").removeClass('active');
		$("label[for='addInterp']").removeClass('active');
		$("label[for='description']").removeClass('active');
	}
