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

    // authentication endpoint. Can be used with no permissions and takes in a username and password as a credentials
    // request body. Returns HTTP status with a JWT Token

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) { // <?> means of generic type

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            if(authentication.isAuthenticated()){
                HashMap<String, String> map = new HashMap<>();
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (AuthenticationException ex){
            System.out.println(ex);
        }

        return new ResponseEntity<>((HttpStatus.FORBIDDEN));
    }
}
