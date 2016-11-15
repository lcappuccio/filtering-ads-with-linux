/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	"use strict";

	var jsonRequestTypes = commons.getRestResponse("logarchiver/groupbyquerytype", "json");
	var jsonRequestDomains = commons.getRestResponse("logarchiver/counttoprequests", "json");

	var jsonHourData = commons.textResponseToArray(jsonRequestTypes, "QUERY_TYPE");
	var jsonDayData = commons.textResponseToArray(jsonRequestDomains, "QUERY_DOMAIN");

	var optionsPie = {
		width: 600,
		height: 600,
		fontSize: 12,
		chartArea: {
			top: 5
		},
		sliceVisibilityThreshold: .04
	};

	var optionsBar = {
		width: 800,
		height: 600,
		fontSize: 12,
		chartArea: {
			top: 5
		},
		legend: {
			position: "none"
		},
		animation: {
			startup: true,
			duration: 1000,
			easing: "out"
		}
	};

	var requestTypesPie = new google.visualization.PieChart(document.getElementById("query_types_pie"));
	var topRequestsBar = new google.visualization.BarChart(document.getElementById("top_requests_bar"));

	requestTypesPie.draw(jsonHourData, optionsPie);
	topRequestsBar.draw(jsonDayData, optionsBar);
}