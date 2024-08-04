package com.musictransfer.music_transfer;
import java.io.IOException;
import java.net.URI;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

@RestController
@RequestMapping("/api")
public class ApiControllers {
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-code");
    private String code = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
    .setClientId(ApiConfig.getSpotifyClientID())
    .setClientSecret(ApiConfig.getSpotifyClientSecret())
    .setRedirectUri(redirectUri)
    .build();

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/login")
    @ResponseBody
    public String spotifyLogin() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
          .scope("playlist-read-private user-read-email user-read-private")
          .show_dialog(true)
          .build();

        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "get-code")
    public String getSpotifyCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
      code = userCode;
      AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
      .build();

      try {
        final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        // set access and refresh token for more spotifyApi object usage
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
      } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
        System.out.println("Error: " + e.getMessage());
      }

      response.sendRedirect("http://localhost:8080/api/top-artists");
      return spotifyApi.getAccessToken();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "top-artists")
    public String getArtists() {
      return "Hey Artist";
    } 
}
