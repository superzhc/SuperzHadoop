package com.github.superzhc.data.warehouse.bigdata.workflow;

import java.util.List;
import java.util.Map;

/**
 * 流程元素
 *
 * @author superz
 * @create 2021/12/8 11:39
 */
public class WorkflowItem {
    /* 流程唯一标识 */
    private String uid;
    /* 流程名称 */
    private String name;
    /* 流程操作算子 */
    // 源：file、jdbc...
    // 转换：map、flatmap...
    // 行动：reduce、count...
    private String operator;
    /* 操作算子参数 */
    private Map<String, Object> params;
    /* 父流程 */
    private List<WorkflowItem> parents;
    /* 子流程 */
    private List<WorkflowItem> children;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<WorkflowItem> getParents() {
        return parents;
    }

    public void setParents(List<WorkflowItem> parents) {
        this.parents = parents;
    }

    public List<WorkflowItem> getChildren() {
        return children;
    }

    public void setChildren(List<WorkflowItem> children) {
        this.children = children;
    }
}
