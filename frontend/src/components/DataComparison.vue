<template>
  <div class="comparison-container">
    <!-- 통계 비교 -->
    <div class="stats-comparison">
      <h3>📊 통계 비교</h3>
      <div class="comparison-grid">
        <div class="comparison-item">
          <div class="comparison-label">총 사용자 수</div>
          <div class="comparison-values">
            <div class="value jpa">
              <span class="source">JPA</span>
              <span class="number">{{ jpaStats.totalUsers }}</span>
            </div>
            <div class="vs">VS</div>
            <div class="value mybatis">
              <span class="source">MyBatis</span>
              <span class="number">{{ mybatisStats.totalUsers }}</span>
            </div>
          </div>
          <div class="comparison-result">
            <span v-if="jpaStats.totalUsers === mybatisStats.totalUsers" class="match">
              ✅ 일치
            </span>
            <span v-else class="mismatch">
              ❌ 불일치 (차이: {{ Math.abs(jpaStats.totalUsers - mybatisStats.totalUsers) }})
            </span>
          </div>
        </div>
        
        <div class="comparison-item">
          <div class="comparison-label">활성 사용자 수</div>
          <div class="comparison-values">
            <div class="value jpa">
              <span class="source">JPA</span>
              <span class="number">{{ jpaStats.activeUsers }}</span>
            </div>
            <div class="vs">VS</div>
            <div class="value mybatis">
              <span class="source">MyBatis</span>
              <span class="number">{{ mybatisStats.activeUsers }}</span>
            </div>
          </div>
          <div class="comparison-result">
            <span v-if="jpaStats.activeUsers === mybatisStats.activeUsers" class="match">
              ✅ 일치
            </span>
            <span v-else class="mismatch">
              ❌ 불일치 (차이: {{ Math.abs(jpaStats.activeUsers - mybatisStats.activeUsers) }})
            </span>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 데이터 일치성 분석 -->
    <div class="data-analysis">
      <h3>🔍 데이터 일치성 분석</h3>
      <div class="analysis-grid">
        <div class="analysis-card">
          <div class="analysis-header">
            <span class="analysis-title">사용자 ID 비교</span>
            <span class="analysis-badge" :class="userIdMatch ? 'match' : 'mismatch'">
              {{ userIdMatch ? '일치' : '불일치' }}
            </span>
          </div>
          <div class="analysis-content">
            <p>JPA 사용자 ID: {{ jpaUserIds.join(', ') || '없음' }}</p>
            <p>MyBatis 사용자 ID: {{ mybatisUserIds.join(', ') || '없음' }}</p>
            <p v-if="!userIdMatch" class="mismatch-detail">
              차이점: {{ userIdDifferences.join(', ') }}
            </p>
          </div>
        </div>
        
        <div class="analysis-card">
          <div class="analysis-header">
            <span class="analysis-title">사용자명 비교</span>
            <span class="analysis-badge" :class="usernameMatch ? 'match' : 'mismatch'">
              {{ usernameMatch ? '일치' : '불일치' }}
            </span>
          </div>
          <div class="analysis-content">
            <p>JPA 고유 사용자명: {{ jpaUsernames.length }}개</p>
            <p>MyBatis 고유 사용자명: {{ mybatisUsernames.length }}개</p>
            <p v-if="!usernameMatch" class="mismatch-detail">
              차이점: {{ usernameDifferences.join(', ') }}
            </p>
          </div>
        </div>
        
        <div class="analysis-card">
          <div class="analysis-header">
            <span class="analysis-title">이메일 비교</span>
            <span class="analysis-badge" :class="emailMatch ? 'match' : 'mismatch'">
              {{ emailMatch ? '일치' : '불일치' }}
            </span>
          </div>
          <div class="analysis-content">
            <p>JPA 고유 이메일: {{ jpaEmails.length }}개</p>
            <p>MyBatis 고유 이메일: {{ mybatisEmails.length }}개</p>
            <p v-if="!emailMatch" class="mismatch-detail">
              차이점: {{ emailDifferences.join(', ') }}
            </p>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 성능 비교 (시뮬레이션) -->
    <div class="performance-comparison">
      <h3>⚡ 성능 비교 (시뮬레이션)</h3>
      <div class="performance-grid">
        <div class="performance-item">
          <div class="performance-label">조회 속도</div>
          <div class="performance-bars">
            <div class="performance-bar">
              <span class="bar-label">JPA</span>
              <div class="bar jpa">
                <div class="bar-fill" style="width: 75%"></div>
              </div>
              <span class="bar-value">75ms</span>
            </div>
            <div class="performance-bar">
              <span class="bar-label">MyBatis</span>
              <div class="bar mybatis">
                <div class="bar-fill" style="width: 60%"></div>
              </div>
              <span class="bar-value">60ms</span>
            </div>
          </div>
        </div>
        
        <div class="performance-item">
          <div class="performance-label">메모리 사용량</div>
          <div class="performance-bars">
            <div class="performance-bar">
              <span class="bar-label">JPA</span>
              <div class="bar jpa">
                <div class="bar-fill" style="width: 85%"></div>
              </div>
              <span class="bar-value">85MB</span>
            </div>
            <div class="performance-bar">
              <span class="bar-label">MyBatis</span>
              <div class="bar mybatis">
                <div class="bar-fill" style="width: 70%"></div>
              </div>
              <span class="bar-value">70MB</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 종합 평가 -->
    <div class="overall-assessment">
      <h3>📋 종합 평가</h3>
      <div class="assessment-content">
        <div class="assessment-score">
          <div class="score-circle" :class="overallScoreClass">
            <span class="score-number">{{ overallScore }}%</span>
            <span class="score-label">일치도</span>
          </div>
        </div>
        <div class="assessment-details">
          <div class="assessment-item">
            <span class="item-icon">📊</span>
            <span class="item-text">통계 데이터 일치도: {{ statsMatchPercentage }}%</span>
          </div>
          <div class="assessment-item">
            <span class="item-icon">👥</span>
            <span class="item-text">사용자 데이터 일치도: {{ userDataMatchPercentage }}%</span>
          </div>
          <div class="assessment-item">
            <span class="item-icon">⚡</span>
            <span class="item-text">MyBatis가 평균 {{ performanceAdvantage }}% 더 빠름</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'

export default {
  name: 'DataComparison',
  props: {
    jpaUsers: {
      type: Array,
      required: true
    },
    mybatisUsers: {
      type: Array,
      required: true
    },
    jpaStats: {
      type: Object,
      required: true
    },
    mybatisStats: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    // 사용자 ID 비교
    const jpaUserIds = computed(() => 
      props.jpaUsers.map(user => user.id).sort((a, b) => a - b)
    )
    
    const mybatisUserIds = computed(() => 
      props.mybatisUsers.map(user => user.id).sort((a, b) => a - b)
    )
    
    const userIdMatch = computed(() => 
      JSON.stringify(jpaUserIds.value) === JSON.stringify(mybatisUserIds.value)
    )
    
    const userIdDifferences = computed(() => {
      const jpaSet = new Set(jpaUserIds.value)
      const mybatisSet = new Set(mybatisUserIds.value)
      const differences = []
      
      jpaSet.forEach(id => {
        if (!mybatisSet.has(id)) {
          differences.push(`JPA에만 있음: ${id}`)
        }
      })
      
      mybatisSet.forEach(id => {
        if (!jpaSet.has(id)) {
          differences.push(`MyBatis에만 있음: ${id}`)
        }
      })
      
      return differences
    })
    
    // 사용자명 비교
    const jpaUsernames = computed(() => 
      [...new Set(props.jpaUsers.map(user => user.username))].sort()
    )
    
    const mybatisUsernames = computed(() => 
      [...new Set(props.mybatisUsers.map(user => user.username))].sort()
    )
    
    const usernameMatch = computed(() => 
      JSON.stringify(jpaUsernames.value) === JSON.stringify(mybatisUsernames.value)
    )
    
    const usernameDifferences = computed(() => {
      const jpaSet = new Set(jpaUsernames.value)
      const mybatisSet = new Set(mybatisUsernames.value)
      const differences = []
      
      jpaSet.forEach(username => {
        if (!mybatisSet.has(username)) {
          differences.push(`JPA에만 있음: ${username}`)
        }
      })
      
      mybatisSet.forEach(username => {
        if (!jpaSet.has(username)) {
          differences.push(`MyBatis에만 있음: ${username}`)
        }
      })
      
      return differences
    })
    
    // 이메일 비교
    const jpaEmails = computed(() => 
      [...new Set(props.jpaUsers.map(user => user.email))].sort()
    )
    
    const mybatisEmails = computed(() => 
      [...new Set(props.mybatisUsers.map(user => user.email))].sort()
    )
    
    const emailMatch = computed(() => 
      JSON.stringify(jpaEmails.value) === JSON.stringify(mybatisEmails.value)
    )
    
    const emailDifferences = computed(() => {
      const jpaSet = new Set(jpaEmails.value)
      const mybatisSet = new Set(mybatisEmails.value)
      const differences = []
      
      jpaSet.forEach(email => {
        if (!mybatisSet.has(email)) {
          differences.push(`JPA에만 있음: ${email}`)
        }
      })
      
      mybatisSet.forEach(email => {
        if (!jpaSet.has(email)) {
          differences.push(`MyBatis에만 있음: ${email}`)
        }
      })
      
      return differences
    })
    
    // 통계 일치도
    const statsMatchPercentage = computed(() => {
      let matches = 0
      let total = 2
      
      if (props.jpaStats.totalUsers === props.mybatisStats.totalUsers) matches++
      if (props.jpaStats.activeUsers === props.mybatisStats.activeUsers) matches++
      
      return Math.round((matches / total) * 100)
    })
    
    // 사용자 데이터 일치도
    const userDataMatchPercentage = computed(() => {
      let matches = 0
      let total = 3
      
      if (userIdMatch.value) matches++
      if (usernameMatch.value) matches++
      if (emailMatch.value) matches++
      
      return Math.round((matches / total) * 100)
    })
    
    // 전체 일치도
    const overallScore = computed(() => {
      return Math.round((statsMatchPercentage.value + userDataMatchPercentage.value) / 2)
    })
    
    const overallScoreClass = computed(() => {
      if (overallScore.value >= 90) return 'excellent'
      if (overallScore.value >= 70) return 'good'
      if (overallScore.value >= 50) return 'fair'
      return 'poor'
    })
    
    // 성능 우위
    const performanceAdvantage = computed(() => {
      return Math.round(((75 + 85) - (60 + 70)) / (75 + 85) * 100)
    })
    
    return {
      jpaUserIds,
      mybatisUserIds,
      userIdMatch,
      userIdDifferences,
      jpaUsernames,
      mybatisUsernames,
      usernameMatch,
      usernameDifferences,
      jpaEmails,
      mybatisEmails,
      emailMatch,
      emailDifferences,
      statsMatchPercentage,
      userDataMatchPercentage,
      overallScore,
      overallScoreClass,
      performanceAdvantage
    }
  }
}
</script>

<style scoped>
.comparison-container {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.stats-comparison h3,
.data-analysis h3,
.performance-comparison h3,
.overall-assessment h3 {
  margin: 0 0 20px 0;
  color: #1f2937;
  font-size: 1.25rem;
  font-weight: 600;
}

.comparison-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.comparison-item {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.comparison-label {
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
  text-align: center;
}

.comparison-values {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.value {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.value .source {
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.value .number {
  font-size: 2rem;
  font-weight: 700;
}

.value.jpa .source {
  color: #1e40af;
}

.value.jpa .number {
  color: #1e40af;
}

.value.mybatis .source {
  color: #92400e;
}

.value.mybatis .number {
  color: #92400e;
}

.vs {
  font-weight: 700;
  color: #6b7280;
  font-size: 1.1rem;
}

.comparison-result {
  text-align: center;
  font-weight: 600;
}

.match {
  color: #059669;
}

.mismatch {
  color: #dc2626;
}

.analysis-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.analysis-card {
  background: white;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
  overflow: hidden;
}

.analysis-header {
  background: #f9fafb;
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e5e7eb;
}

.analysis-title {
  font-weight: 600;
  color: #374151;
}

.analysis-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
}

.analysis-badge.match {
  background: #d1fae5;
  color: #065f46;
}

.analysis-badge.mismatch {
  background: #fee2e2;
  color: #991b1b;
}

.analysis-content {
  padding: 20px;
}

.analysis-content p {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #4b5563;
}

.mismatch-detail {
  color: #dc2626 !important;
  font-weight: 500;
}

.performance-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.performance-item {
  background: #f8fafc;
  border-radius: 12px;
  padding: 24px;
  border: 1px solid #e2e8f0;
}

.performance-label {
  font-weight: 600;
  color: #374151;
  margin-bottom: 20px;
  text-align: center;
}

.performance-bars {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.performance-bar {
  display: flex;
  align-items: center;
  gap: 12px;
}

.bar-label {
  width: 80px;
  font-weight: 600;
  font-size: 14px;
}

.bar {
  flex: 1;
  height: 24px;
  background: #e5e7eb;
  border-radius: 12px;
  overflow: hidden;
  position: relative;
}

.bar-fill {
  height: 100%;
  border-radius: 12px;
  transition: width 0.3s ease;
}

.bar.jpa .bar-fill {
  background: linear-gradient(90deg, #3b82f6, #1e40af);
}

.bar.mybatis .bar-fill {
  background: linear-gradient(90deg, #f59e0b, #92400e);
}

.bar-value {
  width: 60px;
  text-align: right;
  font-weight: 600;
  font-size: 14px;
  color: #374151;
}

.overall-assessment {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 16px;
  padding: 32px;
}

.overall-assessment h3 {
  color: white;
  margin-bottom: 24px;
}

.assessment-content {
  display: flex;
  align-items: center;
  gap: 32px;
}

.assessment-score {
  flex-shrink: 0;
}

.score-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 4px solid rgba(255, 255, 255, 0.3);
  position: relative;
}

.score-circle.excellent {
  background: rgba(16, 185, 129, 0.2);
  border-color: #10b981;
}

.score-circle.good {
  background: rgba(59, 130, 246, 0.2);
  border-color: #3b82f6;
}

.score-circle.fair {
  background: rgba(245, 158, 11, 0.2);
  border-color: #f59e0b;
}

.score-circle.poor {
  background: rgba(239, 68, 68, 0.2);
  border-color: #ef4444;
}

.score-number {
  font-size: 2rem;
  font-weight: 700;
}

.score-label {
  font-size: 0.9rem;
  opacity: 0.9;
}

.assessment-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.assessment-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 1.1rem;
}

.item-icon {
  font-size: 1.5rem;
}

.item-text {
  font-weight: 500;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .comparison-values {
    flex-direction: column;
    gap: 16px;
  }
  
  .vs {
    transform: rotate(90deg);
  }
  
  .assessment-content {
    flex-direction: column;
    text-align: center;
  }
  
  .performance-bar {
    flex-direction: column;
    gap: 8px;
    align-items: stretch;
  }
  
  .bar-label,
  .bar-value {
    text-align: center;
  }
}
</style>