
//boxes
$(document).ready(function(){
	$('div#initialloader').fadeIn('fast');
	var url;
	var graphRequest="";
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	[40,50,31,36,47].forEach(function(reportnameId) {
	$.ajaxSetup({
	headers:
	{ 'X-CSRF-TOKEN': token }
	});
	 if(reportnameId==50 || reportnameId==31 ){
	 graphRequest={

			"reportnameId": reportnameId,
			"file" : 0,
			"pageSize" :1,
			"pageNo" :0,
			"typeFlag": 1
			
	}
	 }
	 else  if(reportnameId==40 || reportnameId==47  || reportnameId==36){
		 graphRequest={

					"reportnameId": reportnameId,
					"file" : 0,
					"pageSize" :1,
					"pageNo" :0,
					"typeFlag": 2
			}	 
	 }
	
	$.ajax({
		type : 'POST',
		url : './importer/count/retailer',
		contentType : "application/json",
		async: false,
		data : JSON.stringify(graphRequest),
		success: function(data){
var i=0;
				Object.keys(data['rowData'][0]).map(function(key){ 
				if(key == 'Date'){
					$('#dateVal').text('Last Update Date: '+data['rowData'][0][key]);
				}
				else{if(data['rowData'][0][key]!=null)
				{
					$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");	
					}
				else{
					//$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">0</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
				}

				}
				$('div#initialloader').delay(300).fadeOut('slow');

				
		});
		}
	});
	});
});




//boxes

	//$('div#initialloader').fadeIn('fast');
	var url;

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var graphRequest1="";
	[40,36,43].forEach(function(reportnameId) {
	$.ajaxSetup({
	headers:
	{ 'X-CSRF-TOKEN': token }
	});
	//alert(reportnameId);
	if(reportnameId==43){
		graphRequest1={
				 "columns": [
					    "Month",
					    "Stock Status",
					    "Count"
					  ],
				"reportnameId": reportnameId,
				"groupBy": "Stock Status",
				"file" : 0,
				"pageSize" :10,
				"pageNo" :0,
				"typeFlag": 3
		}
	}
	else{
	graphRequest1={

			"reportnameId": reportnameId,
			"file" : 0,
			"pageSize" :1,
			"pageNo" :0,
			"typeFlag": 3
	}
	}
	
	$.ajax({
		type : 'POST',
		url : './importer/count/retailer',
		contentType : "application/json",
		async: false,
		data : JSON.stringify(graphRequest1),
		success: function(data){
			$.i18n().load( {
				'en': './resources/i18n/en.json'
				
			} ).done( function() { 
			
var i=0;
var key1='monthWise.';
				Object.keys(data['rowData'][0]).map(function(key){ 
					
					
					if(key =='Month'){
						$('#monthdateVal').text('Last Month: '+data['rowData'][0][key]);
					}
					else{
					if(data['rowData'][0][key]!=null )
				{
						if(reportnameId==43){
		                 	$("#infoBox1").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
						}
						else{
				      	$("#infoBox1").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' >"+$.i18n(key1+key)+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");	
					}}
				else{
					//$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">0</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
				}
					}
				//$('div#initialloader').delay(300).fadeOut('slow');

				
		});
			});
		}

	});
});
	
	
	

	// line graph for stock-------------------------------------------------------------------------
	
	function LawfulGraph() {
	
			var graphRequest=null;
			var chartID=null;
			var type=null;
			var title=null;
			var urlHit=null;
			var featureFlag =null;
			[61].forEach(function(reportnameId) {
				
				 if(reportnameId==61){
				 featureFlag ="MostStolen";
					graphRequest={
							"columns": [
								  "Date",
							      "Stolen Count",
							      "Recoverd Count",
							      "Blocked Count",
							      "Pending Count"
								],
								"reportnameId": reportnameId,
								"lastDate": false,
								"file" : 0,
								"pageSize" :40,
								"pageNo" :0,
								"typeFlag": 2
					}
					
					urlHit='./brandModel/data/'+featureFlag;
				}
				
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$.ajaxSetup({
				headers:
				{ 'X-CSRF-TOKEN': token }
			});
			$.ajax({
				type : 'POST',
				url :urlHit ,
				contentType : "application/json",
				data : JSON.stringify(graphRequest),
				/*beforeSend : function() {
					$("#loading-image").show();
				},*/
				success : function(data) {
					var response = data;
					 if(reportnameId == 61 ){
						//labelSet=["Stolen Count","Recoverd Count","Blocked Count","Pending Count"];
						
						MostStolenGraph(response,'lineGraphMonthlyStock','line','User Login HorizontalBar Graph');
					}
				
					
				},
				error : function() {
				}
			});
			});
		}


	function MostStolenGraph(response,id,chartType,chartTitle)
	{
		    	

		  var date=[];
		  var noOfUsers=[];
		  var uniqueUsers=[];
			//var pieLabelName=response['columns'];
		  var pieLabelName=['No of user logged','Unique user logged'];
		  var pieData=[];
		  var BlockedCount=[];
		 
		
			for(var i=0;i<response['rowData'].length;i++){
				noOfUsers.push(response['rowData'][i]['Number of user logged']);
			   	 date.push(response['rowData'][i]['Date']);
				BlockedCount.push(response['rowData'][i]['Blocked Count']);
				
			    }
		    if( chartType == 'line'){
		    	
				$("#expMonthlyStock").unbind("click").click(function(){
			        var data = response['rowData'];
			        if(data == '')
			            return;
			        
			        JSONToCSVConvertor(data, "Monthly stock", true);
			    });
			   	var ctx = document.getElementById(''+id+'').getContext('2d');
			    var chart = new Chart(ctx, {
			      // The type of chart we want to create
			      type: ''+chartType+'',

			      // The data for our dataset
			      data: {
			        labels: date,
			        datasets: [{
			            label: "Stolen Count",
			            borderColor:  '#006400',
			            data: StolenCount,
			            fill: false
			            
			        }
			        ]
			      },

			      // Configuration options go here
			      options: {
			    	    responsive: false,
			    	    maintainAspectRatio: false,
			    	    animation: {
			    	        onComplete: captureImage
			    	      },
			    	    elements: {
			                point:{
			                    radius: 0
			                }
			            },
			    	    plugins: {
						    datalabels: {
						        display: false,
						    },
						    anchor :'end',
				            align :'top',
				            // and if you need to format how the value is displayed...
				            formatter: function(value, context) {
				                return GetValueFormatted(value);
				            }
						},
						
			    	    scales: {
			                xAxes: [{
			                   gridLines: {
			                      display: false
			                   },
			                   scaleLabel: {
			                       display: true,
			                       labelString: 'Date'
			                     }
			                }],
			                yAxes: [{
			                   gridLines: {
			                      display: false
			                   },
			                   scaleLabel: {
			                       display: true,
			                       labelString: 'Device Count'
			                     }
			                }]
			             }           
			             
			    	}
			    });
			    function captureImage(){  
			        var url=chart.toBase64Image();
			        document.getElementById("lineGraphMonthlyStock").href=url;
			    }
				} 
		    $('div#initialloader').delay(300).fadeOut('slow');

	}


