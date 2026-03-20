package com.juyou.common.permissiondata;

import com.juyou.common.base.service.BaseService;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.tenants.Tenants;
import com.juyou.common.utils.SpringContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * 数据权限切面处理类
 * @author kaedeliu
 *
 */
@Aspect
@Component
public class PermissionDataAspect {


  @Autowired
  @Lazy
  BaseService baseService;

  @Autowired
  LoginUtilsService loginUtils;

  /**
   * 拦截shiro权限注解
   */
  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) or @annotation(com.juyou.common.tenants.Tenants) or @annotation(org.springframework.web.bind.annotation.GetMapping)")
  public void logPointCut() {

  }

  @Around("logPointCut()")
  public Object around(ProceedingJoinPoint point) throws Throwable {
      HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
      MethodSignature signature = (MethodSignature) point.getSignature();
      Method method = signature.getMethod();

      List<PermissionDataRule> dataRules= new ArrayList<>();
      Tenants tenants=method.getAnnotation(Tenants.class); //处理多租户
      LoginUserBasicDto loginUserBasicDto= null;
      if(tenants!=null){
          loginUserBasicDto=loginUtils.getLoginUserInfo();
          dataRules.add(new PermissionDataRule().setRuleColumn(QueryGenerator.SQL_RULES_COLUMN).setRuleValue(tenants.value()+" = '" +loginUserBasicDto.getTenantsId()+"'" ));
      }
//      PermissionAnno pd = method.getAnnotation(PermissionAnno.class);
//      if(pd!=null) {
//          if(loginUserBasicDto==null) loginUserBasicDto=loginUtils.getLoginUserInfo();
//          String usetId= JwtUtil.getUserIdByToken(request);
//
//          List<PermissionDataRule> dataRules=getDataRule(request.getRequestURI(), usetId);
//          if(dataRules!=null) {
//              //存储数据权限
//              PermissionDataRuleUtils.setMenuDataRules(request, dataRules);
//              LoginUserDto loginUser= loginUtils.getLoginUser();
//              PermissionDataRuleUtils.setLoginUser(request, loginUser);
//          }
//      }
      if(!dataRules.isEmpty()){
          PermissionDataRuleUtils.setMenuDataRules(request, dataRules);
          PermissionDataRuleUtils.setLoginUser(request, loginUserBasicDto);
      }
      return  point.proceed();
  }

    /**
     * 等到用户当前菜单得数据权限
     * @param permss
     * @param userCode
     * @return
     */
  public List<PermissionDataRule> getDataRule(String[] permss,String userCode) {
      List<PermissionDataRule> ps=getMenuData(permss);
      if(ps==null || ps.isEmpty()) return null;
      List<PermissionDataRule> temps=new ArrayList<PermissionDataRule>();
      if(ps!=null && ps.size()>0) {
          Set<String> userPds=baseService.queryUserDataRule(userCode);
          for (PermissionDataRule permissionDataRule : ps) {
              if(userPds.contains(permissionDataRule.getDataRuleId())) {
                  temps.add(permissionDataRule);
              }
          }
      }
      if(!temps.isEmpty())
          return temps;
      else
          return null;
  }

  private List<PermissionDataRule> getMenuData(String[] permss){
      List<PermissionDataRule> temps=new ArrayList<PermissionDataRule>();
      Map<String,List<PermissionDataRule>> map = baseService.getAllPermissionData();
      for (String perms : permss) {
          List<PermissionDataRule> t=map.get(perms);
          if(t!=null)
              temps.addAll(t);
      }

      return temps;
  }
}
