package com.github.superzhc.sql.parser.druid.lineage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author superz
 * @create 2022/6/16 15:28
 **/
@Deprecated
public class DataLineage {
    private List<String> sources;
    private String target;
    //private List<String> fields;

    public DataLineage addSource(String source) {
        getSources().add(source);
        return this;
    }

    public DataLineage addSources(List<String> anotherSources) {
        getSources().addAll(anotherSources);
        return this;
    }

    public List<String> getSources() {
        if (null == sources) {
            sources = new ArrayList<>();
        }
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

//    public List<String> getFields() {
//        return fields;
//    }
//
//    public void setFields(List<String> fields) {
//        this.fields = fields;
//    }


    @Override
    public String toString() {
        return "DataLineage{" +
                "sources=" + sources +
                ", target='" + target + '\'' +
                '}';
    }
}
