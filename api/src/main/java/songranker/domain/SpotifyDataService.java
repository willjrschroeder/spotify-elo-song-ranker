package songranker.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import songranker.data.AppUserJdbcRepo;
import songranker.data.SpotifyDataJdbcRepo;
import songranker.models.*;

import java.util.List;

@Service
public class SpotifyDataService {

    @Autowired
    SpotifyDataJdbcRepo repository;

    @Autowired
    AppUserJdbcRepo appUserJdbcRepo;

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
        Result result = new Result();

        if (playlist == null) {
            result.addMessage("Playlist is required", ResultType.INVALID);
            return result;
        }

        Playlist duplicatePlaylist = repository.getPlaylistByPlaylistUri(playlist.getPlaylistUri(), playlist.getAppUserId());
        if (duplicatePlaylist != null) {
            result.addMessage("Playlist is already added to the database", ResultType.INVALID);
            return result;
        }

        AppUser user = appUserJdbcRepo.getAppUserById(playlist.getAppUserId());
        if(user == null) {
            result.addMessage("Playlist must contain an existing appUserId", ResultType.INVALID);
            return result;
        }

        if(user.isDisabled()) {
            result.addMessage("Playlist must contain a non-disabled appUserId", ResultType.INVALID);
            return result;
        }

        if(playlist.getPlaylistUri() == null || playlist.getPlaylistUri().isBlank()) {
            result.addMessage("Playlist URI is required", ResultType.INVALID);
        }

        if(playlist.getPlaylistName() == null || playlist.getPlaylistName().isBlank()) {
            result.addMessage("Playlist name is required", ResultType.INVALID);
        }

        if(playlist.getDescription() != null) {
            if (playlist.getDescription().length() > 300) {
                result.addMessage("Description must be less than 300 characters", ResultType.INVALID);
            }
        }

        if(playlist.getPlaylistUrl() == null || playlist.getPlaylistUrl().isBlank()) {
            result.addMessage("Playlist Spotify url is required", ResultType.INVALID);
        }

        //TODO: need to add a check that the appUser corresponding the appUserId exists AND is not disabled. Need a repo method getUserById

        return result;
    }

    private Result validateTracks(List<Track> tracks){
        Result result = new Result();

        if(tracks == null || tracks.isEmpty()) {
            result.addMessage("All playlists must have tracks", ResultType.INVALID);
            return result;
        }

        for(Track track : tracks) {
            result = validateTrack(track, result);
        }

        return result;
    }

    private Result validateTrack(Track track, Result result){ // add on to result if there is an error, return result
        if(track.getTrack_uri() == null || track.getTrack_uri().isBlank()) {
            result.addMessage("All tracks must have a Spotify URI", ResultType.INVALID);
        }

        if(track.getTitle() == null || track.getTitle().isBlank()) {
            result.addMessage("All tracks must have a title", ResultType.INVALID);
        }

        if(track.getSpotifyUrl() == null || track.getSpotifyUrl().isBlank()) {
            result.addMessage("All tracks must have a Spotify url", ResultType.INVALID);
        }

        if(track.getTrackDuration() <= 0) {
            result.addMessage("All tracks must have a non-negative duration", ResultType.INVALID);
        }

        if(track.getArtists() == null
                || track.getArtists().isEmpty()) {
            result.addMessage("All tracks must have artists", ResultType.INVALID);
            return result;
        }

        for (Artist artist : track.getArtists()) { // check every artist in the artists array
            result = validateArtist(artist, result);
        }
        if(!result.isSuccess()) {
            return result;
        }

        if(track.getAlbums() == null
                || track.getAlbums().isEmpty()) {
            result.addMessage("All tracks must have an album", ResultType.INVALID);
            return result;
        }

        for (Album album : track.getAlbums()) {
            result = validateAlbum(album, result);
        }

        return result;
    }


    private Result validateArtist(Artist artist, Result result){
        if(artist.getArtistUri() == null || artist.getArtistUri().isBlank()) {
            result.addMessage("All artists must have a Spotify URI", ResultType.INVALID);
        }

        if(artist.getArtistName() == null || artist.getArtistName().isBlank()) {
            result.addMessage("All artists must have a name", ResultType.INVALID);
        }

        if(artist.getSpotifyUrl() == null || artist.getSpotifyUrl().isBlank()) {
            result.addMessage("All artists must have a Spotify url", ResultType.INVALID);
        }

        if(artist.getGenres() == null
                || artist.getGenres().isEmpty()) { // this doesn't add an error, not all artists have genres
            return result; // just need to short circuit if there is no genre array
        }

        for (Genre genre : artist.getGenres()) {
            result = validateGenre(genre, result);
        }

        return result;
    }

    private Result validateGenre(Genre genre, Result result){
        if (genre.getGenreName() == null || genre.getGenreName().isBlank()) {
            result.addMessage("All genres must have a name", ResultType.INVALID);
        }
        return result;
    }

    private Result validateAlbum(Album album, Result result) {
        if(album.getAlbumUri() == null || album.getAlbumUri().isBlank()){
            result.addMessage("All albums must have a Spotify URI", ResultType.INVALID);
        }

        if(album.getAlbumName() == null || album.getAlbumName().isBlank()){
            result.addMessage("All albums must have a name", ResultType.INVALID);
        }

        if(album.getSpotifyUrl() == null || album.getSpotifyUrl().isBlank()){
            result.addMessage("All albums must have a Spotify url", ResultType.INVALID);
        }

        if(album.getReleaseDate() == null){
            result.addMessage("All albums must have a release date", ResultType.INVALID);
        }

        return result;
    }


}
