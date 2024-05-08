import React, { useState, useEffect } from 'react';

function WeatherWidget() {
    const [weather, setWeather] = useState(null);

    useEffect(() => {
        const fetchWeather = async (latitude, longitude) => {
            const apiKey = process.env.REACT_APP_WEATHER_API_KEY;
            const url = `http://api.weatherapi.com/v1/current.json?key=${apiKey}&q=${latitude},${longitude}&aqi=no&lang=fr`;

            try {
                const response = await fetch(url);
                const data = await response.json();
                setWeather(data);
            } catch (error) {
                console.error('Failed to fetch weather data:', error);
            }
        };

        const getLocation = () => {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    (position) => {
                        fetchWeather(position.coords.latitude, position.coords.longitude);
                    },
                    () => {
                        fetchWeather(45.508888, -73.561668); // Montreal
                    }
                );
            } else {
                fetchWeather(45.508888, -73.561668); // Montreal
            }
        };

        getLocation();
    }, []);

    if (!weather) {
        return <div>Chargement...</div>;
    }

    return (
        <div className="bg-white shadow-md rounded-lg p-4">
            <div className="flex items-center space-x-4">
                <img src={`https:${weather.current.condition.icon}`} alt="Weather Icon" className="w-12 h-12" />
                <div>
                    <h2 className="text-xl font-semibold">{weather.current.temp_c}°C</h2>
                    <p className="text-gray-600">{weather.current.condition.text}</p>
                </div>
            </div>
            <div className="mt-4">
                <p className="text-sm text-gray-500">Humidité: {weather.current.humidity}%</p>
                <p className="text-sm text-gray-500">Vent: {weather.current.wind_kph} kph du {weather.current.wind_dir}</p>
                <p className="text-sm text-gray-500">Ressenti: {weather.current.feelslike_c}°C</p>
            </div>
            <div className="mt-2 text-right text-xs text-gray-400">
                Dernière mise à jour: {weather.current.last_updated}
            </div>
        </div>
    );
}

export default WeatherWidget;
