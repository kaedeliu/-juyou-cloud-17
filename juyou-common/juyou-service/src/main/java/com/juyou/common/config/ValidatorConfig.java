//package com.juyou.common.config;
//
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;
//
//import org.hibernate.validator.HibernateValidator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * 验证框架配置
// * @author kaedeliu
// *
// */
//@Configuration
//public class ValidatorConfig {
//
//	@Bean
//    public Validator validator() {
//        //failFast true 遇到一个错误就返回
//        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class )
//                .configure()
//                .failFast(true)
//                .buildValidatorFactory();
//
//
//        return validatorFactory.getValidator();
//    }
//}
