package pl.inpar.socketrpc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import pl.inpar.socketrpc.mocks.MockService;
import pl.inpar.socketrpc.mocks.MockServiceImpl;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleCallTest {
	
	public static int PORT;
	private static SocketRpcServer socketRpcServer;
	private static MockService service;
	private static SocketRpcClient socketRpcClient;
	
	final int threads = 50;
	final int executeInOneThread = 20;
	
	@BeforeClass
	public static void prepareServersAndClients() throws IOException {
		int PORT = 10000 + new Random().nextInt(10000);
		socketRpcServer = new SocketRpcServer(PORT);
		socketRpcServer.register(MockService.class, new MockServiceImpl());
		
		socketRpcClient = new SocketRpcClient("localhost", PORT);
	}
	
	@AfterClass
	public static void closeServer() throws IOException {
		socketRpcServer.close();
	}
	
	@Test
	public void testCallOverSocket() throws IOException, InterruptedException {
		System.out.println("OverSocket");
		service = socketRpcClient.getService(MockService.class);
		callManyTimes(threads, executeInOneThread);
	}
	
	@Test
	public void testCallLocalMock() throws InterruptedException {
		System.out.println("Local");
		service = new MockServiceImpl();
		callManyTimes(threads, executeInOneThread);
	}

	private void callManyTimes(final int threads, final int executeInOneThread) throws InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		long startTime = System.currentTimeMillis();
		for (int i=0; i<threads; i++) {
    		executor.execute(new Runnable() {
    			@Override
    			public void run() {
    				for (int i=0; i<executeInOneThread; i++) {
    		    		service.doStuff();
    		    		int value = service.getValue();
    		    		assertThat(value, is(greaterThan(0)));
    				}
    			}
    		});
		}
		executor.shutdown();
		System.out.println("awaitTermination="+executor.awaitTermination(60, TimeUnit.SECONDS));
		System.out.println("took [ms]: " + (System.currentTimeMillis()-startTime));
		assertThat(service.getValue(), is(threads*executeInOneThread));

    }
	
	
	
	
}
