/*axle*/
package com.sap.sapbasket.storefront.forms;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.springframework.stereotype.Component;
import de.hybris.platform.acceleratorstorefrontcommons.util.AddressDataUtil;
import de.hybris.platform.commercefacades.user.data.AddressData;
@Component("customAddressDataUtil")
public class CustomAddressDataUtil extends AddressDataUtil
{
public AddressData convertToVisibleAddressData(final CustomAddressForm addressForm)
{
final AddressData addressData = convertToAddressData(addressForm);
addressData.setVisibleInAddressBook(true);
return addressData;
}
public AddressData convertToAddressData(final CustomAddressForm addressForm)
{
final AddressData addressData = super.convertToAddressData(addressForm);

addressData.setLandmark(addressForm.getLandmark());
 return addressData;
}
public void convert(final AddressData source, final CustomAddressForm target)
{                           
super.convert(source, target);
target.setLandmark(source.getLandmark());
          }}
