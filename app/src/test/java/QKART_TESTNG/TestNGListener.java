package QKART_TESTNG;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestNGListener implements ITestListener{
    
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
    public void onTestFailure(ITestResult result) {
        // TODO Auto-generated method stub
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestFailure", result.getName());
    }
}
