package main.security;

import main.security.jwt.JwtSecurityConfigurer;
import main.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                    .csrf().disable()
                    .formLogin().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers("/wc/auth/signin").permitAll()
                    .antMatchers("/wc/auth/registration").permitAll()
                    .antMatchers("/wc/auth/logout").permitAll()
                    .antMatchers("/wc/auth/checkToken/{token}").permitAll()
                    .antMatchers("/wc/auth/getRoleCurrentUser/{token}").permitAll()
                    .antMatchers(HttpMethod.GET, "/wc/good/getAll").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/good/{id}").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/good/getByName/{name}").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/good/add").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/good/delete/{id}").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/sale/getAll").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/sale/{id}").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/sale/getByName/{name}").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/sale/add").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/sale/delete/{id}").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/warehouses/getAll/1").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/warehouses/getAll/2").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/warehouses/getByName/1/{name}").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/warehouses/getByName/2/{name}").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/warehouses/add/1").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/warehouses/add/2").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/warehouses/delete/1/{id}").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/wc/warehouses/delete/2/{id}").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/warehouses/getSize/1").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/wc/warehouses/getSize/2").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .apply(new JwtSecurityConfigurer(jwtTokenProvider));
    }
}
