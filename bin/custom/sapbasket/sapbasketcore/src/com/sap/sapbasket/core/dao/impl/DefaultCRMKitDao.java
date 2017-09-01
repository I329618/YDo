/*axle*/
package com.sap.sapbasket.core.dao.impl;

import com.sap.sapbasket.core.dao.CRMKitDao;
import com.sap.sapbasket.core.model.CRMKitModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;

public class DefaultCRMKitDao extends AbstractItemDao implements CRMKitDao{
	protected static final String CUSTOMER_QUERY = "SELECT {" + CRMKitModel.PK + "} FROM {" + CRMKitModel._TYPECODE       + "}";

@Override
public List<CRMKitModel> findCRMKit(){
	final FlexibleSearchQuery query = new FlexibleSearchQuery(CUSTOMER_QUERY);
	final SearchResult<CRMKitModel> result = search(query);
	return result.getResult();
	}
}
