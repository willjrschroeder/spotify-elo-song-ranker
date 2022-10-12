import React from 'react';
import useAuth from './useAuth';

const code = new URLSearchParams(window.location.search).get('code');

function CallbackPage() {
    const accessToken = useAuth(code);
    return (
        <div>
            {code}
        </div>
    );
}

export default CallbackPage;