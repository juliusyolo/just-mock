import {ArgInfo} from "../api/vm/types";

export function parseObject(json: string): ArgInfo {
  return JSON.parse(json) as ArgInfo
}

