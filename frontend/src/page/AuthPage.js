import React, { useState, useEffect } from 'react';
import { createClient } from 'pexels';

function AuthPage() {
    const [backgroundImageUrl, setBackgroundImageUrl] = useState('');
    const [opacity, setOpacity] = useState(0);

    useEffect(() => {
        const client = createClient(process.env.REACT_APP_PEXELS_KEY);
        const query = 'Nature';

        client.photos.search({ query, per_page: 1 })
            .then(data => {
                if (data.photos.length > 0) {
                    setBackgroundImageUrl(data.photos[0].src.original);
                    setOpacity(1);
                }
            })
            .catch(error => {
                console.log(error)
            });
    }, []);

    return (
        <div
            className="h-screen"
            style={{
                backgroundImage: `url(${backgroundImageUrl})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                transition: 'opacity 3s ease-out',
                opacity: opacity,
            }}
        >
            AuthPage
        </div>
    );
}

export default AuthPage;
