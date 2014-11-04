package demo;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

// Notes: hub and videoURL parameter is passed in from Jenkins maven job like:
// GOAL: test -Dhub=http://53YOotZpYNKdKrSRvUvKBd4e5ujPCohx:h5cwp1MdFnhfE0X0KiAcmvgiQqYiE5FM@6T9DOVV93KLAE2HR.gridlastic.com:4444/wd/hub -DvideoUrl=https://s3-ap-southeast-2.amazonaws.com/b2729248-ak68-6948-a2y8-80e7479te16a/9ag7b09j-6a38-58w2-bb01-17qw724ce46t
// You will find these parameters in your Gridlastic dashboard after starting your selenium grid
public class test {
 
	 private RemoteWebDriver driver;	 
	 ITestContext myTestContext;
	 DesiredCapabilities capabilities;
		 
		@Parameters({ "browser-name", "platform-name", "browser-version", "hub","videoUrl", "record-video" })
		@BeforeMethod(alwaysRun = true)
		public void beforeMethod(String browser_name, String platform_name, String browser_version, String hub, String videoURL, String record_video,ITestContext myTestContext) throws Exception {	
			
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments(Arrays.asList("--start-maximized"));
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	
			if (platform_name.equalsIgnoreCase("win7")) {
				capabilities.setPlatform(Platform.VISTA);
			}
			if (platform_name.equalsIgnoreCase("win8")) {
				capabilities.setPlatform(Platform.WIN8);
			}
			
		
			
			//video record
			if (record_video.equalsIgnoreCase("True")){
			capabilities.setCapability("video", "True");	
			} else {
			capabilities.setCapability("video", "False");
			}
		
		
	        this.driver = new RemoteWebDriver(
	                new URL(hub),
	                capabilities);
	        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); 
			
	  
	  		//VIDEO	URL
	        if(capabilities.getCapability("video").equals("True")){
	        myTestContext.setAttribute("video_url", videoURL+"/play.html?" + ((RemoteWebDriver) driver).getSessionId()); 
	        } else {
	        myTestContext.removeAttribute("video_url");	
	        }

	  		
		}
		
		@Parameters({"test-title"})  
		@Test
		   public void test_site(String test_title, ITestContext myTestContext) throws Exception  { 	
			driver.get("http://www.java.com");
			driver.findElementByLinkText("About Java").click();
			driver.findElementByLinkText("Troubleshoot Java").click();
			driver.findElementByLinkText("Support");	
	    }
	    
			
	   
	    @AfterMethod(alwaysRun = true)
	    public void tearDown() throws Exception {
	    driver.quit();	    	
	    }
	    
	    
	    
	
}
