package com.sdefaa.just.mock.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Julius Wong
 * <p>
 * 注册API传输实体
 * <p>
 * @since 1.0.0
 */
@Data
public class ApiRegistryDTO implements Serializable {
  private String pid;
  private Integer port;
  private List<ApiInfo> apiInfos;
}
