/* eslint no-undef: "error" */
/* global $, google, commons */

function drawChart() {
	"use strict";

	var jsonDataFilteredDomains = commons.getRestResponse(commons.context + "groupbyfiltereddomains", commons.jsonDataType);
	var data = commons.textResponseToArray(jsonDataFilteredDomains, "Advertiser");

	var totalFiltered = commons.getRestResponse(commons.context + "countallfiltered", commons.textDataType);
	var distinctFiltered = commons.getRestResponse(commons.context + "countdistinctfiltered", commons.textDataType);

	commons.hideLoadingGears();

	$("#total_advertisers").text(totalFiltered);
	$("#distinct_advertisers").text(distinctFiltered);
	var barChart = new google.visualization.BarChart(document.getElementById("top_filtered_bar"));
	barChart.draw(data, commons.optionsBarChart);
}

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);