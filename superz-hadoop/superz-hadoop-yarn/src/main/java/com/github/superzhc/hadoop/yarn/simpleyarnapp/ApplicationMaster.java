package com.github.superzhc.hadoop.yarn.simpleyarnapp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.NMClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Records;

/**
 * 2020年06月19日 superz add
 */
public class ApplicationMaster
{
    public static void main(String[] args) throws Exception {
        final String command=args[0];
        final int n=Integer.valueOf(args[1]);

        // 初始化客户端到ResourceManager和NodeManagers
        Configuration conf=new YarnConfiguration();

        AMRMClient<AMRMClient.ContainerRequest> rmClient=AMRMClient.createAMRMClient();
        rmClient.init(conf);
        rmClient.start();

        NMClient nmClient=NMClient.createNMClient();
        nmClient.init(conf);
        nmClient.start();

        // Register with ResourceManager
        System.out.println("registerApplicationMaster 0");
        rmClient.registerApplicationMaster("",0,"");
        System.out.println("registerApplicationMaster 1");

        // Priority for worker containers - priorities are intra-application
        Priority priority= Records.newRecord(Priority.class);
        priority.setPriority(0);

        // Resource requirements for worker containers
        Resource capability=Records.newRecord(Resource.class);
//        capability.setMemory();
    }
}
