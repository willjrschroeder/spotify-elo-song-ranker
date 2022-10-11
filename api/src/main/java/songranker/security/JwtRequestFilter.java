package songranker.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtRequestFilter extends BasicAuthenticationFilter {
    private final JwtConverter converter;

    public JwtRequestFilter (AuthenticationManager authenticationManager, JwtConverter converter) {
        super(authenticationManager); // satisfies super class. auth manager managers the user details and roles
        this.converter = converter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authorization = request.getHeader("Authorization"); // String holds the JWT token passed in the req.
        if (authorization != null && authorization.startsWith("Bearer ")) { // ensure header is in the right format


        }

    }
}
