package com.example.helloworld.repository;

import com.example.helloworld.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 사용자명으로 사용자 찾기
    Optional<User> findByUsername(String username);
    
    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);
    
    // 활성 사용자만 조회
    List<User> findByIsActiveTrue();
    
    // 사용자명에 특정 문자열이 포함된 사용자 찾기
    List<User> findByUsernameContainingIgnoreCase(String username);
    
    // 이메일에 특정 도메인이 포함된 사용자 찾기
    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain%")
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    // 활성 사용자 수 조회
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    Long countActiveUsers();
    
    // 최근 생성된 사용자 조회 (상위 N개)
    @Query("SELECT u FROM User u ORDER BY u.createdAt DESC")
    List<User> findRecentUsers();
}