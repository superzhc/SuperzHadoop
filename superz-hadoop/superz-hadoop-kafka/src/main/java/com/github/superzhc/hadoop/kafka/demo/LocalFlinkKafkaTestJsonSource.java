package com.github.superzhc.hadoop.kafka.demo;

import com.github.superzhc.hadoop.kafka.tool.MyProducerTool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author superz
 * @create 2021/10/12 11:41
 */
public class LocalFlinkKafkaTestJsonSource extends MyProducerTool {
    private static final String[] NAME = new String[]{
            "暗裔剑魔-亚托克斯-Aatrox",
            "九尾妖狐-阿狸-Ahri",
            "暗影之拳-阿卡丽-Akali",
            "牛头酋长-阿利斯塔-Alistar",
            "殇之木乃伊-阿木木-Amumu",
            "冰晶凤凰-艾尼维亚-Anivia",
            "黑暗之女-安妮-Annie",
            "寒冰射手-艾希-Ashe",
            "铸星龙王-奥瑞利安·索尔-Aurelion Sol",
            "沙漠皇帝-阿兹尔-Azir",
            "星界游神-巴德-Bard",
            "蒸汽机器人-布里茨-Blitzcrank",
            "复仇焰魂-布兰德-Brand",
            "弗雷尔卓德之心-布隆-Braum",
            "皮城女警-凯特琳-Caitlyn",
            "魔蛇之拥-卡西奥佩娅-Cassiopeia",
            "虚空恐惧-科’加斯-Cho’Gath",
            "英勇投弹手-库奇-Corki",
            "诺克萨斯之手-德莱厄斯-Darius",
            "皎月女神-黛安娜-Diana",
            "祖安狂人-蒙多医生-Dr. Mundo",
            "荣耀行刑官-德莱文-Draven",
            "时间刺客-艾克-Ekko",
            "蜘蛛女皇-伊莉丝-Elise",
            "寡妇制造者-伊芙琳-Evelynn",
            "探险家-伊泽瑞尔-Ezreal",
            "末日使者-费德提克-Fiddlesticks",
            "无双剑姬-菲奥娜-Fiora",
            "潮汐海灵-菲兹-Fizz",
            "哨兵之殇-加里奥-Galio",
            "海洋之灾-普朗克-Gangplank",
            "德玛西亚之力-盖伦-Garen",
            "迷失之牙-纳尔-Gnar",
            "酒桶-古拉加斯-Gragas",
            "法外狂徒-格雷福斯-Graves",
            "战争之影-赫卡里姆-Hecarim",
            "大发明家-黑默丁格-Heimerdinger",
            "海兽祭司-俄洛伊-Illaoi",
            "刀锋意志-艾瑞莉娅-Irelia",
            "翠神-艾翁-Ivern",
            "风暴之怒-迦娜-Janna",
            "德玛西亚皇子-嘉文四世-Jarvan IV",
            "武器大师-贾克斯-Jax",
            "未来守护者-杰斯-Jayce",
            "戏命师-烬-Jhin",
            "暴走萝莉-金克丝-Jinx",
            "复仇之矛-卡莉丝塔-Kalista",
            "天启者-卡尔玛-Karma",
            "死亡颂唱者-卡尔萨斯-Karthus",
            "虚空行者-卡萨丁-Kassadin",
            "不祥之刃-卡特琳娜-Katarina",
            "审判天使-凯尔-Kayle",
            "狂暴之心-凯南-Kennen",
            "虚空掠夺者-卡’兹克-Kha’Zix",
            "永猎双子-千珏-Kindred",
            "暴怒骑士-克烈-Kled",
            "深渊巨口-克格’莫-Kog’Maw",
            "诡术妖姬-乐芙兰-LeBlanc",
            "盲僧-李青-Lee Sin",
            "曙光女神-蕾欧娜-Leona",
            "冰霜女巫-丽桑卓-Lissandra",
            "圣枪游侠-卢锡安-Lucian",
            "仙灵女巫-璐璐-Lulu",
            "光辉女郎-拉克丝-Lux",
            "熔岩巨兽-墨菲特-Malphite",
            "虚空先知-玛尔扎哈-Malzahar",
            "扭曲树精-茂凯-Maokai",
            "无极剑圣-易大师-Master Yi",
            "赏金猎人-厄运小姐-Miss Fortune",
            "金属大师-莫德凯撒-Mordekaiser",
            "堕落天使-莫甘娜-Morgana",
            "唤潮鲛姬-娜美-Nami",
            "沙漠死神-内瑟斯-Nasus",
            "深海泰坦-诺提勒斯-Nautilus",
            "狂野女猎手-奈德丽-Nidalee",
            "永恒梦魇-魔腾-Nocturne",
            "雪人骑士-努努-Nunu",
            "狂战士-奥拉夫-Olaf",
            "发条魔灵-奥莉安娜-Orianna",
            "战争之王-潘森-Pantheon",
            "钢铁大使-波比-Poppy",
            "德玛西亚之翼-奎因-Quinn",
            "披甲龙龟-拉莫斯-Rammus",
            "虚空遁地兽-雷克塞-Rek’Sai",
            "荒漠屠夫-雷克顿-Renekton",
            "傲之追猎者-雷恩加尔-Rengar",
            "放逐之刃-锐雯-Riven",
            "机械公敌-兰博-Rumble",
            "流浪法师-瑞兹-Ryze",
            "凛冬之怒-瑟庄妮-Sejuani",
            "恶魔小丑-萨科-Shaco",
            "暮光之眼-慎-Shen",
            "龙血武姬-希瓦娜-Shyvana",
            "炼金术士-辛吉德-Singed",
            "亡灵勇士-赛恩-Sion",
            "战争女神-希维尔-Sivir",
            "水晶先锋-斯卡纳-Skarner",
            "琴瑟仙女-娑娜-Sona",
            "众星之子-索拉卡-Soraka",
            "策士统领-斯维因-Swain",
            "暗黑元首-辛德拉-Syndra",
            "岩雀-塔莉垭-Taliyah",
            "刀锋之影-泰隆-Talon",
            "河流之王-塔姆·肯奇-Tahm Kench",
            "宝石骑士-塔里克-Taric",
            "迅捷斥候-提莫-Teemo",
            "魂锁典狱长-锤石-Thresh",
            "麦林炮手-崔丝塔娜-Tristana",
            "巨魔之王-特朗德尔-Trundle",
            "蛮族之王-泰达米尔-Tryndamere",
            "卡牌大师-崔斯特-Twisted Fate",
            "瘟疫之源-图奇-Twitch",
            "兽灵行者-乌迪尔-Udyr",
            "首领之傲-厄加特-Urgot",
            "惩戒之箭-韦鲁斯-Varus",
            "暗夜猎手-薇恩-Vayne",
            "邪恶小法师-维迦-Veigar",
            "虚空之眼-维克兹-Vel’Koz",
            "皮城执法官-蔚-Vi",
            "机械先驱-维克托-Viktor",
            "猩红收割者-弗拉基米尔-Vladimir",
            "雷霆咆哮-沃利贝尔-Volibear",
            "嗜血猎手-沃里克-Warwick",
            "齐天大圣-孙悟空-Wukong",
            "远古巫灵-泽拉斯-Xerath",
            "德邦总管-赵信-Xin Zhao",
            "疾风剑豪-亚索-Yasuo",
            "掘墓者-约里克-Yorick",
            "生化魔人-扎克-Zac",
            "影流之主-劫-Zed",
            "爆破鬼才-吉格斯-Ziggs",
            "时光守护者-基兰-Zilean",
            "荆棘之兴-婕拉-Zyra"
    };

    @Override
    protected String brokers() {
        return "localhost:9092";
    }

    @Override
    protected String topic() {
        return "flink-test2";
    }

    @Override
    protected String message() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime event = now.minusSeconds(new Random().nextInt(60));
        String content = NAME[new Random().nextInt(NAME.length) % NAME.length] + " send message";
        String msg = "{\"create_time\":\"" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\",\"event_time\":\"" + event.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\",\"content\":\"" + content + "\"}";
        return msg;
    }

    public static void main(String[] args) {
        new LocalFlinkKafkaTestJsonSource().run(args);
    }
}
