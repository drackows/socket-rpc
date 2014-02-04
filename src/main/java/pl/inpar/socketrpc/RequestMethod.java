package pl.inpar.socketrpc;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMethod implements Serializable{
	public static final Logger logger = LoggerFactory.getLogger(RequestMethod.class);
	private static final long serialVersionUID = 1L;

	private final Class<?> clazz;
	private final String name;
	private final Class<?>[] types;
	private final Object[] args;

	public RequestMethod(Class<?> clazz, String name, Class<?>[] types, Object[] args2) {
		this.clazz = clazz;
		this.name = name;
		this.types = types;
		this.args = args2;
	}

	public RequestMethod(Class<?> clazz, String name, Object[] args) {
		this.clazz = clazz;
		this.name = name;
		this.types = new Class<?>[0];
		this.args = args;
	}
	
    public Object invoke(Object obj) {
		if (!getClazz().isAssignableFrom(obj.getClass())) {
			throw new RuntimeException();
		}
		try {
	        Method method = obj.getClass().getMethod(getName(), getTypes());
	        return method.invoke(obj, (Object[])getArgs());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	        e.printStackTrace();
        }
		return obj;
		
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getName() {
		return name;
	}

	public Class<?>[] getTypes() {
		return types;
	}

	public Object[] getArgs() {
		return args;
	}

}
