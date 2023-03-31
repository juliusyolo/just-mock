<template>
  <h3>后置任务定义:</h3>
  <a-space direction="vertical" style="width: 100%">
    <a-space>
      <a-select :options="typeOptions" v-model="selectType" :style="{width:'160px'}" placeholder="first"/>
      <a-button @click="addTaskDefinition" shape="circle">+</a-button>
    </a-space>
    <a-space direction="vertical" style="width: 100%">
      <div style="display:flex;width: 100%" v-for="(taskDefinition,index) in data">
        <a-textarea style="width: 90%;margin-right: 5px" :model-value="taskDefinition"
                    @input="taskTaskDefinitionChange($event,index)" :auto-size="true"/>
        <a-button @click="removeTaskDefinition(index)">移除</a-button>
      </div>
    </a-space>
  </a-space>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {HttpTaskDefinitionContent, TaskDefinition, TaskDefinitionType} from "../api/vm/types";


export default defineComponent({
  name: 'TaskDefinitionInputBox',
  props: {
    taskDefinitions: Array<string>
  },
  emits: ['change'],
  setup(props, {emit}) {
    const data = ref<Array<string>>([])
    onMounted(() => data.value = props.taskDefinitions as Array<string>)
    const typeOptions = Object.values(TaskDefinitionType)
    const selectType = ref<string>(typeOptions[0])
    const httpTaskDefinition: TaskDefinition<HttpTaskDefinitionContent> = {
      type: TaskDefinitionType.HTTP_TASK,
      content: {
        url: "",
        payload: {},
        method: "GET"
      }
    }
    const addTaskDefinition = () => {
      if (TaskDefinitionType.HTTP_TASK === selectType.value) {
        data.value.push(JSON.stringify(httpTaskDefinition, null, 2))
      }
    }
    const removeTaskDefinition = (index: number) => {
      data.value.splice(index, 1)
      emit('change', data.value.filter(e => Object.values(JSON.parse(e).content).filter(e => !e).length === 0 && JSON.parse(e).type))
    }
    const taskTaskDefinitionChange = (value: string, index: number) => {
      data.value[index] = value
      emit('change', data.value.filter(e => Object.values(JSON.parse(e).content).filter(e => !e).length === 0 && JSON.parse(e).type))
    }

    return {
      data,
      typeOptions,
      selectType,
      addTaskDefinition,
      removeTaskDefinition,
      taskTaskDefinitionChange
    }
  }
})
</script>

<style scoped>

</style>
