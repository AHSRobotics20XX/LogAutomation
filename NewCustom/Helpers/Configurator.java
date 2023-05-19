package org.firstinspires.ftc.teamcode.Custom.Helpers;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Custom.BaseClasses.Camera;
import org.firstinspires.ftc.teamcode.Custom.BaseClasses.HardwareDevice;
import org.firstinspires.ftc.teamcode.Custom.Devices.CameraDevices._PhoneCamera;
import org.firstinspires.ftc.teamcode.Custom.Devices.CameraDevices._WebCam;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._CRServo;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._DcMotor;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._DcMotorEncoder;
import org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices._Servo;
import org.firstinspires.ftc.teamcode.Custom.EnvironmentConst;

import java.util.ArrayList;

public class Configurator
{
    private static final Configurator instance = new Configurator();
    private Configurator() {}

    public ArrayList<Camera> cameraDevices = new ArrayList<>();
    public ArrayList<HardwareDevice> hardwareDevices = new ArrayList<>();
    public static boolean Debug;
    public String DebugTag = "ConfigDebug";

    private EnvironmentConst environmentConst = EnvironmentConst.getInstance();
    private Logger logger = Logger.getInstance();
    private static LinearOpMode program;
    private boolean logInitialize = true;
    private boolean isLogOn = false;

    public static Configurator getInstance(LinearOpMode CurrentClass, boolean DebugMode) {
        program = CurrentClass;
        Debug = DebugMode;
        return instance;
    }

    //HardwareMaps for each device

    public void SetDcMotorHardwareMap(String ConfigName, DcMotor.Direction Direction) {
        _DcMotor device = new _DcMotor(program, ConfigName, Direction);

        if (Debug) {
            device.DebugMode(DebugTag);
        }

        device.SetHardwareMap();
        hardwareDevices.add(device);
    }

    public void SetDcMotorEncoderHardwareMap(String ConfigName, DcMotor.Direction Direction) {
        _DcMotorEncoder device = new _DcMotorEncoder(program, ConfigName, Direction);

        if (Debug) {
            device.DebugMode(DebugTag);
        }

        device.SetHardwareMap();
        hardwareDevices.add(device);
    }

    public void SetCRServoHardwareMap(String ConfigName, CRServo.Direction Direction) {
        _CRServo device = new _CRServo(program, ConfigName, Direction);

        if (Debug) {
            device.DebugMode(DebugTag);
        }

        device.SetHardwareMap();
        hardwareDevices.add(device);
    }

    public void SetServoHardwareMap(String ConfigName, double MinValue, double MaxValue, boolean RoundValue) {
        _Servo device = new _Servo(program, ConfigName, MinValue, MaxValue, RoundValue);

        if (Debug) {
            device.DebugMode(DebugTag);
        }

        device.SetHardwareMap();
        hardwareDevices.add(device);
    }

    public void SetPhoneCameraHardwareMap(String ConfigName) {
        _PhoneCamera device = new _PhoneCamera(program, ConfigName);

        if (Debug) {
            device.DebugMode(DebugTag);
        }

        device.SetHardwareMap();
        cameraDevices.add(device);
    }

    public void SetWebCamHardwareMap(String ConfigName) {
        _WebCam device = new _WebCam(program, ConfigName);

        if (Debug) {
            device.DebugMode(DebugTag);
        }

        device.SetHardwareMap();
        cameraDevices.add(device);
    }

    //Getters for each device

    public _DcMotor GetDcMotor(String Name) {
        for (HardwareDevice device : hardwareDevices) {
            if (device instanceof _DcMotor && device.getName().equals(Name)) {
                return (_DcMotor)device;
            }
        }
        return null;
    }
    public _DcMotorEncoder GetDcMotorEncoder(String Name) {
        for (HardwareDevice device : hardwareDevices) {
            if (device instanceof _DcMotorEncoder && device.getName().equals(Name)) {
                return (_DcMotorEncoder) device;
            }
        }
        return null;
    }

    public _CRServo GetCRServo(String Name) {
        for (HardwareDevice device : hardwareDevices) {
            if (device instanceof _CRServo && device.getName().equals(Name)) {
                return (_CRServo) device;
            }
        }
        return null;
    }

    public _Servo GetServo(String Name) {
        for (HardwareDevice device : hardwareDevices) {
            if (device instanceof _Servo && device.getName().equals(Name)) {
                return (_Servo) device;
            }
        }
        return null;
    }

    public _PhoneCamera GetPhoneCamera(String Name) {
        for (Camera device : cameraDevices) {
            if (device instanceof _PhoneCamera && device.getName().equals(Name)) {
                return (_PhoneCamera) device;
            }
        }
        return null;
    }

    public _WebCam GetWebCam(String Name) {
        for (Camera device : cameraDevices) {
            if (device instanceof _WebCam && device.getName().equals(Name)) {
                return (_WebCam) device;
            }
        }
        return null;
    }

    public void TurnLogOn() {
        isLogOn = true;
    }
    public void TurnLogOff() {
        isLogOn = false;
    }
    public void ToggleLog() {
        isLogOn = !isLogOn;
    }

    public void LogIfOn(boolean ShowTelemetry) {
        if (isLogOn) {
            if (logInitialize) {
                logger.Initialize(program, this, environmentConst.CreateFileForLog, environmentConst.Directory, environmentConst.LogTag);
                logInitialize = false;
            }

            logger.LogLogic();
            if (ShowTelemetry) {
                logger.ShowLogTelemetry();
            }
            logger.WriteFile();
        }
    }

    public boolean HasDeviceValuesChanged() {
        boolean hasChanged = false;
        for (HardwareDevice device : hardwareDevices) {
            if (device instanceof _DcMotor) {
                hasChanged = hasChanged || ((_DcMotor) device).ValueHasChanged();
            }
            else if (device instanceof _DcMotorEncoder) {
                hasChanged = hasChanged || ((_DcMotorEncoder) device).ValueHasChanged();
            }
            else if (device instanceof _CRServo) {
                hasChanged = hasChanged || ((_CRServo) device).ValueHasChanged();
            }
            else if (device instanceof _Servo) {
                hasChanged = hasChanged || ((_Servo) device).ValueHasChanged();
            }
        }
        return hasChanged;
    }
}