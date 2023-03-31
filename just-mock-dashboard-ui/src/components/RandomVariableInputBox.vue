<template>
  <h3>随机变量:</h3>
  <a-space direction="vertical" style="width: 100%">
    <a-button @click="addVariable" shape="circle">+</a-button>
    <a-space direction="vertical" style="width: 100%">
      <a-input-group style="width: 100%" v-for="(randomVariable,index) in data">
        <a-input style="width: 30%" v-model="randomVariable.name" placeholder="请输入变量名称..."
                 @input="nameInputChange($event,index)"/>
        <a-input-tag v-model="randomVariable.sequences" placeholder="请输入随机值..."
                     @change="tagsInputChange($event,index)" allow-clear/>
        <a-button @click="removeVariable(index)">移除</a-button>
      </a-input-group>
    </a-space>
  </a-space>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {
  InternalRandomVariableArray,
  RandomVariable
} from "../api/vm/types";
import {Message} from "@arco-design/web-vue";

export default defineComponent({
  name: 'RandomVariableInputBox',
  props: {
    randomVariables: Array<RandomVariable>
  },
  emits: ['change'],
  setup(props, {emit}) {
    onMounted(() => {
      data.value = props.randomVariables?.map(e => ({
        name: e.name,
        sequences: e.sequence.split(',')
      })) as InternalRandomVariableArray
    })
    const data = ref<InternalRandomVariableArray>([])
    const addVariable = () => {
      data.value.push({name: '', sequences: []})
      emit('change', data.value.map(e => ({
        name: e.name,
        sequence: e.sequences.join(',')
      })))
    }
    const removeVariable = (index: number) => {
      data.value.splice(index, 1)
      emit('change', data.value.map(e => ({
        name: e.name,
        sequence: e.sequences.join(',')
      })))
    }
    const nameInputChange = (name: string, index: number) => {
      if (name) {
        const count = data.value.filter(e => e.name === name).length
        if (count > 1) {
          Message.warning({content: '变量名称不能重复！', duration: 2000})
          data.value[index].name = ''
        }
      }
      validate(index)
    }
    const tagsInputChange = (tags: Array<string>, index: number) => {
      validate(index)
    }
    const validate = (index: number) => {
      emit('change', data.value.map(e => ({
        name: e.name,
        sequence: e.sequences.join(',')
      })))
    }
    return {
      data,
      addVariable,
      removeVariable,
      nameInputChange,
      tagsInputChange
    }
  }
})
</script>

<style scoped>

</style>
