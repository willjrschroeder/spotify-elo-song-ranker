package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.SpotifyDataJdbcRepo;
import songranker.models.*;

import java.util.List;

@Service
public class SpotifyDataService {

    @Autowired
    SpotifyDataJdbcRepo repository;

    public Result<?> addSpotifyData(SpotifyData spotifyData) {
        // TODO: Do we need a check for spotifyData being null first?

        Result result = validatePlaylist(spotifyData.getPlaylist());
        if(!result.isSuccess()) { // Error messages are added if any checks are unsuccessful
            return result;
        }

        result = validateTracks(spotifyData.getTracks());
        if(!result.isSuccess()) {
            return result;
        }

        boolean addResult = repository.addSpotifyData(spotifyData);
        if(!addResult) {
            result.addMessage("There was an error writing to the database", ResultType.INVALID); // TODO: Not sure if this is necessary. Depends on what we decide to return from the repo method
        }

        return result;
    }

    private Result<?> validatePlaylist(Playlist playlist){
        throw new UnsupportedOperationException();
    }

    private Result<?> validateTracks(List<Track> tracks){
        throw new UnsupportedOperationException();
    }

    private Result<?> validateTrack(Track track){
        throw new UnsupportedOperationException();
    }

    private Result<?> validateArtists(List<Artist> artists){
        throw new UnsupportedOperationException();
    }

    private Result<?> validateGenres(List<Genre> genres){
        throw new UnsupportedOperationException();
    }
}
