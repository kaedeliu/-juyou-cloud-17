package com.juyou.common.base.mapper;

import com.juyou.common.base.entity.BaseEntity;
import com.juyou.common.log.Operation;
import com.juyou.common.permissiondata.PermissionDataRule;
import com.juyou.common.task.CommonTask;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface BaseMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<BaseEntity> {

    /**
     * 读取用户角色
     *
     * @param id
     * @return
     */
    Set<String> queryUserRole(String id);




    /**
     * 读取角色权限
     * @param roleCode
     * @return
     */
    @Select("select b.perms from yu_role_permission as a join yu_permission as b on a.permission_id=b.permission_id join yu_role as c" +
            " on c.role_id=a.role_id where c.role_code=#{roleCode} group by b.perms")
    Set<String> queryRolePermission(@Param("roleCode") String roleCode);


    /**
     * 读取用户数据权限
     */
    Set<String> queryUserDataRule(String userSeq);

//    /**
//     * 获取登录用户的信息，主要用于处理数据权限
//     *
//     * @param userId
//     * @return
//     */
//    LoginUserDto queryLoginUserInfo(String userId);
//
//    /**
//     * 获取客户端登录用户的信息，主要用于处理数据权限
//     *
//     * @param userId
//     * @return
//     */
//    LoginUserDto queryCcLoginUserInfo(String userId);

    /**
     * 保存操作日志
     *
     * @param operations
     */
    void saveOperation(@Param("list") List<Operation> operations);

    /**
     * 获取所有的数据权限定义
     *
     * @return
     */
    List<PermissionDataRule> queryAllPermissionData();

    /**
     * 获取定时任务
     */
    List<CommonTask> selectCommonTask(@Param("type") String type, @Param("status") Integer status);

    /**
     * 根据ID获取定时任务
     *
     * @param id
     * @return
     */
    CommonTask selectCommonTaskById(String id);

    /**
     * 更新任务
     *
     * @param commonTask
     */
    void updateCommonTaskById(CommonTask commonTask);

    /**
     * 更新业务主键
     *
     * @param pkType
     * @param increasingVal
     * @return
     */
    Long updatePkNum(@Param("pkType") String pkType, @Param("increasingVal") int increasingVal);

    /**
     * 插入新的业务主键
     *
     * @param pkType
     * @param increasingVal
     * @return
     */
    Long insertPk(@Param("pkType") String pkType, @Param("increasingVal") int increasingVal);

    /**
     * 根据type获取主键key
     *
     * @param pkType
     * @return
     */
    Integer selectPkByType(@Param("pkType") String pkType);


    /**
     * @param commodityId:
     * @return
     * @功能 获取待售商品明细
     * @Author kaedeliu
     * @创建时间 2026/3/18 10:49
     * @修改人 kaedeliu
     * @修改时间 2026/3/18 10:49
     * @Param
     **/
    @Select("select commodity_detail_id from yo_commodity_detail where status=0 and commodity_id=#{commodityId} ORDER BY code asc")
    List<String> getSoldCommodityDetail(@Param("commodityId") String commodityId);

    @Select("select sold from yo_commodity_sold where commodity_id=#{commodityId}")
    Integer getCommoditySole(@Param("commodityId") String commodityId);

    @Update("update yo_commodity_sold set sold=#{sole} where commodity_id=#{commodityId}")
    void updateCommoditySole(@Param("commodityId") String commodityId, @Param("sole") Integer sole);

    @Update("update yo_commodity_detail set status=#{status} where commodity_detail_id=#{commodityDetailId}")
    void updateCommodituDetail(@Param("commodityDetailId") String commodityDetailId, @Param("status") Integer sataus);

    @Select("select COUNT(*) from yo_user_commodity where user_id = #{userId} and commodity_id = #{commodityId} and status in (1,3)")
    Integer getUserCommodityNum(@Param("commodityId") String commodityId, @Param("userId") String userId);

    @Select("SELECT user_id as userId, count(commodity_id) AS num FROM yo_user_commodity WHERE status in (1,3) AND commodity_id = #{commodityId} GROUP BY user_id")
    List<Map<String, Integer>> getUserCommodityNumByCommodityId(@Param("commodityId") String commodityId);


	@Select("select now();")
	Date getDbDate();

    @Select("select value from juyou_sys_config where code=#{code} and tenants_id=#{tenantsId}")
    String findConfig(@Param("code") String code,@Param("tenantsId") String tenantsId);
}
