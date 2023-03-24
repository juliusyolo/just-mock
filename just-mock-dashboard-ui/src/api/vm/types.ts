export type VmInstance = {
  pid: string;
  name: string;
  platform: string;
  vendor: string;
  attached: boolean;
}

export type VmInstanceArray = Array<VmInstance>;

export type RegisteredApiInfo = {
  pid: string;
  className: string;
  methodName: string;
  methodArgsDesc: string;
  methodReturnDesc: string;
  apiUrl: string;
  apiType: string;
  apiMethod: string;
}
export type RegisteredApiInfoArray = Array<RegisteredApiInfo>;

export type ArgInfo = {
  type: string;
  jsonStruct: object;
}
