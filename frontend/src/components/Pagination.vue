<template>
  <div class="pagination-container">
    <div class="pagination-info">
      <span>
        총 {{ totalElements }}개 중 
        {{ (currentPage * 10) + 1 }}-{{ Math.min((currentPage + 1) * 10, totalElements) }}개 표시
      </span>
    </div>
    
    <div class="pagination">
      <!-- 첫 페이지 -->
      <button 
        @click="goToPage(0)"
        :disabled="currentPage === 0"
        class="pagination-btn"
        title="첫 페이지"
      >
        ⏮️
      </button>
      
      <!-- 이전 페이지 -->
      <button 
        @click="goToPage(currentPage - 1)"
        :disabled="currentPage === 0"
        class="pagination-btn"
        title="이전 페이지"
      >
        ⬅️
      </button>
      
      <!-- 페이지 번호들 -->
      <button
        v-for="page in visiblePages"
        :key="page"
        @click="goToPage(page)"
        :class="['pagination-btn', { active: page === currentPage }]"
      >
        {{ page + 1 }}
      </button>
      
      <!-- 다음 페이지 -->
      <button 
        @click="goToPage(currentPage + 1)"
        :disabled="currentPage >= totalPages - 1"
        class="pagination-btn"
        title="다음 페이지"
      >
        ➡️
      </button>
      
      <!-- 마지막 페이지 -->
      <button 
        @click="goToPage(totalPages - 1)"
        :disabled="currentPage >= totalPages - 1"
        class="pagination-btn"
        title="마지막 페이지"
      >
        ⏭️
      </button>
    </div>
    
    <div class="page-size-selector">
      <label>
        페이지 크기:
        <select v-model="pageSize" @change="handlePageSizeChange" class="page-size-select">
          <option value="5">5개</option>
          <option value="10">10개</option>
          <option value="20">20개</option>
          <option value="50">50개</option>
        </select>
      </label>
    </div>
  </div>
</template>

<script>
import { computed, ref } from 'vue'

export default {
  name: 'Pagination',
  props: {
    currentPage: {
      type: Number,
      required: true
    },
    totalPages: {
      type: Number,
      required: true
    },
    totalElements: {
      type: Number,
      required: true
    }
  },
  emits: ['page-change', 'page-size-change'],
  setup(props, { emit }) {
    const pageSize = ref(10)
    
    const visiblePages = computed(() => {
      const pages = []
      const maxVisible = 5
      let start = Math.max(0, props.currentPage - Math.floor(maxVisible / 2))
      let end = Math.min(props.totalPages - 1, start + maxVisible - 1)
      
      // 끝에서부터 계산해서 시작점 조정
      if (end - start + 1 < maxVisible) {
        start = Math.max(0, end - maxVisible + 1)
      }
      
      for (let i = start; i <= end; i++) {
        pages.push(i)
      }
      
      return pages
    })
    
    const goToPage = (page) => {
      if (page >= 0 && page < props.totalPages && page !== props.currentPage) {
        emit('page-change', page)
      }
    }
    
    const handlePageSizeChange = () => {
      emit('page-size-change', pageSize.value)
    }
    
    return {
      pageSize,
      visiblePages,
      goToPage,
      handlePageSizeChange
    }
  }
}
</script>

<style scoped>
.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 24px;
  padding: 16px 0;
  border-top: 1px solid #e5e7eb;
  flex-wrap: wrap;
  gap: 16px;
}

.pagination-info {
  color: #6b7280;
  font-size: 14px;
  font-weight: 500;
}

.pagination {
  display: flex;
  gap: 4px;
  align-items: center;
}

.pagination-btn {
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  transition: all 0.2s;
  min-width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-btn:hover:not(:disabled) {
  background: #f3f4f6;
  border-color: #9ca3af;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: #f9fafb;
}

.pagination-btn.active {
  background: #4f46e5;
  color: white;
  border-color: #4f46e5;
  font-weight: 600;
}

.pagination-btn.active:hover {
  background: #4338ca;
  border-color: #4338ca;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #374151;
}

.page-size-select {
  padding: 6px 8px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: white;
  font-size: 14px;
  cursor: pointer;
}

.page-size-select:focus {
  outline: none;
  border-color: #4f46e5;
  box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .pagination-container {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .pagination {
    order: 2;
  }
  
  .pagination-info {
    order: 1;
    margin-bottom: 8px;
  }
  
  .page-size-selector {
    order: 3;
    margin-top: 8px;
  }
  
  .pagination-btn {
    padding: 6px 10px;
    min-width: 36px;
    height: 36px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .pagination {
    gap: 2px;
  }
  
  .pagination-btn {
    padding: 6px 8px;
    min-width: 32px;
    height: 32px;
    font-size: 12px;
  }
  
  .pagination-info,
  .page-size-selector {
    font-size: 13px;
  }
}
</style>