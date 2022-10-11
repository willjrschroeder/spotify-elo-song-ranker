package songranker.data.mappers;

import songranker.models.AppUser;

public interface AppUserRepo {
    AppUser getUserByUsername(String username);

    AppUser add(AppUser appUser);
}
