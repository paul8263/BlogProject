package com.paultech.config;

import com.paultech.core.SecurityHandler.EntryPointUnauthorizedHandler;
import com.paultech.core.SecurityHandler.FailureHandler;
import com.paultech.core.SecurityHandler.LogoutHandler;
import com.paultech.core.SecurityHandler.SuccessHandler;
import com.paultech.core.SecurityUserService.MyBlogUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by paulzhang on 17/04/15.
 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.paultech.core.SecurityUserService","com.paultech.core.SecurityHandler"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyBlogUserDetailsService myBlogUserDetailsService;

    @Autowired
    private SuccessHandler successHandler;

    @Autowired
    private FailureHandler failureHandler;

    @Autowired
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    @Autowired
    private LogoutHandler logoutHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("username").password("password").roles("USER");
        auth.userDetailsService(myBlogUserDetailsService);
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("app/**")
                .permitAll()
                .antMatchers("login")
                .permitAll()
                .antMatchers("logout")
                .permitAll()
                .and()
            .csrf()
                .disable()
            .formLogin()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutHandler)
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(entryPointUnauthorizedHandler);
    }
}
