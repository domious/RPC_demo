package hxd.rpc.transport;


import hxd.rpc.entry.RpcRequest;
import hxd.rpc.entry.RpcResponse;
import hxd.rpc.handler.RequestHandler;
import hxd.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 服务端接口
 * @author huxiaodong
 */
@Slf4j
public class RpcServer {
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;

   private static final int CORE_POOL_SIZE = 5;
   private static final int MAX_POOL_SIZE = 50;
   private static final long KEEP_ALIVE_TIME = 60;
    public RpcServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务端启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费端连接，其ip为：{}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            threadPool.shutdown();
        } catch (Exception e) {
            log.error("服务端启动时发生错误：", e);
        }
    }

    class RequestHandlerThread implements Runnable {

        private Socket socket;
        //需要调用的服务
        private Object service;
        private ServiceRegistry serviceRegistry;

        public RequestHandlerThread(Socket socket, Object service, ServiceRegistry serviceRegistry) {
            this.socket = socket;
            this.service = service;
            this.serviceRegistry = serviceRegistry;
        }

        @Override
        public void run() {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                String interfaceName = rpcRequest.getInterfaceName();
                Object service = serviceRegistry.getService(interfaceName);
                Object result = requestHandler.handle(rpcRequest, service);
                objectOutputStream.writeObject(RpcResponse.success(result));
                objectOutputStream.flush();
            } catch (IOException | ClassNotFoundException e) {
                log.error("调用或发送时有错误发生：", e);
            }
        }
    }
}


