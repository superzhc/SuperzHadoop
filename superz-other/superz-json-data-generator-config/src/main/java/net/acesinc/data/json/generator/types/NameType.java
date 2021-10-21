package net.acesinc.data.json.generator.types;

import com.github.javafaker.Name;

/**
 * @author superz
 * @create 2021/10/21 15:20
 */
public class NameType extends TypeHandler {
    public static final String TYPE_NAME = "name";
    public static final String TYPE_DISPLAY_NAME = "Name";

    private Name name;
    private String type = "full";

    public NameType(){
        super();
        name=getFaker().name();
    }

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length == 1) {
            type = stripQuotes(launchArguments[0]);
        }else{
            type="full";
        }
    }

    @Override
    public Object getNextRandomValue() {
        switch (type) {
            case "first":
                return name.firstName();
            case "last":
                return name.lastName();
            case "full":
            default:
                return name.fullName();
        }
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
