import "./Playlist.css";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";
import { Link } from "react-router-dom";

function HomePagePlaylist(props) {

    const location = {
        pathname: "/playlistRankings",
        state: props.p
    }
    const authServer = useContext(AuthContext);


    return (<>
        <tr>
            <td><img className="playlist" src={props.p.playlistImageLink} alt="img"></img></td>
            <td>
                <p>{props.p.playlistName}</p>
                
            </td>
            <td><Link to={location}><button>View Track Rankings</button></Link></td>
        </tr>



    </>
    )

}
export default HomePagePlaylist;