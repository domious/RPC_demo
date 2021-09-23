package hxd.rpc.transport;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
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

    public RpcServer() {
        int corePoolSize = 5;
        int maxPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void register(Object service, int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            log.info("服务端启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("客户端连接，其ip为：" + socket.getInetAddress());
                threadPool.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class WorkerThread implements Runnable {

        Socket socket;
        Object service;

        public WorkerThread(Socket socket, Object service) {
            this.socket = socket;
            this.service = service;
        }

        @Override
        public void run() {
            try (){

            }
        }
    }
}
