package com.github.superzhc.data.covid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.common.utils.PropertiesUtils;
import com.github.superzhc.data.tushare.TusharePro;
import com.github.superzhc.data.tushare.TushareResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author superz
 * @create 2021/12/31 10:18
 */
public class Covid {
    private TusharePro tusharePro;

    public Covid(String token) {
        tusharePro = new TusharePro(token);
    }

    /**
     * 新冠状肺炎感染人数
     * <p>
     * 获取新冠状肺炎疫情感染人数统计数据
     *
     * @param areaName  地区名称
     * @param level     级别：2-中国内地，3-省级，4-地区市级别
     * @param annDate   公告日期，格式形如：20211231
     * @param startDate 查询开始日期，格式形如：20211231
     * @param endDate   查询结束日期，格式形如：20211231
     * @return
     */
    public /*List<Map<String, Object>>*/ void numbersOfCovid(String areaName, String level, String annDate, String startDate, String endDate) {
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(areaName)) {
            params.put("area_name", areaName);
        }
        if (StringUtils.isNotBlank(level)) {
            params.put("level", level);
        }
        if (StringUtils.isNotBlank(annDate)) {
            params.put("ann_date", annDate);
        }
        if (StringUtils.isNotBlank(startDate)) {
            params.put("start_date", startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            params.put("end_date", endDate);
        }

        Integer limit = 2000;
        Integer offset = 0;

        Boolean hasMore = true;
        while (hasMore) {

            // 分页进行查询
            params.put("limit", String.valueOf(limit));
            params.put("offset", String.valueOf(offset));

            hasMore = tusharePro.execute0(
                    "ncov_num",
                    params,
                    "ann_date,area_name,parent_name,level,confirmed_num,suspected_num,confirmed_num_now,suspected_num_now,cured_num,dead_num",
                    new Function<TushareResponse.Data, Boolean>() {
                        @Override
                        public Boolean apply(TushareResponse.Data data) {

                            // do

                            return data.getHasMore();
                        }
                    });

            offset += limit;
        }
    }

    public static void main(String[] args) {
        PropertiesUtils.read("application.properties");
        Covid covid = new Covid(PropertiesUtils.get("tushare.token"));

        covid.numbersOfCovid(null, null, null, null, null);
    }
}
