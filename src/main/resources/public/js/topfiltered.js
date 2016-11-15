/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	"use strict";

	var jsonDataFilteredDomains = commons.getRestResponse("logarchiver/groupbyfiltereddomains", "json");
	var jsonDataFilteredDomainsLines = JSON.parse(jsonDataFilteredDomains);

	var totalFiltered = commons.getRestResponse("logarchiver/countallfiltered", "text");
	var distinctFiltered = commons.getRestResponse("logarchiver/countdistinctfiltered", "text");

	var jsonArray = [];
	jsonArray.push(["Advertiser", "TOTAL"]);

	$.each(jsonDataFilteredDomainsLines, function (key, value) {
		var array = [];
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});

	var data = new google.visualization.arrayToDataTable(jsonArray);

	$("#total_advertisers").text(totalFiltered);
	$("#distinct_advertisers").text(distinctFiltered);
	var barChart = new google.visualization.BarChart(document.getElementById("top_filtered_bar"));
	barChart.draw(data, commons.optionsBarChart);
}