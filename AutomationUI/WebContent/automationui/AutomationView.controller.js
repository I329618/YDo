sap.ui
		.controller(
				"automationui.AutomationView",
				{

					/**
					 * Called when a controller is instantiated and its View
					 * controls (if available) are already created. Can be used
					 * to modify the View before it is displayed, to bind event
					 * handlers and do other one-time initialization.
					 * 
					 * @memberOf automationui.AutomationView
					 */
					onInit : function() {
						this.setModelData();
						var dataTypeConverterModel = new sap.ui.model.json.JSONModel();
						$.ajax({
									type : 'GET',
									url : 'http://localhost:9001/codegenerationwebservices/v2/sapbasket/ydo/getconverterjson',
									contentType : 'application/json; charset=utf-8',
									success : function(response) {
										dataTypeConverterModel.setData(JSON
												.parse(response.str));
									},
									error : function(error) {
										console.log(error);
									}
								});
						this.getView().setModel(dataTypeConverterModel,"dataTypeConverterModel");
						var packageModel = new sap.ui.model.json.JSONModel() ;
						$.ajax({
							type : 'GET',
							url : 'https://localhost:9002/codegenerationwebservices/v2/sapbasket/ydo/getmodelpackagejson',
							contentType : 'application/json; charset=utf-8',
							success : function(response) {
								packageModel.setData(JSON
										.parse(response.str));
							},
							error : function(error) {
								console.log(error);
							}
						});
						this.getView().setModel(packageModel,"packageModel");
						
						
						var currentSelectionModel = new sap.ui.model.json.JSONModel();
						currentSelectionModel.setData({
							"currentSelection" : ""
						});
						this.getView().setModel(currentSelectionModel,
								"currentSelectionModel");
					},

					/**
					 * Similar to onAfterRendering, but this hook is invoked
					 * before the controller's View is re-rendered (NOT before
					 * the first rendering! onInit() is used for that one!).
					 * 
					 * @memberOf automationui.AutomationView
					 */
					// onBeforeRendering: function() {
					//
					// },
					/**
					 * Called when the View has been rendered (so its HTML is
					 * part of the document). Post-rendering manipulations of
					 * the HTML could be done here. This hook is the same one
					 * that SAPUI5 controls get after being rendered.
					 * 
					 * @memberOf automationui.AutomationView
					 */
					// onAfterRendering: function() {
					//
					// },
					/**
					 * Called when the Controller is destroyed. Use this one to
					 * free resources and finalize activities.
					 * 
					 * @memberOf automationui.AutomationView
					 */
					// onExit: function() {
					//
					// }
					setModelData : function() {
						var formInputModel = new sap.ui.model.json.JSONModel();
						this.getView().setModel(formInputModel,
								"formInputModel");
						formInputModel.setData({
							autoCreate : false,
							autoGenerate : false,
							addAttribute : false,
							abstractItem : false
						});
						this.getDataTypeFromService();
						var dataTypeCombo = new sap.ui.model.json.JSONModel();
						/*
						 * this.getView().setModel(dataTypeCombo,
						 * "dataTypeCombo"); dataTypeCombo.setData({ dataTypes : [ {
						 * dataName : 'String' }, { dataName : 'Integer' }, {
						 * dataName : 'Date' }, { dataName : 'Boolean' }, {
						 * dataName : 'CRMKit' }, { dataName : 'Cart' }, {
						 * dataName : 'Other' } ] });
						 */
						var persistanceModel = new sap.ui.model.json.JSONModel();
						persistanceModel.setData({
							persistances : [ {
								types : 'property'
							}, {
								types : 'dynamic'
							} ]
						});
						this.getView().setModel(persistanceModel,
								"persistanceModel");
						var propertyArrModel = new sap.ui.model.json.JSONModel();
						this.getView().setModel(propertyArrModel,
								"propertyArrModel");
						propertyArrModel.setData({
							properties : []
						});
						var modifiersMultiCombo = new sap.ui.model.json.JSONModel();
						this.getView().setModel(modifiersMultiCombo,
								"modifiersMultiCombo");
						modifiersMultiCombo.setData({
							modifiers : [ {
								modName : 'read'
							}, {
								modName : 'write'
							}, {
								modName : 'search'
							}, {
								modName : 'removable'
							}, {
								modName : 'partof'
							}, {
								modName : 'initial'
							}, {
								modName : 'optional'
							}, {
								modName : 'private'
							}, {
								modName : 'unique'
							} ]
						});
					},

					handleAddProperties : function(oEvent) {
						var propertyArrModel = this.getView().getModel(
								"propertyArrModel");
						var propertyArrData = propertyArrModel.getData();
						var propertyObj = {
							qualifier : "",
							dataType : "",
							extensionName : "",
							persistence : "",
							twinFeild : "",
							dataBeanCanonicalName : "",
							converterName : "",
							aliasName : "",
							redeclare : false,
							modelPackage:""
						};
						propertyArrData.properties.push(propertyObj);
						propertyArrModel.setData(propertyArrData);
					},
					handleCheckBoxSelect : function(oEvent) {
						var formInputModel = this.getView().getModel(
								"formInputModel");
						var sId = oEvent.getSource().sId;
						if (sId.indexOf("generateCheck") !== -1) {
							formInputModel.setProperty("/autoGenerate",
									oEvent.mParameters.selected);
						}
						if (sId.indexOf("AutoCreate") !== -1) {
							formInputModel.setProperty("/autoCreate",
									oEvent.mParameters.selected);
						}
						if (sId.indexOf("addAttributeCB") !== -1) {
							formInputModel.setProperty("/addAttribute",
									oEvent.mParameters.selected);
						}
						if (sId.indexOf("abstractItemCB") !== -1) {
							formInputModel.setProperty("/abstractItem",
									oEvent.mParameters.selected);
							if (oEvent.mParameters.selected === true) {
								this.getView().byId("jaloPackageTextbox")
										.setEnabled(true);
							} else {
								this.getView().byId("jaloPackageTextbox")
										.setEnabled(false);
							}
						}
					},

					handleDeleteQualifier : function(oEvent) {
						var propertyArrModel = this.getView().getModel(
								"propertyArrModel");
						var oSource = oEvent.getSource();
						var prop = oSource._getPropertiesToPropagate()
						if (prop) {
							var oBindingContext = prop.oBindingContexts;
							var sPath = oBindingContext.propertyArrModel
									.getPath()
						}
						var data = propertyArrModel.getObject("/properties");
						this.removeElementInArray(sPath, data)
						propertyArrModel.refresh();
					},

					removeElementInArray : function(sPath, data) {
						var lastIndex = sPath.lastIndexOf("/");
						var index = parseInt(sPath.substr(lastIndex + 1));
						data.splice(index, 1);
					},

					onPressSave : function(oEvent) {
						var propertyArrModel = this.getView().getModel(
								"propertyArrModel");
						var propertyArrData = propertyArrModel.getData();
						var formInputModel = this.getView().getModel(
								"formInputModel");
						var formInputData = formInputModel.getData();
						formInputData.properties = propertyArrData.properties;
						$.ajax({
							type : 'GET',
							url : 'http://localhost:8080/Testing/Automation/',
							data : {
								createData : JSON.stringify(formInputData)
							},
							dataType : 'json',
							contentType : 'application/json; charset=utf-8',
							success : function(response) {
								alert(response.result);
							},
							error : function(error) {
								console.log(error);
							}
						});
					},

					handleDatatypeChange : function(oEvent) {
						var oSource = oEvent.getSource();
						var propertyArrModel = this.getView().getModel(
								"propertyArrModel");
						var dataTypeTextPath = oSource.getBindingContext(
								"propertyArrModel").getPath()
								+ "/dataType";
						var dataTypeText = oSource.getSelectedItem().getText();
						
						var packageModel = this.getView().getModel(
						"packageModel");
						propertyArrModel.setProperty(oSource.getBindingContext(
								"propertyArrModel").getPath()+"/modelPackage",
							packageModel.getProperty("/"+dataTypeText));
						
						propertyArrModel.setProperty(dataTypeTextPath,
								dataTypeText);
						var currentSelectionModel = this.getView().getModel(
								"currentSelectionModel");
						currentSelectionModel
								.setProperty(
										"/currentSelection",
										oEvent.oSource.oPropagatedProperties.oBindingContexts.propertyArrModel.sPath);
						var oDataTypePath = "/" + dataTypeText + "Data";
						var dataTypeConverterModel = this.getView().getModel(
								"dataTypeConverterModel");
						if(dataTypeText === "String" || dataTypeText === "Integer" || dataTypeText === "Boolean" || dataTypeText === "Other" || dataTypeText === "Date")
							{
								return;
							}
						if (dataTypeConverterModel.getProperty(oDataTypePath) === undefined) {
							//var converterModel = new sap.ui.model.json.JSONModel();
							var converterData = [];
							for ( var f in dataTypeConverterModel.getData()) {
								var path = "/" + f;
								var d = dataTypeConverterModel
										.getProperty(path);
								for (var i = 0; i < d.length; i++) {
									var conJson = dataTypeConverterModel
											.getProperty(path)[i].converterName;
									if (conJson.indexOf(dataTypeText) > -1) {
										converterData
												.push(dataTypeConverterModel
														.getProperty(path)[i]);
										console.log(dataTypeConverterModel
														.getProperty(path)[i]);
									}
								}
							}
							if(converterData.length === 0) { return; }
							//converterModel.setData(converterData);
							dataTypeConverterModel.setProperty(oDataTypePath,converterData);
							
						
						} 
							var oView = this.getView();
							var oDataTypeDialog = oView
									.byId("dataTypeFragment");
							if (!oDataTypeDialog) {
								oDataTypeDialog = sap.ui
										.xmlfragment(
												oView.getId(),
												"automationui.fragments.DataTypeFragment",
												oView.getController());
								oView.addDependent(oDataTypeDialog);
							}
							oDataTypeDialog.open();
							var odataTypeList = this.getView().byId(
									"dataTypeList");
							odataTypeList.setModel(dataTypeConverterModel);
							odataTypeList.bindItems({
								path : oDataTypePath,
								template : new sap.m.StandardListItem({
									title : "{converterName}"
								})
							});
						

					},

					handleDataTypeFragmentSave : function(oEvent) {

						// add selected to json model
						var propertyArrModel = this.getView().getModel(
								"propertyArrModel");
						var currentSelectionModel = this.getView().getModel(
								"currentSelectionModel");
						var id = currentSelectionModel
								.getProperty("/currentSelection");
						var dataTypeConverterModel = this.getView().getModel(
								"dataTypeConverterModel");

						var odataTypeList = this.getView().byId("dataTypeList");
						propertyArrModel.setProperty(id + "/converterName",
								odataTypeList.getSelectedItem().getTitle());
						propertyArrModel.setProperty(id + "/aliasName",
								dataTypeConverterModel
										.getProperty(odataTypeList
												.getSelectedItem()
												.getBindingContext().sPath
												+ "/aliasName"));
						propertyArrModel.setProperty(id
								+ "/dataBeanCanonicalName",
								dataTypeConverterModel
										.getProperty(odataTypeList
												.getSelectedItem()
												.getBindingContext().sPath
												+ "/dataBeanCanonicalName"));
						// close dialog
						var oView = this.getView();
						var oDataTypeDialog = oView.byId("dataTypeFragment");
						if (oDataTypeDialog) {
							oDataTypeDialog.close();
						}

					},

					handleDataTypeListFinished : function() {
						var firstItem = this.getView().byId("dataTypeList")
								.getItems()[0];
						this.getView().byId("dataTypeList").setSelectedItem(
								firstItem, true);
					},
					getDataTypeFromService : function() {
						var that = this;
						var dataTypeCombo = new sap.ui.model.json.JSONModel();

						$
								.ajax({
									type : 'GET',
									url : 'http://localhost:9001/codegenerationwebservices/v2/sapbasket/ydo/getcomposedtypesjson',
									contentType : 'application/json; charset=utf-8',
									success : function(response) {
										var dataTypeCombo = JSON
												.parse(response.str);
										var index = [];
										for ( var x in dataTypeCombo) {
											index.push({
												key : dataTypeCombo[x],
												value : x
											});
										}
										var extras = [ {
											value : 'String',
											key : "String"
										}, {
											value : 'Integer',
											key : "Integer"
										}, {
											value : 'Date',
											key : "Date"
										}, {
											value : 'Boolean',
											key : "Boolean"
										}, {
											value : 'Other',
											key : "Other"
										} ];
										for ( var x in extras) {
											index.push({
												key : extras[x].key,
												value : extras[x].value
											});
										}
										dataTypeCombo = {
											"index" : index
										};
										var dataTypesModel = new sap.ui.model.json.JSONModel();
										dataTypesModel.setData(dataTypeCombo);
										dataTypesModel.setSizeLimit(Infinity);
										that.getView().setModel(dataTypesModel,
												"dataTypeCombo");
									},
									error : function(error) {
										console.log(error);
									}
								});
					}
				});