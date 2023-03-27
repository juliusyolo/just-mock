<script lang="ts">
import {defineComponent, onMounted, reactive, ref} from 'vue'
import BasicLayout from "../components/BasicLayout.vue";
import {useRouter} from "vue-router";
import {ArgInfo, RegisteredApiInfo, RegisteredApiInfoArray} from "../api/vm/types";
import {getRegisteredApiList} from "../api/vm";
import {Message} from "@arco-design/web-vue";

export default defineComponent({
  components: {BasicLayout},
  setup() {
    const router = useRouter()
    const pid = router.currentRoute.value.params.pid
    const loading = ref(false)
    const data = ref<RegisteredApiInfoArray>([])
    const attachLoading = ref(false)
    const columns = [{
      title: '接口类型',
      slotName: 'apiType'
    }, {
      title: '接口请求类型',
      slotName: 'apiMethod'
    }, {
      title: '接口URL',
      dataIndex: 'apiUrl',
      ellipsis: true,
      tooltip: true,
    },
      {
        title: '操作',
        slotName: 'optional',
      }];
    const queryRegisteredApiList = () => {
      loading.value = true
      const currentPid = router.currentRoute.value.params.pid as string
      getRegisteredApiList(currentPid).then((d) => {
        data.value = d;
        loading.value = false
      }).catch(error => {
        loading.value = false
        Message.error({content: error.message ? error.message : '系统异常！', duration: 5 * 1000})
      })
    }
    onMounted(() => queryRegisteredApiList())
    const expandable = reactive({
        title: '方法详情',
        width: 80
      }
    );
    const parseToArgInfo = (json: string): ArgInfo => {
      return JSON.parse(json) as ArgInfo
    }
    const parseToArgInfoList = (json: string): Array<ArgInfo> => {
      return JSON.parse(json) as Array<ArgInfo>
    }
    return {
      pid,
      columns,
      data,
      loading,
      expandable,
      parseToArgInfo,
      parseToArgInfoList
    }
  }
})
</script>

<template>
  <BasicLayout>
    <template #header>
      {{ pid }}
    </template>
    <template #content>
      <a-table row-key="title" :columns="columns" :data="data" :loading="loading" :pagination="false"
               :expandable="expandable">
        <template #optional="{ record }">
          <a-button @click="configMock(record.pid)">配置Mock</a-button>
          <a-divider direction="vertical"/>
          <a-button @click="removeClick(record.pid)">移除Mock</a-button>
        </template>
        <template #apiType="{record}">
          <a-tag color="green">{{ record.apiType }}</a-tag>
        </template>
        <template #apiMethod="{record}">
          <a-tag color="green">{{ record.apiMethod }}</a-tag>
        </template>
        <template #expand-row="{record}">
          <h3>类名:</h3><b>{{ record.className }}</b>
          <h3>类注解:</h3>
          <a-textarea disabled v-model="record.classAnnotationsDesc"
                      :auto-size="true"/>
          <h3>方法名:</h3><b>{{ record.methodName }}</b>
          <h3>方法注解:</h3>
          <a-textarea disabled v-model="record.methodAnnotationsDesc"
                      :auto-size="true"/>
          <h3>方法参数描述:</h3>
          <template v-if="parseToArgInfoList(record.methodArgsDesc).length===0">无</template>
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
          <a-textarea disabled v-model="parseToArgInfo(record.methodReturnDesc).type"
                      :auto-size="true"/>
          - 返回结构:
          <a-textarea disabled v-model="parseToArgInfo(record.methodReturnDesc).jsonStruct" :auto-size="true"/>
        </template>
      </a-table>
    </template>
  </BasicLayout>
</template>

<style lang="less" scoped>

</style>
