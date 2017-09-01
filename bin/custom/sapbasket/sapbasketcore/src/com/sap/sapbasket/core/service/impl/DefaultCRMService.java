/*axle*/
package com.sap.sapbasket.core.service.impl;

import com.sap.sapbasket.core.dao.CRMDao;
import javax.annotation.Resource;
import com.sap.sapbasket.core.service.CRMService;
import com.sap.sapbasket.core.model.CRMModel;
import java.util.List;

public class DefaultCRMService implements CRMService
{
@Resource(name = "crmDao")
private CRMDao crmDao;

@Override
public List<CRMModel> getCRM()
{
 return getCRMDao().findCRM();
}

public CRMDao getCRMDao()
{
      return crmDao;
}

public void setCRMDao(final CRMDao crmDao)
{
 this.crmDao = crmDao;
}

}
