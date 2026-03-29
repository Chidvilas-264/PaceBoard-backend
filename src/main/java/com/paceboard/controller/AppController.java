package com.paceboard.controller;

import com.paceboard.entity.User;
import com.paceboard.entity.FitnessGroup;
import com.paceboard.entity.ChecklistItem;
import com.paceboard.repository.UserRepository;
import com.paceboard.repository.FitnessGroupRepository;
import com.paceboard.repository.ChecklistItemRepository;
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

    @Autowired
    private ChecklistItemRepository checklistRepository;

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

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody User updateData) {
        Optional<User> userOpt = userRepository.findByUsername(updateData.getUsername());
        if(userOpt.isPresent()){
            User user = userOpt.get();
            user.setPassword(updateData.getPassword());
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Username not found");
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
            if(updateData.getHeight() != null) user.setHeight(updateData.getHeight());
            if(updateData.getWeight() != null) user.setWeight(updateData.getWeight());
            if(updateData.getAge() != null) user.setAge(updateData.getAge());
            if(updateData.getGender() != null) user.setGender(updateData.getGender());
            if(updateData.getLocality() != null) user.setLocality(updateData.getLocality());
            if(updateData.getPreferredActivity() != null) user.setPreferredActivity(updateData.getPreferredActivity());
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

    @PostMapping("/groups")
    public ResponseEntity<?> createGroup(@RequestBody FitnessGroup group) {
        if (group.getActiveSince() == null) {
            group.setActiveSince(java.time.LocalDate.now().toString());
        }
        if (group.getTotalMembers() == null) {
            group.setTotalMembers(1);
        }
        groupRepository.save(group);
        if (group.getCreatorId() != null) {
            Optional<User> userOpt = userRepository.findById(group.getCreatorId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.getJoinedGroups().add(group);
                userRepository.save(user);
            }
        }
        return ResponseEntity.ok(group);
    }

    @PostMapping("/groups/{groupId}/join/{userId}")
    public ResponseEntity<?> joinGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        Optional<FitnessGroup> groupOpt = groupRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (groupOpt.isPresent() && userOpt.isPresent()) {
            User user = userOpt.get();
            FitnessGroup group = groupOpt.get();
            if(!user.getJoinedGroups().contains(group)) {
                user.getJoinedGroups().add(group);
                userRepository.save(user);
                group.setTotalMembers(group.getTotalMembers() + 1);
                groupRepository.save(group);
            }
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.badRequest().body("Group or User not found");
    }

    @PostMapping("/groups/{groupId}/leave/{userId}")
    public ResponseEntity<?> leaveGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        Optional<FitnessGroup> groupOpt = groupRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (groupOpt.isPresent() && userOpt.isPresent()) {
            User user = userOpt.get();
            FitnessGroup group = groupOpt.get();
            if(user.getJoinedGroups().contains(group)) {
                user.getJoinedGroups().remove(group);
                userRepository.save(user);
                group.setTotalMembers(Math.max(0, group.getTotalMembers() - 1));
                groupRepository.save(group);
            }
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.badRequest().body("Group or User not found");
    }

    @GetMapping("/users/{userId}/groups")
    public ResponseEntity<?> getUserGroups(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get().getJoinedGroups());
        }
        return ResponseEntity.notFound().build();
    }

    // Checklist Endpoints
    @GetMapping("/users/{userId}/checklist")
    public ResponseEntity<List<ChecklistItem>> getChecklist(@PathVariable Long userId) {
        return ResponseEntity.ok(checklistRepository.findByUserId(userId));
    }

    @PostMapping("/users/{userId}/checklist")
    public ResponseEntity<?> addChecklistItem(@PathVariable Long userId, @RequestBody ChecklistItem item) {
        item.setUserId(userId);
        return ResponseEntity.ok(checklistRepository.save(item));
    }

    @PutMapping("/checklist/{id}")
    public ResponseEntity<?> updateChecklistItem(@PathVariable Long id, @RequestBody ChecklistItem updatedItem) {
        Optional<ChecklistItem> itemOpt = checklistRepository.findById(id);
        if (itemOpt.isPresent()) {
            ChecklistItem item = itemOpt.get();
            if (updatedItem.getTaskName() != null) item.setTaskName(updatedItem.getTaskName());
            if (updatedItem.getTime() != null) item.setTime(updatedItem.getTime());
            if (updatedItem.getCompleted() != null) item.setCompleted(updatedItem.getCompleted());
            return ResponseEntity.ok(checklistRepository.save(item));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/checklist/{id}")
    public ResponseEntity<?> deleteChecklistItem(@PathVariable Long id) {
        checklistRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
