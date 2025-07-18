package cn.bike.data.utils;

import io.swagger.annotations.Api;

import java.util.Objects;

/**
 *  
 *
 */
@Api(tags = "判断为空工具类")
public class NullUtils {
    public static boolean isNull(String str){
        if(str == null || Objects.equals("",str) || Objects.equals("null",str) || Objects.equals("undefined",str)) {
            return true;
        }
        return false;
    }
}
