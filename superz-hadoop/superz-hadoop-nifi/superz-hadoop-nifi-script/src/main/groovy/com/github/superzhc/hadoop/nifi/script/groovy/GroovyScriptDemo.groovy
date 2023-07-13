package com.github.superzhc.hadoop.nifi.script.groovy

import com.github.superzhc.hadoop.nifi.script.GroovyEnvironmentTemplate
import groovy.json.JsonOutput
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.xml.XmlSlurper
import org.apache.nifi.processor.io.InputStreamCallback
import org.apache.nifi.processor.io.OutputStreamCallback
import org.apache.nifi.processor.io.StreamCallback

import java.nio.charset.Charset

class GroovyScriptDemo extends GroovyEnvironmentTemplate {
    /**
     * 获取输入FlowFile
     */
    def getFlowFile() {
        def ff = session.get()
        if (!ff) return
        ff
    }

    /**
     * 获取多个输入FlowFile
     */
    def getMultipleFlowFiles() {
        def flowfiles = session.get(100)
        if (flowfiles.isEmpty()) return
        flowfiles
    }

    /**
     * 创建一个新的FlowFile
     */
    def createFlowFile() {
        def ff = session.create()
        ff
    }

    /**
     * 通过父FlowFile创建FlowFile
     * @return
     */
    def createFlowFileByParent() {
        def parentFF = getFlowFile()
        def ff = session.create(ff)
        ff
    }

    /**
     * 获取属性
     * @return
     */
    def getAttribute() {
        def ff = getFlowFile()
        ff.getAttribute("key")
    }

    /**
     * 获取所有属性
     * @return
     */
    def getAllAttributes() {
        def ff = getFlowFile()
        ff.getAttributes()
    }

    /**
     * 向一个FlowFlie添加一个属性
     * @return
     */
    def addAttribute() {
        def ff = getFlowFile()
        ff = session.putAttribute(ff, "key", "value")
        ff
    }

    /**
     * 向一个FlowFile添加多个属性
     * @return
     */
    def addAttributes() {
        def ff = getFlowFile()

        def attrs = ["k1": "v1", "k2": "v2"]
        ff = session.putAllAttributes(ff, attrs)
        ff
    }

    def transferFlowFile() {
        def ff = getFlowFile()
        if (!ff) {
            session.transfer(session.create(), REL_FAILURE)
        } else {
            session.transfer(ff, REL_SUCCESS)
        }
    }

    /**
     * 读数据
     * @return
     */
    def readContent() {
        def ff = getFlowFile()
        session.read(ff, { inputStream ->
            // 读取数据操作
        } as InputStreamCallback)
    }

    def readJsonContent() {
        def ff = getFlowFile()

        def obj = null
        session.read(ff, { inputStream ->
            def parser = new JsonSlurper().setType(JsonParserType.LAX)
            obj = parser.parse(inputStream)
        } as InputStreamCallback)

        obj
    }

    def readXmlContent() {
        def ff = getFlowFile()

        def obj = null
        session.read(ff, { inputStream ->
            def parser = new XmlSlurper()
            obj = parser.parse(inputStream)
        } as InputStreamCallback)

        obj
    }

    /**
     * 写数据
     * @return
     */
    def writeContent() {
        def ff = session.create()
        session.write(ff, { outputStream ->
            // 写数据操作
        } as OutputStreamCallback)
    }

    def writeJsonContent() {
        def obj = [:]

        def ff = session.create()
        session.write(ff, { outputStream ->
            def text = JsonOutput.toJson(obj)
            outputStream.write(text.getBytes(Charset.forName("UTF-8")))
        } as OutputStreamCallback)
    }

    def writeXmlContent(){

    }

    /**
     * 更新数据
     * @return
     */
    def updateContent() {
        def ff = getFlowFile()
        session.write(ff, { inputStream, outputStream ->
            // 先读取FlowFile流inputStream，再使用outputStream流更新数据
        } as StreamCallback)
    }

    def updateJsonContent() {
        def ff = getFlowFile()
        session.write(ff, { inputStream, outputStream ->
            def parser = new JsonSlurper().setType(JsonParserType.LAX)
            def obj = parser.parse(inputStream)

            // 对对象进行操作。。。

            def text = JsonOutput.toJson(obj)
            outputStream.write(text.getBytes(Charset.forName("UTF-8")))
        } as StreamCallback)
    }

    /**
     * 捕捉异常
     */
    def catchException() {
        def ff = getFlowFile()
        try {
            //...
        } catch (e) {
            //...
        }
    }
}
