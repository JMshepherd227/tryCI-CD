package cn.bike.basics.security.validate;

import cn.bike.basics.parameter.CaptchaProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 
 */
@ApiOperation(value = "验证码过滤类")
@Configuration
public class ImageValidateFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PathMatcher pathMatcher;

    private static final boolean RESPONSE_FAIL_FLAG = false;

    private static final int RESPONSE_CODE_FAIL_CODE = 500;

    @Override
    @ApiOperation(value = "验证码过滤")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Boolean filterFlag = false;
        for(String requestURI : captchaProperties.getVerification()){
            if(pathMatcher.match(requestURI, request.getRequestURI())){
                filterFlag = true;
                break;
            }
        }
        if(!filterFlag) {
            filterChain.doFilter(request, response);
            return;
        }
        String verificationCodeId = request.getParameter("captchaId");
        String userInputCode = request.getParameter("code");

        redisTemplate.delete(verificationCodeId);
        filterChain.doFilter(request, response);
    }
}
