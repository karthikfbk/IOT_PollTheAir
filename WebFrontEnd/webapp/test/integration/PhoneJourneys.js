jQuery.sap.require("sap.ui.qunit.qunit-css");
jQuery.sap.require("sap.ui.thirdparty.qunit");
jQuery.sap.require("sap.ui.qunit.qunit-junit");
QUnit.config.autostart = false;

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
		"poll/the/air/test/integration/NavigationJourneyPhone",
		"poll/the/air/test/integration/NotFoundJourneyPhone",
		"poll/the/air/test/integration/BusyJourneyPhone"
	], function () {
		QUnit.start();
	});
});