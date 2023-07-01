package com.github.superzhc.hadoop.nifi.processors.rss;

import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.exception.ProcessException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RssProcessor extends AbstractProcessor {
    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        final List<FlowFile> flowFiles = processSession.get(50);
        if (flowFiles.isEmpty()) {
            return;
        }

        final ComponentLog logger = getLogger();

        for (FlowFile flowFile : flowFiles) {
            try (InputStream in = processSession.read(flowFile)) {

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
