/*axle*/
package com.sap.sapbasket.facades.populators;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
public class CustomAddressPopulator extends AddressPopulator
{
@Override
public void populate(final AddressModel source, final AddressData target) throws ConversionException
{
super.populate(source, target);


target.setLandmark(source.getLandmark());

}
}
