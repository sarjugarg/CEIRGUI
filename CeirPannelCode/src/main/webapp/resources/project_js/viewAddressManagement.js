		var cierRoletype =$("body").attr("data-roleType");	
		var startdate=$('#startDate').val(); 
		var endDate=$('#endDate').val();
		var taxStatus=$('#taxPaidStatus').val();
		var txnId=$('#transactionID').val();
		var consignmentStatus=$('#filterConsignmentStatus').val();
		var userId = $("body").attr("data-userID");
		var userType=$("body").attr("data-roleType");
		var featureId="53";
		var rejectedMsg,consignmentApproved,errorMsg,havingTxnID,updateMsg,hasBeenUpdated;
		var consignmentDeleted,deleteInProgress;
		var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';


		
		
		

         $(window).load(function(){
			$('div#initialloader').fadeIn('fast');
			addressFieldTable(lang);
			sessionStorage.removeItem("session-value");
			pageRendering();
			
			
		 });
		

		$('.datepick').datepicker({
			dateFormat: "yy-mm-dd"
		});


		var sourceType =localStorage.getItem("sourceType");
		var TagId = sessionStorage.getItem("tagId");
		
		
		//**************************************************filter table**********************************************
		
		function addressFieldTable(lang){
			
			var filterDistrict = $('#district').val() =="" || $('#district').val() == undefined ? null : $("#district option:selected").text();
			var filterCommune = $('#commune').val() =="" || $('#commune').val() == undefined ? null : $("#commune option:selected").text();
			var filterVillage = $('#village').val() ==""|| $('#village').val() == undefined ? null : $("#village option:selected").text();
			
			var filterRequest={
					"startDate": $('#startDate').val(),
					"endDate":  $('#endDate').val(),
					"province" : $("#proviance").val(),
					"district" : filterDistrict,
					"commune" :  filterCommune,
					"village" :  filterVillage,
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
				url: 'headers?type=systemAddressHeaders&lang='+lang,
				/*	headers: {"Accept-Language": "en"},*/
				type: 'POST',
				dataType: "json",
				success: function(result){
					var table=	$("#AddressManagementLibraryTable").DataTable({
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
							url : 'addressManagementData',
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
				url: 'addressManagement/pageRendering',
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
							$("#AddressTableDiv").append("<div class='input-field'>"+
									"<div id='enddatepicker' class='input-group'>"+
									"<input class='form-control datepicker' type='text' id="+date[i].id+" autocomplete='off'>"+
									"<label for="+date[i].id+">"+date[i].title
									+"</label>"+
									"<span	class='input-group-addon' style='color: #ff4081'>"+
									"<i	class='fa fa-calendar' aria-hidden='true' style='float: right; margin-top: -37px;'>"+"</i>"+"</span>");
							$( "#"+date[i].id ).datepicker({
								dateFormat: "yy-mm-dd",
								 maxDate: new Date()
					        });
						}else if(date[i].type === "text"){
							$("#AddressTableDiv").append("<div class='input-field'><input type="+date[i].type+" maxlength="+date[i].className+" id="+date[i].id+"><label for='parametername' class='center-align'>"+date[i].title+"</label></div>");
							
						}
						else if(date[i].type === "select"){

							var dropdownDiv=
								$("#AddressTableDiv").append("<div class='selectDropdwn'>"+
										
										"<div class='select-wrapper select2  initialized'>"+
										"<span class='caret'>"+"</span>"+
										"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+
										"<select id="+date[i].id+" class='select2 initialized'>"+
										"<option value='' >"+date[i].title+
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
							$("#AddressTableDiv").append("<div class='col s6 m2 selectDropdwn'>"+
								
									"<div class='select-wrapper select2  initialized'>"+
									"<span class='caret'>"+"</span>"+
									"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

									"<select id="+dropdown[i].id+" class='select2 initialized'>"+
									"<option value='' selected>"+dropdown[i].title+
									"</option>"+
									"</select>"+
									"</div>"+
							"</div>");
					}*/

						$("#AddressTableDiv").append("<div class='filter_btn'><button type='button' class='btn primary botton' id='submitFilter'/></div>");
						$("#AddressTableDiv").append("<div class='filter_btn'><button type='button'  class='btn primary botton' id='clearFilter'>Clear all filters</button></div>");
						$("#AddressTableDiv").append("<div class='filter_btn'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportAddressData()'>"+$.i18n('Export')+"<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");
						$('#clearFilter').attr("onclick", "filterResetAddressManagement('viewFilter')");
						for(i=0; i<button.length; i++){
							$('#'+button[i].id).text(button[i].buttonTitle);
							$('#'+button[i].id).attr("onclick", button[i].buttonURL);
						}

				}
			}); 
			
			//populateCountries( "country",    "state");
	        //$("#country").val("Cambodia");
			setDropdown();
	   }

	

		
	function setDropdown(){
		$.getJSON('./getAllProvince', function(data) {
			for (i = 0; i < data.length; i++) {
				$('<option>').val(data[i].province).text(data[i].province)
				.appendTo('#proviance','#provinceForCommune');
			}
		});
		
	}
	
	$(document).on('change', '#proviance', function(){
		getAllDistrict($('#proviance').val());
		
	});	
	
	$(document).on('change', '#provinceForCommune', function(){
		 getmodelDistrict();
			//getCommune();
	});	
	
	
	$(document).on('change', '#provinceForVillage', function(){
		getDistrictForVillage();
			//getCommune();
	});	
	
	

	
	function getAllDistrict(current) {
		var request = {
				 "province" : $("#proviance").val()
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
						$('#district').empty();
						var html='<option value="">Select District</option>';
						$('#district').append(html);	
						 for (i = 0; i < result.length; i++) {
							
							var html='<option value="'+result[i].id+'">'+result[i].district+'</option>';
							$('#district').append(html);	
						} 

					},
					error : function(jqXHR, textStatus, errorThrown) {
						//////console.log("error in ajax")
					}
				});
	}
	
	$(document).on('change', '#district', function(){
		getCommune();
		//getCommune();
	});	
	
	function getCommune() {
		var request = {
			"districtID" : parseInt($('#district').val())
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
						$('#commune').empty();
						var html='<option value="">Select Commune</option>';
						$('#commune').append(html);	
						 for (i = 0; i < result.length; i++) {
							
							var html='<option value="'+result[i].id+'">'+result[i].commune+'</option>';
							$('#commune').append(html);	
						} 

					},
					error : function(jqXHR, textStatus, errorThrown) {
						//////console.log("error in ajax")
					}
				});
	}
	
	$(document).on('change', '#commune', function(){
		getVillage();
		
	});	
	

	
	function getVillage() {
		var request = {
			"communeID" : parseInt($('#commune').val())
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
						$('#village').empty();
						var html='<option value="">Select Village</option>';
						$('#village').append(html);	
						 for (i = 0; i < result.length; i++) {
							
							var html='<option value="'+result[i].id+'">'+result[i].village+'</option>';
							$('#village').append(html);	
						} 

					},
					error : function(jqXHR, textStatus, errorThrown) {
						//////console.log("error in ajax")
					}
				});
	}
		function AddAddress(){
			$('#addAddressModal').openModal({
		        dismissible:false
		    });
			
		}
		
		 function resetButtons(){
			 $('input[name=group1]').attr('checked',false);
			 $('input[name=group2]').attr('checked',false);
			 $('input[name=group3]').attr('checked',false);
			 $('input[name=group4]').attr('checked',false);
		 }
	
	

	function exportAddressData(){
		var table = $('#AddressManagementLibraryTable').DataTable();
		var info = table.page.info(); 
		var pageNo=info.page;
		var pageSize =info.length;
		var filterDistrict = $('#district').val() == ""|| $('#district').val() == undefined ? null : $("#district option:selected").text();
		var filterCommune = $('#commune').val() == ""|| $('#commune').val() == undefined ? null : $("#commune option:selected").text();
		var filterVillage = $('#village').val() == ""|| $('#village').val() == undefined ? null : $("#village option:selected").text();
		var filterRequest={
				"startDate": $('#startDate').val(),
				"endDate":  $('#endDate').val(),
				"province" : $("#proviance").val(),
				"district" : filterDistrict,
				"commune" :  filterCommune,
				"village" :  filterVillage,
				"userId":parseInt(userId), 
				"featureId":parseInt(featureId),
				"userTypeId": parseInt($("body").attr("data-userTypeID")),
				"userType":$("body").attr("data-roleType"),
				"username" : $("body").attr("data-selected-username"),
				//"userTypeId": parseInt($("body").attr("data-userTypeID")),
				//"userType" : $("body").attr("data-roleType"),
				"pageNo":parseInt(pageNo),
				"pageSize":parseInt(pageSize)
		}
		
		////console.log(JSON.stringify(filterRequest))
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});
		
		$.ajax({
			url: './exportAddress',
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
	
	function DeleteByID(id){
		$("#DeleteAddressModal").openModal({
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
			url : './deleteAddress',
			data : JSON.stringify(request),
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			type : 'DELETE',
			success : function(data, textStatus, xhr) {
				////console.log(data);
				$("#DeleteAddressModal").closeModal();
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
	
	
	
	function AddSystemAddress(entity){
		if (entity == "province"){
			$("#addProvinceModal").openModal({
				dismissible:false
			});
			$('#addProvince').val("");
			populateCountries("country", "state");
			$("#country").val("Cambodia");
		}else if(entity == "district"){
			$("#addDistrictModal").openModal({
				dismissible:false
			});
			$('#provinceForDistrict').val("");
			$('#addDistrict').val("");
			//populateCountries("country1", "state");
			//$("#country1").val("Cambodia");
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.getJSON('./getAllProvince', function(data) {
				$('#provinceForDistrict').empty();
				var html='<option value="">Select Province</option>';
				$('#provinceForDistrict').append(html);	
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].province).text(data[i].province)
					.appendTo('#provinceForDistrict');
				}
			});
		}else if(entity == "commune"){
			$("#addCommuneModal").openModal({
				dismissible:false
			});
			$('#provinceForCommune').val("");
			$('#districtForCommune').val("");
			$('#addCommune').val("");
			$.getJSON('./getAllProvince', function(data) {
				$('#provinceForCommune').empty();
				var html='<option value="">Select Province</option>';
				$('#provinceForCommune').append(html);	
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].province).text(data[i].province)
					.appendTo('#provinceForCommune');
				}
			});
		}else if(entity == "village"){
			$("#addVillageModal").openModal({
				dismissible:false
			});
			$('#provinceForVillage').val("");
			$('#districtForVillage').val("");
			$('#communeForVillage').val("");
			$('#addVillage').val("");
			$.getJSON('./getAllProvince', function(data) {
				$('#provinceForVillage').empty();
				var html='<option value="">Select Province</option>';
				$('#provinceForVillage').append(html);	
				for (i = 0; i < data.length; i++) {
					$('<option>').val(data[i].province).text(data[i].province)
					.appendTo('#provinceForVillage');
				}
			});	
		}
	}
	
	
	
	
	 function saveProvince(){
		// var counrty = $('#country').val() =="" || $('#country').val() == undefined ? null : $("#country option:selected").text();
		 //var province = $('#addProvince').val() =="" || $('#addProvince').val() == undefined ? null : $("#addProvince option:selected").text();
		 	var Request={
					"country" : $("#country").val(),
					"province" : $("#addProvince").val(),
					"username" : $("body").attr("data-selected-username"),
					"userId" : parseInt(userId),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username")
				}
			//console.log("Request-->"+JSON.stringify(Request));
		 	var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url : './addProvince',
				data : JSON.stringify(Request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				success : function(data) {
					$("#confirmSaveModal").openModal({
					 	   dismissible:false
				    });
					
				$('#successMessage').text('Province Added Successfully.');
				},
				error : function() {
					//alert("Failed");
				}
			});
		 return false
	}
	 
	 function saveDistrict(){
		 	//var district = $('#addDistrict').val() =="" || $('#addDistrict').val() == undefined ? null : $("#addDistrict option:selected").text();
		 	var Request={
					"province" : $("#provinceForDistrict").val(),
					"district": $('#addDistrict').val(),
					"username" : $("body").attr("data-selected-username"),
					"userId" : parseInt(userId),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username")
				}
			//console.log("Request-->"+JSON.stringify(Request));
		 	var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url : './addDistrict',
				data : JSON.stringify(Request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				success : function(data) {
					$("#confirmSaveModal").openModal({
					 	   dismissible:false
				    });
					
				$('#successMessage').text('District Added Successfully.');
				},
				error : function() {
					//alert("Failed");
				}
			});
		 return false
	}
	 
	 function saveCommune(){
		 	var Request={
		 			"districtID": parseInt($('#districtForCommune').val()),
					"commune": $('#addCommune').val(),
					"province" : $('#provinceForCommune').val(),
					"username" : $("body").attr("data-selected-username"),
					"userId" : parseInt(userId),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username")
				}
			//console.log("Request-->"+JSON.stringify(Request));
		 	var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url : './addCommune',
				data : JSON.stringify(Request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				success : function(data) {
					$("#confirmSaveModal").openModal({
					 	   dismissible:false
				    });
					
				$('#successMessage').text('Commune Added Successfully.');
				},
				error : function() {
					//alert("Failed");
				}
			});
		 return false
	}
	 
	 function saveVillage(){
		 var Request={
				    "province" : $('#provinceForVillage').val(),
		 			"communeID": parseInt($('#communeForVillage').val()),
		 			"village": $('#addVillage').val(),
		 			"districtID": parseInt($('#districtForVillage').val()),
					"username" : $("body").attr("data-selected-username"),
					"featureId":parseInt(featureId),
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username")
				}
			//console.log("Request-->"+JSON.stringify(Request));
		 	var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url : './addVillage',
				data : JSON.stringify(Request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				type : 'POST',
				success : function(data) {
					$("#confirmSaveModal").openModal({
					 	   dismissible:false
				    });
					
				$('#successMessage').text('Village Added Successfully.');
				},
				error : function() {
					//alert("Failed");
				}
			});
		 return false
	}
	 
	 function getDistrictForVillage(){
			var request = {
					 "province" : $("#provinceForVillage").val()
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
							$('#districtForVillage').empty();
							var html='<option value="">Select District</option>';
							$('#districtForVillage').append(html);	
							 for (i = 0; i < result.length; i++) {
								
								var html='<option value="'+result[i].id+'">'+result[i].district+'</option>';
								$('#districtForVillage').append(html);	
							} 

						},
						error : function(jqXHR, textStatus, errorThrown) {
							//////console.log("error in ajax")
						}
					});
	 }
	 
	 function getmodelDistrict(current) {
			var request = {
					 "province" : $("#provinceForCommune").val()
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
							$('#districtForCommune').empty();
							var html='<option value="">Select District</option>';
							$('#districtForCommune').append(html);	
							 for (i = 0; i < result.length; i++) {
								
								var html='<option value="'+result[i].id+'">'+result[i].district+'</option>';
								$('#districtForCommune').append(html);	
							} 

						},
						error : function(jqXHR, textStatus, errorThrown) {
							//////console.log("error in ajax")
						}
					});
		}
	 
	 
	 
	 $(document).on('change', '#districtForVillage', function(){
		 getmodelCommune();
			//getCommune();
		});	
	 
	 function getmodelCommune() {
			var request = {
				"districtID" : parseInt($('#districtForVillage').val())
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
							$('#communeForVillage').empty();
							var html='<option value="">Select Commune</option>';
							$('#communeForVillage').append(html);	
							 for (i = 0; i < result.length; i++) {
								
								var html='<option value="'+result[i].id+'">'+result[i].commune+'</option>';
								$('#communeForVillage').append(html);	
							} 

						},
						error : function(jqXHR, textStatus, errorThrown) {
							//////console.log("error in ajax")
						}
					});
		}
	 
	/* function closeConfirmantionModel(){
		 $("#addProvinceModal").closeModal({
				dismissible:false
			});
		 $("#addDistrictModal").closeModal({
				dismissible:false
			});
		 $("#addCommuneModal").closeModal({
				dismissible:false
			});
		 $("#addVillageModal").closeModal({
				dismissible:false
			});
		 
	 }
	 */
	 var localityID;
	
	 function viewDetails(id){
		 $("#editAddressModal").openModal({
				dismissible:false
			});
		$("#localityId").val(id);
		localityID = id;
		
		//alert("localityId1--->"+JSON.stringify($("#localityId").val(id))); 
	 }
	
	 /*--------------------------------- view Address -----------------------------------*/
	 
	 function EditSystemAddress(entity){
		// alert("localityId2--->"+JSON.stringify($("#localityId").val())); 
		 		var request ={
		 				"userId":parseInt(userId), 
						"featureId":parseInt(featureId),
						"userTypeId": parseInt($("body").attr("data-userTypeID")),
						"userType":$("body").attr("data-roleType"),
						"username" : $("body").attr("data-selected-username"),
						"localityId" : $("#localityId").val()
		 		}
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$.ajaxSetup({
					headers:
					{ 'X-CSRF-TOKEN': token }
				});
				$.ajax({
						url: './viewAddress',
						type: 'POST',
						data : JSON.stringify(request),
						dataType : 'json',
						contentType : 'application/json; charset=utf-8',
						success: function (data, textStatus, jqXHR) {
							if (entity == "province"){
								$("#editProvinceModal").openModal({
									dismissible:false
								});
								$("#editCountry").val(data.country);
								$('#editProvince,#oldProvince').val(data.province);
								//$('#editProvinceModifiedBy').val(data.modifiedBy);
								data.modifiedBy =="" || data.modifiedBy==null ?  $("#editProvinceModifiedBy").val('NA'): $("#editProvinceModifiedBy").val(data.modifiedBy);
								
							}else if(entity == "district"){
								$("#editDistrictModal").openModal({
									dismissible:false
								});
								$('#editProvinceforDistrict').val(data.province);
								$('#editDistrict,#oldDistrict').val(data.district);
								//$('#editDistrictModifiedBy').val(data.modifiedBy);
								data.modifiedBy =="" || data.modifiedBy==null ?  $("#editDistrictModifiedBy").val('NA'): $("#editDistrictModifiedBy").val(data.modifiedBy);
							}else if(entity == "commune"){
								$("#editCommuneModal").openModal({
									dismissible:false
								});
								$('#editProvinceForCommune').val(data.province);
								$('#editDistrictForCommune,#oldDistrict').val(data.district);
								$('#editCommune,#oldcommune').val(data.commune);
								//$('#editCommuneModifiedBy').val(data.modifiedBy);
								data.modifiedBy =="" || data.modifiedBy==null ?  $("#editCommuneModifiedBy").val('NA'): $("#editCommuneModifiedBy").val(data.modifiedBy);
							}else if(entity == "village"){
								$("#editVillageModal").openModal({
									dismissible:false
								});
								$('#editProvinceForVillage').val(data.province);
								$('#editDistrictForVillage').val(data.district);
								$('#editCommuneForVillage').val(data.commune);
								$('#editVillage,#oldVillage').val(data.village);
								//$('#editVillageModifiedBy').val(data.modifiedBy);
								data.modifiedBy =="" || data.modifiedBy==null ?  $("#editVillageModifiedBy").val('NA'): $("#editVillageModifiedBy").val(data.modifiedBy);
								
								
							}
							$("label[class='center-align']").addClass('active');
						},
						error: function (jqXHR, textStatus, errorThrown) {
							////console.log("error in ajax")
						}
					});	
	}
	
	 /*---------------------------------- Update Province-------------------------------------*/
		
		
		function updateProvince(){
			//alert("localityID--->" +localityID)
			var request ={ 
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username"),
					"id" : parseInt(localityID),
					"country" : $("#editCountry").val(),
					"province" : $("#oldProvince").val(),
					"updatingProvinceName" : $("#editProvince").val(),
			}

			//////console.log("request--->" +JSON.stringify(request))
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: './updateProvince', 
				type: 'PUT',
				data : JSON.stringify(request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				success: function (data, textStatus, jqXHR) {

					////console.log("Updated data---->" +data)
					$("#editProvinceModal").closeModal();	
					$("#updateFieldsSuccess").openModal({
						dismissible:false
					});
					$('#updateFieldMessage').text('');
					$('#updateFieldMessage').text('Province Updated Successfully.');
				},
				error: function (jqXHR, textStatus, errorThrown) {
					////console.log("error in ajax")
				}
			});	
			
			return false
		}
		
		
		/*---------------------------------- Update District-------------------------------------*/
		
		
		function updateDistrict(){
			//alert("localityID--->" +localityID)
			var request ={ 
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username"),
					"id" : localityID,
					"province" : $("#editProvinceforDistrict").val(),
					"currentDistrictName": $("#editDistrict").val(),
					"district": $("#oldDistrict").val(),
				}

			//////console.log("request--->" +JSON.stringify(request))
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: './updateDistrict', 
				type: 'PUT',
				data : JSON.stringify(request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				success: function (data, textStatus, jqXHR) {

					////console.log("Updated data---->" +data)
					$("#editDistrictModal").closeModal();	
					$("#updateFieldsSuccess").openModal({
						dismissible:false
					});
					$('#updateFieldMessage').text('');
					$('#updateFieldMessage').text('District Updated Successfully.');

				},
				error: function (jqXHR, textStatus, errorThrown) {
					////console.log("error in ajax")
				}
			});	
			
			return false
		}
		 
		/*---------------------------------- Update Commune-------------------------------------*/
		
		
		function updateCommune(){
			//alert("localityID--->" +localityID)
			var request ={ 
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username"),
					"id" : localityID,
					"commune": $("#oldcommune").val(),
					"currentCommuneName": $("#editCommune").val(),
					"districtName": $("#editDistrictForCommune").val(),
					"province" : $("#editProvinceForCommune").val(),
				}

			//////console.log("request--->" +JSON.stringify(request))
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: './updateCommune', 
				type: 'PUT',
				data : JSON.stringify(request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				success: function (data, textStatus, jqXHR) {

					////console.log("Updated data---->" +data)
					$("#editCommuneModal").closeModal();	
					$("#updateFieldsSuccess").openModal({
						dismissible:false
					});
					$('#updateFieldMessage').text('');
					$('#updateFieldMessage').text('Commune Updated Successfully.');
				},
				error: function (jqXHR, textStatus, errorThrown) {
					////console.log("error in ajax")
				}
			});	
			
			return false
		}
		
		
		/*---------------------------------- Update Village-------------------------------------*/
		
		
		function updateVillage(){
			//alert("localityID--->" +localityID)
			var request ={ 
					"userTypeId": parseInt($("body").attr("data-userTypeID")),
					"userType":$("body").attr("data-roleType"),
					"username" : $("body").attr("data-selected-username"),
					"id" : localityID,
					"currentVillage": $("#editVillage").val(),
					"commune": $('#editCommuneForVillage').val(),
					"districtName":  $("#editDistrictForVillage").val(),
					"village" : $("#oldVillage").val()
					
				}

			//////console.log("request--->" +JSON.stringify(request))
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				url: './updateVillage', 
				type: 'PUT',
				data : JSON.stringify(request),
				dataType : 'json',
				contentType : 'application/json; charset=utf-8',
				success: function (data, textStatus, jqXHR) {

					////console.log("Updated data---->" +data)
					$("#editVillageModal").closeModal();	
					$("#updateFieldsSuccess").openModal({
						dismissible:false
					});
					$('#updateFieldMessage').text('');
					$('#updateFieldMessage').text('Village Updated Successfully.');
				},
				error: function (jqXHR, textStatus, errorThrown) {
					////console.log("error in ajax")
				}
			});	
			
			return false
		}		
		
		
		function filterResetAddressManagement(formID){
			$('#'+formID).trigger('reset');
			$("label").removeClass('active');
			addressFieldTable(lang);
		}