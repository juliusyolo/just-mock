export type VmInstance = {
  pid: string;
  name: string;
  platform: string;
  vendor: string;
  attached: boolean;
}

export type VmInstanceArray = Array<VmInstance>;
