/*eslint no-undef: "error"*/
/* global google, commons */

function drawChart() {
	"use strict";

	var jsonRequestTypes = commons.getRestResponse(commons.context + "groupbyquerytype", commons.jsonDataType);
	var jsonRequestDomains = commons.getRestResponse(commons.context + "counttoprequests", commons.jsonDataType);

	var jsonHourData = commons.textResponseToArray(jsonRequestTypes, "QUERY_TYPE");
	var jsonDayData = commons.textResponseToArray(jsonRequestDomains, "QUERY_DOMAIN");

	var requestTypesPie = new google.visualization.PieChart(document.getElementById("query_types_pie"));
	var topRequestsBar = new google.visualization.BarChart(document.getElementById("top_requests_bar"));

	requestTypesPie.draw(jsonHourData, commons.optionsPieChart);
	topRequestsBar.draw(jsonDayData, commons.optionsBarChart);
}

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);