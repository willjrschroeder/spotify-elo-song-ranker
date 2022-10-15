package songranker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songranker.domain.SpotifyDataService;
import songranker.models.Result;
import songranker.models.SpotifyData;



@RestController
@RequestMapping("/api/spotify_data")
@CrossOrigin
public class SpotifyDataController {

    @Autowired
    SpotifyDataService service;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SpotifyData spotifyData) {

        Result<?> result = service.addSpotifyData(spotifyData);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

}
