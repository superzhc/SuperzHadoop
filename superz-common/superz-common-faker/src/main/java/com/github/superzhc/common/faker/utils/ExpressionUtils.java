package com.github.superzhc.common.faker.utils;

import com.github.javafaker.Faker;
import com.github.superzhc.common.format.PlaceholderResolver;

import java.util.*;

/**
 * @author superz
 * @create 2022/7/29 11:33
 **/
public class ExpressionUtils {
    private static final String EXPRESSION_PREFIX = "#{";
    private static final String EXPRESSION_SUFFIX = "}";
    private static final String EXPRESSION_TEMPLATE = "#{%s %s}";

    private static String SURNAME = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季";
    private static String GIRL = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
    private static String BOY = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";

    public static final String NAME = expression("Name.name");
    public static final String SEX = options("男", "女");
    public static final String ID_CARD = regexify("(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)");
    public static final String QQ = regexify("[1-9][0-9]{4,10}");
    public static final String IP = regexify("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)");
    public static final String EDUCATION = options("小学", "初中", "高中", "专科", "本科", "硕士", "博士", "博士后");
    public static final String JOB = options("农民工", "会计,银行出纳员,银行经理,和尚,修女,牧师,演员,女演员,舞蹈演员,导演,电台的音乐节目主持人,节目主持人,司仪,模特儿,电影制片人,歌手,置景工,牙医,医生,护士,儿科医师,内科医师,内科医师助理,精神病医师,心理学者,外科医生,兽医,行李管理者,副驾驶员,海关官员,飞机上的服务员,航海家,飞行员,收票员,旅行社,建筑师,土木工程师,消防人员,警官,政客,邮政工人,垃圾清洁工,面包师,酒吧招待，酒吧侍者,餐厅侍者助手,屠宰商厨师,餐馆老板,,饭店主人,助理厨师,服务员,商人,顾问,资料登录员,行政助理,档案管理者,业务经理,接待员,秘书,主管,速记打字员,律师,执行官,法庭办事员,法官,陪审团主席,律师,翻译者,建筑工人,木匠,电工,壮工,维修工程师,机械工,油漆匠,水管工人,裁缝师,公交司机,机车司机,私人司机,旅行团的服务员,发报机,司机,叉式升降机操作员,出租车司机,卡车司机,海军将军,水手长,船长,渔夫,海军的士官,公务员,水手,水手,海员,换工住宿的女孩,临时照顾幼儿者,看管者,奶妈,儿科医师,产科医师,教师,教授,小学生,学生,老师,导师,理发师,美容师,发型师,宝石商,鞋店,皮鞋匠,钟表匠,男管家,私人司机,园丁,打扫房屋者,女仆,仆人,宇航员,药剂师,工程师,科学家,技师,书商,职员,雇员,售货员,店员,作者,主编,记者,摄影师,摄影记者,作家,农民,矿工,牧羊人,焊接工,统计员,测量技师,裁缝师,计程车司机,房地产经纪人,计划员".split(","));

    private static final String ORDINARY_LICENSE_PLATE_PATTERN = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}";
    private static final String NEW_ENERGY_LICENSE_PLATE_PATTERN = "[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF][A-HJ-NP-Z0-9][0-9]{4}))";
    private static final String LICENSE_PLATE_PATTERN = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|(DF[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";
    private static final String[] LICENSE_PLATE_COLOR_PATTERN = {"蓝", "黄", "白", "绿", "黑"};
    /* 车架 VIN 正则表达式 */
    private static final String VIN_PATTERN = "[A-HJ-NPR-Z\\d]{8}[X\\d][A-HJ-NPR-Z\\d]{3}\\d{5}";

    /**
     * 车牌号（包含新能源车牌号）
     */
    public static final String CAR_LICESE_PLATE = regexify(LICENSE_PLATE_PATTERN);

    /**
     * 普通车牌号
     */
    public static final String CAR_ORDINARY_LICENSE_PLATE = regexify(ORDINARY_LICENSE_PLATE_PATTERN);

    /**
     * 新能源车牌号
     */
    public static final String CAR_NEW_ENERGY_LICENSE_PLATE = regexify(NEW_ENERGY_LICENSE_PLATE_PATTERN);

    /**
     * 车辆的 vin
     * <p>
     * 规则：
     * 1. 由大写字母和数字组成，长度17位；
     * 2. 字母不会出现O、Q、I三个字母；
     * 3. 第9位只能是【0-9】的数字和字母X;
     * 4. 第13-17位只能是数字；
     *
     * @return
     */
    public static final String CAR_VIN = regexify(VIN_PATTERN);

    private static PlaceholderResolver resolver = PlaceholderResolver.getResolver(EXPRESSION_PREFIX, EXPRESSION_SUFFIX);
    private static Faker faker = Faker.instance(new Locale("zh-CN"));

//    public static String name() {
//        return regexify(String.format("[%s]{1}[%s]{2}", SURNAME, faker.bool().bool() ? BOY : GIRL));
//    }

    public static String age(int min, int max) {
        return numberBetween(min, max);
    }

    public static String numberBetween(int min, int max) {
        return expression("number.number_between", min, max);
    }

    public static String options(Object one, Object... params) {
        String template = "(%s){1}";
        StringBuilder optionSb = new StringBuilder();
        optionSb.append(one);
        if (null != params) {
            for (Object param : params) {
                optionSb.append("|");
                optionSb.append(param);
            }
        }
        return regexify(String.format(template, optionSb.toString()));
    }

    public static String regexify(String pattern) {
        return expression("regexify", pattern);
    }

    public static String bothify(String pattern) {
        return expression("bothify", pattern);
    }

    public static String expression(String method, Object... params) {
        StringBuilder sb = new StringBuilder();
        if (null != params && params.length > 0) {
            for (int i = 0, len = params.length; i < len; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("'").append(params[i]).append("'");
            }
        }
        return String.format(EXPRESSION_TEMPLATE, method, sb.toString());
    }

    public static List<String> extractExpressions(String str, boolean standard) {
//        List<String> expressions = new ArrayList<>();
//        int start = str.indexOf("#{");
//        if (start > -1) {
//            while (start != -1) {
//                int end = str.indexOf("}", start);
//                String expression = str.substring(start + 2, end).trim();
//                int expressionStart = expression.indexOf("{");
//                while (expressionStart != -1) {
//                    end = str.indexOf("}", end + 1);
//                    expressionStart = expression.indexOf("{", expressionStart + 1);
//                }
//                expression = str.substring(start + 2, end).trim();
//                // 验证表达式是否有效
//                try {
//                    String expression2 = String.format("%s%s%s", EXPRESSION_PREFIX, expression, EXPRESSION_SUFFIX);
//                    faker.expression(expression2);
//                    expressions.add(standard ? expression2 : expression);
//                } catch (Exception e) {
//                    //ignore
//                }
//                start = str.indexOf("#{", end);
//            }
//        }
        List<String> expressions = resolver.fetchPlaceholders(str);
        List<String> expressions2;
        if (standard) {
            expressions2 = new ArrayList<>(expressions.size());
            for (String expression : expressions) {
                expressions2.add(String.format("%s%s%s", EXPRESSION_PREFIX, expression, EXPRESSION_SUFFIX));
            }
        } else {
            expressions2 = expressions;
        }
        return expressions2;
    }

    public static Map<String, String> build(List<String> expressions, boolean standard) {
        Map<String, String> values = new HashMap<>();
        for (String expression : expressions) {
            if (standard) {
                values.put(expression, faker.expression(expression));
            } else {
                String standardExpression = String.format("%s%s%s", EXPRESSION_PREFIX, expression, EXPRESSION_SUFFIX);
                values.put(expression, faker.expression(standardExpression));
            }
        }
        return values;
    }

    public static String convert(String str, List<String> expressions) {
        Map<String, String> values = new HashMap<>();
        for (String expression : expressions) {
            if (expression.startsWith(EXPRESSION_PREFIX) && expression.endsWith(EXPRESSION_SUFFIX)) {
                values.put(expression.substring(EXPRESSION_PREFIX.length(), expression.length() - EXPRESSION_SUFFIX.length()), faker.expression(expression));
            } else {
                values.put(expression, faker.expression(String.format("%s%s%s", EXPRESSION_PREFIX, expression, EXPRESSION_SUFFIX)));
            }
        }

        PlaceholderResolver resolver = PlaceholderResolver.getResolver(EXPRESSION_PREFIX, EXPRESSION_SUFFIX);
        return resolver.resolveByMap(str, values);
    }

    public static String convert(String str) {
        List<String> expressions = extractExpressions(str, false);
        Map<String, String> values = build(expressions, false);

        PlaceholderResolver resolver = PlaceholderResolver.getResolver(EXPRESSION_PREFIX, EXPRESSION_SUFFIX);
        return resolver.resolveByMap(str, values);
    }

    public static void main(String[] args) {
        String str = "qq:#{regexify [1-9][0-9]{4,10\\}}";
        System.out.println(extractExpressions(str, false));
    }
}