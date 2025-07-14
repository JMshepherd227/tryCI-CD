package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.DictDataMapper;
import cn.bike.data.entity.DictData;
import cn.bike.data.service.IDictDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 *
 */
@Service
public class IDictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements IDictDataService {

}
