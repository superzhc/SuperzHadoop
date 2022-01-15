package com.github.superzhc.data.eastmoney;

import com.github.superzhc.data.common.HtmlData;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author superz
 * @create 2022/1/12 11:10
 */
public class FundBasic extends HtmlData {
    private static final String URL_TEMPLATE = "http://fundf10.eastmoney.com/jbgk_%s.html";

    public Map<String, Object> fund(String fundCode) {
        Doc doc = get(String.format(URL_TEMPLATE, fundCode));
        JXDocument jxDoc = doc.getjXDocument();

        // 名称
        String fullName = jxDoc.selNOne("//div/div/table/tbody/tr[1]/td[1]/text()").asString();
        // 简称
        String simpleName = jxDoc.selNOne("//div/div/table/tbody/tr[1]/td[2]/text()").asString();
        // 代码
        String code = jxDoc.selNOne("//div/div/table/tbody/tr[2]/td[1]/text()").asString();
        // 类型
        String type = jxDoc.selNOne("//div/div/table/tbody/tr[2]/td[2]/text()").asString();
        // 发行时间
        String issueDate = jxDoc.selNOne("//div/div/table/tbody/tr[3]/td[1]/text()").asString();
        // 成立日期
        String foundDate = jxDoc.selNOne("//div/div/table/tbody/tr[3]/td[2]/text()").asString();
        // 资产规模
        String assetSize = jxDoc.selNOne("//div/div/table/tbody/tr[4]/td[1]/text()").asString();
        // 份额规模
        String shareSize = jxDoc.selNOne("//div/div/table/tbody/tr[4]/td[2]/allText()").asString();
        // 管理人
        String keeper = jxDoc.selNOne("//div/div/table/tbody/tr[5]/td[1]/a/text()").asString();
        // 托管人
        String custodian = jxDoc.selNOne("//div/div/table/tbody/tr[5]/td[2]/a/text()").asString();
        // 经理人
        String manager = jxDoc.selNOne("//div/div/table/tbody/tr[6]/td[1]/a/text()").asString();
        // 分红
        String dividend = jxDoc.selNOne("//div/div/table/tbody/tr[6]/td[2]/a/text()").asString();
        // 追踪指数
        String trackIndex = jxDoc.selNOne("//div/div/table/tbody/tr[10]/td[2]/text()").asString();
        // 目标
        JXNode investmentObjective = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[2]/div/p/text()");
        // 理念
        String investmentConcept = doc.selOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[3]/div/p/text()").asString();
        // 范围
        String investmentScope = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[4]/div/p/text()").asString();
        // 策略
        String investmentTatics = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[5]/div/p/text()").asString();
        // 分红策略
        String dividendPolicy = doc.selOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[6]/div/p/text()").asString();
        // 风险收益特征
        String riskIncomeCharacteristics = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[7]/div/p/text()").asString();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("code", code);
        map.put("full_name", fullName);
        map.put("name", simpleName);
        map.put("type", type);
        map.put("issue_date", issueDate);
        map.put("found_date", foundDate);
        map.put("asset_size", assetSize);
        map.put("share_size", shareSize);
        map.put("manager", manager);
        map.put("dividend", dividend);
        map.put("track_index", trackIndex);
        map.put("investment_objective", null == investmentObjective ? null : investmentObjective.asString());
        map.put("investment_concept", investmentConcept);
        map.put("investment_scope", investmentScope);
        map.put("investment_tatics", investmentTatics);
        map.put("dividend_policy", dividendPolicy);
        map.put("risk_income_characteristics", riskIncomeCharacteristics);
        return map;
    }

    public static void main(String[] args) {
        new FundBasic().fund("159857");
    }
}
