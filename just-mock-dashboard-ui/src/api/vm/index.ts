import axios, {AxiosResponse} from "axios";
import {VmInstanceArray} from "./types";

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

export async function attachVMInstance(pid:string):Promise<void>{
  return await axios.get('/v1/api/vm/instance/'+pid+'/attach') as void;
}
