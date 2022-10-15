import "./Playlist.css";
import useGetSpotifyData from "../ManagePlaylists/GetSpotifyData/useGetSpotifyData";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";

function Playlist(props) {


    const authServer = useContext(AuthContext);
    const userId = authServer.user.id;

    const spotifyData = useGetSpotifyData(props.p.id, userId);




    return(<>
                <div >
                    <img className="images" src={props.p.images[0].url}></img>
                    <button onClick={() => props.addPlaylist(spotifyData)}>Add Tracks</button>
                </div>
    </>
    )

}
export default Playlist;