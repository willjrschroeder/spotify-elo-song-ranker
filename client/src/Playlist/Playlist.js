import "./Playlist.css";
import useGetSpotifyData from "../ManagePlaylists/GetSpotifyData/useGetSpotifyData";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";

function Playlist(props) {


    const authServer = useContext(AuthContext);
    const userId = authServer.user.id;

    const spotifyData = useGetSpotifyData(props.p.id, userId);




    return (<>
        <tr>
            <td><img className="img-fluid rounded track" src={props.p.images[0].url} alt="Spotify playlist"></img></td>
            <td>
                <p className="name">{props.p.name}</p></td>
            <td>
                <button onClick={() => props.addPlaylist(spotifyData)}>Add Playlist to Tracking</button>
            </td>
        </tr>



    </>
    )

}
export default Playlist;