package com.juyou.common.utils;

import com.juyou.common.dto.SelectDto;
import com.juyou.common.exception.BaseException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理下拉列表框属性转换
 */
public class SelectUtils {

    /**
     * 格式化select值
     * @param sources 数据源
     * @param nameCloumn name字段名称
     * @param valueCloumn value字段名称
     * @param clazz  class类型
     * @param attrCloumns 附加值名称
     * @return
     * @param <T>
     */
    public static <T> List<SelectDto> fromatSelect(List<T> sources,String nameCloumn,String valueCloumn,Class<T> clazz,String... attrCloumns) throws IllegalAccessException, NoSuchFieldException {
        List<SelectDto> selectDtos=new ArrayList<>();
        Field nameField=null;
        Field valueField=null;
        try {
            nameField=clazz.getDeclaredField(nameCloumn);
            valueField=clazz.getDeclaredField(valueCloumn);
            nameField.setAccessible(true);
            valueField.setAccessible(true);
        }catch (Exception e){
            throw new BaseException(nameCloumn+" or" +valueCloumn +" 属性错误");
        }
        for (T source: sources) {
            SelectDto selectDto=new SelectDto();
            selectDto.setName((String) nameField.get(source)).setValue((String) valueField.get(source));

            if(attrCloumns!=null && attrCloumns.length>0){
                Map<String,Object> attrs=new HashMap<>();
                for (String attrCloumn: attrCloumns) {
                    Field field= clazz.getDeclaredField(attrCloumn);
                    field.setAccessible(true);
                    attrs.put(attrCloumn,field.get(source));
                }
                selectDto.setAttrs(attrs);
            }
            selectDtos.add(selectDto);

        }
        return selectDtos;
    }
}
