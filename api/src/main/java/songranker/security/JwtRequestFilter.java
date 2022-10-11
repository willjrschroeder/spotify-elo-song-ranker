package songranker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends BasicAuthenticationFilter {

    @Autowired
    UserDetailsServiceImplementation service;

    @Autowired
    JwtConverter converter;

    public JwtRequestFilter (AuthenticationManager authenticationManager, JwtConverter converter) {
        super(authenticationManager); // satisfies super class. auth manager managers the user details and roles
        this.converter = converter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authorization = request.getHeader("Authorization"); // String holds the JWT token passed in the req.

        if (authorization != null && authorization.startsWith("Bearer ")) { // ensure header is in the right format
            authorization = authorization.substring(7); // remove 'Bearer ' from the auth string to get plain token

            Jws<Claims> claims = converter.parseJwt( authorization );

            String username = claims.getBody().getSubject();

            UserDetails matchingUser = service.loadUserByUsername(username); // pulls user details from repo


            // Builds a token containing the user along with their authorities
            UsernamePasswordAuthenticationToken rawToken = new UsernamePasswordAuthenticationToken(
                    matchingUser,
                    null,
                    matchingUser.getAuthorities()
            );

            // Attaches the user's authorities to the context of the request.
            // When request is passed along, other filters will be able to check the user's authorities
            // to grant/deny permissions to specific endpoints
            SecurityContextHolder.getContext().setAuthentication(rawToken);
        } else {
            // this is the case where the user did not provide a token
            // they could be first logging in or trying to access a public endpoint
            // in either case, they get no authentications added and the request should be passed on
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        filterChain.doFilter(request, response);
    }
}
