/*axle*/
package com.sap.sapbasket.facades.impl;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import com.sap.sapbasket.core.model.CRMKitModel;
import de.hybris.platform.commercefacades.automation.data.CRMKitData;
import com.sap.sapbasket.core.service.impl.DefaultCRMKitService;
import com.sap.sapbasket.facades.CRMKitFacade;
import com.sap.sapbasket.facades.populators.CustomCRMKitPopulator;
public class DefaultCRMKitFacade implements CRMKitFacade
{
@Resource(name = "crmkitService")
DefaultCRMKitService defaultcrmkitService;
@Resource(name = "crmkitPopulator")
CustomCRMKitPopulator crmkitPopulator;
@Override
public List<CRMKitData> getCRMKit()
{
final List<CRMKitModel> crmkitModels = defaultcrmkitService.getCRMKit();
final List<CRMKitData> crmkitList = new ArrayList<CRMKitData>();
CRMKitData crmkitData = null;
for (final CRMKitModel crmkitModel : crmkitModels)
{
crmkitData = new CRMKitData();
crmkitPopulator.populate(crmkitModel, crmkitData);
crmkitList.add(crmkitData);
}
return crmkitList;
}
}
