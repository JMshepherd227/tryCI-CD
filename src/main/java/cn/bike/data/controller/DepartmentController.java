package cn.bike.data.controller;

import cn.bike.basics.log.LogType;
import cn.bike.basics.log.SystemLog;
import cn.bike.basics.parameter.CommonConstant;
import cn.bike.basics.redis.RedisTemplateHelper;
import cn.bike.basics.utils.ResultUtil;
import cn.bike.basics.utils.SecurityUtil;
import cn.bike.basics.baseVo.Result;
import cn.bike.data.entity.Department;
import cn.bike.data.entity.DepartmentHeader;
import cn.bike.data.entity.User;
import cn.bike.data.service.IDepartmentHeaderService;
import cn.bike.data.service.IDepartmentService;
import cn.bike.data.service.IUserService;
import cn.bike.data.utils.NullUtils;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@RestController
@Api(tags = "部门管理接口")
@RequestMapping("/zwz/department")
@CacheConfig(cacheNames = "department")
@Transactional
public class DepartmentController {

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IDepartmentService iDepartmentService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IDepartmentHeaderService iDepartmentHeaderService;

    private static final String ONE_LEVEL_PARENT_TITLE = "一级部门";

    private static final String REDIS_DEPARTMENT_PRE_STR = "department::";

    private static final String REDIS_STEP_STR = ":";

    @SystemLog(about = "查询子部门", type = LogType.DATA_CENTER,doType = "DEP-01")
    @RequestMapping(value = "/getByParentId", method = RequestMethod.GET)
    @ApiOperation(value = "查询子部门")
    public Result<List<Department>> getByParentId(@RequestParam(required = false,defaultValue = "0") String parentId){
        List<Department> list = null;
        User nowUser = securityUtil.getCurrUser();
        String key = REDIS_DEPARTMENT_PRE_STR + parentId + REDIS_STEP_STR + nowUser.getId();
        String value = redisTemplateHelper.get(key);
        if(!NullUtils.isNull(value)){
            return new ResultUtil<List<Department>>().setData(JSON.parseArray(value,Department.class));
        }
        QueryWrapper<Department> depQw = new QueryWrapper<>();
        depQw.eq("parent_id",parentId);
        depQw.orderByAsc("sort_order");
        list = iDepartmentService.list(depQw);
        list = setInfo(list);
        redisTemplateHelper.set(key,JSON.toJSONString(list), 15L, TimeUnit.DAYS);
        return new ResultUtil<List<Department>>().setData(list);
    }

    @ApiOperation(value = "增加一级部门标识")
    public List<Department> setInfo(List<Department> list) {
        list.forEach(item -> {
            if(!Objects.equals(CommonConstant.PARENT_ID,item.getParentId())){
                Department parentDepartment = iDepartmentService.getById(item.getParentId());
                if(parentDepartment == null) {
                    item.setParentTitle("无");
                } else {
                    item.setParentTitle(parentDepartment.getTitle());
                }
            }else{
                item.setParentTitle(ONE_LEVEL_PARENT_TITLE);
            }
            QueryWrapper<DepartmentHeader> dh1 = new QueryWrapper<>();
            dh1.eq("department_id",item.getId());
            dh1.eq("type",0);
            List<DepartmentHeader> headerList1 = iDepartmentHeaderService.list(dh1);
            List<String> mainHeaderList = new ArrayList<>();
            for (DepartmentHeader dh : headerList1) {
                mainHeaderList.add(dh.getUserId());
            }
            item.setMainHeader(mainHeaderList);

            QueryWrapper<DepartmentHeader> dh2 = new QueryWrapper<>();
            dh2.eq("department_id",item.getId());
            dh2.eq("type",1);
            List<DepartmentHeader> headerList2 = iDepartmentHeaderService.list(dh2);
            List<String> viceHeaderList = new ArrayList<>();
            for (DepartmentHeader dh : headerList2) {
                viceHeaderList.add(dh.getUserId());
            }
            item.setViceHeader(viceHeaderList);
        });
        return list;
    }

}
