/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function getJsonData(restUrl) {
	"use strict";

	return $.ajax({
		url: restUrl,
		dataType: "json",
		async: false
	}).responseText;
}

function textResponseToArray(responseText, columnName) {
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
}

function drawChart() {
	"use strict";

	var jsonDataByHour = getJsonData("logarchiver/dailybyhour");
	var jsonDataByDay = getJsonData("logarchiver/monthlybyday");

	var jsonHourData = textResponseToArray(jsonDataByHour, "Hour");
	var jsonDayData = textResponseToArray(jsonDataByDay, "Day");

	var options = {
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

	var chartByHour = new google.visualization.LineChart(document.getElementById("advertisers_by_hour"));
	var chartByDay = new google.visualization.LineChart(document.getElementById("advertisers_by_day"));

	chartByHour.draw(jsonHourData, options);
	chartByDay.draw(jsonDayData, options);
}