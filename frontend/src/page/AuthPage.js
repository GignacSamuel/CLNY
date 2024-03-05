import React, { useState, useEffect } from 'react';
import { createClient } from 'pexels';
import { Tabs, TabsContent, TabsList, TabsTrigger } from "../components/ui/tabs"

function AuthPage() {
    const [backgroundUrl, setBackgroundUrl] = useState('');
    const [backgroundOpacity, setBackgroundOpacity] = useState(0);

    useEffect(() => {
        const client = createClient(process.env.REACT_APP_PEXELS_KEY);
        const query = 'Nature';

        client.photos.search({ query, per_page: 1 })
            .then(data => {
                if (data.photos.length > 0) {
                    setBackgroundUrl(data.photos[0].src.original);
                    setBackgroundOpacity(1);
                }
            })
            .catch(error => {
                console.log(error)
            });
    }, []);

    return (
        <div
            className="h-screen flex justify-center items-center"
            style={{
                backgroundImage: `url(${backgroundUrl})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                transition: 'opacity 3s ease-out',
                opacity: backgroundOpacity,
            }}
        >
            <div className="h-2/3 w-2/3 flex justify-around items-center bg-white rounded-lg shadow">
                <div className="flex-1 flex justify-center items-center">
                    <img src="/logo.png" alt="CLNY Logo"/>
                </div>
                <div className="flex-1 flex justify-center items-center">
                    <Tabs defaultValue="login" className="w-[400px]">
                        <TabsList>
                            <TabsTrigger value="login">Login</TabsTrigger>
                            <TabsTrigger value="register">Register</TabsTrigger>
                        </TabsList>
                        <TabsContent value="login">Login Form</TabsContent>
                        <TabsContent value="register">Register Form</TabsContent>
                    </Tabs>
                </div>
            </div>
        </div>
    );
}

export default AuthPage;
