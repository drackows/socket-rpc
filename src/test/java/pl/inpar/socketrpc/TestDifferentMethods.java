package pl.inpar.socketrpc;

import pl.inpar.socketrpc.mocks.SampleService;
import pl.inpar.socketrpc.mocks.SampleServiceImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDifferentMethods {

	public static int PORT;
	private static SocketRpcServer socketRpcServer;
	private static SocketRpcClient socketRpcClient;
	
	final int threads = 50;
	final int executeInOneThread = 20;
	
	@BeforeClass
	public static void prepareServersAndClients() throws IOException {
		int PORT = 10000 + new Random().nextInt(10000);
		socketRpcServer = new SocketRpcServer(PORT);
		socketRpcServer.register(SampleService.class, new SampleServiceImpl());
		
		socketRpcClient = new SocketRpcClient("localhost", PORT);
	}
	
	@AfterClass
	public static void closeServer() throws IOException {
		socketRpcServer.close();
	}
	
	@Test
	public void testSimpleInt() {
		SampleService service = socketRpcClient.getRemoteService(SampleService.class);
		
		service.intAsArg(1234);
		
	}
	
	@Test
	public void testGenericArg() {
		SampleService service = socketRpcClient.getRemoteService(SampleService.class);
		
		System.out.println("got: "+service.stringify(Arrays.asList(123, 456, 789)));
		
	}
	
}
