//Confirm if user wants to use the original file name

function validateInput() {
	var name = document.forms["uform"]["name"].value;

	if (name == null || name == "") {
		var ans = confirm("Do you want to save the file with original name?");
		if (ans)
			return true;
		else
			return false;

	}
}


//Prompt user to enter a filename while downloading
function check() {
	var name1 = document.forms["dform"]["dname"].value;
	if (name1 == null || name1 == "") 
	{
		alert("Please enter a filename (E.g: test.pdf, book1.txt)");
		return false;
	}
	else 
		return true;
}


//Enable the upload button only when a user selects a file to be uploaded.

$(document).ready(function() {
	$('input:file').change(function() {
		if ($(this).val()) {
			$('input:submit').attr('disabled', false);
		}
	});
});