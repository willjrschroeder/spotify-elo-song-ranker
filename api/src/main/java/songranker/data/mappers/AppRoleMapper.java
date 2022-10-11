package songranker.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import songranker.models.AppRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppRoleMapper implements RowMapper<AppRole> {

    // This method builds an app role from a ResultSet. Used to map SQL data to Java objects
    @Override
    public AppRole mapRow(ResultSet rs, int rowNum) throws SQLException {
        AppRole toReturn = new AppRole();

        toReturn.setAppRoleId( rs.getInt("app_role_id") );
        toReturn.setRoleName( rs.getString("role_name") );

        return toReturn;
    }
}
