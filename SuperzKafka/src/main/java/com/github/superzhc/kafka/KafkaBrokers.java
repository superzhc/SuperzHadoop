package com.github.superzhc.kafka;

/**
 * 2020年04月26日 superz add
 */
public abstract class KafkaBrokers
{
    protected String brokers;

    public KafkaBrokers(String brokers) {
        this.brokers = brokers;
    }
}
