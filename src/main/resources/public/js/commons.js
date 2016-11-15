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

commons.optionsBarChart = {
	width: 800,
	height: 600,
	fontSize: 12,
	chartArea: {
		top: 5
	},
	legend: {
		position: "none"
	},
	animation: {
		startup: true,
		duration: 1000,
		easing: "out"
	}
};

commons.optionsPieChart = {
	width: 600,
	height: 600,
	fontSize: 12,
	chartArea: {
		top: 5
	},
	sliceVisibilityThreshold: 0.04
};