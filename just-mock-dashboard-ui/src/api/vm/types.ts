export type VmInstance = {
  pid: string;
  name: string;
  platform: string;
  vendor: string;
  attached: boolean;
  environmentVariables: string;
}

export type VmInstanceArray = Array<VmInstance>;

export type RegisteredApiInfo = {
  pid: string;
  className: string;
  classAnnotationsDesc: string;
  methodName: string;
  methodArgsDesc: string;
  methodReturnDesc: string;
  methodAnnotationsDesc: string;
  apiUrl: string;
  apiType: string;
  apiMethod: string;
  mockEnable: boolean;
  mockTemplateId: number;
}
export type RegisteredApiInfoArray = Array<RegisteredApiInfo>;

export type ArgInfo = {
  type: string;
  jsonStruct: object;
}


export type MockTemplateInfo = {
  id: number;
  templateContent: string;
  el: string;
  tag: string;
  taskDefinitions: Array<string>;
  randomVariables: Array<RandomVariable>;
}

export type MockTemplateInfoArray = Array<MockTemplateInfo>;


export type PutMockInfo = {
  pid: string;
  className: string;
  methodName: string;
  mockTemplateId: number;
  templateContent: string;
  el: string;
  taskDefinitions: Array<string>;
  randomVariables: Array<RandomVariable>;
}

export type RandomVariable = {
  name: string;
  sequence: string;
}

export type InternalRandomVariable = {
  name: string;
  sequences: Array<string>;
}
export type RandomVariableArray = Array<RandomVariable>;

export type InternalRandomVariableArray = Array<InternalRandomVariable>;

export type AttachVMInstance = {
  pid: string;
  environmentVariables: Array<string>;
}


export type TaskDefinition<T> = {
  type: string;
  content: T;
}

export enum TaskDefinitionType  {
  HTTP_TASK = 'HTTP_TASK'
}

export type HttpTaskDefinitionContent = {
  url:string;
  payload: object;
  method: 'GET'|'POST'
}
