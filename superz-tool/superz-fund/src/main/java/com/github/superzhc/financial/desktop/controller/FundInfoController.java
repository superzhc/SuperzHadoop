package com.github.superzhc.financial.desktop.controller;

import com.github.superzhc.common.http.HttpRequest;
import com.github.superzhc.common.javafx.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

/**
 * @author superz
 * @create 2022/7/14 23:41
 */
public class FundInfoController {
    private static final String URL_TEMPLATE = "http://fundf10.eastmoney.com/jbgk_%s.html";

    @FXML
    private TextField txtFundCode;

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtType;

    @FXML
    private TextField txtFoundDate;

    @FXML
    private TextField txtAssetSize;

    @FXML
    private TextField txtKeeper;

    @FXML
    private TextField txtManager;

    @FXML
    private TextField txtInvestmentObjective;

    @FXML
    private TextField txtInvestmentScope;

    @FXML
    private TextField txtDividendPolicy;

    @FXML
    private TextField txtSimpleName;

    @FXML
    private TextField txtTrackIndex;

    @FXML
    private TextField txtIssueDate;

    @FXML
    private TextField txtShareSize;

    @FXML
    private TextField txtCustodian;

    @FXML
    private TextField txtDividend;

    @FXML
    private TextField txtInvestmentConcept;

    @FXML
    private TextField txtInvestmentTatics;

    @FXML
    private TextField txtRiskIncomeCharacteristics;

    public void btnSearchFund(ActionEvent actionEvent) {
        String fundCode = txtFundCode.getText();
        if (null == fundCode || fundCode.trim().length() == 0) {
            DialogUtils.error("提示", "请输入基金Code");
            return;
        }

        String url = String.format(URL_TEMPLATE, fundCode);
        String html = HttpRequest.get(url).body();
        Document doc = Jsoup.parse(html);
        JXDocument jxDoc = JXDocument.create(doc);

        // 名称
        String fullName = jxDoc.selNOne("//div/div/table/tbody/tr[1]/td[1]/text()").asString();
        txtFullName.setText(fullName);

        // 简称
        String simpleName = jxDoc.selNOne("//div/div/table/tbody/tr[1]/td[2]/text()").asString();
        txtSimpleName.setText(simpleName);

        // // 代码
        // String code = jxDoc.selNOne("//div/div/table/tbody/tr[2]/td[1]/text()").asString();

        // 类型
        String type = jxDoc.selNOne("//div/div/table/tbody/tr[2]/td[2]/text()").asString();
        txtType.setText(type);

        // 发行时间
        String issueDate = jxDoc.selNOne("//div/div/table/tbody/tr[3]/td[1]/text()").asString();
        txtIssueDate.setText(issueDate);

        // 成立日期
        String foundDate = jxDoc.selNOne("//div/div/table/tbody/tr[3]/td[2]/text()").asString();
        txtFoundDate.setText(foundDate);

        // 资产规模
        String assetSize = jxDoc.selNOne("//div/div/table/tbody/tr[4]/td[1]/text()").asString();
        txtAssetSize.setText(assetSize);

        // 份额规模
        String shareSize = jxDoc.selNOne("//div/div/table/tbody/tr[4]/td[2]/allText()").asString();
        txtShareSize.setText(shareSize);

        // 管理人
        String keeper = jxDoc.selNOne("//div/div/table/tbody/tr[5]/td[1]/a/text()").asString();
        txtKeeper.setText(keeper);

        // 托管人
        String custodian = jxDoc.selNOne("//div/div/table/tbody/tr[5]/td[2]/a/text()").asString();
        txtCustodian.setText(custodian);

        // 经理人
        String manager = jxDoc.selNOne("//div/div/table/tbody/tr[6]/td[1]/a/text()").asString();
        txtManager.setText(manager);

        // 分红
        String dividend = jxDoc.selNOne("//div/div/table/tbody/tr[6]/td[2]/a/text()").asString();
        txtDividend.setText(dividend);

        // 追踪指数
        String trackIndex = jxDoc.selNOne("//div/div/table/tbody/tr[10]/td[2]/text()").asString();
        txtTrackIndex.setText(trackIndex);

        // 目标
        JXNode investmentObjective = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[2]/div/p/text()");
        txtInvestmentObjective.setText(null == investmentObjective ? null : investmentObjective.asString());

        // 理念
        String investmentConcept = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[3]/div/p/text()").asString();
        txtInvestmentConcept.setText(investmentConcept);

        // 范围
        String investmentScope = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[4]/div/p/text()").asString();
        txtInvestmentScope.setText(investmentScope);

        // 策略
        String investmentTatics = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[5]/div/p/text()").asString();
        txtInvestmentTatics.setText(investmentTatics);

        // 分红策略
        String dividendPolicy = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[6]/div/p/text()").asString();
        txtDividendPolicy.setText(dividendPolicy);

        // 风险收益特征
        String riskIncomeCharacteristics = jxDoc.selNOne("//*[@id=\"bodydiv\"]/div[8]/div[3]/div[2]/div[3]/div/div[7]/div/p/text()").asString();
        txtRiskIncomeCharacteristics.setText(riskIncomeCharacteristics);
    }
}
