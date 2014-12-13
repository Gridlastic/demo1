package demo;


import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class CustomListener extends TestListenerAdapter{
    private int m_count = 0;
	 
    @Override
    public void onTestFailure(ITestResult tr) {  	
    System.out.println("******************** FAILURE: " + tr.getTestContext().getCurrentXmlTest().getParameter("test-title") + " " +tr.getTestContext().getCurrentXmlTest().getParameter("platform-name") + " TEST METHOD: "+tr.getMethod().getMethodName() +" REASON:"+tr.getThrowable().getMessage().substring(0, 30).trim()+"... VIDEO: "+tr.getTestContext().getAttribute("video_url"));
      
    }
	 
    @Override
    public void onTestSkipped(ITestResult tr) {
        log(tr.getName()+ "--Test method skipped\n");
    }
	 
    
    @Override
    public void onTestSuccess(ITestResult tr) {
    if(tr.getTestContext().getCurrentXmlTest().getParameter("record-video").equalsIgnoreCase("True")){
    System.out.println("SUCCESS: " + tr.getTestContext().getCurrentXmlTest().getParameter("test-title")+ " " +tr.getTestContext().getCurrentXmlTest().getParameter("platform-name") + " TEST METHOD: "+tr.getMethod().getMethodName() +"... VIDEO: "+tr.getTestContext().getAttribute("video_url"));
    } else {
    System.out.println("SUCCESS: " + tr.getTestContext().getCurrentXmlTest().getParameter("test-title")+ " " +tr.getTestContext().getCurrentXmlTest().getParameter("platform-name") + " TEST METHOD: "+tr.getMethod().getMethodName() +"... SCREEN SHOT: "+tr.getTestContext().getAttribute("screenshot_url"));
    }
    }
    
  	 
    private void log(String string) {
        System.out.print(string);
        if (++m_count % 40 == 0) {
	    System.out.println("");
        }
    }
    
    
    
  
    
    

}