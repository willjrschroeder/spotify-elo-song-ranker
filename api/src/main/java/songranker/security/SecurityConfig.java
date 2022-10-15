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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtRequestFilter filter;

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
            .antMatchers(HttpMethod.POST,
    "/api/security/register").permitAll() // allow anyone to access register endpoint
            .antMatchers(HttpMethod.POST,
    "/api/spotify_data").authenticated() // allow only users and admins to access write SpotifyData endpoint
            .antMatchers("/**").denyAll() // deny all requests that reach the end of the chain w/o permission
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


}
