package com.musictransfer.music_transfer;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class ApiConfig {
    private static final Dotenv dotenv = Dotenv.load();

    private static final String SPOTIFY_CLIENT_ID = dotenv.get("SPOTIFY_CLIENT_ID");
    private static final String SPOTIFY_CLIENT_SECRET = dotenv.get("SPOTIFY_CLIENT_SECRET");

    public static String getSpotifyClientID() {
        return SPOTIFY_CLIENT_ID;
    }

    public static String getSpotifyClientSecret() {
        return SPOTIFY_CLIENT_SECRET;
    }

}

