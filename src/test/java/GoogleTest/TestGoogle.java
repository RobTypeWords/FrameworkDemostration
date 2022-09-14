package GoogleTest;

import base.Common;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestGoogle extends Common {
    @Test
    public void GoogleMethod(){
        System.out.println("Google call!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Assert.assertEquals(true,true); // This works
    }
    //@Test
   // public void Amazonmethod2(){
       // System.out.println("Amazon call 2");
       // Assert.assertEquals(true,true); // This works
   // }
}
