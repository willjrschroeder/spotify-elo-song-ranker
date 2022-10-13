
function Playlist(props, { playlist, handleDelete, canDelete }) {


    
    return(<>
        <div className="card">
            <div className="card-header">
            </div>
            <div className="card-body">
                <h5 className="card-title">Id: {props.playlistData.id}</h5>
                <p className="card-img">
                    <img src={props.playlistData.images[0].url}></img>
                </p>
            </div>
        </div>
    </>
        
    )

}