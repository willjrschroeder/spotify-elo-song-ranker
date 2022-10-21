import "./Playlist.css";

function Playlist(props) {

    return (<>
        <tr>
            <td><img className="rounded playlist" src={props.p.images[0].url} alt="Spotify playlist"></img></td>
            <td>
                <p className="name">{props.p.name}</p></td>
            <td>
                <button onClick={() => props.addPlaylist(props.p.id)}>Add Playlist to Tracking</button>
            </td>
        </tr>



    </>
    )

}
export default Playlist;