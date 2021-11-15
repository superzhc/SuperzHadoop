package com.github.superzhc.geo.geomesa.accumulo.source.config;

import com.github.superzhc.geo.geomesa.GeomesaAdmin;
import com.github.superzhc.geo.geomesa.source.GeomesaDataStore;
import com.github.superzhc.geo.geomesa.source.config.GeomesaSourceConfig;

/**
 * @author superz
 * @create 2021/11/15 15:59
 */
public class DockerSourceConfig extends GeomesaSourceConfig {
    @Override
    protected void init() {
        sourceParams.put("accumulo.instance.id", "accumulo");
        sourceParams.put("accumulo.zookeepers", "localhost:12181");
        sourceParams.put("accumulo.user", "root");
        sourceParams.put("accumulo.password", "GisPwd");
        sourceParams.put("accumulo.catalog", "example");
    }

    public static void main(String[] args) {
        try (GeomesaDataStore geomesaDataStore = new GeomesaDataStore(new DockerSourceConfig())) {
            GeomesaAdmin admin = new GeomesaAdmin(geomesaDataStore);
            String[] schemas = admin.list();

            if (schemas == null) {
                System.out.println("无 Schema 存在");
                return;
            }

            for (String schema : schemas) {
                System.out.println(schema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
