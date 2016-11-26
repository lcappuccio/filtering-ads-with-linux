/*eslint no-undef: "error"*/
/* global $, commons */

function drawRow(rowData) {
    var row = $("<tr>");
    $("#dhcp_leases_table").append(row);
    row.append($("<td class=col-md-1>" + rowData.hostname + "</td>"));
    row.append($("<td class=col-md-1>" + rowData.vendor + "</td>"));
    row.append($("<td class=col-md-1>" + rowData.ipAddress + "</td>"));
    row.append($("<td class=col-md-1>" + rowData.macAddress + "</td>"));
    row.append($("<td class=col-md-1>" + rowData.leaseExpireDate + "</td>"));
    row.append("</tr>");
}

function drawTable(data) {
    for (var i = 0; i < data.length; i++) {
        drawRow(data[i]);
    }
}

$(document).ready(function () {
    $.get(commons.context + "listdhcpleases", function (data) {
        drawTable(data);
    });
});