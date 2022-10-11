package songranker.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import songranker.models.AppRole;
import songranker.models.AppUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AppUserMapper implements RowMapper<AppUser> {
    List<AppRole> roles; // the list of roles associated with this user. Must be passed at construction
    public AppUserMapper(List<AppRole> roles){
        this.roles = roles;
    }


    // This method builds an app user from a ResultSet. Used to map SQL data to Java objects
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        int appUserId = rs.getInt("app_user_id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");
        String displayName = rs.getString("display_name");
        boolean disabled = rs.getBoolean("disabled");

        return new AppUser(appUserId, username, passwordHash, displayName, disabled, roles); // returns a fully hydrated AppUser
    }
}
