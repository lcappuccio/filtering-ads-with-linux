google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function getJsonData(restUrl) {

	var jsonData = $.ajax({
		url: restUrl,
		dataType: "json",
		async: false
	}).responseText;

	return jsonData;
}

function textResponseToArray(responseText, columnName) {

	var jsonData = JSON.parse(responseText);
	var jsonArray = [];
	jsonArray.push([columnName, "TOTAL"]);

	$.each(jsonData, function (key, value) {
		var array = [];
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});
	return new google.visualization.arrayToDataTable(jsonArray);
}

function drawChart() {

	var jsonRequestTypes = getJsonData("logarchiver/groupbyquerytype");
	var jsonRequestDomains = getJsonData("logarchiver/counttoprequests");

	var jsonHourData = textResponseToArray(jsonRequestTypes, "QUERY_TYPE");
	var jsonDayData = textResponseToArray(jsonRequestDomains, "QUERY_DOMAIN");

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