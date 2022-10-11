package songranker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import songranker.data.mappers.AppUserJdbcRepo;
import songranker.data.mappers.AppUserRepo;
import songranker.models.AppUser;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    AppUserRepo repo;

    // This method gets a user from the DB and returns details about their roles to the JwtRequestFilter
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repo.getUserByUsername( username );

        if ( user == null ) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return user;
    }
}
