package GoogleTest;

import base.Common;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGoogle extends Common {

   // WebDriver drive;
    @Test
    public void GoogleMethod(){
        System.out.println("Google call!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Assert.assertEquals(true,true); // This works
    }

}
