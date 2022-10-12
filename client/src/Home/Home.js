import { useContext, useState } from "react"
import AuthContext from "../context/AuthContext";
import SpotifyAuthContext from "../context/SpotifyAuthContext";

function Home() {

    const spotifyAuth = useContext(SpotifyAuthContext);

    console.log("Hello from the homepage! Spotify auth token: " + spotifyAuth.spotifyAuthToken);

    const [playlists, setplaylists] = useState([]);

    const auth = useContext(AuthContext);

    const canPlay = auth.user !== null;

    const canDelete = auth.user && auth.user.hasRole("admin")


    return (<>
    <div>
        {canPlay && (<h2>play</h2>)}
    </div>

    <div>
        {canDelete && (<h2>delete</h2>)}
    </div>
    </>
    )
}
export default Home;