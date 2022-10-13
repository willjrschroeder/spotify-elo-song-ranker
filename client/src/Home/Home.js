import { useContext } from "react"
import AuthContext from "../context/AuthContext";
import {Link} from "react-router-dom";

function Home() {


    const auth = useContext(AuthContext);

    const canPlay = auth.user !== null;

    const canDelete = auth.user && auth.user.hasRole("admin")


    return (
        <>
            <div>
                <button className='login' onClick={() => auth.logout()}>logout</button>
            </div>
            <div>
                {canPlay && (<h2>play</h2>)}
            </div>

            <div>
                {canDelete && (<h2>delete</h2>)}
            </div>
            <div>
                <Link to="/playlistManager">Playlists</Link>
            </div>
        </>
    )
}
export default Home;