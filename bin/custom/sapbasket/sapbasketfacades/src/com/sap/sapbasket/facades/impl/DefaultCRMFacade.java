/*axle*/
package com.sap.sapbasket.facades.impl;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import com.sap.sapbasket.core.model.CRMModel;
import de.hybris.platform.commercefacades.automation.data.CRMData;
import com.sap.sapbasket.core.service.impl.DefaultCRMService;
import com.sap.sapbasket.facades.CRMFacade;
import com.sap.sapbasket.facades.populators.CustomCRMPopulator;
public class DefaultCRMFacade implements CRMFacade
{
@Resource(name = "crmService")
DefaultCRMService defaultcrmService;
@Resource(name = "crmPopulator")
CustomCRMPopulator crmPopulator;
@Override
public List<CRMData> getCRM()
{
final List<CRMModel> crmModels = defaultcrmService.getCRM();
final List<CRMData> crmList = new ArrayList<CRMData>();
CRMData crmData = null;
for (final CRMModel crmModel : crmModels)
{
crmData = new CRMData();
crmPopulator.populate(crmModel, crmData);
crmList.add(crmData);
}
return crmList;
}
}
