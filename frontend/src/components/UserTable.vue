<template>
  <div class="table-container">
    <div v-if="users.length === 0" class="empty-state">
      <div class="empty-icon">📭</div>
      <h3>사용자가 없습니다</h3>
      <p>{{ dataSource }} 데이터베이스에 등록된 사용자가 없습니다.</p>
    </div>
    
    <table v-else class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>사용자명</th>
          <th>이메일</th>
          <th>전체 이름</th>
          <th>상태</th>
          <th>생성일</th>
          <th>수정일</th>
          <th>데이터 소스</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="`${dataSource}-${user.id}`" class="user-row">
          <td class="user-id">{{ user.id }}</td>
          <td class="user-username">
            <div class="username-cell">
              <div class="avatar">{{ getInitials(user.username) }}</div>
              <span>{{ user.username }}</span>
            </div>
          </td>
          <td class="user-email">
            <a :href="`mailto:${user.email}`" class="email-link">
              {{ user.email }}
            </a>
          </td>
          <td class="user-fullname">{{ user.fullName || '-' }}</td>
          <td class="user-status">
            <span 
              class="badge" 
              :class="user.isActive ? 'badge-success' : 'badge-danger'"
            >
              {{ user.isActive ? '활성' : '비활성' }}
            </span>
          </td>
          <td class="user-created">
            <div class="date-cell">
              <div class="date">{{ formatDate(user.createdAt) }}</div>
              <div class="time">{{ formatTime(user.createdAt) }}</div>
            </div>
          </td>
          <td class="user-updated">
            <div class="date-cell">
              <div class="date">{{ formatDate(user.updatedAt) }}</div>
              <div class="time">{{ formatTime(user.updatedAt) }}</div>
            </div>
          </td>
          <td class="user-source">
            <span 
              class="source-badge" 
              :class="dataSource.toLowerCase()"
            >
              {{ dataSource }}
            </span>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'UserTable',
  props: {
    users: {
      type: Array,
      required: true
    },
    dataSource: {
      type: String,
      required: true
    }
  },
  emits: ['refresh'],
  methods: {
    getInitials(username) {
      if (!username) return '?'
      return username.substring(0, 2).toUpperCase()
    },
    
    formatDate(dateString) {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      })
    },
    
    formatTime(dateString) {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.table-container {
  overflow-x: auto;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

.table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.table th {
  background: #f8fafc;
  padding: 16px 12px;
  text-align: left;
  font-weight: 600;
  color: #374151;
  border-bottom: 2px solid #e5e7eb;
  white-space: nowrap;
}

.table td {
  padding: 16px 12px;
  border-bottom: 1px solid #f3f4f6;
  vertical-align: middle;
}

.user-row:hover {
  background: #f9fafb;
}

.user-id {
  font-weight: 600;
  color: #6b7280;
  font-family: 'Monaco', 'Menlo', monospace;
}

.username-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 12px;
}

.email-link {
  color: #4f46e5;
  text-decoration: none;
  transition: color 0.2s;
}

.email-link:hover {
  color: #3730a3;
  text-decoration: underline;
}

.date-cell {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.date {
  font-weight: 500;
  color: #374151;
}

.time {
  font-size: 12px;
  color: #6b7280;
}

.source-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.source-badge.jpa {
  background: #dbeafe;
  color: #1e40af;
}

.source-badge.mybatis {
  background: #fef3c7;
  color: #92400e;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #6b7280;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 16px;
}

.empty-state h3 {
  margin: 0 0 8px 0;
  color: #374151;
}

.empty-state p {
  margin: 0;
  font-size: 14px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .table-container {
    font-size: 14px;
  }
  
  .table th,
  .table td {
    padding: 12px 8px;
  }
  
  .username-cell {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }
  
  .avatar {
    width: 32px;
    height: 32px;
    font-size: 11px;
  }
  
  .date-cell {
    gap: 1px;
  }
  
  .date {
    font-size: 13px;
  }
  
  .time {
    font-size: 11px;
  }
}

/* 매우 작은 화면에서는 일부 컬럼 숨김 */
@media (max-width: 480px) {
  .table th:nth-child(4),
  .table td:nth-child(4),
  .table th:nth-child(7),
  .table td:nth-child(7) {
    display: none;
  }
}
</style>