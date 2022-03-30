package com.github.superzhc.common.tablesaw.read;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.io.ReadOptions;
import tech.tablesaw.io.Source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author superz
 * @create 2022/3/30 15:29
 **/
public class EmptyReadOptions extends ReadOptions {
    protected EmptyReadOptions(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ReadOptions.Builder {
        protected Builder() {
            super();
        }

//        protected Builder(Source source) {
//            super(source);
//        }
//
//        protected Builder(URL url) throws IOException {
//            super(url);
//        }
//
//        public Builder(File file) {
//            super(file);
//        }
//
//        protected Builder(Reader reader) {
//            super(reader);
//        }
//
//        protected Builder(InputStream stream) {
//            super(stream);
//        }

        @Override
        public EmptyReadOptions build() {
            return new EmptyReadOptions(this);
        }

        // Override super-class setters to return an instance of this class

//        @Override
//        public Builder header(boolean header) {
//            super.header(header);
//            return this;
//        }
//
//        @Override
//        public Builder tableName(String tableName) {
//            super.tableName(tableName);
//            return this;
//        }
//
//        @Override
//        public Builder sample(boolean sample) {
//            super.sample(sample);
//            return this;
//        }
//
//        @Override
//        @Deprecated
//        public Builder dateFormat(String dateFormat) {
//            super.dateFormat(dateFormat);
//            return this;
//        }
//
//        @Override
//        @Deprecated
//        public Builder timeFormat(String timeFormat) {
//            super.timeFormat(timeFormat);
//            return this;
//        }
//
//        @Override
//        @Deprecated
//        public Builder dateTimeFormat(String dateTimeFormat) {
//            super.dateTimeFormat(dateTimeFormat);
//            return this;
//        }
//
//        @Override
//        public Builder dateFormat(DateTimeFormatter dateFormat) {
//            super.dateFormat(dateFormat);
//            return this;
//        }
//
//        @Override
//        public Builder timeFormat(DateTimeFormatter timeFormat) {
//            super.timeFormat(timeFormat);
//            return this;
//        }
//
//        @Override
//        public Builder dateTimeFormat(DateTimeFormatter dateTimeFormat) {
//            super.dateTimeFormat(dateTimeFormat);
//            return this;
//        }
//
//        @Override
//        public Builder locale(Locale locale) {
//            super.locale(locale);
//            return this;
//        }
//
//        @Override
//        public Builder missingValueIndicator(String... missingValueIndicators) {
//            super.missingValueIndicator(missingValueIndicators);
//            return this;
//        }
//
//        @Override
//        public Builder minimizeColumnSizes() {
//            super.minimizeColumnSizes();
//            return this;
//        }

        @Override
        public Builder columnTypes(ColumnType[] columnTypes) {
            super.columnTypes(columnTypes);
            return this;
        }

        @Override
        public Builder columnTypes(Function<String, ColumnType> columnTypeFunction) {
            super.columnTypes(columnTypeFunction);
            return this;
        }

        @Override
        public Builder columnTypesPartial(Function<String, Optional<ColumnType>> columnTypeFunction) {
            super.columnTypesPartial(columnTypeFunction);
            return this;
        }

        @Override
        public Builder columnTypesPartial(Map<String, ColumnType> columnTypeByName) {
            super.columnTypesPartial(columnTypeByName);
            return this;
        }
    }
}
