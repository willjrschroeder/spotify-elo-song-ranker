package songranker.models;

public class Genre {
    private int genreId;
    private String genreName;


    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public Genre() {
    }

    public Genre(int genre_id, String genre_name) {
        this.genreId = genre_id;
        this.genreName = genre_name;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
