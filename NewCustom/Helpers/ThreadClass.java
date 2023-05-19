package org.firstinspires.ftc.teamcode.Custom.Helpers;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadClass implements Callable<String>
{
    public static String filePath = "";
    public static ArrayList<String> list = new ArrayList<>();
    public static String action = "";
    public static boolean debug;
    public static String debugTag;

    public static void CreateFileWithBackgroundThread(ArrayList<String> ArrayList, String FilePath, String logTag, boolean Debug)
    {
        action = "action1";
        filePath = FilePath;
        list = ArrayList;
        debugTag = logTag;
        debug = Debug;

        try
        {
            //Creates Executor
            ExecutorService executor = Executors.newSingleThreadExecutor();

            //List of returns from call method
            Callable<String> callable = new ThreadClass();

            //Future object of list of callable
            Future<String> future = executor.submit(callable);

            Thread.sleep(1000);

            executor.shutdown();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String call() throws Exception
    {
        if (action.equals("action1"))
        {
            if (!filePath.isEmpty())
            {
                try
                {
                    BufferedWriter bufferedOutput = new BufferedWriter(new FileWriter(filePath, false));

                    for (int i = 0; i < list.size(); i++)
                    {
                        String line = list.get(i) + "\r\n";
                        bufferedOutput.write(line);
                    }

                    bufferedOutput.flush();
                    bufferedOutput.close();
                    list.clear();

                    if (debug) {
                        Log.i(debugTag, "File Has Been Created");
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }
}
