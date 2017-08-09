/*axle*/
package com.sap.sapbasket.facades.populators;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
public class CustomAddressReversePopulator extends AddressReversePopulator
{
@Override
public void populate(final AddressData source, final AddressModel target) throws ConversionException
{
super.populate(source, target);

target.setLandmark(source.getLandmark());
}
}
