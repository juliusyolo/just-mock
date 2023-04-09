package com.sdefaa.just.mock.agent.core.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class CommonConstant {
    public final static String REQUEST_MAPPING_CLASS = "org.springframework.web.bind.annotation.RequestMapping";
    public final static List<String> TARGET_MVC_MAPPING_CLASS_LIST = Arrays.asList("org.springframework.web.bind.annotation.PostMapping",
            "org.springframework.web.bind.annotation.GetMapping",
            "org.springframework.web.bind.annotation.PutMapping",
            "org.springframework.web.bind.annotation.PatchMapping",
            "org.springframework.web.bind.annotation.DeleteMapping",
            REQUEST_MAPPING_CLASS);
}
