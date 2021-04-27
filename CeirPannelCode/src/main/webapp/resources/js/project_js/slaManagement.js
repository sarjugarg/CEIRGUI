
			var featureId = 40;
			var cierRoletype = sessionStorage.getItem("cierRoletype");

			var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';
		/*		window.parent.$('#langlist').on('change', function() {
					var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';
				window.location.assign("./grievanceManagement?lang="+lang);
			
				});*/ 


				$.i18n().locale = lang;
				var documenttype,selectfile,selectDocumentType;
				$.i18n().load( {
					'en': './resources/i18n/en.json',
					'km': './resources/i18n/km.json'
				} ).done( function() { 
						documenttype=$.i18n('documenttype');
						selectfile=$.i18n('selectfile');
						selectDocumentType=$.i18n('selectDocumentType');
				
				});



			$(document).ready(function(){
				$('div#initialloader').fadeIn('fast');
				DataTable(lang);
				pageRendering();
			});

			var userId = $("body").attr("data-userID");

			/*localStorage.setItem("grievancePageSource", "viaGriebva");*/




			//**************************************************Grievance table**********************************************

			function DataTable(lang){
				
				var featureName = $('#feature').val() == null ? null : $("#feature option:selected").text();
				
				//var usertype = $('#userType').val() == null ? null : $("#userType option:selected").text(); 
				
				var filterRequest={
						//"endDate":$('#endDate').val(),
						//"startDate":$('#startDate').val(),
						"feature" : parseInt($('#feature').val()),
						"usertype" : parseInt($('#userType').val()),
						"username" : $("body").attr("data-selected-username"),
						"featureId":parseInt(featureId),
						"userTypeId": parseInt($("body").attr("data-userTypeID")),
						"userType":$("body").attr("data-roleType"),
						"userId" : parseInt($("body").attr("data-userID"))
						
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
					url: 'headers?type=slaTableHeaders&lang='+lang,
					type: 'POST',
					dataType: "json",
					success: function(result){
						
						var table=	$("#slaLibraryTable").DataTable({
							destroy:true,
							"serverSide": true,
							orderCellsTop : true,
							"ordering" : false,
							"bPaginate" : true,
							"bFilter" : true,
							"bInfo" : true,
							"bSearchable" : true,
							"oLanguage": {  
									"sUrl": langFile  
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
								url : 'slaData',
								type: 'POST',
								dataType: "json",
								data : function(d) {
									d.filter = JSON.stringify(filterRequest); 
									//////console.log(JSON.stringify(filterRequest));
								}

							},
							"columns": result
						});
						$('div#initialloader').delay(300).fadeOut('slow');
						
					},
					error: function (jqXHR, textStatus, errorThrown) {
						//////console.log("error in ajax");
					}
				});
			}



			//**************************************************Grievance page buttons**********************************************

			function pageRendering(){
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$.ajaxSetup({
					headers:
					{ 'X-CSRF-TOKEN': token }
				});
				$.ajax({
					url: 'sla/pageRendering',
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
								$("#slaTableDiv").append("<div class='input-field col s6 m2'>"+
										"<div id='enddatepicker' class='input-group date'>"+
										"<input class='form-control datepicker' type='text' onchange='checkDate(startDate,endDate)' id="+date[i].id+" autocomplete='off'>"+
										"<label for="+date[i].id+">"+date[i].title
										+"</label>"+
										"<span	class='input-group-addon' style='color: #ff4081'>"+
										"<i	class='fa fa-calendar' aria-hidden='true' style='float: right; margin-top: -37px;'>"+"</i>"+"</span>");
								$( "#"+date[i].id ).datepicker({
									dateFormat: "yy-mm-dd",
									 maxDate: new Date()
						        });
								}
							else if(date[i].type === "text"){
								$("#slaTableDiv").append("<div class='input-field col s6 m2'><input type="+date[i].type+" id="+date[i].id+" maxlength='19' /><label for="+date[i].id+" class='center-align'>"+date[i].title+"</label></div>");

							}
						} */

						// dynamic dropdown portion
						var dropdown=data.dropdownList;
						for(i=0; i<dropdown.length; i++){
							var dropdownDiv=
								$("#slaTableDiv").append("<div class='col s6 m2 l2 selectDropdwn'>"+
										
										"<div class='select-wrapper select2 form-control boxBorder boxHeight initialized'>"+
										"<span class='caret'>"+"</span>"+
										"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

										"<select id="+dropdown[i].id+"  class='select-wrapper select2  initialized'>"+
										"<option value=null>"+dropdown[i].title+
										"</option>"+
										"</select>"+
										"</div>"+
								"</div>");
						}

						$("#slaTableDiv").append("<div class=' col s3 m2 l1'><button type='button' class='btn primary botton' id='submitFilter'></div>");
						$("#slaTableDiv").append("<div class=' col s3 m2 l1'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportData()'>"+$.i18n('Export')+"<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");

						for(i=0; i<button.length; i++){
							$('#'+button[i].id).text(button[i].buttonTitle);
							if(button[i].type === "HeaderButton"){
								$('#'+button[i].id).attr("href", button[i].buttonURL);
							}
							else{
								$('#'+button[i].id).attr("onclick", button[i].buttonURL);
							}
						}

						if(cierRoletype=="CEIRAdmin"){
							$("#btnLink").css({display: "none"});
						}

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
				$.getJSON('./getAllfeatures', function(data) {
				for (i = 0; i < data.length; i++) {
				$('<option>').val(data[i].id).text(data[i].name).appendTo('#feature');
				}
			});
				
				$.getJSON('./registrationUserType?type=2', function(data) {
					for (i = 0; i < data.length; i++) {
						$('<option>').val(data[i].id).text(data[i].usertypeName)
						.appendTo('#userType');
					}
				});
				
		}


			//**********************************************************Export Excel file************************************************************************
			function exportData()
			{
				var roleType = $("body").attr("data-roleType");
				var currentRoleType = $("body").attr("data-stolenselected-roleType");
				var table = $('#slaLibraryTable').DataTable();
				var info = table.page.info(); 
				var pageNo=info.page;
				var pageSize =info.length;
				
				var filterRequest={
						"feature" : parseInt($('#feature').val()),
						"usertype" : $('#userType').val(),
						"username" : $("body").attr("data-selected-username"),
						"featureId":parseInt(featureId),
						"userTypeId": parseInt($("body").attr("data-userTypeID")),
						"userType":$("body").attr("data-roleType"),
						"userId" : parseInt($("body").attr("data-userID")),
						"pageNo":parseInt(pageNo),
						"pageSize":parseInt(pageSize)
						
						/*"userId":parseInt(userId),
						"featureId":parseInt(featureId),
						"userTypeId": parseInt($("body").attr("data-userTypeID")),
						"userType":$("body").attr("data-roleType")*/
				}
				console.log(JSON.stringify(filterRequest))
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$.ajaxSetup({
					headers:
					{ 'X-CSRF-TOKEN': token }
				});
				$.ajax({
					url: './exportSlaData',
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
			

			


			