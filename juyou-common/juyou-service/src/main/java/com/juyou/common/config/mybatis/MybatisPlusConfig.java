package com.juyou.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * 单数据源配置（jeecg.datasource.open = false时生效）
 * @Author kaedeliu
 *
 */
@Configuration
public class MybatisPlusConfig {


//    /**
//     * 多租户属于 SQL 解析部分，依赖 MP 分页插件
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor().setLimit(-1);
//        //多租户配置 配置后每次执行sql会走一遍他的转化器 如果不需要多租户功能 可以将其注释
//        return paginationInterceptor;
//    }




        /**
         * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题
         */
        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor() {
            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
            return interceptor;
        }

    /**

     //    /**
     //     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     //     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }


}
 


