import "./Playlist.css";
function Playlist(props) {

    return(<>
    <div className="card">
        <h3>{props.playlistData.name}</h3>
        <img src={props.playlistData.images[0]} alt="playlist image"></img>
            <button onClick={props.addPlaylistToDatabase}>Add all tracks from this playlist to rank</button>
    </div>
    </>
    )

}
export default Playlist;