package org.firstinspires.ftc.teamcode.Custom.TeleOp.Examples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.HardwareDeviceClass;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.LogClass;


@TeleOp(name="ImportExample", group="Linear Opmode")
@Disabled
public class ImportExample extends LinearOpMode {

    //===============================================
    //Importing Current Program to use Custom Classes
    //NOTE: Look at the "DeviceExample" examples for some more explanation before this
    //===============================================



    //First you need to call the custom classes
    //If you want to just use the HardwareClass just call the HardwareClass
    //If you want to use both the Hardware and Logging classes, call them both
    HardwareDeviceClass hardwareDeviceClass = new HardwareDeviceClass(this);
    LogClass logClass = new LogClass("AHSLog", hardwareDeviceClass, true);



    //Instead of using multiple different variables for each device all we need are 2 variables. Its configName and a power/position variable
    //The except is Servo in which it also needs a min, max, and increment variable
    //Example of what to replace:
        //Replace private DcMotor dcMotor = null; with private String motor = "dcmotor";
        //This goes for every device
        private String motorConfigName = "dcmotor";
        private String servoConfigName = "servo";

        double motorPwr;

        double minpos = 0;
        double maxpos = 1;
        double increment = 0.1;
        double incrementVal;


    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();



        //Instead of doing creating a HardwareMap for each device, let the HardwareDeviceClass do it for you
        //Example
            //Replace DcMotor dcMotor = hardwareMap.get(DcMotor.class, "dcmotor");
            //with hardwareDeviceClass.SetMotorHardwareMap(motorConfigName, DcMotor.Direction.FORWARD, false);

            //This goes for every device
            hardwareDeviceClass.SetMotorHardwareMap(motorConfigName, DcMotor.Direction.FORWARD, false);
            hardwareDeviceClass.SetServoHardwareMap(servoConfigName, 100);



        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {


            //If you do not have every power/position set to 0 at the start of the while loop, you may encounter a bug
            //This bug being the robot moving without the controller being touched
            motorPwr = 0;
            incrementVal = 0;



            //Setting the Pwr variables using the controller(s) values
            if (gamepad1.right_stick_y != 0)
            {
                motorPwr = gamepad1.right_stick_y;
            }

            if (gamepad1.left_trigger > 0)
            {
                incrementVal = -increment;
            }
            else if (gamepad1.right_trigger > 0)
            {
                incrementVal = increment;
            }



            //Instead of setting the power directly to the device, let the HardwareDeviceClass handle it
            //Example
                //Replace dcmotor.setPower(motorPwr);
                //with hardwareDeviceClass.GetMotor(motorConfigName).SetPower(motorPwr);
                //This goes for every device that has a power/position
                hardwareDeviceClass.GetMotor(motorConfigName).SetPower(motorPwr);

                //For servos its a little bit different. The HardwareDeviceClass has a helper method for the min and max position
                //Example
                    //Replace the logic for min and max position
                    //with hardwareDeviceClass.GetServo(servoConfigName).SetPositionHelper(incrementVal, minpos, maxpos);
                    hardwareDeviceClass.GetServo(servoConfigName).SetPositionHelper(incrementVal, minpos, maxpos);
        }
    }
}