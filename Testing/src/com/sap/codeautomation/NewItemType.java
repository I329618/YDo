package com.sap.codeautomation;

import com.sap.hybris.GenerateCode;
import com.sap.hybris.HybrisActions;
import com.sap.jsonmodels.InputData;

public class NewItemType {
	public static void CreateNewItemType(InputData requestParameters){
		GenerateCode.initialize(requestParameters);
		GenerateCode.generateSpringXML();
		GenerateCode.generateDataBeansXML();
		
		HybrisActions.AntCleanAll();
		GenerateCode.generateDaoInterface();
		GenerateCode.generateDaoClass();
		GenerateCode.generateDaoBean();
		GenerateCode.generateServiceInterface();
		GenerateCode.generateServiceClass();
		GenerateCode.generateServiceBean();
		GenerateCode.generateFacadeInterface();
		GenerateCode.generateFacadeClass();
		GenerateCode.generatefacadeBean();
		GenerateCode.generatePopulatorForNewItemType();
		GenerateCode.generatepopulatorBeanForNewItemType();
		GenerateCode.generateControllerForNewItemType();
		HybrisActions.AntCleanAll();
	}
}
