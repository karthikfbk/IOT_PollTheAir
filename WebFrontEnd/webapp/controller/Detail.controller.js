/*global location */
sap.ui.define([
		"poll/the/air/controller/BaseController",
		"sap/ui/model/json/JSONModel",
		"poll/the/air/model/formatter",
		"sap/m/MessageBox"
	], function (BaseController, JSONModel, formatter,MessageBox) {
		"use strict";

		return BaseController.extend("poll.the.air.controller.Detail", {

			formatter: formatter,

			/* =========================================================== */
			/* lifecycle methods                                           */
			/* =========================================================== */

			onInit : function () {
				// Model used to manipulate control states. The chosen values make sure,
				// detail page is busy indication immediately so there is no break in
				// between the busy indication for loading the view's meta data
				var oViewModel = new JSONModel({
					busy : false,
					delay : 0,
					lineItemListTitle : this.getResourceBundle().getText("detailLineItemTableHeading")
				});

				var oCoordinates = new JSONModel({
					name : "",
					lat : 0,
					lon : 0,
					pollutiondata : 0
				});
				this.getRouter().getRoute("object").attachPatternMatched(this._onObjectMatched, this);
				
				this.setModel(oCoordinates, "oCord");
				this.setModel(oViewModel, "detailView");

				this.getOwnerComponent().getModel().metadataLoaded().then(this._onMetadataLoaded.bind(this));
				this.getView().byId("map_canvas").addStyleClass("mymap");
			},

			onAfterRendering : function(){
					if (!this.initialized) {
								this.initialized = true;
								this.geocoder = new google.maps.Geocoder();
								window.mapOptions = {                          
								center: new google.maps.LatLng(48.1351253, 11.581980599999952),
								zoom: 8,
								mapTypeId: google.maps.MapTypeId.ROADMAP
								};                           
								//This is basically for setting the initial position of the map, ie. Setting the coordinates, for the place by default
								
								this.map = new google.maps.Map(this.getView().byId("map_canvas").getDomRef(),mapOptions);
								this.uluru = {lat: 48.1351253, lng: 11.581980599999952};
								this.infowindow = new google.maps.InfoWindow;
								this.geocoder = new google.maps.Geocoder();
								this.marker = new google.maps.Marker({
								position: this.uluru,
								map: this.map
								});	
								
								google.maps.event.addListener(this.map, "click", this._handleMapClick.bind(this));
								}
			},
			/* =========================================================== */
			/* event handlers                                              */
			/* =========================================================== */

			_handleMapClick : function(event){
			var lolatitude = event.latLng.lat(); //calculates latitude of the point of click
			var lolongitude = event.latLng.lng();//calculates longitude of the point of click
			
			//var latlng = new google.maps.LatLng("latlng",lolatitude, lolongitude);
			var latlng = {lat: parseFloat(lolatitude), lng: parseFloat(lolongitude)};
			this._updateMap(latlng);
			this._updatePollutionData(latlng);
			},
			
			_updateMap : function(latlng){
			this.geocoder.geocode({'location': latlng}, function(results, status) {
				if (status === google.maps.GeocoderStatus.OK) {
				if (results[1]) {
				//Here result will consist of many result, but we have to take fist result //itself, since that would be the appropriate one
				this.map.setZoom(8);
				this.marker.setPosition(latlng);
				var address1 = results[1].formatted_address;
				var content = address1;
				this.getModel("oCord").setProperty('/name',content);
				this.getModel("oCord").setProperty('/lat',latlng.lat);
				this.getModel("oCord").setProperty('/lon',latlng.lng);
				this.infowindow.setContent(content);
				this.infowindow.open(this.map, this.marker);
				} else {
				MessageBox.alert('No results found');
				}
				} else {
				MessageBox.alert('Geocoder failed due to: ' + status);
				}
				
				}.bind(this));
				
			
			},
			
			_updatePollutionData : function(latlng){
				var olocalModel = this.getModel("oCord");
				var lat = latlng.lat.toString();
				var lng = latlng.lng.toString();
				var globaloDataModel = this.getOwnerComponent().getModel();
				//var oUrlParams = "$filter=Latitude eq " + lat + "d" + " and Longitude eq " + lng + "d";
					var oUrlParams = "$filter=Pollution eq 100";
					
					
					var Lafilters = new Array(); // Don't normally do this but just for the example.
				
					var LatitudeFilter = new sap.ui.model.Filter({
                     path: "Latitude",
                     operator: sap.ui.model.FilterOperator.EQ,
                     value1: lat
            		});
            		
            		var LongitudeFilter = new sap.ui.model.Filter({
                     path: "Longitude",
                     operator: sap.ui.model.FilterOperator.EQ,
                     value1: lng
            		});
            		
					Lafilters.push(LatitudeFilter);
					Lafilters.push(LongitudeFilter);
					
				globaloDataModel.read("/Pollutiondatas",
				{
					context : null,
					urlParameters : null,
					filters : Lafilters,
					async : true,
					success : function(oData, response){
						var receivedData = oData.results;
						var PolVal = [];
						for(var obj in receivedData){
							PolVal.push(receivedData[obj].Pollution);
						}
						if(PolVal.length == 0){
							olocalModel.setProperty("/pollutiondata", "<-Not Found->");
						}
						else{
							olocalModel.setProperty("/pollutiondata", PolVal.join(","));
						}
					}.bind(this),
					error : function(oError){
						
					}.bind(this)
				});
			},
			/**
			 * Event handler when the share by E-Mail button has been clicked
			 * @public
			 */
			onShareEmailPress : function () {
				var oViewModel = this.getModel("detailView");

				sap.m.URLHelper.triggerEmail(
					null,
					oViewModel.getProperty("/shareSendEmailSubject"),
					oViewModel.getProperty("/shareSendEmailMessage")
				);
			},


			/**
			 * Updates the item count within the line item table's header
			 * @param {object} oEvent an event containing the total number of items in the list
			 * @private
			 */
			onListUpdateFinished : function (oEvent) {
				var sTitle,
					iTotalItems = oEvent.getParameter("total"),
					oViewModel = this.getModel("detailView");

				// only update the counter if the length is final
				if (this.byId("lineItemsList").getBinding("items").isLengthFinal()) {
					if (iTotalItems) {
						sTitle = this.getResourceBundle().getText("detailLineItemTableHeadingCount", [iTotalItems]);
					} else {
						//Display 'Line Items' instead of 'Line items (0)'
						sTitle = this.getResourceBundle().getText("detailLineItemTableHeading");
					}
					oViewModel.setProperty("/lineItemListTitle", sTitle);
				}
			},

			/* =========================================================== */
			/* begin: internal methods                                     */
			/* =========================================================== */

			/**
			 * Binds the view to the object path and expands the aggregated line items.
			 * @function
			 * @param {sap.ui.base.Event} oEvent pattern match event in route 'object'
			 * @private
			 */
			_onObjectMatched : function (oEvent) {
				var sObjectId =  oEvent.getParameter("arguments").objectId;
				var latitude =  oEvent.getParameter("arguments").lati;
				var longitude =  oEvent.getParameter("arguments").longi;
				var pollution = oEvent.getParameter("arguments").polli;
				this.getModel().metadataLoaded().then( function() {
					var sObjectPath = this.getModel().createKey("Pollutiondatas", {
						Id :  sObjectId
					});
					this._bindView("/" + sObjectPath);
					
					var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
					this._updateMap(latlng);
					this.getModel("oCord").setProperty('/pollutiondata',pollution);
				}.bind(this));
			},
			
			_bindPol : function(sObjectId){
					
			},

			/**
			 * Binds the view to the object path. Makes sure that detail view displays
			 * a busy indicator while data for the corresponding element binding is loaded.
			 * @function
			 * @param {string} sObjectPath path to the object to be bound to the view.
			 * @private
			 */
			_bindView : function (sObjectPath) {
				// Set busy indicator during view binding
				var oViewModel = this.getModel("detailView");

				// If the view was not bound yet its not busy, only if the binding requests data it is set to busy again
				oViewModel.setProperty("/busy", false);

				this.getView().bindElement({
					path : sObjectPath,
					events: {
						change : this._onBindingChange.bind(this),
						dataRequested : function () {
							oViewModel.setProperty("/busy", true);
						},
						dataReceived: function () {
							oViewModel.setProperty("/busy", false);
						}
					}
				});
			},

			_onBindingChange : function () {
				var oView = this.getView(),
					oElementBinding = oView.getElementBinding();

				// No data for the binding
				if (!oElementBinding.getBoundContext()) {
					this.getRouter().getTargets().display("detailObjectNotFound");
					// if object could not be found, the selection in the master list
					// does not make sense anymore.
					this.getOwnerComponent().oListSelector.clearMasterListSelection();
					return;
				}

				var sPath = oElementBinding.getPath(),
					oResourceBundle = this.getResourceBundle(),
					oObject = oView.getModel().getObject(sPath),
					sObjectId = oObject.Id,
					sObjectName = oObject.Name,
					oViewModel = this.getModel("detailView");

				this.getOwnerComponent().oListSelector.selectAListItem(sPath);

				oViewModel.setProperty("/shareSendEmailSubject",
					oResourceBundle.getText("shareSendEmailObjectSubject", [sObjectId]));
				oViewModel.setProperty("/shareSendEmailMessage",
					oResourceBundle.getText("shareSendEmailObjectMessage", [sObjectName, sObjectId, location.href]));
			},

			_onMetadataLoaded : function () {
				/*
				// Store original busy indicator delay for the detail view
				var iOriginalViewBusyDelay = this.getView().getBusyIndicatorDelay(),
					oViewModel = this.getModel("detailView"),
					oLineItemTable = this.byId("lineItemsList"),
					iOriginalLineItemTableBusyDelay = oLineItemTable.getBusyIndicatorDelay();

				// Make sure busy indicator is displayed immediately when
				// detail view is displayed for the first time
				oViewModel.setProperty("/delay", 0);
				oViewModel.setProperty("/lineItemTableDelay", 0);

				oLineItemTable.attachEventOnce("updateFinished", function() {
					// Restore original busy indicator delay for line item table
					oViewModel.setProperty("/lineItemTableDelay", iOriginalLineItemTableBusyDelay);
				});

				// Binding the view will set it to not busy - so the view is always busy if it is not bound
				oViewModel.setProperty("/busy", true);
				// Restore original busy indicator delay for the detail view
				oViewModel.setProperty("/delay", iOriginalViewBusyDelay);*/
			}

		});

	}
);