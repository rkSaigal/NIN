package com.example.jwt.controler;

import com.example.jwt.entities.UserProfile;
import com.example.jwt.service.UserProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
    @RequestMapping("/api/")
@Tag(name = "User Profile Controller", description = "Api for Authentication")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public Iterable<UserProfile> getAllUserProfiles() {
        return userProfileService.getAllUserProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long id) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(id);
        return userProfile.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @PostMapping
//    public UserProfile createUserProfile(@RequestBody UserProfile userProfile) {
//        return userProfileService.createUserProfile(userProfile);
//    }


//    @PostMapping
    @PostMapping("/user/{userId}/posts")
    public ResponseEntity<UserProfile> createUserProfile(
            @RequestBody UserProfile userProfile,
            @PathVariable Long userId
    ) {
//        return userProfileService.createUserProfile(userProfile);
        UserProfile userProfile1 = this.userProfileService.createUserProfile(userProfile, userId);
        return new ResponseEntity<UserProfile>(userProfile1, HttpStatus.CREATED);
    }




    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long id, @RequestBody UserProfile updatedProfile) {
        userProfileService.updateUserProfile(id, updatedProfile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
//        userProfileService.deleteUserProfile(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
