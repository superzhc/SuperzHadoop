package com.github.superzhc.geo.geomesa;

import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author superz
 * @create 2021/8/12 16:54
 */
public class GeomesaQuery {
    private static final Logger log = LoggerFactory.getLogger(GeomesaQuery.class);

    private GeomesaDataStore dataStore;
    /* 默认查询数据的条数 */
    private Integer number = null;

    public GeomesaQuery(GeomesaDataStore dataStore) {
        this(dataStore, null);
    }

    public GeomesaQuery(GeomesaDataStore dataStore, Integer number) {
        this.dataStore = dataStore;
        this.number = number;
    }

    public List<Map<String, Object>> scan(String schema) {
        return query(schema);
    }

    public List<Map<String, Object>> scan(String schema, Integer number, String sortField, String sortOrder) {
        return query(schema, (String) null, number, sortField, sortOrder);
    }

    public List<Map<String, Object>> query(String schema) {
        return query(schema, number, null, null);
    }

    public List<Map<String, Object>> query(String schema, Integer number, String sortField, String sortOrder) {
        return query(schema, (String) null, number, sortField, sortOrder);
    }

    public List<Map<String, Object>> query(String schema, QueryWrapper queryWrapper) {
        return query(schema, queryWrapper, number, null, null);
    }

    public List<Map<String, Object>> query(String schema, String ecql) {
        return query(schema, ecql, number, null, null);
    }

    public List<Map<String, Object>> query(String schema, QueryWrapper queryWrapper, Integer number, String sortField, String sortOrder) {
        return query(schema, null == queryWrapper ? null : queryWrapper.getECQL(), number, sortField, sortOrder);
    }

    public List<Map<String, Object>> query(String schema, String ecql, Integer number, String sortField, String sortOrder) {
        try {
            SimpleFeatureType sft = dataStore.getDataStore().getSchema(schema);
            if (null == sft) {
                throw new RuntimeException("schema[" + schema + "] not exist");
            }
            List<String> attributeNames = sft.getAttributeDescriptors().stream().map(attributeDescriptor -> attributeDescriptor.getLocalName()).collect(Collectors.toList());

            List<Map<String, Object>> result = query(schema, ecql, number, sortField, sortOrder, new Function<SimpleFeature, Map<String, Object>>() {
                @Override
                public Map<String, Object> apply(SimpleFeature feature) {
                    if (null == feature) {
                        return null;
                    }

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
                    return map;
                }
            });

            return result;
        } catch (IOException e) {
            log.error("Geomesa 查询异常", e);
        }
        return null;
    }

    public <T> List<T> scan(String schema, Class<T> clazz) {
        return query(schema, clazz);
    }

    public <T> List<T> query(String schema, Class<T> clazz) {
        return query(schema, number, null, null, clazz);
    }

    public <T> List<T> query(String schema, Integer number, String sortField, String sortOrder, Class<T> clazz) {
        return query(schema, (String) null, number, sortField, sortOrder, clazz);
    }

    public <T> List<T> query(String schema, QueryWrapper queryWrapper, Class<T> clazz) {
        return query(schema, queryWrapper.getECQL(), number, null, null, clazz);
    }

    public <T> List<T> query(String schema, String ecql, Class<T> clazz) {
        return query(schema, ecql, number, null, null, clazz);
    }

    public <T> List<T> query(String schema, QueryWrapper queryWrapper, Integer number, String sortField, String sortOrder, Class<T> clazz) {
        return query(schema, queryWrapper.getECQL(), number, sortField, sortOrder, clazz);
    }

    public <T> List<T> query(String schema, String ecql /*QueryWrapper queryWrapper*/, Integer number, String sortField, String sortOrder, Class<T> clazz) {
        try {
            SimpleFeatureType sft = dataStore.getDataStore().getSchema(schema);
            if (null == sft) {
                throw new RuntimeException("schema[" + schema + "] not exist");
            }
            List<String> attributeNames = sft.getAttributeDescriptors().stream().map(attributeDescriptor -> attributeDescriptor.getLocalName()).collect(Collectors.toList());

            List<T> result = query(schema, ecql, number, sortField, sortOrder, new Function<SimpleFeature, T>() {
                @Override
                public T apply(SimpleFeature feature) {
                    try {
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
                        return obj;
                    } catch (Exception e) {
                        log.error("Geomesa 转实体异常", e);
                        return null;
                    }
                }
            });
            return result;
        } catch (Exception e) {
            log.error("Geomesa 查询异常", e);
        }
        return null;
    }

    public <T> List<T> query(String schema, String ecql, Integer number, String sortField, String sortOrder, Function<SimpleFeature, T> function) {
        try {
            Query query;
            if (StringUtils.isBlank(ecql)) {
                query = new Query(schema);
            } else {
                query = new Query(schema, ECQL.toFilter(ecql));
            }

            // 设置返回数据的数量
            if (null != number && number > 0) {
                query.setMaxFeatures(number);
            }

            // 排序
            if (StringUtils.isNotBlank(sortField)) {
                FilterFactoryImpl ff = new FilterFactoryImpl();
                SortBy sort;
                if ("desc".equalsIgnoreCase(sortOrder)) {
                    sort = new SortByImpl(ff.property(sortField), SortOrder.DESCENDING);
                } else {
                    sort = new SortByImpl(ff.property(sortField), SortOrder.ASCENDING);
                }
                query.setSortBy(new SortBy[]{sort});
            }

            /* 2021年9月6日 note org.locationtech.geomesa.utils.audit.AuditLogger$ 打印查询的相关参数 */
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getDataStore().getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                if (!reader.hasNext()) {
                    return null;
                }

                List<T> result = new ArrayList<>();

//                SimpleFeatureType sft = reader.getFeatureType();
//                List<String> attributeNames = sft.getAttributeDescriptors().stream().map(attributeDescriptor -> attributeDescriptor.getLocalName()).collect(Collectors.toList());

                Long count = 0L;
                while (reader.hasNext()) {
                    count++;
                    SimpleFeature feature = reader.next();

                    T obj = function.apply(feature);
                    if (null != obj) {
                        result.add(obj);
                    }
                }
                log.info("deal data count:{}", count);
                return result;
            }
        } catch (CQLException | IOException e) {
            log.error("Geomesa 查询异常", e);
        }
        return null;
    }

    public Long count(String schema, String ecql) {
        try {
            Query query;
            if (StringUtils.isBlank(ecql)) {
                query = new Query(schema);
            } else {
                query = new Query(schema, ECQL.toFilter(ecql));
            }

            /* 2021年9月6日 note org.locationtech.geomesa.utils.audit.AuditLogger$ 打印查询的相关参数 */
            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getDataStore().getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                if (!reader.hasNext()) {
                    return null;
                }

                Long count = 0L;
                while (reader.hasNext()) {
                    SimpleFeature feature = reader.next();

                    if (null == feature) {
                        continue;
                    }

                    count++;
                }
                return count;
            }
        } catch (CQLException | IOException e) {
            log.error("Geomesa 查询数据量异常", e);
        }
        return null;
    }

    public List<Object> distinct(String schema, String ecql, String... attributeNames) {
        try {
            if (null == attributeNames || attributeNames.length == 0) {
                throw new RuntimeException("Distinct 属性不允许为空");
            }

            Query query;
            if (StringUtils.isBlank(ecql)) {
                query = new Query(schema);
            } else {
                query = new Query(schema, ECQL.toFilter(ecql));
            }

            try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getDataStore().getFeatureReader(query, Transaction.AUTO_COMMIT)) {
                if (!reader.hasNext()) {
                    return null;
                }

                Set<String> keys = new HashSet<>();
                List<Object> result = new ArrayList<>();

                Long count = 0L;
                while (reader.hasNext()) {
                    count++;

                    SimpleFeature feature = reader.next();
                    if (null == feature) {
                        continue;
                    }

                    String key;
                    Object val;
                    if (attributeNames.length > 1) {
                        StringBuilder transformKey = new StringBuilder();
                        Map<String, Object> originValue = new HashMap<>();
                        for (String name : attributeNames) {
                            Object value = feature.getAttribute(name);
                            if (value instanceof Geometry) {
                                // 转换成 WTK 格式数据
                                value = ((Geometry) value).toText();
                            }

                            transformKey.append(",").append(value);
                            originValue.put(name, value);
                        }
                        key = transformKey.toString();
                        val = originValue;
                    } else {
                        Object value = feature.getAttribute(attributeNames[0]);
                        if (value instanceof Geometry) {
                            // 转换成 WTK 格式数据
                            value = ((Geometry) value).toText();
                        }

                        key = String.valueOf(value);
                        val = value;
                    }

                    if (!keys.contains(key)) {
                        keys.add(key);
                        result.add(val);
                    }

                }
                log.info("deal data count:{}", count);
                return result;
            }
        } catch (CQLException | IOException e) {
            log.error("Geomesa 查询数据量异常", e);
        }
        return null;
    }
}
