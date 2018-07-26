package com.kazurayam.ksbackyard

import org.apache.http.HttpHost
import org.apache.http.StatusLine
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration


public class MyCustomKeywords {

	static String version_ = '0.1'
	static KeywordLogger logger = new KeywordLogger()

	@Keyword
	def static getVersion() {
		return version_
	}

	@Keyword
	def static verifyUrlAccessibility(String urlString) {
		CloseableHttpClient httpclient = HttpClients.createDefault()
		HttpGet httpGet = new HttpGet(urlString)
		if (amIBehindProxy()) {
			httpGet.setConfig(createProxyConfig())
		}
		CloseableHttpResponse response1 = httpclient.execute(httpGet)
		boolean result = makeJudgement(response1)
		if (!result) {
			logger.logFailed("Unable to get access to ${urlString}")
		}
		return result
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	private static boolean makeJudgement(CloseableHttpResponse response) {
		int statusCode = 0
		try {
			StatusLine statusLine = response.getStatusLine()
			statusCode = statusLine.getStatusCode()
		} finally {
			response.close()
		}
		return (statusCode == 200) ? true : false
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
