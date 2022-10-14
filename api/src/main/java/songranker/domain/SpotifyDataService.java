package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.SpotifyDataJdbcRepo;
import songranker.models.SpotifyData;

@Service
public class SpotifyDataService {

    @Autowired
    SpotifyDataJdbcRepo repository;

    public boolean addSpotifyData(SpotifyData spotifyData) {
        throw new UnsupportedOperationException();
    }
}
