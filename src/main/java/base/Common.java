package base;

import Reporting.ExtentManager;

import Reporting.ExtentTestManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.*;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;



public class Common {

    public static ExtentReports extent;

    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void startExtent(Method method) { // Dont know if its the right class
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName().toLowerCase();
        ExtentTestManager.startTest(method.getName());
        ExtentTestManager.getTest().assignCategory(className);
    }

    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        // Time is missing

        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));
        for (String group : result.getMethod().getGroups()) {
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        }
        ExtentTestManager.endTest();
        extent.flush();
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(DRIVER, result.getName());
        }
        DRIVER.quit();
    }

    @AfterSuite
    public void generateReport() {
        extent.close();
    }


    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }


    public static WebDriver DRIVER = null; // The static may cause an issue for Parallel execution

    //TBA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public String username = "robtypewords_zw7xaQ";//
    public String accesskey = "jVf5fVrsN7yqkvCTo1zr";
    // I could use the properties file instead

    @Parameters({"useCloudEnv", "cloudEnvName", "os", "os_version", "browser", "BrowserVersion", "URL"})
    @BeforeMethod
    public void setup(@Optional("false") boolean useCloudEnv, @Optional("browserstack") String cloudEnvname, @Optional("windows") String os,
                      @Optional("10") String os_version, @Optional("chrome") String browser, @Optional("104") String browserVersion,
                      @Optional("https://www.amazon.com/") String url) throws IOException {

        System.out.println("Being used: " + " " + useCloudEnv + " " + cloudEnvname + " " + os + " " + os_version + " " + " " + browser + " " + browserVersion + " " + url);

        // Here: Implement cloud logic. If theres no cloud testing the default should get the local driver then
        if (useCloudEnv == true) {
            if (cloudEnvname.equalsIgnoreCase("browserstack")) {

                GetCloudDriver(cloudEnvname, username, accesskey, os, os_version, browser, browserVersion);
            }
        } else {
            getdriver(os, browser); // This should allow mr to remove the else condition below
        }
        DRIVER.get(url);
        DRIVER.manage().window().fullscreen();
        DRIVER.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    public WebDriver getdriver(String os, String Browsername) {
        if (Browsername.equalsIgnoreCase("chrome")) {
            if (os.equalsIgnoreCase("OS X")) {
                System.setProperty("webdriver.chrome.driver", "Driver/chromedriver.exe");
            } else if (os.equalsIgnoreCase("Windows")) {
                System.setProperty("webdriver.chrome.driver", "Driver/chromedriver.exe");
            }
            DRIVER = new ChromeDriver();
        } else if (Browsername.equalsIgnoreCase("firefox")) {
            if (os.equalsIgnoreCase("OS X")) {
                System.setProperty("webdriver.gecko.driver", "Driver/geckodriver.exe");
            } else if (os.equalsIgnoreCase("windows")) {
                System.setProperty("webdriver.gecko.driver", "Driver/geckodriver.exe");
            }
            DRIVER = new FirefoxDriver();
        }
        return DRIVER;
    }

    //New here!!!!!
    public WebDriver GetCloudDriver(String envName, String envUsername, String envKey, String os, String os_version, String browser,
                                    String browserVersion) throws IOException {

        System.out.println("Cloud Driver Parameters being passed: " + envName + " " + envUsername + " " + envKey + " " +
                os + " " + os_version + " " + browser + " " + browserVersion);

        //Configuration setup Remote Driver for Cloud environment
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browser", browser);
        cap.setCapability("browserVersion", browserVersion); //Having this name causes issues
        cap.setCapability("os", os);
        cap.setCapability("os_version", os_version);
        cap.setCapability("browserstack.idleTimeout", 5); // Does this solve the idle sessions?
        cap.setCapability("resolution", "1024x768"); // NEW!!!!!!!!!!!1

        // If I had multiple cloud testing environment
        if (envName.equalsIgnoreCase("browserstack")) {
            // Remote Driver Creation

            try {
                DRIVER = new RemoteWebDriver(new URL(("http://") + envUsername + ":" + envKey +
                        "@hub-cloud.browserstack.com/wd/hub"), cap);
                DRIVER.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return DRIVER;


    }

    @AfterMethod
    public void cleanup() {
        DRIVER.close();
    }


    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
        Date date = new Date();
        df.format(date);

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            //FileUtils.copyFile(file, new File(System.getProperty("user.dir") + "/screenshots/" + screenshotName + " " + df.format(date) + ".png"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot " + e.getMessage());
            ;
        }




    }
}
