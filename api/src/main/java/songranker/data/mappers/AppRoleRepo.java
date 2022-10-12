package songranker.data.mappers;

import songranker.models.AppRole;

import java.util.List;


public interface AppRoleRepo {

    List<AppRole> getAllRoles();

    List<AppRole> getRolesByUsername(String username);

    List<AppRole> getRoleByRoleName(String roleName);
}
