package com.sdefaa.just.mock.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.agent.config.JustMockAgentConfigLoader;
import com.sdefaa.just.mock.agent.core.collector.ClassInformationCollectorManager;
import com.sdefaa.just.mock.agent.core.pojo.TargetClass;
import com.sdefaa.just.mock.agent.core.utils.CollectorUtils;
import com.sdefaa.just.mock.agent.pojo.AgentConfigProperties;
import com.sdefaa.just.mock.agent.server.EmbeddedHttpServer;
import com.sdefaa.just.mock.agent.transformer.MockClassFileASMTransformer;
import com.sdefaa.just.mock.common.constant.CommonConstant;
import com.sdefaa.just.mock.common.pojo.*;
import com.sdefaa.just.mock.common.utils.CommonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Julius Wong
 * <p>
 * 代理agentmain入口类
 * </p>
 * @since 1.0.0
 */
public class MockAgentMain {

    private static final Logger logger = Logger.getLogger(MockAgentMain.class.getName());
    private static final AtomicBoolean LOADED = new AtomicBoolean(false);

    public static final AtomicBoolean TRANSFORM_HAS_EXCEPTION = new AtomicBoolean(false);

    private static final String WHITE_SPACE = " ";

    /**
     * @param args config.yml pid environmentVariable1 environmentVariable2 ...
     */
    public static void agentmain(String args, Instrumentation instrumentation) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (LOADED.compareAndSet(false, true)) {
            String[] argvs = args.split(WHITE_SPACE);
            int availablePort = CommonUtils.getAvailablePort();
            // 启动内嵌HttpServer，用于心跳检测和注册Mock拦截
            EmbeddedHttpServer server = new EmbeddedHttpServer(availablePort);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    server.start();
                } catch (InterruptedException e) {
                    logger.info("interrupted");
                    server.stop();
                }
            });
            // 添加虚拟机shutdownHook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                future.cancel(true);
                server.stop();
            }));
            AgentConfigProperties agentConfigProperties = JustMockAgentConfigLoader.INSTANCE.load(argvs[0]).agentConfigProperties();
            Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
            Arrays.stream(allLoadedClasses).filter(aClass -> aClass.getName().contains("RiskDispatchController")).findFirst().ifPresent(aClass -> {
                logger.info(aClass.getName());
                CollectorUtils.generateAnnotationList(aClass).forEach(logger::info);
                logger.info(aClass.getSuperclass().getName());
                CollectorUtils.generateAnnotationList(aClass.getSuperclass()).forEach(logger::info);
                CollectorUtils.generateAnnotationList(aClass.getInterfaces()).forEach(logger::info);
            });
            List<TargetClass> loadedTargetClasses = Arrays.stream(allLoadedClasses).flatMap(ClassInformationCollectorManager::collectTargetClassStream).collect(Collectors.toList());
            logger.info("Loaded Target Classes:" + loadedTargetClasses);
            List<String> environmentVariableList;
            if (argvs.length > 2) {
                environmentVariableList = new ArrayList<>();
                for (int i = 2; i < argvs.length; i++) {
                    environmentVariableList.add(argvs[i]);
                }
            } else {
                environmentVariableList = null;
            }
            loadedTargetClasses.forEach(targetClass -> {
                instrumentation.addTransformer(new MockClassFileASMTransformer(targetClass, environmentVariableList), true);
                try {
                    logger.info(targetClass.getClazz().getName());
                    instrumentation.retransformClasses(targetClass.getClazz());
                } catch (Exception e) {
                    logger.info("unmodifiable class exception:" + e);
                    throw new RuntimeException(e);
                }
            });
            if (TRANSFORM_HAS_EXCEPTION.get()) {
                throw new RuntimeException("目标类转换存在异常");
            }
            List<ApiInfo> apiInfoList = loadedTargetClasses.stream()
                    .flatMap(MockAgentMain::generateApiInfoFromTargetClass)
                    .collect(Collectors.toList());
            ApiRegistryDTO apiRegistryDTO = new ApiRegistryDTO();
            apiRegistryDTO.setApiInfos(apiInfoList);
            apiRegistryDTO.setPid(argvs[1]);
            apiRegistryDTO.setPort(availablePort);
            //  Upload apiInfoList
            logger.info(apiRegistryDTO.toString());
            registry(agentConfigProperties.getRegistryUrl(), apiRegistryDTO);
        }
    }

    public static void registry(String url, ApiRegistryDTO apiRegistryDTO) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(apiRegistryDTO));
        byte[] bytes = objectMapper.writeValueAsBytes(apiRegistryDTO);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", CommonConstant.APPLICATION_JSON);
        con.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream();) {
            os.write(bytes);
        }
        int responseCode = con.getResponseCode();
        String response = new BufferedReader(new InputStreamReader(con.getInputStream())).lines().collect(Collectors.joining(System.lineSeparator()));
        logger.info("register Api, status code:" + responseCode + ",message:" + response);
    }

    public final static Stream<ApiInfo> generateApiInfoFromTargetClass(TargetClass targetClass) {
        return targetClass.getTargetMethods().stream().map(targetMethod -> {
            ApiInfo apiInfo = new ApiInfo();
            apiInfo.setApiType(targetClass.getMark());
            apiInfo.setApiUrl(targetMethod.getMark());
            apiInfo.setApiMethod(targetMethod.getMethodName());
            ApiClassInfo apiClassInfo = new ApiClassInfo();
            apiClassInfo.setAnnotations(targetClass.getAnnotations());
            apiClassInfo.setClassName(targetClass.getClazz().getName());
            apiInfo.setApiClassInfo(apiClassInfo);
            ApiMethodInfo apiMethodInfo = new ApiMethodInfo();
            apiMethodInfo.setMethodName(targetMethod.getMethodName());
            apiMethodInfo.setAnnotations(targetMethod.getAnnotations());
            ApiMethodArgInfo output = new ApiMethodArgInfo();
            output.setType(targetMethod.getMethod().getReturnType().getName());
            output.setJsonStruct(CommonUtils.generateClassStruct(targetMethod.getMethod().getReturnType()));
            apiMethodInfo.setOutput(output);
            List<ApiMethodArgInfo> inputs = Arrays.stream(targetMethod.getMethod().getParameters()).map(parameter -> {
                ApiMethodArgInfo input = new ApiMethodArgInfo();
                input.setType(parameter.getType().getTypeName());
                input.setJsonStruct(CommonUtils.generateClassStruct(parameter.getType()));
                return input;
            }).collect(Collectors.toList());
            apiMethodInfo.setInputs(inputs);
            apiInfo.setApiMethodInfo(apiMethodInfo);
            return apiInfo;
        });
    }
}
