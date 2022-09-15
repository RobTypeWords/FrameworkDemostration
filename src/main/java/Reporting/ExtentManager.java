package Reporting;



import com.relevantcodes.extentreports.ExtentReports;
import org.testng.ITestContext;
import org.testng.Reporter;

import java.io.File;

public class ExtentManager {

    private static ExtentReports extent;
    static ITestContext context;
    String box  = System.getProperty("user.dir") + "\\ExtentReporter\\Results.html";

    public synchronized static ExtentReports getInstance(){
        if(extent == null){
            File outputDirectory = new File(context.getOutputDirectory());
            File resultDirectory = new File(outputDirectory.getParentFile(),"html");
            extent = new ExtentReports(System.getProperty("user.dir") + "\\ExtentReporter\\Results.html",true);
            Reporter.log("Extent Report Directory"+ resultDirectory, true);
            extent.addSystemInfo("Host Name", "Demo").addSystemInfo("Environment","QA")
                    .addSystemInfo("User Name", "Robert");
            //extent.loadConfig(new File(System.getProperty("user.dir")+ "/report-config.xml"));

        }
        return extent;
}
    public static void setOutputDirectory(ITestContext context){
        ExtentManager.context = context;

    }

}
