/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	"use strict";

	var jsonDataByHour = commons.getRestResponse("logarchiver/dailybyhour", "json");
	var jsonDataByDay = commons.getRestResponse("logarchiver/monthlybyday", "json");

	var jsonHourData = commons.textResponseToArray(jsonDataByHour, "Hour", "json");
	var jsonDayData = commons.textResponseToArray(jsonDataByDay, "Day", "json");

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