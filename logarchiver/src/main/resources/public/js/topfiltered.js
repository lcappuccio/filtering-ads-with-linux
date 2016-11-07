google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	var jsonData = $.ajax({
		url: "logarchiver/groupbyfiltereddomains",
		dataType: "json",
		async: false
	}).responseText;

	var totalFiltered = $.ajax({
		url: "logarchiver/countallfiltered",
		dataType: "text",
		async: false
	}).responseText;

	var jsonLines = JSON.parse(jsonData);

	var jsonArray = [];
	jsonArray.push(["Advertiser", "TOTAL"]);

	$.each(jsonLines, function (key, value) {
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
		chartArea : {
			top: 5
		},
		animation:{
			startup: true,
			duration: 1000,
			easing: 'out'
		}
	};

	$("#total_advertisers").text(totalFiltered);
	var barChart = new google.visualization.BarChart(document.getElementById("top_filtered_bar"));
	barChart.draw(data, optionsBar);
}