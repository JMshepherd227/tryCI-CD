package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.SettingMapper;
import cn.bike.data.entity.Setting;
import cn.bike.data.service.ISettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ISettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements ISettingService {

}
