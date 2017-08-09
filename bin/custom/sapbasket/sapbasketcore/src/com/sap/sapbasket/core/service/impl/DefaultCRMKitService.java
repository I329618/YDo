/*axle*/
package com.sap.sapbasket.core.service.impl;

import com.sap.sapbasket.core.dao.CRMKitDao;
import javax.annotation.Resource;
import com.sap.sapbasket.core.service.CRMKitService;
import com.sap.sapbasket.core.model.CRMKitModel;
import java.util.List;

public class DefaultCRMKitService implements CRMKitService
{
@Resource(name = "crmkitDao")
private CRMKitDao crmkitDao;

@Override
public List<CRMKitModel> getCRMKit()
{
 return getCRMKitDao().findCRMKit();
}

public CRMKitDao getCRMKitDao()
{
      return crmkitDao;
}

public void setCRMKitDao(final CRMKitDao crmkitDao)
{
 this.crmkitDao = crmkitDao;
}

}
