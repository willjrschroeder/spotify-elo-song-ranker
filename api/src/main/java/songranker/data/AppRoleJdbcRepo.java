package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import songranker.data.mappers.AppRoleMapper;
import songranker.models.AppRole;

import java.util.List;

@Repository
public class AppRoleJdbcRepo implements AppRoleRepo{

    @Autowired
    JdbcTemplate template;

    @Override
    public List<AppRole> getAllRoles(){
        final String sql = "select app_role_id, role_name from app_role;";

        return template.query(sql, new AppRoleMapper());
    }





}
