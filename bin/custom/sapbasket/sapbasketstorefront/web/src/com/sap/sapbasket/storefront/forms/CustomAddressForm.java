/*axle*/
package com.sap.sapbasket.storefront.forms;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
public class CustomAddressForm extends AddressForm{
private String landmark;
public String getLandmark()
{
return landmark;
}
public void setLandmark(final String landmark){
this.landmark = landmark;
}
}
