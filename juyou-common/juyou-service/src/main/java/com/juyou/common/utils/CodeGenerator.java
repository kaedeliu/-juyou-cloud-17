package com.juyou.common.utils;//package com.juyou.common.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

import java.util.Collections;

/**
 * 生成数据库
 * @author kaedeliu
 *
 */
public class CodeGenerator{

//	private static String username="root"; //DB用户名
//
//	private static String password="root";//DB密码
//
//	private static String dburl="jdbc:mysql://127.0.0.1:3306/bf?useUnicode=true&characterEncoding=UTF-8";
//	d
//	private static String username="root"; //DB用户名
//
//	private static String password="cddyxt#$%";//DB密码

	private static String username="number_mg"; //DB用户名

	private static String password="number#$%_12_mg";//DB密码

	//private static String dburl="jdbc:mysql://192.168.8.78:3306/bf?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false";

	private static String dburl="jdbc:mysql://39.100.211.85:3306/number?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull";
	private static String moduleName="admin"; //moduleName
	private static String parent="com.juyou.number"; //父包
	static String driverName="com.mysql.cj.jdbc.Driver";
	//static String driverName="com.mysql.jdbc.Driver";

	static final String projectPath="D:\\test\\number";//生成路径
	static String Author="kaedeliu";//作者

	//公共父类
	static String superEntityClass="com.juyou.common.base.entity.BaseEntity";

	//写在父类的公共字段
	static String SuperEntityColumns="";

	//需要生成的表，空表示全部
	static String[] tables= {};


    public static void create(){

        FastAutoGenerator fastAutoGenerator= FastAutoGenerator.create(dburl, username, password)
                .globalConfig(builder -> {
                    builder.author("kaedeliu") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
							.dateType(DateType.ONLY_DATE) //设置日期为 Date
                            .outputDir(projectPath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(parent) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,projectPath+"\\mapper\\"+moduleName)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
					builder.entityBuilder()
							.enableLombok()
							.enableChainModel()
							.idType(IdType.ASSIGN_UUID);
					builder.addTablePrefix("yu_");
					builder.addTablePrefix("yx_");
					builder.addTablePrefix("yo_");
//                    builder.addInclude("all") // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                }); // 使用Freemarker引擎模板，默认的是Velocity引擎模板
        fastAutoGenerator.templateConfig(builder -> {builder.entity("/templates/entity.java.vm").controller("/templates/controller2.java.vm").build();});
        fastAutoGenerator.execute();

    }
    /**
     * 模板配置
     */
    private static TemplateConfig.Builder templateConfig() {
        return new TemplateConfig.Builder();
    }

	public static void main(String[] args) {
		create();
	}

}
