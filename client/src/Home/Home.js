import { useContext, useEffect } from "react"
import AuthContext from "../context/AuthContext";
import {Link} from "react-router-dom";
import "./Home.css"

function Home() {


    const auth = useContext(AuthContext);

    function loadAllTracks() {

    }
    useEffect(
        () => {
            loadAllTracks();
        },[]
    )
    return (
        <>
            <div className="header">Home Page</div>
            <div className="button-area">
                <Link to="/playlistManager" ><button>Manage Playlists</button></Link>
            </div>
            <div className="flex-summary">

            </div>
        </>
    )
}
export default Home;