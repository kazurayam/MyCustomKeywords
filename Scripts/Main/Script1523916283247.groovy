import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

def keywordVersion = CustomKeywords.'com.kazurayam.ksbackyard.MyCustomKeywords.getVersion'()
WebUI.comment("The version of MyCustomKeywords is ${keywordVersion}")

// open a browser and navigate to the Google Search page
WebUI.openBrowser('https://www.google.co.jp/')
WebUI.verifyElementPresent(findTestObject('Page_Google/img_hplogo'), 10)

//def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src')
def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src') + "_!!!!!!!!!!!!"

// check if the Google Logo image is displayed
def result = CustomKeywords.'com.kazurayam.ksbackyard.MyCustomKeywords.verifyUrlAccessibility'(imgSrc)

if (result) {
    WebUI.comment(">>> <img href=\"${imgSrc}\"> has been verified accessible")
} else {
    KeywordUtil.markFailed(">>> Unable to get access to ${imgSrc}")
}

WebUI.closeBrowser()



