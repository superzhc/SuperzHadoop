package com.github.superzhc.hadoop.iceberg;

import com.github.superzhc.common.utils.ListUtils;
import com.github.superzhc.hadoop.iceberg.catalog.IcebergHadoopS3Catalog;
import com.github.superzhc.hadoop.iceberg.utils.SchemaUtils;
import com.github.superzhc.hadoop.iceberg.utils.TableUtils;
import org.apache.iceberg.*;
import org.apache.iceberg.catalog.Catalog;
import org.apache.iceberg.catalog.Namespace;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.expressions.Expressions;
import org.apache.iceberg.types.Types;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

public class IcebergHadoopCatalogTest {

}