package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
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
                +"where t.track_uri = ?\n" +
                "limit 1;";

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

        final String sql = "select t.track_uri from track as t\n" +
                "inner join playlist_track as pt\n" +
                "on pt.track_uri = t.track_uri\n" +
                "where pt.playlist_uri = ?\n" +
                "order by elo_score desc;";

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
        final String sql = "select * from artist as a\n"+
        "inner join track_artist\n"+
        "as ta on ta.artist_uri = a.artist_uri\n"+
        "inner join track as t on t.track_uri = ta.track_uri\n"+
        "where t.app_user_id = ? and a.artist_uri = ?\n"+
                "limit 1;";

        List<String> artistUris = getTop10ArtistUrisByUserId(appUserId);
        List<Genre> artistGenre;
        List<Integer> artistElos;
        List<Artist> artists = new ArrayList<>();

        List<String> written = new ArrayList<>();

        for(String eachUri : artistUris){
            if(!written.contains(eachUri)){
                artistGenre = getGenresByArtistUri(eachUri);
                artistElos = getEloByArtistUri(eachUri);
                artists.addAll(template.query(sql, new ArtistMapper(artistElos,artistGenre),appUserId, eachUri));
                written.add(eachUri);
            }

        }

        return artists;
    }

    private List<String> getTop10ArtistUrisByUserId(int appUserId) {
        final String sql = "select avg(t.elo_score) as elo, a.artist_uri\n"+
                "from artist as a\n"+
                "inner join track_artist\n"+
                "as ta on ta.artist_uri = a.artist_uri\n"+
                "inner join track as t on t.track_uri = ta.track_uri\n"+
                "where t.app_user_id = ?\n"+
                "group by a.artist_uri\n"+
                "order by elo desc\n"+
                "limit 10;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("artist_uri");}, appUserId);

    }

    private List<Integer> getEloByArtistUri(String eachUri) {
        final String sql = "select avg(t.elo_score) as elo\n" +
                "from artist a\n" +
                "inner join track_artist\n" +
                "as ta on ta.artist_uri = a.artist_uri\n" +
                "inner join track as t on t.track_uri = ta.track_uri\n" +
                "where a.artist_uri = ?;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getInt("elo");}, eachUri);
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
                +"inner join artist as a\n"
                +"on a.artist_uri = ga.artist_uri\n"
                +"where a.artist_uri = ?;";


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
                "group by g.genre_id\n" +
                "order by avg(t.elo_score) desc\n" +
                "limit 10;";
        return template.query(sql, new GenreMapper(), appUserId);
    }
    @Override
    public List<Track> getTop10Tracks(int appUserId){
        final String sql = "select * from track where app_user_id = ? and track_uri = ?;";

        List<String> trackUris = getTop10TrackUrisByUserId(appUserId);
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

    private List<String> getTop10TrackUrisByUserId(int appUserId) {
        final String sql = "select track_uri from track\n" +
                "where app_user_id = ?\n" +
                "order by elo_score desc\n" +
                "limit 10;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("track_uri");}, appUserId);
    }



}

