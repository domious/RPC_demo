package hxd.rpc.transport.socket.server;

import hxd.rpc.factory.ThreadPoolFactory;
import hxd.rpc.handler.RequestHandler;
import hxd.rpc.hook.ShutdownHook;
import hxd.rpc.loadBalance.RandomLoadBalancer;
import hxd.rpc.provider.ServiceProvider;
import hxd.rpc.provider.ServiceProviderImpl;
import hxd.rpc.registry.NacosServiceRegistry;
import hxd.rpc.serializer.CommonSerializer;
import hxd.rpc.transport.AbstractRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author huxiaodong
 */
@Slf4j
public class SocketServer extends AbstractRpcServer {
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final CommonSerializer serializer;

    public SocketServer(String host, int port, Integer serializer) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry(new RandomLoadBalancer());
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
        serviceScan();
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("服务器启动……");
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }
}
