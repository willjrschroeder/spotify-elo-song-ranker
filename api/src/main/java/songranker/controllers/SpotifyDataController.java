package songranker.controllers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songranker.models.Playlist;
import songranker.models.Result;
import songranker.models.SpotifyData;
import songranker.models.Track;
import songranker.services.SpotifyDataService;

import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequestMapping("/api/spotifyData")
@CrossOrigin
public class SpotifyDataController {

    private final SpotifyDataService service;

    public SpotifyDataController(SpotifyDataService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody SpotifyData spotifyData) {

        Result<Void> result = service.addSpotifyData(spotifyData);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

}
