/**
 *
 */
package com.sap.sapbasket.facades.impl;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author I328935
 *
 */
@XmlRootElement
public class DataBeanResouces
{
	private String dataBeanCanonicalName;
	private String converterName;
	private String aliasName;

	public String getDataBeanCanonicalName()
	{
		return dataBeanCanonicalName;
	}

	public void setDataBeanCanonicalName(final String dataBeanCanonicalName)
	{
		this.dataBeanCanonicalName = dataBeanCanonicalName;
	}

	public String getConverterName()
	{
		return converterName;
	}

	public void setConverterName(final String converterName)
	{
		this.converterName = converterName;
	}

	public String getAliasName()
	{
		return aliasName;
	}

	public void setAliasName(final String aliasName)
	{
		this.aliasName = aliasName;
	}


}

