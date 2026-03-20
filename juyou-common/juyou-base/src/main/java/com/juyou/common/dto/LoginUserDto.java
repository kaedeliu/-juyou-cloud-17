package com.juyou.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Accessors(chain = true)
public class LoginUserDto extends LoginUserBasicDto implements java.io.Serializable {

    private static final long serialVersionUID = -7012990668457907358L;

    @Schema(title = "密码", description = "密码")
    private String password;

    @Schema(title = "加密盐", description = "加密盐")
    private String secret;

    @Schema(title = "用户简称", description = "用户简称")
    private String spell;

    @Schema(title = "身份证", description = "身份证")
    private String idNumber;
    @Schema(title = "电话", description = "电话")
    private String phone;

    @Schema(description = "锁定时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lockTime;


}
