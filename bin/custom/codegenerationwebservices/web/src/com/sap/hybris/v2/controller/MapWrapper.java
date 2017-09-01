/**
 *
 */
package com.sap.hybris.v2.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sap.sapbasket.facades.impl.DataBeanResouces;


/**
 * @author I328935
 *
 */
@XmlRootElement
public class MapWrapper
{
	private Map<String, ArrayList<DataBeanResouces>> map;

	@XmlElement
	public Map<String, ArrayList<DataBeanResouces>> getMap()
	{
		return map;
	}

	public void setMap(final Map<String, ArrayList<DataBeanResouces>> map)
	{
		this.map = map;
	}

}
