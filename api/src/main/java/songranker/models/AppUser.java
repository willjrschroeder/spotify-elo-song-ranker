package songranker.models;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AppUser extends User {
    private int appUserId;

    private String username;

    private String passwordHash;

    private String displayName;

    private boolean disabled;

    private List<AppRole> roles;

    // AppUser constructor. Fills out the custom field variables and satisfies the constructor
    public AppUser( int appUserId, String username, String passwordHash, String displayName, boolean disabled, List<AppRole> roles  ){
        super( username, passwordHash, roles.stream().map( r -> r.getAuthority()).collect(Collectors.toList()));
        this.appUserId = appUserId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.displayName = displayName;
        this.disabled = disabled;
    }

    // This constructor creates a PH AppUser without an ID. Used for creating a new AppUser in the DB
    public AppUser( String username, String passwordHash, String displayName, boolean disabled, List<AppRole> roles){
        super( username, passwordHash, roles.stream().map( r -> r.getAuthority()).collect(Collectors.toList()));

        this.username = username;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.displayName = displayName;
        this.disabled = disabled;
    }

    public List<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AppRole> roles) {
        this.roles = roles;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
