
function Playlist(props, { playlist, handleDelete, canDelete }) {

    return(<>
        <div className="card">
            <div className="card-header">
            </div>
            <div className="card-body">
                <h5 className="card-title">{props.playlistData.name}</h5>
                <p className="card-img">
                    <img src={props.playlistData.images[0].url} alt="Playlist"></img>
                </p>
            </div>
        </div>
    </>
    )

}
export default Playlist;