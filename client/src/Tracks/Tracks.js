// data.body.items gives an array/list of playlists

function Tracks() {

    const spotifyAuth = useContext(SpotifyAuthContext); 

    const [tracks, setTracks] = useState([]);

    function loadAllTracksFromPlaylist() {

        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken);

        spotifyApi.getMe()
        .then(function (data) {
            return spotifyApi.getUserPlaylists(data.body.id);
        })
        .then(function (data) {
            setPlaylists(data.body.items);
        });

        
        }
        useEffect(
            () => {
                loadAllPlaylists();
            },
            []
        )


    return(
        <>
        <table className="table">
            <thead>
                <tr>
                    <th>Track Name</th>
                    <th>Release Date</th>
                    <th>Artist(s)</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
        </>
    )
}
export default Tracks;