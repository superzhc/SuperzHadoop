package com.github.superzhc.data.warehouse.openmetadata.client;

import feign.Response;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.openmetadata.client.api.DashboardsApi;
import org.openmetadata.client.api.ElasticSearchApi;
import org.openmetadata.client.api.TablesApi;
import org.openmetadata.client.api.UsersApi;
import org.openmetadata.client.gateway.OpenMetadata;
import org.openmetadata.schema.security.client.OpenMetadataJWTClientConfig;
import org.openmetadata.schema.services.connections.metadata.OpenMetadataConnection;

public class OpenMetadataSdk {
    public static void main(String[] args) throws Exception {
        OpenMetadataConnection openMetadataConnection = new OpenMetadataConnection();
        openMetadataConnection.setHostPort("http://10.90.18.142:8585/api");
        openMetadataConnection.setApiVersion("v1");

        openMetadataConnection.setAuthProvider(OpenMetadataConnection.AuthProvider.OPENMETADATA);
        OpenMetadataJWTClientConfig openMetadataJWTClientConfig = new OpenMetadataJWTClientConfig();
        openMetadataJWTClientConfig.setJwtToken("");
        openMetadataConnection.setSecurityConfig(openMetadataJWTClientConfig);

        OpenMetadata openMetadataGateway = new OpenMetadata(openMetadataConnection);

        // Dashboards API
        DashboardsApi dashboardApi = openMetadataGateway.buildClient(DashboardsApi.class);

        // Tables API
        TablesApi tablesApiClient = openMetadataGateway.buildClient(TablesApi.class);

        // Users API
        UsersApi usersApi = openMetadataGateway.buildClient(UsersApi.class);

        // ElasticSearch API
        ElasticSearchApi esApi = openMetadataGateway.buildClient(ElasticSearchApi.class);
        Response response = esApi.searchEntitiesWithQuery("", "pipeline_search_index", false, 0, 0, null, null, true, "{\"query\":{\"bool\":{}}}", null, false, null);
        String data = IOUtils.toString(response.body().asInputStream(), Charsets.UTF_8);

    }
}
