/* global $, google */

google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function getRestResponse(urlRest, responseDataType) {
	"use strict";

	return $.ajax({
		url: urlRest,
		dataType: responseDataType,
		async: false
	}).responseText;
}

function drawChart() {
	"use strict";

	var jsonDataFilteredDomains = getRestResponse("logarchiver/groupbyfiltereddomains", "json");
	var jsonDataFilteredDomainsLines = JSON.parse(jsonDataFilteredDomains);

	var totalFiltered = getRestResponse("logarchiver/countallfiltered", "text");
	var distinctFiltered = getRestResponse("logarchiver/countdistinctfiltered", "text");

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

	$("#total_advertisers").text(totalFiltered);
	$("#distinct_advertisers").text(distinctFiltered);
	var barChart = new google.visualization.BarChart(document.getElementById("top_filtered_bar"));
	barChart.draw(data, optionsBar);
}