<template>
  <template v-if="data.length>0||!disabled">
    <h3>随机变量:</h3>
    <a-space style="width: 100%;margin: 0 0 8px 0">
      <a-button @click="addVariable" shape="circle" v-if="!disabled">+</a-button>
    </a-space>
    <a-space direction="vertical" style="width: 100%">
      <a-input-group style="width: 100%" v-for="(randomVariable,index) in data">
        <a-input :disabled="disabled" style="width: 30%" v-model="randomVariable.name" placeholder="请输入变量名称..."
                 @input="nameInputChange($event,index)"/>
        <a-input-tag :disabled="disabled" v-model="randomVariable.sequences" placeholder="请输入随机值..."
                     @change="tagsInputChange($event,index)" allow-clear/>
        <a-button @click="removeVariable(index)" v-if="!disabled">移除</a-button>
      </a-input-group>
    </a-space>
  </template>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {InternalRandomVariableArray, RandomVariable} from "../api/vm/types";
import {Message} from "@arco-design/web-vue";

export default defineComponent({
  name: 'RandomVariableInputBox',
  props: {
    randomVariables: {type: Array<RandomVariable>},
    disabled: {type: Boolean, default: false}
  },
  expose: ['validate'],
  emits: ['change'],
  watch: {
    randomVariables(newValue: Array<RandomVariable>, ordValue: Array<RandomVariable>) {
      this.data = newValue?.map(e => ({
        name: e.name,
        sequences: e.sequence ? e.sequence.split(',') : []
      })) as InternalRandomVariableArray
      console.log(this.data)
    }
  },
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
      emit('change', data.value.map(e => ({
        name: e.name,
        sequence: e.sequences.join(',')
      })))
    }
    const tagsInputChange = (tags: Array<string>, index: number) => {
      emit('change', data.value.map(e => ({
        name: e.name,
        sequence: e.sequences.join(',')
      })))
    }
    const validate = (): boolean => {
      return data.value.filter(e => !e.name || e.sequences.length === 0).length === 0
    }
    return {
      data,
      validate,
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
