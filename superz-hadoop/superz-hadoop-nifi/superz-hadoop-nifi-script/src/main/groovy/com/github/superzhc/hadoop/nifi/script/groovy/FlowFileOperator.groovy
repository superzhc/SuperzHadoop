package com.github.superzhc.hadoop.nifi.script.groovy

import com.github.superzhc.hadoop.nifi.script.GroovyEnvironmentTemplate
import groovy.json.JsonOutput
import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import org.apache.nifi.processor.io.StreamCallback

import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FlowFileOperator extends GroovyEnvironmentTemplate {

    def readJson() {
        def ff = session.get()
        if (!ff) return

        session.write(ff, { inputStream, outputStream ->
            def parser = new JsonSlurper().setType(JsonParserType.LAX)
            def array = parser.parse(inputStream)

            def start = LocalDate.of(1996, 2, 1)
            def dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            def newMap = [:]
            array.each { item ->
                newMap.put(start.format(dateFormat), item)
                start = start.plusMonths(1)
            }

            def text = JsonOutput.toJson(newMap)
            outputStream.write(text.getBytes(Charset.forName("UTF-8")))
        } as StreamCallback)

        session.transfer(ff, REL_SUCCESS)
    }
}
