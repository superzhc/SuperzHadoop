package com.github.superzhc.data.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.jackson.JsonUtils;
import com.github.superzhc.common.utils.CamelCaseUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/10/9 9:24
 **/
public class BiCiDo {

    /**
     * [热搜]微博｜热搜榜
     * url:<https://weibo.com/>
     */
    public static List<Map<String, String>> weiboTop() {
        return execute(1);
    }

    /**
     * [热搜]知乎｜热搜
     * url:<https://zhihu.com/>
     */
    public static List<Map<String, String>> zhihuWord() {
        return execute(4);
    }

    /**
     * [热搜]豆瓣｜话题
     * url:<https://www.douban.com/>
     */
    public static List<Map<String, String>> doubanTopic() {
        return execute(5);
    }

    /**
     * [热搜]抖音｜热点榜（无链接）
     * url:<https://www.douyin.com/>
     */
    public static List<Map<String, String>> douyinHotSearch() {
        return execute(130);
    }

    /**
     * [热搜]今日头条｜热搜
     * url:<https://www.toutiao.com/>
     */
    public static List<Map<String, String>> touTiaoHotSearch() {
        return execute(104);
    }

    /**
     * [热搜]微博｜话题榜
     * url:<https://weibo.com/>
     */
    public static List<Map<String, String>> weiboTopic() {
        return execute(2);
    }

    /**
     * [热搜]微博｜新时代榜
     * url:<https://weibo.com/>
     */
    public static List<Map<String, String>> weiboSocialEvent() {
        return execute(120);
    }

    /**
     * [热搜]百度贴吧热议
     * url:<https://tieba.baidu.com/index.html>
     */
    public static List<Map<String, String>> baiduTiebaHotTopic() {
        return execute(79);
    }

    /**
     * [热搜]百度｜今日热点
     * url:<http://top.baidu.com/>
     */
    public static List<Map<String, String>> baiduTop() {
        return execute(37);
    }

    /**
     * [热搜]360热搜榜
     * url:<https://so.com/>
     */
    public static List<Map<String, String>> trends360() {
        return execute(18);
    }

    /**
     * [热搜]搜狗热搜榜
     * url:<http://top.sogou.com/home.html>
     */
    public static List<Map<String, String>> sogouHot() {
        return execute(24);
    }

    /**
     * [热搜]哔哔资讯｜公告
     * url:<https://bicido.com>
     */
    public static List<Map<String, String>> bbnewsNotice() {
        return execute(200);
    }

    /**
     * [热搜]神马搜索｜热搜
     * url:<https://m.sm.cn/>
     */
    public static List<Map<String, String>> smSearchHotNews() {
        return execute(189);
    }

    /**
     * [热搜]今日头条｜热搜推荐
     * url:<https://www.toutiao.com/>
     */
    public static List<Map<String, String>> touTiaoHotSearchRcmd() {
        return execute(190);
    }

    /**
     * [快讯]虎嗅｜快讯
     * url:<https://www.huxiu.com/>
     */
    public static List<Map<String, String>> huxiuFlash() {
        return execute(118);
    }

    /**
     * [快讯]36氪｜快讯
     * url:<https://36kr.com/>
     */
    public static List<Map<String, String>> jy36krFlash() {
        return execute(7);
    }

    /**
     * [快讯]界面｜快讯
     * url:<https://www.jiemian.com/>
     */
    public static List<Map<String, String>> jiemianFlash() {
        return execute(6);
    }

    /**
     * [快讯]硅谷动力
     * url:<http://www.enet.com.cn/>
     */
    public static List<Map<String, String>> enetcn() {
        return execute(72);
    }

    /**
     * [快讯]格隆汇｜快讯
     * url:<https://www.gelonghui.com/>
     */
    public static List<Map<String, String>> gelonghuiFlash() {
        return execute(35);
    }

    /**
     * [快讯]Solidot - 科技行者
     * url:<https://www.solidot.org/>
     */
    public static List<Map<String, String>> solidot() {
        return execute(51);
    }

    /**
     * [快讯]金融八卦女｜快讯
     * url:<http://jinrongbaguanv.com/>
     */
    public static List<Map<String, String>> jrbgnFlash() {
        return execute(139);
    }

    /**
     * [快讯]金色财经｜快讯
     * url:<https://www.jinse.com/>
     */
    public static List<Map<String, String>> jinseFlash() {
        return execute(142);
    }

    /**
     * [快讯]亿欧｜快讯
     * url:<https://www.iyiou.com/>
     */
    public static List<Map<String, String>> iYiOuFlash() {
        return execute(157);
    }

    /**
     * [快讯]币世界｜快讯
     * url:<https://www.bishijie.com/>
     */
    public static List<Map<String, String>> biShiJieFlash() {
        return execute(156);
    }

    /**
     * [快讯]老虎证券｜快讯
     * url:<https://www.laohu8.com/>
     */
    public static List<Map<String, String>> tigerFlash() {
        return execute(171);
    }

    /**
     * [快讯]财联社电报
     * url:<https://www.cls.cn/>
     */
    public static List<Map<String, String>> clsTelegraphs() {
        return execute(180);
    }

    /**
     * [快讯]每日经济新闻｜快讯
     * url:<http://www.nbd.com.cn/>
     */
    public static List<Map<String, String>> nbdFlash() {
        return execute(185);
    }

    /**
     * [新闻]澎湃新闻
     * url:<https://www.thepaper.cn/>
     */
    public static List<Map<String, String>> thepaper() {
        return execute(25);
    }

    /**
     * [新闻]财新网
     * url:<http://www.caixin.com/>
     */
    public static List<Map<String, String>> caiXin() {
        return execute(169);
    }

    /**
     * [新闻]界面
     * url:<https://www.jiemian.com/>
     */
    public static List<Map<String, String>> jiemian() {
        return execute(34);
    }

    /**
     * [新闻]好奇心日报
     * url:<http://www.qdaily.com/>
     */
    public static List<Map<String, String>> qdaily() {
        return execute(20);
    }

    /**
     * [新闻]人民日报
     * url:<http://people.com.cn/>
     */
    public static List<Map<String, String>> peopleDaily() {
        return execute(119);
    }

    /**
     * [新闻]网易新闻
     * url:<http://news.163.com/>
     */
    public static List<Map<String, String>> neteaseNews() {
        return execute(19);
    }

    /**
     * [新闻]知乎｜深度
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuDepth() {
        return execute(195);
    }

    /**
     * [新闻]知乎｜焦点
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuFocus() {
        return execute(194);
    }

    /**
     * [新闻]南方周末
     * url:<http://www.infzm.com/>
     */
    public static List<Map<String, String>> infzm() {
        return execute(88);
    }

    /**
     * [新闻]每日经济新闻｜日报
     * url:<http://www.nbd.com.cn/>
     */
    public static List<Map<String, String>> nbdDaily() {
        return execute(183);
    }

    /**
     * [新闻]观察者
     * url:<https://www.guancha.cn/>
     */
    public static List<Map<String, String>> guancha() {
        return execute(31);
    }

    /**
     * [新闻]新华网
     * url:<http://m.xinhuanet.com/>
     */
    public static List<Map<String, String>> xinHuaNet() {
        return execute(196);
    }

    /**
     * [新闻]中国政府网
     * url:<http://www.gov.cn/>
     */
    public static List<Map<String, String>> govYaowen() {
        return execute(64);
    }

    /**
     * [新闻]人民网
     * url:<http://people.com.cn/>
     */
    public static List<Map<String, String>> peopleNews() {
        return execute(93);
    }

    /**
     * [新闻]环球网
     * url:<https://www.huanqiu.com/>
     */
    public static List<Map<String, String>> huanqiu() {
        return execute(138);
    }

    /**
     * [新闻]参考消息｜要闻
     * url:<http://www.cankaoxiaoxi.com/>
     */
    public static List<Map<String, String>> ckxxIndex() {
        return execute(137);
    }

    /**
     * [新闻]亿欧
     * url:<https://www.iyiou.com/>
     */
    public static List<Map<String, String>> iYiOuHot() {
        return execute(161);
    }

    /**
     * [新闻]网易｜数读
     * url:<http://data.163.com/>
     */
    public static List<Map<String, String>> neteaseDataReport() {
        return execute(108);
    }

    /**
     * [新闻]澎湃新闻｜美数课
     * url:<https://www.thepaper.cn/>
     */
    public static List<Map<String, String>> thepaperDataReport() {
        return execute(109);
    }

    /**
     * [新闻]新京报网｜数据新闻
     * url:<http://www.bjnews.com.cn/>
     */
    public static List<Map<String, String>> bjNewsDataReport() {
        return execute(110);
    }

    /**
     * [新闻]百度｜历史上的今天
     * url:<https://baike.baidu.com/>
     */
    public static List<Map<String, String>> baiduLssdjt() {
        return execute(121);
    }

    /**
     * [新闻]历史上的今天
     * url:<http://www.lssdjt.com/>
     */
    public static List<Map<String, String>> lssdjt() {
        return execute(78);
    }

    /**
     * [疫情]疫情地图
     * url:<https://bbnews.app>
     */
    public static List<Map<String, String>> pneumonia() {
        return execute(177);
    }

    /**
     * [疫情]疫情辟谣
     * url:<https://bbnews.app>
     */
    public static List<Map<String, String>> pneumoniaFact() {
        return execute(179);
    }

    /**
     * [疫情]疫情相关
     * url:<https://bbnews.app>
     */
    public static List<Map<String, String>> pneumoniaOther() {
        return execute(178);
    }

    /**
     * [热文]知乎｜热榜
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuTotal() {
        return execute(3);
    }

    /**
     * [热文]知乎｜每日精选
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuDailySelect() {
        return execute(193);
    }

    /**
     * [热文]知乎｜日报
     * url:<https://daily.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuDaily() {
        return execute(70);
    }

    /**
     * [热文]微信｜订阅号（测试版）
     * url:<https://bbnews.app>
     */
    public static List<Map<String, String>> jyWxArticle() {
        return execute(197);
    }

    /**
     * [热文]虎扑｜社区热门
     * url:<https://bbs.hupu.com/>
     */
    public static List<Map<String, String>> hupuHot() {
        return execute(122);
    }

    /**
     * [热文]抽屉新热榜
     * url:<https://dig.chouti.com/>
     */
    public static List<Map<String, String>> choutiHot() {
        return execute(9);
    }

    /**
     * [热文]哔哩哔哩｜专栏推荐
     * url:<https://www.bilibili.com/>
     */
    public static List<Map<String, String>> bilibiliReadRecommends() {
        return execute(75);
    }

    /**
     * [热文]金融八卦女
     * url:<http://jinrongbaguanv.com/>
     */
    public static List<Map<String, String>> jrbgnIndex() {
        return execute(77);
    }

    /**
     * [热文]喷嚏网
     * url:<http://www.dapenti.com/>
     */
    public static List<Map<String, String>> dapentiAll() {
        return execute(127);
    }

    /**
     * [热文]MONO猫弄
     * url:<http://mmmono.com/>
     */
    public static List<Map<String, String>> mono() {
        return execute(112);
    }

    /**
     * [热文]招行｜热榜
     * url:<http://www.cmbchina.com/>
     */
    public static List<Map<String, String>> cmbchinaHotArticle() {
        return execute(111);
    }

    /**
     * [热文]喷嚏网｜图卦
     * url:<http://www.dapenti.com/>
     */
    public static List<Map<String, String>> dapentiXilei() {
        return execute(126);
    }

    /**
     * [热文]湾区日报
     * url:<https://wanqu.co/>
     */
    public static List<Map<String, String>> wanqu() {
        return execute(69);
    }

    /**
     * [热文]简书
     * url:<https://www.jianshu.com/>
     */
    public static List<Map<String, String>> jianshu() {
        return execute(15);
    }

    /**
     * [热文]中国新闻周刊
     * url:<http://www.inewsweek.cn/>
     */
    public static List<Map<String, String>> iNewsWeek() {
        return execute(186);
    }

    /**
     * [科技]Readhub
     * url:<https://readhub.cn/>
     */
    public static List<Map<String, String>> readhub() {
        return execute(21);
    }

    /**
     * [科技]虎嗅
     * url:<https://www.huxiu.com/>
     */
    public static List<Map<String, String>> huxiu() {
        return execute(13);
    }

    /**
     * [科技]36氪
     * url:<https://36kr.com/>
     */
    public static List<Map<String, String>> jy36kr() {
        return execute(17);
    }

    /**
     * [科技]IT之家
     * url:<https://www.ithome.com/>
     */
    public static List<Map<String, String>> ithome() {
        return execute(90);
    }

    /**
     * [科技]cnBeta
     * url:<https://www.cnbeta.com/>
     */
    public static List<Map<String, String>> cnbeta() {
        return execute(55);
    }

    /**
     * [科技]少数派
     * url:<https://sspai.com/>
     */
    public static List<Map<String, String>> sspai() {
        return execute(45);
    }

    /**
     * [科技]爱范儿
     * url:<https://www.ifanr.com/>
     */
    public static List<Map<String, String>> ifanr() {
        return execute(41);
    }

    /**
     * [科技]知乎｜数码
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuDigital() {
        return execute(150);
    }

    /**
     * [科技]知乎｜科学
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuScience() {
        return execute(154);
    }

    /**
     * [科技]极客公园
     * url:<http://www.geekpark.net/>
     */
    public static List<Map<String, String>> geekpark() {
        return execute(40);
    }

    /**
     * [科技]什么值得买
     * url:<https://www.smzdm.com/>
     */
    public static List<Map<String, String>> smzdm() {
        return execute(44);
    }

    /**
     * [科技]钛媒体
     * url:<https://www.tmtpost.com/>
     */
    public static List<Map<String, String>> tmtpost() {
        return execute(46);
    }

    /**
     * [科技]数字尾巴
     * url:<https://www.dgtle.com/>
     */
    public static List<Map<String, String>> dgtle() {
        return execute(49);
    }

    /**
     * [科技]煎蛋
     * url:<http://jandan.net/>
     */
    public static List<Map<String, String>> jiandan() {
        return execute(80);
    }

    /**
     * [科技]AppSo
     * url:<https://www.ifanr.com/app>
     */
    public static List<Map<String, String>> appSolution() {
        return execute(36);
    }

    /**
     * [科技]威锋网
     * url:<https://www.feng.com/>
     */
    public static List<Map<String, String>> feng() {
        return execute(107);
    }

    /**
     * [科技]数英网
     * url:<https://www.digitaling.com/>
     */
    public static List<Map<String, String>> digitaling() {
        return execute(159);
    }

    /**
     * [科技]品玩
     * url:<https://www.pingwest.com/>
     */
    public static List<Map<String, String>> pingWest() {
        return execute(123);
    }

    /**
     * [科技]中关村在线
     * url:<http://www.zol.com.cn/>
     */
    public static List<Map<String, String>> zol() {
        return execute(91);
    }

    /**
     * [科技]小众软件
     * url:<https://www.appinn.com/>
     */
    public static List<Map<String, String>> appinn() {
        return execute(100);
    }

    /**
     * [科技]异次元软件世界
     * url:<https://www.iplaysoft.com/>
     */
    public static List<Map<String, String>> iplaysoft() {
        return execute(101);
    }

    /**
     * [科技]反斗软件
     * url:<http://www.apprcn.com/>
     */
    public static List<Map<String, String>> apprcn() {
        return execute(102);
    }

    /**
     * [科技]量子位
     * url:<https://www.qbitai.com/>
     */
    public static List<Map<String, String>> qbitai() {
        return execute(124);
    }

    /**
     * [科技]快科技
     * url:<https://www.mydrivers.com/>
     */
    public static List<Map<String, String>> myDrivers() {
        return execute(201);
    }

    /**
     * [科技]雷锋网
     * url:<https://www.leiphone.com/>
     */
    public static List<Map<String, String>> leiPhone() {
        return execute(205);
    }

    /**
     * [游戏]虎扑电竞
     * url:<https://bbs.hupu.com/all-gg>
     */
    public static List<Map<String, String>> hupuGg() {
        return execute(32);
    }

    /**
     * [游戏]小黑盒
     * url:<https://www.xiaoheihe.cn/>
     */
    public static List<Map<String, String>> xiaoheihe() {
        return execute(33);
    }

    /**
     * [游戏]触乐网
     * url:<http://www.chuapp.com/>
     */
    public static List<Map<String, String>> chuapp() {
        return execute(62);
    }

    /**
     * [游戏]游民星空
     * url:<https://www.gamersky.com/>
     */
    public static List<Map<String, String>> gamersky() {
        return execute(56);
    }

    /**
     * [游戏]3DMGame
     * url:<https://www.3dmgame.com/>
     */
    public static List<Map<String, String>> jy3dmGame() {
        return execute(58);
    }

    /**
     * [游戏]游研社
     * url:<https://www.yystv.cn/>
     */
    public static List<Map<String, String>> yystv() {
        return execute(60);
    }

    /**
     * [游戏]机核网
     * url:<https://www.gcores.com/>
     */
    public static List<Map<String, String>> gcores() {
        return execute(61);
    }

    /**
     * [游戏]篝火营地
     * url:<https://gouhuo.qq.com/>
     */
    public static List<Map<String, String>> gouhuo() {
        return execute(99);
    }

    /**
     * [游戏]第二手柄
     * url:<http://www.diershoubing.com/>
     */
    public static List<Map<String, String>> diershoubing() {
        return execute(98);
    }

    /**
     * [游戏]VGTime
     * url:<http://www.vgtime.com/>
     */
    public static List<Map<String, String>> vgtime() {
        return execute(96);
    }

    /**
     * [游戏]头榜｜主播资讯
     * url:<http://www.toubang.tv/>
     */
    public static List<Map<String, String>> touBangNews() {
        return execute(204);
    }

    /**
     * [BBS]虎扑步行街
     * url:<https://bbs.hupu.com/all-gambia>
     */
    public static List<Map<String, String>> hupuTop() {
        return execute(12);
    }

    /**
     * [BBS]豆瓣｜小组讨论
     * url:<https://www.douban.com/>
     */
    public static List<Map<String, String>> doubanGroupExplore() {
        return execute(128);
    }

    /**
     * [BBS]知乎｜校园
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuSchool() {
        return execute(153);
    }

    /**
     * [BBS]天涯
     * url:<https://bbs.tianya.cn/>
     */
    public static List<Map<String, String>> tianya() {
        return execute(26);
    }

    /**
     * [BBS]风闻社区
     * url:<https://user.guancha.cn/>
     */
    public static List<Map<String, String>> fengwen() {
        return execute(50);
    }

    /**
     * [BBS]牛客网
     * url:<https://www.nowcoder.com/>
     */
    public static List<Map<String, String>> nowCoder() {
        return execute(92);
    }

    /**
     * [BBS]水木社区
     * url:<http://www.newsmth.net/>
     */
    public static List<Map<String, String>> newsmth() {
        return execute(160);
    }

    /**
     * [BBS]飞客茶馆｜热门
     * url:<http://www.flyertea.com/>
     */
    public static List<Map<String, String>> flyerTeaHot() {
        return execute(158);
    }

    /**
     * [BBS]飞客茶馆｜精华
     * url:<http://www.flyertea.com/>
     */
    public static List<Map<String, String>> flyerTeaEssence() {
        return execute(166);
    }

    /**
     * [BBS]凯迪网络
     * url:<https://www.kdnet.net/>
     */
    public static List<Map<String, String>> kdNetHotShare() {
        return execute(191);
    }

    /**
     * [BBS]知乎｜热门想法
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuHotPin() {
        return execute(192);
    }

    /**
     * [BBS]电鸭社区
     * url:<https://eleduck.com/>
     */
    public static List<Map<String, String>> eleDuck() {
        return execute(203);
    }

    /**
     * [财经]雪球
     * url:<https://xueqiu.com/>
     */
    public static List<Map<String, String>> xueqiu() {
        return execute(28);
    }

    /**
     * [财经]币乎
     * url:<https://bihu.com/>
     */
    public static List<Map<String, String>> bihu() {
        return execute(8);
    }

    /**
     * [财经]东方财富
     * url:<http://eastmoney.com/>
     */
    public static List<Map<String, String>> eastMoney() {
        return execute(11);
    }

    /**
     * [财经]新浪财经
     * url:<https://finance.sina.com.cn/>
     */
    public static List<Map<String, String>> sinaFinance() {
        return execute(23);
    }

    /**
     * [财经]第一财经
     * url:<https://www.yicai.com/>
     */
    public static List<Map<String, String>> yicai() {
        return execute(29);
    }

    /**
     * [财经]格隆汇
     * url:<https://www.gelonghui.com/>
     */
    public static List<Map<String, String>> gelonghui() {
        return execute(16);
    }

    /**
     * [财经]金色财经｜热门
     * url:<https://www.jinse.com/>
     */
    public static List<Map<String, String>> jinseHot() {
        return execute(144);
    }

    /**
     * [财经]金色财经｜专栏
     * url:<https://www.jinse.com/>
     */
    public static List<Map<String, String>> jinseColumns() {
        return execute(143);
    }

    /**
     * [财经]老虎证券｜资讯
     * url:<https://www.laohu8.com/>
     */
    public static List<Map<String, String>> tigerNews() {
        return execute(174);
    }

    /**
     * [财经]老虎证券｜精选
     * url:<https://www.laohu8.com/>
     */
    public static List<Map<String, String>> tigerBest() {
        return execute(173);
    }

    /**
     * [财经]每日经济新闻｜要闻
     * url:<http://www.nbd.com.cn/>
     */
    public static List<Map<String, String>> nbdImportant() {
        return execute(184);
    }

    /**
     * [体育]虎扑体育
     * url:<https://www.hupu.com/>
     */
    public static List<Map<String, String>> hupuSport() {
        return execute(65);
    }

    /**
     * [体育]知乎｜体育
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuSport() {
        return execute(155);
    }

    /**
     * [体育]网易体育
     * url:<http://news.163.com/>
     */
    public static List<Map<String, String>> neteaseSports() {
        return execute(52);
    }

    /**
     * [体育]腾讯体育
     * url:<https://sports.qq.com/>
     */
    public static List<Map<String, String>> qqSports() {
        return execute(54);
    }

    /**
     * [体育]新浪体育
     * url:<http://sports.sina.com.cn/>
     */
    public static List<Map<String, String>> sinaSports() {
        return execute(67);
    }

    /**
     * [体育]人民网体育
     * url:<http://sports.people.com.cn/>
     */
    public static List<Map<String, String>> peopleSports() {
        return execute(66);
    }

    /**
     * [体育]懂球帝
     * url:<https://www.dongqiudi.com/>
     */
    public static List<Map<String, String>> dongqiudi() {
        return execute(53);
    }

    /**
     * [体育]中羽在线
     * url:<https://www.badmintoncn.com/>
     */
    public static List<Map<String, String>> badmintoncn() {
        return execute(89);
    }

    /**
     * [产品]PMCAFF
     * url:<https://coffee.pmcaff.com/>
     */
    public static List<Map<String, String>> pmcaff() {
        return execute(43);
    }

    /**
     * [产品]增长黑客
     * url:<https://www.growthhk.cn/>
     */
    public static List<Map<String, String>> growthhk() {
        return execute(63);
    }

    /**
     * [产品]人人都是产品经理
     * url:<http://www.woshipm.com/>
     */
    public static List<Map<String, String>> woshipm() {
        return execute(47);
    }

    /**
     * [产品]鸟哥笔记
     * url:<https://www.niaogebiji.com/>
     */
    public static List<Map<String, String>> niaogebiji() {
        return execute(71);
    }

    /**
     * [产品]产品壹佰
     * url:<http://www.chanpin100.com/>
     */
    public static List<Map<String, String>> chanpin100() {
        return execute(38);
    }

    /**
     * [编程]InfoQ
     * url:<https://www.infoq.cn/>
     */
    public static List<Map<String, String>> infoq() {
        return execute(14);
    }

    /**
     * [编程]SegmentFault
     * url:<https://segmentfault.com/>
     */
    public static List<Map<String, String>> segmentfault() {
        return execute(22);
    }

    /**
     * [编程]博客园
     * url:<https://news.cnblogs.com/>
     */
    public static List<Map<String, String>> cnblogs() {
        return execute(39);
    }

    /**
     * [编程]掘金
     * url:<https://juejin.im/>
     */
    public static List<Map<String, String>> juejin() {
        return execute(42);
    }

    /**
     * [编程]极客时间
     * url:<https://time.geekbang.org/>
     */
    public static List<Map<String, String>> geekTime() {
        return execute(68);
    }

    /**
     * [编程]开发者头条
     * url:<https://toutiao.io/>
     */
    public static List<Map<String, String>> toutiaoio() {
        return execute(59);
    }

    /**
     * [编程]开源中国
     * url:<https://www.oschina.net/>
     */
    public static List<Map<String, String>> oschina() {
        return execute(73);
    }

    /**
     * [编程]CSDN｜资讯
     * url:<https://www.csdn.net/>
     */
    public static List<Map<String, String>> csdnNews() {
        return execute(10);
    }

    /**
     * [编程]CSDN｜博客
     * url:<https://www.csdn.net/>
     */
    public static List<Map<String, String>> csdnBlog() {
        return execute(187);
    }

    /**
     * [编程]GitHub
     * url:<https://github.com/>
     */
    public static List<Map<String, String>> github() {
        return execute(57);
    }

    /**
     * [编程]阮一峰的网络日志
     * url:<http://www.ruanyifeng.com/>
     */
    public static List<Map<String, String>> ruanyifeng() {
        return execute(85);
    }

    /**
     * [编程]王垠的博客 - 当然我在扯淡
     * url:<http://www.yinwang.org/>
     */
    public static List<Map<String, String>> yinwang() {
        return execute(84);
    }

    /**
     * [编程]知道创宇｜洞悉漏洞
     * url:<https://www.seebug.org/>
     */
    public static List<Map<String, String>> seebug() {
        return execute(114);
    }

    /**
     * [编程]看雪安全论坛
     * url:<https://bbs.pediy.com/>
     */
    public static List<Map<String, String>> pediy() {
        return execute(165);
    }

    /**
     * [编程]SecNews安全文摘
     * url:<https://wiki.ioin.in/>
     */
    public static List<Map<String, String>> secNews() {
        return execute(117);
    }

    /**
     * [编程]先知社区
     * url:<https://xz.aliyun.com/>
     */
    public static List<Map<String, String>> xianzhi() {
        return execute(115);
    }

    /**
     * [编程]Linux中国
     * url:<https://linux.cn/>
     */
    public static List<Map<String, String>> linuxCn() {
        return execute(95);
    }

    /**
     * [编程]Draveness 博客 - 面向信仰编程
     * url:<https://draven.co/>
     */
    public static List<Map<String, String>> dravenBlog() {
        return execute(148);
    }

    /**
     * [编程]蟒周刊
     * url:<http://weekly.pychina.org/>
     */
    public static List<Map<String, String>> pychinaWeekly() {
        return execute(86);
    }

    /**
     * [编程]FreeBuf
     * url:<https://www.freebuf.com/>
     */
    public static List<Map<String, String>> freebuf() {
        return execute(147);
    }

    /**
     * [编程]月光博客
     * url:<https://www.williamlong.info/>
     */
    public static List<Map<String, String>> williamLong() {
        return execute(199);
    }

    /**
     * [编程]GeeksForGeeks
     * url:<https://www.geeksforgeeks.org/>
     */
    public static List<Map<String, String>> geeksForGeeks() {
        return execute(198);
    }

    /**
     * [视频]哔哩哔哩｜热门视频
     * url:<https://www.bilibili.com/>
     */
    public static List<Map<String, String>> bilibiliHotVideo() {
        return execute(135);
    }

    /**
     * [视频]AcFun｜日榜
     * url:<https://www.acfun.cn/>
     */
    public static List<Map<String, String>> acfunTopDay() {
        return execute(136);
    }

    /**
     * [视频]开眼
     * url:<https://www.kaiyanapp.com/>
     */
    public static List<Map<String, String>> kaiyan() {
        return execute(134);
    }

    /**
     * [视频]梨视频
     * url:<https://www.pearvideo.com/>
     */
    public static List<Map<String, String>> pearVideo() {
        return execute(133);
    }

    /**
     * [视频]抖音｜视频榜
     * url:<https://www.douyin.com/>
     */
    public static List<Map<String, String>> douyinHotVideo() {
        return execute(132);
    }

    /**
     * [视频]抖音｜正能量
     * url:<https://www.douyin.com/>
     */
    public static List<Map<String, String>> douyinHotVideoPositive() {
        return execute(131);
    }

    /**
     * [视频]抖音｜音乐榜
     * url:<https://www.douyin.com/>
     */
    public static List<Map<String, String>> douyinHotMusic() {
        return execute(129);
    }

    /**
     * [视频]MTime时光网｜资讯
     * url:<http://www.mtime.com/>
     */
    public static List<Map<String, String>> mtimeNews() {
        return execute(146);
    }

    /**
     * [视频]哔哩哔哩｜每周必看
     * url:<https://www.bilibili.com/>
     */
    public static List<Map<String, String>> bilibiliVideoWeeklyRcmd() {
        return execute(206);
    }

    /**
     * [影视]知乎｜影视
     * url:<https://www.zhihu.com/>
     */
    public static List<Map<String, String>> zhihuFilm() {
        return execute(152);
    }

    /**
     * [影视]微博｜电影榜
     * url:<https://weibo.com/>
     */
    public static List<Map<String, String>> weiboMovie() {
        return execute(176);
    }

    /**
     * [影视]虎扑｜影视娱乐
     * url:<https://bbs.hupu.com/ent>
     */
    public static List<Map<String, String>> hupuEnt() {
        return execute(170);
    }

    /**
     * [知识]科学松鼠会
     * url:<https://songshuhui.net/>
     */
    public static List<Map<String, String>> songShuHui() {
        return execute(168);
    }

    /**
     * [知识]科学网
     * url:<http://news.sciencenet.cn/>
     */
    public static List<Map<String, String>> sciencent() {
        return execute(145);
    }

    /**
     * [知识]参考消息｜读书
     * url:<http://www.cankaoxiaoxi.com/>
     */
    public static List<Map<String, String>> ckxxBook() {
        return execute(141);
    }

    /**
     * [知识]参考消息｜文化
     * url:<http://www.cankaoxiaoxi.com/>
     */
    public static List<Map<String, String>> ckxxCulture() {
        return execute(140);
    }

    /**
     * [知识]印象识堂
     * url:<https://www.yinxiang.com/>
     */
    public static List<Map<String, String>> yinXiangPublic() {
        return execute(188);
    }

    /**
     * [设计]设计癖
     * url:<http://www.shejipi.com/>
     */
    public static List<Map<String, String>> sheJiPi() {
        return execute(164);
    }

    /**
     * [设计]优设网
     * url:<https://www.uisdc.com/>
     */
    public static List<Map<String, String>> uisdc() {
        return execute(163);
    }

    /**
     * [设计]站酷
     * url:<https://www.zcool.com.cn/>
     */
    public static List<Map<String, String>> zcool() {
        return execute(162);
    }

    /**
     * [设计]SocialBeta
     * url:<https://socialbeta.com/>
     */
    public static List<Map<String, String>> socialBeta() {
        return execute(202);
    }

    private static List<Map<String, String>> execute(Integer typeId) {
        String url = "https://news.bicido.com/api/news/";

        Map<String, Object> params = new HashMap<>();
        params.put("type_id", typeId);

        String result = HttpRequest.get(url, params)
                .accept("application/json, text/plain, */*")
                .referer("https://news.bicido.com/")
                .body();
        JsonNode json = JsonUtils.json(result);
        List<Map<String, String>> data = Arrays.asList(JsonUtils.objectArray2Map(json));
        return data;
    }

    public static void generateCode() {
        String url = "https://news.bicido.com/api/config/news_group/";

        String result = HttpRequest.get(url)
                .referer("https://news.bicido.com/")
                .accept("application/json, text/plain, */*")
                .body();

        JsonNode json = JsonUtils.json(result);

        String tmp = "/**\n" +
                "     * [%s]%s\n" +
                "     * url:<%s>\n" +
                "     */\n" +
                "    public static List<Map<String,String>> %s(){\n" +
                "        return execute(%d);\n" +
                "    }";

        for (JsonNode item : json) {
            String category = JsonUtils.string(item, "name");

            JsonNode news = item.get("news_types");
            for (JsonNode n : news) {
                Integer typeId = JsonUtils.integer(n, "id");
                String desc = JsonUtils.string(n, "name");
                String codeName = JsonUtils.string(n, "code");
                String originUrl = JsonUtils.string(n, "origin_url");

                String str = String.format(tmp, category, desc, originUrl, CamelCaseUtils.underscore2camelCase(codeName), typeId);
                System.out.println(str);
            }
        }

        /**
         * {
         *   "id": 1,
         *   "name": "微博｜热搜榜",
         *   "code": "weibo_top",
         *   "icon_url": "https://weibo.com/favicon.ico",
         *   "origin_url": "https://weibo.com/",
         *   "order": 0
         * }
         */
    }

    public static void main(String[] args) {
        // System.out.println(execute(1));
        generateCode();
    }
}
