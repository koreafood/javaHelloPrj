package com.example.helloworld.controller;

import com.example.helloworld.entity.User;
import com.example.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * 모든 사용자 조회
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "사용자 목록 조회 성공");
            response.put("data", users);
            response.put("count", users.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "사용자 목록 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 페이징된 사용자 목록 조회
     * GET /api/users/paged?page=0&size=10&sortBy=createdAt
     */
    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getUsersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        try {
            Page<User> userPage = userService.getAllUsers(page, size, sortBy);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "페이징된 사용자 목록 조회 성공");
            response.put("data", userPage.getContent());
            response.put("currentPage", userPage.getNumber());
            response.put("totalPages", userPage.getTotalPages());
            response.put("totalElements", userPage.getTotalElements());
            response.put("size", userPage.getSize());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "페이징된 사용자 목록 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * ID로 사용자 조회
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userService.getUserById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (userOpt.isPresent()) {
                response.put("success", true);
                response.put("message", "사용자 조회 성공");
                response.put("data", userOpt.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "사용자를 찾을 수 없습니다. ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "사용자 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 활성 사용자만 조회
     * GET /api/users/active
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveUsers() {
        try {
            List<User> activeUsers = userService.getActiveUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "활성 사용자 목록 조회 성공");
            response.put("data", activeUsers);
            response.put("count", activeUsers.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "활성 사용자 목록 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 사용자명으로 검색
     * GET /api/users/search?username=john
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String username) {
        try {
            List<User> users = userService.searchUsersByUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "사용자 검색 성공");
            response.put("data", users);
            response.put("count", users.size());
            response.put("searchTerm", username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "사용자 검색 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 이메일 도메인으로 사용자 검색
     * GET /api/users/domain?domain=gmail.com
     */
    @GetMapping("/domain")
    public ResponseEntity<Map<String, Object>> getUsersByDomain(@RequestParam String domain) {
        try {
            List<User> users = userService.getUsersByEmailDomain(domain);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "도메인별 사용자 조회 성공");
            response.put("data", users);
            response.put("count", users.size());
            response.put("domain", domain);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "도메인별 사용자 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 최근 생성된 사용자 조회
     * GET /api/users/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentUsers() {
        try {
            List<User> recentUsers = userService.getRecentUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "최근 사용자 목록 조회 성공");
            response.put("data", recentUsers);
            response.put("count", recentUsers.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "최근 사용자 목록 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 활성 사용자 수 조회
     * GET /api/users/count/active
     */
    @GetMapping("/count/active")
    public ResponseEntity<Map<String, Object>> getActiveUserCount() {
        try {
            Long count = userService.getActiveUserCount();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "활성 사용자 수 조회 성공");
            response.put("activeUserCount", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "활성 사용자 수 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 사용자 생성 (테스트용)
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        try {
            // 중복 검사
            if (userService.existsByUsername(user.getUsername())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "이미 존재하는 사용자명입니다: " + user.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            if (userService.existsByEmail(user.getEmail())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "이미 존재하는 이메일입니다: " + user.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            User savedUser = userService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "사용자 생성 성공");
            response.put("data", savedUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "사용자 생성 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}