package io.gary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by pugang on 2018/5/7.
 */

public class LoadTask {

    private String searchPath;

    private String dbUrl;
    private String dbUser;
    private String dbPass;

    public LoadTask(String dbUrl, String dbUser, String dbPass, String searchPath) {
        this.dbUrl = dbUrl;
        this.dbPass = dbPass;
        this.dbUser = dbUser;
        this.searchPath = searchPath;
    }

    public String getSearchPath() {
        return searchPath;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }
    public String getDbPass() {
        return dbPass;
    }

    public long Execute(){
        int numOfCpu = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numOfCpu);
        List<ParseFileTask> tasks = new ArrayList<>();
        File file = new File(this.getSearchPath());
        if (!file.isDirectory()){
            throw new RuntimeException(this.getSearchPath()+" is not availble");
        }
        File[] files = file.listFiles( f -> f.getAbsolutePath().endsWith("csv"));
        for (File f:files) {
            tasks.add(new ParseFileTask(f,this.getDbUrl(),this.getDbUser(),this.getDbPass()));
        }
        long total = 0;
        try {
            for (Future<Long> result: executorService.invokeAll(tasks)){
                total = total + result.get();
            }
            Framework.getLogger().info("total add data {}",total);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdownNow();
        return total;
    }
}