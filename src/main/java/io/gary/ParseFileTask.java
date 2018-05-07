package io.gary;

import java.io.*;
import java.sql.*;
import java.util.concurrent.Callable;

/**
 * Created by pugang on 2018/5/7.
 */
public class ParseFileTask implements Callable<Long> {
    File file;
    String dbUrl;
    String dbUser;
    String dbPass;
    private static final String SQL ="insert into time_series_data (item_id,trading_date,stock_code,item_value) values( uuid(), ?,?,?)";
    public ParseFileTask(File file,String dbUrl,String dbUser,String dbPass){
        this.file = file;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }
    @Override
    public Long call() throws Exception {
        long total = 0l;
        Framework.getLogger().debug("begin connection database");
        try (
                Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);
                PreparedStatement stmt = conn.prepareStatement(SQL);
        ) {
            conn.setAutoCommit(false);
            String dates = file.getName().replace(".csv", "");
            Framework.getLogger().debug("begin load file {}...", file.getAbsolutePath());
            Date date = Date.valueOf(dates);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line ;
                while ((line = br.readLine()) != null) {
                    for (LineItem item : LineItem.parseItem(date, line)) {
                        stmt.setDate(1, item.tradeDate);
                        stmt.setString(2, item.stockCode);
                        stmt.setDouble(3, item.itemValue);
                        stmt.addBatch();
                        total ++;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Framework.getLogger().info("finish load file {}, total {}",file.getAbsolutePath(),total);
        return total;
    }
}
