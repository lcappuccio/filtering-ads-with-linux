/*eslint no-undef: "error"*/
/* global $, commons, jQuery */

function addIgnoredDomain() { // eslint-disable-line 
    var findByNameString = document.getElementById("addDomainTextField").value;
    $.ajax({
        data: jQuery.param({domain: findByNameString}),
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        url: commons.context + "addignoreddomain",
        type: "POST",
        success() {
            location.reload();
        }
    });
}

function removeIgnoredDomain(domainToRemove) { // eslint-disable-line
    $.ajax({
        data: jQuery.param({domain: domainToRemove}),
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        url: commons.context + "removeignoreddomain",
        type: "POST",
        success() {
            location.reload();
        }
    });
}

$(document).ready(function () {
    commons.hideLoadingGears();
});