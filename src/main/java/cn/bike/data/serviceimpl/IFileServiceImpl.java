package cn.bike.data.serviceimpl;

import cn.bike.data.dao.mapper.FileMapper;
import cn.bike.data.entity.File;
import cn.bike.data.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class IFileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
