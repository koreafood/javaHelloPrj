package com.example.helloworld.service;

import com.example.helloworld.entity.User;
import com.example.helloworld.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserMybatisService {
    
    private final UserMapper userMapper;
    
    @Autowired
    public UserMybatisService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    /**
     * 모든 사용자 조회
     * @return 사용자 목록
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
    
    /**
     * ID로 사용자 조회
     * @param id 사용자 ID
     * @return 사용자 정보 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        User user = userMapper.findById(id);
        return Optional.ofNullable(user);
    }
    
    /**
     * 사용자명으로 검색
     * @param username 검색할 사용자명
     * @return 검색된 사용자 목록
     */
    @Transactional(readOnly = true)
    public List<User> searchUsersByUsername(String username) {
        return userMapper.findByUsernameContaining(username);
    }
    
    /**
     * 활성 사용자만 조회
     * @return 활성 사용자 목록
     */
    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userMapper.findByIsActiveTrue();
    }
    
    /**
     * 이메일 도메인으로 사용자 검색
     * @param domain 이메일 도메인
     * @return 해당 도메인의 사용자 목록
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByEmailDomain(String domain) {
        return userMapper.findByEmailDomain(domain);
    }
    
    /**
     * 최근 생성된 사용자 조회
     * @return 최근 사용자 목록
     */
    @Transactional(readOnly = true)
    public List<User> getRecentUsers() {
        return userMapper.findRecentUsers();
    }
    
    /**
     * 활성 사용자 수 조회
     * @return 활성 사용자 수
     */
    @Transactional(readOnly = true)
    public Long getActiveUserCount() {
        return userMapper.countActiveUsers();
    }
    
    /**
     * 사용자명 존재 여부 확인
     * @param username 확인할 사용자명
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userMapper.existsByUsername(username);
    }
    
    /**
     * 이메일 존재 여부 확인
     * @param email 확인할 이메일
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userMapper.existsByEmail(email);
    }
    
    /**
     * 새 사용자 생성
     * @param user 생성할 사용자 정보
     * @return 생성된 사용자 정보
     */
    public User createUser(User user) {
        // 생성 시간 설정
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        // 기본값 설정
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        
        // 사용자 생성
        int result = userMapper.insert(user);
        if (result > 0) {
            return user; // MyBatis에서 생성된 ID가 자동으로 설정됨
        } else {
            throw new RuntimeException("사용자 생성에 실패했습니다.");
        }
    }
    
    /**
     * 사용자 정보 수정
     * @param user 수정할 사용자 정보
     * @return 수정된 사용자 정보
     */
    public User updateUser(User user) {
        // 수정 시간 설정
        user.setUpdatedAt(LocalDateTime.now());
        
        int result = userMapper.update(user);
        if (result > 0) {
            return getUserById(user.getId()).orElse(null);
        } else {
            throw new RuntimeException("사용자 정보 수정에 실패했습니다.");
        }
    }
    
    /**
     * 사용자 삭제
     * @param id 삭제할 사용자 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteUser(Long id) {
        int result = userMapper.deleteById(id);
        return result > 0;
    }
    
    /**
     * 페이징된 사용자 목록 조회
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @return 페이징된 사용자 목록과 총 개수 정보
     */
    @Transactional(readOnly = true)
    public UserPageResult getAllUsers(int page, int size, String sortBy) {
        int offset = page * size;
        List<User> users = userMapper.findAllWithPagination(offset, size, sortBy);
        Long totalElements = userMapper.countAll();
        
        return new UserPageResult(users, page, size, totalElements);
    }
    
    /**
     * 다중 조건으로 사용자 검색
     * @param username 사용자명 (부분 일치)
     * @param email 이메일 (부분 일치)
     * @param fullName 전체 이름 (부분 일치)
     * @param isActive 활성 상태
     * @return 검색된 사용자 목록
     */
    @Transactional(readOnly = true)
    public List<User> searchUsers(String username, String email, String fullName, Boolean isActive) {
        return userMapper.searchUsers(username, email, fullName, isActive);
    }
    
    /**
     * 페이징 결과를 담는 내부 클래스
     */
    public static class UserPageResult {
        private final List<User> content;
        private final int currentPage;
        private final int size;
        private final long totalElements;
        private final int totalPages;
        
        public UserPageResult(List<User> content, int currentPage, int size, long totalElements) {
            this.content = content;
            this.currentPage = currentPage;
            this.size = size;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / size);
        }
        
        public List<User> getContent() { return content; }
        public int getCurrentPage() { return currentPage; }
        public int getSize() { return size; }
        public long getTotalElements() { return totalElements; }
        public int getTotalPages() { return totalPages; }
        public boolean hasNext() { return currentPage < totalPages - 1; }
        public boolean hasPrevious() { return currentPage > 0; }
    }
}