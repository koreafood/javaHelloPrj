package com.example.helloworld.service;

import com.example.helloworld.entity.User;
import com.example.helloworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * 모든 사용자 조회
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * 페이징된 사용자 목록 조회
     */
    public Page<User> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return userRepository.findAll(pageable);
    }
    
    /**
     * ID로 사용자 조회
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * 사용자명으로 사용자 조회
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * 이메일로 사용자 조회
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * 활성 사용자만 조회
     */
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    /**
     * 사용자명 검색
     */
    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
    
    /**
     * 이메일 도메인으로 사용자 검색
     */
    public List<User> getUsersByEmailDomain(String domain) {
        return userRepository.findByEmailDomain(domain);
    }
    
    /**
     * 활성 사용자 수 조회
     */
    public Long getActiveUserCount() {
        return userRepository.countActiveUsers();
    }
    
    /**
     * 최근 생성된 사용자 조회
     */
    public List<User> getRecentUsers() {
        return userRepository.findRecentUsers();
    }
    
    /**
     * 사용자 생성
     */
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * 사용자 정보 업데이트
     */
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * 사용자 삭제 (논리적 삭제)
     */
    @Transactional
    public void deactivateUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(false);
            userRepository.save(user);
        }
    }
    
    /**
     * 사용자 물리적 삭제
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * 사용자 존재 여부 확인
     */
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
    
    /**
     * 사용자명 중복 확인
     */
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    
    /**
     * 이메일 중복 확인
     */
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}