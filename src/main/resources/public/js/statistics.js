/* eslint no-undef: "error" */
/* global google, commons */

function drawChart() {
	"use strict";

	var jsonDataByHour = commons.getRestResponse(commons.context + "dailybyhour", commons.jsonDataType);
	var jsonHourData = commons.textResponseToArray(jsonDataByHour, "Hour", commons.jsonDataType);

	var jsonDataByDay = commons.getRestResponse(commons.context + "monthlybyday", commons.jsonDataType);
	var jsonDayData = commons.textResponseToArray(jsonDataByDay, "Day", commons.jsonDataType);

	commons.hideLoadingGears();

	var chartByHour = new google.visualization.LineChart(document.getElementById("advertisers_by_hour"));
	var chartByDay = new google.visualization.LineChart(document.getElementById("advertisers_by_day"));

	chartByHour.draw(jsonHourData, commons.optionsLineChart);
	chartByDay.draw(jsonDayData, commons.optionsLineChart);
}

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);