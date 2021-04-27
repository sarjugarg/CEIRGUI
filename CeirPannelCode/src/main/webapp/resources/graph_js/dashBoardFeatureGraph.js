var featureId=48;
function featureDashboardGraph() {
/*	var currentTime = new Date()
	var month = ("0" + (currentTime.getMonth() + 1)).slice(-2)
	var day = ("0" + (currentTime.getDate())).slice(-2)
	var year = currentTime.getFullYear();
	var endDate=year+"-"+month+"-"+day;
	var startDate=year+"-"+month+"-"+(day-15);
	*/
	var featureFlag =null;
	var graphRequest=null;
	[41,44,16,57].forEach(function(reportnameId) {
		if(reportnameId==41){
			featureFlag="Stock";
	 graphRequest={
			"columns": [
				"Date",
				"Stock Status",
				"Count"
				],

			"reportnameId": reportnameId,
			"groupBy": "Stock Status",
			"file" : 0,
			"pageSize" :1000,
			"pageNo" :0,
			"typeFlag": 2,
			 "dayDataLimit":15,
			 "featureId":parseInt(featureId),
			    "userType":$("body").attr("data-roleType"),
			    "userId" : parseInt($("body").attr("data-userID"))
	}
	}
		else if(reportnameId==44){
			featureFlag="Consignment";
			graphRequest={
						"columns": [
							"Date",
							"Consignment Status",
							"Count"
							],

						"reportnameId": reportnameId,
						"groupBy": "Consignment Status",
						"file" : 0,
						"pageSize" :1000,
						"pageNo" :0,
						"typeFlag": 2,"dayDataLimit":15,
						 "featureId":parseInt(featureId),
						    "userType":$("body").attr("data-roleType"),
						    "userId" : parseInt($("body").attr("data-userID"))
				}			
		}
else if(reportnameId==16){
	featureFlag="Grievance";
			graphRequest={
						"columns": [
							"Date",
							"Grievance Status",
							"Count"
							],

						"reportnameId": reportnameId,
						"groupBy": "Grievance Status",
						"file" : 0,
						"pageSize" :1000,
						"pageNo" :0,
						"typeFlag": 2,"dayDataLimit":15,
						 "featureId":parseInt(featureId),
						    "userType":$("body").attr("data-roleType"),
						    "userId" : parseInt($("body").attr("data-userID"))
				}			
		}
		
else if(reportnameId==57){
	featureFlag="UserType";
	graphRequest={
				"columns": [
					"Date",
					"User Type",
					"Count"
					],

				"reportnameId": reportnameId,
				"groupBy": "User Type",
				"file" : 0,
				"pageSize" :1000,
				"pageNo" :0,
				"typeFlag": 2,"dayDataLimit":15,
				 "featureId":parseInt(featureId),
				    "userType":$("body").attr("data-roleType"),
				    "userId" : parseInt($("body").attr("data-userID"))
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
		async:false,
		url : './brandModel/data/'+featureFlag,
		contentType : "application/json",
		data : JSON.stringify(graphRequest),
		/*beforeSend : function() {
			$("#loading-image").show();
		},*/
		success : function(data) {
			var response = data;
			var labelSet=null;
			if(reportnameId==41){
				
				labelSet=setLabelByID(4 , 4);
				graph(response,'horizontalBarGraph','horizontalBar','User Login HorizontalBar Graph',labelSet,'stockStatusImg','expStockStatus','Stock_Status');
				
				
			}
			else if(reportnameId==44){
				
				labelSet=setLabelByID(3 , 4);
				graph(response,'ConsignmentBarGraph','horizontalBar','User Login HorizontalBar Graph',labelSet,'consignmentStatusImg','expConsignmentStatus','Consignment_Status');
				
				
			}
				
            else if(reportnameId==16){
            	
            	labelSet=setLabelByID(6 , 4);
            	graph(response,'grievanceStatusWise','horizontalBar','User Login HorizontalBar Graph',labelSet,'grievanceBarImg','expGrievanceStatus','Grievance_Status');
				
            }
            	else if(reportnameId==57){
            	
            	labelSet=UserTypeList();
            	graph(response,'grievanceUserWise','horizontalBar','User Login HorizontalBar Graph',labelSet,'grievanceUserImg','expUserStatus','Users_Grievance');
				
            }
		},
		error : function() {
		}
	});
	});
}



function graph(response,id,chartType,chartTitle,pieLabelName,GraphImageId,GraphExcel,reportName)
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
    	          	stacked: true,
    	          	scaleLabel: {
    	                display: true,
    	                labelString: 'Count'
    	              },
    	            
    	            gridLines: { display: false },
    	            ticks: {
    	                precision: 0
    	              }
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

$('div#initialloader').delay(300).fadeOut('slow');

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

function UserTypeList(){
	var userTypeList=[];
	$.getJSON('./registrationUserType', function(data) {
		for (i = 0; i < data.length; i++) {
			userTypeList.push(data[i].usertypeName);
		}
	});
	return userTypeList;
}
