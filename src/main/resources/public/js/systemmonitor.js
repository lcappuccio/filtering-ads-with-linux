google.charts.load("current", {"packages": ["gauge"]});
google.charts.setOnLoadCallback(drawChart);

function getJsonData() {

	var jsonData = $.ajax({
		url: "metrics",
		dataType: "json",
		async: false
	}).responseText;

	return jsonData;
}

function getMemPercentUsed() {

	var jsonData = getJsonData();

	var jsonLines = $.parseJSON(jsonData);
	var mem = jsonLines["mem"];
	var memFree = jsonLines["mem.free"];
	var memPercentUsed = (memFree / mem) * 100;

	return memPercentUsed;
}

function getHeapPercentUsed() {

	var jsonData = getJsonData();
	var jsonLines = $.parseJSON(jsonData);
	var heap =  jsonLines["heap"];
	var heapUsed = jsonLines["heap.used"];
	var heapPercentUsed = (heapUsed / heap) * 100;

	return heapPercentUsed;
}

function getLoadPercentAverage() {

	var jsonData = getJsonData();
	var jsonLines = $.parseJSON(jsonData);
	var systemLoadAverage = jsonLines["systemload.average"];
	var systemProcessors = jsonLines["processors"];
	var systemLoadPercentage = (systemLoadAverage / parseInt(systemProcessors)) * 100;

	return systemLoadPercentage;
}

function drawChart() {

	var data = google.visualization.arrayToDataTable([
		["Label", "Value"],
		["Memory", getMemPercentUsed()],
		["Heap", getHeapPercentUsed()],
		["Load", getLoadPercentAverage()]
	]);

	var options = {
		width: 400, height: 120,
		redFrom: 90, redTo: 100,
		yellowFrom:75, yellowTo: 90,
		minorTicks: 5
	};

	var chart = new google.visualization.Gauge(document.getElementById("gauges"));
	chart.draw(data, options);

	setInterval(function() {
		data.setValue(0, 1, getMemPercentUsed());
		chart.draw(data, options);
	}, 1000);
	setInterval(function() {
		data.setValue(1, 1, getHeapPercentUsed());
		chart.draw(data, options);
	}, 1000);
	setInterval(function() {
		data.setValue(2, 1, getLoadPercentAverage());
		chart.draw(data, options);
	}, 1000);

}