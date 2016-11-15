/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	"use strict";

	var jsonDataByHour = commons.getRestResponse("logarchiver/dailybyhour", "json");
	var jsonDataByDay = commons.getRestResponse("logarchiver/monthlybyday", "json");

	var jsonHourData = commons.textResponseToArray(jsonDataByHour, "Hour", "json");
	var jsonDayData = commons.textResponseToArray(jsonDataByDay, "Day", "json");

	var chartByHour = new google.visualization.LineChart(document.getElementById("advertisers_by_hour"));
	var chartByDay = new google.visualization.LineChart(document.getElementById("advertisers_by_day"));

	chartByHour.draw(jsonHourData, commons.optionsLineChart);
	chartByDay.draw(jsonDayData, commons.optionsLineChart);
}