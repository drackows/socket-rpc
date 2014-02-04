package pl.inpar.socketrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class SocketRpcClient {
	public static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
	
	private String host;
	private int port;

	public SocketRpcClient(String host, int port) {
		this.host = host;
		this.port = port;
    }

	@SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
		return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new Handler());
    }
	
	class Handler implements InvocationHandler {
		
		@Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			RequestMethod request = new RequestMethod(method.getDeclaringClass(), method.getName(), method.getParameterTypes(), (Serializable[]) args);
			Socket socket = null;
			ObjectOutputStream output = null;
			ObjectInputStream input = null;
			Object result = null;
			try {
    			socket = new Socket(host, port);
    			
    			output = new ObjectOutputStream(socket.getOutputStream());
    			output.writeObject(request);

    			input = new ObjectInputStream(socket.getInputStream());
    			result = input.readObject();

			} finally {
				input.close();
				output.close();
				socket.close();
			}
	        return result;
        }
		
	}
	
}
