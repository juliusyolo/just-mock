<script lang="ts">
import {defineComponent, onMounted, onUnmounted, reactive, ref} from 'vue'
import BasicLayout from "../components/BasicLayout.vue";
import {useRouter} from "vue-router";
import {
  ArgInfo,
  MockTemplateInfo,
  MockTemplateInfoArray,
  PutMockInfo,
  RegisteredApiInfo,
  RegisteredApiInfoArray
} from "../api/vm/types";
import {getMockTemplateInfoList, getRegisteredApiList, putMock, removeMock} from "../api/vm";
import {Message} from "@arco-design/web-vue";
import TaskDefinitionInputBox from "../components/TaskDefinitionInputBox.vue";
import RandomVariableInputBox from "../components/RandomVariableInputBox.vue";
import {IconInfoCircle} from '@arco-design/web-vue/es/icon';

export default defineComponent({
  components: {BasicLayout, TaskDefinitionInputBox, RandomVariableInputBox, IconInfoCircle},
  setup() {
    const router = useRouter()
    const pid = router.currentRoute.value.params.pid
    const appName = router.currentRoute.value.query.name
    const loading = ref<boolean>(false)
    const data = ref<RegisteredApiInfoArray>([])
    const expandKeys = ref<string[]>([])
    const mockRecord = ref<RegisteredApiInfo>()
    const modalVisible = ref<boolean>(false)
    const selectedMockTemplateInfo = ref<MockTemplateInfo>()
    const mockTemplateInfoData = ref<MockTemplateInfoArray>([])
    const snapshotWarning = ref<boolean>(false)
    let intervalId: any = null;
    const columns = [{
      title: '接口类型',
      slotName: 'apiType'
    }, {
      title: '接口',
      slotName: 'apiUrl'
    }, {
      title: '是否开启Mock',
      slotName: 'mockEnable'
    },
      {
        title: '操作',
        slotName: 'optional',
      }];
    const queryRegisteredApiList = () => {
      loading.value = true
      const currentPid = router.currentRoute.value.params.pid as string
      getRegisteredApiList(currentPid).then((d) => {
        data.value = d.map<RegisteredApiInfo>((value, index) => ({...value, index}));
        loading.value = false
      }).catch(error => {
        loading.value = false
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    const queryRegisteredApiListWithoutState = () => {
      const currentPid = router.currentRoute.value.params.pid as string
      getRegisteredApiList(currentPid).then((d) => {
        data.value = d.map<RegisteredApiInfo>((value, index) => ({...value, index}));
      }).catch(error => {
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    onMounted(() => {
      queryRegisteredApiList()
      intervalId = setInterval(() => {
        queryRegisteredApiListWithoutState()
      }, 5000)
    })
    onUnmounted(() => clearInterval(intervalId))
    const expandable = reactive({
          title: '接口详情',
          width: 80
        }
    );
    const expandedChange = (keys: string[]) => {
      expandKeys.value = keys.filter(value => !expandKeys.value.includes(value))
    }
    const parseToArgInfo = (json: string): ArgInfo => {
      return JSON.parse(json) as ArgInfo
    }
    const parseToArgInfoList = (json: string): Array<ArgInfo> => {
      return JSON.parse(json) as Array<ArgInfo>
    }
    const configMock = (record: RegisteredApiInfo) => {
      mockRecord.value = record
      getMockTemplateInfoList().then(d => {
        mockTemplateInfoData.value = d;
        if (record.mockEnable) {
          selectedMockTemplateInfo.value = d.filter(e => e.id === record.mockTemplateId)[0]
          if (mockRecord.value?.mockTemplateSnapshot !== generateSnapshot(selectedMockTemplateInfo.value)) {
            snapshotWarning.value = true;
          }
        }
        modalVisible.value = true;
      }).catch(error => {
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    const dealRemoveMock = (record: RegisteredApiInfo) => {
      mockRecord.value = record
      removeMock(record).then(() => {
        selectedMockTemplateInfo.value = undefined
        Message.success({content: '移除Mock成功！', duration: 1000, onClose: () => queryRegisteredApiList()})
      }).catch(error => {
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    const generateSnapshot = (record: MockTemplateInfo): string => {
      return record.templateContent + record.el + record.taskDefinitions + JSON.stringify(record.randomVariables)
    }
    const handleBeforeOk = async () => {
      console.log(selectedMockTemplateInfo.value)
      if (!selectedMockTemplateInfo.value) {
        Message.error({content: '请选择模板！', duration: 2 * 1000})
      }
      if (selectedMockTemplateInfo.value) {
        putMock({
          ...mockRecord.value, ...selectedMockTemplateInfo.value,
          mockTemplateId: selectedMockTemplateInfo.value.id,
          mockTemplateSnapshot: generateSnapshot(selectedMockTemplateInfo.value)
        } as PutMockInfo).then(() => {
          Message.success({content: '配置Mock成功！', duration: 1000, onClose: () => queryRegisteredApiList()})
        }).catch(error => {
          Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
        })
      }
      return selectedMockTemplateInfo.value ? true : false;
    };
    const handleCancel = () => {
      modalVisible.value = false;
      selectedMockTemplateInfo.value = undefined
    }
    return {
      pid,
      appName,
      columns,
      data,
      loading,
      expandable,
      expandKeys,
      modalVisible,
      selectedMockTemplateInfo,
      mockTemplateInfoData,
      snapshotWarning,
      handleBeforeOk,
      handleCancel,
      configMock,
      dealRemoveMock,
      expandedChange,
      parseToArgInfo,
      parseToArgInfoList
    }
  }
})
</script>
<template>
  <BasicLayout>
    <template #header>
      <div style="display: flex;justify-content: center;align-items: center;color:#4b4b4b">
        <b>应用名:</b>{{ appName }}<b>，进程号:</b>{{ pid }}
      </div>
    </template>
    <template #content>
      <a-table row-key="index" :columns="columns" :data="data" :loading="loading" :pagination="false"
               :expandable="expandable" :expanded-keys="expandKeys" @expanded-change="expandedChange">
        <template #optional="{ record }">
          <a-button @click="configMock(record)">配置Mock</a-button>
          <a-divider direction="vertical"/>
          <a-popconfirm content="确定移除该Mock配置?" @ok="dealRemoveMock(record)">
            <a-button :disabled="!record.mockEnable">移除Mock</a-button>
          </a-popconfirm>
        </template>
        <template #apiType="{record}">
          <a-tooltip :content="record.classAnnotationsDesc">
            <a-tag color="green">{{ record.apiType }}</a-tag>
          </a-tooltip>
        </template>
        <template #apiUrl="{record}">
          <a-tooltip :content="record.methodAnnotationsDesc">
            <a-tag color="green">{{ record.apiMethod + ' ' + record.apiUrl }}</a-tag>
          </a-tooltip>
        </template>
        <template #mockEnable="{ record }">
          <a-tag v-if="record.mockEnable" color="green">是</a-tag>
          <a-tag v-else color="red">否</a-tag>
        </template>
        <template #expand-row="{record}">
          <h3>类名:</h3>
          <a-textarea disabled v-model="record.className" :auto-size="true"/>
          <h3>方法名:</h3>
          <a-textarea disabled v-model="record.methodName" :auto-size="true"/>
          <h3>方法参数描述:</h3>
          <template v-if="parseToArgInfoList(record.methodArgsDesc).length===0">
            <a-textarea disabled model-value="无" :auto-size="true"/>
          </template>
          <template v-else v-for="(argInfo,index) in parseToArgInfoList(record.methodArgsDesc)">
            方法参数{{ index + 1 }}:
            <br/>
            - 参数类型:
            <a-textarea disabled v-model="argInfo.type" :auto-size="true"/>
            - 参数结构:
            <a-textarea disabled v-model="argInfo.jsonStruct" :auto-size="true"/>
          </template>
          <h3>方法返回类型描述:</h3>
          - 返回类型:
          <a-textarea disabled v-model="parseToArgInfo(record.methodReturnDesc).type" :auto-size="true"/>
          - 返回结构:
          <a-textarea disabled v-model="parseToArgInfo(record.methodReturnDesc).jsonStruct" :auto-size="true"/>
        </template>
      </a-table>
      <a-modal width="50%" v-model:visible="modalVisible" @cancel="handleCancel" :on-before-ok="handleBeforeOk"
               unmountOnClose>
        <template #title>
          Mock配置
        </template>
        <div>
          <a-input v-if="snapshotWarning" disabled default-value="该模板已发生改变，上次Mock配置可能较旧"
                   placeholder="Error status" error>
            <template #prefix>
              <icon-info-circle/>
            </template>
          </a-input>
          <h3>选择模板:</h3>
          <a-select v-model="selectedMockTemplateInfo" value-key="id" :style="{width:'100%'}"
                    placeholder="请选择Mock模板...">
            <a-option v-for="item of mockTemplateInfoData" :value="item" :label="item.tag"/>
            <template #label="{data}">
              <a-space>
                <a-tag v-for="e of data.label.split(';')" color="green">{{ e }}</a-tag>
              </a-space>
            </template>
          </a-select>
          <template v-if="selectedMockTemplateInfo">
            <h3>模板内容:</h3>
            <a-textarea disabled v-model="selectedMockTemplateInfo.templateContent" :auto-size="true"/>
            <template v-if="selectedMockTemplateInfo.el">
              <h3>模板EL表达式:</h3>
              <a-textarea disabled v-model="selectedMockTemplateInfo.el" :auto-size="true"/>
            </template>
            <random-variable-input-box :disabled="true" :random-variables="selectedMockTemplateInfo.randomVariables"/>
            <task-definition-input-box :disabled="true" :task-definitions="selectedMockTemplateInfo.taskDefinitions"/>
          </template>
          <h3>备注:</h3>
          <a-textarea disabled default-value="配置Mock只用于一次性生效，修改模板信息，需要重新配置Mock。"
                      :auto-size="true"/>
        </div>
      </a-modal>
    </template>
  </BasicLayout>
</template>

<style lang="less" scoped>

</style>
