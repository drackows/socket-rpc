package pl.inpar.socketrpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketRpcServer {
	public static final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);
	
	private boolean running = false;
	private final ExecutorService serverThread = Executors.newSingleThreadExecutor();
	private final ExecutorService methodConcurrentThreads;
	private final Map<Class<?>, Object> registeredServices = new ConcurrentHashMap<Class<?>, Object>();
	
	private ServerSocket serverSocket;
	
	public SocketRpcServer(int port) throws IOException {
		this(port, 10);
	}
	
	public SocketRpcServer(int port, int threadPoolSize) throws IOException {
		logger.info("Server starting");
		methodConcurrentThreads = Executors.newFixedThreadPool(threadPoolSize);
		serverSocket = new ServerSocket(port);
		serverThread.execute(mainRunnable());
		logger.info("Listening for methods");
	}
	
	public void close() throws IOException {
		logger.info("Server is going to shutdown");
		running = false;
		List<Runnable> notExecuted = methodConcurrentThreads.shutdownNow();
		if (notExecuted.size()>0) {
			for (Runnable r : notExecuted) {
				logger.error("Not executed method: "+r.toString());
			}
		}
		serverSocket.close();
		logger.info("Server closed");
	}
	
	private Runnable mainRunnable() {
	    return new Runnable() {
			public void run() {
				running = true;
				while (running) {
					Socket socket = null;
					try {
						socket = serverSocket.accept();
						logger.debug("Accepted client socket.getInetAddress():"+socket.getInetAddress());
						MethodExecutor callMethod = new MethodExecutor(SocketRpcServer.this, socket);
						methodConcurrentThreads.execute(callMethod);
					} catch (IOException e) {
						if (running) { 
							logger.error("Could not accept client", e);
						} else {
							logger.info("Server stopped while waiting for connection.");
						}
					}
				}
			}
		};
    }
	
	@SuppressWarnings("unchecked")
    public <T extends C, C> T getService(Class<C> clazz) {
		Object object = registeredServices.get(clazz);
		if (object!=null) {
			return (T) object;
		}
		return null;
	}
	
	public <T extends C, C> void register(Class<C> clazz, T serviceImpl) {
		registeredServices.put(clazz, serviceImpl);
		logger.info("Registered "+serviceImpl.getClass()+" as implements of: " + clazz);
	}

}
