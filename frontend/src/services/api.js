import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 요청 인터셉터
api.interceptors.request.use(
  config => {
    console.log(`API 요청: ${config.method?.toUpperCase()} ${config.url}`)
    return config
  },
  error => {
    console.error('API 요청 오류:', error)
    return Promise.reject(error)
  }
)

// 응답 인터셉터
api.interceptors.response.use(
  response => {
    console.log(`API 응답: ${response.status} ${response.config.url}`)
    return response
  },
  error => {
    console.error('API 응답 오류:', error.response?.data || error.message)
    return Promise.reject(error)
  }
)

// JPA API 서비스
export const jpaUserService = {
  // 모든 사용자 조회
  getAllUsers() {
    return api.get('/users')
  },
  
  // 페이징된 사용자 조회
  getUsersPaged(page = 0, size = 10, sortBy = 'createdAt') {
    return api.get('/users/paged', {
      params: { page, size, sortBy }
    })
  },
  
  // ID로 사용자 조회
  getUserById(id) {
    return api.get(`/users/${id}`)
  },
  
  // 활성 사용자 조회
  getActiveUsers() {
    return api.get('/users/active')
  },
  
  // 사용자명으로 검색
  searchUsers(username) {
    return api.get('/users/search', {
      params: { username }
    })
  },
  
  // 도메인으로 검색
  getUsersByDomain(domain) {
    return api.get('/users/domain', {
      params: { domain }
    })
  },
  
  // 최근 사용자 조회
  getRecentUsers() {
    return api.get('/users/recent')
  },
  
  // 활성 사용자 수 조회
  getActiveUserCount() {
    return api.get('/users/count/active')
  },
  
  // 사용자 생성
  createUser(user) {
    return api.post('/users', user)
  }
}

// MyBatis API 서비스
export const mybatisUserService = {
  // 모든 사용자 조회
  getAllUsers() {
    return api.get('/mybatis/users')
  },
  
  // 페이징된 사용자 조회
  getUsersPaged(page = 0, size = 10, sortBy = 'createdAt') {
    return api.get('/mybatis/users/paged', {
      params: { page, size, sortBy }
    })
  },
  
  // ID로 사용자 조회
  getUserById(id) {
    return api.get(`/mybatis/users/${id}`)
  },
  
  // 활성 사용자 조회
  getActiveUsers() {
    return api.get('/mybatis/users/active')
  },
  
  // 사용자명으로 검색
  searchUsers(username) {
    return api.get('/mybatis/users/search', {
      params: { username }
    })
  },
  
  // 도메인으로 검색
  getUsersByDomain(domain) {
    return api.get('/mybatis/users/domain', {
      params: { domain }
    })
  },
  
  // 최근 사용자 조회
  getRecentUsers() {
    return api.get('/mybatis/users/recent')
  },
  
  // 활성 사용자 수 조회
  getActiveUserCount() {
    return api.get('/mybatis/users/count/active')
  },
  
  // 고급 검색
  advancedSearch(criteria) {
    return api.get('/mybatis/users/advanced-search', {
      params: criteria
    })
  },
  
  // 사용자 생성
  createUser(user) {
    return api.post('/mybatis/users', user)
  },
  
  // 사용자 수정
  updateUser(id, user) {
    return api.put(`/mybatis/users/${id}`, user)
  },
  
  // 사용자 삭제
  deleteUser(id) {
    return api.delete(`/mybatis/users/${id}`)
  }
}

export default api