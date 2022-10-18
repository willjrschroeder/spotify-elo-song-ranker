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
            <td><img className="img-fluid ${3|rounded-top,rounded-right,rounded-bottom,rounded-left,rounded-circle,|}" src={props.p.images[0].url} alt="img"></img></td>
            <td>
                <p>Playlist Title:</p>
                <p>{props.p.name}</p>
                <button onClick={() => props.addPlaylist(spotifyData)}>Add Playlist to Tracking</button>
            </td>
        </tr>



    </>
    )

}
export default Playlist;