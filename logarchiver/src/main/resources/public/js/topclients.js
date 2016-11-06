// Load the Visualization API and the corechart package.
google.charts.load('current', {'packages': ['corechart']});

// Set a callback to run when the Google Visualization API is loaded.
google.charts.setOnLoadCallback(drawChart);

// Callback that creates and populates a data table,
// instantiates the pie chart, passes in the data and
// draws it.
function drawChart() {
	var jsonData = $.ajax({
		url: "logarchiver/counttopclients",
		dataType: "json",
		async: false
	}).responseText;

	var jsonLines = JSON.parse(jsonData);

	console.log(jsonLines);

	var jsonArray = [];
	jsonArray.push(['Client', 'TOTAL']);

	$.each(jsonLines, function (key, value) {
		var array = []
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});

	console.log(jsonArray);

	var data = new google.visualization.arrayToDataTable(jsonArray);

	console.log(data);

	var options = {
		title: 'Top Clients',
		width: 640,
		height: 480,
		sliceVisibilityThreshold: .04
	};

	var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
	chart.draw(data, options);
}