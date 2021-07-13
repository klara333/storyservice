package com.nefertiti.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nefertiti.oath.CustomOAuth2UserService;
import com.nefertiti.oath.OAuth2LoginSuccessHandler;
import com.nefertiti.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .authorizeRequests()
        .antMatchers("/oauth2/**").permitAll()
            .anyRequest().permitAll()
            .and()
            .formLogin()
            	.loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)             
            .and()
            .oauth2Login()
            	.loginPage("/login")
            	.userInfoEndpoint()
            	.userService(oAuth2UserService)
            .and()
            	.successHandler(oAuth2LoginSuccessHandler)
            .and()
            .logout().logoutSuccessUrl("/").permitAll()
            .and()
            .rememberMe().rememberMeParameter("remember-me")
            ;
    }
   
    @Autowired
    private CustomOAuth2UserService oAuth2UserService;
    
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
     
}
