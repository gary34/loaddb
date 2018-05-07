import io.gary.ParseFileTask;
import org.junit.Test;

import java.io.File;
import java.sql.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by pugang on 2018/5/7.
 */
public class TestParseFileTask {
    @Test
    public void testParseFileTask() throws Exception {
        File testFile = new File("datafile/2017-09-19.csv");
        String dbUser = "root";
        String dbUrl  = "jdbc:mysql://localhost:3306/test_db";
        String dbPass = "root";
        ParseFileTask task = new ParseFileTask(testFile,dbUrl,dbUser,dbPass);

        try (
                Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);
                Statement stmt = conn.createStatement();
        ){
            long total = task.call();
            long t = 0;
            try (ResultSet rs = stmt.executeQuery("select count(*) from time_series_data")){
                while (rs.next()){
                     t = rs.getLong(1);
                }
            }
            stmt.execute("truncate table time_series_data");
            assertEquals(total,t);
        }
    }
}
