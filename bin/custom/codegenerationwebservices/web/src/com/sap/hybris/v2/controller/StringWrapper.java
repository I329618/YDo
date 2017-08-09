/**
 *
 */
package com.sap.hybris.v2.controller;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author I328935
 *
 */
@XmlRootElement
public class StringWrapper
{
	private String str;

	@XmlElement
	public String getStr()
	{
		return str;
	}

	public void setStr(final String str)
	{
		this.str = str;
	}

}
