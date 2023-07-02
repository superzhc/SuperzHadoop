package com.github.superzhc.hadoop.nifi.processors.rss;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.*;
import org.apache.nifi.processor.exception.ProcessException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// @Tags()
@CapabilityDescription("RSS 内容解析")
public class RssProcessor extends AbstractProcessor {

    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("success")
            .build();

    public static final Relationship FAILED = new Relationship.Builder()
            .name("failed")
            .build();

    private Set<Relationship> relationships;

    @Override
    protected void init(ProcessorInitializationContext context) {
        final Set<Relationship> relationships = new HashSet<>();
        relationships.add(SUCCESS);
        relationships.add(FAILED);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return relationships;
    }

    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        final List<FlowFile> flowFiles = processSession.get(50);
        if (flowFiles.isEmpty()) {
            return;
        }

        final ComponentLog logger = getLogger();

        for (FlowFile flowFile : flowFiles) {
            try (InputStream in = processSession.read(flowFile)) {

                SyndFeed feed = new SyndFeedInput().build(new InputStreamReader(in));

                // 公用信息

                // 获取每个实体内容
                List<SyndEntry> entries = feed.getEntries();
                for (SyndEntry entry : entries) {
                    FlowFile splitFlowFile = processSession.create(flowFile);


                }

            } catch (Exception e) {
                logger.error("RSS内容解析失败", e);
                processSession.transfer(flowFile, FAILED);
            }
        }
    }
}
