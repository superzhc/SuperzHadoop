package com.github.superzhc.hadoop.es.search;

import com.github.superzhc.hadoop.es.ESClient;

/**
 * 只是为了好辨识，使用ESIndexsSearch也是可以实现的
 * 2020年06月17日 superz add
 */
@Deprecated
public class ESFullSearch extends ESIndexsSearch
{
    public ESFullSearch(ESClient client) {
        super(client);
    }
}
