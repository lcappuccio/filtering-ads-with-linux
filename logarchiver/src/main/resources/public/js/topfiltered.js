google.charts.load('current', {'packages': ['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	var jsonData = $.ajax({
		url: "logarchiver/groupbyfiltereddomains",
		dataType: "json",
		async: false
	}).responseText;

	var jsonLines = JSON.parse(jsonData);

	var jsonArray = [];
	jsonArray.push(['Advertiser', 'TOTAL']);

	$.each(jsonLines, function (key, value) {
		var array = []
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});

	var data = new google.visualization.arrayToDataTable(jsonArray);

	var options = {
		title: 'Top Advertisers',
		width: 800,
		height: 480,
		sliceVisibilityThreshold: .04
	};

	var pieChart = new google.visualization.PieChart(document.getElementById('top_filtered_pie'));
	pieChart.draw(data, options);

	var barChart = new google.visualization.BarChart(document.getElementById("top_filtered_bar"));
	barChart.draw(data, options);
}