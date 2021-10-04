package hxd.rpc.socket.server;

import hxd.rpc.entry.RpcRequest;
import hxd.rpc.entry.RpcResponse;
import hxd.rpc.RequestHandler;
import hxd.rpc.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author huxiaodong
 */
@Slf4j
public class RequestHandlerThread implements Runnable{
    private RequestHandler requestHandler = new RequestHandler();
    private Socket socket;
    //需要调用的服务
    private Object service;
    private ServiceProvider serviceProvider;

    public RequestHandlerThread(Socket socket, Object service, ServiceProvider serviceProvider) {
        this.socket = socket;
        this.service = service;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceProvider.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用或发送时有错误发生：", e);
        }
    }
}
