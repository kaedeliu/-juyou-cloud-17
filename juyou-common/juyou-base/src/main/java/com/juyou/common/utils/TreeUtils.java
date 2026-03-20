package com.juyou.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author kaedeliu
 * 树型工具类
 */
public class TreeUtils {

    /**
     * 父Id字段名称
     */
    public final static String PARENT_ID_CLOUMN="parentId";

    public final static String ID_CLOUMN="id";

    public final static String CHILDREN_CLOUMN="children";

    /**
     * 转换为树型菜单
     * @param sources 源数据
     * @param parentId 第一级父对象的值，null=""
     * @param targetClass 目标对象的Class类型
     * @param sourceClass 源对象类型
     * @return
     * @param <T>
     * @throws Exception
     */
    public static <T> List<T> formatTree(List<?> sources, String parentId, Class<T> targetClass, Class<?> sourceClass) throws Exception {
        return formatTree(sources,parentId,ID_CLOUMN,PARENT_ID_CLOUMN,CHILDREN_CLOUMN,targetClass,sourceClass);
    }

    /**
     * 转换为树型对象
     * @param sources 源数据
     * @param parentId 第一级父对象的值，null=""
     * @param idCloumn 标识字段名称
     * @param targetClass 目标对象的Class类型
     * @param sourceClass 源对象类型
     * @return 树型List对象
     * @param <T>
     * @throws Exception
     */
    public static <T> List<T> formatTree(List<?> sources, String parentId, String idCloumn, Class<T> targetClass, Class<?> sourceClass) throws Exception {
        return formatTree(sources,parentId,idCloumn,PARENT_ID_CLOUMN,CHILDREN_CLOUMN,targetClass,sourceClass);
    }


    /**
     * 转换为树型对象
     * @param sources 源数据
     * @param parentId 第一级父对象的值，null=""
     * @param idCloumn 标识字段名称
     * @param parentIdCloumn 父字段名称
     * @param childrenCloumn 子级字段名称
     * @param targetClass 目标对象的Class类型
     * @param sourceClass 源对象类型
     * @return 树型List对象
     * @param <T>
     * @throws Exception
     */
    public static <T> List<T> formatTree(List<?> sources, String parentId, String idCloumn, String parentIdCloumn,String childrenCloumn, Class<T> targetClass, Class<?> sourceClass) throws Exception {
        List<T> targets=new ArrayList<>();
        if(sources==null || sources.isEmpty()) return targets;
        Field field = sourceClass.getDeclaredField(parentIdCloumn);
        field.setAccessible(true);
        List<?> items=sources.stream().filter(item->{
            String value=null;
            try {
                value = (String)field.get(item);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return (!StringUtils.hasLength(parentId) && !StringUtils.hasLength(value)) || (parentId!=null &&parentId.equals(value));
        }).collect(Collectors.toList());
        if(!items.isEmpty()) {
            for (Object item : items) {
                T t = targetClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(item, t);
                targets.add(t);
            }

            sources.remove(items);

            for (T t : targets) {
                Field idField=targetClass.getDeclaredField(idCloumn);
                idField.setAccessible(true);
                String idValue= (String) idField.get(t);
                List<T> children=formatTree(sources, idValue, idCloumn , parentIdCloumn ,childrenCloumn, targetClass,sourceClass);
                Field tfield=targetClass.getDeclaredField(childrenCloumn);
                tfield.setAccessible(true);
                tfield.set(t,children);
            }
        }
        return targets;
    }
}
