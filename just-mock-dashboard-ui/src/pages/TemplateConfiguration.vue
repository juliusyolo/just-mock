<template>
  <div class="header-group">
    <a-breadcrumb>
      <a-breadcrumb-item>模板列表</a-breadcrumb-item>
    </a-breadcrumb>
    <a-button-group>
      <a-button @click="dealAddTemplate">新增</a-button>
    </a-button-group>
  </div>
  <a-table :columns="columns" :data="data" :loading="loading" :pagination="false">
    <template #tag="{ record }">
      <a-space>
        <template v-for="e in record.tag.split(';')">
          <a-tag color="green">{{ e }}</a-tag>
        </template>
      </a-space>
    </template>
    <template #conditionalMock="{ record }">
      <a-tooltip v-if="record.el" :content="record.el">
        <a-tag color="green">是</a-tag>
      </a-tooltip>
      <a-tag v-else color="red">否</a-tag>
    </template>
    <template #optional="{ record }">
      <a-button @click="dealEditTemplate(record)">编辑</a-button>
      <a-divider direction="vertical"/>
      <a-popconfirm content="确定移除该Mock模板?" @ok="dealRemoveTemplate(record)">
        <a-button>删除</a-button>
      </a-popconfirm>
    </template>
  </a-table>
  <a-modal width="50%" v-model:visible="modalVisible" @cancel="handleCancel" :on-before-ok="handleBeforeOk"
           unmountOnClose>
    <template #title>
      <template v-if="isEditable">编辑模板</template>
      <template v-else>新增模板</template>
    </template>
    <div>
      <h3>模板标签:</h3>
      <a-input-tag v-model="inputTags" placeholder="请输入标签..." @change="tagInputChange" allow-clear/>
      <h3>模板内容:</h3>
      <a-textarea v-model="mockTemplateInfoModel.templateContent" :auto-size="true"/>
      <h3>模板EL表达式:</h3>
      <a-textarea v-model="mockTemplateInfoModel.el" :auto-size="true"/>
      <random-variable-input-box ref="randomVariableInputBox" :random-variables="mockTemplateInfoModel.randomVariables"
                                 @change="randomVariablesChange"/>
      <task-definition-input-box ref="taskDefinitionInputBox" :task-definitions="mockTemplateInfoModel.taskDefinitions"
                                 @change="taskDefinitionsChange"/>
      <h3>备注:</h3>
      <a-textarea disabled
                  default-value="使用freemarker作为模板引擎，EL表达式支持JSR245规范，环境变量现有请求方法参数，全局环境变量（变量名以p0,p1,p2,p3以此类推，全局环境变量在末尾），自定义随机变量（变量名以定义的为准）。"
                  :auto-size="true"/>
    </div>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue'
import {MockTemplateInfo, MockTemplateInfoArray, RandomVariableArray} from "../api/vm/types";
import {getMockTemplateInfoList, putMockTemplateInfo, removeMockTemplateInfo} from "../api/vm";
import {Message} from "@arco-design/web-vue";
import RandomVariableInputBox from "../components/RandomVariableInputBox.vue";
import TaskDefinitionInputBox from "../components/TaskDefinitionInputBox.vue";

export default defineComponent({
  components: {TaskDefinitionInputBox, RandomVariableInputBox},
  setup: function () {
    const loading = ref(false)
    const data = ref<MockTemplateInfoArray>([])
    const modalVisible = ref<boolean>(false)
    const isEditable = ref<boolean>(false)
    const mockTemplateInfoModel = ref<MockTemplateInfo>()
    const inputTags = ref<Array<string>>([])
    const randomVariables = ref<RandomVariableArray>([])
    const randomVariableInputBox = ref<InstanceType<typeof RandomVariableInputBox> | null>(null)
    const taskDefinitionInputBox = ref<InstanceType<typeof TaskDefinitionInputBox> | null>(null)
    const queryAllMockTemplateInfos = () => {
      loading.value = true
      getMockTemplateInfoList().then((d) => {
        data.value = d;
        loading.value = false
      }).catch(error => {
        loading.value = false
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    const queryAllMockTemplateInfosWithoutState = () => {
      getMockTemplateInfoList().then((d) => {
        data.value = d;
      }).catch(error => {
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    const dealAddTemplate = () => {
      mockTemplateInfoModel.value = {
        randomVariables: [],
        taskDefinitions: []
      } as unknown as MockTemplateInfo;
      inputTags.value = []
      modalVisible.value = true;
      isEditable.value = false;
    }
    const dealEditTemplate = (record: MockTemplateInfo) => {
      mockTemplateInfoModel.value = record;
      inputTags.value = record.tag.split(';')
      modalVisible.value = true;
      isEditable.value = true;
    }
    const dealRemoveTemplate = (record: MockTemplateInfo) => {
      console.log(record.id)
      removeMockTemplateInfo(record.id).then(() => {
        Message.success({content: '删除Mock模板成功！', duration: 1000, onClose: () => queryAllMockTemplateInfos()})
      }).catch(error => {
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    const handleBeforeOk = async () => {
      if (!mockTemplateInfoModel.value || !mockTemplateInfoModel.value?.tag || !mockTemplateInfoModel.value?.templateContent) {
        Message.error({content: '模板标签和内容必填！', duration: 2 * 1000})
        return false;
      }
      if (!randomVariableInputBox.value?.validate()) {
        Message.error({content: '随机变量存在未填写！', duration: 2 * 1000})
        return false;
      }
      if (!taskDefinitionInputBox.value?.validate()) {
        Message.error({content: '任务定义存在未填写（值不能为空）！', duration: 2 * 1000})
        return false;
      }
      if (mockTemplateInfoModel.value) {
        putMockTemplateInfo(mockTemplateInfoModel.value).then(() => {
          Message.success({content: '配置模板成功！', duration: 1000, onClose: () => queryAllMockTemplateInfos()})
        }).catch(error => {
          Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
        })
      }
      return true;
    };
    const handleCancel = () => {
      modalVisible.value = false;
    }
    const tagInputChange = (tags: Array<string>) => {
      mockTemplateInfoModel.value = {...mockTemplateInfoModel.value, tag: tags.join(";")} as MockTemplateInfo
    }
    onMounted(() => {
      queryAllMockTemplateInfos()
      setInterval(() => {
        queryAllMockTemplateInfosWithoutState()
      }, 5000)
    })
    const columns = [{
      title: '模板标签',
      slotName: 'tag',
    }, {
      title: '是否el条件mock',
      slotName: 'conditionalMock',
    }, {
      title: '模板内容',
      dataIndex: 'templateContent',
      ellipsis: true,
      tooltip: true
    }, {
      title: '操作',
      slotName: 'optional',
    }];
    const randomVariablesChange = (records: RandomVariableArray) => {
      mockTemplateInfoModel.value = {
        ...mockTemplateInfoModel.value,
        randomVariables: records
      } as MockTemplateInfo
    }
    const taskDefinitionsChange = (records: Array<string>) => {
      mockTemplateInfoModel.value = {
        ...mockTemplateInfoModel.value,
        taskDefinitions: records
      } as MockTemplateInfo
    }
    return {
      columns,
      loading,
      data,
      modalVisible,
      mockTemplateInfoModel,
      inputTags,
      isEditable,
      randomVariables,
      randomVariableInputBox,
      taskDefinitionInputBox,
      randomVariablesChange,
      taskDefinitionsChange,
      tagInputChange,
      dealAddTemplate,
      dealEditTemplate,
      dealRemoveTemplate,
      handleBeforeOk,
      handleCancel
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
