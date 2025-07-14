package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.UserMapper;
import cn.bike.data.entity.User;
import cn.bike.data.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
