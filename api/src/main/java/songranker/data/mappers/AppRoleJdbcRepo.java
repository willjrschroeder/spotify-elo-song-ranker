package songranker.data.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import songranker.models.AppRole;
import songranker.models.AppUser;

import java.util.List;

@Repository
public class AppRoleJdbcRepo implements AppRoleRepo{

    @Autowired
    JdbcTemplate template;
    @Override
    public List<AppRole> getRolesByUsername(String username){
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
