import "./Playlist.css";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";

function HomePagePlaylist(props) {


    const authServer = useContext(AuthContext);


    return (<>
        <tr>
            <td><img className="img-fluid ${3|rounded-top,rounded-right,rounded-bottom,rounded-left,rounded-circle,|}" src={props.p.playlistImageLink} alt="img"></img></td>
            <td>
                <p>Playlist Title:</p>
                <p>{props.p.name}</p>
                <button>View Song Rankings</button>
            </td>
        </tr>



    </>
    )

}
export default HomePagePlaylist;