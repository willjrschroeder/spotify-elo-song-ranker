import "./Playlist.css";
function Playlist(props) {

    return(<>
    <div className="container">
        <div className="cards">
            <label className="card">
                <img src={props.playlistData.images[0].url} alt="Playlist"></img>
            </label>
        </div>
        <label className="song-info" id="song-info-1">
            <div className="title">{props.playlistData.name}</div>
            <div className="subtitle"></div>
            <button onClick={props.addPlaylistToDatabase}>Add all tracks from this playlist to rank</button>
            
        </label>
    </div>
    </>
    )

}
export default Playlist;