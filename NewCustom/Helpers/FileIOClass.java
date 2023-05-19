package org.firstinspires.ftc.teamcode.Custom.Helpers;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileIOClass {
    private ParseClass parseClass = new ParseClass();
    private ArrayListClass arrayListClass = new ArrayListClass();

    private boolean logMessages = true;

    public void FileAppendFile(String ReadPath, String WriteFile)
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
    public void FileAppendLine(String FilePath, String LineToAppend)
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
    public void FileDelete(String FilePath)
    {
        File file = new File(FilePath);
        file.delete();
    }

    public void FileRenameInSameDirectory(String Directory, String OldFileName, String NewFileName)
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
    public boolean FileExists(String FilePath)
    {
        File file = new File(FilePath);
        return file.exists();
    }
    public void FinalFileChecks(LinearOpMode program, String AutonomousFilePath, Boolean DebugFile, String DebugFilePath, Boolean MergedFile, String MergedLogFilePath)
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
    public String FileGetLineContaining(String FilePath, String SearchString)
    {
        String ReturnValue = null;

        ArrayList<String> temp = FileIntoArrayList(FilePath);

        ReturnValue = arrayListClass.ArrayListItemContains(temp, SearchString);

        return ReturnValue;
    }
    public ArrayList<String> FileIntoArrayList(String FilePath)
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

    public void LogMessage(String Message)
    {
        if (logMessages)
        {
            Log.i("AHSDebug", Message);
        }
    }
}
