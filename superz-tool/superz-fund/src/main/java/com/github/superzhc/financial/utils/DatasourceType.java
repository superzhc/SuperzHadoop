package com.github.superzhc.financial.utils;

public enum DatasourceType {

    Sina("Sina"), CS("中证"), Eastmoney("Eastmoney");

    private String type;

    DatasourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static DatasourceType fromType(String type) {
        DatasourceType[] datasourceTypes = values();
        for (DatasourceType datasourceType : datasourceTypes) {
            if (datasourceType.getType().equals(type)) {
                return datasourceType;
            }
        }
        return null;
    }
}
