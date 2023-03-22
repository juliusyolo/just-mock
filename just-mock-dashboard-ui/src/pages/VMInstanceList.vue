<template>
  <a-table :columns="columns" :data="data" :loading="loading" :pagination="false">
    <template #attached="{ record }">
      <a-tag v-if="record.attached" color="green">是</a-tag>
      <a-tag v-else color="red">否</a-tag>
    </template>
    <template #optional="{ record }">
      <a-button v-if="record.attached" @click="doAttachVMInstance(record.pid)" :loading="attachLoading">已连接
      </a-button>
      <a-button v-else @click="doAttachVMInstance(record.pid)" :loading="attachLoading">连接</a-button>
      <a-divider direction="vertical"/>
      <a-button :disabled="!record.attached" @click="mockClick(record.pid)">开始Mock
      </a-button>
    </template>
  </a-table>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue'
import {VmInstanceArray} from "../api/vm/types";
import {attachVMInstance, getAllVMInstances} from "../api/vm";
import {Message} from "@arco-design/web-vue";
import {useRouter} from "vue-router";

export default defineComponent({
  setup() {
    const loading = ref(false)
    const data = ref<VmInstanceArray>([])
    const attachLoading = ref(false)
    const router = useRouter()
    const queryAllVMInstances = () => {
      loading.value = true
      getAllVMInstances().then((d) => {
        data.value = d;
        loading.value = false
      }).catch(error => {
        loading.value = false
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }

    const queryAllVMInstancesWithoutState = () => {
      getAllVMInstances().then((d) => {
        data.value = d;
      }).catch(error => {
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }

    const doAttachVMInstance = (pid: string) => {
      attachLoading.value = true
      attachVMInstance(pid).then(() => {
        attachLoading.value = false
        Message.success({content: '连接成功！', duration: 1000, onClose: () => queryAllVMInstances()})
      }).catch(error => {
        attachLoading.value = false
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }

    onMounted(() => {
      queryAllVMInstances()
      setInterval(()=>{
        queryAllVMInstancesWithoutState()
      },5000)
    })
    const columns = [{
      title: '进程号',
      dataIndex: 'pid',
    }, {
      title: '进程名称',
      dataIndex: 'name',
    }, {
      title: '平台',
      dataIndex: 'platform',
    }, {
      title: '厂商',
      dataIndex: 'vendor',
    }, {
      title: '是否已连接',
      slotName: 'attached'
    }, {
      title: '操作',
      slotName: 'optional',
    }];
    const mockClick = (pid: string) => {
      router.push('/mock/info/'+pid)
    }
    return {
      columns,
      loading,
      data,
      attachLoading,
      queryAllVMInstances,
      doAttachVMInstance,
      mockClick
    }
  }
})
</script>

<style scoped>

</style>
