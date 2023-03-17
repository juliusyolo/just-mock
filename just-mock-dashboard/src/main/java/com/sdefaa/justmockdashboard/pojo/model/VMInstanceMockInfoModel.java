package com.sdefaa.justmockdashboard.pojo.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Julius Wong
 * @date 2023/3/17 14:42
 * @since 1.0.0
 */
@Data
public class VMInstanceMockInfoModel {
  private Long id;
  private String pid;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
