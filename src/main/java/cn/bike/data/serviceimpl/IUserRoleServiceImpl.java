package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.UserRoleMapper;
import cn.bike.data.entity.UserRole;
import cn.bike.data.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IUserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
