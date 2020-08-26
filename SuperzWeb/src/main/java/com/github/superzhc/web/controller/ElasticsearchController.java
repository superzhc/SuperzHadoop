package com.github.superzhc.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2020年08月26日 superz add
 */
@RestController
@RequestMapping("/elasticsearch")
public class ElasticsearchController
{
    @GetMapping("/indices")
    public String indices() {
        String ret = "[{\"name\": \"x1\", \"value\": \"0\"}," + "{\"name\": \"x2\", \"value\": \"1\"},"
                + "{\"name\": \"x3\", \"value\": \"2\"}]";

        return ret;
    }
}
