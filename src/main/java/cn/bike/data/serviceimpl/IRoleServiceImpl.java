package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.RoleMapper;
import cn.bike.data.entity.Role;
import cn.bike.data.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
