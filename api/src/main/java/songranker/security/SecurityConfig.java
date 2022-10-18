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
    "/api/security/authenticate").permitAll()
            .antMatchers(HttpMethod.POST,
    "/api/security/register").permitAll()
            .antMatchers(HttpMethod.POST,
    "/api/security/refresh_token").permitAll()
            .antMatchers(HttpMethod.GET,
    "/api/playlist/*").authenticated()
            .antMatchers(HttpMethod.POST,
    "/api/spotify_data").authenticated()
            .antMatchers(HttpMethod.DELETE,
    "/api/spotify_data/delete/*/*").authenticated()
            .antMatchers(HttpMethod.GET,
    "/api/track/*").authenticated()
            .antMatchers(HttpMethod.GET,
    "/api/track/playlist/*").authenticated()
            .antMatchers(HttpMethod.PUT,
    "/api/track").authenticated()
            .antMatchers(HttpMethod.GET,
    "/api/track/top10artist/*").authenticated()
            .antMatchers(HttpMethod.GET,
    "/api/track/top10genre/*").authenticated()
            .antMatchers(HttpMethod.GET,
    "/api/track/top10track/*").authenticated()
            .antMatchers(HttpMethod.GET,
    "/api/user").hasAnyRole("admin")
            .antMatchers(HttpMethod.DELETE,
    "/api/user/delete/*").hasAnyRole("admin")
            .antMatchers("/**").denyAll() // deny all requests that reach the end of the chain w/o permission //TODO: turn this back on
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
