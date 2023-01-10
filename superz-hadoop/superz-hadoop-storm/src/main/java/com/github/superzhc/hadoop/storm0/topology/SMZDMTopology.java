package com.github.superzhc.hadoop.storm0.topology;

import com.github.superzhc.hadoop.storm0.bolt.DealBolt;
import com.github.superzhc.hadoop.storm0.bolt.PrintBolt;
import com.github.superzhc.hadoop.storm0.spout.SMZDMSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

/**
 * @author superz
 * @create 2022/11/4 14:49
 **/
public class SMZDMTopology {
    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("smzdm", new SMZDMSpout(), 1);
        builder.setBolt("deal", new DealBolt("article_title", "article_price", "mall_name", "article_pubdate"), 3)
                .shuffleGrouping("smzdm");
        builder.setBolt("print", new PrintBolt(), 3).shuffleGrouping("deal");

        Config conf = new Config();
        conf.setDebug(true);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("superz_storm_smzdm", conf, builder.createTopology());
        Utils.sleep(Long.MAX_VALUE);
        cluster.killTopology("superz_storm_smzdm");
        cluster.shutdown();
    }
}
