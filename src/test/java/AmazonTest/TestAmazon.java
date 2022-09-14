package AmazonTest;

import base.Common;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestAmazon extends Common {

    @Test
    public void AmazonMethod(){

        System.out.println("Amazon method 1!!!!!!!!!");
        Assert.assertEquals(true,true); // This works

    }

    //@Test
    //public void Yelling(){
        //System.out.println("Amazon method 2");
      //  Assert.assertEquals(true,true); // This works
    //}
}
