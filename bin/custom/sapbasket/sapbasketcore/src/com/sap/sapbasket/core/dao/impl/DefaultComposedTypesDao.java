/**
 *
 */
package com.sap.sapbasket.core.dao.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sap.sapbasket.core.dao.ComposedTypesDao;


/**
 * @author I329618
 *
 */
public class DefaultComposedTypesDao implements ComposedTypesDao
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.sapbasket.core.dao.ComposedTypesDao#getComposedType()
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public Map<String, String> getComposedType()
	{
		final Map<String, String> map = new LinkedHashMap<String, String>();
		final String QueryString = "Select " + ComposedTypeModel.PK + " from {composedType}";

		final FlexibleSearchQuery Query = new FlexibleSearchQuery(QueryString);


		final SearchResult<ComposedTypeModel> extensionNames = flexibleSearchService.search(Query);

		for (final ComposedTypeModel model : extensionNames.getResult())
		{
			map.put(model.getCode(), model.getExtensionName());
		}


		return map;


	}

}
