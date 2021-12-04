package com.github.superzhc.data.eastmoney;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.superzhc.common.okhttp3.OkHttpUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 中国宏观数据
 *
 * @author superz
 * @create 2021/12/4 11:25
 */
public class MarcoCN {
    private static final Logger log = LoggerFactory.getLogger(MarcoCN.class);

    private ObjectMapper mapper;

    public MarcoCN() {
        mapper = new ObjectMapper();
    }

    public String marcoCmlrd() {
        URL url = null;
        try {
            url = new URL("http://114.115.232.154:8080/handler/download.ashx");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            try (InputStream in = conn.getInputStream()) {
                XSSFWorkbook workbook = new XSSFWorkbook(in);
                XSSFSheet sheet = workbook.getSheet("Data");

                ArrayNode nodes = mapper.createArrayNode();
                List<String> headers = new ArrayList<>();
                int index = 0;
                for (Row row : sheet) {
                    if (index == 0) {
                        for (Cell cell : row) {
                            headers.add(cell.getStringCellValue());
                        }
                    } else if (index > 1) {
                        int cellIndex = 0;
                        ObjectNode obj = nodes.addObject();
                        for (Cell cell : row) {
                            if (null != headers.get(cellIndex) && headers.get(cellIndex).trim().length() > 0) {
                                obj.putPOJO(headers.get(cellIndex), cellValue(cell));
                            }
                            cellIndex++;
                        }
                    }
                    index++;
                }
                return mapper.writeValueAsString(nodes);
            }
        } catch (IOException e) {
            log.error("读取网络文件异常");
        }
        return null;
    }

    private Object cellValue(Cell cell) {
        Object value = null;
        // 区分不同类型的单元格数值格式，分别处理
        switch (cell.getCellType()) {
            case STRING: // 字符串类型
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC: // 数值类型
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    value = new SimpleDateFormat("yyyy-MM").format(date);
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN: // 布尔值类型
                value = cell.getBooleanCellValue();
                break;
            case FORMULA: // 公式类型
                value = cell.getCellFormula();
                break;
            case BLANK: // 空字符串
                value = "";
                break;
        }
        return value;
    }

    /**
     * 获取季度国内生产总值数据
     *
     * @return
     */
    public String GDPQuarter() {
        String[] columns = new String[]{"季度", "国内生产总值 绝对值(亿元)", "国内生产总值 同比增长", "第一产业 绝对值(亿元)", "第一产业 同比增长", "第二产业 绝对值(亿元)", "第二产业 同比增长", "第三产业 绝对值(亿元)", "第三产业 同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "20");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取居民消费价格指数数据（CPI）
     *
     * @return
     */
    public String CPI() {
        String[] columns = new String[]{"月份", "全国当月", "全国同比增长", "全国环比增长", "全国累计", "城市当月", "城市同比增长", "城市环比增长", "城市累计", "农村当月", "农村同比增长", "农村环比增长", "农村累计"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "19");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取工业品出厂价格指数数据
     *
     * @return
     */
    public String PPI() {
        String[] columns = new String[]{"月份", "当月", "当月同比增长", "累计"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "22");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取采购经理人指数(PMI)
     * @return
     */
    public String PMI(){
        String[] columns = new String[]{"月份", "制造业指数", "制造业同比增长", "非制造业指数", "非制造业同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "21");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取存款准备金率数据
     * @return
     */
    public String RRR(){
        String[] columns = new String[]{"公布时间", "生效时间", "大型金融机构 调整前", "大型金融机构 调整后", "大型金融机构 调整幅度", "中小型金融机构 调整前", "中小型金融机构 调整后", "中小型金融机构 调整幅度", "备注", "消息公布次日指数涨跌 上证", "消息公布次日指数涨跌 深证"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "23");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取货币供应量数据
     * @return
     */
    public String moneySupply(){
        String[] columns = new String[]{"月份", "货币和准货币(M2) 数量(亿元)", "货币和准货币(M2) 同比增长", "货币和准货币(M2) 环比增长", "货币(M1) 数量(亿元)", "货币(M1) 同比增长", "货币(M1) 环比增长", "流通中的现金(M0) 数量(亿元)", "流通中的现金(M0) 同比增长", "流通中的现金(M0) 环比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "11");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取外汇储备
     * @return
     */
    public String goldAndForeignReserve(){
        String[] columns = new String[]{"月份", "国家外汇储备(亿美元) 数值", "国家外汇储备(亿美元) 同比", "国家外汇储备(亿美元) 环比", "黄金储备(万盎司) 数值", "黄金储备(万盎司) 同比", "黄金储备(万盎司) 环比"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "16");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取工业增加值增长
     * @return
     */
    public String industrialGrowth(){
        String[] columns = new String[]{"月份", "制造业指数", "制造业同比增长", "非制造业指数", "非制造业同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "0");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取财政收入
     * @return
     */
    public String fiscalRevenue(){
        String[] columns = new String[]{"月份", "当月(亿元)", "同比增长", "环比增长", "累计(亿元)", "同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "14");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取社会消费品零售总额
     * @return
     */
    public String consumerTotal(){
        String[] columns = new String[]{"月份", "当月(亿元)", "同比增长", "环比增长", "累计(亿元)", "同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "5");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取信贷数据
     * @return
     */
    public String creditData(){
        String[] columns = new String[]{"月份", "当月(亿元)", "同比增长", "环比增长", "累计(亿元)", "同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "7");
        return EMDataCenter(params, columns);
    }

    /**
     * 获取外商直接投资数据(FDI)
     * @return
     */
    public String fdiData(){
        String[] columns = new String[]{"月份", "当月(十万元)", "同比增长", "环比增长", "累计(十万元)", "同比增长"};
        Map<String, String> params = new HashMap<>();
        params.put("type", "GJZB");
        params.put("sty", "ZGZB");
        params.put("p", "1");
        params.put("ps", "200");
        params.put("mkt", "15");
        return EMDataCenter(params, columns);
    }

    private String EMDataCenter(Map<String, String> params, String[] columns) {
        String url = "http://datainterface.eastmoney.com/EM_DataCenter/JS.aspx";
        try {
            OkHttpUtils.OkHttpResponse response = OkHttpUtils.get(url, null, params);
            String result = response.getBody();

            ArrayNode ret = mapper.createArrayNode();

            JsonNode arr = mapper.readTree(result.substring(1, result.length() - 1));
            for (JsonNode node : arr) {
                String row = node.asText();
                String[] item = row.split(",");

                ObjectNode data = ret.addObject();

                for (int i = 0, len = item.length; i < len; i++) {
                    data.put(columns[i], item[i]);
                }
            }

            return mapper.writeValueAsString(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new MarcoCN().fdiData());
    }
}
