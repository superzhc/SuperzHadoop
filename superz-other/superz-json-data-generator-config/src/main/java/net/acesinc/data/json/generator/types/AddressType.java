package net.acesinc.data.json.generator.types;

import com.github.javafaker.Address;

/**
 * @author superz
 * @create 2021/10/21 15:27
 */
public class AddressType extends TypeHandler {
    public static final String TYPE_NAME = "address";
    public static final String TYPE_DISPLAY_NAME = "Address";

    private Address address;
    private String type = "";

    public AddressType() {
        super();
        address = getFaker().address();
    }

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length == 1) {
            type = stripQuotes(launchArguments[0]);
        } else {
            type = "";
        }
    }

    @Override
    public Object getNextRandomValue() {
        String str = null;
        switch (type) {
            // 楼号
            case "buildingNumber":
                str = address.buildingNumber();
                break;
            case "city":
                str = address.city();
                break;
            case "cityName":
                str = address.cityName();
                break;
            case "country":
                str = address.country();
                break;
            case "countryCode":
                str = address.countryCode();
                break;
            case "street":
            case "streetAddress":
                str = address.streetAddress();
                break;
            case "streetName":
                str = address.streetName();
                break;
            case "latitude":
                str = address.latitude();
                break;
            case "longitude":
                str = address.longitude();
                break;
            case "latlon":
                str = String.format("(%s,%s)", address.latitude(), address.longitude());
                break;
            case "lonlat":
                str = String.format("(%s,%s)", address.longitude(), address.latitude());
                break;
            case "zip":
            case "zipCode":
                str = address.zipCode();
                break;
            case "full":
            default:
                /* fix bug:Faker工具的Address是按照美国写的方式来的，不符合中国 */
                str = address.cityName() + "市" + address.streetAddress();
                break;
        }
        return str;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
