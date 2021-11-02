// Shorthand for DELETE AJAX requests.
$.DELETE = function (url) {
	return $.ajax({
		url: url,
		type: 'DELETE',
		headers: getBearerTokenHeader()
	});
};

// Shorthand for POST AJAX requests with JSON.
// Same as $.post but with object-to-JSON conversion.
$.POST = function (url, data) {
	return $.ajax({
		url: url,
		type : 'POST',
		headers: getBearerTokenHeader(),
		data : JSON.stringify(data),
		contentType : 'application/json'
	});
};

$.GET = function (url) {
	return $.ajax({
		url: url,
		type : 'GET',
		headers: getBearerTokenHeader()
	});
};

$.PUT = function (url, data) {
	return $.ajax({
		url: url,
		type : 'PUT',
		headers: getBearerTokenHeader(),
		data : JSON.stringify(data),
		contentType : 'application/json'
	});
};

// Usage: $('form.person-edit').serializeObject();
// Goes through all fields in the form, and collects their values.
// returns object with variables and values that correspond to the form fields.
$.fn.serializeObject = function () {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function () {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};