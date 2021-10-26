package net.acesinc.data.json.generator.types;

/**
 * @author superz
 * @create 2021/10/26 17:04
 */
public class FakerExpressionType extends TypeHandler {
    public static final String TYPE_NAME = "faker";
    public static final String TYPE_DISPLAY_NAME = "Faker Expression";

    private String expression;

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length != 1) {
            throw new IllegalArgumentException("Faker Expression's args must one");
        }

        expression = stripQuotes(launchArguments[0]);
        // 验证表达式是否符合规定
        try {
            getFaker().expression(expression);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getNextRandomValue() {
        return getFaker().expression(expression);
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
