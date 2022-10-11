package songranker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
public class AuthController {

    // The `AuthenticationManager` interface defines a single method `authenticate()`
    // that processes an Authentication request.
    private final AuthenticationManager authManager;
    public AuthController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    // Post mapping for the authentication endpoint. Can be used with no permissions and takes in a username and
    // password as a credentials request body. Returns HTTP status with a JWT Token
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) { // <?> means of generic type
        // The `UsernamePasswordAuthenticationToken` class is an `Authentication` implementation
        // packages our passed in credentials (Username and PW) for the authManager
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));

        try {
            // attempts to authenticate our credentials with the authManager.
            Authentication authentication = authManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {
                HashMap<String, String> map = new HashMap<>();
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

        } catch (AuthenticationException ex) {
            System.out.println(ex);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
