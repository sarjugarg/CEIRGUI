/*[

	'./resources/js/materialize.js',
	'./resources/custom_js/bootstrap.min.js',
	'./resources/js/plugins/perfect-scrollbar/perfect-scrollbar.min.js'
	].forEach(function(src) {
		$('body').append('<script type="text/javascript" src='+src+' async defer><\/script>');

	});
	

*/
function activeDeviceGraph() {
	[31,48,52,53,14].forEach(function(reportnameId) {
		var graphRequest=null;
		var chartID=null;
		var type=null;
		var title=null;
		var urlHit=null;
		var featureFlag =null;
		if(reportnameId == 31){
			graphRequest={
					"columns": [
						"Approved TAC",
						"Rejected TAC"
						],
						"reportnameId": reportnameId,
						"lastDate": true,
						"file" : 0,
						"pageSize" :1,
						"pageNo" :0,
						 "typeFlag": 1
			}
			chartID='pieGraph';
			type='pie';
			title='User Login Pie Graph';
			urlHit='./report/data';
		}

		
		else if(reportnameId == 52 ){
			featureFlag="Brand";
			graphRequest={

					"reportnameId": reportnameId,
					"lastDate": true,
					"file" : 0,
					"pageSize" :10,
					"pageNo" :0,
					 "typeFlag": 1
			}
			
			chartID='pieGraphBrandName';
			type='pie';
			title='User Login Pie Graph';
			urlHit='./brandModel/data/'+featureFlag;
		}
		else if(reportnameId == 53 ){
			featureFlag="Model";
			graphRequest={

					"reportnameId": reportnameId,
					"lastDate": true,
					"file" : 0,
					"pageSize" :10,
					"pageNo" :0,
					 "typeFlag": 1
			}
			
			chartID='pieGraphModelNumber';
			type='pie';
			title='User Login Pie Graph';
			urlHit='./brandModel/data/'+featureFlag;
		}
		else if(reportnameId == 14 ){
			graphRequest={
					"columns": [
						    "Date",
						    "Equipment Type",
						    "Count"
						  ],
					"reportnameId": reportnameId,
					"file" : 0,
					"pageSize" :45,
					"pageNo" :0,
					"lastDate": false,
					"groupBy":"Equipment Type",
					 "typeFlag": 2
						  
							
			}
			chartID='barGraph';
			type='bar';
			title='Types of Registered Devices';
			urlHit='./report/data?Type=registeredDeviceGraph';
		}
		else if(reportnameId == 48){
			graphRequest={

					"reportnameId": reportnameId,
					"file" : 0,
					"pageSize" :10,
					"pageNo" :0
			}
			chartID='lineGraph';
			type='line';
			title='User Login Line Graph';
			urlHit='./report/data';
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
				
				var response = JSON.parse(data);
				
				if(reportnameId==52){
					
					graphBrandName(response,chartID,type,title);
				}
				
				else if(reportnameId==53){
					
					graphTopModelNumber(response,chartID,type,title);
					}
				else if(reportnameId==14){
					
					graph(response,chartID,type,title);
					}
				else{
				
					graph(response,chartID,type,title);	
				}
			},
			error : function() {
			}
		});
	});

}


function graph(response,id,chartType,chartTitle)
{
	var imeiCount=[];
	var msisdnFrequency=[];
	var date=[];
	var pieLabelName=response['columns'];
	var pieData=[];
	var equipmentType=[];
	var count=[];
	
	var notKnown=[]; 
	var wearable=[]; 
	var wlanRouter=[]; 
	var handheld=[]; 
	var modem=[]; 
	var vehicle=[]; 
	var dongle=[]; 
	var iotDevice=[]; 
	var smartPhone=[]; 
	var mobileFeaturePhone=[]; 
	var na=[]; 
	var tablet=[]; 
	var connectedComputer=[]; 
	var portableIncludePDA=[]; 
	var eBook=[]; 
	var module=[]; 
	
	 if (chartType=='pie' ){
		for(var i=0;i<response['rowData'].length;i++){
			pieData.push(response['rowData'][i].approvedTAC);
			pieData.push(response['rowData'][i].rejectedTAC);
			date.push(response['rowData'][i].createdOn);
			msisdnFrequency.push(response['rowData'][i].msisdnFrequency);
			imeiCount.push(response['rowData'][i].imeiCount);
		}
	}else if(chartType=='line'){
		for(var i=0;i<response['rowData'].length;i++){
			pieData.push(response['rowData'][i].approvedTAC);
			pieData.push(response['rowData'][i].rejectedTAC);
			date.push(response['rowData'][i].createdOn);
			msisdnFrequency.push(response['rowData'][i].msisdnFrequency);
			imeiCount.push(response['rowData'][i].imeiCount);
		}
	}else if(chartType='bar'){
		for(var i=0;i<response['rowData'].length;i++){
			date.push(response['rowData'][i]['Date']);
			notKnown.push(response['rowData'][i]['NOT KNOWN']);
			wearable.push(response['rowData'][i]['Wearable']);
			wlanRouter.push(response['rowData'][i]['WLAN Router']);
			handheld.push(response['rowData'][i]['Handheld']);
			modem.push(response['rowData'][i]['Modem']);
			vehicle.push(response['rowData'][i]['Vehicle']);
			dongle.push(response['rowData'][i]['Dongle']);
			iotDevice.push(response['rowData'][i]['IoT Device']);
			smartPhone.push(response['rowData'][i]['Smartphone']);
			mobileFeaturePhone.push(response['rowData'][i]['Mobile Phone/Feature phone']);
			na.push(response['rowData'][i]['NA']);
			tablet.push(response['rowData'][i]['Tablet']);
			connectedComputer.push(response['rowData'][i]['Connected Computer']);
			portableIncludePDA.push(response['rowData'][i]['Portable(include PDA)']);
			eBook.push(response['rowData'][i]['e-Book']);
			module.push(response['rowData'][i]['Module']);
		}
	}
	
	if(chartType=='pie'){
/*

		['./resources/graph_js/chartjs-plugin-datalabel.js'].forEach(function(src) {
			$('body').append('<script type="text/javascript" id="pieJS" src='+src+' async defer><\/script>');

		});
		
*/
		$("#exportDeviceReport").unbind("click").click(function(){
	        var data = JSON.stringify(response['rowData']);
	        //console.log(JSON.stringify(data));
	        if(data == '')
	            return;
	        JSONToCSVConvertor(data, "Active_Device", true);

	    });
		var options = {
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
				      };
				    
		

		var ctx = document.getElementById(''+id+'').getContext('2d');

		var chart = new Chart(ctx, {
			// The type of chart we want to create
			type: ''+chartType+'',

			// The data for our dataset
			data: {
				labels: pieLabelName,
				datasets: [ {
					backgroundColor: [
						'#512DA8',
						'#D32F2F'],
						data: pieData
				}]
			},

			// Configuration options go here
			options: options
		});

		function captureLineImage(){  
            var url=chart.toBase64Image();
            //alert("urll="+url);
            document.getElementById("DeviceReport").href=url;
          //  $("#Top5BrandName").attr("href",url);
            }
	
	}


	else if(chartType == "line"){

		$("#exportLineDeviceReport").unbind("click").click(function(){
	        var data = JSON.stringify(response['rowData']);
	        //console.log(JSON.stringify(data));
	        if(data == '')
	            return;
	        JSONToCSVConvertor(data, "Active_Device", true);

	    });
		var ctx = document.getElementById(''+id+'').getContext('2d');
		var chart = new Chart(ctx, {
			// The type of chart we want to create
			type: ''+chartType+'',

			// The data for our dataset
			data: {
				labels: date,
				datasets: [ {
					label: "IMEI Count",
					backgroundColor:  'rgb(70, 191, 189)',
					borderColor: 'rgb(70, 191, 189)',
					data: imeiCount
				},
				{
					label: "MSISDN Frequency",
					backgroundColor: 'rgb(235, 203, 138)',
					borderColor:  'rgb(235, 203, 138)',
					data: msisdnFrequency
				}]
			},
			
			// Configuration options go here
			options: {
				responsive: false,
				maintainAspectRatio: false,
                   animation: {
    	        	onComplete:captureLineImage
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
    			},
				scales: {
					xAxes: [{
						gridLines: {
							display: false,
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
		
		function captureLineImage(){  
            var url=chart.toBase64Image();
            //alert("urll="+url);
            document.getElementById("LineDeviceReport").href=url;
          //  $("#Top5BrandName").attr("href",url);
            }
		}
	
	else if(chartType == 'bar'){
		$("#expLineBar").unbind("click").click(function(){
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
					label: "Not Known",
					backgroundColor: 'rgb(235, 203, 138)',
					borderColor:  'rgb(235, 203, 138)',
					data: notKnown
				},{
					label: "Wearable",
					backgroundColor: 'rgb(241, 15, 0)',
					borderColor:  'rgb(235, 203, 138)',
					data: wearable
				},{
					label: "WLAN Router",
					backgroundColor:  'rgb(70, 191, 189)',
					borderColor: 'rgb(70, 191, 189)',
					data: wlanRouter
				},{
					label: "Handheld",
					backgroundColor: 'rgb(241, 236, 0)',
					borderColor:  'rgb(235, 203, 138)',
					data: handheld
				},{
					label: "Modem",
					backgroundColor: 'rgb(241, 208, 111)',
					borderColor:  'rgb(235, 203, 138)',
					data: modem
				},{
					label: "Vehicle",
					backgroundColor: 'rgb(241, 159, 28)',
					borderColor:  'rgb(235, 203, 138)',
					data: vehicle
				},{
					label: "Dongle",
					backgroundColor: 'rgb(47, 161, 28)',
					borderColor:  'rgb(235, 203, 138)',
					data: dongle
				},{
					label: "IoT Device",
					backgroundColor: 'rgb(188, 112, 28)',
					borderColor:  'rgb(235, 203, 138)',
					data: iotDevice
				},{
					label: "Smartphone",
					backgroundColor: 'rgb(175, 239, 224)',
					borderColor:  'rgb(235, 203, 138)',
					data: smartPhone
				},{
					label: "Mobile Phone/Feature phone",
					backgroundColor: 'rgb(246, 191, 224)',
					borderColor:  'rgb(235, 203, 138)',
					data: mobileFeaturePhone
				}]
			},
			
			// Configuration options go here
			options: {
				responsive: false,
				maintainAspectRatio: false,
				animation: {
					onComplete: captureLineImage
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
						stacked: true, 
						scaleLabel: {
							display: true,
							labelString: 'Date'
						},
					}],
					yAxes: [{
						gridLines: {
							display: false
						},
						stacked: true ,
						scaleLabel: {
							display: true,
							labelString: 'Number Of Devices'
						},
					}]
				}           

			}
		});
function captureLineImage(){  
    var url=chart.toBase64Image();
    document.getElementById("lineBarImage").href=url;
    }
}
}

















//boxes
$(document).ready(function(){
	$('div#initialloader').fadeIn('fast');
	var url;

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var graphRequest="";
	[31,1,27].forEach(function(reportnameId) {
	$.ajaxSetup({
	headers:
	{ 'X-CSRF-TOKEN': token }
	});
	if(reportnameId==31){
	 graphRequest={

			"reportnameId": reportnameId,
			"file" : 0,
			"pageSize" :1,
			"pageNo" :0,
			"typeFlag": 1
	}
	}
	else if(reportnameId==1 || reportnameId==27){
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
		url : './report/count/'+reportnameId,
		contentType : "application/json",
		async: false,
		data : JSON.stringify(graphRequest),
		success: function(data){
		$("#expActiveDeviceTable").unbind("click").click(function(){
		        var result = JSON.stringify(data['rowData']);
		        if(result == '')
		            return;
		        
		        JSONToCSVConvertor(result, "Report", true);
		    });
var i=0;
				Object.keys(data['rowData'][0]).map(function(key){ 
				if(key == 'Date'){
					$('#dateVal').text('Last Update Date: '+data['rowData'][0][key]);
				}
				else{
					if(i == 0){ $('#firstTD').text(data['rowData'][0][key]);}
					else if(i == 1){$('#secondTD').text(data['rowData'][0][key]);}
					else if(i == 2){$('#thirdTD').text(data['rowData'][0][key]);}
					//alert(data['rowData'][0]['Total Stolen Device Count']);
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

/*
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
		Object.keys(data['rowData'][0]).map(function(key){ 
			if(key != 'Date'){
				
				$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b>"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
			}
			
		});
	}
});

*/

function graphBrandName(response,id,chartType,chartTitle)
{


	var imeiCount=[];
	var msisdnFrequency=[];
	var date1=[];
	var pieLabelName1=[];
	var pieData1=[];
	var expoData=[];
	
	for(var i=0;i<=5;i++){
		
		pieData1.push(parseInt(response['rowData'][i]['Count']));
		pieLabelName1.push(response['rowData'][i]['Brand Name']);
		expoData.push(response['rowData'][i]);
		//pieData.push(response['rowData'][i].rejectedTAC);
		//date1.push(response['rowData'][i].date);
	//	msisdnFrequency.push(response['rowData'][i].msisdnFrequency);
		//imeiCount.push(response['rowData'][i].imeiCount);
	}
	if(chartType=='pie'){
/*

		['./resources/graph_js/chartjs-plugin-datalabel.js'].forEach(function(src) {
			$('body').append('<script type="text/javascript" id="pieJS" src='+src+' async defer><\/script>');

		});
		
*/
		$("#exportBrandReport").unbind("click").click(function(){
	        var data = JSON.stringify(response['rowData']);
	        //console.log(JSON.stringify(data));
	        if(data == '')
	            return;
	        JSONToCSVConvertor(expoData, "Top5Brand", true);

	    });
		
		var ctx = document.getElementById(''+id+'').getContext('2d');
		
		var options = {
				responsive: false,
				maintainAspectRatio: false,
				animation: {
    	        	onComplete:captureLineImage
    	        },
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
		

		var chart = new Chart(ctx, {
			// The type of chart we want to create
			type: ''+chartType+'',

			// The data for our dataset
			data: {
				labels: pieLabelName1,
				datasets: [ {
					backgroundColor: [
						'#512DA8',
						'#D32F2F',
						'#808000',
						'#B8860B',
						'#006400',
						'#2F4F4F',
						'#008B8B',
						'#4682B4',
						'#8B4513',
						'#696969'],
						data: pieData1
				}]
			},

			// Configuration options go here

			 options : options
		});
		function captureLineImage(){  
            var url=chart.toBase64Image();
            //alert("urll="+url);
            document.getElementById("Top5BrandName").href=url;
          //  $("#Top5BrandName").attr("href",url);
            }
		
	
	}


}

function graphTopModelNumber(response,id,chartType,chartTitle)
{

   
	var imeiCount=[];
	var msisdnFrequency=[];
	var date1=[];
	var pieLabelName2=[];
	var pieData2=[];
	var expoData1=[];
	for(var i=0;i<=5;i++){
		
		pieData2.push(parseInt(response['rowData'][i]['Count']));
		pieLabelName2.push(response['rowData'][i]['Model Name']);
		expoData1.push(response['rowData'][i]);
		//pieData.push(response['rowData'][i].rejectedTAC);
		//date1.push(response['rowData'][i].date);
	//	msisdnFrequency.push(response['rowData'][i].msisdnFrequency);
		//imeiCount.push(response['rowData'][i].imeiCount);
	}
	if(chartType=='pie'){
		$("#exportModelReport").unbind("click").click(function(){
	        var data = JSON.stringify(response['rowData']);
	        //console.log(JSON.stringify(data));
	        if(data == '')
	            return;
	        JSONToCSVConvertor(expoData1, "Top5Model", true);

	    });

		['./resources/graph_js/chartjs-plugin-datalabel.js'].forEach(function(src) {
			$('body').append('<script type="text/javascript" id="pieJS" src='+src+' async defer><\/script>');

		});
		

		
		var ctx = document.getElementById(''+id+'').getContext('2d');
		var options = {
				responsive: false,
				maintainAspectRatio: false,
				animation: {
    	        	onComplete:captureLineImage
    	        },
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
		var chart = new Chart(ctx, {
			// The type of chart we want to create
			type: ''+chartType+'',

			// The data for our dataset
			data: {
				labels: pieLabelName2,
				datasets: [ {
					backgroundColor: [
						'#512DA8',
						'#D32F2F',
						'#808000',
						'#B8860B',
						'#006400',
						'#2F4F4F',
						'#008B8B',
						'#4682B4',
						'#8B4513',
						'#696969'],
						data: pieData2
				}]
			},

			// Configuration options go here
			 options : options		});

		function captureLineImage(){  
            var url=chart.toBase64Image();
            //alert("urll="+url);
            document.getElementById("Top5ModelName").href=url;
          //  $("#Top5BrandName").attr("href",url);
            }
	}


}

