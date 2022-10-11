import { useContext, useState } from "react"
import AuthContext from "../context/AuthContext";

function Home() {

    const [playlists, setplaylists] = useState([]);

    const auth = useContext(AuthContext);




    return (<>
    <div>
        play
    </div>
    <div>
        add
    </div>
    <div>
        delete
    </div>
    </>
    )
}