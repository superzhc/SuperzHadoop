package com.github.superzhc.common.faker;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

/**
 * @author superz
 * @create 2021/12/8 16:40
 */
public class NameEnhance extends Name {
    private static String surname = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季";
    private static String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
    private static String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";

    private final Faker faker;

    /**
     * Internal constructor, not to be used by clients.  Instances of {@link Name} should be accessed via
     * {@link Faker#name()}.
     *
     * @param faker
     */
    protected NameEnhance(Faker faker) {
        super(faker);
        this.faker = faker;
    }

    public String boyName() {
        return faker.regexify(String.format("[%s]{1}[%s]{2}", surname, boy));
    }

    public String girlName() {
        return faker.regexify(String.format("[%s]{1}[%s]{1,2}", surname, girl));
    }

    public static void main(String[] args) throws Exception {
        NameEnhance nameEnhance = new NameEnhance(new Faker());
        for (int i = 0; i < 1000; i++) {
            System.out.println(nameEnhance.boyName());
            System.out.println(nameEnhance.girlName());
            Thread.sleep(1000);
        }
    }
}
