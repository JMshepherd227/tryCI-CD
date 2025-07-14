package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.LogMapper;
import cn.bike.data.entity.Log;
import cn.bike.data.service.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ILogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

}
