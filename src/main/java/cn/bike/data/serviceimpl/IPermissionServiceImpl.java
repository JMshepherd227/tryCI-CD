package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.PermissionMapper;
import cn.bike.data.entity.Permission;
import cn.bike.data.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IPermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
