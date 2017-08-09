/*ydo*/
package com.sap.sapbasket.facades.populators;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.commercefacades.automation.data.CRMKitData;
import com.sap.sapbasket.core.model.CRMKitModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
public class CustomCRMKitPopulator implements Populator<CRMKitModel, CRMKitData>
{

private Converter<B2BCustomerModel, CustomerData> kitOwnerConverter;

public Converter<B2BCustomerModel, CustomerData> getKitOwnerConverter() {
return kitOwnerConverter;
}
public void setKitOwnerConverter(final Converter<B2BCustomerModel, CustomerData>kitOwnerConverter) {
this.kitOwnerConverter = kitOwnerConverter;
}
private Converter<B2BUnitModel, B2BUnitData> b2bUnitConverter;

public Converter<B2BUnitModel, B2BUnitData> getB2bUnitConverter() {
return b2bUnitConverter;
}
public void setB2bUnitConverter(final Converter<B2BUnitModel, B2BUnitData>b2bUnitConverter) {
this.b2bUnitConverter = b2bUnitConverter;
}
@Override
public void populate(final CRMKitModel source, final CRMKitData target) throws ConversionException
{


target.setKitCode(source.getKitCode());


target.setKitName(source.getKitName());

target.setKitOwner(getKitOwnerConverter().convert(source.getKitOwner()));

target.setTimestamp(source.getTimestamp());


target.setActive(source.getActive());

target.setB2bUnit(getB2bUnitConverter().convert(source.getB2bUnit()));
}
}
