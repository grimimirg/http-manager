package it.httpmanager.example;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.httpmanager.example.PropertiesService;

@Service
public class HttpManager {

	@Autowired
	private PropertiesService propertiesService;

	public static enum HttpMethods {
		GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
	}

	private String url = null;

	private String proxyUrl = null;

	private Integer proxySocket = null;

	private String proxyProtocol = null;

	private List<NameValuePair> parameters = null;

	private List<Header> headers = null;

	private String encoding = null;

	private String entity = null;

	// --------------------------------------------

	 
	public HttpManager init() {

		this.parameters = new ArrayList<>();
		this.headers = new ArrayList<>();

		return this;
	}

	public HttpManager init(Boolean useProxy) {

		this.init();

		if (useProxy) {
			this.proxyUrl = propertiesService.getProxyUrl();
			this.proxySocket = propertiesService.getProxySocket();
			this.proxyProtocol = propertiesService.getProxyProtocol();
		}

		return this;

	}

	public HttpManager init(String url) {

		this.url = url;

		this.init();

		return this;
	}

	public HttpManager init(String url, Boolean useProxy) {

		this.init(url);

		if (useProxy) {
			this.proxyUrl = propertiesService.getProxyUrl();
			this.proxySocket = propertiesService.getProxySocket();
			this.proxyProtocol = propertiesService.getProxyProtocol();
		}

		return this;
	}

	public HttpManager init(String url, String proxyUrl, Integer proxySocket, String proxyProtocol) {

		this.init(url);

		this.proxyUrl = proxyUrl;
		this.proxySocket = proxySocket;
		this.proxyProtocol = proxyProtocol;

		return this;
	}

	// --------------------------------------------

	public String getProxyUrl() {
		return this.proxyUrl;
	}

	public Integer getProxySocket() {
		return this.proxySocket;
	}

	public String getProxyProtocol() {
		return this.proxyProtocol;
	}

	public List<NameValuePair> getParameters() {
		return this.parameters;
	}

	public List<Header> getHeaders() {
		return this.headers;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public String getUrl() {
		return this.url;
	}

	public String getEntity() {
		return this.entity;
	}

	// -----------------------------------------

	 
	public HttpManager setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
		return this;
	}

	public HttpManager setProxySocket(Integer proxySocket) {
		this.proxySocket = proxySocket;
		return this;
	}

	public HttpManager setProxyProtocol(String proxyProtocol) {
		this.proxyProtocol = proxyProtocol;
		return this;
	}

	public HttpManager setParameters(List<NameValuePair> parameters) {
		this.parameters = parameters;
		return this;
	}

	public HttpManager setHeaders(List<Header> headers) {
		this.headers = headers;
		return this;
	}

	public HttpManager addParameter(NameValuePair param) {
		this.parameters.add(param);
		return this;
	}

	public HttpManager addHeader(Header header) {
		this.headers.add(header);
		return this;
	}

	public HttpManager setEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public HttpManager setUrl(String url) {
		this.url = url;
		return this;
	}

	public HttpManager setEntity(String entity) {
		this.entity = entity;
		return this;
	}

	// ------------------ NO PROXY -----------------

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod) throws Exception {

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url) throws Exception {

		this.url = url;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, List<NameValuePair> parameters)
			throws Exception {

		this.url = url;
		this.parameters = parameters;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, List<NameValuePair> parameters,
			List<Header> headers) throws Exception {

		this.url = url;
		this.parameters = parameters;
		this.headers = headers;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, String entity) throws Exception {

		this.url = url;
		this.entity = entity;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, String entity, List<Header> headers)
			throws Exception {

		this.url = url;
		this.entity = entity;
		this.headers = headers;

		return this.doHttpCall(httpMethod);

	}

	// ----------------- PROXY --------------
	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String proxyUrl, Integer proxySocket,
			String proxyProtocol) throws Exception {

		this.proxyUrl = proxyUrl;
		this.proxySocket = proxySocket;
		this.proxyProtocol = proxyProtocol;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, String proxyUrl, Integer proxySocket,
			String proxyProtocol) throws Exception {

		this.url = url;

		this.proxyUrl = proxyUrl;
		this.proxySocket = proxySocket;
		this.proxyProtocol = proxyProtocol;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, List<NameValuePair> parameters,
			String proxyUrl, Integer proxySocket, String proxyProtocol) throws Exception {

		this.url = url;
		this.parameters = parameters;

		this.proxyUrl = proxyUrl;
		this.proxySocket = proxySocket;
		this.proxyProtocol = proxyProtocol;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, List<NameValuePair> parameters,
			List<Header> headers, String proxyUrl, Integer proxySocket, String proxyProtocol) throws Exception {

		this.url = url;
		this.parameters = parameters;
		this.headers = headers;

		this.proxyUrl = proxyUrl;
		this.proxySocket = proxySocket;
		this.proxyProtocol = proxyProtocol;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, String entity, String proxyUrl,
			Integer proxySocket, String proxyProtocol) throws Exception {

		this.url = url;
		this.entity = entity;

		this.proxyUrl = proxyUrl;
		this.proxySocket = proxySocket;
		this.proxyProtocol = proxyProtocol;

		return this.doHttpCall(httpMethod);

	}

	public SimpleHttpResponseEntity httpCall(HttpMethods httpMethod, String url, String entity, List<Header> headers,
			String proxyUrl, Integer proxySocket, String proxyProtocol) throws Exception {

		this.url = url;
		this.entity = entity;
		this.headers = headers;

		this.proxyUrl = proxyUrl;
		this.proxySocket = proxySocket;
		this.proxyProtocol = proxyProtocol;

		return this.doHttpCall(httpMethod);

	}

	// ---------------------------------------

	/**
	 * Builds URS
	 * 
	 * @param url
	 * @param parameters
	 * @return
	 * @throws URISyntaxException
	 */

	private HttpUriRequest buildHttpGetUrl(String url, List<NameValuePair> parameters) throws URISyntaxException {
		URI urlTmp = new URI(url); // variabile di appoggio
		return new HttpGet(new URIBuilder().setScheme(urlTmp.getScheme()).setHost(urlTmp.getHost())
				.setPath(urlTmp.getPath()).setParameters(parameters).build());
	}

	/**
	 * Do the HTTP call
	 * 
	 * @param httpMethod
	 * @return
	 * @throws Exception
	 */

	 
	private SimpleHttpResponseEntity doHttpCall(HttpMethods httpMethod) throws Exception {

		//accepts every certificates (to change)
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}

		} };

		SSLContext sc = null;

		try {
			sc = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException e) {
			Logger.getLogger(getClass()).error(e);
		}
		try {
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			Logger.getLogger(getClass()).error(e);
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		//supports TLS versions
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sc, new String[] { "TLSv1.1", "TLSv1.2" },
				null, allHostsValid);

		HttpClientBuilder httpCLientBuilder = HttpClients.custom().setSSLSocketFactory(sslsf)
				.setConnectionManager(
						new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create()
								.register("http", PlainConnectionSocketFactory.getSocketFactory())
								.register("https", sslsf).build()))
				.setConnectionManagerShared(true);

		if (!StringUtil.isvoid(this.proxyUrl)) {
			httpCLientBuilder.setProxy(new HttpHost(this.proxyUrl, this.proxySocket, this.proxyProtocol));
		}

		CloseableHttpClient httpClient = httpCLientBuilder.build();

		try {

			HttpUriRequest httpUriRequest = null;

			switch (httpMethod) {
			case GET:
				httpUriRequest = this.parameters != null && this.parameters.size() > 0
						? buildHttpGetUrl(this.url, this.parameters) : new HttpGet(this.url);
				break;
			case POST:
				httpUriRequest = new HttpPost(this.url);
				break;
			case PUT:
				httpUriRequest = new HttpPut(this.url);
				break;
			case DELETE:
				httpUriRequest = new HttpDelete(this.url);
				break;
			default:
				break;
			}

			// presenza degli headers
			if (this.headers != null && this.headers.size() > 0) {
				for (Header header : this.headers) {
					httpUriRequest.setHeader(header);
				}
				// httpUriRequest.setHeaders((Header[]) this.headers.toArray());
			}

			// only for POST requests
			if (httpMethod == HttpMethods.POST || httpMethod == HttpMethods.PUT) {
				Class<? extends HttpUriRequest> httpClass = httpUriRequest.getClass();
				for (Method method : httpClass.getMethods()) {
					// set the entity payload
					if (method.getName().equals("setEntity")) {
						if (this.parameters != null && this.parameters.size() > 0) {
							// form-urlencoded
							method.invoke(httpUriRequest, new UrlEncodedFormEntity(this.parameters,
									this.encoding != null ? this.encoding : StandardCharsets.UTF_8.name()));
						} else if (this.entity != null && !this.entity.equals("")) {
							// RAW
							method.invoke(httpUriRequest, new StringEntity(this.entity,
									this.encoding != null ? this.encoding : StandardCharsets.UTF_8.name()));
						}
						break;
					}
				}
			}

			ResponseHandler<SimpleHttpResponseEntity> responseHandler = new ResponseHandler<SimpleHttpResponseEntity>() {
				@Override
				public SimpleHttpResponseEntity handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					String body = (entity != null) ? EntityUtils.toString(entity) : null;
					return new SimpleHttpResponseEntity(response.getStatusLine().getStatusCode(), body);
				}
			};

			return httpClient.execute(httpUriRequest, responseHandler);
		} finally {
			httpClient.close();
		}
	}
}
