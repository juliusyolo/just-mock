<template>
  <template v-if="data.length>0||!disabled">
    <h3>后置任务定义:</h3>
    <a-space style="width: 100%;margin: 0 0 8px 0" v-if="!disabled">
      <a-select :options="typeOptions" v-model="selectType" :style="{width:'160px'}" placeholder="first"/>
      <a-button @click="addTaskDefinition" shape="circle">+</a-button>
    </a-space>
    <a-space direction="vertical" style="width: 100%">
      <div style="display:flex;width: 100%" v-for="(taskDefinition,index) in data">
        <a-textarea :disabled="disabled" style="width: 100%;margin-right: 1px" :model-value="taskDefinition"
                    @input="taskTaskDefinitionChange($event,index)" :auto-size="true"/>
        <a-button @click="removeTaskDefinition(index)" v-if="!disabled">移除</a-button>
      </div>
    </a-space>
  </template>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {HttpTaskDefinitionContent, TaskDefinition, TaskDefinitionType} from "../api/vm/types";


export default defineComponent({
  name: 'TaskDefinitionInputBox',
  props: {
    taskDefinitions: {type: Array<string>},
    disabled: {type: Boolean, default: false}
  },
  emits: ['change'],
  expose: ['validate'],
  watch: {
    taskDefinitions(newValue: Array<string>, oldValue) {
      this.data = newValue
    }
  },
  setup(props, {emit}) {
    const data = ref<Array<string>>([])
    onMounted(() => data.value = props.taskDefinitions as Array<string>)
    const typeOptions = Object.values(TaskDefinitionType)
    const selectType = ref<string>(typeOptions[0])
    const httpTaskDefinition: TaskDefinition<HttpTaskDefinitionContent> = {
      type: TaskDefinitionType.HTTP_TASK,
      content: {
        url: "",
        payloadType: 'application/json',
        payload: "{}",
        method: "POST"
      }
    }
    const addTaskDefinition = () => {
      if (TaskDefinitionType.HTTP_TASK === selectType.value) {
        data.value.push(JSON.stringify(httpTaskDefinition, null, 2))
        emit('change', data.value)
      }
    }
    const removeTaskDefinition = (index: number) => {
      data.value.splice(index, 1)
      emit('change', data.value)
    }
    const taskTaskDefinitionChange = (value: string, index: number) => {
      data.value[index] = value
      emit('change', data.value)
    }
    const validate = (): boolean => {
      return data.value.filter(e => Object.values(JSON.parse(e).content).filter(v => !v).length > 0 || !JSON.parse(e).type).length === 0
    }
    return {
      data,
      typeOptions,
      selectType,
      validate,
      addTaskDefinition,
      removeTaskDefinition,
      taskTaskDefinitionChange
    }
  }
})
</script>

<style scoped>

</style>
