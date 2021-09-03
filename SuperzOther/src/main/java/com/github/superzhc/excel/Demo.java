package com.github.superzhc.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.github.superzhc.excel.entity.PointData;
import com.github.superzhc.excel.entity.PointData2;
import com.github.superzhc.excel.entity.TDeviceInfo;
import com.github.superzhc.excel.listener.PointListener;
import com.github.superzhc.excel.listener.PointListener2;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author superz
 * @create 2021/8/12 17:23
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        String csvPath = "D:\\downloads\\WeChat\\WeChat Files\\zc1013849463\\FileStorage\\File\\2021-08\\t_device_info.csv";
        List<TDeviceInfo> lst = new CsvToBeanBuilder(new FileReader(csvPath)).withType(TDeviceInfo.class).build().parse();
        Map<String, TDeviceInfo> ipMap = new HashMap<>(lst.size());
        for (TDeviceInfo tDeviceInfo : lst) {
            String[] ss = tDeviceInfo.getDesc().split("-");
            String ip = ss[ss.length - 1];
            if (isIp(ip)) {
                ipMap.put(ip, tDeviceInfo);
            }
        }
//        System.out.println(ipMap.size());
//        System.out.println(ipMap);

        String path = "C:\\Users\\superz\\Desktop\\泰兴安监点位-场景调试进展-0618.xlsx";
        EasyExcel.read(path, PointData.class, /*new PointListener()*/
                new AnalysisEventListener<PointData>() {
                    private final String sqlTemplate = "INSERT INTO `cloud_db`.`t_pole`(`id`, `region_id`, `pole_no`, `pole_type`, `location_alias`, `longitude`, `latitude`, `heading`, `del_flag`, `update_time`, `create_time`) VALUES (%d, %s, '%s', 1, '%s', %f, %f, 90.000000000000000, 0, NULL, '2021-08-13 14:00:54');";
                    private StringBuilder others = new StringBuilder();
                    private String location;
                    private String baoganxiang;

                    private Map<String, List<String>> map = new HashMap<>();

                    @Override
                    public void invoke(PointData data, AnalysisContext context) {
                        // 填充“点位位置”的合并列数据
                        if (StringUtils.isBlank(location) || StringUtils.isNotBlank(data.getLocation())) {
                            location = data.getLocation();
                        } else {
                            data.setLocation(location);
                        }

                        // 填充“抱杆箱内设备”的合并列数据
                        if (StringUtils.isBlank(baoganxiang) || StringUtils.isNotBlank(data.getBaoganxiang())) {
                            baoganxiang = data.getBaoganxiang();
                        } else {
                            data.setBaoganxiang(baoganxiang);
                        }

                        // 替换掉换行
                        String location2 = data.getLocation().replaceAll("\r\n|\r|\n", "-");
                        if (StringUtils.isNotBlank(data.getBaoganxiang()) && data.getBaoganxiang().lastIndexOf("（") > 0) {
                            location2 += data.getBaoganxiang().substring(data.getBaoganxiang().lastIndexOf("（"));
                        }

                        if (StringUtils.isBlank(data.getNewIp())) {
//                            System.out.printf("设备[%s-%s]无Ip数据\n", location, data.getGanjian(), data.getNewIp());
                            others.append(String.format("设备[%s-%s]无Ip数据\n", location2, data.getGanjian(), data.getNewIp()));
                            return;
                        }

                        if (data.getLocation().contains("拆除")) {
//                            System.out.printf("设备[%s-%s,ip=%s]已拆除\n", location2, data.getGanjian(), data.getNewIp());
                            others.append(String.format("设备[%s-%s,ip=%s]已拆除\n", location2, data.getGanjian(), data.getNewIp()));
                            return;
                        }

                        List<String> ips = map.getOrDefault(location2, new ArrayList<>());
                        ips.add(data.getNewIp());
                        map.put(location2, ips);

//                        TDeviceInfo info = ipMap.get(data.getNewIp());
                        // System.out.printf("设备[%s-%s,ip=%s]{备注：%s}:[%f,%f]\n", location, data.getGanjian(), data.getNewIp(), info.getDesc(), info.getLongitude(), info.getLatitude());

                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        int id = 2;
                        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                            TDeviceInfo info = null;
                            List<String> ips = entry.getValue();

                            List<String> devices=new ArrayList<>();
                            for (String ip : ips) {
//                                if (ipMap.containsKey(ip)) {
//                                    info = ipMap.get(ip);
//                                    break;
//                                }

                                if (ipMap.containsKey(ip)){
                                    devices.add(ipMap.get(ip).getId());
                                }
                            }
                            System.out.println(entry.getKey()+":"+devices);

//                            if (null == info) {
//                                // System.out.printf("设备[%s]的IP{%s}无经纬度信息\n", entry.getKey(), ips);
//                                others.append(String.format("设备[%s]的IP{%s}无经纬度信息\n", entry.getKey(), ips));
//                                continue;
//                            }

//                            String poleNo = "POLE-00" + id;
//                            String sql = String.format(sqlTemplate, id, info.getRegionId(), poleNo, entry.getKey()/*info.getName()*/, info.getLongitude(), info.getLatitude());
//                            System.out.println(sql);
//
//                            id++;
                        }


//                        System.out.println(others.toString());
                    }
                }
        )
                .extraRead(CellExtraTypeEnum.MERGE)
                .headRowNumber(2)
                .sheet().doRead();
//        EasyExcel.read(path,new PointListener2()).sheet().doRead();
    }

    static boolean isIp(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }
}
