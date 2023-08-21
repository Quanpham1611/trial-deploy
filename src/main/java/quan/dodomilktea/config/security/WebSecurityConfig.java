package quan.dodomilktea.config.security;

import org.hibernate.validator.internal.util.TypeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import quan.dodomilktea.globalenum.ERoles;
import quan.dodomilktea.jwt.JwtTokenFilter;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@CrossOrigin(origins = "http://localhost:3000")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired DataSource  dataSource;

    @Autowired JwtTokenFilter jwtTokenFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomAccountDetailService();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.cors().and().sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors().and().exceptionHandling(handling -> handling.authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                }
        ));


        http.cors().and().authorizeHttpRequests(requests -> requests
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/forgot-password/**").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority(ERoles.ADMIN.toString())
                .antMatchers("/shipper/**").hasAnyAuthority(ERoles.SHIPPER.toString())
                .antMatchers("/staff-manager/**").hasAnyAuthority(ERoles.STAFF_MANAGER.toString())
                .antMatchers("/customer/**").hasAnyAuthority(ERoles.CUSTOMER.toString())
                .anyRequest().authenticated());


        http.cors().and().addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
