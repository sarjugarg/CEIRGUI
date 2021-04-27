var userTypeID = $("body").attr("data-userTypeID");

$('#langlist').on('change', function() {
	var lang = $('#langlist').val();
	sessionStorage.setItem("sessionLang", lang);
	$('#mainArea').attr('src', function() {

		currentPageLocation = $(this).contents().get(0).location;
		var feature = $(this).contents().find("body").attr('data-id');
		sessionStorage.setItem("data-feature", feature);
		changeLanguage(lang);

		sessionStorage.setItem("currentPageLocation", currentPageLocation);
	});
	window.location.replace("?lang=" + lang);

});
var urlController;
if ($('.navData li a').attr("data-featureid") == 31
		|| $('.navData li a').attr("data-featureid") == 26
		|| $('.navData li a').attr("data-featureid") == 45
		|| $('.navData li a').attr("data-featureid") == 16) {

	urlController = $("body").attr("data-defaultLink");
} else {
	urlController = "./Home?FeatureId=1";
}

$('.navData li:nth-child(1)').addClass("active");
var featurID = sessionStorage.getItem("data-feature") == null ? $(
		'.navData li a').attr("data-featureid") : sessionStorage
		.getItem("data-feature");
var intialController = sessionStorage.getItem("currentPageLocation") == null ? urlController
		: sessionStorage.getItem("currentPageLocation");
$(document)
		.ready(
				function() {

					// var DB_LANG_VALUE= sessionStorage.getItem("sessionLang")
					// == null ? window.parent.$("body").attr("data-lang") :
					// sessionStorage.getItem("sessionLang");
					$("#section")
							.append(
									" <iframe name='mainArea' class='embed-responsive-item' id='mainArea' frameBorder='0' src="
											+ intialController
											+ " width='100%' onLoad='self.scrollTo(0,5)'></iframe>");
					// window.parent.$("body").attr("data-lang", DB_LANG_VALUE);
					var url = new URL(window.location.href);
					/* sessionStorage.getItem("sessionLang") */
					// var langParameter = url.searchParams.get("lang")== (null
					// || 'null') ? 'en' : url.searchParams.get("lang");
					var langParameter = url.searchParams.get("lang") == 'km' ? 'km'
							: 'en';

					window.parent.$('#langlist').val(langParameter);
					// dataByTag("copyright_footer","copyrightText",2);
					sessionStorage.removeItem("currentPageLocation");
					$('div#initialloader').delay(300).fadeOut('slow');
					isActive(featurID);
					sessionStorage.removeItem("data-feature");

					/*$("#manualDownload").attr(
							"onclick",
							"openPDF('./Consignment/ManualFileDownload?userTypeId="+userTypeID+"')"); */

					$(window).scrollTop(0);
					
					$.i18n().locale = langParameter;

					$.i18n().load({

						'en' : './resources/i18n/en.json',
						'km' : './resources/i18n/km.json'
					}).done(function() {
						var state = $("body").attr("data-user-state");
						if (state == '3') {
							$('#userState').text(""+$.i18n('state')+" : Enabled");

						}

						else if (state == '5') {
							$('#userState').text(""+$.i18n('state')+" : Disabled");

						}

					});
					});
					
// var password = document.getElementById("password");
// var confirm_password = document.getElementById("confirm_password");

// function validatePassword(){
// if(password.value != confirm_password.value) {
// confirm_password.setCustomValidity("Passwords Don't Match");
// } else {
// confirm_password.setCustomValidity('');
// }
// }
// password.onchange = validatePassword;
// confirm_password.onkeyup = validatePassword;

var cierRoletype = $("body").attr("data-usertype");
sessionStorage.setItem("cierRoletype", cierRoletype);
// $(".navData li:first").addClass("active");
$('.navData li').on('click', function() {
	$('.navData li:not(.inactive)').addClass("inactive");
	$('.navData li').removeClass("active");
	$(this).removeClass("inactive");
	$(this).addClass("active");
});

function changeLanguage(lang) {

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$.ajaxSetup({
		headers : {
			'X-CSRF-TOKEN' : token
		}
	});
	$.ajax({
		type : 'POST',
		url : './changeLanguage/' + lang,
		contentType : "application/json",
		dataType : 'html',
		success : function(data) {
		},
		error : function(xhr, ajaxOptions, thrownError) {
		}
	});
}

function isActive(feature) {
	$('.navData li:nth-child(1)').removeClass("active");
	$('.navData li a').each(function() {
		if ($(this).attr('data-featureid') == feature) {
			$(this).closest('li').addClass("inactive");
			$(this).closest('li').removeClass("active");
			$(this).closest('li').removeClass("inactive");
			$(this).closest('li').addClass("active");
		}
	})
}

function openEditPage(usertypeId) {
	var asTypeDropdown = $("#editLink");
	if (usertypeId != 0) {
		if (usertypeId == 4 || usertypeId == 5 || usertypeId == 6) {
			// return "./editProfile";

			asTypeDropdown.attr('href', contextpath + "/editProfile");

		} else {
			asTypeDropdown.attr('href', contextpath + "/editOthersProfile");
		}
	} else {

		return "";
	}

}

$("#rightSideToggle").click(function() {
	$("#rightSideToggle").removeAttr('id');
	$('#slide-out').css("display", "none");
	$('.side-nav.fixed').css("left", "0");
	$("#slide-out").toggle();
	$('.mdi-navigation-menu').attr('onclick', 'removeToggle()');
});

function removeToggle() {
	$('#slide-out').css("display", "none");
	$(".mdi-navigation-menu").removeAttr('onclick');
	$('.mdi-navigation-menu').attr('id', 'rightSideToggle');
}

$(function() {
	$('*').tooltip({
		track : true
	});
	$('*[title]').tooltip('disable');
});

var url = new URL(window.location.href);

$.i18n().locale = url.searchParams.get("lang") == 'km' ? 'km' : 'en';

$.i18n().load({

	'en' : './resources/i18n/en.json',
	'km' : './resources/i18n/km.json'
}).done(function() {
});
function InvalidMsg(textbox, type, msg) {
	var element = document.getElementById(textbox.id);
	if (element.validity.valueMissing) {
		if (type == "input") {
			element.setCustomValidity($.i18n('requiredMsg_input'));
		} else if (type == "date") {
			element.setCustomValidity($.i18n('requiredMsg_date'));
		} else if (type == "select") {
			element.setCustomValidity($.i18n('requiredMsg_select'));
		} else if (type == "fileType") {
			element.setCustomValidity($.i18n('requiredMsg_fileType'));
		} else if (type == "email") {
			element.setCustomValidity($.i18n('requiredMsg_email'));
		} else if (type == "checkbox"
				&& $('#' + textbox.id).is(":checked") == false) {

			element.setCustomValidity($.i18n('requiredMsg_checkbox'));
		}

	}

	else if (element.validity.patternMismatch) {

		if (type == "input" || type == "date" || type == "select"
				|| type == "fileType" || type == "email") {
			element.setCustomValidity(msg);
		}

		else if (type == "checkbox"
				&& $('#' + textbox.id).is(":checked") == true) {
			element.setCustomValidity('');
		}

		return false;
	} else {

		if (type == "input"
				|| type == "date"
				|| type == "select"
				|| type == "fileType"
				|| type == "email"
				|| (type == "checkbox" && $('#' + textbox.id).is(":checked") == true)) {
			element.setCustomValidity('');
		}

		return true;

	}
}

function InvalidRadioMsg(type) {

	var element = document.getElementById('deac');
	if (type == 'radio'
			&& $("input:radio[name='status']").is(":checked") == true) {
		element.setCustomValidity('');

	} else if (type == "radio"
			&& $("input:radio[name='status']").is(":checked") == false) {

		element.setCustomValidity($.i18n('requiredMsg_radio'));

	}
	return false;
}
