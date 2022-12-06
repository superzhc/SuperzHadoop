package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.MapUtils;

import java.util.*;

/**
 * @author superz
 * @create 2022/9/22 9:21
 **/
public class MoFish {
    /**
     * [综合]知乎热榜
     *
     * @return
     */
    public static List<Map<String, Object>> zhiHu() {
        return execute(1);
    }

    /**
     * [社区]虎扑热榜
     *
     * @return
     */
    public static List<Map<String, Object>> huPu() {
        return execute(2);
    }

    /**
     * [综合]MSN热榜
     *
     * @return
     */
    public static List<Map<String, Object>> MSN() {
        return execute(3);
    }

    /**
     * [社区]天涯热榜
     *
     * @return
     */
    public static List<Map<String, Object>> tianYa() {
        return execute(6);
    }

    /**
     * [综合]知乎日报
     *
     * @return
     */
    public static List<Map<String, Object>> ZHDaily() {
        return execute(7);
    }

    /**
     * [科技]虎嗅热榜
     *
     * @return
     */
    public static List<Map<String, Object>> huXiu() {
        return execute(8);
    }

    /**
     * [社区]水木社区
     *
     * @return
     */
    public static List<Map<String, Object>> waterAndWood() {
        return execute(9);
    }

    /**
     * [综合]网易热榜
     *
     * @return
     */
    public static List<Map<String, Object>> WYNews() {
        return execute(10);
    }

    /**
     * [综合]微信热榜
     *
     * @return
     */
    public static List<Map<String, Object>> weiXin() {
        return execute(11);
    }

    /**
     * [科技]36Kr热榜
     *
     * @return
     */
    public static List<Map<String, Object>> a36Kr() {
        return execute(12);
    }

    /**
     * [社区]贴吧热榜
     *
     * @return
     */
    public static List<Map<String, Object>> tieBa() {
        return execute(56);
    }

    /**
     * [综合]豆瓣热榜
     *
     * @return
     */
    public static List<Map<String, Object>> douBan() {
        return execute(57);
    }

    /**
     * [综合]微博热搜
     *
     * @return
     */
    public static List<Map<String, Object>> weiBo() {
        return execute(58);
    }

    /**
     * [社区]V2EX热榜
     *
     * @return
     */
    public static List<Map<String, Object>> V2EX() {
        return execute(59);
    }

    /**
     * [IT]思否热榜
     *
     * @return
     */
    public static List<Map<String, Object>> segmentfault() {
        return execute(60);
    }

    /**
     * [社区]黑客派榜
     *
     * @return
     */
    public static List<Map<String, Object>> hacPai() {
        return execute(62);
    }

    /**
     * [综合]百度热搜
     *
     * @return
     */
    public static List<Map<String, Object>> baidu() {
        return execute(83);
    }

    /**
     * [IT]GitHub热榜
     *
     * @return
     */
    public static List<Map<String, Object>> gitHub() {
        return execute(85);
    }

    /**
     * [科技]果壳热榜
     *
     * @return
     */
    public static List<Map<String, Object>> guoKr() {
        return execute(86);
    }

    /**
     * [综合]Zaker推荐
     *
     * @return
     */
    public static List<Map<String, Object>> myZaker() {
        return execute(90);
    }

    /**
     * [IT]CSDN热榜
     *
     * @return
     */
    public static List<Map<String, Object>> csdn() {
        return execute(104);
    }

    /**
     * [社区]凯迪热榜
     *
     * @return
     */
    public static List<Map<String, Object>> kD() {
        return execute(105);
    }

    /**
     * [社区]NGA热榜
     *
     * @return
     */
    public static List<Map<String, Object>> NGA() {
        return execute(106);
    }

    /**
     * [科技]Chiphell
     *
     * @return
     */
    public static List<Map<String, Object>> chiphell() {
        return execute(109);
    }

    /**
     * [综合]抽屉热榜
     *
     * @return
     */
    public static List<Map<String, Object>> chouTi() {
        return execute(110);
    }

    /**
     * [娱乐]煎蛋热榜
     *
     * @return
     */
    public static List<Map<String, Object>> jianDan() {
        return execute(111);
    }

    /**
     * [科技]IT之家
     *
     * @return
     */
    public static List<Map<String, Object>> iTHome() {
        return execute(112);
    }

    /**
     * [IT]开源中国
     *
     * @return
     */
    public static List<Map<String, Object>> OSChina() {
        return execute(114);
    }

    /**
     * [娱乐]Bilibili热榜
     *
     * @return
     */
    public static List<Map<String, Object>> bilibili() {
        return execute(115);
    }

    /**
     * [科技]少数派榜
     *
     * @return
     */
    public static List<Map<String, Object>> SSPAI() {
        return execute(116);
    }

    /**
     * [综合]什么值得买
     *
     * @return
     */
    public static List<Map<String, Object>> smzdm() {
        return execute(117);
    }

    /**
     * [社区]汽车之家
     *
     * @return
     */
    public static List<Map<String, Object>> carModel() {
        return execute(118);
    }

    /**
     * [科技]雷科技榜
     *
     * @return
     */
    public static List<Map<String, Object>> leiKeJi() {
        return execute(119);
    }

    /**
     * [综合]澎湃新闻
     *
     * @return
     */
    public static List<Map<String, Object>> thePaper() {
        return execute(120);
    }

    /**
     * [综合]亿欧热榜
     *
     * @return
     */
    public static List<Map<String, Object>> iyiou() {
        return execute(121);
    }

    /**
     * [综合]篝火热榜
     *
     * @return
     */
    public static List<Map<String, Object>> gouHuo() {
        return execute(122);
    }

    /**
     * [综合]观察者榜
     *
     * @return
     */
    public static List<Map<String, Object>> guanCha() {
        return execute(123);
    }

    /**
     * [综合]CBNData
     *
     * @return
     */
    public static List<Map<String, Object>> cbnData() {
        return execute(124);
    }

    /**
     * [社区]吾爱破解
     *
     * @return
     */
    public static List<Map<String, Object>> a52pojie() {
        return execute(125);
    }

    /**
     * [综合]凤凰网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> iFeng() {
        return execute(126);
    }

    /**
     * [综合]腾讯科技
     *
     * @return
     */
    public static List<Map<String, Object>> QQTech() {
        return execute(127);
    }

    /**
     * [综合]界面新闻
     *
     * @return
     */
    public static List<Map<String, Object>> jieMian() {
        return execute(128);
    }

    /**
     * [娱乐]机核热榜
     *
     * @return
     */
    public static List<Map<String, Object>> gcores() {
        return execute(129);
    }

    /**
     * [娱乐]马蜂窝热榜
     *
     * @return
     */
    public static List<Map<String, Object>> maFengWo() {
        return execute(130);
    }

    /**
     * [科技]投资界热榜
     *
     * @return
     */
    public static List<Map<String, Object>> pedaily() {
        return execute(131);
    }

    /**
     * [娱乐]数字尾巴
     *
     * @return
     */
    public static List<Map<String, Object>> dgtle() {
        return execute(132);
    }

    /**
     * [娱乐]极客公园
     *
     * @return
     */
    public static List<Map<String, Object>> gPark() {
        return execute(133);
    }

    /**
     * [娱乐]时光网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> mTime() {
        return execute(134);
    }

    /**
     * [沙雕图]每日趣图
     *
     * @return
     */
    public static List<Map<String, Object>> img() {
        return execute(135);
    }

    /**
     * [沙雕图]每日动图
     *
     * @return
     */
    public static List<Map<String, Object>> gif() {
        return execute(136);
    }

    /**
     * [社区]蜂鸟网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> fengNiao() {
        return execute(138);
    }

    /**
     * [综合]TED热榜
     *
     * @return
     */
    public static List<Map<String, Object>> TED() {
        return execute(139);
    }

    /**
     * [综合]ReadHub
     *
     * @return
     */
    public static List<Map<String, Object>> readHub() {
        return execute(141);
    }

    /**
     * [娱乐]AcFun热榜
     *
     * @return
     */
    public static List<Map<String, Object>> acFun() {
        return execute(142);
    }

    /**
     * [社区]一亩三分地
     *
     * @return
     */
    public static List<Map<String, Object>> sanDi() {
        return execute(143);
    }

    /**
     * [财经]巴比特热榜
     *
     * @return
     */
    public static List<Map<String, Object>> a8btc() {
        return execute(144);
    }

    /**
     * [财经]火星财经
     *
     * @return
     */
    public static List<Map<String, Object>> huoXing() {
        return execute(146);
    }

    /**
     * [财经]星球日报
     *
     * @return
     */
    public static List<Map<String, Object>> oDaily() {
        return execute(147);
    }

    /**
     * [综合]抖音热榜
     *
     * @return
     */
    public static List<Map<String, Object>> douYin() {
        return execute(148);
    }

    /**
     * [综合]梨视频热榜
     *
     * @return
     */
    public static List<Map<String, Object>> liVideo() {
        return execute(149);
    }

    /**
     * [综合]今日头条
     *
     * @return
     */
    public static List<Map<String, Object>> touTiao() {
        return execute(150);
    }

    /**
     * [综合]Zaker热榜
     *
     * @return
     */
    public static List<Map<String, Object>> newMyZaker() {
        return execute(151);
    }

    /**
     * [综合]简书热榜
     *
     * @return
     */
    public static List<Map<String, Object>> jianShu() {
        return execute(152);
    }

    /**
     * [综合]国家地理
     *
     * @return
     */
    public static List<Map<String, Object>> dili360() {
        return execute(153);
    }

    /**
     * [IT]掘金热榜
     *
     * @return
     */
    public static List<Map<String, Object>> jueJin() {
        return execute(154);
    }

    /**
     * [综合]上观热榜
     *
     * @return
     */
    public static List<Map<String, Object>> jFdaily() {
        return execute(155);
    }

    /**
     * [综合]打喷嚏热榜
     *
     * @return
     */
    public static List<Map<String, Object>> daPenTi() {
        return execute(156);
    }

    /**
     * [综合]壹心理热榜
     *
     * @return
     */
    public static List<Map<String, Object>> xinLi() {
        return execute(157);
    }

    /**
     * [综合]收趣热榜
     *
     * @return
     */
    public static List<Map<String, Object>> shouQu() {
        return execute(159);
    }

    /**
     * [综合]360Doc
     *
     * @return
     */
    public static List<Map<String, Object>> a360Doc() {
        return execute(160);
    }

    /**
     * [综合]看看热榜
     *
     * @return
     */
    public static List<Map<String, Object>> kanKan() {
        return execute(161);
    }

    /**
     * [综合]今日看点
     *
     * @return
     */
    public static List<Map<String, Object>> todayFocus() {
        return execute(162);
    }

    /**
     * [综合]新京报热榜
     *
     * @return
     */
    public static List<Map<String, Object>> BJnews() {
        return execute(164);
    }

    /**
     * [科技]爱范热榜
     *
     * @return
     */
    public static List<Map<String, Object>> iFanr() {
        return execute(1007);
    }

    /**
     * [IT]InfoQ最热
     *
     * @return
     */
    public static List<Map<String, Object>> infoQTop() {
        return execute(1008);
    }

    /**
     * [IT]InfoQ每日
     *
     * @return
     */
    public static List<Map<String, Object>> infoQDaily() {
        return execute(1009);
    }

    /**
     * [娱乐]百度日报
     *
     * @return
     */
    public static List<Map<String, Object>> baiDuDaily() {
        return execute(1010);
    }

    /**
     * [娱乐]博海拾贝
     *
     * @return
     */
    public static List<Map<String, Object>> bHSB() {
        return execute(1011);
    }

    /**
     * [科技]威峰网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> feng() {
        return execute(1012);
    }

    /**
     * [科技]科普中国
     *
     * @return
     */
    public static List<Map<String, Object>> kePuChina() {
        return execute(1013);
    }

    /**
     * [科技]中关村在线
     *
     * @return
     */
    public static List<Map<String, Object>> zol() {
        return execute(1014);
    }

    /**
     * [科技]钛媒体热榜
     *
     * @return
     */
    public static List<Map<String, Object>> tMTPost() {
        return execute(1015);
    }

    /**
     * [娱乐]场库热榜
     *
     * @return
     */
    public static List<Map<String, Object>> vMovier() {
        return execute(1016);
    }

    /**
     * [娱乐]B站专栏
     *
     * @return
     */
    public static List<Map<String, Object>> bArticle() {
        return execute(1017);
    }

    /**
     * [科技]CnBeta热榜
     *
     * @return
     */
    public static List<Map<String, Object>> cnBeta() {
        return execute(1018);
    }

    /**
     * [娱乐]站酷热榜
     *
     * @return
     */
    public static List<Map<String, Object>> zcool() {
        return execute(1019);
    }

    /**
     * [社区]飞客茶馆
     *
     * @return
     */
    public static List<Map<String, Object>> flyerTea() {
        return execute(1020);
    }

    /**
     * [科技]数英每日
     *
     * @return
     */
    public static List<Map<String, Object>> digitalingDay() {
        return execute(1021);
    }

    /**
     * [科技]数英最热
     *
     * @return
     */
    public static List<Map<String, Object>> digitaling() {
        return execute(1022);
    }

    /**
     * [科技]机器之心最新
     *
     * @return
     */
    public static List<Map<String, Object>> robotHeartDay() {
        return execute(1024);
    }

    /**
     * [科技]机器之心
     *
     * @return
     */
    public static List<Map<String, Object>> robotHeart() {
        return execute(1025);
    }

    /**
     * [财经]金色财经
     *
     * @return
     */
    public static List<Map<String, Object>> jinSe() {
        return execute(1027);
    }

    /**
     * [购物]淘宝实时榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao() {
        return execute(1028);
    }

    /**
     * [购物]羊毛线报
     *
     * @return
     */
    public static List<Map<String, Object>> MMM() {
        return execute(1029);
    }

    /**
     * [购物]淘宝热售榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobaoAll() {
        return execute(1031);
    }

    /**
     * [综合]微信阅读
     *
     * @return
     */
    public static List<Map<String, Object>> WXRead() {
        return execute(1033);
    }

    /**
     * [IT]开发者头条
     *
     * @return
     */
    public static List<Map<String, Object>> KFTT() {
        return execute(1034);
    }

    /**
     * [IT]Linux中国
     *
     * @return
     */
    public static List<Map<String, Object>> cNLinux() {
        return execute(1035);
    }

    /**
     * [购物]京东热销榜
     *
     * @return
     */
    public static List<Map<String, Object>> jingdong() {
        return execute(1036);
    }

    /**
     * [购物]京东秒杀
     *
     * @return
     */
    public static List<Map<String, Object>> jdSeckill() {
        return execute(1037);
    }

    /**
     * [购物]京东拼团
     *
     * @return
     */
    public static List<Map<String, Object>> jdAssemble() {
        return execute(1038);
    }

    /**
     * [购物]京东热榜
     *
     * @return
     */
    public static List<Map<String, Object>> jdzyrx() {
        return execute(1039);
    }

    /**
     * [购物]京东自营榜
     *
     * @return
     */
    public static List<Map<String, Object>> jdzyms() {
        return execute(1040);
    }

    /**
     * [体育]懂球帝热
     *
     * @return
     */
    public static List<Map<String, Object>> dqd() {
        return execute(1042);
    }

    /**
     * [体育]虎扑NBA
     *
     * @return
     */
    public static List<Map<String, Object>> hupuNBA() {
        return execute(1044);
    }

    /**
     * [科技]雪球热榜
     *
     * @return
     */
    public static List<Map<String, Object>> snowBall() {
        return execute(1045);
    }

    /**
     * [科技]快科技热榜
     *
     * @return
     */
    public static List<Map<String, Object>> kkj() {
        return execute(1048);
    }

    /**
     * [科技]老司机热榜
     *
     * @return
     */
    public static List<Map<String, Object>> lsj() {
        return execute(1049);
    }

    /**
     * [购物]淘宝零食榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobaoFood() {
        return execute(1050);
    }

    /**
     * [购物]淘宝数码榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobaoIT() {
        return execute(1051);
    }

    /**
     * [IT]FreeBuf
     *
     * @return
     */
    public static List<Map<String, Object>> freeBuf() {
        return execute(1052);
    }

    /**
     * [综合]知乎推荐
     *
     * @return
     */
    public static List<Map<String, Object>> zhiHuRecommend() {
        return execute(1053);
    }

    /**
     * [娱乐]微博热门
     *
     * @return
     */
    public static List<Map<String, Object>> weiBoHot() {
        return execute(1054);
    }

    /**
     * [娱乐]品玩热榜
     *
     * @return
     */
    public static List<Map<String, Object>> pingWest() {
        return execute(1055);
    }

    /**
     * [科技]量子位热榜
     *
     * @return
     */
    public static List<Map<String, Object>> qBitai() {
        return execute(1056);
    }

    /**
     * [综合]人间热榜
     *
     * @return
     */
    public static List<Map<String, Object>> renJian() {
        return execute(1057);
    }

    /**
     * [游戏]3DMGAME
     *
     * @return
     */
    public static List<Map<String, Object>> a3dmGame() {
        return execute(1058);
    }

    /**
     * [综合]龙腾网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> longTeng() {
        return execute(1059);
    }

    /**
     * [社区]吾爱人气热门
     *
     * @return
     */
    public static List<Map<String, Object>> a52pojieRenQiHot() {
        return execute(1060);
    }

    /**
     * [体育]腾讯体育
     *
     * @return
     */
    public static List<Map<String, Object>> qQTiYU() {
        return execute(1061);
    }

    /**
     * [购物]聚划算单品榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobaoJHS() {
        return execute(1062);
    }

    /**
     * [购物]天猫超市榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobaoMall() {
        return execute(1063);
    }

    /**
     * [购物]淘宝巅峰榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobaoHigh() {
        return execute(1064);
    }

    /**
     * [综合]鱼塘热榜
     *
     * @return
     */
    public static List<Map<String, Object>> fish() {
        return execute(1065);
    }

    /**
     * [购物]淘宝综合榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3756() {
        return execute(1066);
    }

    /**
     * [购物]淘宝母婴榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3760() {
        return execute(1067);
    }

    /**
     * [购物]淘宝内衣榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3765() {
        return execute(1068);
    }

    /**
     * [购物]淘宝男装榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3764() {
        return execute(1069);
    }

    /**
     * [购物]淘宝美妆榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3763() {
        return execute(1070);
    }

    /**
     * [购物]淘宝鞋包榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3762() {
        return execute(1071);
    }

    /**
     * [购物]淘宝数码榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3759() {
        return execute(1072);
    }

    /**
     * [购物]淘宝家居榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3758() {
        return execute(1073);
    }

    /**
     * [购物]淘宝食品榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3761() {
        return execute(1074);
    }

    /**
     * [购物]淘宝运动榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3766() {
        return execute(1075);
    }

    /**
     * [购物]淘宝女装榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao3767() {
        return execute(1076);
    }

    /**
     * [购物]淘宝特惠榜
     *
     * @return
     */
    public static List<Map<String, Object>> taobao4094() {
        return execute(1077);
    }

    /**
     * [社区]Saraba1st
     *
     * @return
     */
    public static List<Map<String, Object>> saraba1st() {
        return execute(1078);
    }

    /**
     * [IT]安全客热榜
     *
     * @return
     */
    public static List<Map<String, Object>> anquanke() {
        return execute(1079);
    }

    /**
     * [社区]宽带山热榜
     *
     * @return
     */
    public static List<Map<String, Object>> kdslife() {
        return execute(1080);
    }

    /**
     * [综合]设计癖热榜
     *
     * @return
     */
    public static List<Map<String, Object>> shejipi() {
        return execute(1081);
    }

    /**
     * [科技]新浪科技
     *
     * @return
     */
    public static List<Map<String, Object>> techSina() {
        return execute(1082);
    }

    /**
     * [科技]DoNews热榜
     *
     * @return
     */
    public static List<Map<String, Object>> doNews() {
        return execute(1083);
    }

    /**
     * [军事]超级大本营
     *
     * @return
     */
    public static List<Map<String, Object>> cJDBY() {
        return execute(1085);
    }

    /**
     * [游戏]游侠网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> yXW() {
        return execute(1086);
    }

    /**
     * [游戏]17173热榜
     *
     * @return
     */
    public static List<Map<String, Object>> a17173() {
        return execute(1087);
    }

    /**
     * [游戏]多玩游戏
     *
     * @return
     */
    public static List<Map<String, Object>> dw() {
        return execute(1088);
    }

    /**
     * [游戏]游民星空
     *
     * @return
     */
    public static List<Map<String, Object>> ymxk() {
        return execute(1089);
    }

    /**
     * [游戏]巴哈姆特
     *
     * @return
     */
    public static List<Map<String, Object>> bhmt() {
        return execute(1090);
    }

    /**
     * [军事]中华网军事
     *
     * @return
     */
    public static List<Map<String, Object>> zhjsw() {
        return execute(1096);
    }

    /**
     * [军事]西陆网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> xlw() {
        return execute(1097);
    }

    /**
     * [军事]中国军网
     *
     * @return
     */
    public static List<Map<String, Object>> zgjw() {
        return execute(1098);
    }

    /**
     * [军事]铁血网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> txw() {
        return execute(1099);
    }

    /**
     * [游戏]腾讯游戏
     *
     * @return
     */
    public static List<Map<String, Object>> txyx() {
        return execute(1100);
    }

    /**
     * [IT]Laravel社区
     *
     * @return
     */
    public static List<Map<String, Object>> laravelChina() {
        return execute(1101);
    }

    /**
     * [IT]golang中文网
     *
     * @return
     */
    public static List<Map<String, Object>> goyyzww() {
        return execute(1102);
    }

    /**
     * [游戏]游久网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> youjiuw() {
        return execute(1103);
    }

    /**
     * [公众号]科技热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 科技() {
        return execute(1104);
    }

    /**
     * [公众号]搞笑热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 搞笑() {
        return execute(1105);
    }

    /**
     * [公众号]开发热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 开发() {
        return execute(1107);
    }

    /**
     * [公众号]数码热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 数码() {
        return execute(1108);
    }

    /**
     * [公众号]财经热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 财经() {
        return execute(1109);
    }

    /**
     * [公众号]文化热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 文化() {
        return execute(1110);
    }

    /**
     * [公众号]情感热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 情感() {
        return execute(1111);
    }

    /**
     * [公众号]美食热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 美食() {
        return execute(1112);
    }

    /**
     * [公众号]职场热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 职场() {
        return execute(1113);
    }

    /**
     * [公众号]运动热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 运动() {
        return execute(1114);
    }

    /**
     * [公众号]教育热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 教育() {
        return execute(1115);
    }

    /**
     * [公众号]科学热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 科学() {
        return execute(1116);
    }

    /**
     * [公众号]游戏热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 游戏() {
        return execute(1117);
    }

    /**
     * [公众号]汽车热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 汽车() {
        return execute(1118);
    }

    /**
     * [公众号]房产热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 房产() {
        return execute(1119);
    }

    /**
     * [公众号]孕育热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 孕妇() {
        return execute(1120);
    }

    /**
     * [公众号]动漫热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 动漫() {
        return execute(1121);
    }

    /**
     * [公众号]品牌热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 品牌() {
        return execute(1122);
    }

    /**
     * [公众号]营销热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 营销() {
        return execute(1123);
    }

    /**
     * [公众号]家居热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 家居() {
        return execute(1124);
    }

    /**
     * [公众号]宗教热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 宗教() {
        return execute(1125);
    }

    /**
     * [公众号]星座热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 星座() {
        return execute(1126);
    }

    /**
     * [公众号]宠物热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 宠物() {
        return execute(1127);
    }

    /**
     * [公众号]政务热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 政务() {
        return execute(1128);
    }

    /**
     * [公众号]媒体热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 媒体() {
        return execute(1129);
    }

    /**
     * [公众号]百科热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 百科() {
        return execute(1130);
    }

    /**
     * [公众号]时尚热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 时尚() {
        return execute(1131);
    }

    /**
     * [公众号]影视热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 影视() {
        return execute(1132);
    }

    /**
     * [公众号]摄影热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 摄影() {
        return execute(1133);
    }

    /**
     * [公众号]健康热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 健康() {
        return execute(1134);
    }

    /**
     * [公众号]旅游热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 旅游() {
        return execute(1135);
    }

    /**
     * [公众号]军事热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 军事() {
        return execute(1136);
    }

    /**
     * [公众号]人文热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 人文() {
        return execute(1137);
    }

    /**
     * [公众号]文摘热榜
     *
     * @return
     */
    public static List<Map<String, Object>> 文摘() {
        return execute(1138);
    }

    /**
     * [社区]其乐热榜
     *
     * @return
     */
    public static List<Map<String, Object>> qiLe() {
        return execute(2719);
    }

    /**
     * [IT]199it热榜
     *
     * @return
     */
    public static List<Map<String, Object>> iT199() {
        return execute(2720);
    }

    /**
     * [社区]全球主机交流
     *
     * @return
     */
    public static List<Map<String, Object>> hostLoc() {
        return execute(2721);
    }

    /**
     * [科技]expreview
     *
     * @return
     */
    public static List<Map<String, Object>> expreview() {
        return execute(2722);
    }

    /**
     * [社区]geekhub热榜
     *
     * @return
     */
    public static List<Map<String, Object>> geekhub() {
        return execute(2723);
    }

    /**
     * [科技]懂车帝热榜
     *
     * @return
     */
    public static List<Map<String, Object>> dongCheDi() {
        return execute(2724);
    }

    /**
     * [财经]指股网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> zhigu() {
        return execute(2725);
    }

    /**
     * [财经]财新网热榜
     *
     * @return
     */
    public static List<Map<String, Object>> caixin() {
        return execute(2726);
    }

    /**
     * [科技]小众软件
     *
     * @return
     */
    public static List<Map<String, Object>> xiaozhong() {
        return execute(2727);
    }

    /**
     * [科技]daydown热榜
     *
     * @return
     */
    public static List<Map<String, Object>> favicon() {
        return execute(2728);
    }

    /**
     * [综合]全现在热榜
     *
     * @return
     */
    public static List<Map<String, Object>> allNow() {
        return execute(2778);
    }

    /**
     * [购物]鱼塘拾贝
     *
     * @return
     */
    public static List<Map<String, Object>> fishNice() {
        return execute(5000);
    }

    /**
     * [综合]设计癖推荐
     *
     * @return
     */
    public static List<Map<String, Object>> shejipiNew() {
        return execute(151064);
    }

    /**
     * [综合]值得买最新
     *
     * @return
     */
    public static List<Map<String, Object>> smzdmNew() {
        return execute(151079);
    }

    /**
     * [科技]CnBeta推荐
     *
     * @return
     */
    public static List<Map<String, Object>> cnBetaNew() {
        return execute(151102);
    }

    public static List<Map<String, Object>> execute(Integer id) {
        String url = "https://api.tophub.fun/v2/GetAllInfoGzip";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("page", 0);
        params.put("type", "pc");

        List<Map<String, Object>> dataRows = new ArrayList<>();

//        // 取3页
//        for (int i = 0; i < 3; i++) {
//            params.put("page", i);

            String result = HttpRequest.get(url, params).body();
            JsonNode data = JsonUtils.json(result, "Data", "data");
            dataRows.addAll(Arrays.asList(JsonUtils.newObjectArray(data)));
//        }
        return dataRows;
    }

    public static List<Map<String, Object>> allTypes() {
        String url = "https://api.tophub.fun/GetAllType";
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "Data", "全部");
        return Arrays.asList(JsonUtils.newObjectArray(json));
    }

    public static void generateCode() {
        String url = "https://api.tophub.fun/GetAllType";
        String result = HttpRequest.get(url).body();
        JsonNode json = JsonUtils.json(result, "Data", "全部");

        List<Map<String, Object>> lst = new ArrayList<>();

        for (JsonNode item : json) {
            Map<String, Object> map = new HashMap<>();

            String str = JsonUtils.string(item, "icon");
            str = str.substring(str.lastIndexOf("/"));
            if (str.indexOf(".") > -1) {
                str = str.substring(1, str.indexOf("."));
            }
            // 判断首字母是否是数字
            boolean isDigit = Character.isDigit(str.charAt(0));
            boolean isUpperCase = Character.isUpperCase(str.charAt(0));
            if (isDigit) {
                str = String.format("a%s", str);
            } else if (isUpperCase) {
                str = Character.toLowerCase(str.charAt(0)) + str.substring(1);
            }


            map.put("name", str);
            map.put("id", JsonUtils.integer(item, "id"));
            map.put("type", JsonUtils.string(item, "type"));
            map.put("desc", JsonUtils.string(item, "name"));
            lst.add(map);
        }

        Collections.sort(lst, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return ((int) o1.get("id") - (int) o2.get("id"));
            }
        });

        String tmp = "/**\n" +
                "     * [%s]%s\n" +
                "     * @return\n" +
                "     */\n" +
                "    public static List<Map<String,Object>> %s(){\n" +
                "        return execute(%d);\n" +
                "    }";

        for (Map<String, Object> map : lst) {
            System.out.printf(tmp, map.get("type"), map.get("desc"), map.get("name"), map.get("id"));
            System.out.println();
        }
    }

    public static void main(String[] args) {
//        generateCode();
        System.out.println(MapUtils.print(taobao4094()));
    }
}
