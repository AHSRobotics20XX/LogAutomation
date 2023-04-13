package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.HardwareDeviceClass;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.LogClass;


@TeleOp(name="2 Drivers", group="Linear Opmode")
//@Disabled
public class Driver2 extends LinearOpMode {

    HardwareDeviceClass hardwareDeviceClass = new HardwareDeviceClass(this);
    LogClass logClass = new LogClass("AHSLog", hardwareDeviceClass, true);

    private String lF = "L";
    private String rF = "R";
    private String lB = "l";
    private String rB = "r";
    private String arm = "arm";
    private String pinch = "pinch";
    private String extender = "extender";
    private String camera = "camera";

    private String assetFile = "PowerPlay.tflite";
    private String[] labels = {"1 Bolt","2 Bulb","3 Panel"};

    double powLf;
    double powRf;
    double powLb;
    double powRb;
    double powArm;
    double powExtender;

    double minpos = 0;
    double maxpos = .14;
    double increment = 0.01;
    double incrementVal;


    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        hardwareDeviceClass.SetMotorHardwareMap(lF, DcMotor.Direction.FORWARD, false);
        hardwareDeviceClass.SetMotorHardwareMap(rF, DcMotor.Direction.REVERSE,false);
        hardwareDeviceClass.SetMotorHardwareMap(lB, DcMotor.Direction.FORWARD,false);
        hardwareDeviceClass.SetMotorHardwareMap(rB, DcMotor.Direction.REVERSE,false);
        //hardwareDeviceClass.SetMotorHardwareMap(arm, DcMotor.Direction.FORWARD,false);
        hardwareDeviceClass.SetCRServoHardwareMap(extender, CRServo.Direction.FORWARD);
        hardwareDeviceClass.SetServoHardwareMap(pinch,100);

        hardwareDeviceClass.SetCameraHardwareMap(camera, false);
        hardwareDeviceClass.GetCamera(camera).SetupObjectDetection(false, assetFile, labels);
        hardwareDeviceClass.GetCamera(camera).UseObjectDetectionClass().SetParameters(VuforiaLocalizer.CameraDirection.FRONT, 0.75f, false, 5000, 1);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            powLf = 0;
            powRf = 0;
            powLb = 0;
            powRb = 0;
            powArm = 0;
            powExtender = 0;
            incrementVal = 0;

            SetDriverValues();
            SetSecondaryValues();

            Log();
        }
    }

    public void SetDriverValues()
    {
        Gamepad gamepadPrimary = gamepad1;

        if (gamepadPrimary.right_trigger > 0)  // Strafe to Right
        {
            powLf = -gamepadPrimary.right_trigger;
            powLb = gamepadPrimary.right_trigger;
            powRf = gamepadPrimary.right_trigger;
            powRb = -gamepadPrimary.right_trigger;
        }
        else //Right trigger not being pressed
        {
            if (gamepadPrimary.left_trigger > 0) // Strafe to Left
            {
                powLf = gamepadPrimary.left_trigger;
                powLb = -gamepadPrimary.left_trigger;
                powRf = -gamepadPrimary.left_trigger;
                powRb = gamepadPrimary.left_trigger;
            }
            else //Left trigger not being pressed
            {
                if (gamepadPrimary.left_stick_y != 0)
                {
                    powLf = gamepadPrimary.left_stick_y;
                    powLb = gamepadPrimary.left_stick_y;
                }

                if (gamepadPrimary.right_stick_y != 0)
                {
                    powRf = gamepadPrimary.right_stick_y;
                    powRb = gamepadPrimary.right_stick_y;
                }
            }
        }

        hardwareDeviceClass.GetMotor(lF).SetPower(powLf);
        hardwareDeviceClass.GetMotor(rF).SetPower(powRf);
        hardwareDeviceClass.GetMotor(lB).SetPower(powLb);
        hardwareDeviceClass.GetMotor(rB).SetPower(powRb);
    }

    public void SetSecondaryValues()
    {
        Gamepad gamepadSecondary = gamepad2;

        if (gamepadSecondary.right_stick_y!=0)
        {
            powArm = gamepadSecondary.right_stick_y/2;
        }

        if (gamepadSecondary.left_trigger>0)
        {
            incrementVal = -increment;
        }
        else if (gamepadSecondary.right_trigger>0)
        {
            incrementVal = increment;
        }

        if (gamepadSecondary.left_stick_y!=0)
        {
            powExtender=-gamepadSecondary.left_stick_y;
        }

        //hardwareDeviceClass.GetMotor(arm).SetPower(powArm);
        hardwareDeviceClass.GetCRServo(extender).SetPower(powExtender);
        hardwareDeviceClass.GetServo(pinch).SetPositionHelper(incrementVal,minpos,maxpos);
    }

    //=======================================================================


    public void Log()
    {
        if (gamepad1.left_bumper && gamepad1.right_bumper)
        {
            logClass.logDevice = true;
        }
        if (gamepad1.a && gamepad1.b)
        {
            hardwareDeviceClass.GetCamera(camera).UseObjectDetectionClass().UseVuforiaAndTensorFlow();
            logClass.logDetection = true;
        }

        if (logClass.logDevice)
        {
            logClass.LogDevices();
        }
        if (logClass.logDetection)
        {
            logClass.LogObjectDetection();
        }

        logClass.ShowLogTelemetry();
    }
}