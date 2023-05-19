package org.firstinspires.ftc.teamcode.Custom.HelperClasses;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileIOClass {
    private static ParseClass parseClass = new ParseClass();
    private static ArrayListClass arrayListClass = new ArrayListClass();

    private static boolean logMessages = true;

    public static void FileAppendFile(String ReadPath, String WriteFile)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(WriteFile, true);
            Scanner scanner = new Scanner(new File(ReadPath));

            fileWriter.write("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\r\n");
            fileWriter.write( "// " + ReadPath+ "\r\n");
            fileWriter.write("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////\r\n");
            while (scanner.hasNextLine())
            {
                fileWriter.write(scanner.nextLine() + "\r\n");
            }
            fileWriter.close();
            scanner.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could Not Create File");
        }
    }
    public static void CheckLogFile(String LogFilePath)
    {
        if (FileExists(LogFilePath))
        {
            ArrayList<String> temp = FileIntoArrayList(LogFilePath);

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
    public static void FileAppendLine(String FilePath, String LineToAppend)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(FilePath, true);
            fileWriter.write(LineToAppend + "\r\n");
            fileWriter.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException("File Could Not Append");
        }
    }
    public static void CheckDirectories(String RootDirectory)
    {
        if (!FileExists(RootDirectory + "/CustomLog"))
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
    public static void FileDelete(String FilePath)
    {
        File file = new File(FilePath);
        file.delete();
    }
    public static ArrayList<String> FileDetectActionPatterns(String FilePath)
    {
        ArrayList<String> ReturnValue = new ArrayList<>();
        ArrayList<String> temp = FileIntoArrayList(FilePath);

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
    public static String FileDetectRunPattern(String FilePath)
    {
        String ReturnValue;

        if (FileExists(FilePath))
        {
            ArrayList<String> temp = FileIntoArrayList(FilePath);
            ReturnValue = arrayListClass.ArrayListItemContains(temp, "RUNPATTERN");
        }
        else
        {
            ReturnValue = "RUNPATTERN START";
        }

        LogMessage(ReturnValue);

        return ReturnValue;
    }
    public static void FileRenameInSameDirectory(String Directory, String OldFileName, String NewFileName)
    {
        String OldFile = Directory + OldFileName;
        String NewFile = Directory + NewFileName;
        if (FileExists(OldFile))
        {
            File fileOld = new File(OldFile);
            File fileNew = new File(NewFile);

            if (FileExists(NewFile))
            {
                throw new RuntimeException("File Already Exists");
            }

            fileOld.renameTo(fileNew);

            if (!FileExists(NewFile))
            {
                throw new RuntimeException("File Did Not Create");
            }
        }
        else
        {
            throw new RuntimeException("Initial File Does Not Exist");
        }
    }
    public static boolean FileExists(String FilePath)
    {
        File file = new File(FilePath);
        return file.exists();
    }
    public static void FinalFileChecks(LinearOpMode program, String AutonomousFilePath, Boolean DebugFile, String DebugFilePath, Boolean MergedFile, String MergedLogFilePath)
    {
        boolean correct = false;
        while (!correct)
        {
            if (FileExists(AutonomousFilePath))
            {
                LogMessage("Main FIle Exists");


                if (DebugFile && MergedFile)
                {
                    if (FileExists(DebugFilePath) && FileExists(MergedLogFilePath))
                    {
                        LogMessage("Debug and Merged File Exists");
                        correct = true;
                    }
                }
                if (DebugFile && !MergedFile)
                {
                    if (FileExists(DebugFilePath))
                    {
                        LogMessage("Debug File Exists");
                        correct = true;
                    }
                }
                else if (MergedFile && !DebugFile)
                {
                    if (FileExists(MergedLogFilePath))
                    {
                        LogMessage("Merged File Exists");
                        correct = true;
                    }
                }
                else
                {
                    correct = true;
                }
            }
            else
            {
                throw new RuntimeException("Main File Not Created");
            }

            program.telemetry.addData("Info","Checking Files");
            program.telemetry.update();
        }
        program.telemetry.addData("Info","Files Have Been Created");
        program.telemetry.update();
    }
    public static String FileGetLineContaining(String FilePath, String SearchString)
    {
        String ReturnValue = null;

        ArrayList<String> temp = FileIntoArrayList(FilePath);

        ReturnValue = arrayListClass.ArrayListItemContains(temp, SearchString);

        return ReturnValue;
    }
    public static ArrayList<String> FileIntoArrayList(String FilePath)
    {
        ArrayList<String> LogReturn = new ArrayList<String>();
        if (FileExists(FilePath))
        {
            try
            {
                Scanner scanner = new Scanner(new File(FilePath));

                while (scanner.hasNextLine())
                {
                    LogReturn.add(scanner.nextLine());
                }
                scanner.close();
            }
            catch (IOException e)
            {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        return LogReturn;
    }

    public static void LogMessage(String Message)
    {
        if (logMessages)
        {
            Log.i("AHSDebug", Message);
        }
    }
}
