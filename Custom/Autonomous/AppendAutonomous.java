package org.firstinspires.ftc.teamcode.Custom.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Custom.HelperClasses.ArrayListClass;
import org.firstinspires.ftc.teamcode.Custom.HelperClasses.FileIOClass;
import org.firstinspires.ftc.teamcode.Custom.HelperClasses.ParseClass;
import org.firstinspires.ftc.teamcode.Custom.HelperClasses.ThreadClass;
import org.firstinspires.ftc.teamcode.Custom.HelperClasses.VariableHub;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Autonomous(name="AutoGeneration", group="Tool")
//@Disabled
public class AppendAutonomous extends LinearOpMode
{
    //Custom Helper Classes
    private static VariableHub variableHub = new VariableHub();
    private static FileIOClass fileIOClass = new FileIOClass();
    private static ParseClass parseClass = new ParseClass();
    private static ArrayListClass arrayListClass = new ArrayListClass();
    private static ThreadClass threadClass = new ThreadClass();

    //Global Variables
    private static boolean firstCreation = false;

    private static String programName = variableHub.programName;
    private static boolean CreateDebugFile = variableHub.CreateDebugFile;
    private static boolean CreateMergeLogFile = variableHub.CreateMergeLogFile;
    private static String VuforiaKey = variableHub.VuforiaKey;
    private static String rawConfigLine;
    private static String strippedConfigLine;
    private static ArrayList<String> rawLogLine = new ArrayList<>();
    private static ArrayList<String> strippedLogLine = new ArrayList<>();
    private static String runPattern;
    private static String strippedRunPattern;
    private static ArrayList<String> bufferedAutonomous = new ArrayList<>();
    private static ArrayList<String> bufferedDebug = new ArrayList<>();
    private static ArrayList<String> lstActions = new ArrayList<>();

    //File Locations
    private static String InitialDirectory = variableHub.InitialDirectory;
    private static String ReadLogsFilePath = InitialDirectory + "/CustomLog/RecordingLog.txt";
    private static String AutonomousFilePath = InitialDirectory + "/CustomLog/" + programName + ".java";
    private static String MergedLogsFilePath = InitialDirectory + "/CustomLog/MergeLogs/" + programName + "_Mergedlogs.txt";
    private static String DebugFilePath = InitialDirectory + "/CustomLog/DebugFiles/" + programName + "_Debug.txt";
    private static String RunPatternFilePath = InitialDirectory + "/CustomLog/ActionPattern/" + programName + "_ActionPattern.txt";

    @Override
    public void runOpMode()
    {
        waitForStart();

        fileIOClass.CheckDirectories(InitialDirectory);

        if (opModeIsActive())
        {
            AutonomousStartup();

            if (firstCreation)
            {
                FirstCreation();
                ReplaceDetectionImports();
                ReplaceProgramDeclarations();
                ReplaceOpModeDeclarations();
                ReplaceSetModes();
                RemovePlaceHolders();
            }
            else
            {
                ImportExistingAutonomous();
            }

            MethodHandler();

            FileGarbageCollection();

            OutputAutonomousFile();
            OutputDebugFile();
            OutputMergeLogsFile();

            FinalChecks();
            sleep(1000);
        }
    }
    public static void AutonomousStartup()
    {
        fileIOClass.CheckLogFile(ReadLogsFilePath);

        //Variable Startup
        rawConfigLine = fileIOClass.FileGetLineContaining(ReadLogsFilePath, "CONFIG = ");
        rawLogLine = fileIOClass.FileIntoArrayList(ReadLogsFilePath);
        lstActions = fileIOClass.FileDetectActionPatterns(ReadLogsFilePath);
        runPattern = fileIOClass.FileDetectRunPattern(RunPatternFilePath);
        strippedConfigLine = parseClass.StringAfterDelimiter(rawConfigLine, "= ");
        strippedRunPattern = parseClass.StringAfterDelimiter(runPattern, "RUNPATTERN ");
        strippedLogLine = arrayListClass.ArrayListAdjustAfterDelimiter(rawLogLine, ": ");


        parseClass.ParseConfigIntoArrays(rawConfigLine);

        if (strippedRunPattern.equals("START"))
        {
            firstCreation = true;
        }
    }
    //region Initial Steps
    public static void FirstCreation()
    {
        bufferedAutonomous.add("package org.firstinspires.ftc.teamcode;");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("import com.qualcomm.robotcore.eventloop.opmode.Autonomous;");
        bufferedAutonomous.add("import com.qualcomm.robotcore.eventloop.opmode.Disabled;");
        bufferedAutonomous.add("import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;");
        bufferedAutonomous.add("import com.qualcomm.robotcore.hardware.DcMotor;");
        bufferedAutonomous.add("import com.qualcomm.robotcore.hardware.Servo;");
        bufferedAutonomous.add("import com.qualcomm.robotcore.hardware.CRServo;");
        bufferedAutonomous.add("import com.qualcomm.robotcore.util.ElapsedTime;");
        bufferedAutonomous.add("import java.io.*;");
        bufferedAutonomous.add("//DetectionImports");
        bufferedAutonomous.add( "");
        bufferedAutonomous.add("@Autonomous(name=\"" + programName + "\", group=\"Linear Opmode\")");
        bufferedAutonomous.add("//@Disabled");
        bufferedAutonomous.add("public class " + programName + " extends LinearOpMode");
        bufferedAutonomous.add("{");
        bufferedAutonomous.add("    private ElapsedTime runtime = new ElapsedTime();");
        bufferedAutonomous.add("    private String lastAction = \"\";");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("    //ProgramDeclarations");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("    @Override");
        bufferedAutonomous.add("    public void runOpMode()");
        bufferedAutonomous.add("    {");
        bufferedAutonomous.add("       telemetry.addData(\"Status\", \"Initialized\");");
        bufferedAutonomous.add("       telemetry.update();");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("       //OpModeDeclarations");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("       waitForStart();");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("       //SetMotorModes");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("       if (opModeIsActive())");
        bufferedAutonomous.add("       {");
        bufferedAutonomous.add("           DeleteRunPattern();");
        bufferedAutonomous.add("           runtime.reset();");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("           //END-RunOpModeCalls");
        bufferedAutonomous.add("           WriteRunPattern();");
        bufferedAutonomous.add("       }");
        bufferedAutonomous.add("    }");
        bufferedAutonomous.add("    //END-RunOpMode");
        bufferedAutonomous.add("    public void DeleteRunPattern()");
        bufferedAutonomous.add("    {");
        bufferedAutonomous.add("        File file = new File(\"" + RunPatternFilePath + "\");");
        bufferedAutonomous.add("");
        bufferedAutonomous.add("        if (file.exists())");
        bufferedAutonomous.add("        {");
        bufferedAutonomous.add("            file.delete();");
        bufferedAutonomous.add("        }");
        bufferedAutonomous.add("    }");
        bufferedAutonomous.add("    public void WriteRunPattern()");
        bufferedAutonomous.add("    {");
        bufferedAutonomous.add("        if (lastAction != null)");
        bufferedAutonomous.add("        {");
        bufferedAutonomous.add("            try");
        bufferedAutonomous.add("            {");
        bufferedAutonomous.add("                FileWriter fileWriter = new FileWriter(\"" + RunPatternFilePath + "\");");
        bufferedAutonomous.add("                fileWriter.write(\"RUNPATTERN \" + lastAction);");
        bufferedAutonomous.add("                fileWriter.close();");
        bufferedAutonomous.add("            }");
        bufferedAutonomous.add("            catch (IOException e)");
        bufferedAutonomous.add("            {");
        bufferedAutonomous.add("                throw new RuntimeException(\"Could Not Write To File\");");
        bufferedAutonomous.add("            }");
        bufferedAutonomous.add("        }");
        bufferedAutonomous.add("    }");
        bufferedAutonomous.add("}");
    }

    //region ReplaceInitialPlaceHolders
    public static void ReplaceDetectionImports()
    {
        String SearchString = "//DetectionImports";
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"import org.firstinspires.ftc.robotcore.external.ClassFactory;");
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString, "import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;");
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString, "import org.firstinspires.ftc.robotcore.external.tfod.Recognition;");
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString, "import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;");
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString, "import com.vuforia.CameraDevice;");
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString, "import java.util.List;");
    }
    public static void ReplaceProgramDeclarations()
    {
        String SearchString = "//ProgramDeclarations";

        for (int i = 0; i < parseClass.DcMotorCount(); i++)
        {
            String configDeviceType = "DcMotor";
            String configDeviceName = parseClass.GetDcMotorName(i);
            String programDeviceName = configDeviceType.toLowerCase() + configDeviceName;
            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString, "    private " + configDeviceType + " " + programDeviceName + " = null;");
        }
        for (int i = 0; i < parseClass.CRServoCount(); i++)
        {
            String configDeviceType = "CRServo";
            String configDeviceName = parseClass.GetCRServoName(i);
            String programDeviceName = configDeviceType.toLowerCase() + configDeviceName;
            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"    private " + configDeviceType + " " + programDeviceName + " = null;");
        }
        for (int i = 0; i < parseClass.ServoCount(); i++)
        {
            String configDeviceType = "Servo";
            String configDeviceName = parseClass.GetServoName(i);
            String programDeviceName = configDeviceType.toLowerCase() + configDeviceName;
            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"    private " + configDeviceType + " " + programDeviceName + " = null;");
        }
        for (int i = 0; i < parseClass.CameraCount(); i++)
        {
            String isItWebcam = parseClass.GetCameraIsItWebcam(i);

            if (isItWebcam.equals("true"))
            {
                String configDeviceType = "CameraName";
                String configDeviceName = parseClass.GetCameraName(i);
                String programDeviceName = configDeviceType.toLowerCase() + configDeviceName;

                arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"    private " + configDeviceType + " " + programDeviceName + " = null;");
            }
        }
    }
    public static void ReplaceOpModeDeclarations()
    {
        String SearchString = "//OpModeDeclarations";

        for (int i=0; i < parseClass.DcMotorCount(); i++)
        {
            String DcMotorType = "DcMotor";
            String DcMotorName = parseClass.GetDcMotorName(i);
            String DcMotorDirection = parseClass.GetDcMotorDirection(i);
            String programDeviceName = DcMotorType.toLowerCase() + DcMotorName;

            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"       " + programDeviceName + " = hardwareMap.get(" + DcMotorType + ".class, \"" + DcMotorName + "\");");
            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"       " + programDeviceName + ".setDirection(" + DcMotorType + ".Direction." + DcMotorDirection + ");");
        }
        for (int i=0; i < parseClass.CRServoCount(); i++)
        {
            String CRServoType = "CRServo";
            String CRServoName = parseClass.GetCRServoName(i);
            String CRServoDirection = parseClass.GetCRServoDirection(i);
            String programDeviceName = CRServoType.toLowerCase() + CRServoName;

            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"       " + programDeviceName + " = hardwareMap.get(" + CRServoType + ".class, \"" + CRServoName + "\");");
            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"       " + programDeviceName + ".setDirection(" + CRServoType + ".Direction." + CRServoDirection + ");");
        }
        for (int i=0; i < parseClass.ServoCount(); i++)
        {
            String ServoType = "Servo";
            String ServoName = parseClass.GetServoName(i);
            String programDeviceName = ServoType.toLowerCase() + ServoName;

            arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"       " + programDeviceName + " = hardwareMap.get(" + ServoType + ".class, \"" + ServoName + "\");");
        }
        for (int i=0; i < parseClass.CameraCount(); i++)
        {
            String isItWebcam = parseClass.GetCameraIsItWebcam(i);

            if (isItWebcam.equals("true"))
            {
                String CameraType = "CameraName";
                String CameraName = parseClass.GetCameraName(i);
                String programDeviceName = CameraType.toLowerCase() + CameraName;

                arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"       " + programDeviceName + " = hardwareMap.get(" + CameraType + ".class, \"" + CameraName + "\");");
            }
        }
    }
    public static void ReplaceSetModes()
    {
        String SearchString = "//SetMotorModes";
        for (int i=0; i < parseClass.DcMotorCount(); i++)
        {
            String DcMotorEncoder = parseClass.GetDcMotorEncoder(i);

            if (DcMotorEncoder.equals("true"))
            {
                String DcMotorType = "DcMotor";
                String DcMotorName = parseClass.GetDcMotorName(i);
                String programDeviceName = DcMotorType.toLowerCase() + DcMotorName;

                arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, SearchString,"       " + programDeviceName + ".setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);");
            }
        }
    }
    public static void RemovePlaceHolders()
    {
        arrayListClass.RemoveStringInArrayFromSearchString(bufferedAutonomous, "//DetectionImports");
        arrayListClass.RemoveStringInArrayFromSearchString(bufferedAutonomous, "//ProgramDeclarations");
        arrayListClass.RemoveStringInArrayFromSearchString(bufferedAutonomous, "//OpModeDeclarations");
        arrayListClass.RemoveStringInArrayFromSearchString(bufferedAutonomous, "//SetMotorModes");
    }
    //endregion
    //endregion

    //region Move Methods
    public static void CreateMove01Method(int beginIndex, int endIndex)
    {
        //MethodCall
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, "//END-RunOpModeCalls", "           Move01();");

        ArrayList<String> methodContents = new ArrayList<>();

        methodContents.add("    public void Move01()");
        methodContents.add("    {");
        methodContents.add("        lastAction = \"M01\";");

        for (int i = beginIndex; i <= endIndex; i++)
        {
            String logLine = parseClass.StringAfterDelimiter(strippedLogLine.get(i), "MOVE ").replace(" ", ",");
            String currentTimestamp = parseClass.StringBeforeDelimiter(rawLogLine.get(i), " ");
            String nextTimeStamp;

            if (i == endIndex)
            {
                nextTimeStamp = currentTimestamp;
            }
            else
            {
                nextTimeStamp = parseClass.StringBeforeDelimiter(rawLogLine.get(i + 1), " ");
            }

            double ms = GetTimeStamp(nextTimeStamp, currentTimestamp);
            String msString = String.format("%.0f", ms);

            methodContents.add("        Move(" + msString + "," + logLine + ");");
        }
        methodContents.add("    }//END-Move01");

        arrayListClass.AddArrayStringAfterSearchStringInArray(bufferedAutonomous, methodContents, "//END-RunOpMode");
    }
    public static void AppendMove(int beginIndex, int endIndex)
    {
        ArrayList<String> methodContents = new ArrayList<>();
        for (int j = beginIndex; j <= endIndex; j++)
        {
            String logLine = parseClass.StringAfterDelimiter(strippedLogLine.get(j), "MOVE ").replace(" ", ",");
            String currentTimestamp = parseClass.StringBeforeDelimiter(rawLogLine.get(j), " ");
            String nextTimeStamp;

            if (j == endIndex)
            {
                nextTimeStamp = currentTimestamp;
            }
            else
            {
                nextTimeStamp = parseClass.StringBeforeDelimiter(rawLogLine.get(j + 1), " ");
            }

            double ms = GetTimeStamp(nextTimeStamp, currentTimestamp);
            String msString = String.format("%.0f", ms);

            methodContents.add("        Move(" + msString + "," + logLine + ");");
        }
        arrayListClass.AddArrayStringBeforeSearchStringInArray(bufferedAutonomous, methodContents, "}//END-Move01");
    }
    public static void CreateMoveLogic()
    {
        ArrayList<String> methodContents = new ArrayList<>();
        boolean hasEncoder = false;


        //==========Method Start==========
        String parms = "";

        for (int i = 0; i < parseClass.DcMotorCount(); i++)
        {
            String name = parseClass.GetDcMotorName(i);
            String encoder = parseClass.GetDcMotorEncoder(i);

            if (encoder.equals("true"))
            {
                String power = "double " + name + "Pwr";
                String position = "int " + name + "Pos";

                parms = parms + "," + power + "," + position;
                hasEncoder = true;
            }
            else
            {
                String power = "double " + name + "Pwr";
                parms = parms + "," + power;
            }
        }
        for (int i = 0; i < parseClass.CRServoCount(); i++)
        {
            String name = parseClass.GetCRServoName(i);
            String power = "double " + name + "Pwr";

            parms = parms + "," + power;
        }
        for (int i = 0; i < parseClass.ServoCount(); i++)
        {
            String name = parseClass.GetServoName(i);
            String position = "double " + name + "Pos";

            parms = parms + "," + position;
        }


        methodContents.add("    public void Move(double durationMilliseconds" + parms + ")");
        methodContents.add("    {");
        methodContents.add("        ElapsedTime durationTime = new ElapsedTime();");
        methodContents.add("        Boolean stillHaveTime = true;");

        if (hasEncoder)
        {
            methodContents.add("        Boolean stillBusy = true;");
        }
        methodContents.add("");


        String isStillBusyLine = "";
        for (int i = 0; i < parseClass.DcMotorCount(); i++)
        {
            String name = parseClass.GetDcMotorName(i);
            String encoder = parseClass.GetDcMotorEncoder(i);
            String programName = "dcmotor" + name;

            if (encoder.equals("true"))
            {
                methodContents.add("        " + programName + ".setMode(DcMotor.RunMode.RUN_USING_ENCODER);");
                methodContents.add("        " + programName + ".setTargetPosition(" + name + "Pos);");
                methodContents.add("        " + programName + ".setPower(" + name + "Pwr);");
                methodContents.add("        " + programName + ".setMode(DcMotor.RunMode.RUN_TO_POSITION);");

                isStillBusyLine = isStillBusyLine + programName + ".isBusy():";
            }
            else
            {
                methodContents.add("        " + programName + ".setPower(" + name + "Pwr);");
            }
            methodContents.add("");
        }
        for (int i = 0; i < parseClass.CRServoCount(); i++)
        {
            String name = parseClass.GetCRServoName(i);
            String programName = "crservo" + name;

            methodContents.add("        " + programName + ".setPower(" + name + "Pwr);");
            methodContents.add("");
        }
        for (int i = 0; i < parseClass.ServoCount(); i++)
        {
            String name = parseClass.GetServoName(i);
            String programName = "servo" + name;

            methodContents.add("        " + programName + ".setPosition(" + name + "Pos);");
            methodContents.add("");
        }

        //==========While Loop==========
        String whileParm = "";
        if (hasEncoder)
        {
            whileParm = "(stillHaveTime || stillBusy)";
        }
        else
        {
            whileParm = "stillHaveTime";
        }


        methodContents.add("        durationTime.reset();");
        methodContents.add("        while (opModeIsActive() && " + whileParm + ")");
        methodContents.add("        {");
        methodContents.add("            stillHaveTime = durationTime.milliseconds() < durationMilliseconds;");

        if (hasEncoder)
        {
            isStillBusyLine = isStillBusyLine.substring(0,isStillBusyLine.length()-1).replace(":", " || ");

            methodContents.add("            stillBusy = " + isStillBusyLine + ";");
        }

        methodContents.add("        }");
        methodContents.add("    }");

        arrayListClass.AddArrayStringAfterSearchStringInArray(bufferedAutonomous, methodContents, "//END-RunOpMode");
    }
    //endregion
    //region Detection Methods
    public static void CreateDetect()
    {
        //MethodCall
        arrayListClass.AddStringBeforeSearchStringInArray(bufferedAutonomous, "//END-RunOpModeCalls", "           Detect01();");
        ArrayList<String> methodContents = new ArrayList<>();

        for (int i = 0; i < parseClass.DetectionCount(); i++)
        {
            String labels = parseClass.GetDetectionLabelsFromIndex(i);
            ArrayList<String> lstLabels = arrayListClass.StringSplitIntoArrayList(labels, "~");
            String methodNum = "";

            if (i > 9)
            {
                methodNum = String.valueOf(i);
            }
            else
            {
                methodNum = "0" + String.valueOf(i+1);
            }

            methodContents.add("    public void Detect" + methodNum + "()");
            methodContents.add("    {");
            methodContents.add("        String getLabel = UseVuforiaAndTensorFlowObjectDetection();");
            methodContents.add("        if (getLabel == null)");
            methodContents.add("        {");
            methodContents.add("            lastAction = \"D" + methodNum + "_0\";");
            methodContents.add("            //Append D" + methodNum + "_0 Here");
            methodContents.add("        }");
            for (int j = 0; j < lstLabels.size(); j++)
            {
                String label = parseClass.StringAfterDelimiter(lstLabels.get(j), " ");
                String labelNum = String.valueOf(j+1);

                methodContents.add("        else if (getLabel.equals(\"" + label + "\"))");
                methodContents.add("        {");
                methodContents.add("            lastAction = \"D" + methodNum + "_" + labelNum + "\";");
                methodContents.add("            //Append D" + methodNum + "_" + labelNum + " Here");
                methodContents.add("        }");
            }
        }
        methodContents.add("    }");

        arrayListClass.AddArrayStringAfterSearchStringInArray(bufferedAutonomous, methodContents, "//END-RunOpMode");
    }
    public static void AppendDetect(int beginIndex, int endIndex, String runPattern)
    {
        ArrayList<String> methodContents = new ArrayList<>();
        for (int i = 0; i < parseClass.DetectionCount(); i++)
        {
            String methodNum = "";

            if (i > 9)
            {
                methodNum = String.valueOf(i);
            }
            else
            {
                methodNum = "0" + String.valueOf(i+1);
            }

            if (runPattern.equals("START") || runPattern.equals("M01"))
            {
                for (int j = 0; j < lstActions.size(); j++)
                {
                    String currentAction = lstActions.get(j);

                    if (currentAction.startsWith("D"))
                    {
                        runPattern = "D" + methodNum + "_" + arrayListClass.StringSplitIntoArrayList(currentAction, ":").get(3);
                    }
                }
            }

            for (int j = beginIndex; j <= endIndex; j++)
            {
                String logLine = parseClass.StringAfterDelimiter(strippedLogLine.get(j), "MOVE ").replace(" ", ",");
                String currentTimestamp = parseClass.StringBeforeDelimiter(rawLogLine.get(j), " ");
                String nextTimeStamp;

                if (j == endIndex)
                {
                    nextTimeStamp = currentTimestamp;
                }
                else
                {
                    nextTimeStamp = parseClass.StringBeforeDelimiter(rawLogLine.get(j + 1), " ");
                }

                double ms = GetTimeStamp(nextTimeStamp, currentTimestamp);
                String msString = String.format("%.0f", ms);

                methodContents.add("            Move(" + msString + "," + logLine + ");");
            }
            arrayListClass.AddArrayStringAfterSearchStringInArray(bufferedAutonomous, methodContents, "//Append " + runPattern + " Here");
        }
    }
    public static void AutonomousCreateObjectDetectionLogicMethod()
    {
        if (parseClass.DetectionCount() > 0)
        {
            ArrayList<String> methodContents = new ArrayList<>();
            for (int i = 0; i < parseClass.DetectionCount(); i++)
            {
                String flashlight = parseClass.GetDetectionFlashlight(i);
                String cameraName = parseClass.GetDetectionCameraName(i);
                String cameraDirection = parseClass.GetDetectionCameraDirection(i);
                String durationMilliseconds = parseClass.GetDetectionSnapshotTime(i);
                String minConfidence = parseClass.GetDetectionConfidence(i);
                String customAsset = parseClass.GetDetectionCustomAsset(i);
                String assetFile = parseClass.GetDetectionAssetFile(i);
                String labels = parseClass.GetDetectionLabelsFromIndex(i);
                String zoom = parseClass.GetDetectionZoom(i);

                ArrayList<String> lstLabels = arrayListClass.StringSplitIntoArrayList(labels, "~");
                lstLabels = arrayListClass.AddCharactersToArrayListString(lstLabels, "\"", "\"");
                labels = parseClass.ArrayListJoinIntoString(lstLabels, ",");


                methodContents.add("    public String UseVuforiaAndTensorFlowObjectDetection()");
                methodContents.add("    {");
                methodContents.add("        String getLabel = null;");
                methodContents.add("        String[] labels = {" + labels + "};");
                methodContents.add("        TFObjectDetector tensorFlow;");
                methodContents.add("        VuforiaLocalizer vuforia;");
                methodContents.add("");
                methodContents.add("        int tensorFlowMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(\"tfodMonitorViewId\", \"id\", hardwareMap.appContext.getPackageName());");
                methodContents.add("        VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters();");
                methodContents.add("        vuforiaParameters.vuforiaLicenseKey = \"" + VuforiaKey + "\";");

                for (int j = 0; j < parseClass.CameraCount(); j++)
                {
                    String isItWebcam = parseClass.GetCameraIsItWebcam(j);
                    if (isItWebcam.equals("true"))
                    {
                        methodContents.add("        vuforiaParameters.cameraName = " + cameraName + ";");
                    }
                    else
                    {
                        methodContents.add("        vuforiaParameters.cameraDirection = VuforiaLocalizer.CameraDirection." + cameraDirection + ";");
                    }
                }

                methodContents.add("        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);");
                methodContents.add("");
                methodContents.add("        TFObjectDetector.Parameters tensorFlowParameters = new TFObjectDetector.Parameters(tensorFlowMonitorViewId);");
                methodContents.add("        tensorFlowParameters.minResultConfidence = " + minConfidence + ";");
                methodContents.add("        tensorFlowParameters.isModelTensorFlow2 = true;");
                methodContents.add("        tensorFlowParameters.inputSize = 300;");
                methodContents.add("        tensorFlow = ClassFactory.getInstance().createTFObjectDetector(tensorFlowParameters, vuforia);");

                if (customAsset.equals("true"))
                {
                    methodContents.add("        tensorFlow.loadModelFromFile(" + assetFile + ",labels);");
                }
                else
                {
                    methodContents.add("        tensorFlow.loadModelFromAsset(\"" + assetFile + "\",labels);");
                }

                methodContents.add("");
                methodContents.add("        if (tensorFlow != null)");
                methodContents.add("        {");
                methodContents.add("            tensorFlow.activate();");
                methodContents.add("            tensorFlow.setZoom(" + zoom + ", 16.0/9.0);");
                methodContents.add("            ElapsedTime timer = new ElapsedTime();");
                methodContents.add("");

                if (flashlight.equals("true"))
                {
                    methodContents.add("            CameraDevice.getInstance().setFlashTorchMode(true);");
                }

                methodContents.add("            boolean notDetectedYet = true;");
                methodContents.add("            timer.reset();");
                methodContents.add("            while (opModeIsActive() && (" + durationMilliseconds + " > timer.milliseconds()) && notDetectedYet)");
                methodContents.add("            {");
                methodContents.add("                List<Recognition> updatedRecognitions = tensorFlow.getUpdatedRecognitions();");
                methodContents.add("                if (updatedRecognitions != null)");
                methodContents.add("                {");
                methodContents.add("                    for (Recognition recognition : updatedRecognitions)");
                methodContents.add("                    {");
                methodContents.add("                        getLabel = recognition.getLabel();");
                methodContents.add("                        notDetectedYet = false;");
                methodContents.add("                    }");
                methodContents.add("                }");
                methodContents.add("            }");
                methodContents.add("            tensorFlow.shutdown();");
                methodContents.add("            vuforia.close();");

                if (flashlight.equals("true"))
                {
                    methodContents.add("            CameraDevice.getInstance().setFlashTorchMode(false);");
                }

                methodContents.add("        }");
                methodContents.add("        return getLabel;");
                methodContents.add("    }");
                methodContents.add("");
                arrayListClass.AddArrayStringAfterSearchStringInArray(bufferedAutonomous, methodContents, "//END-RunOpMode");
            }
        }
    }
    //endregion
    //region Other Methods
    public static void MethodHandler()
    {
        boolean createDetect = false;
        boolean createMove = false;
        boolean appendToMove = false;
        boolean appendToDetect = false;
        String RunPattern = strippedRunPattern;
        String currentMethod = "";
        String prevAction = "";

        if (RunPattern.startsWith("D"))
        {
            currentMethod = RunPattern.replace("D", "Detect");
        }
        else if (RunPattern.startsWith("M"))
        {
            currentMethod = RunPattern.replace("M", "Move");
        }

        for (int i = 0; i < lstActions.size(); i++)
        {
            String currentAction = lstActions.get(i);
            int beginIndex = Integer.parseInt(arrayListClass.StringSplitIntoArrayList(currentAction, ":").get(1)) - 1;
            int endIndex = Integer.parseInt(arrayListClass.StringSplitIntoArrayList(currentAction, ":").get(2)) - 1;

            createDetect = false;
            createMove = false;
            appendToMove = false;
            appendToDetect = false;

            if (RunPattern.equals("START"))
            {
                if (currentAction.startsWith("D"))
                {
                    createDetect = true;
                }
                else if (currentAction.startsWith("M"))
                {
                    if (prevAction.startsWith("D"))
                    {
                        appendToDetect = true;
                    }
                    else
                    {
                        createMove = true;
                    }
                }
            }
            else
            {
                if (currentMethod.startsWith("Move01"))
                {
                    if (currentAction.startsWith("M"))
                    {
                        if (prevAction.startsWith("D"))
                        {
                            appendToDetect = true;
                        }
                        else
                        {
                            appendToMove = true;
                        }
                    }
                    else if (currentAction.startsWith("D"))
                    {
                        createDetect = true;
                    }
                }
                else if (currentMethod.startsWith("Detect01"))
                {
                    if (currentAction.startsWith("M"))
                    {
                        appendToDetect = true;
                    }
                }
            }
//            bufferedAutonomous.add("RunPattern " + RunPattern);
//            bufferedAutonomous.add("PrevAction " + prevAction);
            prevAction = currentAction;

//            bufferedAutonomous.add("CurrentAction " + currentAction);
//            bufferedAutonomous.add("CreateDetect " + createDetect);
//            bufferedAutonomous.add("CreateMove " + createMove);
//            bufferedAutonomous.add("AppendToMove " + appendToMove);
//            bufferedAutonomous.add("AppendToDetect " + appendToDetect);
//            bufferedAutonomous.add("");


            if (createMove)
            {
                CreateMove01Method(beginIndex, endIndex);
                CreateMoveLogic();
            }
            else if (createDetect)
            {
                CreateDetect();
                AutonomousCreateObjectDetectionLogicMethod();
            }
            else if (appendToMove)
            {
                AppendMove(beginIndex, endIndex);
            }
            else if (appendToDetect)
            {
                AppendDetect(beginIndex, endIndex, RunPattern);
            }
        }
    }
    public static double GetTimeStamp(String NextTime, String CurrentTime)
    {
        return (Double.parseDouble(NextTime) - Double.parseDouble(CurrentTime));
    }
    //endregion

    //region EndingMethods
    public static void ImportExistingAutonomous()
    {
        bufferedAutonomous.clear();
        bufferedAutonomous = fileIOClass.FileIntoArrayList(AutonomousFilePath);
    }
    public static void FileGarbageCollection()
    {
        if (firstCreation)
        {
            fileIOClass.FileDelete(AutonomousFilePath);
            fileIOClass.FileDelete(DebugFilePath);
            fileIOClass.FileDelete(MergedLogsFilePath);
        }
        else
        {
            fileIOClass.FileRenameInSameDirectory(InitialDirectory + "/CustomLog/", programName + ".java", programName + "OLD.java");
            fileIOClass.FileDelete(InitialDirectory + "/CustomLog/" + programName + "OLD.java");
        }
    }
    public void FinalChecks()
    {
        fileIOClass.FinalFileChecks(this, AutonomousFilePath, CreateDebugFile, DebugFilePath, CreateMergeLogFile, MergedLogsFilePath);
    }

    //endregion
    //region FileCreationMethods
    public static void OutputAutonomousFile()
    {
        OutputArrayToFile(bufferedAutonomous, AutonomousFilePath);
    }
    public static void OutputDebugFile()
    {
        if (CreateDebugFile)
        {
            bufferedDebug.clear();
            if (firstCreation)
            {
                bufferedDebug.add("Config: Master~" + strippedConfigLine);
                bufferedDebug.add("Config: Current~" + strippedConfigLine);
                bufferedDebug.add("");
                bufferedDebug.add("PrevMethod: " + strippedRunPattern);
                bufferedDebug.add("");

                //Display given information for every device
                for (int i = 0; i < parseClass.DcMotorCount(); i++)
                {
                    bufferedDebug.add("Info: Device Type   : (DcMotor)");
                    bufferedDebug.add("Info: Name          : (" + parseClass.GetDcMotorName(i) + ")");
                    bufferedDebug.add("Info: Direction     : (" + parseClass.GetDcMotorDirection(i) + ")");
                    bufferedDebug.add("Info: Encoder       : (" + parseClass.GetDcMotorEncoder(i) + ")");
                    bufferedDebug.add("");
                }

                for (int i = 0; i < parseClass.CRServoCount(); i++)
                {
                    bufferedDebug.add("Info: Device Type   : (CRServo)");
                    bufferedDebug.add("Info: Name          : (" + parseClass.GetCRServoName(i) + ")");
                    bufferedDebug.add("Info: Direction     : (" + parseClass.GetCRServoDirection(i) + ")");
                    bufferedDebug.add("");
                }

                for (int i = 0; i < parseClass.ServoCount(); i++)
                {
                    bufferedDebug.add("Info: Device Type   : (Servo)");
                    bufferedDebug.add("Info: Name          : (" + parseClass.GetServoName(i) + ")");
                    bufferedDebug.add("");
                }

                for (int i = 0; i < parseClass.CameraCount(); i++)
                {
                    bufferedDebug.add("Info: Device Type   : (Camera)");
                    bufferedDebug.add("Info: Name          : (" + parseClass.GetCameraName(i) + ")");
                    bufferedDebug.add("Info: IsItWebcam    : (" + parseClass.GetCameraIsItWebcam(i) + ")");
                    bufferedDebug.add("");
                }

                for (int i = 0; i < parseClass.DetectionCount(); i++)
                {
                    bufferedDebug.add("Info: Detection Items");
                    bufferedDebug.add("Info: Name          : (" + parseClass.GetDetectionCameraName(i) + ")");
                    bufferedDebug.add("Info: Asset File    : (" + parseClass.GetDetectionAssetFile(i) + ")");
                    bufferedDebug.add("Info: Custom Asset  : (" + parseClass.GetDetectionCustomAsset(i) + ")");
                    bufferedDebug.add("Info: Labels        : (" + parseClass.GetDetectionLabelsFromIndex(i) + ")");
                    bufferedDebug.add("Info: CamDirection  : (" + parseClass.GetDetectionCameraDirection(i) + ")");
                    bufferedDebug.add("Info: Flashlight    : (" + parseClass.GetDetectionFlashlight(i) + ")");
                    bufferedDebug.add("Info: Confidence    : (" + parseClass.GetDetectionConfidence(i) + ")");
                    bufferedDebug.add("Info: SnapshotTime  : (" + parseClass.GetDetectionSnapshotTime(i) + ")");
                    bufferedDebug.add("Info: Zoom          : (" + parseClass.GetDetectionZoom(i) + ")");
                    bufferedDebug.add("");
                }
                OutputArrayToFile(bufferedDebug, DebugFilePath);
            }
            else
            {
                bufferedDebug = fileIOClass.FileIntoArrayList(DebugFilePath);
                arrayListClass.CheckIfConfigsEqual(bufferedDebug);

                bufferedDebug.set(1, "Config: Current~" + strippedConfigLine);
                OutputArrayToFile(bufferedDebug, DebugFilePath);
            }
        }
    }
    public static void OutputMergeLogsFile()
    {
        if (CreateMergeLogFile)
        {
            fileIOClass.FileAppendFile(ReadLogsFilePath, MergedLogsFilePath);
        }
    }
    public static void OutputArrayToFile(ArrayList<String> ArrayList, String FilePath)
    {
        threadClass.CreateFileWithBackgroundThread(ArrayList, FilePath);
    }
    //endregion
}
