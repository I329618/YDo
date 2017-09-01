/**
 *
 */
package com.sap.sapbasket.facades.impl;

import de.hybris.platform.converters.impl.AbstractConverter;
import de.hybris.platform.core.Registry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * @author I333795
 *
 */
public class DefaultAutomationFacade
{
	public Map<String, ArrayList<DataBeanResouces>> getList()
	{
		// For convertors getting the target classes for convertors
		final Map<String, ArrayList<DataBeanResouces>> dataBeanResoucesMap = new HashMap<String, ArrayList<DataBeanResouces>>();
		try
		{
			final Map<String, AbstractConverter> beanTypeConvertor = Registry.getCoreApplicationContext()
					.getBeansOfType(AbstractConverter.class);
			System.err.println(beanTypeConvertor);

			beanTypeConvertor.forEach((n, m) -> {

				final Class targetClass = getTargetClassName(m, m.getClass());

				if (targetClass != null)
				{
					final DataBeanResouces dataBeanResouces = new DataBeanResouces();
					final String targetClassName = targetClass.getSimpleName();
					ArrayList<DataBeanResouces> dataBeanResoucesList = new ArrayList<DataBeanResouces>();

					if (dataBeanResoucesMap.get(targetClassName) == null)
					{
						dataBeanResouces.setDataBeanCanonicalName(targetClass.getCanonicalName());
						dataBeanResouces.setConverterName(n);
						final String aliasName[] = Registry.getCoreApplicationContext().getAliases(n);
						if (aliasName != null && aliasName.length > 0)
						{
							dataBeanResouces.setAliasName(aliasName[0]);
						}
						else
						{
							dataBeanResouces.setAliasName("No Alias Defined");
						}
						dataBeanResoucesList.add(dataBeanResouces);
						dataBeanResoucesMap.put(targetClassName, dataBeanResoucesList);
					}
					else
					{
						dataBeanResoucesList = dataBeanResoucesMap.get(targetClassName);
						dataBeanResouces.setDataBeanCanonicalName(targetClass.getCanonicalName());
						dataBeanResouces.setConverterName(n);

						final String aliasName[] = Registry.getCoreApplicationContext().getAliases(n);
						if (aliasName != null && aliasName.length > 0)
						{
							dataBeanResouces.setAliasName(aliasName[0]);
						}
						else
						{
							dataBeanResouces.setAliasName("No Alias Defined");
						}
						dataBeanResoucesList.add(dataBeanResouces);

						dataBeanResoucesMap.put(targetClassName, dataBeanResoucesList);
					}
					System.err.println("Class name is printing " + targetClassName);
				}
				else
				{
					System.err.println("Bean Resouces Not Found for Bean name" + n);
				}
			});

			//((dataBeanResoucesMap.get("CustomerData")).get(0)).getConverterName();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		return dataBeanResoucesMap;
	}

	private Class getTargetClassName(final Object obj, final Class className)
	{
		final Method methodList[] = className.getDeclaredMethods();
		for (int i = 0; i < methodList.length; i++)
		{
			try
			{
				if (methodList[i].getName().equalsIgnoreCase("createFromClass"))
				{
					methodList[i].setAccessible(true);

					final Object returnVal = methodList[i].invoke(obj);
					if (returnVal != null)
					{
						return returnVal.getClass();
					}
				}
				else if (methodList[i].getName().equalsIgnoreCase("createTarget"))
				{
					methodList[i].setAccessible(true);

					final Object returnVal = methodList[i].invoke(obj);
					if (returnVal != null)
					{
						return returnVal.getClass();
					}
				}
			}
			catch (final Exception e)
			{
				// YTODO: handle exception
			}
		}

		final Field fieldList[] = className.getDeclaredFields();

		for (int i = 0; i < fieldList.length; i++)
		{
			try
			{
				if (fieldList[i].getName().equalsIgnoreCase("targetClass"))
				{
					fieldList[i].setAccessible(true);

					final Object returnValue = fieldList[i].get(obj);
					return returnValue.getClass();
				}
			}
			catch (final Exception e)
			{
				//do nothing
			}
		}

		for (int i = 0; i < fieldList.length; i++)
		{
			try
			{
				final String fieldCanonicalName = fieldList[i].getType().getCanonicalName();
				if (fieldCanonicalName.equalsIgnoreCase("de.hybris.platform.servicelayer.dto.converter.Converter")
						|| fieldCanonicalName.equalsIgnoreCase("de.hybris.platform.converters.impl.AbstractConverter")
						|| fieldCanonicalName
								.equalsIgnoreCase("de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter")
						|| fieldCanonicalName.equalsIgnoreCase("de.hybris.platform.converters.impl.AbstractPopulatingConverter"))
				{
					fieldList[i].setAccessible(true);

					final Object returnValue = fieldList[i].get(obj);
					return getTargetClassName(returnValue, returnValue.getClass());

				}
			}
			catch (final Exception e)
			{
				//do nothing
			}
		}

		if (className.getCanonicalName().equalsIgnoreCase("de.hybris.platform.converters.impl.AbstractConverter"))
		{
			return null;
		}
		else
		{
			return getTargetClassName(obj, obj.getClass().getSuperclass());
		}
	}

}
