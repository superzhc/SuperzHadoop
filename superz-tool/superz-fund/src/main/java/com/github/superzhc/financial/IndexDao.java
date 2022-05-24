package com.github.superzhc.financial;

import com.github.superzhc.common.jdbc.JdbcHelper;
import com.github.superzhc.tablesaw.utils.TableUtils;
import tech.tablesaw.api.Table;

/**
 * @author superz
 * @create 2022/5/24 16:51
 **/
public class IndexDao extends BaseDao {
    public static final String TABLE_USER_INDEX = "CREATE TABLE USER_INDEX(ID INTEGER PRIMARY KEY,INDEX_CODE TEXT NOT NULL,INDEX_NAME TEXT NOT NULL,INDEX_TYPE TEXT NOT NULL,INVEST DOUBLE NOT NULL,WORTH DOUBLE NOT NULL,DIVIDEND DOUBLE NOT NULL)";

    public IndexDao(JdbcHelper dao) {
        super(dao);
    }

    public Table indices() {
        String sql = "SELECT * FROM USER_INDEX";
        Table table = TableUtils.db(dao, sql);
        return table;
    }
}
