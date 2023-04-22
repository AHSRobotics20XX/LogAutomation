package org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes;

import android.graphics.Path;
import android.provider.ContactsContract;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Custom.HelperClasses.FileIOClass;
import org.firstinspires.ftc.teamcode.Custom.HelperClasses.ThreadClass;
import org.firstinspires.ftc.teamcode.Custom.HelperClasses.VariableHub;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;


@Disabled

/**
 * LogClass outputs everything out to logcat
 */
public class LogClass extends LinearOpMode
{
    VariableHub variableHub = new VariableHub();
    FileIOClass fileIOClass = new FileIOClass();
    LinearOpMode program;

    private String TAG;
    private String configBuffer;
    private HardwareDeviceClass hardwareDeviceClass;
    private String foundLabel;

    private String FilePath = variableHub.InitialDirectory + "/CustomLog/RecordingLog.txt";
    private ArrayList<String> logBuffer = new ArrayList<>();

    private boolean initialLogEntries = true;
    private boolean UseFileForLogging;
    private boolean logEnabled = false;
    private boolean TelemetryLogDetection = false;
    public boolean logDevice = false;
    public boolean logDetection = false;

    /**
     * Required for extension of LinearOpMode
     */
    @Override
    public void runOpMode()
    {
    }

    /**
     * @param Tag your logcat filter tag
     * @param UseFile Use file on phone or android logcat
     */
    public LogClass(String Tag, HardwareDeviceClass hardwareDeviceClass, boolean UseFile)
    {
        UseFileForLogging = UseFile;
        TAG = Tag;
        this.hardwareDeviceClass = hardwareDeviceClass;
        program = hardwareDeviceClass.program;


        fileIOClass.CheckDirectories(variableHub.InitialDirectory);
        File file = new File(FilePath);
        if (file.exists())
        {
            file.delete();
        }
    }

    /**
     * Creates config line using the hardwareDeviceClass and cameraClass lists and values
     */
    private void GetLogConfigInfo()
    {
        configBuffer = "";

        for (int i=0; i < hardwareDeviceClass.lstDcMotors.size(); i++)
        {
            configBuffer = configBuffer + ":DcMotor";
            configBuffer = configBuffer + "(" + hardwareDeviceClass.lstDcMotors.get(i).motorName;

            if (hardwareDeviceClass.lstDcMotors.get(i).direction == DcMotor.Direction.FORWARD)
            {
                configBuffer = configBuffer + ", FORWARD";
            }
            else
            {
                configBuffer = configBuffer + ", REVERSE";
            }

            if (hardwareDeviceClass.lstDcMotors.get(i).useEncoder)
            {
                configBuffer = configBuffer + ", true";
            }
            else
            {
                configBuffer = configBuffer + ", false";
            }

            configBuffer = configBuffer + ")";
        }
        for (int i=0; i < hardwareDeviceClass.lstCRServos.size(); i++)
        {
            configBuffer = configBuffer + ":CRServo";
            configBuffer = configBuffer + "(" + hardwareDeviceClass.lstCRServos.get(i).crServoName;

            if (hardwareDeviceClass.lstCRServos.get(i).direction == CRServo.Direction.FORWARD)
            {
                configBuffer = configBuffer + ", FORWARD)";
            }
            else
            {
                configBuffer = configBuffer + ", REVERSE)";
            }
        }
        for (int i=0; i < hardwareDeviceClass.lstServos.size(); i++)
        {
            configBuffer = configBuffer + ":Servo";
            configBuffer = configBuffer + "(" + hardwareDeviceClass.lstServos.get(i).servoName;
            configBuffer = configBuffer + ")";
        }
        for (int i=0; i < hardwareDeviceClass.lstCameras.size(); i++)
        {
            configBuffer = configBuffer + ":Camera";
            configBuffer = configBuffer + "(" + hardwareDeviceClass.lstCameras.get(i).configName;
            configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).isItWebcam;
            configBuffer = configBuffer + ")";

            for (int j=0; j < hardwareDeviceClass.lstCameras.get(i).lstObjectDetection.size(); j++)
            {
                if (hardwareDeviceClass.lstCameras.get(i).configName == hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().configName)
                {
                    configBuffer = configBuffer + ":Detection";
                    configBuffer = configBuffer + "(" + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().configName;
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().assetFile;
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().useCustomAsset;
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().GetLabelListDelimited();
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().cameraDirection;
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().flashlight;
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().minConfidence + "f";
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().durationMilliseconds;
                    configBuffer = configBuffer + ", " + hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().zoom;
                    configBuffer = configBuffer + ")";
                }
            }
        }

        configBuffer = configBuffer.substring(1); // Removes first colon
        LogMessage("CONFIG = " + configBuffer, 0);
    }

    /**
     * Logs device powers and positions to logcat
     */
    public void LogDevices()
    {
        logEnabled = true;

        if (initialLogEntries)
        {
            GetLogConfigInfo();

            initialLogEntries = false;
        }

        String logEntry = "";

        if (hardwareDeviceClass.HasDeviceValuesChanged())
        {
            double sum = 0;
            for (int i = 0; i < hardwareDeviceClass.lstDcMotors.size(); i++)
            {
                logEntry = logEntry + " " + hardwareDeviceClass.lstDcMotors.get(i).currentPower;
                sum += hardwareDeviceClass.lstDcMotors.get(i).actionTime;

                if (hardwareDeviceClass.lstDcMotors.get(i).useEncoder)
                {
                    logEntry = logEntry + " " + hardwareDeviceClass.lstDcMotors.get(i).GetCurrentPosition();
                }
            }

            for (int i = 0; i < hardwareDeviceClass.lstCRServos.size(); i++)
            {
                logEntry = logEntry + " " + hardwareDeviceClass.lstCRServos.get(i).currentPower;
                sum += hardwareDeviceClass.lstCRServos.get(i).actionTime;
            }

            for (int i = 0; i < hardwareDeviceClass.lstServos.size(); i++)
            {
                logEntry = logEntry + " " + hardwareDeviceClass.lstServos.get(i).currentPosition;
                sum += hardwareDeviceClass.lstServos.get(i).actionTime;
            }
            logEntry = logEntry.substring(1); // Removes first space
            LogMessage("MOVE " + logEntry, sum);
        }
    }

    /**
     * Logs any found detections
     */
    public void LogObjectDetection()
    {
        logEnabled = true;
        TelemetryLogDetection = true;

        if (initialLogEntries)
        {
            GetLogConfigInfo();

            initialLogEntries = false;
        }

        for (int i=0; i < hardwareDeviceClass.lstCameras.size(); i++)
        {
            if (hardwareDeviceClass.lstCameras.get(i).configName == hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().configName)
            {
                foundLabel = hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().GetLabel();
                int labelIndex = hardwareDeviceClass.lstCameras.get(i).UseObjectDetectionClass().GetLabelIndex(foundLabel) + 1;

                LogMessage("DETECT " + String.valueOf(labelIndex) + " " + foundLabel, 0);
            }
        }

        logDetection = false;
        logDevice = true;
    }

    /**
     * Shows telemetry on DS for Logging
     */
    public void ShowLogTelemetry()
    {
        if (logEnabled)
        {
            program.telemetry.addData("---------Status: LOGGING-----------", "");
            program.telemetry.addData("--------NOTE: Logging is on--------", "");
            if (TelemetryLogDetection)
            {
                program.telemetry.addData("Object Detection Label: ", foundLabel);
            }
            program.telemetry.update();
        }
    }

    /**
     * Added messages to logBuffer list
     */
    private void LogMessage(String Message, double ActionTime)
    {
        if (UseFileForLogging)
        {
            logBuffer.add(System.currentTimeMillis() + " " + ActionTime + " I/" + TAG + ": " + Message);
        }
        else
        {
            Log.i(TAG, Message);
        }
    }

    public void WriteFile()
    {
        if (program.isStopRequested())
        {
            ThreadClass threadClass = new ThreadClass();
            threadClass.CreateFileWithBackgroundThread(logBuffer, FilePath);
        }
    }
}