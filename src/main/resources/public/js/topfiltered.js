/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	"use strict";

	var jsonDataFilteredDomains = commons.getRestResponse(commons.context + "groupbyfiltereddomains", commons.jsonDataType);
	var data = commons.textResponseToArray(jsonDataFilteredDomains, "Advertiser");

	var totalFiltered = commons.getRestResponse(commons.context + "countallfiltered", "text");
	var distinctFiltered = commons.getRestResponse(commons.context + "countdistinctfiltered", "text");

	$("#total_advertisers").text(totalFiltered);
	$("#distinct_advertisers").text(distinctFiltered);
	var barChart = new google.visualization.BarChart(document.getElementById("top_filtered_bar"));
	barChart.draw(data, commons.optionsBarChart);
}