package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.DepartmentMapper;
import cn.bike.data.entity.Department;
import cn.bike.data.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *
 *
 */
@Service
public class IDepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

}
