package com.sap.codeautomation;

import com.sap.jsonmodels.InputData;
import com.sap.hybris.*;

public class AddAttributeToExistingType {
	public static void AddAttributes(InputData requestParameters){
		GenerateCode.initialize(requestParameters);
		GenerateCode.generateSpringXML();
		GenerateCode.generateDataBeansXML();
		HybrisActions.AntCleanAll();
		GenerateCode.generatePopulatorForAddingNewAttributeToExistingType();
		GenerateCode.generatepopulatorBeanForAddingNewAttributeToExistingType();
		GenerateCode.generateJspForm();
		GenerateCode.editExsistingController();
		GenerateCode.editExsistingControllertest();
		GenerateCode.generatereversePopulator();
		GenerateCode.generatereversepopulatorBeanForAddingNewAttributeToExistingType();
		GenerateCode.generateFormToDataClass();
		GenerateCode.editJsp1();
		GenerateCode.editJsp2();
		GenerateCode.editJsp3();
		HybrisActions.AntCleanAll();
	}
}
