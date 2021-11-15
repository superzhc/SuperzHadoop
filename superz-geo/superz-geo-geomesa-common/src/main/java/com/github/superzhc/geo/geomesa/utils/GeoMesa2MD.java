package com.github.superzhc.geo.geomesa.utils;

import org.geotools.util.Classes;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * @author superz
 * @create 2021/7/21 19:54
 */
public class GeoMesa2MD {
    public static String template(SimpleFeatureType sft) {
        StringBuilder sb = new StringBuilder();
        // 标题
        sb.append("## `").append(sft.getTypeName()).append("`").append("\r\n");
        sb.append("\r\n");
        // 属性列表
        sb.append("**属性列表**").append("\r\n");
        sb.append("\r\n");
        sb.append("|属性名称|属性类型|描述|").append("\r\n");
        sb.append("|---|---|---|").append("\r\n");
        String attrTemplate = "|%s|%s| |";
        for (AttributeDescriptor attribute : sft.getAttributeDescriptors()) {
            String attributeName = attribute.getLocalName();
            String attributeType = Classes.getShortName(attribute.getType().getBinding());
            sb.append(String.format(attrTemplate, attributeName, attributeType)).append("\r\n");
        }
        sb.append("\r\n");
        // 索引信息
        sb.append("**索引信息**").append("\r\n");
        sb.append("\r\n");
        // sb.append("-").append("\r\n");
        sb.append("- ").append("时空索引的日期字段[`geomesa.index.dtg`]:").append("`" + sft.getUserData().get("geomesa.index.dtg") + "`").append("\r\n");

        Object strIndexs = sft.getUserData().get("geomesa.indices");
        if (null != strIndexs && !"".equals(strIndexs.toString())) {
            sb.append("- 索引名称列表：").append("\r\n");
            sb.append("\r\n");
            sb.append("|索引名称|\r\n|---|\r\n");
            String[] arrIndexs = strIndexs.toString().split(",");
            for (String index : arrIndexs) {
                sb.append("|").append(index).append("|").append("\r\n");
            }
        }
        sb.append("\r\n");

        return sb.toString();
    }
}
