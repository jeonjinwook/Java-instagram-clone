package com.Java_instagram_clone.domain.profile.controller;

import com.Java_instagram_clone.domain.profile.entity.RequestProfile;
import com.Java_instagram_clone.domain.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("profiles")
    public ResponseEntity<?> profiles(@RequestPart("file") MultipartFile[] files) {
        return profileService.create(files);
    }

    @GetMapping("/findProfileById")
    public ResponseEntity<?> findProfileById(@RequestBody RequestProfile profile) {
        return profileService.findProfileById(profile);
    }
//    @GetMapping("/findByUserId")
//    public ResponseEntity<?> findByUserId(@RequestParam int id){
//        return profileService.findByUserId(id);
//    }
//    @PatchMapping("/updateFile")
//    public ResponseEntity<?> updateFile(@RequestBody String file){
//        return profileService.updateFile(file);
//    }
//    @DeleteMapping("/remove")
//    public ResponseEntity<?> remove(@RequestParam int id){
//        return profileService.remove(id);
//    }
//    @DeleteMapping("/removeByUserId")
//    public ResponseEntity<?> removeByUserId(@RequestParam int id){
//        return profileService.removeByUserId(id);
//    }
}
