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

        //TODO: Do we need to validate that all of our primary keys are unique? Or do we let the repo layer do that
        // and throw and exception - seems wrong? Probably need service layer checks
        // we'll need repo methods for getPlaylistByUri, getTrackByUri, getAlbumByUri, getArtistByUri, getGenreByName
        return result;
    }

    private Result<?> validatePlaylist(Playlist playlist){
        Result result = new Result();

        if (playlist == null) {
            result.addMessage("Playlist is required", ResultType.INVALID);
        }

        if(playlist.getPlaylistUri().isBlank()) {
            result.addMessage("Playlist URI is required", ResultType.INVALID);
        }

        if(playlist.getPlaylistName().isBlank()) {
            result.addMessage("Playlist name is required", ResultType.INVALID);
        }

        if(playlist.getDescription().length() > 300) {
            result.addMessage("Description must be less than 300 characters", ResultType.INVALID);
        }

        if(playlist.getPlaylistUrl().isBlank()) {
            result.addMessage("Playlist Spotify URL is required", ResultType.INVALID);
        }

        //TODO: need to add a check that the appUser corresponding the appUserId exists AND is not disabled. Need a repo method getUserById

        return result;
    }

    private Result validateTracks(List<Track> tracks){
        Result result = new Result();

        if(tracks == null || tracks.isEmpty()) {
            result.addMessage("All playlists must have tracks", ResultType.INVALID);
        }

        for(Track track : tracks) {
            result = validateTrack(track, result);
        }

        return result;
    }

    private Result validateTrack(Track track, Result result){ // add on to result if there is an error, return result
        if(track.getTrack_uri() == null || track.getTrack_uri().isBlank()) { //TODO: it looks like the front end is passing tracks without these sometimes. (You're Gonna Go Far, Kid from the playlist My Second Playlist!)
            result.addMessage("All tracks must have a Spotify URI", ResultType.INVALID);
        }

        if(track.getTitle().isBlank()) {
            result.addMessage("All tracks must have title", ResultType.INVALID);
        }

        if(track.getTrackDuration() <= 0) {
            result.addMessage("All tracks must have a non-negative duration", ResultType.INVALID);
        }

        if(track.getTrackDuration() <= 0) {
            result.addMessage("All tracks must have a non-negative duration", ResultType.INVALID);
        }

        if(track.getArtists() == null
                || track.getArtists().isEmpty()) {
            result.addMessage("All tracks must have artists", ResultType.INVALID);
        }

        for (Artist artist : track.getArtists()) { // check every artist in the artists array
            result = validateArtist(artist, result);
        }
        if(!result.isSuccess()) {
            return result;
        }

        if(track.getAlbums() == null
                || track.getAlbums().isEmpty()) {
            result.addMessage("All tracks must have an album", ResultType.INVALID); // TODO: find out if this is how singles work. They may have an album which only contains the single, or it may be null/Empty
            return result;
        }

        for (Album album : track.getAlbums()) { //TODO: this should be an [albums], which will always(probably) be length 1. We will have to go into the front end and change how album is packaged into the SpotifyData object
            result = validateAlbum(album, result);
        }

        return result;
    }


    private Result validateArtist(Artist artist, Result result){
        if(artist.getArtistUri().isBlank()) {
            result.addMessage("All artists must have a Spotify URI", ResultType.INVALID);
        }

        if(artist.getArtistName().isBlank()) {
            result.addMessage("All artists must have a name", ResultType.INVALID);
        }

        if(artist.getSpotifyUrl().isBlank()) {
            result.addMessage("All artists must have a Spotify URL", ResultType.INVALID);
        }

        if(artist.getArtistImageLink().isBlank()) {
            result.addMessage("All artists must have a non-blank image link (can be null)", ResultType.INVALID);
        }

        if(artist.getGenres() == null
                || artist.getGenres().isEmpty()) { // this doesn't add an error, not all artists have genres
            return result;
        }

        for (Genre genre : artist.getGenres()) {
            result = validateGenre(genre, result);
        }

        return result;
    }

    private Result validateGenre(Genre genre, Result result){
        return result; //TODO:
        //throw new UnsupportedOperationException();
    }

    private Result validateAlbum(Album album, Result result) {
        return result; //TODO:
        // throw new UnsupportedOperationException();
    }


}
