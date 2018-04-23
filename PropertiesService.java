package it.httpmanager.example;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {

	@Value("${proxyUrl}")
	private String proxyUrl;

	@Value("${proxySocket}")
	private String proxySocket;

	@Value("${proxyProtocol}")
	private String proxyProtocol;

	@Value("${useProxy}")
	private String useProxy;

	public String getProxyUrl() {
		return proxyUrl;
	}
	 
	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}
	 
	public Integer getProxySocket() {
		return Integer.parseInt(proxySocket);
	}
	 
	public void setProxySocket(String proxySocket) {
		this.proxySocket = proxySocket;
	}
	 
	public String getProxyProtocol() {
		return proxyProtocol;
	}
	 
	public void setProxyProtocol(String proxyProtocol) {
		this.proxyProtocol = proxyProtocol;
	}
	 
	public Boolean getUseProxy() {
		return Boolean.valueOf(useProxy);
	}
	 
	public void setUseProxy(String useProxy) {
		this.useProxy = useProxy;
	}

}
