package com.sdefaa.just.mock.common.task.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.constant.CommonConstant;
import com.sdefaa.just.mock.common.enums.HttpMethodEnum;
import com.sdefaa.just.mock.common.pojo.HttpTaskDefinition;
import com.sdefaa.just.mock.common.pojo.HttpTaskDefinitionContent;
import com.sdefaa.just.mock.common.task.AbstractPostProcessor;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class HttpTaskPostProcessor extends AbstractPostProcessor {
  private static final Logger logger = Logger.getLogger(HttpTaskPostProcessor.class.getName());
  private final static String HTTP_TASK_TEMPLATE_KEY = "HTTP_TASK";
  private final HttpTaskDefinition httpTaskDefinition;

  private final Map<String,Object> modelMap;

  public HttpTaskPostProcessor(HttpTaskDefinition httpTaskDefinition,Map<String,Object> modelMap) {
    this.httpTaskDefinition = httpTaskDefinition;
    this.modelMap = modelMap;
  }

  @Override
  protected void process() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    HttpTaskDefinitionContent content = httpTaskDefinition.getContent();
    StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
    stringTemplateLoader.putTemplate(HTTP_TASK_TEMPLATE_KEY, content.getPayload());
    Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
    configuration.setTemplateLoader(stringTemplateLoader);
    Template template = configuration.getTemplate(HTTP_TASK_TEMPLATE_KEY);
    try(StringWriter writer = new StringWriter()) {
      template.process(modelMap,writer);
      HttpURLConnection con = null;
      if (HttpMethodEnum.GET.name().equals(content.getMethod().toUpperCase())){
        URL obj = new URL(content.getUrl()+"?"+writer.toString());
        con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(content.getMethod().toUpperCase());
      }else {
        URL obj = new URL(content.getUrl());
        con = (HttpURLConnection) obj.openConnection();
        byte[] bytes = writer.toString().getBytes();
        con.setRequestMethod(content.getMethod().toUpperCase());
        con.setRequestProperty("Content-Type", content.getPayloadType());
        con.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream();) {
          os.write(bytes);
        }
      }
      int responseCode = con.getResponseCode();
      String response = new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining(System.lineSeparator()));
      logger.info("Http Task Post Process,code:" + responseCode + ",message:" + response);
    }
  }
}
