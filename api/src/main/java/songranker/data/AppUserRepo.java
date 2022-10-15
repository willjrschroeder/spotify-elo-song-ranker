package songranker.data;

import songranker.models.AppUser;

import java.util.List;

public interface AppUserRepo {
    List<AppUser> getAllUsers();

    AppUser getUserByUsername(String username);

    AppUser getAppUserById(int app_user_id);

    AppUser createUser(AppUser appUser);
}
