package songranker.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

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
}
