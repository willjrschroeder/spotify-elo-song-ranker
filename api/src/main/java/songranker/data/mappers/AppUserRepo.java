package songranker.data.mappers;

import songranker.models.AppUser;

import java.util.List;

public interface AppUserRepo {
    List<AppUser> getAllUsers();

    AppUser getUserByUsername(String username);

    AppUser createUser(AppUser appUser);
}
