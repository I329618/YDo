/*axle*/
package com.sap.sapbasket.core.dao.impl;

import com.sap.sapbasket.core.dao.CRMDao;
import com.sap.sapbasket.core.model.CRMModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;

public class DefaultCRMDao extends AbstractItemDao implements CRMDao{
	protected static final String CUSTOMER_QUERY = "SELECT {" + CRMModel.PK + "} FROM {" + CRMModel._TYPECODE       + "}";

@Override
public List<CRMModel> findCRM(){
	final FlexibleSearchQuery query = new FlexibleSearchQuery(CUSTOMER_QUERY);
	final SearchResult<CRMModel> result = search(query);
	return result.getResult();
	}
}
