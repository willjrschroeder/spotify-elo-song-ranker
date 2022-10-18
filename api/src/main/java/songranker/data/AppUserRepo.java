package songranker.data;

import songranker.models.AppRole;
import songranker.models.AppUser;

import java.util.List;

public interface AppUserRepo {
    List<AppUser> getAllUsers();

    List<AppUser> getAllActiveUsers();

    AppUser getUserByUsername(String username);

    AppUser getAppUserById(int app_user_id);

    AppUser createUser(AppUser appUser);

    boolean deleteAppUserById(int appUserId);

    List<AppRole> getRolesByUsername(String username);
}
