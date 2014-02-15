package com.mpayme.pcidss.util;

import org.github.lsiu.pcidss.util.FixPciDssv3Req821;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class FixPciDssv3Req821Test {

    @Test
    @SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch", "CallToThreadDumpStack"})
    public void testDecrypt() {
        try {
            Assert.assertEquals("secret", FixPciDssv3Req821.decrypt("32jwFvVJ9iVL74arZ302pg=="));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
