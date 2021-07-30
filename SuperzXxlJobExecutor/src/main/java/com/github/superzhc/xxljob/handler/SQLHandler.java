package com.github.superzhc.xxljob.handler;

import com.alibaba.fastjson.JSON;
import com.github.superzhc.xxljob.handler.param.SQLAddRecordParam;
import com.github.superzhc.xxljob.handler.param.SQLConnectParam;
import com.github.superzhc.xxljob.handler.param.SQLParam2;
import com.github.superzhc.xxljob.util.db.JdbcHelper;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author superz
 * @create 2021/7/26 11:02
 */
@Component
public class SQLHandler {

    @XxlJob("sqlPreview")
    public void preview() throws Exception {
        String param = XxlJobHelper.getJobParam();
        SQLParam2 sqlParam = JSON.parseObject(param, SQLParam2.class);

        try (JdbcHelper helper = new JdbcHelper(sqlParam.getUrl(), sqlParam.getUsername(), sqlParam.getPassword())) {
            String records = helper.show(sqlParam.getNum(), sqlParam.getSql(), sqlParam.getParams());
            XxlJobHelper.handleSuccess(records);
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    @XxlJob("sqlAddRecord")
    public void add() throws Exception {
        String param = XxlJobHelper.getJobParam();
        SQLAddRecordParam sqlParam = JSON.parseObject(param, SQLAddRecordParam.class);
        try (JdbcHelper helper = new JdbcHelper(sqlParam.getUrl(), sqlParam.getUsername(), sqlParam.getPassword())) {
            int code = helper.insert(sqlParam.getTableName(), sqlParam.getFieldValues());
            if (-1 == code) {
                XxlJobHelper.handleFail("新增失败");
            } else {
                XxlJobHelper.handleSuccess("新增成功");
            }
        }
    }
}
