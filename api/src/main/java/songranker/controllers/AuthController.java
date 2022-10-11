package songranker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import songranker.models.AppUser;
import songranker.security.JwtConverter;

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

    @Autowired
    JwtConverter converter; // obj for converting JWT tokens to users and vice a versa

    // Post mapping for the authentication endpoint. Can be accessed with no authentications. Takes in a username and
    // password as a credentials request body. Returns 200 with a JWT Token on success, else 403
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) { // <?> means of generic type
        // The `UsernamePasswordAuthenticationToken` class is an `Authentication` implementation
        // packages our passed in credentials (Username and PW) for the authManager
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));

        try {
            // attempts to authenticate user credentials with the authManager.
            Authentication authentication = authManager.authenticate(authToken);

            AppUser user = (AppUser)authentication.getPrincipal(); // gets an AppUser from the authentication details
            SecurityContextHolder.getContext().setAuthentication(authentication); // sets authentication for the request
                                                                                  // from the user's roles

            // Builds a JWT token from the AppUser, which is to be returned to the front end
            String token = converter.buildJwt(user);

            if (authentication.isAuthenticated()) { // if the user was successfully authenticated, return their JWT
                HashMap<String, String> tokenHolder = new HashMap<>();
                tokenHolder.put("jwt_token", token); // returns a token in a map with a 'jwt_token' label for convenience
                return new ResponseEntity<>(tokenHolder, HttpStatus.OK);
            }

        } catch (AuthenticationException ex) {
            System.out.println(ex);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // Post mapping for the token refresh endpoint. A currently valid JWT token is supplied,
    // and a refreshed JWT token will be returned
    @PostMapping("/refresh_token")
    public ResponseEntity<Map<String,String>> refreshToken() {

        // This method will take no body, and it will return a new token if the passed JWT auth token is still valid.
        // Returns a 403 with some explanation if the token is not valid
        // Can use validation bearer token to parse a user, and then use the user to issue a new token

        throw new UnsupportedOperationException();
    }
}
