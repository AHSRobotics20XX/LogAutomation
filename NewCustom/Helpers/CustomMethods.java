package org.firstinspires.ftc.teamcode.Custom.Helpers;

import java.io.File;
import java.util.ArrayList;

public class CustomMethods {
    private static ParseClass parseClass = new ParseClass();
    private static FileIOClass fileIOClass = new FileIOClass();
    private static ArrayListClass arrayListClass = new ArrayListClass();

    public static void CheckIfConfigsEqual(ArrayList<String> ArrayList)
    {
        boolean ReturnValue = false;

        String masterConfig = "";
        String currentConfig = "";

        for (int i = 0; i < ArrayList.size(); i++)
        {
            String line = ArrayList.get(i);

            if (line.startsWith("Config: Master"))
            {
                masterConfig = line;
            }
            else if (line.startsWith("Config: Current"))
            {
                currentConfig = line;
            }
        }

        if (masterConfig.length() > 0 && currentConfig.length() > 0)
        {
            masterConfig = parseClass.StringAfterDelimiter(masterConfig, "~");
            fileIOClass.LogMessage(masterConfig);
            currentConfig = parseClass.StringAfterDelimiter(currentConfig, "~");
            fileIOClass.LogMessage(currentConfig);

            if (masterConfig.equals(currentConfig))
            {
                ReturnValue = true;
            }
        }

        if (!ReturnValue)
        {
            throw new RuntimeException("Mismatched Configs");
        }
    }

    public void CheckLogFile(String LogFilePath)
    {
        if (fileIOClass.FileExists(LogFilePath))
        {
            ArrayList<String> temp = fileIOClass.FileIntoArrayList(LogFilePath);

            int count = 0;
            for (int i = 0; i < temp.size(); i++)
            {
                String line = temp.get(i);
                if (line.contains("= "))
                {
                    count++;
                }
            }

            if (count > 1)
            {
                throw new RuntimeException("Too Many Config Lines");
            }
            else if (count < 1)
            {
                throw new RuntimeException("No Config Line Found");
            }
        }
        else
        {
            throw new RuntimeException("Log File Does Not Exist");
        }
    }

    public void CheckDirectories(String RootDirectory)
    {
        if (!fileIOClass.FileExists(RootDirectory + "/CustomLog"))
        {
            File CustomLogDirectory = new File(RootDirectory + "/CustomLog");

            boolean error = false;

            if (CustomLogDirectory.mkdirs())
            {
                File MergeLogsDirectory = new File(RootDirectory + "/CustomLog/MergeLogs");
                File DebugFilesDirectory = new File(RootDirectory + "/CustomLog/DebugFiles");
                File ActionPatternDirectory = new File(RootDirectory + "/CustomLog/ActionPattern");

                if (!MergeLogsDirectory.mkdirs())
                {
                    error = true;
                }
                if (!DebugFilesDirectory.mkdirs())
                {
                    error = true;
                }
                if (!ActionPatternDirectory.mkdirs())
                {
                    error = true;
                }
            }
            else
            {
                error = true;
            }


            if (error)
            {
                throw new RuntimeException("Some Directories Could Not Be Created");
            }
        }
    }
    public ArrayList<String> FileDetectActionPatterns(String FilePath)
    {
        ArrayList<String> ReturnValue = new ArrayList<>();
        ArrayList<String> temp = fileIOClass.FileIntoArrayList(FilePath);

        String CurrentActionCode = "";
        String LastActionCode = "";
        String currentDetectionLabel = "";
        String lastDetectionLabel = "";

        int rangeBegin = 0;
        int rangeEnd = 0;

        for (int i = 0; i < temp.size(); i++)
        {
            String textLine = temp.get(i);

            if (textLine.contains("DETECT") || textLine.contains("MOVE") || textLine.contains("TRACK"))
            {
                LastActionCode = CurrentActionCode;
                lastDetectionLabel = currentDetectionLabel;

                if (textLine.contains("DETECT"))
                {
                    CurrentActionCode = "D";
                    rangeEnd = i;
                    currentDetectionLabel = parseClass.GetDetectionLabelFromString(textLine);
                }
                else if (textLine.contains("MOVE"))
                {
                    CurrentActionCode = "M";
                    rangeEnd = i;
                    currentDetectionLabel = "";
                }
                else if (textLine.contains("TRACK"))
                {
                    CurrentActionCode = "T";
                    rangeEnd = i;
                    currentDetectionLabel = "0";
                }

                if (!LastActionCode.equals(CurrentActionCode))
                {
                    if (LastActionCode.length() > 0)
                    {
                        if (LastActionCode.equals("D"))
                        {
                            ReturnValue.add(LastActionCode + ":" + String.valueOf(rangeBegin) + ":" + String.valueOf(rangeEnd) + ":" + lastDetectionLabel);
                        }
                        else if (LastActionCode.equals("M"))
                        {
                            ReturnValue.add(LastActionCode + ":" + String.valueOf(rangeBegin) + ":" + String.valueOf(rangeEnd));
                        }
                    }
                    rangeBegin = i+1;
                    rangeEnd = i;
                }
            }
        }

        if (CurrentActionCode.length() > 0)
        {
            rangeEnd = rangeEnd + 1;
            ReturnValue.add(CurrentActionCode + ":" + String.valueOf(rangeBegin) + ":" + String.valueOf(rangeEnd) + ":" + currentDetectionLabel);
        }

        return ReturnValue;
    }
    public String FileDetectRunPattern(String FilePath)
    {
        String ReturnValue;

        if (fileIOClass.FileExists(FilePath))
        {
            ArrayList<String> temp = fileIOClass.FileIntoArrayList(FilePath);
            ReturnValue =  arrayListClass.ArrayListItemContains(temp, "RUNPATTERN");
        }
        else
        {
            ReturnValue = "RUNPATTERN START";
        }

        fileIOClass.LogMessage(ReturnValue);

        return ReturnValue;
    }
}
