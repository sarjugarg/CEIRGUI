//datepicker ID's to select date range
$( "#expectedDispatcheDateEdit,#expectedArrivaldateEdit" ).datepicker({
dateFormat: "yy-mm-dd"
});

//error message DIV's
$('#consignmentTableDIv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#registrationTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#greivanceTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#FieldTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#updateModal div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#typeAprroveTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#stolenRecoveryFormDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#operatorTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#deviceActivationTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#userManageTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#auditTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#userManageLibraryTable div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#CurrencyTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#PortTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#userManagementTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#userFeatureTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#userTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#dbTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')

$('#alertTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#ipLogTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')
$('#pendingTacTableDiv div:last').after('<p id="errorMsg" style="color: red;font-size: 12px;position: absolute;left: 40px;margin: 0;top: 122px;"class="left"></p>')


function checkDate(startDate,endDate) {
var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';
$.i18n().locale = lang;	
$.i18n().load( {
'en': '../resources/i18n/en.json',
'km': '../resources/i18n/km.json',
'en': './resources/i18n/en.json',
'km': './resources/i18n/km.json'
} ).done( function() { 
});
var input1 = startDate.value;
var input2 = endDate.value;
var currentTime = new Date()
var month = ("0" + (currentTime.getMonth() + 1)).slice(-2)
var day = ("0" + (currentTime.getDate())).slice(-2)
var year = currentTime.getFullYear();
var input3=year+"-"+month+"-"+day;

$('#errorMsgOnModal').text('');

var searchParams = new URLSearchParams(window.location.search);
//alert(window.location.href+"-----------"+searchParams) 
if(searchParams=='reqType=formPage'){

if (input2 == input1) {
$('#errorMsgOnModal').text('');
$('#'+endDate.id).css('border-color', '');
$('#submitFilter,#consignmentSubbmitButton,#filterFieldTable').removeClass( "eventNone" );


} 
else if(input2 < input1 && !input2==''){
$('#'+endDate.id).css('border-color', 'red'); 
$('#errorMsgOnModal').text($.i18n(endDate.id));
$('#submitFilter,#consignmentSubbmitButton,#filterFieldTable').addClass( "eventNone" );
$('#consignmentSubbmitButton').addClass( "eventNone" );
}
else{
$('#errorMsgOnModal').text('');
$('#'+endDate.id).css('border-color', '');
$('#submitFilter,#consignmentSubbmitButton').removeClass( "eventNone" );
}
}



else{
if((input1 <= input3) && (input2 <= input3)){
if (input2 == input1) {
$('#errorMsg').text('');
$('#'+endDate.id).css('border-color', '');
$('#submitFilter,#consignmentSubbmitButton,#filterFieldTable').removeClass( "eventNone" );


} 
else if(input2 < input1 && !input2==''){
$('#'+endDate.id).css('border-color', 'red');

$('#errorMsg').text($.i18n(endDate.id));
$('#submitFilter,#consignmentSubbmitButton,#filterFieldTable').addClass( "eventNone" );
$('#consignmentSubbmitButton').addClass( "eventNone" );
}

else{
$('#errorMsg').text('');
$('#'+endDate.id).css('border-color', '');
$('#submitFilter,#consignmentSubbmitButton').removeClass( "eventNone" );
}
}
else{
$('#'+endDate.id).css('border-color', 'red');
$('#errorMsg').text($.i18n('currentDateError'));
$('#submitFilter,#consignmentSubbmitButton,#filterFieldTable').addClass( "eventNone" );
$('#consignmentSubbmitButton').addClass( "eventNone" );
}
}

 if(input2 == input1){
	
	$('#expiryMessage').hide()
	$('#updateVisaButton').removeClass( "eventNone" );
}
 else if(input2 <= input1){
	
	 $('#expiryMessage').show();
	 $('#expiryMessage').text($.i18n('visaExpiryCheck'));
	 $('#updateVisaButton').addClass( "eventNone" );
	 
	
 }
 else if(input2 >= input1){
	 $('#expiryMessage').hide();
	 $('#updateVisaButton').removeClass( "eventNone" );
	
 }
}





// for modal
function checkDateOnModal(startDate,endDate) {
var lang=window.parent.$('#langlist').val() == 'km' ? 'km' : 'en';


$.i18n().locale = lang;	
$.i18n().load( {
'en': '../resources/i18n/en.json',
'km': '../resources/i18n/km.json',
'en': './resources/i18n/en.json',
'km': './resources/i18n/km.json'
} ).done( function() { 

});
var input_start = myStringToDate(startDate.value);
var input_end = myStringToDate(endDate.value);
$('#errorMsgOnModal').text('');
if (input_end.getTime() == input_start.getTime()) {
$('#errorMsgOnModal').text('');
$('#'+endDate.id).css('border-color', '');


} 
else if(input_end.getTime() < input_start.getTime()){
$('#'+endDate.id).css('border-color', 'red');
$('#errorMsgOnModal').text($.i18n(endDate.id));
$(':input[type="submit"]').addClass( "eventNone" );

}
else{
$('#errorMsgOnModal').text('');
$('#'+endDate.id).css('border-color', '');
$(':input[type="submit"]').removeClass( "eventNone" );
}

}