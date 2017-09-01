/*ydo*/
package com.sap.sapbasket.facades.populators;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.commercefacades.automation.data.CRMData;
import com.sap.sapbasket.core.model.CRMModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
public class CustomCRMPopulator implements Populator<CRMModel, CRMData>
{

private Converter<B2BCustomerModel, CustomerData> kitOwnerConverter;

public Converter<B2BCustomerModel, CustomerData> getKitOwnerConverter() {
return kitOwnerConverter;
}
public void setKitOwnerConverter(final Converter<B2BCustomerModel, CustomerData>kitOwnerConverter) {
this.kitOwnerConverter = kitOwnerConverter;
}
@Override
public void populate(final CRMModel source, final CRMData target) throws ConversionException
{


target.setKitName(source.getKitName());

target.setKitOwner(getKitOwnerConverter().convert(source.getKitOwner()));
}
}
