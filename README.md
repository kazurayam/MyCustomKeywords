A Katalon Studio project which verifies accessibility of URL refered in HTML
====

# What is this?

This is a simple [Katalon Studio](https://www.katalon.com/) project for demonstration purpose. You can check this out onto your PC and execute with your Katalon Studio.

This project proposes a Custom Keyword `verifyUrlAccessibility(String url)`. The keyword requires a String argument as URL. The URL could be, for example, src attribute of &lt;img src="...."&gt; tag or href attribute of &lt;a href="..."&gt; tag within a HTML. The keyword internally execute HTTP GET request and check the response. If the HTTP Status code is 200 OK, then the keyword returns true. Otherwise returns false.

The following code would show you what it is for:  
```
// open Google Search page
WebUI.openBrowser('https://www.google.co.jp/')

// in the page, look up the node
//   <img alt="Google" id="hplogo" src="/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png" >`
// and get the value of src attribute.

def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src')

def result = CustomKeywords.'com.kazurayam.ksbackyard.MyCustomKeywords.verifyUrlAccessibility'(imgSrc)

if (result) {
    WebUI.comment(">>> The Image in <img src=\"${imgSrc}\"> is accessible")
} else {
    WebUI.comment(">>> The Image in <img src=\"${imgSrc}\"> is NOT accessible")
}

WebUI.closeBrowser()
```


Provided with `verifyUrlAccessibility(String url)` in your Katalon Studio project, you can make sure your web site free from broken-links. You can assert that &lt;a&gt; tags are successfully linked to pages, and &lt;img&gt; tags are associated with some image entities.

# Problem to solve

Most of web pages contain elements which refers to external resources. For example,
the [Google Search page](https://www.google.co.jp/) contains following code snippet:

```
<img style="padding-top:112px" height="92" src="/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png" width="272" alt="Google" id="hplogo" title="Google" onload="typeof google==='object'&amp;&amp;google.aft&amp;&amp;google.aft(this)">
```

Surely the `src="/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"` above is correct and associated with a nice 5-colored image entity. Highly unlikely for you to find broken links there.

However, it is a shame though, my web site is not as good as Google. It tends to have broken links. Therefore I need to test if all the &lt;img src="..."&gt; tags in my web site are NOT broken, are associated with image entities. And I want to use Katalon Studio to automate the test.


# How to run the demo

1. git clone this demo project to your PC
2. Start Katalon Studio and open the downloaded project.
3. This demo depends on `org.apache.httpcomponents.httpclient_x.x.x.jar` and other related jars. However you do not need to worry about the dependencies. These jars are bundled in the Katalon Studio distributable.
4. Select the Test Case `Main` and run it with whatever browser you like.
5. The test case `Main` is supposed to fail with such message:
```
Unable to get access to https://www.google.co.jp/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png_!!!!!!!!!!
```
6. I appended a string `_!!!!!!!!!` intentionally to the URL, so that the test case fails.
7. Open the Test Case `Main` in script mode and find the following portion:
```
//def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src')
def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src') + "_!!!!!!!!!!!!"
```
8. Uncomment the first line, and comment-out the second line. Save the code.
9. Run the Test Case `Main` again. This time it should succeed.

# Design

## How the test case is coded

The Test Case [`Main`](https://github.com/kazurayam/GoogleImgTest/blob/master/Scripts/Main/Script1523916283247.groovy) is coded like this:

```
WebUI.openBrowser('https://www.google.co.jp/')
def imgSrc = WebUI.getAttribute(findTestObject('Page_Google/img_hplogo'), 'src')
def result = CustomKeywords.'com.kazurayam.ksbackyard.MyCustomKeywords.verifyUrlAccessibility'(imgSrc)
if (result) {
    WebUI.comment(">>> do whatever you want in case <img> is proved accessible")
} else {
    WebUI.comment(">>> do whatever you want in case <img> is found inaccessible")
}
```

This picks up the src value of a &lt;img&gt; tag, and invoke custom keyword `veirfyUrlAccesibility` with URL as argument.
This custom keyword internally makes HTTP GET request to the URL, and verifies the response.
The custom keyword returns boolean value, so that you have a chance to make `if ... then ... else` control after the URL verification.

## How the custom keyword is coded

The Custome Keyword is coded [as such](https://github.com/kazurayam/GoogleImgTest/blob/master/Keywords/com/kazurayam/ksbackyard/MyCustomKeywords.groovy).
This is a simple application of the Apache HttpClient. I learned how to use this library by reading the sample code in [HttpClient Quick Start](https://hc.apache.org/httpcomponents-client-ga/quickstart.html)

Another keyword `getVersion()` is supported. This method returns a String such as '0.1', '0.1.2', '0.2' and so one. This method is just to coin the version of `MyCustomKeywords` class. This is supposed for the caller Test Case to print the class version.

## Notes

The URL as argument to verifyUrlAccessibility is supposed to be an absolute URL starting with `http://` or `https://`.
