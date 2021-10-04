package hxd.rpc.netty.server;

import hxd.rpc.RpcServer;
import hxd.rpc.coder.CommonDecoder;
import hxd.rpc.coder.CommonEncoder;
import hxd.rpc.enumCommon.RpcError;
import hxd.rpc.exception.RpcException;
import hxd.rpc.provider.ServiceProvider;
import hxd.rpc.provider.ServiceProviderImpl;
import hxd.rpc.registry.NacosServiceRegistry;
import hxd.rpc.registry.ServiceRegistry;
import hxd.rpc.serializer.CommonSerializer;
import hxd.rpc.serializer.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author huxiaodong
 */
@Slf4j
public class NettyServer implements RpcServer {

    private final String host;
    private final int port;
    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;
    private CommonSerializer serializer;


    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    /**
     * 服务端注册netty，设置编码器，解码器，数据处理器等绑定port
     */
    @Override
    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //编码器
                            pipeline.addLast(new CommonEncoder(new KryoSerializer()));
                            //解码器
                            pipeline.addLast(new CommonDecoder());
                            //数据处理器
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("启动服务器有错误：", e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * publishService把服务保存到本地注册表中，并注册到nacos上
     */
    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.registry(service);
        serviceRegistry.registry(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
        start();
    }

    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
