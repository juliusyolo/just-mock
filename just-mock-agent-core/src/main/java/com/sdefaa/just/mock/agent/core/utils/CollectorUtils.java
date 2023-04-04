package com.sdefaa.just.mock.agent.core.utils;

import com.sdefaa.just.mock.agent.core.constant.CommonConstant;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class CollectorUtils {
//"@org.springframework.web.bind.annotation.GetMapping(headers=[], path=[], produces=[], name=, params=[], value=[/hello], consumes=[])
    public final static String getMVCMappingMark(List<String> annotationList){
      String targetAnnotation = annotationList.stream().filter(annotation -> CommonConstant.TARGET_MVC_MAPPING_CLASS_LIST.stream().anyMatch(annotation::contains)).collect(Collectors.joining());
      if (targetAnnotation.contains(CommonConstant.REQUEST_MAPPING_CLASS)){

      }
      return null;
    }
}
