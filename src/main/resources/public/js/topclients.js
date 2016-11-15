/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	"use strict";

	var jsonData = commons.getRestResponse("logarchiver/counttopclients", "json");

	var jsonLines = JSON.parse(jsonData);

	var jsonArray = [];
	jsonArray.push(["Client", "TOTAL"]);

	$.each(jsonLines, function (key, value) {
		var array = [];
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});

	var data = new google.visualization.arrayToDataTable(jsonArray);

	var pieChart = new google.visualization.PieChart(document.getElementById("top_clients_pie"));
	pieChart.draw(data, commons.optionsPieChart);

	var barChart = new google.visualization.BarChart(document.getElementById("top_clients_bar"));
	barChart.draw(data, commons.optionsBarChart);
}