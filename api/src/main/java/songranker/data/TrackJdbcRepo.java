package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import songranker.data.mappers.AlbumMapper;
import songranker.data.mappers.ArtistMapper;
import songranker.data.mappers.GenreMapper;
import songranker.data.mappers.TracksMapper;
import songranker.models.Album;
import songranker.models.Artist;
import songranker.models.Genre;
import songranker.models.Track;

import java.util.ArrayList;
import java.util.List;
@Repository
public class TrackJdbcRepo implements TrackRepo{

    @Autowired
    JdbcTemplate template;

    @Override
    public List<Track> getAllTracks(int appUserId){
        final String sql = "select * from track where app_user_id = ? and track_uri = ?;";

        List<String> trackUris = getTrackUrisByUserId(appUserId);
        List<Album> trackAlbums;
        List<Artist> trackArtists;
        List<Track> tracks = new ArrayList<>();

        for(String eachUri : trackUris){
            trackAlbums = getAlbumsByTrackUri(eachUri);
            trackArtists = getArtistsByTrackUri(eachUri);
            tracks.addAll(template.query(sql, new TracksMapper(trackArtists, trackAlbums), appUserId, eachUri));
        }

        return tracks;

    }



    @Override
    public List<Track> getTracksByPlaylistUri(String playlistUri) {
        List<String> trackUris = getTrackUrisByPlaylistUri(playlistUri);
        List<Album> trackAlbums;
        List<Artist> trackArtists;
        List<Track> tracks = new ArrayList<>();
        final String sql = "select * from track as t\n"
                +"inner join playlist as p\n"
                +"on p.app_user_id = t.app_user_id\n"
                +"where t.track_uri = ?;";

        for(String eachUri : trackUris){
            trackAlbums = getAlbumsByTrackUri(eachUri);
            trackArtists = getArtistsByTrackUri(eachUri);
            tracks.addAll(template.query(sql, new TracksMapper(trackArtists, trackAlbums), eachUri));
        }

        return tracks;

    }


    @Override
    public boolean updateTrackEloScore(Track updatedTrack) {

        final String sql = "update track\n"
                +"set elo_score = ?,\n"
                +"num_of_matches_played = num_of_matches_played + 1\n"
                +"where track_uri=? and app_user_id=?;";

        return (template.update(sql, updatedTrack.getEloScore(), updatedTrack.getTrack_uri(), updatedTrack.getAppUserId())) > 0;
    }

    private List<String> getTrackUrisByPlaylistUri(String playlistUri) {

        final String sql = "select track_uri from playlist_track\n"
                +"where playlist_uri = ?;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("track_uri");}, playlistUri);
    }

    private List<String> getTrackUrisByUserId(int appUserId) {
        final String sql = "select track_uri from playlist_track\n"
                +"where app_user_id = ?;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("track_uri");}, appUserId);
    }

    private List<Artist> getArtistsByTrackUri(String trackUri) {
        final String sql = "select * from artist as a\n"
                +"inner join track_artist as ta\n"
                +"on a.artist_uri = ta.artist_uri\n"
                +"inner join track as t\n"
                +"on t.track_uri = ta.track_uri\n"
                +"where t.track_uri = ?;";

        List<Artist> trackArtists = new ArrayList<>();

        List<Genre> artistGenres = getGenresByTrackUri(trackUri);


        return template.query(sql, new ArtistMapper(artistGenres), trackUri);
    }

    private List<Genre> getGenresByTrackUri(String trackUri) {

        final String sql = "select * from genre as g\n"
                +"inner join genre_artist as ga\n"
                +"on g.genre_id = ga.genre_id\n"
                +"inner join track_artist as ta\n"
                +"on ta.artist_uri = ga.artist_uri\n"
                +"where ta.track_uri = ?;";


        return template.query(sql, new GenreMapper(), trackUri);
    }

    private List<Album> getAlbumsByTrackUri(String trackUri) {
        final String sql = "select * from album as ab\n"
                +"inner join track_album as ta\n"
                +"on ab.album_uri = ta.album_uri\n"
                +"where ta.track_uri = ?;";


        return template.query(sql, new AlbumMapper(), trackUri);
    }



    public List<Artist> getTop10Artists(int appUserId) {
        final String sql = "select avg(t.elo_score), a.artist_uri, a.artist_name, a.spotify_url, a.artist_image_link\n" +
                "from artist a\n" +
                "inner join \n" +
                "(select *\n" +
                "from track_artist\n" +
                "where artist_uri = ?\n" +
                ")\n" +
                "as ta on ta.artist_uri = a.artist_uri\n" +
                "inner join (\n" +
                "\tselect *\n" +
                "    from track \n" +
                "    where app_user_id = ?\n" +
                ") as t\n" +
                "on t.track_uri = ta.track_uri\n" +
                "group by a.artist_uri;";

        List<String> artistUris = getArtistUrisByUserId(appUserId);
        List<Genre> artistGenre;
        List<Artist> artists = new ArrayList<>();

        for(String eachUri : artistUris){
            artistGenre = getGenresByArtistUri(eachUri);
            artists.addAll(template.query(sql, new ArtistMapper(artistGenre), eachUri, appUserId));
        }

        return artists;
    }
    private List<String> getArtistUrisByUserId(int appUserId) {
        final String sql = "select ta.artist_uri \n" +
                "from track_artist ta\n" +
                "\tinner join track t on t.track_uri = ta.track_uri\n" +
                "\twhere t.app_user_id = ?;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("artist_uri");}, appUserId);
    }
    private List<Genre> getGenresByArtistUri(String artistUri) {

        final String sql = "select * from genre as g\n"
                +"inner join genre_artist as ga\n"
                +"on g.genre_id = ga.genre_id\n"
                +"inner join track_artist as ta\n"
                +"on ta.artist_uri = ga.artist_uri\n"
                +"where ta.artist_uri = ?;";


        return template.query(sql, new GenreMapper(), artistUri);
    }

    public List<Genre> getTop10Genres(int appUserId) {
        final String sql = "select avg(t.elo_score), g.genre_id, g.genre_name\n" +
                "from artist a\n" +
                "inner join track_artist ta on ta.artist_uri = a.artist_uri\n" +
                "inner join (\n" +
                "\tselect *\n" +
                "    from track \n" +
                "    where app_user_id = ?\n" +
                ") as t\n" +
                "on t.track_uri = ta.track_uri\n" +
                "inner join genre_artist ga on ga.artist_uri = a.artist_uri\n" +
                "inner join genre g on g.genre_id = ga.genre_id\n" +
                "group by g.genre_id;";
        return template.query(sql, new GenreMapper(), appUserId);
    }



}

