package com.juyou.sms.dto;

public enum SmsTypeEnum {

    阿里云("ali"),
    腾讯("tx");

    public String val;

    SmsTypeEnum(String val){
        this.val=val;
    }
}
