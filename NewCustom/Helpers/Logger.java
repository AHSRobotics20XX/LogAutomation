package org.firstinspires.ftc.teamcode.Custom.Helpers;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Custom.BaseClasses.Camera;
import org.firstinspires.ftc.teamcode.Custom.BaseClasses.HardwareDevice;
import org.firstinspires.ftc.teamcode.Custom.Devices.CameraDevices._WebCam;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._CRServo;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._DcMotor;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._DcMotorEncoder;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._Servo;

import java.util.ArrayList;

public class Logger {
    private static final Logger instance = new Logger();
    private Logger() {}
    public static Logger getInstance() {
        return instance;
    }

    FileIOClass fileIOClass = new FileIOClass();
    CustomMethods customMethods = new CustomMethods();
    Configurator configurator;


    private LinearOpMode program;
    private boolean useFileForLogging;
    private boolean initialLogEntries = true;
    private ArrayList<String> logBuffer = new ArrayList<>();
    private boolean initialObjDetectionEntry = true;
    private boolean objDetectLabelFound = false;
    private String foundLabel;
    private int foundLabelIndex;
    private String logTag;
    private String fullFilePath;

    public boolean logStarted = false;

    public void Initialize(LinearOpMode Program, Configurator configurator, boolean UseFileForLogging, String Directory, String LogTag) {
        program = Program;
        useFileForLogging = UseFileForLogging;
        this.configurator = configurator;
        logTag = LogTag;

        fullFilePath = Directory + "/CustomLog/RecordingLog.txt";

        customMethods.CheckDirectories(Directory);
        if (fileIOClass.FileExists(fullFilePath)) {
            fileIOClass.FileDelete(fullFilePath);
        }

        if (configurator.Debug) {
            Log.i(configurator.DebugTag, "Logger has been Initialized");
        }
    }


    private void CreateConfigLine()
    {
        String configBuffer = "";

        for (HardwareDevice device : configurator.hardwareDevices) {
            if (device instanceof _DcMotor) {
                configBuffer = configBuffer + ":DcMotor";
                configBuffer = configBuffer + "(" + device.getName();

                if (((_DcMotor) device).GetDirection() == DcMotor.Direction.FORWARD) {
                    configBuffer = configBuffer + ", FORWARD";
                }
                else {
                    configBuffer = configBuffer + ", REVERSE";
                }
                configBuffer = configBuffer + ", false)";
            }
            else if (device instanceof _DcMotorEncoder) {
                configBuffer = configBuffer + ":DcMotor";
                configBuffer = configBuffer + "(" + device.getName();

                if (((_DcMotorEncoder) device).GetDirection() == DcMotor.Direction.FORWARD) {
                    configBuffer = configBuffer + ", FORWARD";
                }
                else {
                    configBuffer = configBuffer + ", REVERSE";
                }
                configBuffer = configBuffer + ", true)";
            }
            else if (device instanceof _CRServo) {
                configBuffer = configBuffer + ":CRServo";
                configBuffer = configBuffer + "(" + device.getName();

                if (((_CRServo) device).GetDirection() == CRServo.Direction.FORWARD) {
                    configBuffer = configBuffer + ", FORWARD";
                }
                else {
                    configBuffer = configBuffer + ", REVERSE";
                }
                configBuffer = configBuffer + ")";
            }
            else if (device instanceof _Servo) {
                configBuffer = configBuffer + ":Servo";
                configBuffer = configBuffer + "(" + device.getName();
                configBuffer = configBuffer + ")";
            }
        }

        for (Camera device : configurator.cameraDevices) {
            configBuffer = configBuffer + ":Camera";
            configBuffer = configBuffer + "(" + device.getName();

            if (device instanceof _WebCam) {
                configBuffer = configBuffer + ", true";
            }
            else {
                configBuffer = configBuffer + ", false";
            }
            configBuffer = configBuffer + ")";

            if (device.UseObjectDetection() != null)
            {
                configBuffer = configBuffer + ":Detection";
                configBuffer = configBuffer + "(" + device.getName();
                configBuffer = configBuffer + ", " + device.UseObjectDetection().GetAssetFile();
                configBuffer = configBuffer + ", " + device.UseObjectDetection().GetCustomAsset();
                configBuffer = configBuffer + ", " + device.UseObjectDetection().GetLabelDelimited();
                configBuffer = configBuffer + ", " + device.UseObjectDetection().GetCameraDirection();
                configBuffer = configBuffer + ", false"; //TODO Removed Flashlight From List
                configBuffer = configBuffer + ", " + device.UseObjectDetection().GetConfidence() + "f";
                configBuffer = configBuffer + ", " + device.UseObjectDetection().GetDuration();
                configBuffer = configBuffer + ", " + device.UseObjectDetection().GetZoom();
                configBuffer = configBuffer + ")";
            }
        }

        configBuffer = configBuffer.substring(1); // Removes first colon
        LogMessage("CONFIG = " + configBuffer, 0);
    }

    public void LogLogic()
    {
        logStarted = true;

        if (initialLogEntries) {
            CreateConfigLine();
            initialLogEntries = false;
        }

        String logEntry = "";

        if (configurator.HasDeviceValuesChanged())
        {
            double sum = 0;

            for (HardwareDevice device : configurator.hardwareDevices) {
                if (device instanceof _DcMotor) {
                    logEntry = logEntry + " " + device.currentValue;
                    sum += device.actionTime;
                }
                else if (device instanceof _DcMotorEncoder) {
                    logEntry = logEntry + " " + device.currentValue;
                    logEntry = logEntry + " " + ((_DcMotorEncoder) device).GetCurrentPosition();
                    sum += device.actionTime;
                }
                else if (device instanceof _CRServo) {
                    logEntry = logEntry + " " + device.currentValue;
                    sum += device.actionTime;
                }
                else if (device instanceof _Servo) {
                    logEntry = logEntry + " " + device.currentValue;
                    sum += device.actionTime;
                }
            }

            logEntry = logEntry.substring(1); // Removes first space
            LogMessage("MOVE " + logEntry, sum);
        }

        for (Camera device : configurator.cameraDevices) {
            if (device.UseObjectDetection() != null) {
                if (device.UseObjectDetection().logHasDetected) {
                    if (initialObjDetectionEntry) {
                        foundLabel = device.UseObjectDetection().GetFoundLabel();
                        foundLabelIndex = device.UseObjectDetection().labelIndex;
                        LogMessage("DETECT " + foundLabelIndex + " " + foundLabel, 0);
                        objDetectLabelFound = true;
                        initialObjDetectionEntry = false;
                    }
                }
            }
        }
    }

    private void LogMessage(String Message, double ActionTime)
    {
        if (useFileForLogging) {
            logBuffer.add(System.currentTimeMillis() + " " + ActionTime + " I/" + logTag + ": " + Message);
        }
        else {
            Log.i(logTag, Message);
        }
    }

    public void WriteFile() {
        if (program.isStopRequested())
        {
            configurator.TurnLogOff();
            ThreadClass threadClass = new ThreadClass();
            threadClass.CreateFileWithBackgroundThread(logBuffer, fullFilePath, configurator.DebugTag, configurator.Debug);
        }
    }

    public void ShowLogTelemetry() {
        program.telemetry.addData("---------Status: LOGGING-----------", "");
        program.telemetry.addData("--------NOTE: Logging is on--------", "");
        if (objDetectLabelFound) {
            program.telemetry.addData("Object Detection Label: ", foundLabel);
        }
        program.telemetry.update();
    }

}
