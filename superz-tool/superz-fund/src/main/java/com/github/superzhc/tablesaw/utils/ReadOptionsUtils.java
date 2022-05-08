package com.github.superzhc.tablesaw.utils;

import com.github.superzhc.common.tablesaw.read.EmptyReadOptions;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.io.ReadOptions;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/4/2 11:25
 **/
public class ReadOptionsUtils {
    public static ReadOptions empty() {
        EmptyReadOptions options = EmptyReadOptions.builder().build();
        return options;
    }

    public static ReadOptions columnTypeByName(Map<String, ColumnType> map){
        return EmptyReadOptions.builder()
                .columnTypesPartial(map)
                .build();
    }

    public static ReadOptions columnTypeByFunction(Function<String, Optional<ColumnType>> func){
        return EmptyReadOptions.builder()
                .columnTypesPartial(func)
                .build();
    }
}
