package ${package.Service};

import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}PageDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}AddDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}EditDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}ListDto;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
public interface ${table.serviceName} extends IService<${entity}>{

    /**
    ** 获取分页对象
    **/
    IPage<${entity}ListDto> pageList(${entity}PageDto ${table.entityPath}PageDto) throws  Exception;


    /**
    ** 插入对象
    **/
    void insert( ${entity}AddDto ${table.entityPath}AddDto);

    /**
    ** 更新对象
    **/
    void update(${entity}EditDto ${table.entityPath}EditDto);

    /**
    ** 删除对象
    **/
    void delete( IdDto dto);

    /**
    ** 批量删除对象
    **/
    void deletes( IdsDto dto);
}