package com.juyou.common.utils;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class HttpRep {
    /*1~*/@Schema(title ="返回数据")
    String res;
    /*1~*/@Schema(title ="返回code")
    int code=200;
}
