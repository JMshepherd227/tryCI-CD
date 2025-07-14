package cn.bike.basics.security;

import cn.bike.basics.redis.RedisTemplateHelper;
import cn.bike.basics.security.jwt.*;
import cn.bike.basics.utils.SecurityUtil;
import cn.bike.basics.parameter.ZwzLoginProperties;
import cn.bike.basics.security.validate.ImageValidateFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 *
 */
@ApiOperation(value = "SpringSecurity配置类")
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private ZwzLoginProperties zwzLoginProperties;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailHandler authenticationFailHandler;

    @Autowired
    private AccessDeniedHandler zwzAccessDeniedHandler;

    @Autowired
    private ImageValidateFilter imageValidateFilter;

    @Autowired
    private RedisTemplateHelper redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(
                                "/zwz/dictData/getByType/**",
                                "/zwz/file/view/**",
                                "/zwz/user/regist",
                                "/zwz/common/**",
                                "/*/*.js",
                                "/*/*.css",
                                "/*/*.png",
                                "/*/*.ico",
                                "/swagger-ui.html",

                                "/zwz/agreement/getAllUserNicknames"
                        ).permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/zwz/common/needLogin")
                        .loginProcessingUrl("/zwz/login")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailHandler)
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .logout(logout -> logout.permitAll())

                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(zwzAccessDeniedHandler))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageValidateFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userDetailsService.loadUserByUsername(username);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtTokenOncePerRequestFilter authenticationJwtTokenFilter() throws Exception {
        return new JwtTokenOncePerRequestFilter(redisTemplate, securityUtil, zwzLoginProperties);
    }
}
