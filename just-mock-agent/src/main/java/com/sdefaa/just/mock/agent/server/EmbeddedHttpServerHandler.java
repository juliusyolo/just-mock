package com.sdefaa.just.mock.agent.server;

import com.sdefaa.just.mock.agent.MockAgentMain;
import com.sdefaa.just.mock.common.constant.CommonConstant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Julius Wong
 * @since 1.0.0
 */
public class EmbeddedHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Map<String, RequestHandler> REQUEST_HANDLER_MAPPING = new HashMap<>();

    static {
        REQUEST_HANDLER_MAPPING.put("GET ".concat(CommonConstant.PING_URL), RequestHandler.PING);
        REQUEST_HANDLER_MAPPING.put("POST ".concat(CommonConstant.ACTIVE_URL), RequestHandler.ACTIVE);
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
            System.out.println(body);
            System.out.println("请求方法名：" + request.method().name());
            System.out.println(request.uri());
            MockAgentMain.map.put("","");
            ByteBuf content = Unpooled.copiedBuffer(body, CharsetUtil.UTF_8);
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
