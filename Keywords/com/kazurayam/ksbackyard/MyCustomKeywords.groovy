package com.kazurayam.ksbackyard

import org.apache.http.Header
import org.apache.http.HttpHost
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpHead
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration


public class MyCustomKeywords {

	static String version_ = '0.2'
	static KeywordLogger logger = new KeywordLogger()

	@Keyword
	def static getVersion() {
		return version_
	}

	@Keyword
	static int getHttpResponseStatus(String url) {
		HttpResponse res = doHead(url)
		StatusLine statusLine = res.getStatusLine()
		int statusCode = statusLine.getStatusCode()
		return statusCode
	}

	@Keyword
	static String getContentType(String url) {
		HttpResponse res = doHead(url)
		Header[] headers = res.getAllHeaders()
		for (Header header : headers) {
			if (header.getName() == 'Content-Type') {
				return header.getValue()
			}
		}
		return null
	}

	/**
	 * make a HTTP GET Request to get a Response, close the response and return it to the caller
	 * 
	 * @param urlString
	 * @return
	 */
	@Keyword
	static HttpResponse doHead(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault()
		HttpHead command = new HttpHead(url)
		if (amIBehindProxy()) {
			command.setConfig(createProxyConfig())
		}
		CloseableHttpResponse res = null
		try {
			res = httpclient.execute(command)
		} finally {
			if (res != null) {
				res.close()
			}
		}
		return res
	}

	// check if I am behind Proxy or not
	private static boolean amIBehindProxy() {
		def pi = RunConfiguration.getProxyInformation()
		if (pi.proxyOption == 'MANUAL_CONFIG' &&
		pi.proxyServerType == 'HTTP' &&
		pi.proxyServerAddress.length() > 0 &&
		pi.proxyServerPort > 0) {
			return true
		} else {
			return false
		}
	}

	private static RequestConfig createProxyConfig() {
		def pi = RunConfiguration.getProxyInformation()
		if (pi.proxyOption == 'MANUAL_CONFIG' &&
		pi.proxyServerType == 'HTTP' &&
		pi.proxyServerAddress.length() > 0 &&
		pi.proxyServerPort > 0) {
			HttpHost proxy = new HttpHost(pi.proxyServerAddress,
					pi.proxyServerPort, 'http')
			RequestConfig config = RequestConfig.custom()
					.setProxy(proxy)
					.build()
			return config
		} else {
			return null
		}

	}
}
