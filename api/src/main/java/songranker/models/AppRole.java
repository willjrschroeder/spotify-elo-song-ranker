package songranker.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class AppRole {
    private int appRoleId;
    private String roleName;
    private List<AppUser> roleUsers; // list containing users with the given role

    public int getAppRoleId() {
        return appRoleId;
    }

    public void setAppRoleId(int appRoleId) {
        this.appRoleId = appRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<AppUser> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(List<AppUser> roleUsers) {
        this.roleUsers = roleUsers;
    }

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(roleName);
    }
}
