

function ConfirmationPage(){

    const spotifyAuth = useContext(SpotifyAuthContext); // get access to the spotify token stored in Context

    useEffect(() => { // make this request once on page load and whenever our token updates
        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAuthToken); // allow the api helper to use the current token

        spotifyApi.getMe()
            .then(function (data) {
                console.log('Some information about the authenticated user', data.body);
            }, function (err) {
                console.log('Something went wrong!', err);
            });

    }, [spotifyAuth.spotifyAccessToken])

    return (
        <>

        </>
    );
}

export default ConfirmationPage;