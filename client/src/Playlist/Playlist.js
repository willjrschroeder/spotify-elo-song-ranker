
function Playlist(props, { playlist, handleDelete, canDelete }) {

    return(<>
        <div className="card">
            <img src={props.playlistData.images[0].url} alt="Playlist"></img>
            <h5>{props.playlistData.name}</h5>
        </div>
    </>
    )

}
export default Playlist;