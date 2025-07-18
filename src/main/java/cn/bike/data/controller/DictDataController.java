package cn.bike.data.controller;

import cn.bike.basics.log.LogType;
import cn.bike.basics.log.SystemLog;
import cn.bike.basics.utils.PageUtil;
import cn.bike.basics.utils.ResultUtil;
import cn.bike.basics.baseVo.PageVo;
import cn.bike.basics.baseVo.Result;
import cn.bike.data.entity.Dict;
import cn.bike.data.entity.DictData;
import cn.bike.data.service.IDictDataService;
import cn.bike.data.service.IDictService;
import cn.bike.data.utils.NullUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@RestController
@RequestMapping("/zwz/dictData")
@Api(tags = "字典数据值接口")
@Transactional
public class DictDataController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IDictService iDictService;

    @Autowired
    private IDictDataService iDictDataService;

    private static final String REDIS_DIST_DATA_PRE_STR = "dictData::";

    @SystemLog(about = "查询单个数据字典的值", type = LogType.DATA_CENTER,doType = "DICT_DATA-01")
    @RequestMapping(value = "/getByType/{type}", method = RequestMethod.GET)
    @ApiOperation(value = "查询单个数据字典的值")
    public Result<Object> getByType(@PathVariable String type){
        QueryWrapper<Dict> qw = new QueryWrapper<>();
        qw.eq("type",type);
        Dict selectDict = iDictService.getOne(qw);
        if (selectDict == null) {
            return ResultUtil.error("字典 "+ type +" 不存在");
        }
        QueryWrapper<DictData> dataQw = new QueryWrapper<>();
        dataQw.eq("dict_id",selectDict.getId());
        return ResultUtil.data(iDictDataService.list(dataQw));
    }

    @SystemLog(about = "查询数据字典值", type = LogType.DATA_CENTER,doType = "DICT_DATA-02")
    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "查询数据字典值")
    public Result<IPage<DictData>> getByCondition(@ModelAttribute DictData dictData, @ModelAttribute PageVo page) {
        QueryWrapper<DictData> qw = new QueryWrapper<>();
        if(!NullUtils.isNull(dictData.getDictId())) {
            qw.eq("dict_id",dictData.getDictId());
        }
        if(!Objects.equals(null,dictData.getStatus())) {
            qw.eq("status",dictData.getStatus());
        }
        if(!NullUtils.isNull(dictData.getTitle())) {
            qw.like("title",dictData.getTitle());
        }
        if(!NullUtils.isNull(dictData.getValue())) {
            qw.like("value",dictData.getValue());
        }
        if(!NullUtils.isNull(dictData.getDescription())) {
            qw.like("description",dictData.getDescription());
        }
        IPage<DictData> data = iDictDataService.page(PageUtil.initMpPage(page),qw);
        for (DictData dd : data.getRecords()) {
            if(dd != null) {
                Dict dict = iDictService.getById(dd.getDictId());
                if(dict != null) {
                    dd.setDictName(dict.getTitle());
                }
            }
        }
        return new ResultUtil<IPage<DictData>>().setData(data);
    }

}
