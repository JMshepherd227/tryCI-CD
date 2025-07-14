package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.DepartmentHeaderMapper;
import cn.bike.data.entity.DepartmentHeader;
import cn.bike.data.service.IDepartmentHeaderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IDepartmentHeaderServiceImpl extends ServiceImpl<DepartmentHeaderMapper, DepartmentHeader> implements IDepartmentHeaderService {

}
