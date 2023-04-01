<template>
  <div class="header-group">
    <a-breadcrumb>
      <a-breadcrumb-item>虚拟机实例列表</a-breadcrumb-item>
    </a-breadcrumb>
  </div>
  <a-table :columns="columns" :data="data" :loading="loading" :pagination="false">
    <template #attached="{ record }">
      <a-tooltip v-if="record.attached"
                 :content="record.environmentVariables?record.environmentVariables:'没有设置全局环境变量'">
        <a-tag color="green">是</a-tag>
      </a-tooltip>
      <a-tag v-else color="red">否</a-tag>
    </template>
    <template #optional="{ record }">
      <a-button v-if="record.attached" @click="doAttachVMInstanceWithNothing">已连接
      </a-button>
      <a-popconfirm v-else content="连接是否需要设置目标应用的环境变量?" cancel-text="否" ok-text="是"
                    @cancel="doAttachVMInstance(record.pid)"
                    @ok="doAttachVMInstanceWithEnvironmentVariable(record.pid)">
        <a-button :loading="attachLoading">连接</a-button>
      </a-popconfirm>
      <a-divider direction="vertical"/>
      <a-button :disabled="!record.attached" @click="mockClick(record.pid,record.name)">开始Mock
      </a-button>
    </template>
  </a-table>
  <a-modal width="50%" v-model:visible="modalVisible" @cancel="handleCancel" :on-before-ok="handleBeforeOk"
           unmountOnClose>
    <template #title>
      添加环境变量
    </template>
    <div>
      <h3>环境变量:</h3>
      <a-input-tag v-model="inputTags" placeholder="请输入标签..." @change="tagInputChange" allow-clear/>
      <h3>备注:</h3>
      <a-textarea disabled
                  default-value="应添加该应用拥有的全局变环境量或静态方法，会添加到环境变量末尾。全局环境变量无效会导致连接异常。"
                  :auto-size="true"/>
    </div>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, onUnmounted, ref} from 'vue'
import {AttachVMInstance, VmInstanceArray} from "../api/vm/types";
import {attachVMInstance, getAllVMInstances} from "../api/vm";
import {Message} from "@arco-design/web-vue";
import {useRouter} from "vue-router";

export default defineComponent({
  setup() {
    const loading = ref(false)
    const data = ref<VmInstanceArray>([])
    const attachLoading = ref(false)
    const attachVMInstanceModel = ref<AttachVMInstance>()
    const inputTags = ref<Array<string>>([])
    const modalVisible = ref<boolean>(false)
    const router = useRouter()
    let intervalId: any = null;
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

    const doAttachVMInstanceWithNothing = () => {
      Message.warning({content: '该虚拟机实例已经连接！', duration: 1000})
    }

    const doAttachVMInstance = (pid: string) => {
      attachVMInstanceModel.value = {
        pid: pid,
        environmentVariables: []
      }
      doAttachVMInstance0(attachVMInstanceModel.value)
    }

    const doAttachVMInstance0 = (attachVMInstanceModel: AttachVMInstance) => {
      attachLoading.value = true
      attachVMInstance(attachVMInstanceModel).then(() => {
        attachLoading.value = false
        Message.success({content: '连接成功！', duration: 1000, onClose: () => queryAllVMInstances()})
      }).catch(error => {
        attachLoading.value = false
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    const doAttachVMInstanceWithEnvironmentVariable = (pid: string) => {
      attachVMInstanceModel.value = {
        pid: pid,
        environmentVariables: []
      }
      modalVisible.value = true
    }
    onMounted(() => {
      queryAllVMInstances()
      intervalId = setInterval(() => {
        queryAllVMInstancesWithoutState()
      }, 5000)
    })
    onUnmounted(() => clearInterval(intervalId))
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
    const mockClick = (pid: string, name: string) => {
      router.push({path: '/mock/info/' + pid, query: {name}})
    }
    const handleBeforeOk = async () => {
      if (!attachVMInstanceModel.value || attachVMInstanceModel.value?.environmentVariables.length === 0) {
        console.log(attachVMInstanceModel.value)
        Message.error({content: '环境变量必填！', duration: 2 * 1000})
        return false
      }
      if (attachVMInstanceModel.value) {
        doAttachVMInstance0(attachVMInstanceModel.value)
      }
      inputTags.value = []
      return true
    };
    const handleCancel = () => {
      modalVisible.value = false
      inputTags.value = []
    }
    const tagInputChange = (tags: Array<string>) => {
      attachVMInstanceModel.value = {...attachVMInstanceModel.value, environmentVariables: tags} as AttachVMInstance
    }
    return {
      columns,
      loading,
      data,
      attachLoading,
      inputTags,
      modalVisible,
      queryAllVMInstances,
      doAttachVMInstanceWithEnvironmentVariable,
      doAttachVMInstance,
      doAttachVMInstanceWithNothing,
      mockClick,
      handleBeforeOk,
      handleCancel,
      tagInputChange
    }
  }
})
</script>

<style lang="less" scoped>
.header-group {
  display: flex;
  justify-content: space-between;
  margin: 0 0 2px 0;
  padding: 2px;
  border: 1px solid #E5E6EB;
  border-radius: 4px;
}
</style>
