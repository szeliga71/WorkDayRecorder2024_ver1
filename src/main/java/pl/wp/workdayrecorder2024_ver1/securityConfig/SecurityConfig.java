package pl.wp.gameofthroneapplication.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;;
import pl.wp.gameofthroneapplication.service.CustomUserSecurityService;
;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


private final CustomUserSecurityService customUserSecurityService;
private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

    public SecurityConfig(CustomUserSecurityService customUserSecurityService) {
        this.customUserSecurityService = customUserSecurityService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security.
                authorizeHttpRequests((request)->request
                        .requestMatchers("/","/register").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .formLogin((form)->form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/books" )
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout ->logout

                        .logoutSuccessUrl("/logoutPage")
                        .permitAll());
        return security.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider(){
        DaoAuthenticationProvider dao=new DaoAuthenticationProvider();
        dao.setUserDetailsService(customUserSecurityService);
        dao.setPasswordEncoder(encoder);
        return dao;
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity)throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder=httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider());
        return authenticationManagerBuilder.build();
    }

}
