package com.github.superzhc.hadoop.nifi.processors.rss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.behavior.SupportsBatching;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.*;
import org.apache.nifi.processor.exception.ProcessException;

import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SideEffectFree
@SupportsBatching
@Tags({"xml", "rss"})
@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@CapabilityDescription("RSS Content Parse")
public class RssProcessor extends AbstractProcessor {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final PropertyDescriptor DATE_FORMAT = new PropertyDescriptor.Builder()
            .name("时间格式")
            .description(String.format("若为空，则默认使用格式【%s】", DEFAULT_DATE_FORMAT))
            .expressionLanguageSupported(ExpressionLanguageScope.NONE)
            .required(false)
            .build();

    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("success")
            .build();

    public static final Relationship FAILED = new Relationship.Builder()
            .name("failed")
            .build();

    private Set<Relationship> relationships;
    private List<PropertyDescriptor> properties;

    private final AtomicReference<SyndFeed> syndFeed = new AtomicReference<>();

    @Override
    protected void init(ProcessorInitializationContext context) {
        final Set<Relationship> relationships = new HashSet<>();
        relationships.add(SUCCESS);
        relationships.add(FAILED);
        this.relationships = Collections.unmodifiableSet(relationships);

        final List<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(DATE_FORMAT);
        this.properties = Collections.unmodifiableList(properties);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return relationships;
    }

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return this.properties;
    }

    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        final List<FlowFile> flowFiles = processSession.get(50);
        if (flowFiles.isEmpty()) {
            return;
        }

        String dateFormat = processContext.getProperty(DATE_FORMAT).getValue();
        if (null == dateFormat || dateFormat.trim().length() == 0) {
            dateFormat = DEFAULT_DATE_FORMAT;
        }

        final ComponentLog logger = getLogger();

        for (FlowFile flowFile : flowFiles) {
            try {
                processSession.read(flowFile, rawIn -> {
                    try (InputStreamReader reader = new InputStreamReader(rawIn)) {
                        SyndFeed feed = new SyndFeedInput().build(reader);
                        syndFeed.set(feed);
                    } catch (FeedException e) {
                        logger.error("读取文件异常", e);
                        processSession.transfer(flowFile, FAILED);
                    }
                });

                SyndFeed feed = syndFeed.get();
                String platform = feed.getTitle();
                String home = feed.getLink();

                List<SyndEntry> entries = feed.getEntries();
                int cursor = 0;
                for (SyndEntry entry : entries) {
                    String title = entry.getTitle();
                    String description = entry.getDescription().getValue();
                    Date publishDate = entry.getPublishedDate();
                    String link = entry.getLink();
                    String content = entry.getContents().stream().map(d -> d.getValue()).collect(Collectors.joining("<br />"));

                    ObjectNode node = mapper.createObjectNode();
                    node.put("platform", platform);
                    node.put("home", home);
                    node.put("title", title);
                    node.put("link", link);
                    node.put("publishDate", new SimpleDateFormat(dateFormat).format(publishDate));
                    node.put("description", description);
                    node.put("content", content);

                    final byte[] data = mapper.writeValueAsBytes(node);

                    FlowFile splitFlowFile = processSession.create(/*flowFile*/);
                    splitFlowFile = processSession.write(splitFlowFile, rawOut -> {
                        try (final OutputStream out = new BufferedOutputStream(rawOut)) {
                            out.write(data);
                        }
                    });
                    // processSession.putAttribute(splitFlowFile, "Content-Type", "application/json;charset=UTF-8");
//                    processSession.putAttribute(splitFlowFile,"mime.type")
                    processSession.putAttribute(splitFlowFile, "split.number", String.valueOf(++cursor));
                    processSession.transfer(splitFlowFile, SUCCESS);
                }

                // 分割完成删除原始文件不做保留
                processSession.remove(flowFile);
            } catch (Exception e) {
                logger.error("RSS Content Parse Failed", e);
                processSession.transfer(flowFile, FAILED);
            }
        }
    }
}
