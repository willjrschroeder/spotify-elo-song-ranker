package songranker.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/security")
public class AuthController {

    // authentication endpoint. Can be used with no permissions and takes in a username and password as a credentials
    // request body. Returns HTTP status with a JWT Token
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) { // <?> means of generic type
        throw new UnsupportedOperationException();
    }
}
