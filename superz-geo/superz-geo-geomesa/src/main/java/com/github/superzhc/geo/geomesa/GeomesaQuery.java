package com.github.superzhc.geo.geomesa;

import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.github.superzhc.geo.geomesa.source.config.Cloud4ControlSourceConfig;
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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
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
    /* 默认查询数据的条数 */
    private Integer number = null;

    public GeomesaQuery(GeomesaDataStore dataStore) {
        this(dataStore, null);
    }

    public GeomesaQuery(GeomesaDataStore dataStore, Integer number) {
        this.dataStore = dataStore;
        this.number = number;
    }

    public List<Map<String, Object>> query(String schema) {
        return query(schema, number, null, null);
    }

    public List<Map<String, Object>> query(String schema, Integer number, String sortField, String sortOrder) {
        return query(schema, null, number, sortField, sortOrder);
    }

    public List<Map<String, Object>> query(String schema, QueryWrapper queryWrapper) {
        return query(schema, queryWrapper, number, null, null);
    }

    public List<Map<String, Object>> query(String schema, QueryWrapper queryWrapper, Integer number, String sortField, String sortOrder) {
        try {
            Query query;
            if (null == queryWrapper) {
                query = new Query(schema);
            } else {
                query = new Query(schema, ECQL.toFilter(queryWrapper.getECQL()));
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
        return query(schema, number, null, null, clazz);
    }

    public <T> List<T> query(String schema, Integer number, String sortField, String sortOrder, Class<T> clazz) {
        return query(schema, null, number, sortField, sortOrder, clazz);
    }

    public <T> List<T> query(String schema, QueryWrapper queryWrapper, Class<T> clazz) {
        return query(schema, queryWrapper, number, null, null, clazz);
    }

    public <T> List<T> query(String schema, QueryWrapper queryWrapper, Integer number, String sortField, String sortOrder, Class<T> clazz) {
        try {
            Query query;
            if (null == queryWrapper) {
                query = new Query(schema);
            } else {
                query = new Query(schema, ECQL.toFilter(queryWrapper.getECQL()));
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
            GeomesaQuery geomesaQuery = new GeomesaQuery(geomesaDataStore, 1);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.during("timestamp", LocalDateTime.of(2021, 8, 4, 0, 0), LocalDateTime.of(2021, 8, 4, 23, 59))
                    .eq("plate_number", "苏A19096");
            List<Map<String, Object>> lst = geomesaQuery.query("bsm.gps", queryWrapper, 100, "timestamp", "desc");
            System.out.println(lst);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
