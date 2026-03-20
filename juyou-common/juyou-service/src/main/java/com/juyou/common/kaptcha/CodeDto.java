package com.juyou.common.kaptcha;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CodeDto {

    @Schema(description = "上部图片,base64")
    String image;

    @Schema(description = "下部图片，只给名称且不含后缀，自己拼接OSS，验证时将选择顺序的值上传")
    List<String> images;
}
