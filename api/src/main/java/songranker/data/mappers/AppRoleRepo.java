package songranker.data.mappers;

import songranker.models.AppRole;

import java.util.List;


public interface AppRoleRepo {

    List<AppRole> getRolesByUsername(String username);
}
