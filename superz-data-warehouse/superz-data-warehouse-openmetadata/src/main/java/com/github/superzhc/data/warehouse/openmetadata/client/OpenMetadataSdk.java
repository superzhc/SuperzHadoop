package com.github.superzhc.data.warehouse.openmetadata.client;

import feign.Response;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.openmetadata.client.api.*;
import org.openmetadata.client.gateway.OpenMetadata;
import org.openmetadata.client.model.*;
import org.openmetadata.schema.security.client.OpenMetadataJWTClientConfig;
import org.openmetadata.schema.services.connections.metadata.OpenMetadataConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenMetadataSdk {
    public static void main(String[] args) throws Exception {
        OpenMetadataConnection openMetadataConnection = new OpenMetadataConnection();
        openMetadataConnection.setHostPort("http://127.0.0.1:8585/api");
        openMetadataConnection.setApiVersion("v1");

        openMetadataConnection.setAuthProvider(OpenMetadataConnection.AuthProvider.OPENMETADATA);
        OpenMetadataJWTClientConfig openMetadataJWTClientConfig = new OpenMetadataJWTClientConfig();
        openMetadataJWTClientConfig.setJwtToken("eyJraWQiOiJHYjM4OWEtOWY3Ni1nZGpzLWE5MmotMDI0MmJrOTQzNTYiLCJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJvcGVuLW1ldGFkYXRhLm9yZyIsInN1YiI6ImluZ2VzdGlvbi1ib3QiLCJlbWFpbCI6ImluZ2VzdGlvbi1ib3RAb3Blbm1ldGFkYXRhLm9yZyIsImlzQm90Ijp0cnVlLCJ0b2tlblR5cGUiOiJCT1QiLCJpYXQiOjE2ODQ0NjY3NjksImV4cCI6bnVsbH0.c2TeP3u29Q2wfKhFuMZLHXn1tZaofGefBB2YwpejDtLCFgsUYmgdww9j5eZmYIcxSTEHcLPqsRnutFVhZJgw3P2dnvDQT7avsKZ4gstcLaLm5JQYxCW-NQ3gB7A-YWr182DUbtG-XHHtwbPPmP9RvQnk5T2gvX2JAHbd2Vn0DkLCmuCEDQuTy30V7ucVTwAfUO3hoP30PbRGqt3VoAqbC_UoDynvhORt76jiT0s4wcs0gj3pkDXZacYd0tnX9SzYdtDLHQe1E8FvILCyHvrkOp9kmD5-NCBQzqLocLKmmnpqZK6YLgYLmNrJYEiu9HidjzMCMyZJZ0hzNAKXxyL9EA");
        openMetadataConnection.setSecurityConfig(openMetadataJWTClientConfig);

        OpenMetadata openMetadataGateway = new OpenMetadata(openMetadataConnection);

        Map<String, Object> params = new HashMap<>();

//        BotsApi botsApi = openMetadataGateway.buildClient(BotsApi.class);
//        params.clear();
//        params.put("limit", 100);
//        BotList botList = botsApi.listBots(params);
//        List<Bot> bots = botList.getData();
//        if (null != bots && bots.size() > 0) {
//            System.out.println(bots.get(0));
//        }

//        ChartsApi chartsApi = openMetadataGateway.buildClient(ChartsApi.class);
//        params.clear();
//        ChartList chartList = chartsApi.listCharts(params);
//        List<Chart> charts = chartList.getData();
//        System.out.println(null == charts ? null : charts.size());

//        // Dashboards API
//        DashboardsApi dashboardApi = openMetadataGateway.buildClient(DashboardsApi.class);

//        DatabasesApi databasesApi = openMetadataGateway.buildClient(DatabasesApi.class);
//        params.clear();
//        DatabaseList databaseList = databasesApi.listDatabases(params);
//        List<Database> databases = databaseList.getData();
//        if (null != databases && databases.size() > 0) {
//            for (Database database : databases) {
//                System.out.println(database);
//            }
//        }

//        DatabaseSchemasApi databaseSchemasApi=openMetadataGateway.buildClient(DatabaseSchemasApi.class);
//        params.clear();
//        DatabaseSchemaList databaseSchemaList=databaseSchemasApi.listDBSchemas(params);
//        List<DatabaseSchema> databaseSchemas=databaseSchemaList.getData();
//        if(null!=databaseSchemas&&databaseSchemas.size()>0){
//            for (DatabaseSchema databaseSchema:databaseSchemas){
//                System.out.println(databaseSchema);
//                System.out.println("===================华丽分割线=============================");
//            }
//        }

//        // Tables API
//        TablesApi tablesApi = openMetadataGateway.buildClient(TablesApi.class);
//        params.clear();
//        TableList tableList = tablesApi.listTables(params);
//        List<Table> tables = tableList.getData();
//        if (null != tables && tables.size() > 0) {
//            for (Table table : tables) {
//                System.out.println(table);
//                System.out.println("=======================华丽分割线===============================");
//            }
//        }

        DatabaseServicesApi databaseServicesApi = openMetadataGateway.buildClient(DatabaseServicesApi.class);
//        params.clear();
//        DatabaseServiceList databaseServiceList=databaseServicesApi.listDatabaseServices(params);
//        List<DatabaseService> databaseServices=databaseServiceList.getData();
//        if(null!=databaseServices&&databaseServices.size()>0){
//            for(DatabaseService databaseService:databaseServices){
//                System.out.println(databaseService);
//                System.out.println("=======================华丽分割线===============================");
//            }
//        }

        CreateDatabaseService createDatabaseService = new CreateDatabaseService();

        Map<String, Object> dbConnectionConfig = new HashMap<>();
        dbConnectionConfig.put("type", "Mysql");
        dbConnectionConfig.put("scheme", "mysql+pymysql");
        dbConnectionConfig.put("hostPort", "10.90.255.79:3306");
        dbConnectionConfig.put("username", "root");
        dbConnectionConfig.put("password", "123456");
        dbConnectionConfig.put("databaseSchema", "xgit");
        dbConnectionConfig.put("connectionOptions", new HashMap<>());
        dbConnectionConfig.put("connectionArguments", new HashMap<>());
        dbConnectionConfig.put("supportsMetadataExtraction", true);
        dbConnectionConfig.put("supportsDBTExtraction", true);
        dbConnectionConfig.put("supportsProfiler", true);
        dbConnectionConfig.put("supportsQueryComment", true);
        DatabaseConnection dbConnection = new DatabaseConnection().config(dbConnectionConfig);

        createDatabaseService.connection(dbConnection)
                .name("code_mysql")
                .displayName("MySql连接")
                .description("generate by code")
                .serviceType(CreateDatabaseService.ServiceTypeEnum.MYSQL)
        ;
        DatabaseService databaseService = databaseServicesApi.createDatabaseService(createDatabaseService);
        System.out.println(databaseService);

//        // Users API
//        UsersApi usersApi = openMetadataGateway.buildClient(UsersApi.class);
//
//        // ElasticSearch API
//        ElasticSearchApi esApi = openMetadataGateway.buildClient(ElasticSearchApi.class);
//        Response response = esApi.searchEntitiesWithQuery("", "pipeline_search_index", false, 0, 0, null, null, true, "{\"query\":{\"bool\":{}}}", null, false, null);
//        String data = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8);

    }
}
