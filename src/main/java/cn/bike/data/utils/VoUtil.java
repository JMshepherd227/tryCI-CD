package cn.bike.data.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.bike.basics.exception.ZwzException;
import cn.bike.data.entity.Permission;
import cn.bike.data.entity.Setting;
import cn.bike.data.service.ISettingService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.bike.data.vo.MenuVo;
import cn.bike.data.vo.OssSettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;


@Api(tags = "本地文件工具类")
@Component
public class VoUtil implements FileManage {

    @Autowired
    private ISettingService iSettingService;

    private static final String LOCAL_FILE_PATH_STEP = "/";

    public static void view(String url, HttpServletResponse response){
        File viewFile = new File(url);
        if (!viewFile.exists()) {
            throw new ZwzException("没有文件");
        }
        try (FileInputStream is = new FileInputStream(viewFile); BufferedInputStream bis = new BufferedInputStream(is)) {
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buf)) > 0) {
                out.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new ZwzException("读取下载文件出错" + e);
        }
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String key, MultipartFile file) {
        OssSettingVo os = getOssSetting();
        String day = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String path = os.getFilePath() + LOCAL_FILE_PATH_STEP + day;
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File f = new File(path + LOCAL_FILE_PATH_STEP + key);
        if(f.exists()){
            throw new ZwzException("文件名称重复");
        }
        try {
            file.transferTo(f);
            return path + LOCAL_FILE_PATH_STEP + key;
        } catch (IOException e) {
            throw new ZwzException("上传文件出错 " + e);
        }
    }

    @Override
    public void deleteFile(String url) {
        FileUtil.del(new File(url));
    }

    @Override
    public String copyFile(String url, String toKey) {
        File copyFile = new File(url);
        String newUrl = copyFile.getParentFile() + LOCAL_FILE_PATH_STEP + toKey;
        FileUtil.copy(copyFile, new File(newUrl), true);
        return newUrl;
    }

    @Override
    public String renameFile(String url, String toKey) {
        File renameFile = new File(url);
        FileUtil.rename(renameFile, toKey, false, true);
        return renameFile.getParentFile() + LOCAL_FILE_PATH_STEP + toKey;
    }

    @Override
    public OssSettingVo getOssSetting() {
        Setting s1 = iSettingService.getById("FILE_VIEW");
        Setting s2 = iSettingService.getById("FILE_HTTP");
        Setting s3 = iSettingService.getById("FILE_PATH");
        if(s1 == null || s1 == null || s1 == null) {
            return null;
        }
        return new OssSettingVo(s1.getValue(),s2.getValue(),s3.getValue());
    }

    @ApiOperation(value = "菜单转换VO类转换")
    public static MenuVo permissionToMenuVo(Permission permission){
        MenuVo vo = new MenuVo();
        BeanUtil.copyProperties(permission, vo);
        return vo;
    }
}
