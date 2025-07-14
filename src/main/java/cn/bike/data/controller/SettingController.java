package cn.bike.data.controller;

import cn.bike.basics.baseVo.Result;
import cn.bike.basics.log.LogType;
import cn.bike.basics.log.SystemLog;
import cn.bike.basics.utils.ResultUtil;
import cn.bike.data.entity.Setting;
import cn.bike.data.service.ISettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@Api(tags = "全局设置接口")
@RequestMapping("/zwz/setting")
public class SettingController {

    @Autowired
    private ISettingService iSettingService;

    @SystemLog(about = "查看单个配置", type = LogType.DATA_CENTER,doType = "SETTING-01")
    @RequestMapping(value = "/getOne", method = RequestMethod.GET)
    @ApiOperation(value = "查看单个配置")
    public Result<Setting> getOne(@RequestParam String id) {
        return new ResultUtil<Setting>().setData(iSettingService.getById(id));
    }

    @SystemLog(about = "修改单个配置", type = LogType.DATA_CENTER,doType = "SETTING-02")
    @RequestMapping(value = "/setOne", method = RequestMethod.GET)
    @ApiOperation(value = "修改单个配置")
    public Result<Object> setOne(@RequestParam String id,@RequestParam String value) {
        Setting setting = iSettingService.getById(id);
        if(setting == null) {
            return ResultUtil.error("不存在");
        }
        if(!Objects.equals(value,setting.getValue())) {
            setting.setValue(value);
            iSettingService.saveOrUpdate(setting);
        }
        return ResultUtil.success();
    }
}
