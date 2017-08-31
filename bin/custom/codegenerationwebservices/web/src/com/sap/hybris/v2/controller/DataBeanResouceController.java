/**
 *
 */
package com.sap.hybris.v2.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sap.sapbasket.core.service.impl.DefaultComposedTypeService;
import com.sap.sapbasket.facades.impl.DataBeanResouces;
import com.sap.sapbasket.facades.impl.DefaultAutomationFacade;


/**
 * @author I328935
 *
 */
@Controller
@RequestMapping("/{baseSiteId}/ydo")
public class DataBeanResouceController
{

	@Resource(name = "TestAutomationFacade")
	private DefaultAutomationFacade defaultAutomationFacade;

	@Resource(name = "ComposedTypesService")
	private DefaultComposedTypeService defaultComposedTypeService;

	/*
	 * @GET
	 *
	 * @RequestMapping(value = "/getconverter", method = RequestMethod.GET)
	 *
	 * @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON }) public @ResponseBody MapWrapper
	 * getconverter() { System.out.println("HI in map web service"); final Map<String, ArrayList<DataBeanResouces>>
	 * converterDataMap = defaultAutomationFacade.getList(); final MapWrapper mw = new MapWrapper();
	 * mw.setMap(converterDataMap);
	 *
	 * return mw; }
	 */

	@GET
	@RequestMapping(value = "/getconverterjson", method = RequestMethod.GET)
	@Produces(
	{ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public @ResponseBody StringWrapper getconverterjson()
	{
		//System.out.println("HI in json web service");
		final Map<String, ArrayList<DataBeanResouces>> converterDataMap = defaultAutomationFacade.getList();
		final StringWrapper sw = new StringWrapper();
		final Gson gson = new Gson();
		final String json = gson.toJson(converterDataMap);
		sw.setStr(json);

		return sw;
	}

	@GET
	@RequestMapping(value = "/getmodelpackagejson", method = RequestMethod.GET)
	@Produces(
	{ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public @ResponseBody StringWrapper getmodelpackagejson()
	{
		final Map<String, String> modelPackages = defaultComposedTypeService.getModelpackages();
		final StringWrapper sw = new StringWrapper();
		final Gson gson = new Gson();
		final String json = gson.toJson(modelPackages);
		sw.setStr(json);

		return sw;
	}

	@GET
	@RequestMapping(value = "/getcomposedtypesjson", method = RequestMethod.GET)
	@Produces(
	{ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public @ResponseBody StringWrapper getcomposedtypesjson()
	{
		//System.out.println("HI in json web service"); //final Map<String,
		final Map<String, String> composedMap = defaultComposedTypeService.getComposedType();
		final StringWrapper sw = new StringWrapper();
		final Gson gson = new Gson();
		final String json = gson.toJson(composedMap);
		sw.setStr(json);

		return sw;
	}

	@GET
	@RequestMapping(value = "/getfieldsforcomposedtypesjson", method = RequestMethod.GET)
	@Produces(
	{ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public @ResponseBody StringWrapper getfieldsforcomposedtypesjson()
	{
		final Map<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();

		final Map<String, ArrayList<DataBeanResouces>> converterDataMap = defaultAutomationFacade.getList();

		for (final Map.Entry<String, ArrayList<DataBeanResouces>> entry : converterDataMap.entrySet())
		{
			String x = null;

			for (final DataBeanResouces y : entry.getValue())
			{
				x = y.getDataBeanCanonicalName();
			}

			Field fieldList[] = null;
			try
			{
				Class<?> current = Class.forName(x);
				final ArrayList<String> columnNames = new ArrayList<String>();

				while (current.getSuperclass() != null)
				{
					fieldList = current.getDeclaredFields();
					current = current.getSuperclass();

					for (int i = 0; i < fieldList.length; i++)
					{
						columnNames.add(fieldList[i].getName());
					}

				}
				map.put(entry.getKey(), columnNames);

			}
			catch (final SecurityException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (final ClassNotFoundException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		final StringWrapper sw = new StringWrapper();
		final Gson gson = new Gson();
		final String json = gson.toJson(map);
		sw.setStr(json);

		return sw;
	}
}







