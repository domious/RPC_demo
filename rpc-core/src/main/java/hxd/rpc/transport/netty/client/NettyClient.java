package hxd.rpc.transport.netty.client;

import hxd.rpc.loadBalance.LoadBalancer;
import hxd.rpc.transport.RpcClient;
import hxd.rpc.coder.CommonDecoder;
import hxd.rpc.coder.CommonEncoder;
import hxd.rpc.entry.RpcRequest;
import hxd.rpc.entry.RpcResponse;
import hxd.rpc.enumCommon.RpcError;
import hxd.rpc.exception.RpcException;
import hxd.rpc.loadBalance.RandomLoadBalancer;
import hxd.rpc.registry.NacosServiceRegistry;
import hxd.rpc.registry.ServiceRegistry;
import hxd.rpc.serializer.CommonSerializer;
import hxd.rpc.serializer.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author huxiaodong
 */
@Slf4j
public class NettyClient implements RpcClient {

    private static final Bootstrap bootstrap;
    private CommonSerializer serializer;
    private final ServiceRegistry serviceRegistry;

    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }
    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }
    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }
    public NettyClient(Integer serializer, LoadBalancer loadBalancer) {
        this.serviceRegistry = new NacosServiceRegistry(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
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
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        //CompletableFuture<RpcResponse> result = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceRegistry.lookUpService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            //log.info("客户端连接到服务端{}:{}", host, port);
            if (channel != null) {
                //发送request
                channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                    if (future1.isSuccess()) {
                        log.info("客户端发送消息：{}", rpcRequest.toString());
                    }else {
                        //future1.channel().close();
                        log.error("发送消息出现错误：", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                //在channel绑定rpcResponse，后续通过这个key可在channel中得到RpcResponse
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse;
            }
        } catch (InterruptedException e) {
            log.error("发送消息时发生错误：", e);
        }
        return null;
    }

}
