package songranker.data.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import songranker.models.AppRole;
import songranker.models.AppUser;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AppUserJdbcRepo implements AppUserRepo {

    @Autowired
    JdbcTemplate template;

    @Autowired
    AppRoleJdbcRepo appRoleJdbcRepo;


    @Override
    public List<AppUser> getAllUsers(){
        List<AppRole> roles = appRoleJdbcRepo.getRoleByRoleName("user");
        final String sql = "select au.app_user_id, au.username, au.password_hash, au.display_name, au.disabled\n"+
                "from app_user as au\n"+
                "inner join user_roles as ur\n"+
                "on au.app_user_id = ur.app_user_id\n"+
                "inner join app_role as r\n"+
                "on r.app_role_id = ur.app_role_id\n"+
                "where r.role_name = ?;";
        return template.query(sql, new AppUserMapper(roles), "user");
    }


    @Override
    public AppUser getUserByUsername(String username){
        List<AppRole> userRoles = appRoleJdbcRepo.getRolesByUsername(username);

        return template.query("select app_user_id, username, password_hash, display_name, disabled from app_user where username = ?",
                new AppUserMapper(userRoles), username).stream().findFirst().orElse(null);
    }

    @Override
    @Transactional
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

        addUserRoles(appUser);

        return appUser;
    }

    private void addUserRoles(AppUser appUser){
        final String sql = "insert into user_roles (app_user_id, app_role_id) values (?,?);";


        List<Integer> roleIds = getRoleIds(appUser.getRoles());

        for(Integer roleId : roleIds){
            template.update(sql, appUser.getAppUserId(), roleId);
        }


    }

    private List<Integer> getRoleIds(List<AppRole> roles) {

        //Todo: ask DB for Ids

        List<Integer> roleId = new ArrayList<>();
        roleId.add(1);

        return roleId;

    }


}