package com.juyou.gen.utils;


import com.juyou.gen.dto.tableinfo.GenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取代码生成器配置
 */
@Component
public class GenConfigUtils {

    @Autowired
    public Environment env;

    String configFix="cdyx.gen.";

    public  GenConfig getConfig(String moduleName,String[] tables){
        configFix=env.getProperty("cdyx.gen.fix","cdyx.gen.");
        String parenBuff=env.getProperty(configFix+"parent","com.juyou");
        if(moduleName.contains(".")){
            parenBuff=parenBuff+"."+moduleName.substring(0,moduleName.lastIndexOf("."));
            moduleName = moduleName.substring(moduleName.lastIndexOf(".")+1);
        }
        String outputDir=env.getProperty(configFix+"outputDir");
        GenConfig genConfig=new GenConfig();
        genConfig.setUrl(env.getProperty(configFix+"url"));
        genConfig.setUsername(env.getProperty(configFix+"username"));
        genConfig.setPassword(env.getProperty(configFix+"password"));
        genConfig.setParent(parenBuff).setModuleName(moduleName).
                setOutputDir(outputDir)
                .setEntityTemplate(env.getProperty(configFix+"entityTemplate","/templates/myentity.java"))
                .setServiceTemplate(env.getProperty(configFix+"serviceTemplate","/templates/myservice.java"))
                .setServiceImplTemplate(env.getProperty(configFix+"serviceImplTemplate","/templates/myserviceImpl.java"))
                .setControllerTemplate(env.getProperty(configFix+"controllerTemplate","/templates/mycontroller.java"));
//                .setMapperTemplate(env.getProperty(configFix+"mapperTemplate","/templates/mapper.xml"));

        if(tables!=null && tables.length>0)
            genConfig.setTables(tables);
        String tablePrefixs=env.getProperty(configFix+"tablePrefixs","");
        if(StringUtils.hasLength(tablePrefixs))
            genConfig.setTablePrefixs(tablePrefixs.split(","));

        genConfig.setSuperClass(env.getProperty(configFix+"superClass"));
        genConfig.setSuperEntityColumns(env.getProperty(configFix+"superEntityColumns"));

        Map<String,String> pathInfo=new HashMap<>();
        pathInfo.put("dto/PageDto.java", "/templates/page.dto.java.ftl");
        pathInfo.put("dto/AddDto.java", "/templates/add.dto.java.ftl");
        pathInfo.put("dto/EditDto.java", "/templates/edit.dto.java.ftl");
        pathInfo.put("dto/ListDto.java", "/templates/list.dto.java.ftl");

        pathInfo.put("vue/index.vue.ftl", "/templates/index.vue.ftl");
        pathInfo.put("vue/popup.vue.ftl", "/templates/popup.vue.ftl");
        genConfig.setPathInfo(pathInfo);

        genConfig.setZipPath(env.getProperty(configFix+"zipPath"));

        return genConfig;
    }
}
