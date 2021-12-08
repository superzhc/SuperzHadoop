package com.github.superzhc.data.warehouse.bigdata.executor;

import com.github.superzhc.data.warehouse.bigdata.workflow.WorkflowItem;

import java.util.List;

/**
 * 执行器
 *
 * @author superz
 * @create 2021/12/8 11:58
 */
public interface Executor {
    void execute(List<WorkflowItem> startWrokflowItmes);
}
