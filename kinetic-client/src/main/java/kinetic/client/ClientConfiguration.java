/**
 * Copyright (C) 2014 Seagate Technology.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package kinetic.client;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Kinetic Client configuration.
 * <p>
 * Kinetic applications construct a new instance of this instance and set
 * appropriate configurations. Application then calls
 * {@link KineticClientFactory#createInstance(ClientConfiguration)} to create a
 * new instance of {@link KineticClient}
 * 
 * @author James Hughes.
 * @author Chiaming Yang
 * 
 * @see KineticClientFactory#createInstance(ClientConfiguration)
 * @see KineticClient
 */
public class ClientConfiguration extends Properties {

	private final static Logger logger = Logger
			.getLogger(ClientConfiguration.class.getName());

	private static final long serialVersionUID = 7330657102192607375L;

	// default request timeout is set to 30000 milli seconds
	private static final long DEFAULT_REQUEST_TIMEOUT = 30000L;
	
	 /**
     * current supported kinetic protocol version on kinetic-protocol repo.
     */
    public static final String PROTOCOL_VERSION = "2.0.2";
    
    /**
     * current supported protocol source commit hash on kinetic-protocol repo.
     */
    public static final String PROTOCOL_SOURCE_HASH = "f6e21e281272b46c620284781cdb3a36a6c7a564";

	// kinetic server host
	private String host = "localhost";

	// kinetic server port
	private int port = 8123;

	// user id
	private long userId = 1;

	// key
	private String key = "asdfasdf";

	// cluster version
	private long clusterVersion = 0;

	/**
	 * use nio flag
	 */
	private volatile boolean useNio = true;

	/**
	 * Nio service threads number in service thread pool.
	 */
	private int nThreads = 0;

	/**
	 * flag to use ssl. if the system property is set, ssl is used.
	 */
	private volatile boolean useSsl = Boolean.getBoolean("kinetic.io.ssl");

	
	/**
	 * ssl default port if useSsl is set to true.
	 */
	private final static int SSL_DEFAULT_PORT = 8443;

	// socket time out
	private int timeoutMillis = 0;

	/**
	 * request timeout in milli seconds. For synchronous request, each request
	 * will return immediately when received a response or up to the timeout set
	 * in this configuration instance.
	 * <p>
	 * default is set to 60000 milli seconds
	 */
	private long requestTimeout = 60000;

	/**
	 * client nio thread pool exit await timeout in milli secs.
	 */
	private long threadPoolAwaitTimeout = 50;

	/**
	 * Asynchronous put queued size.
	 */
	private int asyncQueueSize = 10;

	/**
	 * Client configuration constructor.
	 * 
	 * @param props
	 *            default initialized properties for the kinetic client.
	 */
	public ClientConfiguration(Properties props) {
		super(props);
	}

	/**
	 * Client configuration constructor.
	 * <p>
	 * System properties are used as default configuration properties.
	 */
	public ClientConfiguration() {
		super(System.getProperties());
	}

	/**
	 * Set kinetic server host name.
	 * 
	 * @param host
	 *            kinetic server host name.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Set server port.
	 * 
	 * @param port
	 *            server port.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Get server host name.
	 * 
	 * @return server host name.
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Get server port number.
	 * 
	 * @return server port number.
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Get user id.
	 * 
	 * @return user id.
	 */
	public long getUserId() {
		return this.userId;
	}

	/**
	 * set user/client id.
	 * 
	 * @param userId
	 *            user/client id
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * set user key.
	 * 
	 * @param key
	 *            user key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get user key.
	 * 
	 * @return user key.
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * set cluster version.
	 * 
	 * @param clusterVersion
	 *            cluster version
	 */
	public void setClusterVersion(long clusterVersion) {
		this.clusterVersion = clusterVersion;
	}

	/**
	 * Get cluster version
	 * 
	 * @return cluster version
	 */
	public long getClusterVersion() {
		return clusterVersion;
	}

	/**
	 * Get connection Id.
	 * 
	 * @return connection id.
	 */
	public long getConnectionId() {

		// connection id
		long connectionID = Long.parseLong(getProperty("kinetic.connectionId",
				"-1"));

		// init if not set
		if (connectionID == -1) {
			connectionID = System.nanoTime();
		}

		return connectionID;
	}

	/**
	 * Set use Java NIO or not.
	 * 
	 * @param flag
	 *            set to true if use nio. Otherwise, set to false. The default
	 *            is set to true if not set.
	 * 
	 */
	public void setUseNio(boolean flag) {
		this.useNio = flag;
	}

	/**
	 * Get if the server uses Java NIO.
	 * 
	 * @return true if the server uses Java NIO. Otherwise, return false.
	 */
	public boolean getUseNio() {
		return this.useNio;
	}

	/**
	 * set if SSL/TLS socket should be used.
	 * 
	 * @param flag
	 *            set to true to use SSL. Default is set to false.
	 * 
	 */
	public void setUseSsl(boolean flag) {
		this.useSsl = flag;
	}

	/**
	 * Get if SSL/TLS transport is used.
	 * 
	 * @return true if SSL/TLS transport is used. Otherwise, return false.
	 */
	public boolean getUseSsl() {
		return this.useSsl;
	}

	/**
	 * Get SSL/TLS transport default port for the kinetic instance if it is not
	 * set by application.
	 * 
	 * @return SSL/TLS transport default port for kinetic instance if it is not
	 *         set by application.
	 */
	public int getSSLDefaultPort() {
		return SSL_DEFAULT_PORT;
	}

	/**
	 * Set socket timeout in milli seconds.
	 * 
	 * @param millis
	 *            socket timeout in milli seconds.
	 */
	public void setConnectTimeoutMillis(int millis) {
		this.timeoutMillis = millis;
	}

	/**
	 * Get socket timeout used in the current client instance.
	 * 
	 * @return socket timeout used in the current client instance.
	 */
	public int getConnectTimeoutMillis() {
		return this.timeoutMillis;
	}

	/**
	 * Set request timeout (in milli seconds) for the client instance.
	 * <p>
	 * The default request timeout is set to 30000 milli seconds if not set. The
	 * default value is used if the specified timeout is equal or less than 0.
	 * <p>
	 * Setting a small request timeout could introduce unexpected side effects.
	 * Such as timed out before a response is received.
	 * <p>
	 * Applications may set a large timeout value when desired, such as for long
	 * running request-response operations.
	 * 
	 * @param millis
	 *            request timeout (in milli seconds) for the current client
	 *            instance
	 * 
	 * @see #setRequestTimeoutMillis(long)
	 */
	public void setRequestTimeoutMillis(long millis) {

		if (millis <= 0) {
			logger.warning("Specified timeout value "
					+ millis
					+ " is not supported. Using default request timeout (30 seconds)");
			millis = DEFAULT_REQUEST_TIMEOUT;
		} else if (millis < DEFAULT_REQUEST_TIMEOUT) {
			logger.warning("request timeout set to " + millis
					+ " milli seconds.  This may cause the client runtime library not to receive responses in time when network/service is slow.");
		}

		this.requestTimeout = millis;

		logger.info("request timeout is set to " + millis + " (milli seconds)");
	}

	/**
	 * Get the request timeout (in milli seconds) used for the client instance.
	 * 
	 * @return the request timeout (in milli seconds) used for the client
	 *         instance.
	 * 
	 * @see #setRequestTimeoutMillis(long)
	 */
	public long getRequestTimeoutMillis() {
		return this.requestTimeout;
	}

	/**
	 * Set asynchronous operation queued size. For asynchronous operations, a
	 * request is blocked when the queued requests reached the count set for
	 * this instance. Default is set to 10.
	 * 
	 * @param qsize
	 *            the max queued operations to be set for the current kinetic
	 *            client instance.
	 */
	public void setAsyncQueueSize(int qsize) {
		this.asyncQueueSize = qsize;
	}

	/**
	 * Get asynchronous operation queued size. For asynchronous operations, a
	 * request is blocked when the queued requests reached the size set for this
	 * instance. Default is set to 10.
	 * 
	 * @return the max queued operations for the current kinetic instance
	 */
	public int getAsyncQueueSize() {
		return this.asyncQueueSize;
	}

	/**
	 * Get Java Nio thread pool exit await timeout - used when connection is
	 * closed and Java client runtime library waiting for thread pool to exit.
	 * 
	 * @return time out in milli seconds.
	 */
	public long getThreadPoolAwaitTimeout() {
		return this.threadPoolAwaitTimeout;
	}

	/**
	 * Set Java Nio thread pool exit await timeout - used when connection is
	 * closed and Java client runtime library waiting for thread pool to exit.
	 * 
	 * @param millis
	 *            timeout in milli seconds
	 */
	public void setThreadPoolAwaitTimeOut(long millis) {
		this.threadPoolAwaitTimeout = millis;
	}

	/**
	 * Set number of thread used in kinetic client nio services.
	 * 
	 * @param nThreads
	 *            number of thread used in kinetic client nio services.
	 */
	public void setNioServiceThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	/**
	 * Get number of thread used in kinetic client nio services.
	 * 
	 * @return number of thread used in kinetic client nio services
	 */
	public int getNioServiceThreads() {
		return this.nThreads;
	}

	/**
     * Get Kinetic protocol version supported by the current API implementation.  
     * The protocol version is defined at the kinetic-protocol repository.
     *
     * @return Kinetic protocol version supported by the current API implementation. 
     * 
     * @see <a href="https://github.com/Seagate/kinetic-protocol">kinetic-protocol</a>
     */
    public static String getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    /**
     * Get the supported protocol source commit hash at the kinetic-protocol repository.
     * 
     * @return protocol source commit hash value at the kinetic-protocol repository.
     * 
     * @see <a href="https://github.com/Seagate/kinetic-protocol">kinetic-protocol</a>
     */
    public static String getProtocolSourceHash() {
        return PROTOCOL_SOURCE_HASH;
    }

}
