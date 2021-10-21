package net.acesinc.data.json.generator.types;

import com.github.javafaker.Color;

/**
 * @author superz
 * @create 2021/10/21 15:58
 */
public class ColorType extends TypeHandler {
    public static final String TYPE_NAME = "color";
    public static final String TYPE_DISPLAY_NAME = "Color";

    private Color color;
    private String type="";

    public ColorType() {
        color = getFaker().color();
    }

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length == 1) {
            type = stripQuotes(launchArguments[0]);
        }else{
            type="";
        }
    }

    @Override
    public Object getNextRandomValue() {
        String str = null;
        switch (type) {
            case "hex":
                str = color.hex();
                break;
            case "name":
            default:
                str = color.name();
        }
        return str;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
