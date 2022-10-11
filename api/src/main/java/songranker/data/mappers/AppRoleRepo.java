package songranker.data.mappers;

import songranker.models.AppUser;

public interface AppRoleRepo {

    AppUser getUserByUsername(String username);
}
