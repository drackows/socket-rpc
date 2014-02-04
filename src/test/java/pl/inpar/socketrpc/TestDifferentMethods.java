package pl.inpar.socketrpc;

import pl.inpar.socketrpc.mocks.MockService;
import pl.inpar.socketrpc.mocks.MockServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
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
		socketRpcServer.register(MockService.class, new MockServiceImpl());
		
		socketRpcClient = new SocketRpcClient("localhost", PORT);
	}
	
	@AfterClass
	public static void closeServer() throws IOException {
		socketRpcServer.close();
	}
	
	@Test
	public void testSimpleInt() {
		MockService service = socketRpcClient.getService(MockService.class);
		
		service.intAsArg(1234);
		
	}
	
	@Test
	public void testGenericArg() {
		MockService service = socketRpcClient.getService(MockService.class);
		
		System.out.println("got: "+service.stringify(Arrays.asList(123, 456, 789)));
		
	}
	
}
