commons = {};

commons.textResponseToArray = function(responseText, columnName) {
	"use strict";

	var jsonData = JSON.parse(responseText);
	var jsonArray = [];
	jsonArray.push([columnName, "TOTAL"]);

	$.each(jsonData, function (key, value) {
		var array = [];
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});
	return new google.visualization.arrayToDataTable(jsonArray);
};

commons.getRestResponse = function(restUrl, restResponseType) {
	"use strict";

	return $.ajax({
		url: restUrl,
		dataType: restResponseType,
		async: false
	}).responseText;
};