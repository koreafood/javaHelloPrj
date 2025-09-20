package com.example.helloworld.mapper;

import com.example.helloworld.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    /**
     * 모든 사용자 조회
     * @return 사용자 목록
     */
    List<User> findAll();
    
    /**
     * ID로 사용자 조회
     * @param id 사용자 ID
     * @return 사용자 정보
     */
    User findById(@Param("id") Long id);
    
    /**
     * 사용자명으로 검색 (부분 일치)
     * @param username 검색할 사용자명
     * @return 검색된 사용자 목록
     */
    List<User> findByUsernameContaining(@Param("username") String username);
    
    /**
     * 활성 사용자만 조회
     * @return 활성 사용자 목록
     */
    List<User> findByIsActiveTrue();
    
    /**
     * 이메일 도메인으로 사용자 검색
     * @param domain 이메일 도메인 (예: gmail.com)
     * @return 해당 도메인의 사용자 목록
     */
    List<User> findByEmailDomain(@Param("domain") String domain);
    
    /**
     * 최근 생성된 사용자 조회 (최대 10명)
     * @return 최근 사용자 목록
     */
    List<User> findRecentUsers();
    
    /**
     * 활성 사용자 수 조회
     * @return 활성 사용자 수
     */
    Long countActiveUsers();
    
    /**
     * 사용자명 존재 여부 확인
     * @param username 확인할 사용자명
     * @return 존재 여부
     */
    boolean existsByUsername(@Param("username") String username);
    
    /**
     * 이메일 존재 여부 확인
     * @param email 확인할 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(@Param("email") String email);
    
    /**
     * 새 사용자 생성
     * @param user 생성할 사용자 정보
     * @return 생성된 행 수
     */
    int insert(User user);
    
    /**
     * 사용자 정보 수정
     * @param user 수정할 사용자 정보
     * @return 수정된 행 수
     */
    int update(User user);
    
    /**
     * 사용자 삭제
     * @param id 삭제할 사용자 ID
     * @return 삭제된 행 수
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 페이징된 사용자 목록 조회
     * @param offset 시작 위치
     * @param size 페이지 크기
     * @param sortBy 정렬 기준
     * @return 페이징된 사용자 목록
     */
    List<User> findAllWithPagination(@Param("offset") int offset, 
                                   @Param("size") int size, 
                                   @Param("sortBy") String sortBy);
    
    /**
     * 전체 사용자 수 조회
     * @return 전체 사용자 수
     */
    Long countAll();
    
    /**
     * 다중 조건으로 사용자 검색
     * @param username 사용자명 (부분 일치)
     * @param email 이메일 (부분 일치)
     * @param fullName 전체 이름 (부분 일치)
     * @param isActive 활성 상태
     * @return 검색된 사용자 목록
     */
    List<User> searchUsers(@Param("username") String username,
                          @Param("email") String email,
                          @Param("fullName") String fullName,
                          @Param("isActive") Boolean isActive);
}