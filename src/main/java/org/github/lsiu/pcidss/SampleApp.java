package org.github.lsiu.pcidss;

import java.io.FileInputStream;
import java.util.Properties;
import org.github.lsiu.pcidss.util.FixPciDssv3Req821;

/**
 * Sample application illustrating usage
 * @author lsiu
 */
public class SampleApp {
    
    public static final void main(String[] args) throws Exception {
        Properties config = new Properties();
        try(FileInputStream fis = new FileInputStream("sample.properties")) {
            config.load(fis);
        }
        
        String dbUser = config.getProperty("db.user");
        String dbEnryptedPassword = config.getProperty("db.encryptedPassword");
        
        System.out.format("DB User Name=%s\n", dbUser);
        System.out.format("DB Encrypted Password=%s\n", dbEnryptedPassword);
        System.out.format("DB Password=%s\n", FixPciDssv3Req821.decrypt(dbEnryptedPassword));
    }
}
