package il.technion.cs236369.webserver;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.http.HttpResponse;

public class Session implements ISession {
	private SessionManager manager;
	public SessionManager getManager() {
		return manager;
	}


	String ID;
	public String getID() {
		return ID;
	}

	private HashMap<String, Object> nameToValMap = new HashMap<String, Object>();
	private boolean enabled = true;
	private int timeout;
	private long expiration;

	public Session(int timeout, SessionManager manager, String ID) {
		this.manager = manager;
		this.ID = ID;
		this.timeout = timeout;
		long currTimeMillis = System.currentTimeMillis();
		this.expiration = currTimeMillis + timeout;
	}
	
	public synchronized Date getExpirationDate() {
		Date d = new Date(expiration);
		return d;
	}
	
	public synchronized boolean isExpired() {
		long currTimeMillis = System.currentTimeMillis();
		if (currTimeMillis > expiration) {
			return true;
		}
		return false;
	}
	
	@Override
	public synchronized void set(String name, Object val) {
		if (enabled) {
			
				nameToValMap.put(name, val);

			
		}
	}

	@Override
	public synchronized Object get(String name) {
		if (enabled) {
			return nameToValMap.get(name);
		}
		return null;
	}

	@Override
	public synchronized void invalidate() {
		nameToValMap.clear();
		enabled = false;
		manager.invalidate(ID);
	}
}
