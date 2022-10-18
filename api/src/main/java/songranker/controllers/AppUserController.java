package songranker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import songranker.domain.AppUserService;
import songranker.models.AppUser;
import songranker.models.Result;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class AppUserController {
    @Autowired
    AppUserService service;

    @GetMapping("/active")
    public ResponseEntity<Object> getAllActiveAppUsers() {
        Result<List<AppUser>> result = service.getAllActiveAppUsers();

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @GetMapping("/all")


    @DeleteMapping("/delete/{appUserId}")
    public ResponseEntity<Object> deleteAppUserById(@PathVariable int appUserId){
        Result result = service.deleteAppUserById(appUserId);

        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}

