/*[

	'./resources/js/materialize.js',
	'./resources/custom_js/bootstrap.min.js',
	'./resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js'
	].forEach(function(src) {
		$('body').append('<script type="text/javascript" src='+src+' async defer><\/script>');

	});
	

*/
function activeDeviceGraph() {
	['operatorActive',29,34,27].forEach(function(reportnameId) {
		var graphRequest=null;
		var chartID=null;
		var type=null;
		var title=null;
		var urlHit=null;
		var featureFlag=null;
		if(reportnameId == 29){
			urlHit="./report/data?Type=OperatorDashboard";
			graphRequest={
					 "columns": [
						    "Date",
						    "Operator Name",
						    "Total IMEI"
						  ],
						  "groupBy": "Operator Name",
						  "reportnameId": 29,
						  "file" : 0, 
						  "pageSize" :55, 
						  "pageNo" :0,
						  "typeFlag": 1
			}
			
			
			chartID='lineGraph';
			type='line';
			title='User Login HorizontalBar Graph';
		}
		else if(reportnameId == 'operatorActive'){
			urlHit="./report/data?Type=OperatorDashboard";
			graphRequest={
					"columns": [
						"Date",
						"Operator Name",
						"Total IMEI"
						],
						 "lastDate": true,
						  "file" : 0, 
						  "pageSize" :10, 
						  "pageNo" :0,
						"groupBy": "Operator Name",
						"reportnameId": 29,
						"typeFlag": 1
						}
			
		}
		else if(reportnameId == 27){
			graphRequest={
					 
					  
					  "lastDate": true,
					  "reportnameId": 27,
						  "file" : 0, 
						  "pageSize" :10, 
						  "pageNo" :0,
						  "typeFlag": 2
			}
			urlHit='./report/imeiUsageDashBoard';
		}
		
		
		else if(reportnameId == 34){
			featureFlag="BlocedkIMEI"
			urlHit='./brandModel/data/'+featureFlag;
			graphRequest={
					 "columns": [
						    "Date",
						    "Operator Name",
						    "count"
						  ],
						  "groupBy": "Operator Name",
						  "reportnameId": 34,
						  "file" : 0, 
						  "pageSize" :30, 
						  "pageNo" :0,
						  "typeFlag": 2
			}
			
		}
		

		

		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});
		$.ajax({
			type : 'POST',
			url : urlHit,
			contentType : "application/json",
			dataType : 'html',
			async:false,
			data : JSON.stringify(graphRequest),
			/*beforeSend : function() {
				$("#loading-image").show();
			},*/
			success : function(data) {
				var labelSet=null;
				var response = JSON.parse(data);
				//graph(response,chartID,type,title);
				if(reportnameId==34){
					
					labelSet=operatorList()
					blockIMEIgraph(response,'blockIMEIGraph','horizontalBar','User Login HorizontalBar Graph',labelSet,'blockedIMEIImg','expblockedIMEI','blockedIMEI');
					
					
				}
				else if(reportnameId==29){
				graph(response,'lineGraph','line','User Login Line Graph')
				}
				else if(reportnameId == 'operatorActive'){
					graph(response,'pieGraph','pie','User Login Pie Graph')
					
					}
				else if(reportnameId==27){
	
	         graph(response,'gaugeGraph','gauge','User Login Doughnut Graph')
				}
			},
			error : function() {
			}
		});
	});

}



function graph(response,id,chartType,chartTitle)
{
  var date=[];
  var operatorName=[];
  var totalImei=[];
  var QB=[];
  var seatel=[];
  var smart=[];
  var cellcard=[];
  var metfone=[];
  var pieLabelName=['Primary','Secondary'];
  var pieData=[];

  var pieOperatorActiveLabelName;
  var pieOperatorActive=[];
	   	//console.log("repsonse-->"+JSON.stringify(response));
		for(var i=0;i<response['rowData'].length;i++){
	   		QB.push(response['rowData'][i]['QB']);
	   		seatel.push(response['rowData'][i]['SEATEL']);
	   		smart.push(response['rowData'][i]['SMART']);
	   		cellcard.push(response['rowData'][i]['CELLCARD']);
	   		metfone.push(response['rowData'][i]['METFONE']);
	   		date.push(response['rowData'][i]['Date']);
	   		//totalImei.push(response['rowData'][i]['Total IMEI']);
	   	}	
		pieData.push(parseInt(response['rowData'][0]['Total Paired IMEI']));
		pieData.push(parseInt(response['rowData'][0]['Total Duplicate IMEI']));
		
		if(chartType=='pie'){
		pieOperatorActiveLabelName=["QB","Seatel", "Smart","Cellcard","Metfone"];
		//pieOperatorActiveLabelName=response['columns'];
		pieOperatorActive.push(parseInt(response['rowData'][0]['QB']));
		pieOperatorActive.push(parseInt(response['rowData'][0]['SEATEL']));
		pieOperatorActive.push(parseInt(response['rowData'][0]['SMART']));
		pieOperatorActive.push(parseInt(response['rowData'][0]['CELLCARD']));
		pieOperatorActive.push(parseInt(response['rowData'][0]['METFONE']));
		}
		
		
	   if(chartType == 'pie'){
	    	$("#expOperatorActivePair").unbind("click").click(function(){
		        var data = JSON.stringify(response['rowData']);
		        //console.log(JSON.stringify(data));
		        if(data == '')
		            return;
		        JSONToCSVConvertor(data, "Operator_Active_Pair_Report", true);

		    });
	
			

			var ctx = document.getElementById(''+id+'').getContext('2d');

			var chart = new Chart(ctx, {
				// The type of chart we want to create
				type: ''+chartType+'',

				// The data for our dataset
				data: {
					labels: pieOperatorActiveLabelName,
					datasets: [ {
						backgroundColor: [
							 '#512DA8','#008B8B','#F20515','#4682B4','#8B4513'],
							data: pieOperatorActive
					}]
				},

				// Configuration options go here
				options: {
					responsive: false,
					maintainAspectRatio: false,
					animation: {
	    	        	
	    	        	onComplete:captureLineImage
	    	        },
					plugins: {
					    datalabels: {
						      formatter: (value, ctx) => {
						    	  
						        let sum = ctx.dataset._meta[0].total;
						    
						        let percentage = (value * 100 / sum).toFixed(2) + "%";
						        return percentage;


						      },
						      color: '#fff',
						    }
						  }
				}
			});

			function captureLineImage(){  
	            var url=chart.toBase64Image();
	            //alert("urll="+url);
	            document.getElementById("OperatorActiveImage").href=url;
	          //  $("#Top5BrandName").attr("href",url);
	            }
	    }
	    else if(chartType=='line' ){
		$("#exp").unbind("click").click(function(){
	        var data = response['rowData'];
	        if(data == '')
	            return;
	        
	        JSONToCSVConvertor(data, "Report", true);
	    });
	   	var ctx = document.getElementById(''+id+'').getContext('2d');
	    var chart = new Chart(ctx, {
	      // The type of chart we want to create
	      type: ''+chartType+'',

	      // The data for our dataset
	      data: {
	        labels: date,
	        datasets: [{
	            label: "QB",
	            borderColor:  'rgb(235, 203, 138)',
	            data: QB,
	            fill: false
	            
	        },
	        {
	            label: "Seatel",
	            borderColor: 'rgb(70, 191, 189)',
	            data: seatel,
	            fill: false
	            
	        },
	        {
	            label: "Smart",
	            borderColor:  '#512DA8',
	            data: smart,
	            fill: false
	            
	        },
	        {
	            label: "Cellcard",
	            borderColor: '#D32F2F',
	            data: cellcard,
	            fill: false
	            
	        },
	        {
	            label: "Metfone",
	            borderColor:  '#FFA000',
	            data: metfone,
	            fill: false
	            
	        }]
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
	                       labelString: 'IMEI Count'
	                     }
	                }]
	             }           
	             
	    	}
	    });
	    function captureImage(){  
	        var url=chart.toBase64Image();
	        document.getElementById("lineImage").href=url;
	    }
		}
		

		else if(chartType=='gauge'){
	    	 $("#expPairingType").unbind("click").click(function(){
	 	        var data = response['rowData'];
	 	        if(data == '')
	 	            return;
	 	        
	 	        JSONToCSVConvertor(data, "Pairing_Type_Report", true);
	 	    });
	        var ctx = document.getElementById(''+id+'').getContext('2d');
	        var chart = new Chart(ctx, {
	          // The type of chart we want to create
	          type: 'doughnut',

	          // The data for our dataset
	          data: {
	            labels: pieLabelName,
	            datasets: [ {
	            	 backgroundColor: [
	            		 '#512DA8',
	                     '#D32F2F'],
	                data: pieData
	            }],
	            borderWidth: 0
	          },

	          // Configuration options go here
	          options: {
	        	    responsive: false,
	        	    maintainAspectRatio: false,
	        	    animation: {
	        	        onComplete: captureImage
	        	      },
	        	    rotation: 1 * Math.PI,
	                circumference: 1 * Math.PI,
	         	    plugins: {
					    datalabels: {
					      formatter: (value, ctx) => {
					    	  let datasets = ctx.chart.data.datasets;

					          if (datasets.indexOf(ctx.dataset) === datasets.length - 1) {
					            let sum = datasets[0].data.reduce((a, b) => a + b, 0);
					            let percentage = Math.round((value / sum) * 100) + '%';
					            return percentage;
					          } else {
					            return percentage;
					          }
					          },
					      color: '#fff',
					    }
					  }
	             }
	 
	        });
	         
	          function captureImage(){  
	              var url=chart.toBase64Image();
	              document.getElementById("pairingTypesImage").href=url;
	              }	
	    
	    }
    $('div#initialloader').delay(300).fadeOut('slow');
}



//boxes
$(document).ready(function(){
	$('div#initialloader').fadeIn('fast');
	var url;

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
	headers:
	{ 'X-CSRF-TOKEN': token }
	});
	
	var graphRequest={
			  "reportnameId": 29,
			  "file" : 0, 
			  "pageSize" :5, 
			  "pageNo" :0,
			  "typeFlag": 1
	}
	
	$.ajax({
		type : 'POST',
		url : './report/data?Type=OperatorDatatable',
		contentType : "application/json",
		data : JSON.stringify(graphRequest),
		success: function(data){
			$("#expOperatorWiseIMEI").unbind("click").click(function(){
		        var result = JSON.stringify(data['rowData']);
		        if(result == '')
		            return;
		        
		        JSONToCSVConvertor(result, "Report", true);
		    });

				$('div#initialloader').delay(300).fadeOut('slow');
				for(var i=0;i<data['rowData'].length;i++){
					if(i == 0){ $('#firstTD').text(data['rowData'][i]['Total IMEI']);}
					else if(i == 1){$('#secondTD').text(data['rowData'][i]['Total IMEI']);}
					else if(i == 2){$('#thirdTD').text(data['rowData'][i]['Total IMEI']);}
					else if(i == 3){$('#fourthTD').text(data['rowData'][i]['Total IMEI']);}
					else if(i == 4){$('#fifthTD').text(data['rowData'][i]['Total IMEI']);}
				}
			
		}
	
	});
});
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$.ajaxSetup({
headers:
{ 'X-CSRF-TOKEN': token }
});
$.ajax({
	type : 'POST',
	url : './mobileDevice/report/count',
	contentType : "application/json",
	async:false,
	data : JSON.stringify({"reportnameId": 28,"file" : 0,"pageSize" :1,"pageNo" :0}),
	success: function(data){
		var i=0;
		Object.keys(data['rowData'][0]).map(function(key){ 
			if(key == 'Date'){
				$('#dateVal').text('Last Update Date: '+data['rowData'][0][key]);
				//$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b>"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
			}
			else{
				if(data['rowData'][0][key]!=null)
				{
					$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");	
					}
				else{
					//$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">0</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
				}
			}
		});
	}
});




//boxes
$(document).ready(function(){
	$('div#initialloader').fadeIn('fast');
	var url;

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	[31].forEach(function(reportnameId) {
	$.ajaxSetup({
	headers:
	{ 'X-CSRF-TOKEN': token }
	});
	var graphRequest={

			"reportnameId": reportnameId,
			"file" : 0,
			"pageSize" :1,
			"pageNo" :0,
			"typeFlag": 1
	}
	
	$.ajax({
		type : 'POST',
		url : './operatorReport/count/'+reportnameId,
		contentType : "application/json",
		async: false,
		
		data : JSON.stringify(graphRequest),
		success: function(data){
			var i=0;
				Object.keys(data['rowData'][0]).map(function(key){ 
				if(key == 'Date'){
					$('#dateVal').text('Last Update Date: '+data['rowData'][0][key]);
				}
				else{
				
					if(data['rowData'][0][key]!=null)
					{
						$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");	
						}
					else{
						//$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">0</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
					}

					//$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
				
				}
				$('div#initialloader').delay(300).fadeOut('slow');
				});
		}
	
	});
	});
});




function blockIMEIgraph(response,id,chartType,chartTitle,pieLabelName,GraphImageId,GraphExcel,reportName)
{
	var date = [];
	//console.log("resonse=="+pieLabelName);
	//var pieLabelName=['New','Approved By CEIR Admin','Pending Approval From CEIR Admin','Rejected by CEIR Admin','Rejected By System','Withdrawn By User','Withdrawn By CEIR Admin'];
	var backgroundColors=['#512DA8','#008B8B','#F20515','#4682B4','#8B4513','#006400','#7C0378','#696969','#800080','#9400D3','#FFFF00','#7E57C2'];
	var backgroundHoverColors=['#512DA8','#008B8B','#F20515','#4682B4','#8B4513','#006400','#7C0378','#696969','#800080','#9400D3','#FFFF00','#7E57C2'];
	var rowData = [];
	var allData = new Map();
	var dataSetList = [];
	for(var i=0;i<response['rowData'].length;i++)
	{
	    for( var j=0; j<pieLabelName.length; j++ )
	    {
	      if( allData.has( pieLabelName[j] ) ){
	        rowData = allData.get( pieLabelName[j] );
	      }
	      else{
	    	  rowData = [];
	      }
	      
	      if(response['rowData'][i][pieLabelName[j]]==null || response['rowData'][i][pieLabelName[j]]=="null"){
	    	  rowData.push(0);  
	      }
	      else{
	    	  rowData.push(response['rowData'][i][pieLabelName[j]]);  
	      }
	      //console.log(rowData);;	
	      	allData.set( pieLabelName[j], rowData );
	    }
	    date.push(response['rowData'][i]['Date']);
	}

	for( var j=0; j<pieLabelName.length; j++ ){
		  dataSetList.push( {
			label: pieLabelName[j],
			backgroundColor: backgroundColors[j],
			hoverBackgroundColor: backgroundHoverColors[j],
			data: allData.get( pieLabelName[j] )
		});

	}
	/*
	$("#expGrievanceStatus").unbind("click").click(function(){
        var data = response['rowData'];
        if(data == '')
            return;
        JSONToCSVConvertor(data, "Report", true);
    });*/
	
	$("#"+GraphExcel).unbind("click").click(function(){
        var data = JSON.stringify(response['rowData']);
        //console.log(JSON.stringify(data));
        if(data == '')
            return;
        JSONToCSVConvertor(data, reportName, true);

    });
    	var bar_ctx = document.getElementById(''+id+'');
    	var bar_chart = new Chart(bar_ctx, {
    	    type: ''+chartType+'',
    	    data: {
    	        labels: date,
    	        datasets:dataSetList
    	    },
    	    options: {
    	        responsive: false,
        	    maintainAspectRatio: false,
    	     		animation: {
    	        	duration: 10,
    	        	onComplete:captureLineImage
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
    	          	stacked: false,
    	          	scaleLabel: {
    	                display: true,
    	                labelString: 'Count'
    	              },
    	            
    	            gridLines: { display: false },
    	            }],
    	          yAxes: [{ 
    	          	stacked: true,
    	          	scaleLabel: {
    	                display: true,
    	                labelString: 'Date'
    	              },
    	            }],
    	        }, // scales
    	        legend: {display: true}
    	    } // options
    	   }
    	);
    	
    	function captureLineImage(){  
            var url=bar_chart.toBase64Image();
            /*document.getElementById("grievanceBarImg").href=url;*/
            $("#"+GraphImageId).attr("href",url);
            }
    	
    	$('div#initialloader').delay(300).fadeOut('slow');    
}

function operatorList(){
	var operatorList=[];
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		async: false,
	headers:
	{ 'X-CSRF-TOKEN': token }
	});
	$.getJSON('./getDropdownList/OPERATORS', function(data) {
		for (i = 0; i < data.length; i++) {
			operatorList.push((data[i].interp));
			
		}
		
	});
	return operatorList;
}

