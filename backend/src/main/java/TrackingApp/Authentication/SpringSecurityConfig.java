package TrackingApp.Authentication;

import TrackingApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().authorizeRequests()
                .antMatchers("/register_user").permitAll()
                .antMatchers("/authenticate").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .authenticationEntryPoint(authEntryPoint);
    }



}
