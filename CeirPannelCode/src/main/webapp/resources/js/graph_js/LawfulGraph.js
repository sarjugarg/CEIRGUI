function LawfulGraph() {
/*	var currentTime = new Date()
	var month = ("0" + (currentTime.getMonth() + 1)).slice(-2)
	var day = ("0" + (currentTime.getDate())).slice(-2)
	var year = currentTime.getFullYear();
	var endDate=year+"-"+month+"-"+day;
	var startDate=year+"-"+month+"-"+(day-15);
	*/
	var graphRequest=null;
	var chartID=null;
	var type=null;
	var title=null;
	var urlHit=null;
	var featureFlag =null;
	[61,60].forEach(function(reportnameId) {
		
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
		else if(reportnameId==60){
			 featureFlag ="Brand";
				graphRequest={
						"columns": [
							  "Date",
	                          "Brand Name",
	                          "Count"
							],
							"reportnameId": reportnameId,
							"lastDate": true,
							"file" : 0,
							"pageSize" :40,
							"pageNo" :0,
							"typeFlag": 2
				}
				chartID='Top5Stolen';
				type='pie';
				title='User Login Pie Graph';
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
				
				MostStolenGraph(response,'lostStolenGraph','line','User Login HorizontalBar Graph');
			}
			else if(reportnameId == 60 ){
				//labelSet=["Stolen Count","Recoverd Count","Blocked Count","Pending Count"];
				graphBrandName(response,chartID,type,title);
				
			}
			//graph(response,'horizontalBarGraph','horizontalBar','User Login HorizontalBar Graph')	
			
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
	  var PendingCount=[];
	  var RecoverdCount=[];
	  var StolenCount=[];
	
		for(var i=0;i<response['rowData'].length;i++){
			noOfUsers.push(response['rowData'][i]['Number of user logged']);
		   	 date.push(response['rowData'][i]['Date']);
			BlockedCount.push(response['rowData'][i]['Blocked Count']);
			PendingCount.push(response['rowData'][i]['Pending Count']);
			RecoverdCount.push(response['rowData'][i]['Recoverd Count']);
			StolenCount.push(response['rowData'][i]['Stolen Count']);
		    }
	    if( chartType == 'line'){
	    	
			$("#expLostStolenGraph").unbind("click").click(function(){
		        var data = response['rowData'];
		        if(data == '')
		            return;
		        
		        JSONToCSVConvertor(data, "Lost/Stolen_trend", true);
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
		            
		        },
		        {
		            label: "Recoverd Count",
		            borderColor: 'rgb(70, 191, 189)',
		            data: RecoverdCount,
		            fill: false
		            
		        },
		        {
		            label: "Blocked Count",
		            borderColor: '#D32F2F',
		            data: BlockedCount,
		            fill: false
		            
		        },
		        {
		            label: "Pending Count",
		            borderColor:  '#FFA000',
		            data: PendingCount,
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
		                       labelString: 'Device Count'
		                     }
		                }]
		             }           
		             
		    	}
		    });
		    function captureImage(){  
		        var url=chart.toBase64Image();
		        document.getElementById("lostStolenGraphImage").href=url;
		    }
			} 
	    $('div#initialloader').delay(300).fadeOut('slow');

}




function graphBrandName(response,id,chartType,chartTitle)
{


	
	var date1=[];
	var pieLabelName1=[];
	var pieData1=[];
	var expoData=[];
/*	response = [
	      {
	          "Date": "2020-10-20",
	          "Brand Name": "NA",
	          "Count": "1"
	        }
	      ];*/
	for(var i=0;i<response['rowData'].length;i++){
		
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
		$("#expTop5StolenGraph").unbind("click").click(function(){
	        var data = JSON.stringify(response['rowData']);
	        //console.log(JSON.stringify(data));
	        if(data == '')
	            return;
	        JSONToCSVConvertor(expoData, "Top5StolenBrand", true);

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
            document.getElementById("Top5StolenGraphImage").href=url;
          //  $("#Top5BrandName").attr("href",url);
            }
		
	
	}


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

			"reportnameId": 31,
			"file" : 0,
			"pageSize" :1,
			"pageNo" :0
	}
	
	$.ajax({
		type : 'POST',
		url : './lawful/report/count',
		contentType : "application/json",
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
						$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">0</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
					}
					
				
				}
				$('div#initialloader').delay(300).fadeOut('slow');

				
		});
		}
	});

});