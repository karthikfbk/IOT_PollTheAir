jQuery.sap.require("sap.ui.qunit.qunit-css");
jQuery.sap.require("sap.ui.thirdparty.qunit");
jQuery.sap.require("sap.ui.qunit.qunit-junit");
QUnit.config.autostart = false;

// We cannot provide stable mock data out of the template.
// If you introduce mock data, by adding .json files in your webapp/localService/mockdata folder you have to provide the following minimum data:
// * At least 3 Geolocations in the list
// * All 3 Geolocations have at least one GasindexDetails

sap.ui.require([
	"sap/ui/test/Opa5",
	"poll/the/air/test/integration/pages/Common",
	"sap/ui/test/opaQunit",
	"poll/the/air/test/integration/pages/App",
	"poll/the/air/test/integration/pages/Browser",
	"poll/the/air/test/integration/pages/Master",
	"poll/the/air/test/integration/pages/Detail",
	"poll/the/air/test/integration/pages/NotFound"
], function (Opa5, Common) {
	"use strict";
	Opa5.extendConfig({
		arrangements: new Common(),
		viewNamespace: "poll.the.air.view."
	});

	sap.ui.require([
		"poll/the/air/test/integration/MasterJourney",
		"poll/the/air/test/integration/NavigationJourney",
		"poll/the/air/test/integration/NotFoundJourney",
		"poll/the/air/test/integration/BusyJourney"
	], function () {
		QUnit.start();
	});
});