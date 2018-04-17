package com.kazurayam.ksbackyard

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

import internal.GlobalVariable

import MobileBuiltInKeywords as Mobile
import WSBuiltInKeywords as WS
import WebUiBuiltInKeywords as WebUI

//
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.util.KeywordUtil

public class MyCustomKeywords {

    @Keyword
    def verifyUrlAccessibility(String urlString) {
        CloseableHttpClient httpclient = HttpClients.createDefault()
        HttpGet httpGet = new HttpGet(urlString)
        if (amIBehindProxy()) {
            httpGet.setConfig(createProxyConfig())
        }
        CloseableHttpResponse response1 = httpclient.execute(httpGet)
        return makeMyJudgement(urlString, response1)
    }
    
    /**
     * 
     * @param response
     * @return
     */
    private boolean makeMyJudgement(String urlString, CloseableHttpResponse response) {
        int statusCode = 0
        try {
            StatusLine statusLine = response.getStatusLine()
            statusCode = statusLine.getStatusCode()
        } finally {
            response.close()
        }
        if (statusCode == 200) {
            KeywordUtil.markPassed("${urlString} is accessible")
            return true
        } else {
            KeywordUtil.markFailed("Unable to get access to ${urlString}")
            return false
        }
    }
    
    private boolean amIBehindProxy() {
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
    
    private RequestConfig createProxyConfig() {
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
