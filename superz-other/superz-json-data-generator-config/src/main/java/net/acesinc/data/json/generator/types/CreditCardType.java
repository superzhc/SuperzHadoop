package net.acesinc.data.json.generator.types;

import com.github.javafaker.Business;

/**
 * @author superz
 * @create 2021/10/21 15:52
 */
public class CreditCardType extends TypeHandler {
    public static final String TYPE_NAME = "creditCard";
    public static final String TYPE_DISPLAY_NAME = "CreditCard";

    private Business business;
    private String type;

    public CreditCardType() {
        business = getFaker().business();
    }

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length != 1) {
            throw new IllegalArgumentException("You must set a param : number , type , expiry .");
        }
        type = stripQuotes(launchArguments[0]);
    }

    @Override
    public Object getNextRandomValue() {
        String str = null;
        switch (type) {
            case "number":
                str = business.creditCardNumber();
                break;
            case "type":
                str = business.creditCardType();
                break;
            case "expiry":
                str = business.creditCardExpiry();
                break;
        }
        return str;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
