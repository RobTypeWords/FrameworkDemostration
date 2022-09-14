package base;

import Reporting.ExtentManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.*;
import sun.security.krb5.internal.crypto.Des;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Common {

    // Extent Set up!!!!!!!!!!!!!!!!!1
//public static ExtentReports extent;
   // @BeforeSuite
   // public void extentSetup(){
   // extent = ExtentManager.getInstance();

    //}

    // Extent close!!!!!!!!!!!!!!!!!!!!!!!
    //@AfterSuite
    //public void CloseExtent(){

       // extent.flush(); //Dont know if this is right?!
   // }

    public static WebDriver DRIVER = null; // The static may cause an issue for Parallel execution

    //TBA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public String username = "robtypewords_zw7xaQ";
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
                DRIVER.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
            } catch (MalformedURLException e){
                e.printStackTrace();
            }
        }
        return DRIVER;


    }

    @AfterMethod
    public void cleanup() {
        DRIVER.close();
    }

}
