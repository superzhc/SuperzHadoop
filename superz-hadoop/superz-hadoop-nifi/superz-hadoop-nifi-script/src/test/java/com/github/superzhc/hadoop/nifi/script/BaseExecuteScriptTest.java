package com.github.superzhc.hadoop.nifi.script;

import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processors.script.ExecuteScript;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.jupiter.api.BeforeEach;

public class BaseExecuteScriptTest {
    protected TestRunner runner;
    protected ProcessSession session;
    protected ProcessContext context;
    protected ComponentLog log;

    @BeforeEach
    public void setUp() {
        this.runner = TestRunners.newTestRunner(new ExecuteScript());
        this.session = this.runner.getProcessSessionFactory().createSession();
        this.context = this.runner.getProcessContext();
    }
}
