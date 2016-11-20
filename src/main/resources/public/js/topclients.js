/* eslint no-undef: "error" */
/* global google, commons */

function drawChart() {
	"use strict";

	var jsonData = commons.getRestResponse(commons.context + "counttopclients", commons.jsonDataType);
	var data = commons.textResponseToArray(jsonData, "Client");

	var pieChart = new google.visualization.PieChart(document.getElementById("top_clients_pie"));
	pieChart.draw(data, commons.optionsPieChart);

	var barChart = new google.visualization.BarChart(document.getElementById("top_clients_bar"));
	barChart.draw(data, commons.optionsBarChart);
}

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);