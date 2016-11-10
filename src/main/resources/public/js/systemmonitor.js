google.charts.load("current", {"packages": ["gauge"]});
google.charts.setOnLoadCallback(drawChart);

function getMetricsJsonData() {

	var jsonData = $.ajax({
		url: "metrics",
		dataType: "json",
		async: false
	}).responseText;

	return jsonData;
}

function getHealthJsonData() {

	var jsonData = $.ajax({
		url: "health",
		dataType: "json",
		async: false
	}).responseText;

	return jsonData;
}

function getMemPercentUsed() {

	var jsonData = getMetricsJsonData();

	var jsonLines = $.parseJSON(jsonData);
	var mem = jsonLines["mem"];
	var memFree = jsonLines["mem.free"];
	var memPercentUsed = (memFree / mem) * 100;

	return memPercentUsed;
}

function getHeapPercentUsed() {

	var jsonData = getMetricsJsonData();
	var jsonLines = $.parseJSON(jsonData);
	var heap = jsonLines["heap"];
	var heapUsed = jsonLines["heap.used"];
	var heapPercentUsed = (heapUsed / heap) * 100;

	return heapPercentUsed;
}

function getLoadPercentAverage() {

	var jsonData = getMetricsJsonData();
	var jsonLines = $.parseJSON(jsonData);
	var systemLoadAverage = jsonLines["systemload.average"];
	var systemProcessors = jsonLines["processors"];
	var systemLoadPercentage = (systemLoadAverage / parseInt(systemProcessors)) * 100;

	return systemLoadPercentage;
}

function getUptime() {

	var jsonData = getMetricsJsonData();
	var jsonLines = $.parseJSON(jsonData);
	var uptime = jsonLines["instance.uptime"];
	var uptimeDate = new Date(uptime);


	return timeConversion(uptimeDate);
}

function getDiskInfo() {

	var jsonData = getHealthJsonData();
	var jsonLines = $.parseJSON(jsonData);
	var totalDisk = jsonLines["diskSpace"]["total"];
	var freeDisk = jsonLines["diskSpace"]["free"];
	var diskFreePercentage = 100 - ((freeDisk / totalDisk) * 100);
	var diskInfo = [diskFreePercentage, formatBytes(totalDisk, 1), formatBytes(freeDisk, 1), formatBytes(totalDisk, 1)];

	return diskInfo;
}

function getDatabaseStatus() {

	var jsonData = getHealthJsonData();
	var jsonLines = $.parseJSON(jsonData);
	var databaseStatus = jsonLines["db"]["status"];

	return ("UP" === databaseStatus);
}

function timeConversion(millisec) {

	var seconds = (millisec / 1000).toFixed(1);

	var minutes = (millisec / (1000 * 60)).toFixed(1);

	var hours = (millisec / (1000 * 60 * 60)).toFixed(1);

	var days = (millisec / (1000 * 60 * 60 * 24)).toFixed(1);

	if (seconds < 60) {
		return seconds + " Sec";
	} else if (minutes < 60) {
		return minutes + " Min";
	} else if (hours < 24) {
		return hours + " Hrs";
	} else {
		return days + " Days"
	}
}

function formatBytes(bytes, decimals) {
	if (bytes == 0) return '0 Byte';
	var k = 1000; // or 1024 for binary
	var dm = decimals + 1 || 3;
	var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
	var i = Math.floor(Math.log(bytes) / Math.log(k));
	return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}

function drawChart() {

	var diskInfo = getDiskInfo();

	var gaugesData = google.visualization.arrayToDataTable([
		["Label", "Value"],
		["Disk %", diskInfo[0]],
		["Memory", getMemPercentUsed()],
		["Heap", getHeapPercentUsed()],
		["Load", getLoadPercentAverage()]
	]);

	var options = {
		width: 800, height: 150,
		redFrom: 85, redTo: 100,
		yellowFrom: 70, yellowTo: 85,
		minorTicks: 5
	};

	$("#uptime").text(getUptime());
	$("#disk_free").text(diskInfo[2]);
	$("#disk_total").text(diskInfo[3]);
	if (getDatabaseStatus()) {
		$("#database_status").addClass("glyphicon glyphicon-ok-circle");
	} else {
		$("#database_status").addClass("glyphicon glyphicon-remove-circle");
	}
	var chart = new google.visualization.Gauge(document.getElementById("gauges"));
	chart.draw(gaugesData, options);

	setInterval(function () {
		gaugesData.setValue(0, 1, getDiskInfo()[0]);
		chart.draw(gaugesData, options);
	}, 1000);
	setInterval(function () {
		gaugesData.setValue(1, 1, getMemPercentUsed());
		chart.draw(gaugesData, options);
	}, 1000);
	setInterval(function () {
		gaugesData.setValue(2, 1, getHeapPercentUsed());
		chart.draw(gaugesData, options);
	}, 1000);
	setInterval(function () {
		gaugesData.setValue(3, 1, getLoadPercentAverage());
		chart.draw(gaugesData, options);
	}, 1000);

}