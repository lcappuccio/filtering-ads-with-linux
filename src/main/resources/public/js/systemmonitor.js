/* eslint no-undef: "error" */
/* global $, google, commons */

function timeConversion(millisec) {
	"use strict";

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
		return days + " Days";
	}
}

function formatBytes(bytes, decimals) {
	"use strict";

	if (bytes === 0) {
		return "0 Byte";
	}
	var k = 1000; // or 1024 for binary
	var dm = decimals + 1 || 3;
	var sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
	var i = Math.floor(Math.log(bytes) / Math.log(k));

	return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
}

function getDatabaseStatus() {
	"use strict";

	var jsonData = commons.getRestResponse("health", commons.jsonDataType);
	var jsonLines = $.parseJSON(jsonData);
	var databaseStatus = jsonLines.db.status;

	return ("UP" === databaseStatus);
}

function getDiskInfo() {
	"use strict";

	var jsonData = commons.getRestResponse("health", commons.jsonDataType);
	var jsonLines = $.parseJSON(jsonData);
	var totalDisk = jsonLines["diskSpace"]["total"];
	var freeDisk = jsonLines["diskSpace"]["free"];
	var diskFreePercentage = 100 - ((freeDisk / totalDisk) * 100);

	return [diskFreePercentage, formatBytes(totalDisk, 1), formatBytes(freeDisk, 1), formatBytes(totalDisk, 1)];
}

function getHeapPercentUsed() {
	"use strict";

	var jsonData = commons.getRestResponse("metrics", commons.jsonDataType);
	var jsonLines = $.parseJSON(jsonData);
	var heap = jsonLines["heap"];
	var heapUsed = jsonLines["heap.used"];

	return (heapUsed / heap) * 100;
}

function getLoadPercentAverage() {
	"use strict";

	var jsonData = commons.getRestResponse("metrics", commons.jsonDataType);
	var jsonLines = $.parseJSON(jsonData);
	var systemLoadAverage = jsonLines["systemload.average"];
	var systemProcessors = jsonLines["processors"];

	return (systemLoadAverage / parseInt(systemProcessors)) * 100;
}

function getMemPercentUsed() {
	"use strict";

	var jsonData = commons.getRestResponse("metrics", commons.jsonDataType);
	var jsonLines = $.parseJSON(jsonData);
	var mem = jsonLines["mem"];
	var memFree = jsonLines["mem.free"];

	return (memFree / mem) * 100;
}

function getUptime() {
	"use strict";

	var jsonData = commons.getRestResponse("metrics", commons.jsonDataType);
	var jsonLines = $.parseJSON(jsonData);
	var uptime = jsonLines["instance.uptime"];
	var uptimeDate = new Date(uptime);

	return timeConversion(uptimeDate);
}

function setRefreshForGauges(chart, gaugesData, options, refreshInMillis) {
	"use strict";

	setInterval(function () {
		gaugesData.setValue(0, 1, getDiskInfo()[0]);
		chart.draw(gaugesData, options);
	}, refreshInMillis);
	setInterval(function () {
		gaugesData.setValue(1, 1, getMemPercentUsed());
		chart.draw(gaugesData, options);
	}, refreshInMillis);
	setInterval(function () {
		gaugesData.setValue(2, 1, getHeapPercentUsed());
		chart.draw(gaugesData, options);
	}, refreshInMillis);
	setInterval(function () {
		gaugesData.setValue(3, 1, getLoadPercentAverage());
		chart.draw(gaugesData, options);
	}, refreshInMillis);
}

function drawChart() {
	"use strict";

	var diskInfo = getDiskInfo();

	var gaugesData = google.visualization.arrayToDataTable([
		["Label", "Value"],
		["Disk %", diskInfo[0]],
		["Memory", getMemPercentUsed()],
		["Heap", getHeapPercentUsed()],
		["Load", getLoadPercentAverage()]
	]);

	var options = {
		width: 800,
		height: 150,
		yellowFrom: 80,
		yellowTo: 90,
		redFrom: 90,
		redTo: 100,
		minorTicks: 5,
		majorTicks: ["0", "20", "40", "60", "80", "100"]
	};

	$("#uptime").text(getUptime());
	$("#disk_free").text(diskInfo[2]);
	$("#disk_total").text(diskInfo[3]);
	if (getDatabaseStatus()) {
		$("#database_status").addClass("glyphicon glyphicon-ok-circle");
	} else {
		$("#database_status").addClass("glyphicon glyphicon-remove-circle");
	}

	commons.hideLoadingGears();

	var chart = new google.visualization.Gauge(document.getElementById("gauges"));
	chart.draw(gaugesData, options);

	setRefreshForGauges(chart, gaugesData, options, 1000);

}

google.charts.load("current", {"packages": ["gauge"]});
google.charts.setOnLoadCallback(drawChart);