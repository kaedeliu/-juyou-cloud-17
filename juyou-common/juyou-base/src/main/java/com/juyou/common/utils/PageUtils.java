package com.juyou.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PageUtils {

    /**
     * 转换返回的Page数据
     * @param source
     * @param clazz
     * @return
     * @param <T>
     */
    public  static <T> IPage<T> toPege(IPage<?> source,Class<T> clazz) throws Exception {
        IPage<T> page=new Page<>();
        BeanUtils.copyProperties(source,page,"records");
        List<?> olds=source.getRecords();
        List<T> news=new ArrayList<>();
        for (Object old:olds) {
            T t=clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(old,t);
            news.add(t);
        }
        page.setRecords(news);
        return page;
    }
}
