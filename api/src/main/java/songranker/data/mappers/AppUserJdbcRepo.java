package songranker.data.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import songranker.models.AppRole;
import songranker.models.AppUser;

import java.sql.PreparedStatement;
import java.sql.Statement;
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

    @Override
    public AppUser add(AppUser appUser){

        final String sql = "insert into app_user (username, password_hash, display_name, disabled) "
                + "values (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, appUser.getUsername());
            ps.setString(2, appUser.getPasswordHash());
            ps.setString(3, appUser.getDisplayName());
            ps.setBoolean(4, appUser.isDisabled());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0){
            return null;
        }

        appUser.setAppUserId(keyHolder.getKey().intValue());
        return appUser;
    }


}