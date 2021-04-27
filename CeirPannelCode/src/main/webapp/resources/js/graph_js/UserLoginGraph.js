function userloginGraph() {
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
	[18].forEach(function(reportnameId) {
		if(reportnameId == 18){
			urlHit="./userLoginGraph";
	 graphRequest={

			"reportnameId": 18,
			"file" : 0,
			"pageSize" :15,
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
		url :urlHit ,
		contentType : "application/json",
		data : JSON.stringify(graphRequest),
		/*beforeSend : function() {
			$("#loading-image").show();
		},*/
		success : function(data) {
			var response = data;
			if(reportnameId == 18 ){
				graph(response,'lineGraph','line','User Login Line Graph')
				graph(response,'barGraph','bar','User Login Bar Graph')
			}
			
			//graph(response,'horizontalBarGraph','horizontalBar','User Login HorizontalBar Graph')	
			
		},
		error : function() {
		}
	});
	});
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

			"reportnameId": 50,
			"file" : 0,
			"pageSize" :1,
			"pageNo" :0,
			"typeFlag": 1
	}
	
	$.ajax({
		type : 'POST',
		url : './user/report/count',
		contentType : "application/json",
		data : JSON.stringify(graphRequest),
		success: function(data){
var i=0;
				Object.keys(data['rowData'][0]).map(function(key){ 
				if(key == 'Date'){
					$('#dateVal').text('Last Update Date: '+data['rowData'][0][key]);
				}
				else{
					$("#infoBox").append("<div class='round-circle-center-responsive'><div class='round-circle'><h6 class='right' style='width: 105px;'>"+key+"</h6><p class='circle-para right' style='position:absolute;margin-top:62px;width: 180px;margin-left: 5px;padding-right: 0px !important;'><b id="+i+++">"+data['rowData'][0][key]+"</b> </p><div class='icon-div center'><i class='fa fa-puzzle-piece test-icon' aria-hidden='true'></i></div></div>");
				
				}
				$('div#initialloader').delay(300).fadeOut('slow');

				
		});
		}
	});
	
	
	
	
//pie
		var graphRequest={

				"reportnameId": 18,
				"file" : 0,
				"pageSize" :1,
				"pageNo" :0,
			     "typeFlag": 2
		}
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajaxSetup({
			headers:
			{ 'X-CSRF-TOKEN': token }
		});
		$.ajax({
			type : 'POST',
			url : './userLoginGraph',
			contentType : "application/json",
			data : JSON.stringify(graphRequest),
			/*beforeSend : function() {
				$("#loading-image").show();
			},*/
			success : function(data) {
				var response = data;
				//graph(response,'pieGraph','pie','User Login Pie Graph')
				//graph(response,'donutGraph','doughnut','User Login Doughnut Graph')
				//graph(response,'gaugeGraph','gauge','User Login Doughnut Graph')

			},
			error : function() {
			}
		});

});




function graph(response,id,chartType,chartTitle)
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
	   	uniqueUsers.push(response['rowData'][i]['Unique user logged']);
		pieData.push(parseInt(response['rowData'][i]['Number of user logged']));
		pieData.push(parseInt(response['rowData'][i]['Unique user logged']));
		
		BlockedCount.push(response['rowData'][i]['Blocked Count']);
		PendingCount.push(response['rowData'][i]['Pending Count']);
		RecoverdCount.push(response['rowData'][i]['Recoverd Count']);
		StolenCount.push(response['rowData'][i]['Stolen Count']);
	    }
 
	//console.log("date: "+date);
	   //console.log("noOfUsers: "+noOfUsers);
	    //console.log("uniqueUserLogged: "+);	

    if(chartType=='pie' || chartType=='doughnut'){
    
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
          options: {
        	    responsive: false,
        	    maintainAspectRatio: false,
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
    	
    }
    else if(chartType=='gauge'){

        
        var ctx = document.getElementById(''+id+'').getContext('2d');
        var chart = new Chart(ctx, {
          // The type of chart we want to create
          type: 'doughnut',

          // The data for our dataset
          data: {
            labels: pieLabelName,
            datasets: [ {
            	 backgroundColor: [
            		   'rgba(255, 99, 132, 0.2)',
                       'rgba(54, 162, 235, 0.2)'],
                data: pieData
            }],
            borderWidth: 0
          },

          // Configuration options go here
          options: {
        	    responsive: false,
        	    maintainAspectRatio: false,
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
    	
    
    }
    
    else if(chartType == 'horizontalBar'){
    	var bar_ctx = document.getElementById(''+id+'');
    	var bar_chart = new Chart(bar_ctx, {
    	    type: ''+chartType+'',
    	    data: {
    	        labels: date,
    	        datasets: [
    	        	{
    	                label: "Number Of Users",
    	                backgroundColor: "#512DA8",
						hoverBackgroundColor: "#7E57C2",
    	                data: noOfUsers
    	            },
    	            {
    	                label: "Unique Users",
    	            	backgroundColor: "#FFA000",
						hoverBackgroundColor: "#FFCA28",
    	                data: uniqueUsers
    	            }]
    	    },
    	    options: {
    	        responsive: false,
        	    maintainAspectRatio: false,
    	     		animation: {
    	        	duration: 10,
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
    	          	stacked: true, 
    	            gridLines: { display: false },
    	            }],
    	          yAxes: [{ 
    	          	stacked: true
    	           
    	            }],
    	        }, // scales
    	        legend: {display: true}
    	    } // options
    	   }
    	);
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
        datasets: [ {
            label: "Number Of Users",
            backgroundColor:  'rgb(70, 191, 189)',
            borderColor: 'rgb(70, 191, 189)',
            data: noOfUsers
        },
        {
            label: "Unique Users",
          backgroundColor: 'rgb(235, 203, 138)',
            borderColor:  'rgb(235, 203, 138)',
            data: uniqueUsers
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
                   scaleLabel: {
   	                display: true,
   	                labelString: 'Date'
   	              },
                }],
                yAxes: [{
                   gridLines: {
                      display: false
                   },
                   scaleLabel: {
   	                display: true,
   	                labelString: 'Number Of Users'
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
    else if(chartType == 'line'){
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
        datasets: [ {
            label: "Number Of Users",
            backgroundColor:  'rgb(70, 191, 189)',
            borderColor: 'rgb(70, 191, 189)',
            data: noOfUsers
        },
        {
            label: "Unique Users",
          backgroundColor: 'rgb(235, 203, 138)',
            borderColor:  'rgb(235, 203, 138)',
            data: uniqueUsers
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
                scaleLabel: {
    	                display: true,
    	                labelString: 'Date'
    	              },
                   gridLines: {
                      display: false
                   }
                }],
                yAxes: [{
                scaleLabel: {
    	                display: true,
    	                labelString: 'Number Of Users'
    	              },
                   gridLines: {
                      display: false
                   }
                }]
             }           
             
    	}
    });
    
    function captureImage(){  
        var url=chart.toBase64Image();
        document.getElementById("pieImage").href=url;
        }
    
   
    }  
    else if( chartType == 'line'){
    	
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
	            borderColor:  'rgb(235, 203, 138)',
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

function setLabelByID(featureId,userTypeId){
	var res=[];
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		async: false,
	headers:
	{ 'X-CSRF-TOKEN': token }
	});
	$.getJSON('./getDropdownList/'+featureId+"/"+userTypeId, function(data) {
		
		for (i = 0; i < data.length; i++) {
			data[i].interp;
			res.push(data[i].interp);
		}
	
	});
	
	return res;
}
