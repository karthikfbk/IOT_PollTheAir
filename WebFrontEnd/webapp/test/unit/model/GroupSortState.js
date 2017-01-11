sap.ui.define([
		"poll/the/air/model/GroupSortState",
		"sap/ui/model/json/JSONModel"
	], function (GroupSortState, JSONModel) {
	"use strict";

	QUnit.module("GroupSortState - grouping and sorting", {
		beforeEach: function () {
			this.oModel = new JSONModel({});
			// System under test
			this.oGroupSortState = new GroupSortState(this.oModel, function() {});
		}
	});

	QUnit.test("Should always return a sorter when sorting", function (assert) {
		// Act + Assert
		assert.strictEqual(this.oGroupSortState.sort("Latitude").length, 1, "The sorting by Latitude returned a sorter");
		assert.strictEqual(this.oGroupSortState.sort("Name").length, 1, "The sorting by Name returned a sorter");
	});

	QUnit.test("Should return a grouper when grouping", function (assert) {
		// Act + Assert
		assert.strictEqual(this.oGroupSortState.group("Latitude").length, 1, "The group by Latitude returned a sorter");
		assert.strictEqual(this.oGroupSortState.group("None").length, 0, "The sorting by None returned no sorter");
	});


	QUnit.test("Should set the sorting to Latitude if the user groupes by Latitude", function (assert) {
		// Act + Assert
		this.oGroupSortState.group("Latitude");
		assert.strictEqual(this.oModel.getProperty("/sortBy"), "Latitude", "The sorting is the same as the grouping");
	});

	QUnit.test("Should set the grouping to None if the user sorts by Name and there was a grouping before", function (assert) {
		// Arrange
		this.oModel.setProperty("/groupBy", "Latitude");

		this.oGroupSortState.sort("Name");

		// Assert
		assert.strictEqual(this.oModel.getProperty("/groupBy"), "None", "The grouping got reset");
	});
});