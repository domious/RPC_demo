package hxd.rpc.transport;

import hxd.rpc.entry.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 客户端接口
 * @author huxiaodong
 */
@Slf4j
public class RpcClient {

    /**
     * 通过java序列化方式，用Socket进行传输，把发送的request对象传给ObjectOutputStream，返回获取inputStream.readObject()
     */
    Object sendRequest(RpcRequest request, String host, int port){
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用时有错误发生：", e);
            return null;
        }
    }
}
