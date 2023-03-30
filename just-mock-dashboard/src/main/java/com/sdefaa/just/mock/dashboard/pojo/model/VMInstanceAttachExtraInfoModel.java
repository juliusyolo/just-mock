package com.sdefaa.just.mock.dashboard.pojo.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
@Data
public class VMInstanceAttachExtraInfoModel {
  private Long id;
  private String pid;
  private Integer port;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
