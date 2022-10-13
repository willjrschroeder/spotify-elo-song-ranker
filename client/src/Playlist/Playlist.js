
function Playlist(props) {

    return(<>
        <div className="card">
            <img src={props.playlistData.images[0].url} alt="Playlist"></img>
            <h5>{props.playlistData.name}</h5>
            <button onClick={props.addPlaylistToDatabase}>Add all tracks from this playlist to rank</button>
        </div>
    </>
    )

}
export default Playlist;