import React from 'react';
import useAuth from './useAuth';

const code = new URLSearchParams(window.location.search).get('code');

function CallbackPage() {
    const spotifyAccessToken = useAuth(code);
    return (
        <div>
            {spotifyAccessToken}
        </div>
    );
}

export default CallbackPage;