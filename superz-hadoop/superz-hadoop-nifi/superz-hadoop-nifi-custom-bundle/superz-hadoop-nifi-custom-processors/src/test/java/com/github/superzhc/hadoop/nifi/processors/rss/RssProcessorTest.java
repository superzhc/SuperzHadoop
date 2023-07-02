package com.github.superzhc.hadoop.nifi.processors.rss;

import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RssProcessorTest {

    @Test
    public void test() throws Exception {
        TestRunner runner = TestRunners.newTestRunner(new RssProcessor());

        runner.enqueue(this.getClass().getResourceAsStream("/rss/smzdm_faxian.xml"));

        runner.run();

    }

}