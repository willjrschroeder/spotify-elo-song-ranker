package songranker.data.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import songranker.models.AppRole;
import songranker.models.AppUser;

import java.util.List;

@Repository
public class AppUserJdbcRepo implements AppUserRepo {

    @Autowired
    JdbcTemplate template;

    @Autowired
    AppRoleJdbcRepo appRoleJdbcRepo;

    @Override
    public AppUser getUserByUsername(String username){
        List<AppRole> userRoles = appRoleJdbcRepo.getRolesByUsername(username);

        return template.query("select app_user_id, username, password_hash, display_name, disabled from app_user where username = ?",
                new AppUserMapper(userRoles), username).stream().findFirst().orElse(null);
    }



}
