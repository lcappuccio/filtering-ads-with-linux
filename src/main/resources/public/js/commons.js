/* eslint no-undef: "error" */
/* global $, google */

var commons = {};

commons.context = "restapi/";

commons.jsonDataType = "json";

commons.textDataType = "text";

commons.spinnerOptions = {
	lines: 11, // The number of lines to draw
	length: 30, // The length of each line
	width: 11, // The line thickness
	radius: 40, // The radius of the inner circle
	scale: 1, // Scales overall size of the spinner
	corners: 1, // Corner roundness (0..1)
	color: "#000", // #rgb or #rrggbb or array of colors
	opacity: 0.3, // Opacity of the lines
	rotate: 0, // The rotation offset
	direction: 1, // 1: clockwise, -1: counterclockwise
	speed: 1, // Rounds per second
	trail: 44, // Afterglow percentage
	fps: 20, // Frames per second when using setTimeout() as a fallback for CSS
	zIndex: 2e9, // The z-index (defaults to 2000000000)
	className: "spinner", // The CSS class to assign to the spinner
	top: "50%", // Top position relative to parent
	left: "50%", // Left position relative to parent
	shadow: false, // Whether to render a shadow
	hwaccel: false, // Whether to use hardware acceleration
	position: "absolute" // Element positioning
}

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

commons.optionsLineChart = {
	height: 600,
	fontSize: 12,
	chartArea: {
		top: 80
	},
	legend: {
		position: "none"
	},
	animation: {
		startup: true,
		duration: 1000,
		easing: "out"
	},
	vAxis: {
		minValue: 0
	},
	hAxis: {
		slantedText: true,
		slantedTextAngle: 45
	},
	pointSize: 5
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

commons.getRestResponse = function (restUrl, restResponseType) {
	"use strict";

	return $.ajax({
		url: restUrl,
		dataType: restResponseType,
		async: false
	}).responseText;
};

commons.textResponseToArray = function (responseText, columnName) {
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