package songranker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    // the configure method configures what roles can access specific API endpoints
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // disables CSRF (Cross Site Request Forgery) checks
        http.csrf().disable();

        // this configures Spring Security to allow
        // CORS related requests (such as preflight checks)
        http.cors();

        http.authorizeRequests()
            .antMatchers(HttpMethod.POST,
    "/api/security/authenticate").permitAll() // allow anyone to access auth endpoint
            .antMatchers("/**").denyAll() // deny all requests that reach the end of the chain w/o permission
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // Stores in-memory, doesn't write users & information to DB
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        var userBuilder = User.withUsername("user")
                .password("user").passwordEncoder(password -> passwordEncoder().encode(password))
                .roles("USER");

        var adminBuilder = User.withUsername("admin")
                .password("admin").passwordEncoder(password -> passwordEncoder().encode(password))
                .roles(("ADMIN"));

        auth.inMemoryAuthentication()
                .withUser(userBuilder)
                .withUser(adminBuilder);
    }

}
