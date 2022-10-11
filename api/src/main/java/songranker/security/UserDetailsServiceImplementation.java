package songranker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import songranker.data.mappers.AppUserRepo;
import songranker.models.AppUser;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    AppUserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repo.getUserByUsername( username );

        if ( user == null ) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return user;
    }
}
