package com.github.superzhc.gis.geomesa;

import com.github.superzhc.gis.geomesa.source.GeomesaDataStore;
import com.github.superzhc.gis.geomesa.source.config.Cloud4ControlSourceConfig;
import com.google.common.base.CaseFormat;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2021/8/12 16:54
 */
public class GeomesaQuery {
    private GeomesaDataStore dataStore;
    private Integer number = null;

    public GeomesaQuery(GeomesaDataStore dataStore) {
        this(dataStore, null);
    }

    public GeomesaQuery(GeomesaDataStore dataStore, Integer number) {
        this.dataStore = dataStore;
        this.number = number;
    }

    public List<Map<String, Object>> query(String schema) {
        return query(schema, (QueryWrapper) null, (Integer) null);
    }

    public List<Map<String, Object>> query(String schema, QueryWrapper queryWrapper) {
        return query(schema, queryWrapper, number);
    }

    public List<Map<String, Object>> query(String schema, QueryWrapper queryWrapper, Integer number) {
        try {
            Query query;
            if (null == queryWrapper) {
                query = new Query(schema);
            } else {
                query = new Query(schema, ECQL.toFilter(queryWrapper.getECQL()));
            }
            if (null != number && number > 0) {
                // 设置最大返回条目
                query.setMaxFeatures(number);
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getDataStore().getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                if (!reader.hasNext()) {
                    return null;
                }

                List<Map<String, Object>> result = new ArrayList<>();

                SimpleFeatureType sft = reader.getFeatureType();
                List<String> attributeNames = sft.getAttributeDescriptors().stream().map(attributeDescriptor -> attributeDescriptor.getLocalName()).collect(Collectors.toList());

                while (reader.hasNext()) {
                    SimpleFeature feature = reader.next();


                    Map<String, Object> map = new HashMap<>();

                    String fid = feature.getID();
                    if (feature.getUserData().containsKey(Hints.PROVIDED_FID)) {
                        fid = (String) feature.getUserData().get(Hints.PROVIDED_FID);
                    }
                    map.put("fid", fid);

                    for (String name : attributeNames) {
                        Object value = feature.getAttribute(name);
                        if (value instanceof Geometry) {
                            // 转换成 WTK 格式数据
                            value = ((Geometry) value).toText();
                        }
                        map.put(name, value);
                    }

                    result.add(map);
                }
                return result;
            }
        } catch (CQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> query(String schema, Class<T> clazz) {
        return query(schema, null, null, clazz);
    }

    public <T> List<T> query(String schema, QueryWrapper queryWrapper, Class<T> clazz) {
        return query(schema, queryWrapper, null, clazz);
    }

    public <T> List<T> query(String schema, QueryWrapper queryWrapper, Integer number, Class<T> clazz) {
        try {
            Query query;
            if (null == queryWrapper) {
                query = new Query(schema);
            } else {
                query = new Query(schema, ECQL.toFilter(queryWrapper.getECQL()));
            }
            if (null != number && number > 0) {
                // 设置最大返回条目
                query.setMaxFeatures(number);
            }
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getDataStore().getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                if (!reader.hasNext()) {
                    return null;
                }

                List<T> result = new ArrayList<>();

                SimpleFeatureType sft = reader.getFeatureType();
                List<String> attributeNames = sft.getAttributeDescriptors().stream().map(attributeDescriptor -> attributeDescriptor.getLocalName()).collect(Collectors.toList());

                while (reader.hasNext()) {
                    SimpleFeature feature = reader.next();


                    boolean emptyConstructor = false;
                    Constructor[] constructors = clazz.getDeclaredConstructors();
                    for (Constructor constructor : constructors) {
                        if (constructor.getParameterCount() == 0) {
                            emptyConstructor = true;
                            break;
                        }
                    }
                    if (!emptyConstructor) {
                        throw new RuntimeException("无空构造函数,无法转换成实体" + clazz.getName());
                    }

                    T obj = clazz.newInstance();

                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        int mod = field.getModifiers();
                        // 静态变量不做处理，一般Bean中不存在静态变量
                        if (Modifier.isStatic(mod)) {
                            continue;
                        }

                        if ("id".equals(field.getName()) || "fid".equals(field.getName())) {
                            String fid = feature.getID();
                            if (feature.getUserData().containsKey(Hints.PROVIDED_FID)) {
                                fid = (String) feature.getUserData().get(Hints.PROVIDED_FID);
                            }
                            field.setAccessible(true);
                            field.set(obj, fid);
                        } else if (attributeNames.contains(field.getName())) {
                            Object value = feature.getAttribute(field.getName());
                            if (value instanceof Geometry) {
                                // 转换成 WTK 格式数据
                                value = ((Geometry) value).toText();
                            }

                            field.setAccessible(true);
                            field.set(obj, value);
                        } else if (attributeNames.contains(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()))) {
                            // 支持驼峰式命名的赋值
                            Object value = feature.getAttribute(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()));
                            if (value instanceof Geometry) {
                                // 转换成 WTK 格式数据
                                value = ((Geometry) value).toText();
                            }

                            field.setAccessible(true);
                            field.set(obj, value);
                        }
                    }

                    result.add(obj);
                }
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(new Cloud4ControlSourceConfig())) {
            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore, 10);
            List<Map<String, Object>> lst = geomesaQuery.query("adas.dsm.alarm");
            System.out.println(lst);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
