<script lang="ts">
import BasicLayout from "../components/BasicLayout.vue";

export default {
  name: 'Main',
  components: {BasicLayout}
}
</script>

<template>
  <BasicLayout>
    <template #header>
      <a-link @click="linkClick('/vmInstances')">
        虚拟机实例
      </a-link>
      <a-link @click="linkClick('/template')">
        模板配置
      </a-link>
    </template>
    <template #content>
      <router-view/>
    </template>
  </BasicLayout>
</template>
<script setup lang="ts">

import {defineComponent} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();
const linkClick = (href: string) => {
  router.push(href)
}
</script>
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
