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
    public List<AppUser> getAllUsers(){
        List<AppRole> roles = appRoleJdbcRepo.getAllRoles();
        final String sql = "select app_user_id, username, password_hash, display_name, disabled\n"+
                "from app_user;";
        return template.query(sql, new AppUserMapper(roles));
    }

    @Override
    public AppUser getUserByUsername(String username){
        List<AppRole> userRoles = appRoleJdbcRepo.getRolesByUsername(username);

        return template.query("select app_user_id, username, password_hash, display_name, disabled from app_user where username = ?",
                new AppUserMapper(userRoles), username).stream().findFirst().orElse(null);
    }

    @Override
    public AppUser createUser(AppUser appUser){
        //TODO: write to the user_roles bridge table after writing the user to the DB. Give new user role of 'user'. Done with @Transactional in case one of the writings fail

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