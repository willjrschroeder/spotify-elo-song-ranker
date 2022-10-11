package songranker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import songranker.data.mappers.AppUserJdbcRepo;
import songranker.data.mappers.AppUserRepo;
import songranker.models.AppRole;
import songranker.models.AppUser;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    AppUserRepo repo;

    @Autowired
    PasswordEncoder encoder;

    // This method gets a user from the DB and returns details about their roles to the JwtRequestFilter
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repo.getUserByUsername( username );

        if ( user == null ) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return user;
    }

    public AppUser createUser(String username, String password, String displayName) throws ValidationException {
        validateUsername(username); // checks for non-null < length 50 usernames. Throws exception if not valid
        validatePassword(password); //checks for a < length 8 PW containing digits, numbers, and special chars

        password = encoder.encode(password);

        AppRole role = new AppRole();
        role.setRoleName("user");
        List<AppRole> roles = Arrays.asList(role); // This just creates an empty list of roles to satisfy the super
                                                  // constructor. Not used by the repository method that writes to the DB

        AppUser userToCreate = new AppUser(username, password, displayName, false, roles);
        return repo.createUser(userToCreate); // returns a FH user from the repo
    }

    private void validateUsername(String username) throws ValidationException { //TODO: Add validation to ensure duplicate usernames are not allowed
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }

        if (username.length() > 50) {
            throw new ValidationException("username must be less than 50 characters");
        }
    }

    private void validatePassword(String password) throws ValidationException {
        if (password == null || password.length() < 8) {
            throw new ValidationException("password must be at least 8 characters");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("password must contain a digit, a letter, and a non-digit/non-letter");
        }
    }
}
