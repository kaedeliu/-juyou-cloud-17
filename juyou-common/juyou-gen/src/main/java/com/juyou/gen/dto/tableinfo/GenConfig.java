package com.juyou.gen.dto.tableinfo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class GenConfig {

    /**
     * 数据库连接
     */
    String url;

    /**
     * 数据库账户
     */
    String username;

    /**
     * 数据库密码
     */
    String password;

    /**
     * 父包名
     */
    String parent;

    String moduleName;


    /**
     * 输入路径
     */
    String outputDir;

    /**
     * 过滤的表前缀
     */
    String[] tablePrefixs;

    String entityTemplate;

    String serviceTemplate;

    String serviceImplTemplate;

    String mapperTemplate;

    /**
     * controller模板
     */
    String controllerTemplate;

    /**
     * 需要生成的表
     */
    String[] tables;

    /**
     * 输出路径
     */
    Map<String,String> pathInfo;

    String zipPath;

    /**
     * 实体父类
     */
    String superClass;

    /**
     * 父类字段英文,号间隔
     */
    String superEntityColumns;

}
