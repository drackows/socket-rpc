package pl.inpar.socketrpc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketRpcClient {
	public static final Logger logger = LoggerFactory.getLogger(SocketRpcClient.class);
	
	private final String host;
	private final int port;

	public SocketRpcClient(String host, int port) {
		this.host = host;
		this.port = port;
    }

	@SuppressWarnings("unchecked")
    public <T> T getRemoteService(Class<T> clazz) {
		return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new InvocationHandler(){
			@Override
	        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				RequestMethod request = new RequestMethod(method.getDeclaringClass(), method.getName(), method.getParameterTypes(), args);
				Socket socket = null;
				ObjectOutputStream output = null;
				ObjectInputStream input = null;
				Object result = null;
				try {
	    			socket = new Socket(host, port);
	    			
					output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	    			output.writeObject(request);
					output.flush();
					input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
	    			result = input.readObject();
				} finally {
					input.close();
					output.close();
					socket.close();
				}
		        return result;
	        }
		});
    }
	
}
