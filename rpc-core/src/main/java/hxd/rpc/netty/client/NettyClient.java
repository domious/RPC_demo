package hxd.rpc.netty.client;

import hxd.rpc.RpcClient;
import hxd.rpc.coder.CommonDecoder;
import hxd.rpc.coder.CommonEncoder;
import hxd.rpc.entry.RpcRequest;
import hxd.rpc.entry.RpcResponse;
import hxd.rpc.serializer.JsonSerializer;
import hxd.rpc.serializer.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huxiaodong
 */
@Slf4j
public class NettyClient implements RpcClient {

    private String host;
    private int port;
    private static final Bootstrap bootstrap;

    public NettyClient(final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    //配置netty客户端
    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new KryoSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(final RpcRequest rpcRequest) {
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务端{}:{}", host, port);
            Channel channel = future.channel();
            if (channel != null) {
                //发送request
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        log.info("客户端发送消息：{}", rpcRequest.toString());
                    }else {
                        log.error("发送消息出现错误：", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                //在channel绑定rpcResponse，后续通过这个key可在channel中得到RpcResponse
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            log.error("发送消息时发生错误：", e);
        }
        return null;
    }
}
