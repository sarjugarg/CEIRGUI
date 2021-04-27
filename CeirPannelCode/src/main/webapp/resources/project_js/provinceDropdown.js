function setDropdown(){

	$.getJSON('./getAllProvince', function(data) {
		$('#state').empty();
		var html='<option value="">Select Province</option>';
		$('#state').append(html);	
		
		for (i = 0; i < data.length; i++) {
			var html='<option value="'+data[i].province+'">'+data[i].province+'</option>';
			$('#state').append(html);	
			
		}
	});
	
}

function setDropdownProviance(state){
 
	$.getJSON('./getAllProvince', function(data) {
		$('#'+state).empty();
		var html='<option value="">Select Province</option>';
		$('#'+state).append(html);	
		
		for (i = 0; i < data.length; i++) {
			var html='<option value="'+data[i].province+'">'+data[i].province+'</option>';
			$('#'+state).append(html);	
			
		}
	});
	
}