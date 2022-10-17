package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import songranker.data.AppUserJdbcRepo;
import songranker.models.AppUser;
import songranker.models.Result;
import songranker.models.ResultType;

import java.util.List;

@Service
public class AppUserService {

    @Autowired
    AppUserJdbcRepo repository;
    public Result<List<AppUser>> getAllAppUsers() {
        Result result = new Result();

        List<AppUser> users = repository.getAllUsers();

        if(users == null) {
            result.addMessage("Error retrieving users from the database", ResultType.INVALID);
            return result;
        }

        result.setPayload(users);
        return result;
    }

    public Result deleteAppUserById(int appUserId) {
        Result result = new Result();

        AppUser user = repository.getAppUserById(appUserId);

        if(user == null || user.isDisabled()) {
            result.addMessage("User could not be found in the database", ResultType.INVALID);
            return result;
        }

        boolean success = repository.deleteAppUserById(appUserId);

        if(!success){
            result.addMessage("Error deleting user from the database", ResultType.INVALID);
        }
        return result;
    }
}
