import axios, {AxiosResponse} from "axios";
import {
  AttachVMInstance,
  MockTemplateInfo,
  MockTemplateInfoArray,
  PutMockInfo,
  RegisteredApiInfo,
  RegisteredApiInfoArray,
  VmInstanceArray
} from "./types";

const SUCCESS_CODE = '000000';

axios.interceptors.response.use((response: AxiosResponse) => {
  // 解包装响应体
  const resp = response.data;
  // 如果解包装成功，则返回解包装后的数据
  if (resp !== undefined && resp.code === SUCCESS_CODE) {
    return resp.data;
  }else {
    throw new Error(resp !== undefined ? resp.message : '响应数据为空~');
  }
})

export async function getAllVMInstances(): Promise<VmInstanceArray> {
  return await axios.get('/v1/api/vm/instances/list') as VmInstanceArray;
}

export async function attachVMInstance(attachVMInstance: AttachVMInstance):Promise<void>{
  return await axios.post('/v1/api/vm/instance/attach',attachVMInstance) as void;
}

export async function getRegisteredApiList(pid:string):Promise<RegisteredApiInfoArray>{
  return await axios.get('/v1/api/vm/instance/'+pid+'/api/list') as RegisteredApiInfoArray;
}

export async function removeMock(record:RegisteredApiInfo):Promise<void>{
  return await axios.post('/v1/api/vm/instance/mock/remove',record) as void;
}

export async function putMock(record:PutMockInfo):Promise<void>{
  return await axios.post('/v1/api/vm/instance/mock/put',record) as void;
}

export async function getMockTemplateInfoList():Promise<MockTemplateInfoArray>{
  return await axios.get('/v1/api/mock/template/list') as MockTemplateInfoArray;
}

export async function putMockTemplateInfo(record:MockTemplateInfo):Promise<void>{
  return await axios.post('/v1/api/mock/template/put',record) as void;
}

export async function removeMockTemplateInfo(id:number):Promise<void>{
  return await axios.get('/v1/api/mock/template/{id}/remove') as void;
}
