<template>
  <div class="container">
    <!-- 통계 카드 -->
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-content">
          <h3>{{ jpaStats.totalUsers }}</h3>
          <p>JPA 총 사용자</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">⚡</div>
        <div class="stat-content">
          <h3>{{ mybatisStats.totalUsers }}</h3>
          <p>MyBatis 총 사용자</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">✅</div>
        <div class="stat-content">
          <h3>{{ jpaStats.activeUsers }}</h3>
          <p>JPA 활성 사용자</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🔥</div>
        <div class="stat-content">
          <h3>{{ mybatisStats.activeUsers }}</h3>
          <p>MyBatis 활성 사용자</p>
        </div>
      </div>
    </div>

    <!-- 탭 네비게이션 -->
    <div class="tabs">
      <div 
        class="tab" 
        :class="{ active: activeTab === 'jpa' }"
        @click="activeTab = 'jpa'"
      >
        🏛️ JPA 데이터
      </div>
      <div 
        class="tab" 
        :class="{ active: activeTab === 'mybatis' }"
        @click="activeTab = 'mybatis'"
      >
        ⚡ MyBatis 데이터
      </div>
      <div 
        class="tab" 
        :class="{ active: activeTab === 'compare' }"
        @click="activeTab = 'compare'"
      >
        🔄 데이터 비교
      </div>
    </div>

    <!-- 검색 및 필터 -->
    <div class="search-bar" v-if="activeTab !== 'compare'">
      <div class="form-group">
        <label class="form-label">사용자명 검색</label>
        <input 
          v-model="searchQuery" 
          type="text" 
          class="form-input" 
          placeholder="사용자명을 입력하세요..."
          @input="handleSearch"
        >
      </div>
      <div class="form-group">
        <label class="form-label">이메일 도메인</label>
        <input 
          v-model="domainQuery" 
          type="text" 
          class="form-input" 
          placeholder="예: gmail.com"
          @input="handleDomainSearch"
        >
      </div>
      <div class="form-group">
        <label class="form-label">상태 필터</label>
        <select v-model="statusFilter" class="form-input" @change="handleStatusFilter">
          <option value="">전체</option>
          <option value="true">활성</option>
          <option value="false">비활성</option>
        </select>
      </div>
      <div class="form-group">
        <label class="form-label">&nbsp;</label>
        <button @click="clearFilters" class="btn btn-secondary">필터 초기화</button>
      </div>
    </div>

    <!-- 사용자 생성 폼 -->
    <div class="card" v-if="activeTab !== 'compare'">
      <div class="card-header">
        <span>새 사용자 생성 ({{ activeTab.toUpperCase() }})</span>
        <button @click="showCreateForm = !showCreateForm" class="btn btn-primary">
          {{ showCreateForm ? '폼 숨기기' : '사용자 추가' }}
        </button>
      </div>
      <div class="card-body" v-if="showCreateForm">
        <form @submit.prevent="createUser" class="user-form">
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">사용자명 *</label>
              <input 
                v-model="newUser.username" 
                type="text" 
                class="form-input" 
                required
                placeholder="사용자명을 입력하세요"
              >
            </div>
            <div class="form-group">
              <label class="form-label">이메일 *</label>
              <input 
                v-model="newUser.email" 
                type="email" 
                class="form-input" 
                required
                placeholder="이메일을 입력하세요"
              >
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">전체 이름</label>
              <input 
                v-model="newUser.fullName" 
                type="text" 
                class="form-input"
                placeholder="전체 이름을 입력하세요"
              >
            </div>
            <div class="form-group">
              <label class="form-label">상태</label>
              <select v-model="newUser.active" class="form-input">
                <option :value="true">활성</option>
                <option :value="false">비활성</option>
              </select>
            </div>
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" :disabled="creating">
              {{ creating ? '생성 중...' : '사용자 생성' }}
            </button>
            <button type="button" @click="resetForm" class="btn btn-secondary">
              초기화
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 로딩 상태 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>데이터를 불러오는 중...</p>
    </div>

    <!-- 에러 메시지 -->
    <div v-if="error" class="error">
      {{ error }}
    </div>

    <!-- 성공 메시지 -->
    <div v-if="successMessage" class="success">
      {{ successMessage }}
    </div>

    <!-- JPA 사용자 목록 -->
    <div v-if="activeTab === 'jpa' && !loading" class="card">
      <div class="card-header">
        <span>JPA 사용자 목록 ({{ jpaUsers.length }}명)</span>
        <button @click="refreshJpaData" class="btn btn-primary">새로고침</button>
      </div>
      <div class="card-body">
        <UserTable 
          :users="jpaUsers" 
          :data-source="'JPA'"
          @refresh="refreshJpaData"
        />
        <Pagination 
          v-if="jpaPagination.totalPages > 1"
          :current-page="jpaPagination.currentPage"
          :total-pages="jpaPagination.totalPages"
          :total-elements="jpaPagination.totalElements"
          @page-change="handleJpaPageChange"
        />
      </div>
    </div>

    <!-- MyBatis 사용자 목록 -->
    <div v-if="activeTab === 'mybatis' && !loading" class="card">
      <div class="card-header">
        <span>MyBatis 사용자 목록 ({{ mybatisUsers.length }}명)</span>
        <button @click="refreshMybatisData" class="btn btn-primary">새로고침</button>
      </div>
      <div class="card-body">
        <UserTable 
          :users="mybatisUsers" 
          :data-source="'MyBatis'"
          @refresh="refreshMybatisData"
        />
        <Pagination 
          v-if="mybatisPagination.totalPages > 1"
          :current-page="mybatisPagination.currentPage"
          :total-pages="mybatisPagination.totalPages"
          :total-elements="mybatisPagination.totalElements"
          @page-change="handleMybatisPageChange"
        />
      </div>
    </div>

    <!-- 데이터 비교 -->
    <div v-if="activeTab === 'compare' && !loading" class="card">
      <div class="card-header">
        <span>JPA vs MyBatis 데이터 비교</span>
        <button @click="refreshAllData" class="btn btn-primary">전체 새로고침</button>
      </div>
      <div class="card-body">
        <DataComparison 
          :jpa-users="jpaUsers"
          :mybatis-users="mybatisUsers"
          :jpa-stats="jpaStats"
          :mybatis-stats="mybatisStats"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { jpaUserService, mybatisUserService } from '../services/api'
import UserTable from './UserTable.vue'
import Pagination from './Pagination.vue'
import DataComparison from './DataComparison.vue'

export default {
  name: 'UserManagement',
  components: {
    UserTable,
    Pagination,
    DataComparison
  },
  setup() {
    // 반응형 상태
    const activeTab = ref('jpa')
    const loading = ref(false)
    const creating = ref(false)
    const error = ref('')
    const successMessage = ref('')
    const showCreateForm = ref(false)
    
    // 검색 및 필터
    const searchQuery = ref('')
    const domainQuery = ref('')
    const statusFilter = ref('')
    
    // 사용자 데이터
    const jpaUsers = ref([])
    const mybatisUsers = ref([])
    
    // 페이징 정보
    const jpaPagination = reactive({
      currentPage: 0,
      totalPages: 0,
      totalElements: 0,
      size: 10
    })
    
    const mybatisPagination = reactive({
      currentPage: 0,
      totalPages: 0,
      totalElements: 0,
      size: 10
    })
    
    // 통계 정보
    const jpaStats = reactive({
      totalUsers: 0,
      activeUsers: 0
    })
    
    const mybatisStats = reactive({
      totalUsers: 0,
      activeUsers: 0
    })
    
    // 새 사용자 폼
    const newUser = reactive({
      username: '',
      email: '',
      fullName: '',
      active: true
    })
    
    // 메서드
    const showError = (message) => {
      error.value = message
      setTimeout(() => error.value = '', 5000)
    }
    
    const showSuccess = (message) => {
      successMessage.value = message
      setTimeout(() => successMessage.value = '', 3000)
    }
    
    const loadJpaUsers = async (page = 0) => {
      try {
        loading.value = true
        const response = await jpaUserService.getUsersPaged(page, jpaPagination.size)
        jpaUsers.value = response.data.data
        jpaPagination.currentPage = response.data.currentPage
        jpaPagination.totalPages = response.data.totalPages
        jpaPagination.totalElements = response.data.totalElements
      } catch (err) {
        showError('JPA 사용자 데이터 로드 실패: ' + err.message)
      } finally {
        loading.value = false
      }
    }
    
    const loadMybatisUsers = async (page = 0) => {
      try {
        loading.value = true
        const response = await mybatisUserService.getUsersPaged(page, mybatisPagination.size)
        mybatisUsers.value = response.data.data
        mybatisPagination.currentPage = response.data.currentPage
        mybatisPagination.totalPages = response.data.totalPages
        mybatisPagination.totalElements = response.data.totalElements
      } catch (err) {
        showError('MyBatis 사용자 데이터 로드 실패: ' + err.message)
      } finally {
        loading.value = false
      }
    }
    
    const loadStats = async () => {
      try {
        // JPA 통계
        const jpaActiveResponse = await jpaUserService.getActiveUserCount()
        const jpaAllResponse = await jpaUserService.getAllUsers()
        jpaStats.activeUsers = jpaActiveResponse.data.activeUserCount
        jpaStats.totalUsers = jpaAllResponse.data.count
        
        // MyBatis 통계
        const mybatisActiveResponse = await mybatisUserService.getActiveUserCount()
        const mybatisAllResponse = await mybatisUserService.getAllUsers()
        mybatisStats.activeUsers = mybatisActiveResponse.data.activeUserCount
        mybatisStats.totalUsers = mybatisAllResponse.data.count
      } catch (err) {
        showError('통계 데이터 로드 실패: ' + err.message)
      }
    }
    
    const refreshJpaData = () => {
      loadJpaUsers(jpaPagination.currentPage)
      loadStats()
    }
    
    const refreshMybatisData = () => {
      loadMybatisUsers(mybatisPagination.currentPage)
      loadStats()
    }
    
    const refreshAllData = () => {
      refreshJpaData()
      refreshMybatisData()
    }
    
    const handleJpaPageChange = (page) => {
      loadJpaUsers(page)
    }
    
    const handleMybatisPageChange = (page) => {
      loadMybatisUsers(page)
    }
    
    const handleSearch = async () => {
      if (!searchQuery.value.trim()) {
        if (activeTab.value === 'jpa') {
          loadJpaUsers()
        } else {
          loadMybatisUsers()
        }
        return
      }
      
      try {
        loading.value = true
        if (activeTab.value === 'jpa') {
          const response = await jpaUserService.searchUsers(searchQuery.value)
          jpaUsers.value = response.data.data
        } else {
          const response = await mybatisUserService.searchUsers(searchQuery.value)
          mybatisUsers.value = response.data.data
        }
      } catch (err) {
        showError('검색 실패: ' + err.message)
      } finally {
        loading.value = false
      }
    }
    
    const handleDomainSearch = async () => {
      if (!domainQuery.value.trim()) {
        if (activeTab.value === 'jpa') {
          loadJpaUsers()
        } else {
          loadMybatisUsers()
        }
        return
      }
      
      try {
        loading.value = true
        if (activeTab.value === 'jpa') {
          const response = await jpaUserService.getUsersByDomain(domainQuery.value)
          jpaUsers.value = response.data.data
        } else {
          const response = await mybatisUserService.getUsersByDomain(domainQuery.value)
          mybatisUsers.value = response.data.data
        }
      } catch (err) {
        showError('도메인 검색 실패: ' + err.message)
      } finally {
        loading.value = false
      }
    }
    
    const handleStatusFilter = async () => {
      if (statusFilter.value === '') {
        if (activeTab.value === 'jpa') {
          loadJpaUsers()
        } else {
          loadMybatisUsers()
        }
        return
      }
      
      try {
        loading.value = true
        if (statusFilter.value === 'true') {
          if (activeTab.value === 'jpa') {
            const response = await jpaUserService.getActiveUsers()
            jpaUsers.value = response.data.data
          } else {
            const response = await mybatisUserService.getActiveUsers()
            mybatisUsers.value = response.data.data
          }
        }
      } catch (err) {
        showError('필터링 실패: ' + err.message)
      } finally {
        loading.value = false
      }
    }
    
    const clearFilters = () => {
      searchQuery.value = ''
      domainQuery.value = ''
      statusFilter.value = ''
      if (activeTab.value === 'jpa') {
        loadJpaUsers()
      } else {
        loadMybatisUsers()
      }
    }
    
    const createUser = async () => {
      try {
        creating.value = true
        const service = activeTab.value === 'jpa' ? jpaUserService : mybatisUserService
        await service.createUser(newUser)
        showSuccess(`${activeTab.value.toUpperCase()}로 사용자가 성공적으로 생성되었습니다!`)
        resetForm()
        showCreateForm.value = false
        refreshAllData()
      } catch (err) {
        showError('사용자 생성 실패: ' + (err.response?.data?.message || err.message))
      } finally {
        creating.value = false
      }
    }
    
    const resetForm = () => {
      newUser.username = ''
      newUser.email = ''
      newUser.fullName = ''
      newUser.active = true
    }
    
    // 컴포넌트 마운트 시 데이터 로드
    onMounted(() => {
      refreshAllData()
    })
    
    return {
      // 상태
      activeTab,
      loading,
      creating,
      error,
      successMessage,
      showCreateForm,
      searchQuery,
      domainQuery,
      statusFilter,
      jpaUsers,
      mybatisUsers,
      jpaPagination,
      mybatisPagination,
      jpaStats,
      mybatisStats,
      newUser,
      
      // 메서드
      refreshJpaData,
      refreshMybatisData,
      refreshAllData,
      handleJpaPageChange,
      handleMybatisPageChange,
      handleSearch,
      handleDomainSearch,
      handleStatusFilter,
      clearFilters,
      createUser,
      resetForm
    }
  }
}
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 16px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 2.5rem;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
}

.stat-content h3 {
  font-size: 2rem;
  font-weight: 700;
  margin: 0;
  color: #1f2937;
}

.stat-content p {
  margin: 4px 0 0 0;
  color: #6b7280;
  font-size: 0.9rem;
}

.user-form {
  max-width: 800px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 16px;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 20px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f4f6;
  border-top: 4px solid #4f46e5;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .search-bar {
    flex-direction: column;
  }
  
  .card-header {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }
}
</style>