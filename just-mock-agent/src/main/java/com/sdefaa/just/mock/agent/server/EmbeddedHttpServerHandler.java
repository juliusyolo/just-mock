package com.sdefaa.just.mock.agent.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdefaa.just.mock.common.constant.CommonConstant;
import com.sdefaa.just.mock.common.pojo.ApiMockCommandDTO;
import com.sdefaa.just.mock.common.strategy.MockManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class EmbeddedHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Map<String, RequestHandler> REQUEST_HANDLER_MAPPING = new HashMap<>();

    static {
        REQUEST_HANDLER_MAPPING.put("GET ".concat(CommonConstant.PING_URL), RequestHandler.PING);
        REQUEST_HANDLER_MAPPING.put("POST ".concat(CommonConstant.COMMAND_URL), RequestHandler.ACTIVE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        RequestHandler requestHandler = Optional.ofNullable(REQUEST_HANDLER_MAPPING.get(fullHttpRequest.method().name() + " " + fullHttpRequest.uri())).orElse(RequestHandler.DEFAULT_404);
        FullHttpResponse response = requestHandler.handle(fullHttpRequest);
        channelHandlerContext.writeAndFlush(response);
        channelHandlerContext.channel().close();
    }

    @FunctionalInterface
    public interface RequestHandler {
        FullHttpResponse handle(FullHttpRequest request);

        RequestHandler PING = request -> {
            ByteBuf content = Unpooled.copiedBuffer(CommonConstant.PONG, CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, CommonConstant.TEXT_PLAIN);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            return response;
        };

        RequestHandler ACTIVE = request -> {
            // 获取请求正文
            String body = request.content().toString(CharsetUtil.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = "{\"code\":\"000000\",\"message\":\"成功\"}";
            try {
              ApiMockCommandDTO  apiMockCommandDTO = objectMapper.readValue(body, ApiMockCommandDTO.class);
              String commandType = apiMockCommandDTO.getCommandType();
              if (Objects.equals(commandType,ApiMockCommandDTO.CommandType.PUT.name())){
                MockManager.INSTANCE.putMock(apiMockCommandDTO.getClazzName(),apiMockCommandDTO.getMethodName(),apiMockCommandDTO.getTemplateContent(),apiMockCommandDTO.getEl(),apiMockCommandDTO.getRandomVariables(),apiMockCommandDTO.getTaskDefinitions());
              }else if (Objects.equals(commandType,ApiMockCommandDTO.CommandType.REMOVE.name())){
                MockManager.INSTANCE.removeMock(apiMockCommandDTO.getClazzName(),apiMockCommandDTO.getMethodName());
              }else {
                responseBody = "{\"code\":\"999999\",\"message\":\"命令未匹配\"}";
              }
            } catch (Exception e) {
              responseBody = "{\"code\":\"999999\",\"message\":\""+e.getMessage()+"\"}";
            }
            ByteBuf content = Unpooled.copiedBuffer(responseBody, CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, CommonConstant.APPLICATION_JSON);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            return response;
        };

        RequestHandler DEFAULT_404 = request -> {
            ByteBuf content = Unpooled.copiedBuffer(CommonConstant.NOT_FOUND, CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, CommonConstant.TEXT_PLAIN);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            return response;
        };

    }

}
