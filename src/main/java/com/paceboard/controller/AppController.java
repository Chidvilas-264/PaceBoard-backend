package com.paceboard.controller;

import com.paceboard.entity.User;
import com.paceboard.entity.FitnessGroup;
import com.paceboard.repository.UserRepository;
import com.paceboard.repository.FitnessGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow all origins for easier Cloudflare Tunnel testing
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FitnessGroupRepository groupRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Username already exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> userOpt = userRepository.findByUsername(loginUser.getUsername());
        if(userOpt.isPresent() && userOpt.get().getPassword().equals(loginUser.getPassword())){
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.badRequest().body("Invalid username or password");
    }

    @PostMapping("/questionnaire/{id}")
    public ResponseEntity<?> submitQuestionnaire(@PathVariable Long id, @RequestBody User qData) {
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setAge(qData.getAge());
            user.setGender(qData.getGender());
            user.setHeight(qData.getHeight());
            user.setWeight(qData.getWeight());
            user.setLocality(qData.getLocality());
            user.setPreferredActivity(qData.getPreferredActivity());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updateData) {
        Optional<User> userOpt = userRepository.findById(id);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            if(updateData.getPhone() != null) user.setPhone(updateData.getPhone());
            if(updateData.getEmail() != null) user.setEmail(updateData.getEmail());
            if(updateData.getPassword() != null && !updateData.getPassword().isEmpty()) user.setPassword(updateData.getPassword());
            if(updateData.getDailyStepGoal() != null) user.setDailyStepGoal(updateData.getDailyStepGoal());
            if(updateData.getTheme() != null) user.setTheme(updateData.getTheme());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/groups")
    public ResponseEntity<List<FitnessGroup>> getGroups(@RequestParam(required=false) String locality, @RequestParam(required=false) String activity) {
        if (locality != null) return ResponseEntity.ok(groupRepository.findByLocality(locality));
        if (activity != null) return ResponseEntity.ok(groupRepository.findByPreferredActivity(activity));
        return ResponseEntity.ok(groupRepository.findAll());
    }
}
