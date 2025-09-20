package com.example.helloworld.controller;

import com.example.helloworld.entity.User;
import com.example.helloworld.service.UserMybatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mybatis/users")
@CrossOrigin(origins = "*")
public class UserMybatisController {
    
    private final UserMybatisService userMybatisService;
    
    @Autowired
    public UserMybatisController(UserMybatisService userMybatisService) {
        this.userMybatisService = userMybatisService;
    }
    
    /**
     * 모든 사용자 조회 (MyBatis)
     * GET /api/mybatis/users
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<User> users = userMybatisService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 사용자 목록 조회 성공");
            response.put("data", users);
            response.put("count", users.size());
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 사용자 목록 조회 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 페이징된 사용자 목록 조회 (MyBatis)
     * GET /api/mybatis/users/paged?page=0&size=10&sortBy=createdAt
     */
    @GetMapping("/paged")
    public ResponseEntity<Map<String, Object>> getUsersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        try {
            UserMybatisService.UserPageResult pageResult = userMybatisService.getAllUsers(page, size, sortBy);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 페이징된 사용자 목록 조회 성공");
            response.put("data", pageResult.getContent());
            response.put("currentPage", pageResult.getCurrentPage());
            response.put("totalPages", pageResult.getTotalPages());
            response.put("totalElements", pageResult.getTotalElements());
            response.put("size", pageResult.getSize());
            response.put("hasNext", pageResult.hasNext());
            response.put("hasPrevious", pageResult.hasPrevious());
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 페이징된 사용자 목록 조회 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * ID로 사용자 조회 (MyBatis)
     * GET /api/mybatis/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userMybatisService.getUserById(id);
            Map<String, Object> response = new HashMap<>();
            
            if (userOpt.isPresent()) {
                response.put("success", true);
                response.put("message", "MyBatis로 사용자 조회 성공");
                response.put("data", userOpt.get());
                response.put("dataSource", "MyBatis");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "사용자를 찾을 수 없습니다. ID: " + id);
                response.put("dataSource", "MyBatis");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 사용자 조회 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 활성 사용자만 조회 (MyBatis)
     * GET /api/mybatis/users/active
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveUsers() {
        try {
            List<User> activeUsers = userMybatisService.getActiveUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 활성 사용자 목록 조회 성공");
            response.put("data", activeUsers);
            response.put("count", activeUsers.size());
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 활성 사용자 목록 조회 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 사용자명으로 검색 (MyBatis)
     * GET /api/mybatis/users/search?username=john
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String username) {
        try {
            List<User> users = userMybatisService.searchUsersByUsername(username);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 사용자 검색 성공");
            response.put("data", users);
            response.put("count", users.size());
            response.put("searchTerm", username);
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 사용자 검색 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 이메일 도메인으로 사용자 검색 (MyBatis)
     * GET /api/mybatis/users/domain?domain=gmail.com
     */
    @GetMapping("/domain")
    public ResponseEntity<Map<String, Object>> getUsersByDomain(@RequestParam String domain) {
        try {
            List<User> users = userMybatisService.getUsersByEmailDomain(domain);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 도메인별 사용자 조회 성공");
            response.put("data", users);
            response.put("count", users.size());
            response.put("domain", domain);
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 도메인별 사용자 조회 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 최근 생성된 사용자 조회 (MyBatis)
     * GET /api/mybatis/users/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentUsers() {
        try {
            List<User> recentUsers = userMybatisService.getRecentUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 최근 사용자 목록 조회 성공");
            response.put("data", recentUsers);
            response.put("count", recentUsers.size());
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 최근 사용자 목록 조회 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 활성 사용자 수 조회 (MyBatis)
     * GET /api/mybatis/users/count/active
     */
    @GetMapping("/count/active")
    public ResponseEntity<Map<String, Object>> getActiveUserCount() {
        try {
            Long count = userMybatisService.getActiveUserCount();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 활성 사용자 수 조회 성공");
            response.put("activeUserCount", count);
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 활성 사용자 수 조회 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 다중 조건 검색 (MyBatis)
     * GET /api/mybatis/users/advanced-search?username=john&email=gmail&fullName=doe&isActive=true
     */
    @GetMapping("/advanced-search")
    public ResponseEntity<Map<String, Object>> advancedSearch(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) Boolean isActive) {
        try {
            List<User> users = userMybatisService.searchUsers(username, email, fullName, isActive);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 고급 검색 성공");
            response.put("data", users);
            response.put("count", users.size());
            response.put("searchCriteria", Map.of(
                "username", username != null ? username : "",
                "email", email != null ? email : "",
                "fullName", fullName != null ? fullName : "",
                "isActive", isActive
            ));
            response.put("dataSource", "MyBatis");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 고급 검색 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 사용자 생성 (MyBatis)
     * POST /api/mybatis/users
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        try {
            // 중복 검사
            if (userMybatisService.existsByUsername(user.getUsername())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "이미 존재하는 사용자명입니다: " + user.getUsername());
                response.put("dataSource", "MyBatis");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            if (userMybatisService.existsByEmail(user.getEmail())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "이미 존재하는 이메일입니다: " + user.getEmail());
                response.put("dataSource", "MyBatis");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            User savedUser = userMybatisService.createUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MyBatis로 사용자 생성 성공");
            response.put("data", savedUser);
            response.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 사용자 생성 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 사용자 정보 수정 (MyBatis)
     * PUT /api/mybatis/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userMybatisService.updateUser(user);
            
            if (updatedUser != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "MyBatis로 사용자 정보 수정 성공");
                response.put("data", updatedUser);
                response.put("dataSource", "MyBatis");
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "사용자를 찾을 수 없습니다. ID: " + id);
                response.put("dataSource", "MyBatis");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 사용자 정보 수정 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * 사용자 삭제 (MyBatis)
     * DELETE /api/mybatis/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            boolean deleted = userMybatisService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "MyBatis로 사용자 삭제 성공");
                response.put("deletedId", id);
                response.put("dataSource", "MyBatis");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "사용자를 찾을 수 없습니다. ID: " + id);
                response.put("dataSource", "MyBatis");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "MyBatis 사용자 삭제 실패: " + e.getMessage());
            errorResponse.put("dataSource", "MyBatis");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}