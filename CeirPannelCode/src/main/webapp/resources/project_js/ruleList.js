		
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
 			table('./headers?lang='+lang+'&type=ruleList','./ruleListData');
 			}

		//**************************************************filter table**********************************************

		function table(url,dataUrl){
		
		var state= $("#State").val() == '' ||  $("#State").val() == undefined ? null : $("#State").val();
		var name = $("#filterRule").val() =='' ||  $("#filterRule").val() == undefined ? null  : $("#filterRule").val();
		var description = $("#filterDescription").val() == '' ||  $("#filterDescription").val() == undefined ? null  : $("#filterDescription").val();
		
		
			var filterRequest={
					  	"state": state,
						"userId":parseInt($("body").attr("data-userID")),
						"featureId":parseInt(featureId),
						"userTypeId": parseInt($("body").attr("data-userTypeID")),
						"userName":$("body").attr("data-selected-username"),
						"roleType":$("body").attr("data-roleType"),
						"name" :  name,
						"description" : description,
					
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
						"ordering" : true,
						"bPaginate" : true,
						"bFilter" : false,
						"bInfo" : true,
						"bSearchable" : true,
						"oLanguage": {
							"sEmptyTable": "No records found in the system"
					    },
					    "aaSorting": [],
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

							},
							error: function (jqXHR, textStatus, errorThrown,data) {
								
								 window.parent.$('#msgDialog').text($.i18n('500ErrorMsg'));
								 // messageWindow(jqXHR['responseJSON']['message']);
								 window.parent.$('#500ErrorModal').openModal({
								 dismissible:false
								 });
								
							}
						},
						"columns": result,
						columnDefs : [
							{ orderable: false, targets: -1 }
							]
					});


					$('div#initialloader').delay(300).fadeOut('slow');
					
				},
				error: function (jqXHR, textStatus, errorThrown) {
					
				}
			});
		}



		function pageRendering(){
			pageButtons('./ruleList/pageRendering');

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
					var date=data.inputTypeDateList;
					for(i=0; i<date.length; i++){
						if(date[i].type === "date"){
							$("#FieldTableDiv").append("<div class='input-field'>"+
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
							$("#FieldTableDiv").append("<div class='input-field' ><input type="+date[i].type+" id="+date[i].id+" maxlength='50' /><label for="+date[i].id+" class='center-align'>"+date[i].title+"</label></div>");
						}
						 
					} 
				
				// dynamic dropdown portion
					var dropdown=data.dropdownList;
					for(i=0; i<dropdown.length; i++){
						var dropdownDiv=
							$("#FieldTableDiv").append("<div class='selectDropdwn'>"+
								
									"<div class='select-wrapper select2  initialized'>"+
									"<span class='caret'>"+"</span>"+
									"<input type='text' class='select-dropdown' readonly='true' data-activates='select-options-1023d34c-eac1-aa22-06a1-e420fcc55868' value='Consignment Status'>"+

									"<select id="+dropdown[i].id+" class='select2 initialized'>"+
									"<option value='' selected>"+dropdown[i].title+
									"</option>"+
									"</select>"+
									"</div>"+
							"</div>");
					}
						var viewFilter="viewFilter";
						$("#FieldTableDiv").append("<div class='filter_btn'><button type='button' class='btn primary botton' id='submitFilter'/></div>");
						$("#FieldTableDiv").append("<div class='filter_btn'><button type='button' class='btn primary botton' id='clearFilter'>"+$.i18n('clearFilter')+"</button></div>");
						$("#FieldTableDiv").append("<div class='filter_btn'><a href='JavaScript:void(0)' type='button' class='export-to-excel right'  onclick='exportRuleListData()'>"+$.i18n('Export')+"<i class='fa fa-file-excel-o' aria-hidden='true'></i></a></div>");
						$('#clearFilter').attr("onclick", "Resetfilter('viewFilter')");
						for(i=0; i<button.length; i++){
							$('#'+button[i].id).text(button[i].buttonTitle);
							$('#'+button[i].id).attr("onclick", button[i].buttonURL);
						}
						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						$.ajaxSetup({
							headers:
							{ 'X-CSRF-TOKEN': token }
						});
						$.getJSON('./getDropdownList/RULE_STATE', function(data) {
							for (i = 0; i < data.length; i++) {
								$('<option>').val(data[i].interp).text(data[i].interp)
								.appendTo('#State,#editState');
							}
						});
						
				}
			
	
			}); 
			
	}






			function getDetailBy(id,output){
			
				window.xid=id;
				window.xoutput=output;
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$.ajaxSetup({
					headers:
					{ 'X-CSRF-TOKEN': token }
				});
				$.ajax({
					url : "./viewRuleListAPI/"+id,
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
						////console.log("Failed");
					}
				});	
			}
			
			
			function setData(result){
				$("label[class='center-align']").addClass('active');
				$("#editName").val(result.name);
				$("#editDescription").val(result.description);
				$("#editState").val(result.state);
				result.modifiedBy =="" || result.modifiedBy==null ?  $("#editModifiedBy").val('NA'): $("#editModifiedBy").val(result.modifiedBy);
			}
			
			
			
			function update(){

				var name=$('#editName').val();
				var description=$('#editDescription').val();
				var state=$('#editState').val();
				var ruleListContent={
						"name":name,
						"description":description,
						"state":state,
						"id":window.xid,
						"output":window.xoutput,
						"userId":parseInt($("body").attr("data-userID")),
						"featureId":parseInt(featureId),
						"userTypeId": parseInt($("body").attr("data-userTypeID")),
						"userName":$("body").attr("data-selected-username"),
						"roleType":$("body").attr("data-roleType")
						
				}
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$.ajaxSetup({
					headers:
					{ 'X-CSRF-TOKEN': token }
				});
				$.ajax({
					
					url : "./updateRuleList",
					data : JSON.stringify(ruleListContent),
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

			function exportRuleListData(){
				var table = $('#table').DataTable();
				var info = table.page.info(); 
				var pageNo=info.page;
				var pageSize =info.length;
				var state= $("#State").val() == '' ||  $("#State").val() == undefined ? null : $("#State").val();
				var name = $("#filterRule").val() =='' ||  $("#filterRule").val() == undefined ? null  : $("#filterRule").val();
				var description = $("#filterDescription").val() == '' ||  $("#filterDescription").val() == undefined ? null  : $("#filterDescription").val();
				
				var filterRequest={
						  "state": state,
							"userId":parseInt($("body").attr("data-userID")),
							"featureId":parseInt(featureId),
							"userTypeId": parseInt($("body").attr("data-userTypeID")),
							"userName":$("body").attr("data-selected-username"),
							"roleType":$("body").attr("data-roleType"),
							"userType":$("body").attr("data-roleType"),
							//"username" : $("body").attr("data-selected-username"),
							"name" :  name,
							"description" : description,
							"pageNo":parseInt(pageNo),
							"pageSize":parseInt(pageSize)
						
				}
				
				//console.log(JSON.stringify(filterRequest))
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$.ajaxSetup({
					headers:
					{ 'X-CSRF-TOKEN': token }
				});
				
				$.ajax({
					url: './exportRuleList',
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
					
		
			
			function viewByID(id,output){			
				window.xid=id;
				window.xoutput=output;
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				$.ajaxSetup({
					headers:
					{ 'X-CSRF-TOKEN': token }
				});
				$.ajax({
					url : "./viewRuleListAPI/"+id,
					dataType : 'json',
					contentType : 'application/json; charset=utf-8',
					type : 'GET',
					success : function(data) {
						var result=JSON.stringify(data);
						$("#viewModel").openModal({
					        dismissible:false
					    });
					
						
						view_data(JSON.parse(result));
					},
					error : function() {
						////console.log("Failed");
					}
				});	
			}
			
			
			function view_data(result){
				$("label[class='center-align']").addClass('active');
				$("#viewName").val(result.name);
				$("#viewDescription").val(result.description);
				$("#viewState").val(result.state);
				result.modifiedBy =="" || result.modifiedBy==null ?  $("#viewModifiedBy").val('NA'): $("#viewModifiedBy").val(result.modifiedBy);
			}
			 function Resetfilter(formID){
					$('#'+formID).trigger('reset');
					$("label").removeClass('active');
					filter(lang,null);
			 }