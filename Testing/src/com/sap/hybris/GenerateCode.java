package com.sap.hybris;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sap.jsonmodels.InputData;
import com.sap.jsonmodels.Properties;
import com.sap.utils.StringUtils;

public class GenerateCode {
	static String code = "";// itemNameCode
	static String extendingFrom = "";// ExtendedItem
	static boolean generate = false;
	static boolean autocreate = false;
	static String qualifier = "";
	static String twinFeild = "";
	static String persistenceType = "property";// or dynamic
	static String extensionName = "";
	static String deploymentTableTypeCode = "";
	static Process runtime;
	static String dataType = "";
	static boolean redeclare = false;
	static String modifiers[];
	static boolean addAttribute = false;
	static boolean abstractItem = false;
	static String jalo = "";
	static List<Properties> prop = new ArrayList<Properties>();

	public static void initialize(InputData requestParameters) {
		code = requestParameters.getItemsName();
		extendingFrom = requestParameters.getExtendedItem();
		generate = requestParameters.isAutoGenerate();
		autocreate = requestParameters.isAutoCreate();
		deploymentTableTypeCode = requestParameters.getDeploymentTable();
		extensionName = requestParameters.getProjectName();
		addAttribute = requestParameters.isAddAttribute();
		abstractItem = requestParameters.isAbstractItem();
		jalo = requestParameters.getJalo();

		prop = requestParameters.getProperties();
		qualifier = prop.get(0).getQualifier();
		persistenceType = prop.get(0).getPersistence();
		dataType = prop.get(0).getDataType();
		redeclare = prop.get(0).getRedeclare();
		modifiers = prop.get(0).getModifiers();
		twinFeild = prop.get(0).getTwinFeild();
	}

	public static boolean generateSpringXML() {
		try {
			// reading the xml file
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketcore/resources/" + extensionName
					+ "core-items.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			// adding new tags
			final Node items = doc.getElementsByTagName("items").item(0);
			System.out.println("" + items.getAttributes().getNamedItem("xsi:noNamespaceSchemaLocation").toString());
			final Node itemTypes = doc.getElementsByTagName("itemtypes").item(0);

			final Element itemType = doc.createElement("itemtype");
			final NodeList itemTypeChildren = itemTypes.getChildNodes();

			if (itemTypeChildren.getLength() >= 0) {
				if (code.equals("") != true)
					itemType.setAttribute("code", code);
				if (addAttribute) {
					itemType.setAttribute("generate", "false");
					itemType.setAttribute("autocreate", "false");
				} else {
					itemType.setAttribute("generate", "true");
					itemType.setAttribute("autocreate", "true");
				}
				/*
				 * itemType.setAttribute("generate", "" + generate);
				 * itemType.setAttribute("autocreate", "" + autocreate);
				 */
				// if (jalo.equals("") != true)
				// itemType.setAttribute("jaloclass", "com.sap.sapbasket.core."
				// + code);
				if (extendingFrom.equals("") != true) {
					// itemType.setAttribute("extends", extendingFrom);
					// itemType.setAttribute("jaloclass",
					// "com.sap.sapbasket.core." + code);
					final Element deploymentTable = doc.createElement("deployment");
					deploymentTable.setAttribute("typecode", "18119");
					deploymentTable.setAttribute("table", code + "Table");
					itemType.appendChild(deploymentTable);
				}
				if (deploymentTableTypeCode.equals("") != true) {
					final Element deploymentTable = doc.createElement("deployment");
					deploymentTable.setAttribute("typecode", deploymentTableTypeCode);
					deploymentTable.setAttribute("table", code + "Table");
					itemType.appendChild(deploymentTable);
				}
				final Element attributes = doc.createElement("attributes");
				// multiple
				for (int i = 0; i < prop.size(); i++) {
					final Element attribute = doc.createElement("attribute");

					attribute.setAttribute("qualifier",
							StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()));
					if (prop.get(i).getDataType().equals("String") || prop.get(i).getDataType().equals("Integer")
							|| prop.get(i).getDataType().equals("Boolean")) {
						attribute.setAttribute("type", "java.lang." + prop.get(i).getDataType());
					} else if (prop.get(i).getDataType().equals("Date")) {
						attribute.setAttribute("type", "java.util." + prop.get(i).getDataType());
					} else {
						attribute.setAttribute("type", prop.get(i).getDataType());
					}
					if (prop.get(i).getRedeclare()) {
						attribute.setAttribute("redeclare", "true");
					}
					final Element persistence = doc.createElement("persistence");
					persistence.setAttribute("type", prop.get(i).getPersistence());

					final Element modifiersForAttribute = doc.createElement("modifiers");
					modifiers = prop.get(i).getModifiers();
					for (int j = 0; j < modifiers.length; j++) {
						modifiersForAttribute.setAttribute("" + modifiers[j].trim(), "true");
					}

					attribute.appendChild(persistence);
					attribute.appendChild(modifiersForAttribute);
					attributes.appendChild(attribute);
				}

				// multiple

				/*
				 * final Element attribute = doc.createElement("attribute");
				 * attribute.setAttribute("qualifier", qualifier.toLowerCase());
				 * attribute.setAttribute("type", "java.lang." + dataType); if
				 * (redeclare) { attribute.setAttribute("redeclare", "true"); }
				 * 
				 * final Element persistence = doc.createElement("persistence");
				 * persistence.setAttribute("type", persistenceType);
				 * 
				 * final Element modifiers1 = doc.createElement("modifiers");
				 * for (int i = 0; i < modifiers.length; i++) {
				 * modifiers1.setAttribute("" + modifiers[i].trim(), "true"); }
				 * 
				 * attribute.appendChild(persistence);
				 * attribute.appendChild(modifiers1);
				 * attributes.appendChild(attribute);
				 */

				itemType.appendChild(attributes);
				itemTypes.insertBefore(itemType, itemTypes.getFirstChild());

				// saving xml file back
				final TransformerFactory transformerFactory = TransformerFactory.newInstance();
				final Transformer transformer = transformerFactory.newTransformer();
				final DOMSource source = new DOMSource(doc);
				final StreamResult result = new StreamResult(new File(filepath));
				transformer.transform(source, result);
			}

		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static void generateDataBeansXML() {
		try {
			// reading the xml file
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/resources/" + extensionName
					+ "facades-beans.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			// adding new tags
			final Node beans = doc.getElementsByTagName("beans").item(0);

			final Element bean = doc.createElement("bean");

			if (addAttribute) {
				// bean.setAttribute("class",
				// "de.hybris.platform.commercefacades.order.data." + code +
				// "HistoryData");
				bean.setAttribute("class", "de.hybris.platform.commercefacades.user.data." + code + "Data");
			} else {
				bean.setAttribute("class", "de.hybris.platform.commercefacades.automation.data." + code + "Data");
			}

			// multiple
			for (int i = 0; i < prop.size(); i++) {
				final Element property = doc.createElement("property");
				property.setAttribute("name", StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()));
				if (prop.get(i).getDataType().equalsIgnoreCase("integer")) {
					property.setAttribute("type", "Integer");
				} else if (prop.get(i).getDataType().equalsIgnoreCase("string")) {
					property.setAttribute("type", "String");
				} else if (prop.get(i).getDataType().equalsIgnoreCase("boolean")) {
					property.setAttribute("type", "Boolean");
				} else if (prop.get(i).getDataType().equalsIgnoreCase("date")) {
					property.setAttribute("type", "java.util.Date");
				} else {
					String canonicalName = prop.get(i).getDataBeanCanonicalName();
					property.setAttribute("type", canonicalName);
					editExtensionInfo(prop.get(i).getExtensionName());

				}

				bean.appendChild(property);
			}

			beans.appendChild(bean);

			// saving xml file backP/AutomationUI
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static void editExtensionInfo(String extensionName) {
		// TODO Auto-generated method stub
		try {
			// reading the xml file
			final String filepath = "C:\\mock\\hybris\\bin\\custom\\sapbasket\\sapbasketcore\\extensioninfo.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			final Node bean = doc.getElementsByTagName("extension").item(0);

			final Node firstRequire = doc.getElementsByTagName("requires-extension").item(0);

			final Element require = doc.createElement("requires-extension");
			require.setAttribute("name", extensionName);
			bean.insertBefore(require, firstRequire);

			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateDaoInterface() {
		try {
			File f1 = new File("C://mock/hybris/bin/custom/sapbasket/sapbasketcore/src/com/sap/sapbasket/core/dao/"
					+ code + "Dao.java");
			PrintWriter pw = new PrintWriter(f1);
			pw.println("/*axle*/\npackage com.sap." + extensionName + ".core.dao;\n\nimport com.sap." + extensionName
					+ ".core.model." + code + "Model;\nimport java.util.List;\n\npublic interface " + code
					+ "Dao{\n\tpublic List<" + code + "Model> find" + code + "();\n}");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateDaoClass() {
		try {
			File f1 = new File("C://mock/hybris/bin/custom/sapbasket/sapbasketcore/src/com/sap/sapbasket/core/dao/impl/"
					+ "Default" + code + "Dao.java");
			PrintWriter pw = new PrintWriter(f1);
			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".core.dao.impl;\n\nimport com.sap.sapbasket.core.dao." + code + "Dao;\nimport com.sap."
					+ extensionName + ".core.model." + code
					+ "Model;\nimport de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;\nimport de.hybris.platform.servicelayer.search.FlexibleSearchQuery;\nimport de.hybris.platform.servicelayer.search.SearchResult;\nimport java.util.List;\n\npublic class Default"
					+ code + "Dao extends AbstractItemDao implements " + code
					+ "Dao{\n\tprotected static final String CUSTOMER_QUERY = \"SELECT {\" + " + code
					+ "Model.PK + \"} FROM {\" + " + code + "Model._TYPECODE       + \"}\";\n\n@Override\npublic List<"
					+ code + "Model> find" + code
					+ "(){\n\tfinal FlexibleSearchQuery query = new FlexibleSearchQuery(CUSTOMER_QUERY);\n\tfinal SearchResult<"
					+ code + "Model> result = search(query);\n\treturn result.getResult();\n\t}\n}");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateDaoBean() {
		try {
			// reading the xml file
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketcore/resources/" + extensionName
					+ "core-spring.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			// adding new tags
			final Node beans = doc.getElementsByTagName("beans").item(0);

			final Element alias = doc.createElement("alias");
			alias.setAttribute("name", "default" + code + "Dao");
			alias.setAttribute("alias", code.toLowerCase() + "Dao");

			final Element bean = doc.createElement("bean");
			bean.setAttribute("id", "default" + code + "Dao");
			bean.setAttribute("class", "com.sap.sapbasket.core.dao.impl.Default" + code + "Dao");
			bean.setAttribute("parent", "abstractItemDao");
			beans.appendChild(alias);
			beans.appendChild(bean);

			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateServiceInterface() {
		try {
			File f1 = new File("C://mock/hybris/bin/custom/sapbasket/sapbasketcore/src/com/sap/sapbasket/core/service/"
					+ code + "Service.java");
			PrintWriter pw = new PrintWriter(f1);
			pw.println("/*axle*/\npackage com.sap." + extensionName + ".core.service;\n\nimport com.sap."
					+ extensionName + ".core.model." + code + "Model;\nimport java.util.List;\n\npublic interface "
					+ code + "Service\n{\npublic List<" + code + "Model> get" + code + "();\n}");
			pw.flush();

		} catch (final Exception e) {

			e.printStackTrace();
		}
	}

	public static void generateServiceClass() {
		try {
			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketcore/src/com/sap/sapbasket/core/service/impl/"
							+ "Default" + code + "Service.java");
			PrintWriter pw = new PrintWriter(f1);
			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".core.service.impl;\n\nimport com.sap.sapbasket.core.dao." + code
					+ "Dao;\nimport javax.annotation.Resource;\nimport com.sap.sapbasket.core.service." + code
					+ "Service;\nimport com.sap." + extensionName + ".core.model." + code
					+ "Model;\nimport java.util.List;\n\npublic class Default" + code + "Service implements " + code
					+ "Service\n{\n@Resource(name = \"" + code.toLowerCase() + "Dao\")\nprivate " + code + "Dao "
					+ code.toLowerCase() + "Dao;\n\n@Override\npublic List<" + code + "Model> get" + code
					+ "()\n{\n return get" + code + "Dao().find" + code + "();\n}\n\npublic " + code + "Dao get" + code
					+ "Dao()\n{\n      return " + code.toLowerCase() + "Dao;\n}\n\npublic void set" + code
					+ "Dao(final " + code + "Dao " + code.toLowerCase() + "Dao)\n{\n this." + code.toLowerCase()
					+ "Dao = " + code.toLowerCase() + "Dao;\n}\n\n}");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateServiceBean() {
		try {
			// reading the xml file
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketcore/resources/" + extensionName
					+ "core-spring.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			// adding new tags
			final Node beans = doc.getElementsByTagName("beans").item(0);

			final Element alias = doc.createElement("alias");
			alias.setAttribute("name", "default" + code + "Service");
			alias.setAttribute("alias", code.toLowerCase() + "Service");

			final Element bean = doc.createElement("bean");
			bean.setAttribute("id", "default" + code + "Service");
			bean.setAttribute("class", "com.sap.sapbasket.core.service.impl.Default" + code + "Service");
			beans.appendChild(alias);
			beans.appendChild(bean);

			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateFacadeInterface() {
		try {
			File f1 = new File("C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/src/com/sap/sapbasket/facades/"
					+ code + "Facade.java");
			PrintWriter pw = new PrintWriter(f1);
			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".facades;\n\nimport de.hybris.platform.commercefacades.automation.data." + code
					+ "Data;\nimport java.util.List;\nimport de.hybris.platform.commerceservices.customer.DuplicateUidException;\nimport de.hybris.platform.servicelayer.dto.converter.ConversionException;\n\n\npublic interface "
					+ code + "Facade\n{\n\tpublic List<" + code + "Data> get" + code + "();\n} ");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateFacadeClass() {
		try {
			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/src/com/sap/sapbasket/facades/impl/Default"
							+ code + "Facade.java");
			PrintWriter pw = new PrintWriter(f1);
			pw.println(
					"/*axle*/\npackage com.sap.sapbasket.facades.impl;\nimport java.util.ArrayList;\nimport java.util.List;\nimport javax.annotation.Resource;\nimport com.sap."
							+ extensionName + ".core.model." + code
							+ "Model;\nimport de.hybris.platform.commercefacades.automation.data." + code
							+ "Data;\nimport com.sap." + extensionName + ".core.service.impl.Default" + code
							+ "Service;\nimport com.sap." + extensionName + ".facades." + code
							+ "Facade;\nimport com.sap." + extensionName + ".facades.populators.Custom" + code
							+ "Populator;\npublic class Default" + code + "Facade implements " + code + "Facade\n"
							+ "{\n" + "@Resource(name = \"" + code.toLowerCase() + "Service\")\n" + "Default" + code
							+ "Service default" + code.toLowerCase() + "Service;\n" + "@Resource(name = \""
							+ code.toLowerCase() + "Populator\")\nCustom" + code + "Populator " + code.toLowerCase()
							+ "Populator;\n" + "@Override\n" + "public List<" + code + "Data> get" + code + "()\n"
							+ "{\n" + "final List<" + code + "Model> " + code.toLowerCase() + "Models = default"
							+ code.toLowerCase() + "Service.get" + code + "();\n" +

							"final List<" + code + "Data> " + code.toLowerCase() + "List = new ArrayList<" + code
							+ "Data>();\n" + code + "Data " + code.toLowerCase() + "Data = null;\n" + "for (final "
							+ code + "Model " + code.toLowerCase() + "Model : " + code.toLowerCase() + "Models)\n"
							+ "{\n" + code.toLowerCase() + "Data = new " + code + "Data();\n" + code.toLowerCase()
							+ "Populator.populate(" + code.toLowerCase() + "Model, " + code.toLowerCase() + "Data);\n"
							+ code.toLowerCase() + "List.add(" + code.toLowerCase() + "Data);\n}\nreturn "
							+ code.toLowerCase() + "List;\n}\n}");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatefacadeBean() {
		try {
			// reading the xml file
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/resources/" + extensionName
					+ "facades-spring.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			// adding new tags
			final Node beans = doc.getElementsByTagName("beans").item(0);

			final Element alias = doc.createElement("alias");
			alias.setAttribute("name", "default" + code + "Facade");
			alias.setAttribute("alias", code.toLowerCase() + "Facade");

			final Element bean = doc.createElement("bean");
			bean.setAttribute("id", "default" + code + "Facade");
			bean.setAttribute("class", "com.sap.sapbasket.facades.impl.Default" + code + "Facade");
			beans.appendChild(alias);
			beans.appendChild(bean);

			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatePopulatorForAddingNewAttributeToExistingType() {
		try {
			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/src/com/sap/sapbasket/facades/populators/Custom"
							+ code + "Populator.java");
			PrintWriter pw = new PrintWriter(f1);
			String dataFromXml = "";
			String superPopulate = "";
			String extendsClass = "";
			String newItemPopulaterInterface = "";
			String newItemOtherImports = "";
			String implementsInterface = "";
			String dataBean = "";
			/*
			 * dataFromXml = "import de.hybris.platform.core.model.order." +
			 * code +
			 * "Model;\nimport de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;\nimport de.hybris.platform.commercefacades.order.data.OrderHistoryData;\n"
			 * ;
			 */
			dataFromXml = "import de.hybris.platform.core.model.user." + code
					+ "Model;\nimport de.hybris.platform.commercefacades.user.converters.populator." + code
					+ "Populator;\nimport de.hybris.platform.commercefacades.user.data." + code + "Data;\n";
			superPopulate = "super.populate(source, target);\n";
			// extendsClass = "extends OrderHistoryPopulator\n";
			extendsClass = "extends " + code + "Populator\n";
			// dataBean = code + "History";
			dataBean = code;

			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".facades.populators;\nimport de.hybris.platform.servicelayer.dto.converter.ConversionException;\n"
					+ newItemPopulaterInterface + newItemOtherImports + dataFromXml +

					"public class Custom" + code + "Populator " + implementsInterface + extendsClass + "{\n"
					+ "@Override\n" + "public void populate(final " + code + "Model source, final " + dataBean
					+ "Data target) throws ConversionException\n" + "{\n" + superPopulate);
			for (int i = 0; i < prop.size(); i++) {
				pw.println("\ntarget.set" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
						+ "(source.get" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
						+ "());\n");
			}
			pw.println("}\n" + "}");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean isAComplexType(String dataType) {
		if (dataType.equalsIgnoreCase("integer") || dataType.equalsIgnoreCase("string")
				|| dataType.equalsIgnoreCase("date") || dataType.equalsIgnoreCase("boolean"))
			return false;
		else
			return true;
	}

	public static void generatePopulatorForNewItemType() {
		try {
			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/src/com/sap/sapbasket/facades/populators/Custom"
							+ code + "Populator.java");
			PrintWriter pw = new PrintWriter(f1);
			String dataFromXml = "";
			String superPopulate = "";
			String extendsClass = "";
			String newItemPopulaterInterface = "";
			String newItemOtherImports = "";
			String implementsInterface = "";
			String dataBean = "";
			String str = "";
			String importData = "";
			/*
			 * String importModel1 =
			 * "import de.hybris.platform.b2b.model.B2BCustomerModel;\n"; String
			 * importModel2 =
			 * "import de.hybris.platform.b2b.model.B2BUnitModel;\n";
			 */
			String importModel = "";

			newItemPopulaterInterface = "import de.hybris.platform.converters.Populator;\n";
			newItemOtherImports = "import de.hybris.platform.commercefacades.automation.data." + code + "Data;\n"
					+ "import com.sap." + extensionName + ".core.model." + code + "Model;\n";
			implementsInterface = "implements Populator<" + code + "Model, " + code + "Data>\n";
			dataBean = code;

			for (int i = 0; i < prop.size(); i++) {
				if (isAComplexType(prop.get(i).getDataType())) {
					str += "import de.hybris.platform.servicelayer.dto.converter.Converter;\n";
					break;
				}
			}

			for (int i = 0; i < prop.size(); i++) {
				if (isAComplexType(prop.get(i).getDataType())) {
					importData += "import " + prop.get(i).getDataBeanCanonicalName() + ";" + "\n";
					importModel += "import " + prop.get(i).getModelPackage() + "."
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getDataType()) + "Model;" + "\n";
				}
			}
			pw.println("/*ydo*/\npackage com.sap." + extensionName
					+ ".facades.populators;\nimport de.hybris.platform.servicelayer.dto.converter.ConversionException;\n"
					+ newItemPopulaterInterface + newItemOtherImports + dataFromXml + str + importModel + importData
					+ "public class Custom" + code + "Populator " + implementsInterface + extendsClass + "{\n");
			for (int i = 0; i < prop.size(); i++) {
				if (isAComplexType(prop.get(i).getDataType())) {
					String s[] = prop.get(i).getDataBeanCanonicalName().split("\\.");
					pw.println("private Converter<"
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getDataType()) + "Model, "
							+ StringUtils.convertFirstLetterToUpperCase(s[(s.length - 1)]) + "> "
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + "Converter;\n");
					pw.println("public Converter<"
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getDataType()) + "Model, "
							+ StringUtils.convertFirstLetterToUpperCase(s[(s.length - 1)]) + "> get"
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
							+ "Converter() {\nreturn "
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + "Converter;\n}");
					pw.println("public void set" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
							+ "Converter(final Converter<"
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getDataType()) + "Model, "
							+ StringUtils.convertFirstLetterToUpperCase(s[(s.length - 1)]) + ">"
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier())
							+ "Converter) {\nthis."
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + "Converter = "
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + "Converter;\n}");
				}
			}
			pw.println("@Override\n" + "public void populate(final " + code + "Model source, final " + dataBean
					+ "Data target) throws ConversionException\n" + "{\n" + superPopulate);

			for (int i = 0; i < prop.size(); i++) {
				if (isAComplexType(prop.get(i).getDataType())) {
					pw.println("target.set" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
							+ "(get" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
							+ "Converter().convert(source.get"
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier()) + "()));");
				} else {
					pw.println("\ntarget.set" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
							+ "(source.get" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
							+ "());\n");
				}
			}
			pw.println("}\n" + "}");
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatepopulatorBeanForAddingNewAttributeToExistingType() {
		try {
			// reading the xml file
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/resources/" + extensionName
					+ "facades-spring.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			final Node beans = doc.getElementsByTagName("beans").item(0);
			// adding new tags
			if (addAttribute) {
				final Element aliaspopulator = doc.createElement("alias");
				aliaspopulator.setAttribute("name", "custom" + code + "Populator");
				// aliaspopulator.setAttribute("alias",
				// "orderHistoryPopulator");
				aliaspopulator.setAttribute("alias", code.toLowerCase() + "Populator");

				final Element beanpopulator = doc.createElement("bean");
				beanpopulator.setAttribute("id", "custom" + code + "Populator");
				beanpopulator.setAttribute("class", "com.sap.sapbasket.facades.populators.Custom" + code + "Populator");
				beanpopulator.setAttribute("parent", "default" + code + "Populator");
				/*
				 * final Element property1 = doc.createElement("property");
				 * property1.setAttribute("name", "enumerationService");
				 * property1.setAttribute("ref", "enumerationService");
				 * 
				 * final Element property2 = doc.createElement("property");
				 * property2.setAttribute("name", "priceDataFactory");
				 * property2.setAttribute("ref", "priceDataFactory");
				 * 
				 * beanpopulator.appendChild(property1);
				 * beanpopulator.appendChild(property2);
				 */

				beans.appendChild(aliaspopulator);
				beans.appendChild(beanpopulator);
			} else {

			}
			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatepopulatorBeanForNewItemType() {
		try {
			// reading the xml file
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/resources/" + extensionName
					+ "facades-spring.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			final Node beans = doc.getElementsByTagName("beans").item(0);
			// adding new tagsfinal Element aliasconverter =
			// doc.createElement("alias");
			final Element aliasconverter = doc.createElement("alias");
			aliasconverter.setAttribute("name", "default" + code + "Converter");
			aliasconverter.setAttribute("alias", code.toLowerCase() + "Converter");

			final Element beanconverter = doc.createElement("bean");
			beanconverter.setAttribute("id", "default" + code + "Converter");
			beanconverter.setAttribute("parent", "abstractPopulatingConverter");

			final Element property1 = doc.createElement("property");
			property1.setAttribute("name", "targetClass");
			property1.setAttribute("value", "de.hybris.platform.commercefacades.automation.data." + code + "Data");

			final Element property2 = doc.createElement("property");
			property2.setAttribute("name", "populators");

			final Element list = doc.createElement("list");
			list.setAttribute("merge", "true");

			final Element ref = doc.createElement("ref");
			ref.setAttribute("bean", code.toLowerCase() + "Populator");

			list.appendChild(ref);
			property2.appendChild(list);
			beanconverter.appendChild(property1);
			beanconverter.appendChild(property2);

			final Element aliaspopulator = doc.createElement("alias");
			aliaspopulator.setAttribute("name", "default" + code + "Populator");
			aliaspopulator.setAttribute("alias", code.toLowerCase() + "Populator");

			final Element beanpopulator = doc.createElement("bean");
			beanpopulator.setAttribute("id", "default" + code + "Populator");
			beanpopulator.setAttribute("class", "com.sap.sapbasket.facades.populators.Custom" + code + "Populator");
			for (int i = 0; i < prop.size(); i++) {
				if (isAComplexType(prop.get(i).getDataType())) {
					Element property = doc.createElement("property");
					property.setAttribute("name",
							StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + "Converter");
					property.setAttribute("ref", prop.get(i).getAliasName());
					beanpopulator.appendChild(property);
				}
			}

			beans.appendChild(aliasconverter);
			beans.appendChild(beanconverter);
			beans.appendChild(aliaspopulator);
			beans.appendChild(beanpopulator);
			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateControllerForAddingNewAttributeToExistingType() {
		try {
			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/src/com/sap/sapbasket/storefront/controllers/pages/Dummy"
							+ code + "Controller.java");
			PrintWriter pw = new PrintWriter(f1);
			/*
			 * pw.println("package com.sap." + extensionName +
			 * ".storefront.controllers.pages;\n\nimport de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;\nimport de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;\nimport de.hybris.platform.commerceservices.search.pagedata.PageableData;\nimport de.hybris.platform.commerceservices.search.pagedata.SearchPageData;\nimport de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;\nimport javax.annotation.Resource;\n\nimport java.util.List;\nimport org.codehaus.jackson.map.ObjectMapper;\nimport org.springframework.stereotype.Controller;\nimport org.springframework.web.bind.annotation.RequestMapping;\nimport org.springframework.web.bind.annotation.RequestMethod;\nimport org.springframework.web.bind.annotation.ResponseBody;\nimport de.hybris.platform.commercefacades.order.OrderFacade;\nimport de.hybris.platform.commercefacades.order.data."
			 * + code +
			 * "HistoryData;\n\n@Controller\n@RequestMapping(\"/metadata" +
			 * code.toLowerCase() + "\")\npublic class Dummy" + code +
			 * "Controller extends AbstractSearchPageController\n{\n@Resource(name = \""
			 * + code.toLowerCase() + "Facade\")\nprivate " + code + "Facade " +
			 * code.toLowerCase() + "Facade;\n\n@RequestMapping(value = \"/" +
			 * code.toLowerCase() +
			 * "\", method = RequestMethod.GET)\n@RequireHardLogIn\n@ResponseBody\npublic String get"
			 * + code +
			 * "() throws CMSItemNotFoundException\n{\nfinal int page = 0;\nfinal ShowMode showMode = ShowMode.Page;\nfinal String sortCode = null;\nfinal PageableData pageableData = createPageableData(page, 5, sortCode, showMode);\nfinal SearchPageData<OrderHistoryData> "
			 * + code.toLowerCase() + "Data = "+code.toLowerCase()+
			 * "Facade.getPagedOrderHistoryForStatuses(pageableData);\ntry\n{\nfinal ObjectMapper m = new ObjectMapper();\nreturn m.writeValueAsString("
			 * + code.toLowerCase() +
			 * "Data);\n}\ncatch (final Exception e)\n{\ne.printStackTrace();\nreturn \"Unable to ftech the Data\";\n}\n}\n}"
			 * );
			 */

			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".storefront.controllers.pages;\n\nimport de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;\nimport de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;\nimport de.hybris.platform.commerceservices.search.pagedata.PageableData;\nimport de.hybris.platform.commerceservices.search.pagedata.SearchPageData;\nimport de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;\nimport javax.annotation.Resource;\n\nimport java.util.List;\nimport org.codehaus.jackson.map.ObjectMapper;\nimport org.springframework.stereotype.Controller;\nimport org.springframework.web.bind.annotation.RequestMapping;\nimport org.springframework.web.bind.annotation.RequestMethod;\nimport org.springframework.web.bind.annotation.ResponseBody;\nimport de.hybris.platform.commercefacades.order.OrderFacade;\nimport de.hybris.platform.commercefacades.order.data."
					+ code + "HistoryData;\n\n@Controller\n@RequestMapping(\"/metadata" + code.toLowerCase()
					+ "\")\npublic class Dummy" + code
					+ "Controller extends AbstractSearchPageController\n{\n@Resource(name = \"" + code.toLowerCase()
					+ "Facade\")\nprivate " + code + "Facade " + code.toLowerCase()
					+ "Facade;\n\n@RequestMapping(value = \"/" + code.toLowerCase()
					+ "\", method = RequestMethod.GET)\n@RequireHardLogIn\n@ResponseBody\npublic String get" + code
					+ "() throws CMSItemNotFoundException\n{\nfinal int page = 0;\nfinal ShowMode showMode = ShowMode.Page;\nfinal String sortCode = null;\nfinal PageableData pageableData = createPageableData(page, 5, sortCode, showMode);\nfinal SearchPageData<OrderHistoryData> "
					+ code.toLowerCase() + "Data = " + code.toLowerCase()
					+ "Facade.getPagedOrderHistoryForStatuses(pageableData);\ntry\n{\nfinal ObjectMapper m = new ObjectMapper();\nreturn m.writeValueAsString("
					+ code.toLowerCase()
					+ "Data);\n}\ncatch (final Exception e)\n{\ne.printStackTrace();\nreturn \"Unable to ftech the Data\";\n}\n}\n}");

			pw.flush();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateControllerForNewItemType() {
		try {
			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/src/com/sap/sapbasket/storefront/controllers/pages/Dummy"
							+ code + "Controller.java");
			PrintWriter pw = new PrintWriter(f1);
			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".storefront.controllers.pages;\n\nimport de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;\nimport de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;\nimport de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;\nimport javax.annotation.Resource;\n\nimport java.util.List;\nimport org.codehaus.jackson.map.ObjectMapper;\nimport org.springframework.stereotype.Controller;\nimport org.springframework.web.bind.annotation.RequestMapping;\nimport org.springframework.web.bind.annotation.RequestMethod;\nimport org.springframework.web.bind.annotation.ResponseBody;\nimport com.sap."
					+ extensionName + ".facades.impl.Default" + code
					+ "Facade;\nimport de.hybris.platform.commercefacades.automation.data." + code
					+ "Data;\n\n@Controller\n@RequestMapping(\"/metadata" + code.toLowerCase()
					+ "\")\npublic class Dummy" + code
					+ "Controller extends AbstractSearchPageController\n{\n@Resource(name = \"" + code.toLowerCase()
					+ "Facade\")\nprivate Default" + code + "Facade " + code.toLowerCase()
					+ "Facade;\n\n@RequestMapping(value = \"/" + code.toLowerCase()
					+ "\", method = RequestMethod.GET)\n@RequireHardLogIn\n@ResponseBody\npublic String get" + code
					+ "() throws CMSItemNotFoundException\n{\nfinal List<" + code + "Data> " + code.toLowerCase()
					+ "Data = " + code.toLowerCase() + "Facade.get" + code
					+ "();\ntry\n{\nfinal ObjectMapper m = new ObjectMapper();\nfinal StringBuilder htmlBuilder = new StringBuilder();\nhtmlBuilder.append(\"<html>\");\nhtmlBuilder.append(\"<head><title>"
					+ code
					+ "Model List</title></head>\");\nhtmlBuilder.append(\"<body>\");\nhtmlBuilder.append(\"<h3>List of "
					+ code + "Model</h3>\");\nfor (int i = 0; i < " + code.toLowerCase()
					+ "Data.size(); i++)\n{\nString temp = m.writeValueAsString(" + code.toLowerCase()
					+ "Data.get(i));\ntemp = temp.replace(\"{\", \"\");\ntemp = temp.replace(\"}\", \"\");\ntemp = temp.replace(\"\\\"\", \"\");\nhtmlBuilder.append(\"<p>\" + temp + \"</p>\");\n}\nhtmlBuilder.append(\"</body>\");\nhtmlBuilder.append(\"</html>\");\nreturn htmlBuilder.toString();\n}\ncatch (final Exception e)\n{\ne.printStackTrace();\nreturn \"Unable to fetech the Data\";\n}\n}\n}");

			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatereversePopulator() {
		try {

			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/src/com/sap/sapbasket/facades/populators/Custom"
							+ code + "ReversePopulator.java");
			PrintWriter pw = new PrintWriter(f1);
			String dataFromXml = "";
			String superPopulate = "";
			String extendsClass = "";
			String newItemPopulaterInterface = "";
			String newItemOtherImports = "";
			String implementsInterface = "";
			String dataBean = "";
			if (addAttribute) {
				/*
				 * dataFromXml = "import de.hybris.platform.core.model.order." +
				 * code +
				 * "Model;\nimport de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;\nimport de.hybris.platform.commercefacades.order.data.OrderHistoryData;\n"
				 * ;
				 */
				dataFromXml = "import de.hybris.platform.core.model.user." + code
						+ "Model;\nimport de.hybris.platform.commercefacades.user.converters.populator." + code
						+ "ReversePopulator;\nimport de.hybris.platform.commercefacades.user.data." + code + "Data;\n";
				superPopulate = "super.populate(source, target);\n";
				// extendsClass = "extends OrderHistoryPopulator\n";
				extendsClass = "extends " + code + "ReversePopulator\n";
				// dataBean = code + "History";
				dataBean = code;

			} else {
				newItemPopulaterInterface = "import de.hybris.platform.converters.Populator;\n";
				newItemOtherImports = "import de.hybris.platform.commercefacades.automation.data." + code + "Data;\n"
						+ "import com.sap." + extensionName + ".core.model." + code + "Model;\n";
				implementsInterface = "implements Populator<" + code + "Model, " + code + "Data>\n";
				dataBean = code;
			}
			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".facades.populators;\nimport de.hybris.platform.servicelayer.dto.converter.ConversionException;\n"
					+ newItemPopulaterInterface + newItemOtherImports + dataFromXml +

					"public class Custom" + code + "ReversePopulator " + implementsInterface + extendsClass + "{\n"
					+ "@Override\n" + "public void populate(final " + code + "Data source, final " + dataBean
					+ "Model target) throws ConversionException\n" + "{\n" + superPopulate);
			for (int i = 0; i < prop.size(); i++) {
				pw.println("\ntarget.set" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
						+ "(source.get" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
						+ "());\n");
			}
			pw.println("}\n" + "}");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatereversepopulatorBeanForAddingNewAttributeToExistingType() {
		try {
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/resources/" + extensionName
					+ "facades-spring.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			final Node beans = doc.getElementsByTagName("beans").item(0);
			// adding new tags
			final Element aliaspopulator = doc.createElement("alias");
			aliaspopulator.setAttribute("name", "custom" + code + "ReversePopulator");
			aliaspopulator.setAttribute("alias", code.toLowerCase() + "ReversePopulator");

			final Element beanpopulator = doc.createElement("bean");
			beanpopulator.setAttribute("id", "custom" + code + "ReversePopulator");
			beanpopulator.setAttribute("class",
					"com.sap.sapbasket.facades.populators.Custom" + code + "ReversePopulator");
			beanpopulator.setAttribute("parent", "default" + code + "ReversePopulator");
			beans.appendChild(aliaspopulator);
			beans.appendChild(beanpopulator);
			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatereversepopulatorBeanForNewItemType() {
		try {
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketfacades/resources/" + extensionName
					+ "facades-spring.xml";
			final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			final Document doc = docBuilder.parse(filepath);

			final Node beans = doc.getElementsByTagName("beans").item(0);
			// adding new tags
			final Element aliasconverter = doc.createElement("alias");
			aliasconverter.setAttribute("name", "default" + code + "Converter");
			aliasconverter.setAttribute("alias", code.toLowerCase() + "Converter");

			final Element beanconverter = doc.createElement("bean");
			beanconverter.setAttribute("id", "default" + code + "Converter");
			beanconverter.setAttribute("parent", "abstractPopulatingConverter");

			final Element property1 = doc.createElement("property");
			property1.setAttribute("name", "targetClass");
			property1.setAttribute("value", "de.hybris.platform.commercefacades.automation.data." + code + "Data");

			final Element property2 = doc.createElement("property");
			property2.setAttribute("name", "populators");

			final Element list = doc.createElement("list");
			list.setAttribute("merge", "true");

			final Element ref = doc.createElement("ref");
			ref.setAttribute("bean", code.toLowerCase() + "Populator");

			list.appendChild(ref);
			property2.appendChild(list);
			beanconverter.appendChild(property1);
			beanconverter.appendChild(property2);

			final Element aliaspopulator = doc.createElement("alias");
			aliaspopulator.setAttribute("name", "default" + code + "Populator");
			aliaspopulator.setAttribute("alias", code.toLowerCase() + "Populator");

			final Element beanpopulator = doc.createElement("bean");
			beanpopulator.setAttribute("id", "default" + code + "Populator");
			beanpopulator.setAttribute("class", "com.sap.sapbasket.facades.populators.Custom" + code + "Populator");

			beans.appendChild(aliasconverter);
			beans.appendChild(beanconverter);
			beans.appendChild(aliaspopulator);
			beans.appendChild(beanpopulator);

			// saving xml file back
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer();
			final DOMSource source = new DOMSource(doc);
			final StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateFormToDataClass() {
		try {

			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/src/com/sap/sapbasket/storefront/forms/Custom"
							+ code + "DataUtil.java");
			PrintWriter pw = new PrintWriter(f1);
			String dataFromXml = "";
			String superPopulate = "";
			String extendsClass = "";
			String newItemPopulaterInterface = "";
			String newItemOtherImports = "";
			String implementsInterface = "";
			String dataBean = "";
			if (addAttribute) {
				/*
				 * dataFromXml = "import de.hybris.platform.core.model.order." +
				 * code +
				 * "Model;\nimport de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;\nimport de.hybris.platform.commercefacades.order.data.OrderHistoryData;\n"
				 * ;
				 */
				dataFromXml = "import org.springframework.stereotype.Component;\nimport de.hybris.platform.acceleratorstorefrontcommons.util."
						+ code + "DataUtil;\nimport de.hybris.platform.commercefacades.user.data." + code + "Data;\n";
				superPopulate = "final " + code + "Data " + code.toLowerCase() + "Data = super.convertToAddressData("
						+ code.toLowerCase() + "Form);\n";
				// extendsClass = "extends OrderHistoryPopulator\n";
				extendsClass = " extends " + code + "DataUtil\n";
				// dataBean = code + "History";
				dataBean = code;

			} else {
				newItemPopulaterInterface = "import de.hybris.platform.converters.Populator;\n";
				newItemOtherImports = "import de.hybris.platform.commercefacades.automation.data." + code + "Data;\n"
						+ "import com.sap." + extensionName + ".core.model." + code + "Model;\n";
				implementsInterface = "implements Populator<" + code + "Model, " + code + "Data>\n";
				dataBean = code;
			}
			pw.println("/*axle*/\npackage com.sap." + extensionName
					+ ".storefront.forms;\nimport de.hybris.platform.servicelayer.dto.converter.ConversionException;\n"
					+ newItemPopulaterInterface + newItemOtherImports + dataFromXml +

					"@Component(\"customAddressDataUtil\")\npublic class Custom" + code + "DataUtil"
					+ implementsInterface + extendsClass + "{\npublic " + code
					+ "Data convertToVisibleAddressData(final Custom" + code + "Form " + code.toLowerCase()
					+ "Form)\n{\nfinal " + code + "Data " + code.toLowerCase() + "Data = convertToAddressData("
					+ code.toLowerCase() + "Form);\n" + code.toLowerCase()
					+ "Data.setVisibleInAddressBook(true);\nreturn " + code.toLowerCase() + "Data;\n}\npublic " + code
					+ "Data convertToAddressData(final Custom" + code + "Form " + code.toLowerCase() + "Form)\n" + "{\n"
					+ superPopulate + "\n");
			for (int i = 0; i < prop.size(); i++) {
				pw.println(StringUtils.convertFirstLetterToLowerCase(code) + "Data.set"
						+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier()) + "("
						+ StringUtils.convertFirstLetterToLowerCase(code) + "Form.get"
						+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier()) + "());\n");
			}
			pw.println("return " + code.toLowerCase() + "Data;\n" + "}\n" + "public void convert(final " + code
					+ "Data source, final Custom" + code + "Form target)\n{		\nsuper.convert(source, target);\n");
			for (int i = 0; i < prop.size(); i++) {
				pw.println("\ntarget.set" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
						+ "(source.get" + StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier())
						+ "());\n");
			}
			pw.println("}" + "\n}");
			pw.flush();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void editExsistingController() {
		try {
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/src/com/sap/sapbasket/storefront/controllers/pages/AccountPageController.java";

			List<String> lines = Files.readAllLines(Paths.get(filepath));

			String currentLine = null;
			for (int i = 0; i < lines.size(); i++) {
				currentLine = lines.get(i);
				if (currentLine.contains("de.hybris.platform.acceleratorstorefrontcommons.util.AddressDataUtil")) {
					lines.remove(i);
					lines.add(i, "/*axle*/\nimport com.sap.sapbasket.storefront.forms.CustomAddressDataUtil;");
				} else if (currentLine.contains("@Resource(name = \"addressDataUtil\")")) {
					lines.remove(i);
					lines.add(i, "/*axle*/\n@Resource(name = \"customAddressDataUtil\")");
				} else if (currentLine.contains("private AddressDataUtil addressDataUtil;")) {
					lines.remove(i);
					lines.add(i, "/*axle*/\nprivate CustomAddressDataUtil addressDataUtil;");
				} else if (currentLine.contains("de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm")) {
					lines.remove(i);
					lines.add(i, "/*axle*/\nimport com.sap.sapbasket.storefront.forms.CustomAddressForm;");
				} else if (currentLine.contains(" AddressForm")) {
					lines.remove(i);
					lines.add(i, currentLine.replace(" AddressForm", " CustomAddressForm"));
				} else if (currentLine.contains("\"addressForm\"")) {
					lines.remove(i);
					lines.add(i, currentLine.replace("\"addressForm\"", "\"customAddressForm\""));
				}

			}
			FileWriter writer = new FileWriter(filepath);
			for (String str : lines) {
				writer.write(str + "\n");
			}
			writer.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void editExsistingControllertest() {
		try {
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/testsrc/com/sap/sapbasket/storefront/controllers/pages/AccountPageControllerTest.java";

			List<String> lines = Files.readAllLines(Paths.get(filepath));
			String currentLine = null;
			for (int i = 0; i < lines.size(); i++) {
				currentLine = lines.get(i);
				if (currentLine.contains("de.hybris.platform.acceleratorstorefrontcommons.forms." + code + "Form")) {
					lines.remove(i);
					lines.add(i, "/*axle*/\nimport com.sap.sapbasket.storefront.forms.Custom" + code + "Form;");
				} else if (currentLine.contains(" " + code + "Form")) {
					lines.remove(i);
					lines.add(i, currentLine.replace(" " + code + "Form", " Custom" + code + "Form"));
				}
			}
			FileWriter writer = new FileWriter(filepath);
			for (String str : lines) {
				writer.write(str + "\n");
			}
			writer.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateJspForm() {
		try {
			File f1 = new File(
					"C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/src/com/sap/sapbasket/storefront/forms/Custom"
							+ code + "Form.java");
			PrintWriter pw = new PrintWriter(f1);
			if (addAttribute) {

				pw.println("/*axle*/\npackage com.sap." + extensionName
						+ ".storefront.forms;\nimport de.hybris.platform.acceleratorstorefrontcommons.forms." + code
						+ "Form;\npublic class Custom" + code + "Form extends " + code + "Form{\n");
				for (int i = 0; i < prop.size(); i++) {
					pw.println("private " + prop.get(i).getDataType() + " "
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + ";\npublic "
							+ prop.get(i).getDataType() + " get"
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier()) + "()\n{\nreturn "
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier())
							+ ";\n}\npublic void set"
							+ StringUtils.convertFirstLetterToUpperCase(prop.get(i).getQualifier()) + "(final "
							+ prop.get(i).getDataType() + " "
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + "){\nthis."
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + " = "
							+ StringUtils.convertFirstLetterToLowerCase(prop.get(i).getQualifier()) + ";\n}");
				}
				pw.println("\n}");
				pw.flush();

			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void editJsp1() {
		try {
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/webroot/WEB-INF/tags/responsive/address/addressFormElements.tag";
			List<String> lines = Files.readAllLines(Paths.get(filepath));
			String currentLine = null;
			for (int j = 0; j < prop.size(); j++) {
				twinFeild = prop.get(j).getTwinFeild();
				for (int i = 0; i < lines.size(); i++) {
					currentLine = lines.get(i);
					if (currentLine.contains(twinFeild)) {
						lines.add(i + 1, currentLine.replace(twinFeild,
								StringUtils.convertFirstLetterToLowerCase(prop.get(j).getQualifier())));
					}
				}
			}
			if (!lines.get(0).contains("<!--"))
				lines.add(0, "<!-- axle -->");
			FileWriter writer = new FileWriter(filepath);
			for (String str : lines) {
				writer.write(str + "\n");
			}
			writer.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public static void editJsp2() {
		try {
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/webroot/WEB-INF/tags/responsive/address/addressFormSelector.tag";
			List<String> lines = Files.readAllLines(Paths.get(filepath));
			String currentLine = null;
			for (int i = 0; i < lines.size(); i++) {
				currentLine = lines.get(i);
				if (currentLine.contains("commandName")) {
					lines.remove(i);
					lines.add(i, currentLine.replaceAll("addressForm", "customAddressForm"));
				}
			}
			if (!lines.get(0).contains("<!--"))
				lines.add(0, "<!-- axle -->");
			FileWriter writer = new FileWriter(filepath);
			for (String str : lines) {
				writer.write(str + "\n");
			}
			writer.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void editJsp3() {
		try {
			final String filepath = "C://mock/hybris/bin/custom/sapbasket/sapbasketstorefront/web/webroot/WEB-INF/views/responsive/fragments/address/countryAddressForm.jsp";
			List<String> lines = Files.readAllLines(Paths.get(filepath));
			String currentLine = null;
			for (int i = 0; i < lines.size(); i++) {
				currentLine = lines.get(i);
				if (currentLine.contains("commandName")) {
					lines.remove(i);
					lines.add(i, currentLine.replaceAll("addressForm", "customAddressForm"));
				}
			}
			if (!lines.get(0).contains("<!--"))
				lines.add(0, "<!-- axle -->");
			FileWriter writer = new FileWriter(filepath);
			for (String str : lines) {
				writer.write(str + "\n");
			}
			writer.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void listf(String directoryName, ArrayList<File> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile() && file.getPath().endsWith("-items.xml")) {
				files.add(file);
			} else if (file.isDirectory()) {
				listf(file.getAbsolutePath(), files);
			}
		}
	}

	public static void extractItemType(ArrayList<File> files, HashSet<String> itemTypeHashSet) {

		try {
			for (int i = 0; i < files.size(); i++) {

				final String filepath = files.get(i).getPath();
				final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				final Document document = docBuilder.parse(filepath);
				final NodeList itemType = document.getElementsByTagName("itemtype");
				for (int temp = 0; temp < itemType.getLength(); temp++) {
					Node nNode = itemType.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) itemType.item(temp);
						// System.out.println("ItemType : "+temp+" "
						// +eElement.getAttribute("code"));
						itemTypeHashSet.add(eElement.getAttribute("code"));

					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
