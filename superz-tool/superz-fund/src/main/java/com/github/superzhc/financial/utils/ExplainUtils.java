package com.github.superzhc.financial.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author superz
 * @create 2022/4/14 15:50
 **/
public class ExplainUtils {
    private static Map<String, String> explainAll = new HashMap<>();

    static {
        //=====================指标意义说明==================================
        //偿债能力
        explainAll.put("偿债能力", "变现能力是企业产生现金的能力，它取决于可以在近期转变为现金的流动资产的多少。");
        explainAll.put("偿债能力.流动比率（比例）", "体现企业的偿还短期债务的能力。流动资产越多，短期债务越少，则流动比率越大，企业的短期偿债能力越强。");
        explainAll.put("偿债能力.速动比率（比例）", "比流动比率更能体现企业的偿还短期债务的能力。因为流动资产中，尚包括变现速度较慢且可能已贬值的存货，因此将流动资产扣除存货再与流动负债对比，以衡量企业的短期偿债能力。");
        explainAll.put("偿债能力.现金到期债务", "它反映了企业可用现金流量偿付到期债务的能力。该比率越高，企业资金流动性越好，企业到期偿还债务的能力就越强。");
        explainAll.put("偿债能力.现金流量利息保障倍数（比例）", "它比收益基础的利息保障倍数更可靠，因为实际用以支付利息的是现金，而非收益。");
        //营运能力
        explainAll.put("营运能力", "");//企业营运能力分析就是要通过对反映企业资产营运效率与效益的指标进行计算与分析，评价企业的营运能力，为企业提高经济效益指明方向。第一，营运能力分析可评价企业资产营运的效率。第二，营运能力分析可发现企业在资产营运中存在的问题。第三，营运能力分析是盈利能力分析和偿债能力分析的基础与补充。
        explainAll.put("营运能力.存货周转率（次）", "存货的周转率是存货周转速度的主要指标。提高存货周转率，缩短营业周期，可以提高企业的变现能力。");
        explainAll.put("营运能力.存货周转天数", "企业购入存货、投入生产到销售出去所需要的天数。提高存货周转率，缩短营业周期，可以提高企业的变现能力。");
        explainAll.put("营运能力.应收账款周转率（次）", "应收账款周转率越高，说明其收回越快。反之，说明营运资金过多呆滞在应收账款上，影响正常资金周转及偿债能力。");
        explainAll.put("营运能力.应收账款周转天数", "应收账款周转率越高，说明其收回越快。反之，说明营运资金过多呆滞在应收账款上，影响正常资金周转及偿债能力。");
        explainAll.put("营运能力.营业周期", "营业周期是从取得存货开始到销售存货并收回现金为止的时间。一般情况下，营业周期短，说明资金周转速度快；营业周期长，说明资金周转速度慢。");
        explainAll.put("营运能力.流动资产周转率（次）", "流动资产周转率反映流动资产的周转速度，周转速度越快，会相对节约流动资产，相当于扩大资产的投入，增强企业的盈利能力；而延缓周转速度，需补充流动资产参加周转，形成资产的浪费，降低企业的盈利能力。");
        explainAll.put("营运能力.总资产周转率（次）", "该项指标反映总资产的周转速度，周转越快，说明销售能力越强。企业可以采用薄利多销的方法，加速资产周转，带来利润绝对额的增加。");
        explainAll.put("营运能力.固定资产周转率", "固定资产周转天数表示在一个会计年度内，固定资产转换成现金平均需要的时间，即平均天数。固定资产的周转次数越多，则周转天数越短；周转次数越少，则周转天数越长。");
        explainAll.put("营运能力.应付账款周转率（次）", "应付账款周转率反映本企业免费使用供货企业资金的能力。合理的应付账款周转率来自于与同行业对比和公司历史正常水平。如公司应付账款周转率低于行业平均水平，说明公司较同行可以更多占用供应商的货款，显示其重要的市场地位，但同时也要承担较多的还款压力，反之亦然);如果公司应付账款周转率较以前出现快速提高，说明公司占用供应商货款降低，可能反映上游供应商谈判实力增强，要求快速回款的情况，也有可能预示原材料供应紧俏甚至吃紧，反之亦然。");
        explainAll.put("营运能力.应付账款周转天数", "应付账款周转天数又称平均付现期，是衡量公司需要多长时间付清供应商的欠款，属于公司经营能力分析范畴。");
        explainAll.put("营运能力.净营业周期", "营业周期的长短是决定公司流动资产需要量的重要因素。较短的营业周期表明对应收账款和存货的有效管理。");
        explainAll.put("营运能力.非流动资产周转率", "投资预算和项目管理主要涉及的是非流动资产，企业的投资一般也是增加长期资产，收购会增加非流动资产，剥离会减少非流动资产，所以通过非流动资产周转率的大小比较可以分析投资与其竞争战略是否一致,收购和剥离是否合理。");
        //负债比率
        explainAll.put("负债比率", "负债比率是反映债务和资产、净资产关系的比率。它反映企业偿付到期长期债务的能力。");
        explainAll.put("负债比率.资产负债比率（%）", "资产负债率反映在总资产中有多大比例是通过借债来筹资的，也可以衡量企业在清算时保护债权人利益的程度。该指标也被称为举债经营比率。");
        explainAll.put("负债比率.产权比率（%）", "反映债权人与股东提供的资本的相对比例。反映企业的资本结构是否合理、稳定。同时也表明债权人投入资本受到股东权益的保障程度。");
        explainAll.put("负债比率.有形净值债务率（%）", "企业负债总额与有形净值的百分比。有形净值是股东权益减去无形资产净值，即股东具有所有权的有形资产的净值。");
        explainAll.put("负债比率.已获利息倍数（比例）", "企业经营业务收益与利息费用的比率，用以衡量企业偿付借款利息的能力，也叫利息保障倍数。只要已获利息倍数足够大，企业就有充足的能力偿付利息。");
        //每股指标
        explainAll.put("每股指标", "");
        explainAll.put("每股指标.每股净资产", "反映每股股票所拥有的资产现值。每股净资产越高，股东拥有的每股资产价值越多；每股净资产越少，股东拥有的每股资产价值越少。通常每股净资产越高越好。");
        explainAll.put("每股指标.每股经营活动产生的现金流量净额", "反映企业支付股利和资本支出的能力。一般而言，该比率越大，证明企业支付股利和资本支出的能力越强。");
        explainAll.put("每股指标.每股营业总收入", "");
        explainAll.put("每股指标.每股息税前利润", "");
        explainAll.put("每股指标.每股资本公积", "");
        explainAll.put("每股指标.每股盈余公积", "");
        explainAll.put("每股指标.每股未分配利润", "");
        explainAll.put("每股指标.每股留存收益", "");
        explainAll.put("每股指标.每股现金流量净额", "");
        //盈利能力
        explainAll.put("盈利能力", "盈利能力就是企业赚取利润的能力。不论是投资人还是债务人，都非常关心这个项目。在分析盈利能力时，应当排除证券买卖等非正常项目、已经或将要停止的营业项目、重大事故或法律更改等特别项目、会计政策和财务制度变更带来的累积影响数等因素。");
        explainAll.put("盈利能力.销售净利率（%）", "该指标反映每一元销售收入带来的净利润是多少。表示销售收入的收益水平。");
        explainAll.put("盈利能力.销售毛利率（%）", "表示每一元销售收入扣除销售成本后，有多少钱可以用于各项期间费用和形成盈利。销售毛利率是企业销售净利率的最初基础，没有足够大的毛利率便不能盈利。 ");
        explainAll.put("盈利能力.资产净利率（总资产报酬率）（%）", "指企业一定时期内获得的报酬总额与资产平均总额的比率。它表示企业包括净资产和负债在内的全部资产的总体获利能力，用以评价企业运用全部资产的总体获利能力，是评价企业资产运营效益的重要指标。");
        explainAll.put("盈利能力.净资产收益率（权益报酬率）（%）", "净资产收益率反映公司所有者权益的投资报酬率，也叫净值报酬率或权益报酬率，具有很强的综合性。是最重要的财务比率。");
        explainAll.put("盈利能力.净资产收益率（平均）（%）", "净资产收益率又称股东权益收益率，是净利润与平均股东权益的百分比，是公司税后利润除以净资产得到的百分比率，该指标反映股东权益的收益水平，用以衡量公司运用自有资本的效率。");
        explainAll.put("盈利能力.主营业务毛利率", "主营业务毛利率指标反映了主营业务的获利能力。");
        explainAll.put("盈利能力.主营业务成本率", "衡量的是主营业务收入中主营业务成本占比例多少，该比例高则主营业务收入的盈利贡献就低。");
        //explainAll.put("盈利能力.主营业务毛利率", "");
        explainAll.put("盈利能力.主营业务净利率", "反映企业基本获利能力。");
        //explainAll.put("盈利能力.主营业务成本率", "该比例高则主营业务收入的盈利贡献就低。");
        explainAll.put("盈利能力.营业费用率", "可以与企业以前期间的该比率进行比较，找出企业成本控制的变化情况，可以进一步了解企业的经营策略。");
        explainAll.put("盈利能力.管理费用率", "是企业管理术语，是管理费用和主营业务收入之比。");
        explainAll.put("盈利能力.财务费用率", "企业应通过这个指标的计算，分析企业的财务负担，调整筹资渠道，改善资金结构，提高盈利水平。");
        explainAll.put("盈利能力.成本、费用利润率", "成本费用利润率指标表明每付出一元成本费用可获得多少利润，体现了经营耗费所带来的经营成果。");
        explainAll.put("盈利能力.主营业务税金率", "");
        explainAll.put("盈利能力.资产净利率", "表示企业全部资产获取收益的水平，全面反映了企业的获利能力和投入产出状况。通过对该指标的深入分析，可以增强各方面对企业资产经营的关注，促进企业提高单位资产的收益水平。一般情况下，企业可据此指标与市场资本利率进行比较，如果该指标大于市场利率，则表明企业可以充分利用财务杠杆，进行负债经营，获取尽可能多的收益。");
        explainAll.put("盈利能力.流动比率（比例）", "体现企业的偿还短期债务的能力。流动资产越多，短期债务越少，则流动比率越大，企业的短期偿债能力越强。");
        explainAll.put("盈利能力.速动比率（比例）", "比流动比率更能体现企业的偿还短期债务的能力。因为流动资产中，尚包括变现速度较慢且可能已贬值的存货，因此将流动资产扣除存货再与流动负债对比，以衡量企业的短期偿债能力。");
        explainAll.put("盈利能力.资产负债比率（%）", "反映债权人提供的资本占全部资本的比例。该指标也被称为举债经营比率。");
        explainAll.put("盈利能力.长期资产适合率（%）", "长期资产适合率是企业所有者权益和长期负债之和与固定资产与长期投资之和的比率。该比率从企业资源配置结构方面反映了企业的偿债能力。");
        explainAll.put("盈利能力.资本积累率（%）", "指企业本年所有者权益增长额同年初所有者权益的比率。该指标反映企业所有者权益在当年的变动水平，体现了企业资本的积累情况，是企业发展强盛的标志，也是企业扩大再生产的源泉，展示了企业的发展潜力，是评价企业发展潜力的重要指标。");
        explainAll.put("盈利能力.销售收现比", "指在一定时期内(一般为一年，也可为几年)企业所实现的销售收人中实际收到现金的比例。");
        explainAll.put("盈利能力.现金流动负债比（%）", "一般该指标大于1，表示企业流动负债的偿还有可靠保证。该指标越大，表明企业经营活动产生的现金净流量越多，越能保障企业按期偿还到期债务，但也并不是越大越好，该指标过大则表明企业流动资金利用不充分，盈利能力不强。意义：反映经营活动产生的现金对流动负债的保障程度。");
        explainAll.put("盈利能力.现金债务总额比（比例）", "该指标属于现金流量的流动性分析比率，企业能够用来偿还债务的除借新债还旧债外，一般应当是经营活动的现金流入才能还债。");
        explainAll.put("盈利能力.销售现金比率（%）", "反映企业销售质量的高低，与企业的赊销政策有关。");
        explainAll.put("盈利能力.应收帐款周转率（次）", "应收账款周转率越高，说明其收回越快。反之，说明营运资金过多呆滞在应收账款上，影响正常资金周转及偿债能力。");
        explainAll.put("盈利能力.应收帐款周转天数", "应收账款周转天数表示在一个会计年度内，应收账款从发生到收回周转一次的平均天数（平均收款期），应收账款周转天数越短越好。应收账款的周转次数越多，则周转天数越短；周转次数越少，则周转天数越长。周转天数越少，说明应收账款变现的速度越快，资金被外单位占用的时间越短，管理工作的效率越高。");
        explainAll.put("盈利能力.存货周转率（次）", "存货周转率不仅可以用来衡量企业生产经营各环节中存货运营效率，而且还被用来评价企业的经营业绩，反映企业的绩效。");
        explainAll.put("盈利能力.存货周转天数", "存货周转天数这个数值是越低越好，越低说明公司存货周转速度快，反映良好的销售状况。该比率需要和公司历史上的数据及同行业其他公司对比后才能得出优劣的判断。存货周转天数加上应收账款周转天数再减去应付账款周转天数即得出公司的现金周转周期这一重要指标。");
        explainAll.put("盈利能力.流动资产周转率（次）", "流动资产周转率指企业一定时期内主营业务收入净额同平均流动资产总额的比率，流动资产周转率是评价企业资产利用率的一个重要指标。");
        explainAll.put("盈利能力.流动资产周转天数", "这个指标表明流动资产周转一次所需的天数。");
        explainAll.put("盈利能力.总资产周转天数", "总资产周转率与流动资产周转率都是衡量公司资产运营效率的指标，一般来说流动资产周转率越高，总资产周转率也越高，这两个指标从不同的角度对公司资产的运营进行了评价。");
        explainAll.put("盈利能力.已获利息倍数（比例）", "已获利息倍数，指上市公司息税前利润相对于所需支付债务利息的倍数，可用来分析公司在一定盈利水平下支付债务利息的能力。");
        explainAll.put("盈利能力.总资产报酬率", "表示企业全部资产获取收益的水平，全面反映了企业的获利能力和投入产出状况。通过对该指标的深入分析，可以增强各方面对企业资产经营的关注，促进企业提高单位资产的收益水平。一般情况下，企业可据此指标与市场资本利率进行比较，如果该指标大于市场利率，则表明企业可以充分利用财务杠杆，进行负债经营，获取尽可能多的收益。");
        explainAll.put("盈利能力.主营业务比率", "揭示在企业的利润构成中，经常性主营业务利润所占的比率。通常来说，企业要获得长足的进步依赖于，主营业务的积累。");
        //资本结构
        explainAll.put("资本结构", "");
        explainAll.put("资本结构.资产负债率（%）", "反映债权人提供的资本占全部资本的比例。该指标也被称为举债经营比率。");
        explainAll.put("资本结构.剔除预收账款后的资产负债率（%）", "更能准确地揭示企业的偿债能力状况，因为公司只能通过增加资本的途径来降低负债率。");
        explainAll.put("资本结构.长期资本负债率（%）", "企业的长期资金来源（长期资本）包括非流动负债和股东权益，因此，本指标的含义就是长期资本中非流动负债所占的比例。——长期资本的构成");
        explainAll.put("资本结构.长期资产适合率（%）", "长期资产适合率是企业所有者权益和长期负债之和与固定资产与长期投资之和的比率。该比率从企业资源配置结构方面反映了企业的偿债能力。");
        explainAll.put("资本结构.权益乘数（比例）", "股东权益比例的倒数称为权益乘数，即资产总额是股东权益总额的多少倍，权益乘数反映了企业财务杠杆的大小，权益乘数越大，说明股东投入的资本在资产中所占的比重越小，财务杠杆越大。");
        explainAll.put("资本结构.非流动负债权益比率（%）", "");
        explainAll.put("资本结构.流动负债权益比率（%）", "");
        explainAll.put("资本结构.资本固定化比率（%）", "即被固化的资产占所有者权益的比重·被固化的资产指固定资产净值，在建工程，无形资产及递延资产等项目·指企业占用的机器设备等固定资产占自有资金的比重。");
        //成长能力
        explainAll.put("成长能力", "");
        explainAll.put("成长能力.销售增长率", "销售增长率是衡量企业经营状况和市场占有能力、预测企业经营业务拓展趋势的重要指标，也是企业扩张增量资本和存量资本的重要前提。");
        explainAll.put("成长能力.资本积累率（%）", "资本积累率表示企业当年资本的积累能力，是评价企业发展潜力的重要指标。");
        explainAll.put("成长能力.主营业务利润", "通过对公司的主营业务利润率纵向对比可以看出公司盈利的变动趋势；通过与同行业其他企业主营业务利润率的横向对比，可以看出公司的相对盈利能力。");
        explainAll.put("成长能力.营业利润", "营业利润是企业最基本经营活动的成果，也是企业一定时期获得利润中最主要、最稳定的来源。");
        explainAll.put("成长能力.利润总额", "利润总额指企业在生产经营过程中各种收入扣除各种耗费后的盈余，反映企业在报告期内实现的盈亏总额。");
        explainAll.put("成长能力.净利润", "净利润（收益）是指在利润总额中按规定交纳了所得税后公司的利润留成，一般也称为税后利润或净利润。");
        explainAll.put("成长能力.每股经营活动产生的现金流量净额", "每股经营活动现金流量净额是指经营活动现金净流量与总股本之比，用来反映企业支付股利和资本支出的能力。");
        explainAll.put("成长能力.营业收入同比增长率（%）", "营业收入同比增长率可以直观地反映企业本期取得的数据与上年同期数据对比取得的增长或一下降幅度的值，是经营情况好坏与否的一个直观反映。");
        explainAll.put("成长能力.经营活动产生的现金流量净额", "编制现金流量表的难点在于确定经营活动产生的现金流量净额，由于筹资活动和投资活动在企业业务中相对较少，财务数据容易获取，因此这两项活动的现金流量项目容易填列，并容易确保这两项活动的现金流量净额结果正确，从而根据该公式计算得出的经营活动产生的现金流量净额也容易确保正确。");
        explainAll.put("成长能力.净资产收益率（摊薄）（%）", "强调年末状况，是一个静态指标，说明期末单位净资产对经营净利润的分享,多用来确定股票的价格。 ");
        explainAll.put("成长能力.每股净资产", "在基本分析（Fundamental Analysis ）的各种指标中，每股净资产是判断企业内在价值最重要的参考指标之一。");
        //现金流分析
        explainAll.put("现金流分析", "");
        explainAll.put("现金流分析.现金流动负债比（%）", "反映经营活动产生的现金对流动负债的保障程度。");
        explainAll.put("现金流分析.现金债务总额比（比例）", "该指标属于现金流量的流动性分析比率，企业能够用来偿还债务的除借新债还旧债外，一般应当是经营活动的现金流入才能还债。");
        explainAll.put("现金流分析.销售现金比率（%）", "反映企业销售质量的高低，与企业的赊销政策有关。如果企业有虚假收入，也会使该指标过低。");
        explainAll.put("现金流分析.每股经营现金流量净额", "反映平均每股所获得的现金流量,隐含了上市公司在维持期初现金流量情况下，有能力发给股东的最高现金股利金额。公司现金流强劲，很大程度上表明主营业务收入回款力度较大，产品竞争性强，公司信用度高，经营发展前景有潜力.");
        explainAll.put("现金流分析.全部资产现金回收率（%）", "该指标旨在考评企业全部资产产生现金的能力，该比值越大越好。");
        explainAll.put("现金流分析.现金股利保障倍数（比例）", "该指标属于财务弹性分析比率，表明企业用年度正常经营活动所产生的现金净流量来支付股利的能力。");
        explainAll.put("现金流分析.经营活动产生的现金流量净额", "");
        explainAll.put("现金流分析.投资活动产生的现金流量净额", "");
        explainAll.put("现金流分析.筹资活动产生的现金流量净额", "");
        //杜邦分析
        explainAll.put("杜邦分析", "");
        explainAll.put("杜邦分析.净资产收益率（ROE）（%）", "净资产收益率又称股东权益收益率，是净利润与平均股东权益的百分比，是公司税后利润除以净资产得到的百分比率，该指标反映股东权益的收益水平，用以衡量公司运用自有资本的效率。");
        explainAll.put("杜邦分析.销售净利率（%）", "该指标反映每一元销售收入带来的净利润是多少。表示销售收入的收益水平。");
        explainAll.put("杜邦分析.权益乘数（比例）", "股东权益比例的倒数称为权益乘数，即资产总额是股东权益总额的多少倍，权益乘数反映了企业财务杠杆的大小，权益乘数越大，说明股东投入的资本在资产中所占的比重越小，财务杠杆越大。");
        explainAll.put("杜邦分析.资产周转率（次）", "该项指标反映总资产的周转速度，周转越快，说明销售能力越强。企业可以采用薄利多销的方法，加速资产周转，带来利润绝对额的增加。");
        explainAll.put("杜邦分析.净利润", "净利润（收益）是指在利润总额中按规定交纳了所得税后公司的利润留成，一般也称为税后利润或净利润。");
        explainAll.put("杜邦分析.利润总额", "利润总额指企业在生产经营过程中各种收入扣除各种耗费后的盈余，反映企业在报告期内实现的盈亏总额。");
        explainAll.put("杜邦分析.息税前收入（EBIT）", "息税前利润是指企业支付利息和交纳所得税前的利润。");
        //估值分析
        explainAll.put("估值分析", "");
        explainAll.put("估值分析.市盈率", "市盈率是很具参考价值的股市指针，一方面，投资者亦往往不认为严格按照会计准则计算得出的盈利数字真实反映公司在持续经营基础上的获利能力");
        explainAll.put("估值分析.市净率", "市净率可用于股票投资分析，一般来说市净率较低的股票，投资价值较高，相反，则投资价值较低；但在判断投资价值时还要考虑当时的市场环境以及公司经营情况、盈利能力等因素。");
        explainAll.put("估值分析.市销率", "由于市盈率、市净率两个指标具有明显的行业特征，限制了公司间的比较，所以西方有些学者就提出了市销率的概念，即每股市价/每股销售收入。这个指标主要用于创业板的企业或高科技企业。");
        explainAll.put("估值分析.市现率", "市现率越小，表明上市公司的每股现金增加额越多，经营压力越小。对于参与资本运作的投资机构，市现率还意味着其运作资本的增加效率。");
        explainAll.put("估值分析.总市值", "指所有上市公司在股票市场上的价值总和。");
        explainAll.put("估值分析.流通市值", "流通市值指在某特定时间内当时可交易的流通股股数乘以当时股价得出的流通股票总价值。");
        //指数分析
        explainAll.put("指数分析", "");
        explainAll.put("指数分析.流动比率（比例）", "体现企业的偿还短期债务的能力。流动资产越多，短期债务越少，则流动比率越大，企业的短期偿债能力越强。");
        explainAll.put("指数分析.速动比率（比例）", "比流动比率更能体现企业的偿还短期债务的能力。因为流动资产中，尚包括变现速度较慢且可能已贬值的存货，因此将流动资产扣除存货再与流动负债对比，以衡量企业的短期偿债能力。");
        explainAll.put("指数分析.资本利润率", "");
        explainAll.put("指数分析.负债比率", "负债比率是反映债务和资产、净资产关系的比率。它反映企业偿付到期长期债务的能力。");
        explainAll.put("指数分析.应收账款周转率（次）", "应收账款周转率越高，说明其收回越快。反之，说明营运资金过多呆滞在应收账款上，影响正常资金周转及偿债能力。");
        explainAll.put("指数分析.存货周转率（次）", "存货周转率不仅可以用来衡量企业生产经营各环节中存货运营效率，而且还被用来评价企业的经营业绩，反映企业的绩效。");
        explainAll.put("指数分析.利息保障倍数", "");
        explainAll.put("指数分析.销售增长率", "销售增长率是衡量企业经营状况和市场占有能力、预测企业经营业务拓展趋势的重要指标，也是企业扩张增量资本和存量资本的重要前提。");


        //=====================指标分析提示说明==================================
        //偿债能力
        explainAll.put("偿债能力", "（1）增加变现能力的因素：可以动用的银行贷款指标；准备很快变现的长期资产；偿债能力的声誉。（2）减弱变现能力的因素：未作记录的或有负债；担保责任引起的或有负债。");
        explainAll.put("偿债能力.流动比率（比例）", "低于正常值，企业的短期偿债风险较大。一般情况下，营业周期、流动资产中的应收账款数额和存货的周转速度是影响流动比率的主要因素。");
        explainAll.put("偿债能力.速动比率（比例）", "低于1 的速动比率通常被认为是短期偿债能力偏低。影响速动比率的可信性的重要因素是应收账款的变现能力，账面上的应收账款不一定都能变现，也不一定非常可靠。");
        explainAll.put("偿债能力.现金到期债务", "通常作为企业到期的长期负债和本期应付票据是不能延期的，到期必须如数偿还，企业设置的标准值为1.5。该比率越高，企业资金流动性越好，企业到期偿还债务的能力就越强。");
        explainAll.put("偿债能力.现金流量利息保障倍数（比例）", "该比率表明1元的利息费用有多少倍的经营现金净流量作保障，以收益为基础的利息保障倍数更可靠。");
        //营运能力
        explainAll.put("营运能力", "");
        explainAll.put("营运能力.存货周转率（次）", "存货周转速度反映存货管理水平，存货周转率越高，存货的占用水平越低，流动性越强，存货转换为现金或应收账款的速度越快。它不仅影响企业的短期偿债能力，也是整个企业管理的重要内容。");
        explainAll.put("营运能力.存货周转天数", "存货周转速度反映存货管理水平，存货周转速度越快，存货的占用水平越低，流动性越强，存货转换为现金或应收账款的速度越快。它不仅影响企业的短期偿债能力，也是整个企业管理的重要内容。");
        explainAll.put("营运能力.应收账款周转率（次）", "应收账款周转率，要与企业的经营方式结合考虑。以下几种情况使用该指标不能反映实际情况：第一，季节性经营的企业；第二，大量使用分期收款结算方式；第三，大量使用现金结算的销售；第四，年末大量销售或年末销售大幅度下降。");
        explainAll.put("营运能力.应收账款周转天数", "应收账款周转率，要与企业的经营方式结合考虑。以下几种情况使用该指标不能反映实际情况：第一，季节性经营的企业；第二，大量使用分期收款结算方式；第三，大量使用现金结算的销售；第四，年末大量销售或年末销售大幅度下降。");
        explainAll.put("营运能力.营业周期", "营业周期，一般应结合存货周转情况和应收账款周转情况一并分析。营业周期的长短，不仅体现企业的资产管理水平，还会影响企业的偿债能力和盈利能力。");
        explainAll.put("营运能力.流动资产周转率（次）", "流动资产周转率要结合存货、应收账款一并进行分析，和反映盈利能力的指标结合在一起使用，可全面评价企业的盈利能力。");
        explainAll.put("营运能力.总资产周转率（次）", "总资产周转指标用于衡量企业运用资产赚取利润的能力。经常和反映盈利能力的指标一起使用，全面评价企业的盈利能力。");
        explainAll.put("营运能力.固定资产周转率", "固定资产周转率主要用于分析对厂房、设备等固定资产的利用效率，比率越高，说明利用率越高，管理水平越好。如果固定资产周转率与同行业平均水平相比偏低，则说明企业对固定资产的利用率较低，可能会影响企业的获利能力。它反应了企业资产的利用程度.");
        explainAll.put("营运能力.应付账款周转率（次）", "这是一个需要密切注意的问题，因为你需要卖方信用作为低成本或无成本融资的一个来源。另外，应付账款周转率的恶化可能是现金危机的征兆并会危害这两者之间的关系。所以目标应该是使应收账款周转率和应付账款周转率的时间尽可能的接近，这样现金流入量才能和现金流出量相抵。");
        explainAll.put("营运能力.应付账款周转天数", "通常应付账款周转天数越长越好，说明公司可以更多的占用供应商货款来补充营运资本而无需向银行短期借款。在同行业中，该比率较高的公司通常是市场地位较强，在行业内采购量巨大的公司，且信誉良好，所以才能在占用货款上拥有主动权。");
        explainAll.put("营运能力.净营业周期", "一般情况下，营业周期短，说明资金周转速度快；营业周期长，说明资金周转速度慢。这就是营业周期与流动比率的关系。决定流动比率高低的主要因素是存货周转天数和应收账款周转天数。");
        explainAll.put("营运能力.非流动资产周转率", "");
        //负债比率
        explainAll.put("负债比率", "负债比率越大，企业面临的财务风险越大，获取利润的能力也越强。如果企业资金不足，依靠欠债维持，导致资产负债率特别高，偿债风险就应该特别注意了。资产负债率在60％—70％，比较合理、稳健；达到85％及以上时，应视为发出预警信号，企业应提起足够的注意。");
        explainAll.put("负债比率.资产负债比率（%）", "资产负债率越低，说明以负债取得的资产越少，企业运用外部资金的能力较差);资产负债越高，说明企业通过借债筹资的资产越多，风险越大。因此，通常资产负债率应保持在40%-60%为佳。");
        explainAll.put("负债比率.产权比率（%）", "负债总额与所有者权益总额的比率。企业设置的标准值：1：2。");
        explainAll.put("负债比率.有形净值债务率（%）", "有形净值债务率指标实质上是产权比率指标的延伸，是更为谨慎、保守地反映在企业清算时债权人投入的资本受到股东权益的保障程度。从长期偿债能力来讲，比率越低越好。");
        explainAll.put("负债比率.已获利息倍数（比例）", "支付债务利息的能力。利息保障倍数至少要大于1，该指标为3时较为适当。");
        //每股指标
        explainAll.put("每股指标", "");
        explainAll.put("每股指标.每股净资产", "每股净资产越少，股东拥有的每股资产价值越少。通常每股净资产越高越好。");
        explainAll.put("每股指标.每股经营活动产生的现金流量净额", "该比率越大，证明企业支付股利和资本支出的能力越强。");
        explainAll.put("每股指标.每股营业总收入", "");
        explainAll.put("每股指标.每股息税前利润", "");
        explainAll.put("每股指标.每股资本公积", "");
        explainAll.put("每股指标.每股盈余公积", "");
        explainAll.put("每股指标.每股未分配利润", "");
        explainAll.put("每股指标.每股留存收益", "");
        explainAll.put("每股指标.每股现金流量净额", "");
        //盈利能力
        explainAll.put("盈利能力", "企业在增加销售收入的同时，必须要相应获取更多的净利润才能使销售净利率保持不变或有所提高。销售净利率可以分解成为销售毛利率、销售税金率、销售成本率、销售期间费用率等指标进行分析。");
        explainAll.put("盈利能力.销售净利率（%）", "该指标值越高，表明企业的资产利用效率越高，注重了增加收入和节约资金两个方面。");
        explainAll.put("盈利能力.销售毛利率（%）", "销售毛利率越高，说明企业销售成本在销售收入净额中所占的比重越小，在期间费用和其他业务利润一定的情况下，营业利润就越高。");
        explainAll.put("盈利能力.资产净利率（总资产报酬率（%））", "该指标越高，表明企业投入产出的水平越好，企业的资产运营越有效。");
        explainAll.put("盈利能力.净资产收益率（权益报酬率）（%）", "净资产收益率可衡量公司对股东投入资本的利用效率。它弥补了每股税后利润指标的不足  指标值越高，说明投资带来的收益越高。");
        explainAll.put("盈利能力.净资产收益率（平均）（%）", "介于20%-50%之间，一般相对合理稳定，流动性强的商品，毛利率低。");
        explainAll.put("盈利能力.主营业务毛利率", "该指标越高，表示取得同样销售收入的销售成本越低，销售利润越高。");
        explainAll.put("盈利能力.主营业务成本率", "该指标越高，表示取得同样销售收入的销售成本越低，销售利润越高。");
        //explainAll.put("盈利能力.主营业务毛利率", "");
        explainAll.put("盈利能力.主营业务净利率", "该比例高则主营业务收入的盈利贡献就低。");
        //explainAll.put("盈利能力.主营业务成本率", "该比例高则主营业务收入的盈利贡献就低。");
        explainAll.put("盈利能力.营业费用率", "说明营业过程中的费用支出越小，获利水平越高。营业费用率的参考标准是14％～16％以下。");
        explainAll.put("盈利能力.管理费用率", "不同的行业有不同的比率，它是个变量。");
        explainAll.put("盈利能力.财务费用率", "该项指标越高，利润就越大，反映企业的经济效益越好。");
        explainAll.put("盈利能力.成本、费用利润率", "该项指标越高，利润就越大，反映企业的经济效益越好。");
        explainAll.put("盈利能力.主营业务税金率", "该指标越高，表明企业投入产出的水平越好，企业的资产运营越有效。");
        explainAll.put("盈利能力.资产净利率", "一般情况下，本指标越高，表明企业沉积下来、不能正常参加经营运转的资金越多，资金利用率越差。该指标越小越好，0是最优水平。");
        explainAll.put("盈利能力.流动比率（比例）", "低于1 的速动比率通常被认为是短期偿债能力偏低。影响速动比率的可信性的重要因素是应收账款的变现能力，账面上的应收账款不一定都能变现，也不一定非常可靠。");
        explainAll.put("盈利能力.速动比率（比例）", "负债比率越大，企业面临的财务风险越大，获取利润的能力也越强。如果企业资金不足，依靠欠债维持，导致资产负债率特别高，偿债风险就应该特别注意了。资产负债率在60％—70％，比较合理、稳健；达到85％及以上时，应视为发出预警信号，企业应提起足够的注意。");
        explainAll.put("盈利能力.资产负债比率（%）", "从维护企业财务结构稳定和长期安全性角度出发，该指标数 值较高比较好，但过高也会带来融资成本增加的问题。");
        explainAll.put("盈利能力.长期资产适合率（%）", "反映了投资者投入企业资本的保全性和增长性，该指标越高，表明企业的资本保全状况越好，所有者权益增长越快，债权人的债务越有保障，企业发展后劲越强。");
        explainAll.put("盈利能力.资本积累率（%）", "若该指标数值等于1，说明企业的销售在当年已全部收现，为无风险收入；若该指标数值大于1，说明企业不仅已将当年的销售全部收现，而且还回笼了部分以往年度的欠款，也为高质量收益；若该指标数值小于1，说明当年销售中仍有部分货款没有在年内回笼或以非现金形式回笼，反映企业仍存在收益上的风险。一旦货款在今后的某一时间成为坏账损失，则收益中的潜在风险就变成了现实。");
        explainAll.put("盈利能力.销售收现比", "接近1，说明企业可以用经营获取的现金与其应获现金相当，收益质量高；若小于1，则说明企业的收益质量不够好。");
        explainAll.put("盈利能力.现金流动负债比（%）", "计算结果要与过去比较，与同业比较才能确定高与低。这个比率越高，企业承担债务的能力越强。这个比率同时也体现企业的最大付息能力。");
        explainAll.put("盈利能力.现金债务总额比（比例）", "该比值也体现了企业最大付息能力。比如一公司最大的付息能力是2%，即利息超过2%时，此公司将不能按时付息。");
        explainAll.put("盈利能力.销售现金比率（%）", "其数值越大表示销售收入变现能力高，资金回收快，公司可运作资金充盈。");
        explainAll.put("盈利能力.应收帐款周转率（次）", "应收账款分析指标还可以应用于会计季度或会计月度的分析。");
        explainAll.put("盈利能力.应收帐款周转天数", "产品销售净额是购货单位最终承诺结算额，也就是真正算得上是销售收入的数额。销售收入净额与销售收入总额之比，反映企业商品在市场的竞争程度，也反映了企业销售策略，以及合同落实程度。");
        explainAll.put("盈利能力.存货周转率（次）", "存货周转速度越快，存货的占用水平越低，流动性越强，存货转换为现金、应收账款等的速度越快。提高存货周转率可以提高企业的变现能力，而存货周转速度越慢则变现能力越差。库存的管理应纳入企业管理的重要内容，关乎企业资金链的运作。");
        explainAll.put("盈利能力.存货周转天数", "该指标越高，表明企业流动资产周转速度越快，利用越好。在较快的周转速度下，流动资产会相对节约，相当于流动资产投入的增加，在一定程度上增强了企业的盈利能力；而周转速度慢，则需要补充流动资金参加周转，会形成资金浪费，降低企业盈利能力。");
        explainAll.put("盈利能力.流动资产周转率（次）", "天数越少，速度越快，利用效果越好。在使用这个指标时，对平均流动资产的计算，一般为（期初+期末）/ 2,企业内部使用时，应按照旬计算较为准确。");
        explainAll.put("盈利能力.流动资产周转天数", "流动资产转换成现金平均需要的时间，流动资产周转天数越多，表明流动资产在生产和销售阶段经历的时间周期越长，说明资产周转的越慢。");
        explainAll.put("盈利能力.总资产周转天数", "一般情况下，该数值越高，表明企业总资产周转速度越快。销售能力越强，资产利用效率越高。");
        explainAll.put("盈利能力.已获利息倍数（比例）", "该指标越高，表明企业投入产出的水平越好，企业的资产运营越有效。");
        explainAll.put("盈利能力.总资产报酬率", "指标值越高，说明投资带来的收益越高。该指标体现了自有资本获得净收益的能力。15-25%为佳。");
        explainAll.put("盈利能力.主营业务比率", "越高越好。");
        //资本结构
        explainAll.put("资本结构", "负债比率越大，企业面临的财务风险越大，获取利润的能力也越强。如果企业资金不足，依靠欠债维持，导致资产负债率特别高，偿债风险就应该特别注意了。资产负债率在60％—70％，比较合理、稳健；达到85％及以上时，应视为发出预警信号，企业应提起足够的注意。");
        explainAll.put("资本结构.资产负债率（%）", "资产负债率的适宜水平是40%～60%。");
        explainAll.put("资本结构.剔除预收账款后的资产负债率（%）", "长期资本负债率反映企业的长期资本的结构，由于流动负债的数额经常变化，资本结构管理大多使用长期资本结构。指非流动负债占长期资本的百分比");
        explainAll.put("资本结构.长期资本负债率（%）", "从维护企业财务结构稳定和长期安全性角度出发，该指标数 值较高比较好，但过高也会带来融资成本增加的问题。资本负债率为200%为一般的警戒线，若超过则应该格外关注。");
        explainAll.put("资本结构.长期资产适合率（%）", "长期资产适合率指标为100%较好，但该指标究竟多高合适，应根据企业的具体情况，参照行业平均水平确定。");
        explainAll.put("资本结构.权益乘数（比例）", "该比率越小，表明所有者投入企业的资本占全部资产的比重越大，企业的负债程度越低，债权人权益受保护的程度越高。");
        explainAll.put("资本结构.非流动负债权益比率（%）", "");
        explainAll.put("资本结构.流动负债权益比率（%）", "该指标值越低，表明公司自由资本用于长期资产的数额相对较少);反之，则表明公司自有资本用于长期资产的数额相对较多，公司日常经营所需资金需靠借款筹集。");
        explainAll.put("资本结构.资本固定化比率（%）", "");
        //成长能力
        explainAll.put("成长能力", "该指标越大，表明其增长速度越快，企业市场前景越好。");
        explainAll.put("成长能力.销售增长率", "该指标越大，表明其增长速度越快，企业市场前景越好。");
        explainAll.put("成长能力.资本积累率（%）", "资本积累率反映了投资者投入企业资本的保全性和增长性，该指标越高，表明企业的资本积累越多，企业资本保全性越强，应付风险、持续发展的能力越大。如果期末期初都为负数，期末负数的绝对值比期初负数的绝对值大，指标虽为正数，实际是亏损的。");
        explainAll.put("成长能力.主营业务利润", "其比重应是最高的，其他业务利润、投资收益和营业外收支相对来讲比重不应很高。");
        explainAll.put("成长能力.营业利润", "越高越好，营业利润是企业最基本经营活动的成果，也是企业一定时期获得利润中最主要、最稳定的来源。");
        explainAll.put("成长能力.利润总额", "越高越好。");
        explainAll.put("成长能力.净利润", "企业的所得税率都是法定的，所得税率愈高，净利润就愈少。");
        explainAll.put("成长能力.每股经营活动产生的现金流量净额", "一般而言，该比率越大，证明企业支付股利和资本支出的能力越强。");
        explainAll.put("成长能力.营业收入同比增长率（%）", "营业收入同比增长率越大，说明企业当期获得的营业收入相对去年同期增长越大，对企业盈利有正面影响；而营业收入同比增长率为负时，则表明企业营业收入出现下降，应引起企业管理者或投资者的注意。");
        explainAll.put("成长能力.经营活动产生的现金流量净额", "");
        explainAll.put("成长能力.净资产收益率（摊薄）（%）", "是一个静态指标，说明期末单位净资产对经营净利润的分享,多用来确定股票的价格。");
        explainAll.put("成长能力.每股净资产", "反映每股股票所拥有的资产现值。每股净资产越高，股东拥有的每股资产价值越多；每股净资产越少，股东拥有的每股资产价值越少。通常每股净资产越高越好。");
        //现金流分析
        explainAll.put("现金流分析", "该比率越高，企业资金流动性越好，企业到期偿还债务的能力就越强。");
        explainAll.put("现金流分析.现金流动负债比（%）", "计算结果要与过去比较，与同业比较才能确定高与低。这个比率越高，企业承担债务的能力越强。这个比率同时也体现企业的最大付息能力。");
        explainAll.put("现金流分析.现金债务总额比（比例）", "该比率反映每元销售收入得到的现金流量净额，其数值越大越好，表明企业的收入质量越好，资金利用效果越好。");
        explainAll.put("现金流分析.销售现金比率（%）", "经营活动现金流入占现金总流入比重大的企业，经营状况较好，财务风险较低，现金流入结构较为合理。其次，分别计算经营活动现金支出、投资活动现金支出和筹资活动现金支出占现金总流出的比重，它能具体反映企业的现金用于哪些方面。经营活动现金支出比重大的企业，其生产经营状况正常，现金支出结构较为合理。");
        explainAll.put("现金流分析.每股经营现金流量净额", "比值越大说明资产利用效果越好，利用资产创造的现金流入越多，整个企业获取现金能力越强，经营管理水平越高。反之，则经营管理水平越低，经营者有待提高管理水平，进而提高企业的经济效益。");
        explainAll.put("现金流分析.全部资产现金回收率（%）", "该比率越大，说明企业资金自给率越高，企业发展能力越强。如果现金满足投资比率大于或等于1，表明企业经营活动所形成的现金流量足以应付各项资本性支、存货增加和现金股利的需要，不需要对外筹资；若该比率小于1，说明企业来自经营活动的现金不足以供应目前营运规模和支付现金股利的需要，不足的部分需要靠外部筹资补充。如果一个企业的现金满足投递比率长期小于1，则其理财政策没有可持续性。");
        explainAll.put("现金流分析.现金股利保障倍数（比例）", "该指标还体现支付股利的现金来源及其可靠程度，是对传统的股利支付率的修正和补充。支付现金股利率越高，说明企业的现金股利占结余现金流量的比重越小，企业支付现金股利的能力越强。");
        explainAll.put("现金流分析.经营活动产生的现金流量净额", "");
        explainAll.put("现金流分析.投资活动产生的现金流量净额", "");
        explainAll.put("现金流分析.筹资活动产生的现金流量净额", "");
        //杜邦分析
        explainAll.put("杜邦分析", "");
        explainAll.put("杜邦分析.净资产收益率（ROE）（%）", "净资产收益率可衡量公司对股东投入资本的利用效率。它弥补了每股税后利润指标的不足  指标值越高，说明投资带来的收益越高。");
        explainAll.put("杜邦分析.销售净利率（%）", "越高越好。");
        explainAll.put("杜邦分析.权益乘数（比例）", "权益乘数越大，代表公司向外融资的财务杠杆倍数也越大，公司将承担较大的风险。但是，若公司营运状况刚好处于向上趋势中，较高的权益乘数反而可以创造更高的公司获利，透过提高公司的股东权益报酬率，对公司的股票价值产生正面激励效果。");
        explainAll.put("杜邦分析.资产周转率（次）", "总资产周转指标用于衡量企业运用资产赚取利润的能力。经常和反映盈利能力的指标一起使用，全面评价企业的盈利能力。");
        explainAll.put("杜邦分析.净利润", "企业的所得税率都是法定的，所得税率愈高，净利润就愈少。");
        explainAll.put("杜邦分析.利润总额", "越高越好。");
        explainAll.put("杜邦分析.息税前收入（EBIT）", "EBIT通过剔除所得税及利息，可以使投资者评价项目的时候不用考虑项目适用的所得税率和融资成本，这样方便了投资者将项目放在不同的资本结构中进行考察。");
        //估值分析
        explainAll.put("估值分析", "");
        explainAll.put("估值分析.市盈率", "随着中国资本市场的快速发展市盈率一般保持在15～20倍左右。");
        explainAll.put("估值分析.市净率", "在成熟股市一般市净率在2。5以下可看作是合理的。");
        explainAll.put("估值分析.市销率", "收入分析是评估企业经营前景至关重要的一步。没有销售，就不可能有收益。这也是最近两年在国际资本市场新兴起来的市场比率，主要用于创业板的企业或高科技企业。");
        explainAll.put("估值分析.市现率", "市现率越小，表明上市公司的每股现金增加额越多，经营压力越小。");
        explainAll.put("估值分析.总市值", "总市值用来表示个股权重大小或大盘的规模大小，对股票买卖没有什么直接作用。");
        explainAll.put("估值分析.流通市值", "流通市值大，利于大盘稳定，不易炒作，流通市值小，对大盘没有太大影响，易于炒作。");
        //指数分析
        explainAll.put("指数分析", "");
        explainAll.put("指数分析.流动比率（比例）", "低于1 的速动比率通常被认为是短期偿债能力偏低。影响速动比率的可信性的重要因素是应收账款的变现能力，账面上的应收账款不一定都能变现，也不一定非常可靠。");
        explainAll.put("指数分析.速动比率（比例）", "负债比率越大，企业面临的财务风险越大，获取利润的能力也越强。如果企业资金不足，依靠欠债维持，导致资产负债率特别高，偿债风险就应该特别注意了。资产负债率在60％—70％，比较合理、稳健；达到85％及以上时，应视为发出预警信号，企业应提起足够的注意。");
        explainAll.put("指数分析.资本利润率", "");
        explainAll.put("指数分析.负债比率", "负债比率越大，企业面临的财务风险越大，获取利润的能力也越强。如果企业资金不足，依靠欠债维持，导致资产负债率特别高，偿债风险就应该特别注意了。资产负债率在60％—70％，比较合理、稳健；达到85％及以上时，应视为发出预警信号，企业应提起足够的注意。");
        explainAll.put("指数分析.应收账款周转率（次）", "应收账款周转率，要与企业的经营方式结合考虑。以下几种情况使用该指标不能反映实际情况：第一，季节性经营的企业；第二，大量使用分期收款结算方式；第三，大量使用现金结算的销售；第四，年末大量销售或年末销售大幅度下降。");
        explainAll.put("指数分析.存货周转率（次）", "存货周转速度越快，存货的占用水平越低，流动性越强，存货转换为现金、应收账款等的速度越快。提高存货周转率可以提高企业的变现能力，而存货周转速度越慢则变现能力越差。库存的管理应纳入企业管理的重要内容，关乎企业资金链的运作。");
        explainAll.put("指数分析.利息保障倍数（比例）", "");
        explainAll.put("指数分析.销售增长率", "该指标越大，表明其增长速度越快，企业市场前景越好。");
    }

    public static void main(String[] args) {

    }
}