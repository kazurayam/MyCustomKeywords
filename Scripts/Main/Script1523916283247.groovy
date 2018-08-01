import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.apache.http.Header
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// open a browser and navigate to the Google Search page
WebUI.openBrowser('https://www.google.co.jp/')
WebUI.verifyElementPresent(findTestObject('Page_Google/img_hplogo'), 10)

// resolve SRC attribute of <img src=".."> in the Google Search page 
def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src')
//def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src') + "_!!!!!!!!!!!!"



// check if the URL responded with HTTP Status 200 OK
def status = CustomKeywords.'com.kazurayam.ksbackyard.MyCustomKeywords.getHttpResponseStatus'(imgSrc)
if (status != 200) {
    KeywordUtil.markFailed(">>> ${imgSrc} respondes HTTP Status ${status}")
}

// check if the URL responded with Content-Type: image/png 
def contentType = CustomKeywords.'com.kazurayam.ksbackyard.MyCustomKeywords.getContentType'(imgSrc)
if (contentType != 'image/png') {
	KeywordUtil.markFailed(">>> expected Content-Type: image/png but was ${contentType}")
}

WebUI.closeBrowser()



