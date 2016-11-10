google.charts.load("current", {"packages": ["corechart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
	var jsonData = $.ajax({
		url: "logarchiver/counttopclients",
		dataType: "json",
		async: false
	}).responseText;

	var jsonLines = JSON.parse(jsonData);

	var jsonArray = [];
	jsonArray.push(["Client", "TOTAL"]);

	$.each(jsonLines, function (key, value) {
		var array = [];
		$.each(value, function (key2, value2) {
			array.push(value2);
		});
		jsonArray.push(array);
	});

	var data = new google.visualization.arrayToDataTable(jsonArray);

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

	var pieChart = new google.visualization.PieChart(document.getElementById("top_clients_pie"));
	pieChart.draw(data, optionsPie);

	var barChart = new google.visualization.BarChart(document.getElementById("top_clients_bar"));
	barChart.draw(data, optionsBar);
}