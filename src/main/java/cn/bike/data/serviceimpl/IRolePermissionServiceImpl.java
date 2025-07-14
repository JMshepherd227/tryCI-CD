package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.RolePermissionMapper;
import cn.bike.data.entity.RolePermission;
import cn.bike.data.service.IRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IRolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

}
