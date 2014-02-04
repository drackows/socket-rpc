package pl.inpar.socketrpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodCaller implements Runnable {
	public static final Logger logger = LoggerFactory.getLogger(MethodCaller.class);
	
	private SocketRpcServer server;
	private Socket socket;

	public MethodCaller(SocketRpcServer server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	@Override
    public void run() {
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		try {
	        input = new ObjectInputStream(socket.getInputStream());
	        
	        Object readed = input.readObject();
	        if (readed instanceof RequestMethod) {
	        	RequestMethod method = (RequestMethod) readed;
	        	
	        	Object service = server.getService(method.getClazz());
	        	Object result = method.invoke(service);
	        	
	        	output = new ObjectOutputStream(socket.getOutputStream());
	        	output.writeObject(result);
	        	
	        }
        } catch (IOException | ClassNotFoundException e) {
        	logger.error("error while trying to retrieve, execute or returning result", e);
        } finally {
        	try {
	            input.close();
            } catch (IOException e) {
            	logger.error("error while closing input", e);
            }
        	try {
	            output.close();
            } catch (IOException e) {
            	logger.error("error while closing output", e);
            }
        }
    }

}
