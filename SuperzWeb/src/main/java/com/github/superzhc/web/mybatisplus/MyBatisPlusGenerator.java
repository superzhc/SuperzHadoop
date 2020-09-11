package com.github.superzhc.web.mybatisplus;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MyBatisPlus代码生成器
 * 2020年09月10日 superz add
 */
public class MyBatisPlusGenerator {
    public static void main(String[] args) {
        String projectPath = "D:/superz/FileParsing/SuperzHadoop/SuperzWeb/";
        String moduleName = "";//scanner("模块名");
        String[] tableNames = scanner("表名，多个英文逗号分割").split(",");

        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(initGlobalConfig(projectPath));
        autoGenerator.setDataSource(initDataSourceConfig());
        autoGenerator.setPackageInfo(initPackageConfig(moduleName));
        autoGenerator.setCfg(initInjectionConfig(projectPath, moduleName));
        autoGenerator.setTemplate(initTemplateConfig());
        autoGenerator.setStrategy(initStrategyConfig(tableNames));
        autoGenerator.setTemplateEngine(new VelocityTemplateEngine());
        autoGenerator.execute();
    }

    /**
     * 读取控制台内容信息
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String next = scanner.next();
            if (StrUtil.isNotEmpty(next)) {
                return next;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 初始化全局配置
     */
    private static GlobalConfig initGlobalConfig(String projectPath) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("superz");
        globalConfig.setOpen(false);
        globalConfig.setSwagger2(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setFileOverride(true);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setEntityName("%s");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");
        return globalConfig;
    }

    /**
     * 初始化数据源配置
     */
    private static DataSourceConfig initDataSourceConfig() {
        Props props = new Props("generatorConfig.properties");
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(props.getStr("db.url"));
        dataSourceConfig.setDriverName(props.getStr("db.driver"));
        dataSourceConfig.setUsername(props.getStr("db.username"));
        dataSourceConfig.setPassword(props.getStr("db.password"));
        return dataSourceConfig;
    }

    /**
     * 初始化包配置
     */
    private static PackageConfig initPackageConfig(String moduleName) {
        Props props = new Props("generatorConfig.properties");
        PackageConfig packageConfig = new PackageConfig();
//        packageConfig.setModuleName(moduleName);
        packageConfig.setParent(props.getStr("package.base"));
        packageConfig.setEntity("model");
        return packageConfig;
    }

    /**
     * 初始化模板配置
     */
    private static TemplateConfig initTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        //可以对controller、service、entity模板进行配置
        //mapper.xml模板需单独配置
        //templateConfig.setXml(null);
        templateConfig.disable(TemplateType.XML,TemplateType.CONTROLLER);
        return templateConfig;
    }

    /**
     * 初始化策略配置
     */
    private static StrategyConfig initStrategyConfig(String[] tableNames) {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setEntityLombokModel(false);//关闭Lombok
        strategyConfig.setRestControllerStyle(true);
        //当表名中带*号时可以启用通配符模式
        if (tableNames.length == 1 && tableNames[0].contains("*")) {
            String[] likeStr = tableNames[0].split("_");
            String likePrefix = likeStr[0] + "_";
            strategyConfig.setLikeTable(new LikeTable(likePrefix));
        } else {
            strategyConfig.setInclude(tableNames);
        }
        return strategyConfig;
    }

    /**
     * 初始化自定义配置
     */
    private static InjectionConfig initInjectionConfig(String projectPath, String moduleName) {
        // 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // 可用于自定义属性
            }
        };
        // 模板引擎是Velocity
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
//                        + moduleName + "/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        return injectionConfig;
    }
}
