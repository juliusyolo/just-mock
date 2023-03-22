<script lang="ts">
export default {
  name: 'Main'
}
</script>

<template>
  <div class="main-layout">
    <a-layout style="height: 100%;">
      <a-layout-header>
        <span>
          <router-link to="/vmInstances">虚拟机实例</router-link>
          <a-divider direction="vertical" />
          <router-link to="/template">模板配置</router-link>
        </span>
      </a-layout-header>
      <a-layout-content>
       <router-view/>
      </a-layout-content>
      <a-layout-footer>非侵入式API Mock @Author sdefaa</a-layout-footer>
    </a-layout>
  </div>
</template>

<style lang="less" scoped>
.main-layout {
  margin: 0 50px 0 50px;
  height: 100vh;
}

.main-layout :deep(.arco-layout-header),
.main-layout :deep(.arco-layout-footer),
.main-layout :deep(.arco-layout-content) {
  display: flex;
  flex-direction: column;
  font-size: 16px;
  font-stretch: condensed;
}

.main-layout :deep(.arco-layout-header),
.main-layout :deep(.arco-layout-footer) {
  height: 64px;
  color: #000000;
  text-align: center;
  justify-content: center;
}

.main-layout :deep(.arco-layout-content) {
  height: calc(100vh - 100px);
}
</style>
