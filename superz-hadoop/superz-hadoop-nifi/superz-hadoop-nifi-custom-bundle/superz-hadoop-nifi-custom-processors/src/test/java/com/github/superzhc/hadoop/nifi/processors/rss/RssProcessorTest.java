package com.github.superzhc.hadoop.nifi.processors.rss;

import com.rometools.rome.io.impl.DateParser;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Test;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class RssProcessorTest {

    @Test
    public void test() throws Exception {
        TestRunner runner = TestRunners.newTestRunner(new RssProcessor());

        runner.enqueue(this.getClass().getResourceAsStream("/rss/smzdm_faxian.xml"));

        runner.run();
    }

}