<template>
  <template v-for="randomVariable in randomVariables">
    <a-input-tag v-model="randomVariable.sequences" placeholder="请输入随机数..." allow-clear/>
  </template>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {InternalRandomVariable} from "../api/vm/types";

export default defineComponent({
  props:{
    randomVariables: Array<InternalRandomVariable>
  },
  setup(){


    return {

    }
  }
})
</script>

<style scoped>

</style>
