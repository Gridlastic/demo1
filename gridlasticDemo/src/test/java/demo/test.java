package demo;

import org.openqa.selenium.remote.Augmenter;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

// Notes: hub and videoURL parameter is passed in from Jenkins maven job like:
// GOAL: test -Dhub=http://USERNAME:USER_KEY@SUBDOMAIN.gridlastic.com:80/wd/hub -DvideoUrl=https://s3-ap-southeast-2.amazonaws.com/b2729248-ak68-6948-a2y8-80e7479te16a/9ag7b09j-6a38-58w2-bb01-17qw724ce46t
// You will find these parameters in your Gridlastic dashboard after starting your selenium grid
// Also, the Jenkins hostname is passed in via -DjenkinsHostname from Jenkins maven job

public class test {
 
	 private RemoteWebDriver driver;	 
	 ITestContext myTestContext;
	 DesiredCapabilities capabilities;
		 
		@Parameters({ "browser-name", "platform-name", "browser-version", "hub","videoUrl", "record-video" })
		@BeforeMethod(alwaysRun = true)
		public void beforeMethod(String browser_name, String platform_name, String browser_version, String hub, String videoURL, String record_video,ITestContext myTestContext) throws Exception {	
			
			
			DesiredCapabilities capabilities = new DesiredCapabilities();	
			capabilities.setBrowserName(browser_name);		
			capabilities.setVersion(browser_version);
						
			if (platform_name.equalsIgnoreCase("win7")) {
				capabilities.setPlatform(Platform.VISTA);
				capabilities.setCapability("platformName", "windows"); //required from selenium version 3.9.1 when testing with firefox or IE. Required when testing with Chrome 77+.
			}
			if (platform_name.equalsIgnoreCase("win8")) {
				capabilities.setPlatform(Platform.WIN8);
				capabilities.setCapability("platformName", "windows"); //required from selenium version 3.9.1 when testing with firefox or IE. Required when testing with Chrome 77+.
			}	
			if (platform_name.equalsIgnoreCase("win8_1")) {
				capabilities.setPlatform(Platform.WIN8_1);
				capabilities.setCapability("platformName", "windows"); //required from selenium version 3.9.1 when testing with firefox or IE. Required when testing with Chrome 77+.
			}
			if (platform_name.equalsIgnoreCase("win10")) {
				capabilities.setPlatform(Platform.WIN10);
				capabilities.setCapability("platformName", "windows"); //required from selenium version 3.9.1 when testing with firefox or IE. Required when testing with Chrome 77+.
			}
			if (platform_name.equalsIgnoreCase("linux")) {
				capabilities.setPlatform(Platform.LINUX);
			}
	
			
			if (browser_name.equalsIgnoreCase("chrome")){
				ChromeOptions options = new ChromeOptions();
				options.addArguments("disable-infobars"); // starting from Chrome 57 the info bar displays with "Chrome is being controlled by automated test software."
				
				// On LINUX the "start-maximized" Chrome option does not expand browser window to max screen size.
				if (platform_name.equalsIgnoreCase("linux")) {
				options.addArguments(Arrays.asList("--window-position=0,0"));
				options.addArguments(Arrays.asList("--window-size=1920,1080"));	
				} else {
				options.addArguments(Arrays.asList("--start-maximized"));
				}
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				} 
			
				
			//video recording
			if (record_video.equalsIgnoreCase("True")){
			capabilities.setCapability("video", "True");	
			} else {
			capabilities.setCapability("video", "False");
			}
		
		
		
	        this.driver = new RemoteWebDriver(
	                new URL(hub),
	                capabilities);
	        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); 
	       
	        // On LINUX/FIREFOX the "driver.manage().window().maximize()" option does not expand browser window to max screen size.
	    	if (platform_name.equalsIgnoreCase("linux") && browser_name.equalsIgnoreCase("firefox")) {
	    		driver.manage().window().setSize(new Dimension(1920, 1080));	
	    	}
	        
	        
	        
	  		// VIDEO URL
	        if(capabilities.getCapability("video").equals("True")){
	        myTestContext.setAttribute("video_url", videoURL+"/play.html?" + ((RemoteWebDriver) driver).getSessionId()); 
	        } else {
	        myTestContext.removeAttribute("video_url");	
	        }

	  		
		}
		
		@Parameters({"test-title","jenkinsHostname"})  
		@Test
		   public void test_site(String test_title, String jenkins_hostname, ITestContext myTestContext) throws Exception  { 	
			driver.get("https://www.google.com/ncr");
			Thread.sleep(10000); //slow down for demo purposes
			WebElement element = driver.findElement(By.name("q"));
	        element.sendKeys("webdriver");
	        element.submit();
	        Thread.sleep(5000);
			
			// Take example screenshot and save it to Jenkins workspace
			String screenshot_filepath = System.getenv("WORKSPACE")+"/";
			String screenshot_filename = "screenshot_" + ((RemoteWebDriver) driver).getSessionId() + ".png";
			myTestContext.setAttribute("screenshot_url", jenkins_hostname+"/job/"+System.getenv("JOB_NAME")+"/ws/"+screenshot_filename);
			take_screenshot(screenshot_filepath+screenshot_filename); 
	    }
	    
			   
	    @AfterMethod(alwaysRun = true)
	    public void tearDown() throws Exception {
	    driver.quit();	    	
	    }
	    
	    
	    private void take_screenshot(String file) throws Exception {
	    	WebDriver augmentedDriver = new Augmenter().augment(driver);
	    	TakesScreenshot ss = (TakesScreenshot) augmentedDriver;
	    	String base64Screenshot = ss.getScreenshotAs(OutputType.BASE64);
	    	byte[] decodedScreenshot = Base64.decodeBase64(base64Screenshot.getBytes());
	    	FileOutputStream fos = new FileOutputStream(new File(file));
	    	fos.write(decodedScreenshot);
	    	fos.close();
	    	}    
	
}