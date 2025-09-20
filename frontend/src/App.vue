<template>
  <div id="app">
    <header class="app-header">
      <div class="container">
        <h1>🚀 사용자 관리 시스템</h1>
        <p>JPA vs MyBatis 데이터 비교 대시보드</p>
      </div>
    </header>
    
    <main class="app-main">
      <router-view />
    </main>
    
    <footer class="app-footer">
      <div class="container">
        <p>&copy; 2025 User Management System - Vue3 + Spring Boot</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style scoped>
.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 2rem 0;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.app-header h1 {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.app-header p {
  font-size: 1.1rem;
  opacity: 0.9;
}

.app-main {
  min-height: calc(100vh - 200px);
  padding: 2rem 0;
}

.app-footer {
  background: #f8f9fa;
  padding: 1rem 0;
  text-align: center;
  border-top: 1px solid #e9ecef;
  color: #6c757d;
}
</style>