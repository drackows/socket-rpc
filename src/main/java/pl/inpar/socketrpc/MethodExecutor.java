package pl.inpar.socketrpc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodExecutor implements Runnable {
	public static final Logger logger = LoggerFactory.getLogger(MethodExecutor.class);
	
	private final SocketRpcServer server;
	private final Socket socket;

	public MethodExecutor(SocketRpcServer server, Socket socket) {
		this.server = server;
		this.socket = socket;
	}

	@Override
    public void run() {
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		try {
			input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
	        
	        Object readed = input.readObject();
	        if (readed instanceof RequestMethod) {
	        	RequestMethod method = (RequestMethod) readed;
	        	
	        	Object service = server.getService(method.getClazz());
	        	Object result = method.invoke(service);
	        	
				output = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	        	output.writeObject(result);
				output.flush();
	        	
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
