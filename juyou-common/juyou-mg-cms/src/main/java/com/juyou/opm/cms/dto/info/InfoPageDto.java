package com.juyou.opm.cms.dto.info;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import com.juyou.common.dto.PageDto;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文章-属性表
 * </p>
 *
 * @author yx
 * @since 2023-04-24 18:47:53
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title="InfoPageDto对象", description="InfoPage查询对象")
public class InfoPageDto extends PageDto {

    private static final long serialVersionUID = 1L;


        @Schema(description = "文章名称")
    private String name;

        @Schema(description = "文章类型")
    private String typeId;

        @Schema(description = "是否热门")
    private Integer hot;

        @Schema(description = "是否推荐")
    private Integer recommended;

        @Schema(description = "是否最新")
    private Integer latest;

        @Schema(description = "是否精选")
    private Integer curation;
}