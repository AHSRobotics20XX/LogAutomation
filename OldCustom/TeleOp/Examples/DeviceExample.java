package org.firstinspires.ftc.teamcode.Custom.TeleOp.Examples;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.HardwareDeviceClass;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.LogClass;


@TeleOp(name="DeviceExample", group="Linear Opmode")
@Disabled
public class DeviceExample extends LinearOpMode {

    //Required: Calls the custom classes
    //HardwareDeviceClass needs a reference to this class to make device objects that the DC/RC apps will recognize
    //LogClass needs a tag which will be used to filter messages in logcat, and the variable for HardwareDeviceClass.
    //  - This tag is the same logcat filter tag you created during the setup steps
    HardwareDeviceClass hardwareDeviceClass = new HardwareDeviceClass(this);
    //LogClass logClass = new LogClass("AHSLog", hardwareDeviceClass);

    //Since the HardwareDeviceClass creates the objects for each device, all we need is a configName
    //This name will be what you use in the DS/RC configuration
    private String motor = "dcmotor";
    private String servo = "servo";
    private String crservo = "crservo";
    private String camera = "camera";

    //If you are using ObjectDetection, you can also create the asset file name / labels list
    private String assetFile = "PowerPlay.tflite";
    private String[] labels = {"1 Bolt","2 Bulb","3 Panel"};

    //For each device we need to make a variable to hold their powers
    //Note: Servos need a min, max, increment, and tempIncrementVal instead if a power variable
    double motorPwr;
    double crservoPwr;

    double minpos = 0;
    double maxpos = .14;
    double increment = 0.01;
    double incrementVal;


    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Each device has its own HardwareMap using the HardwareDeviceClass
        //Note: When setting direction, it may try to use DcMotorSimple instead of the device type
        //I would recommend changing it back to the correct device type

            //For DcMotors the parameters are configName, Direction, and UseEncoder
            //If you want to use an encoder, set UseEncoder to true
            hardwareDeviceClass.SetMotorHardwareMap(motor, DcMotor.Direction.FORWARD, false);

            //For Servo the parameters are configName, and MillisecondDelay
            //Since the "setposition" is called in the while loop the millisecond delay makes the calls more reliable in timing.
            hardwareDeviceClass.SetServoHardwareMap(servo, 100);

            //For CSServo the parameters are configName, and Direction
            hardwareDeviceClass.SetCRServoHardwareMap(crservo, CRServo.Direction.FORWARD);

            //For Camera the parameters are configName, and Webcam
            //If the camera you are using is a webcam then set Webcam to true
            //If its a built-in phone camera, set Webcam to false
            hardwareDeviceClass.SetCameraHardwareMap(camera, false);

                //If you are using Object Detection you need to specify which camera you are using for that detection
                //Then setup the ObjectDetection with the parameters CustomAsset, AssetFileName, and Labels
                //If you are using a custom object detection asset, set CustomAsset to true
                hardwareDeviceClass.GetCamera(camera).SetupObjectDetection(false, assetFile, labels);

                //If you would like to change the parameters for object detection you need to specify the specific camera
                //Then use SetParameters with the parameters CameraDirection, MinConfidence, Flashlight, DetectTime, and Zoom
                //The minimum confidence is a float so it needs a "f" at the end
                //If you have the capability of using a flashlight want to use it, set to true
                    //Note: Flashlight is disabled on webcams. Just put false
                //To prevent the ObjectDetection from being stuck in searching, the DetectTime is the max time it will look for (in milliseconds)
                //Zoom is how much the camera will zoom
                hardwareDeviceClass.GetCamera(camera).UseObjectDetectionClass().SetParameters(VuforiaLocalizer.CameraDirection.FRONT, 0.75f, false, 5000, 1);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            //Set all powers and positions to 0 to remove the bug of setting power without using controller
            motorPwr = 0;
            crservoPwr = 0;
            incrementVal = 0;

            //Setting the Pwr variables using the controller(s) values
            if (gamepad1.right_stick_y != 0)
            {
                motorPwr = gamepad1.right_stick_y;
            }

            if (gamepad1.left_stick_y != 0)
            {
                crservoPwr = gamepad1.left_stick_y;
            }

            if (gamepad1.left_trigger > 0)
            {
                incrementVal = -increment;
            }
            else if (gamepad1.right_trigger > 0)
            {
                incrementVal = increment;
            }

            //For setting power and position to devices you need to specify the device
            //Then use SetPower or SetPositionHelper
            //SetPower parameters are PowerValue
            //SetPositionHelper parameters are tempIncrementVal, MinPosition, and MaxPosition
            hardwareDeviceClass.GetMotor(motor).SetPower(motorPwr);
            hardwareDeviceClass.GetCRServo(crservo).SetPower(crservoPwr);
            hardwareDeviceClass.GetServo(servo).SetPositionHelper(incrementVal,minpos,maxpos);
        }
    }
}