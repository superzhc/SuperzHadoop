package net.acesinc.data.json.generator.types;

/**
 * 提供正则表达式匹配
 *
 * @author superz
 * @create 2021/10/21 17:59
 */
public class RegExpType extends TypeHandler {
    public static final String TYPE_NAME = "pattern";
    public static final String TYPE_DISPLAY_NAME = "RegExp";

    private String pattern;

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length != 1) {
            throw new IllegalArgumentException("You must provide RegExp .");
        }
        pattern = stripQuotes(launchArguments[0]);
    }

    @Override
    public Object getNextRandomValue() {
        return getFaker().regexify(pattern);
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
