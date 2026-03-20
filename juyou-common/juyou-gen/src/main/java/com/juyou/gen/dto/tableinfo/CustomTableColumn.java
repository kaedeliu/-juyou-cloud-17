package com.juyou.gen.dto.tableinfo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class CustomTableColumn {

    String tableName;

    String columnName;

    String javaType;

    String javaName;

    String fieldDesc;

    int addColumns;

    int editColumns;

    int listColumns;

    int queryColumns;

    int queryType;

    int notNullColumns;

    int outType;

    String ditcType;

    String regExp;

    String regMsg;
}
