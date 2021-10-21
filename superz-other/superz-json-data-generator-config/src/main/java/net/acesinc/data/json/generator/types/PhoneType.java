package net.acesinc.data.json.generator.types;

import com.github.javafaker.PhoneNumber;

/**
 * @author superz
 * @create 2021/10/21 16:04
 */
public class PhoneType extends TypeHandler {
    public static final String TYPE_NAME = "phone";
    public static final String TYPE_DISPLAY_NAME = "Phone";

    private PhoneNumber phoneNumber;

    public PhoneType() {
        phoneNumber = getFaker().phoneNumber();
    }

    @Override
    public Object getNextRandomValue() {
        String str = null;
        str = phoneNumber.cellPhone();
        return str;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
