package songranker.data.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import songranker.models.AppRole;
import songranker.models.AppUser;

import java.util.List;

public class AppUserJdbcRepo implements AppUserRepo {

    @Autowired
    JdbcTemplate template;

    @Override
    public AppUser getUserByUsername(String username){
        List<AppRole> userRoles = getRolesByUsername(username);

        return template.query("select app_user_id, username, password_hash, display_name, disabled from app_user where username = ?",
                new AppUserMapper(userRoles), username).stream().findFirst().orElse(null);
    }

    private List<AppRole> getRolesByUsername(String username){
        String sql = "select r.app_role_id, r.role_name\n" +
                "from app_role as r\n"+
                "inner join user_roles as ur\n"+
                "\ton r.app_role_id = ur.app_role_id\n"+
                "inner join app_user as au\n"+
                "\ton au.app_user_id = ur.app_user_id\n"+
                "where au.username = ?;";

        return template.query(sql, new AppRoleMapper(), username);
    }

}
