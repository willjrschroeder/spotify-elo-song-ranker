package songranker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import songranker.models.AppUser;
import songranker.models.RegistrationFields;
import songranker.security.JwtConverter;
import songranker.security.UserDetailsServiceImplementation;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
@CrossOrigin
public class AuthController {

    // The `AuthenticationManager` interface defines a single method `authenticate()`
    // that processes an Authentication request.
    private final AuthenticationManager authManager;
    public AuthController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Autowired
    JwtConverter converter; // obj for converting JWT tokens to users and vice a versa

    @Autowired
    UserDetailsServiceImplementation service; // AppUser service layer

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

    // Post mapping for registering a new user. Validates passed in credentials (non-duplicate username, valid password)
    // Writes passed in user credentials to the database
    // returns a 201 created on success
    // returns a 400 bad request on validation failure
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationFields fields){
        AppUser user = null;
        try {
            String username = fields.getUsername();
            String password = fields.getPassword();
            String displayName = fields.getDisplayName();
            user = service.createUser(username, password, displayName); // TODO: Not currently returning any information about the created user.
                                                                //Not sure if necessary or not

        } catch (ValidationException ex) {
            String test = ex.getMessage();
            return new ResponseEntity<>(test, HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>("The provided username already exists", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED); // This is the happy case
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
