package ingros.ware.client.utils.value.impl;


import ingros.ware.client.utils.value.Value;

/**
 * made by Xen for OhareWare
 *
 * @since 7/21/2019
 **/
public class StringValue extends Value<String> {

    public StringValue(String label, String value) {
        super(label, value);
    }

    public StringValue(String label, String value,Value parentValueObject,String parentValue) {
        super(label, value,parentValueObject,parentValue);
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
