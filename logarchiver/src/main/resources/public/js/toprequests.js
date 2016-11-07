google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	var jsonData = $.ajax({
		url: "logarchiver/counttoprequests",
		dataType: "json",
		async: false
	}).responseText;

	var jsonLines = JSON.parse(jsonData);

	var jsonArray = [];
	jsonArray.push(["Requests", "TOTAL"]);

	$.each(jsonLines, function (key, value) {
		var array = [];
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});

	var data = new google.visualization.arrayToDataTable(jsonArray);

	var options = {
		title: "Top 20 Requests",
		width: 800,
		height: 600,
		fontSize: 12,
		sliceVisibilityThreshold: .04
	};

	var barChart = new google.visualization.BarChart(document.getElementById("top_requests_bar"));
	barChart.draw(data, options);
}