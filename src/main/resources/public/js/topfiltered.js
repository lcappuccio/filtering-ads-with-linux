/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	"use strict";

	var jsonDataFilteredDomains = commons.getRestResponse("logarchiver/groupbyfiltereddomains", "json");
	var data = commons.textResponseToArray(jsonDataFilteredDomains, "Advertiser");

	var totalFiltered = commons.getRestResponse("logarchiver/countallfiltered", "text");
	var distinctFiltered = commons.getRestResponse("logarchiver/countdistinctfiltered", "text");

	$("#total_advertisers").text(totalFiltered);
	$("#distinct_advertisers").text(distinctFiltered);
	var barChart = new google.visualization.BarChart(document.getElementById("top_filtered_bar"));
	barChart.draw(data, commons.optionsBarChart);
}