package com.github.superzhc.flink.manage.config;

import com.github.superzhc.flink.manage.handler.MyAuthenticationFailureHandler;
import com.github.superzhc.flink.manage.handler.MyAuthenticationSuccessHandler;
import com.github.superzhc.flink.manage.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author superz
 * @create 2021/4/25 16:13
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/lib/**", "/page/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // 配置登录页并允许访问
                .formLogin().loginPage("/login.html").loginProcessingUrl("/login").successHandler(new MyAuthenticationSuccessHandler()).failureHandler(new MyAuthenticationFailureHandler()).permitAll()
                // 配置登出页面
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login.html")
                // 开放接口访问权限，不需要登录授权就可以访问
                .and().authorizeRequests().antMatchers("/login.html").permitAll()
                // 其余所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        //解决非thymeleaf的form表单提交被拦截问题
        http.csrf().disable();
        // iframe 情况下不设置这个会报错
        http.headers().frameOptions().sameOrigin();

        //解决中文乱码问题
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);

        //开启记住我功能，登陆成功以后，将cookie发给浏览器保存，以后访问页面带上这个cookie，只要通过检查就可以免登录
        http.rememberMe().rememberMeParameter("remember-me");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(systemUserService()).passwordEncoder(passwordEncoder());
        //也可以将用户名密码写在内存，不推荐
        // auth.inMemoryAuthentication().withUser("admin").password("111111").roles("USER");
    }

    /**
     * 设置用户密码的加密方式为BCrypt加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 从数据库中读取用户信息
     */
    @Bean
    public UserDetailsService systemUserService() {
        return new UserDetailsServiceImpl();
    }
}