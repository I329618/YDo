/*axle*/
package com.sap.sapbasket.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import javax.annotation.Resource;

import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sap.sapbasket.facades.impl.DefaultCRMKitFacade;
import de.hybris.platform.commercefacades.automation.data.CRMKitData;

@Controller
@RequestMapping("/metadatacrmkit")
public class DummyCRMKitController extends AbstractSearchPageController
{
@Resource(name = "crmkitFacade")
private DefaultCRMKitFacade crmkitFacade;

@RequestMapping(value = "/crmkit", method = RequestMethod.GET)
@RequireHardLogIn
@ResponseBody
public String getCRMKit() throws CMSItemNotFoundException
{
final List<CRMKitData> crmkitData = crmkitFacade.getCRMKit();
try
{
final ObjectMapper m = new ObjectMapper();
final StringBuilder htmlBuilder = new StringBuilder();
htmlBuilder.append("<html>");
htmlBuilder.append("<head><title>CRMKitModel List</title></head>");
htmlBuilder.append("<body>");
htmlBuilder.append("<h3>List of CRMKitModel</h3>");
for (int i = 0; i < crmkitData.size(); i++)
{
String temp = m.writeValueAsString(crmkitData.get(i));
temp = temp.replace("{", "");
temp = temp.replace("}", "");
temp = temp.replace("\"", "");
htmlBuilder.append("<p>" + temp + "</p>");
}
htmlBuilder.append("</body>");
htmlBuilder.append("</html>");
return htmlBuilder.toString();
}
catch (final Exception e)
{
e.printStackTrace();
return "Unable to fetech the Data";
}
}
}
