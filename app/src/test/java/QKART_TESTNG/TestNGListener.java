package QKART_TESTNG;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestContext;

public class TestNGListener implements ITestListener{

    @Override
    public void onStart(ITestContext context){
        System.out.println("-----Test Execution Started-----");
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        // TODO Auto-generated method stub
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestStart", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // TODO Auto-generated method stub
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestSuccess", result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // TODO Auto-generated method stub
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestSkipped", result.getName());
    }

    

    @Override
    public void onTestFailure(ITestResult result) {
        // TODO Auto-generated method stub
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestFailure", result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "failedWithinSuccessPercentage", result.getName());
    }

    @Override
    public void onFinish(ITestContext context){
        System.out.println("-----Test Execution Finished-----");
    }
}
