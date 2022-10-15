package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.SpotifyDataJdbcRepo;
import songranker.models.Result;
import songranker.models.SpotifyData;

@Service
public class SpotifyDataService {

    @Autowired
    SpotifyDataJdbcRepo repository;

    public Result<?> addSpotifyData(SpotifyData spotifyData) {
        throw new UnsupportedOperationException();
    }
}
