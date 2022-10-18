package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import songranker.data.mappers.AppRoleMapper;
import songranker.data.mappers.AppUserMapper;
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


    @Override
    public List<AppUser> getAllUsers(){
        List<AppRole> roles = getRoleByRoleName("user");
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
        List<AppRole> userRoles = getRolesByUsername(username);

        return template.query("select app_user_id, username, password_hash, display_name, disabled from app_user where username = ?",
                new AppUserMapper(userRoles), username).stream().findFirst().orElse(null);
    }

    @Override
    public AppUser getAppUserById(int app_user_id){
        List<AppRole> userRoles = getRolesByAppUserId(app_user_id);

        final String sql = "select * from app_user where app_user_id = ?";

        return template.query(sql, new AppUserMapper(userRoles), app_user_id).stream().findFirst().orElse(null);

    }

    @Override
    @Transactional
    public AppUser createUser(AppUser appUser){


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

    @Override
    public boolean deleteAppUserById(int appUserId) {
        final String sql = "update app_user\n"
                +"set disabled = 1\n"
                +"where app_user_id = ?;";

        return (template.update(sql, appUserId)) > 0;
    }

    private void addUserRoles(AppUser appUser){
        final String sql = "insert into user_roles (app_user_id, app_role_id) values (?,?);";


        List<Integer> roleIds = getRoleIds(appUser.getRoles());

        for(Integer roleId : roleIds){
            template.update(sql, appUser.getAppUserId(), roleId);

        }


    }

    private List<Integer> getRoleIds(List<AppRole> roles) {

        final String sql = "select app_role_id from app_role where role_name = ?;";

        List<Integer> roleIds = new ArrayList<>();

        for(AppRole eachRole : roles){
            List<Integer> matchingRoleId = template.query(sql, (resultSet, rowNum) -> {
                return resultSet.getInt("app_role_id");
            } , eachRole.getRoleName());

            roleIds.addAll(matchingRoleId);
        }


        return roleIds;

    }

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

    private List<AppRole> getRolesByAppUserId(int app_user_id){
        String sql = "select r.app_role_id, r.role_name\n" +
                "from app_role as r\n"+
                "inner join user_roles as ur\n"+
                "\ton r.app_role_id = ur.app_role_id\n"+
                "inner join app_user as au\n"+
                "\ton au.app_user_id = ur.app_user_id\n"+
                "where au.app_user_id = ?;";

        return template.query(sql, new AppRoleMapper(), app_user_id);
    }

   public List<AppRole> getRoleByRoleName(String roleName){
        String sql = "select r.app_role_id, r.role_name\n"+
                "from app_role as r\n"+
                "where r.role_name = ?;";

        return template.query(sql, new AppRoleMapper(), roleName);
    }


}