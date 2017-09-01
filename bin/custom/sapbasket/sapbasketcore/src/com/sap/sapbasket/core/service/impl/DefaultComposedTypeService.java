/**
 *
 */
package com.sap.sapbasket.core.service.impl;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.model.ModelNameUtils;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YRelation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.sap.sapbasket.core.dao.impl.DefaultComposedTypesDao;
import com.sap.sapbasket.core.service.ComposedTypesService;


/**
 * @author I329618
 *
 */
public class DefaultComposedTypeService implements ComposedTypesService
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.sapbasket.core.service.GetComposedTypes#getComposedType()
	 */
	@Resource(name = "ComposedTypesDao")
	private DefaultComposedTypesDao defaultComposedTypesDao;

	@Override
	public Map<String, String> getComposedType()
	{
		return defaultComposedTypesDao.getComposedType();
	}

	public Map<String, String> getModelpackages()
	{
		final CodeGenerator gen = new CodeGenerator("C:/mock/hybris/bin/platform");
		final Map<String, String> composedTypes = defaultComposedTypesDao.getComposedType();
		final Set<String> composedTypesSet = new HashSet<String>();
		final Map<String, String> modelPackages = new HashMap<String, String>();
		for (final String extName : composedTypes.values())
		{
			composedTypesSet.add(extName);
		}
		for (final String extensionName : composedTypesSet)
		{
			final YExtension ext = gen.getTypeSystem().getExtension(extensionName);
			if (ext != null)
			{
				final Set<YComposedType> types = ext.getOwnTypes(YComposedType.class, new Class[]
				{ YEnumType.class, YRelation.class });

				for (final YComposedType type : types)
				{
					if (needsCodeGenerationForType(type))
					{
						final String packgeName = ModelNameUtils.getModelPackage(type,
								gen.getExtensionPackage((YExtension) type.getNamespace()));
						System.err.println("[ package ] is " + packgeName + " and [Type] is " + type.getCode());
						modelPackages.put(type.getCode(), packgeName);
					}
				}
			}
			else
			{
				System.out.println("extensionName: " + extensionName);
			}
		}
		return modelPackages;
	}


	private boolean needsCodeGenerationForType(final YComposedType t)
	{
		return (t.isGenerateModel()) && (!t.getCode().equalsIgnoreCase("ExtensibleItem"))
				&& (!t.getCode().equalsIgnoreCase("LocalizableItem")) && (!t.getCode().equalsIgnoreCase("GenericItem"))
				&& ((t.getSuperType() == null) || (!t.getSuperType().getCode().equalsIgnoreCase("Link")));
	}

	/*
	 * public static void main(final String[] args) { final DefaultComposedTypeService dao = new
	 * DefaultComposedTypeService(); dao.test(); }
	 */
}
