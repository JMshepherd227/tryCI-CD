package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.DictMapper;
import cn.bike.data.entity.Dict;
import cn.bike.data.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IDictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

}
