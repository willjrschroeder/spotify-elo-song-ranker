package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.AppUserJdbcRepo;
import songranker.data.PlaylistJdbcRepo;
import songranker.models.AppUser;
import songranker.models.Playlist;
import songranker.models.Result;
import songranker.models.ResultType;

import java.util.List;
@Service
public class PlaylistService {

    @Autowired
    PlaylistJdbcRepo repository;

    @Autowired
    AppUserJdbcRepo appUserRepo;

    public Result<List<Playlist>> getPlaylistsByAppUserId(int appUserId) {
        Result result = validateUser(appUserId);
        if (!result.isSuccess()) {
            return result;
        }

        List<Playlist> playlists = repository.getPlaylistsByAppUserId(appUserId);
        result.setPayload(playlists);
        return result;
    }

    private Result validateUser(int appUserId) {
        AppUser user = appUserRepo.getAppUserById(appUserId);
        Result result = new Result();

        if (user == null) {
            result.addMessage("User does not exist", ResultType.INVALID);
            return result;
        }

        if (user.isDisabled()) {
            result.addMessage("User is disabled in the database", ResultType.INVALID);
        }
        return result;
    }
}
