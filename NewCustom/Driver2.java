package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Custom.Helpers.Configurator;


@TeleOp(name="2 Drivers New", group="Linear Opmode")
//@Disabled
public class Driver2 extends LinearOpMode {
    Configurator configurator = Configurator.getInstance(this, true);

    String motor1 = "m1";
    String motor2Enc = "m2";
    String crservo1 = "cr1";
    String servo1 = "s1";
    String camera = "camera";

    double pm1;
    double pm2;
    double cr1;

    private String assetFile = "PowerPlay.tflite";
    private String[] labels = {"1 Bolt","2 Bulb","3 Panel"};

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        configurator.SetDcMotorHardwareMap(motor1, DcMotorSimple.Direction.REVERSE);
        configurator.SetDcMotorEncoderHardwareMap(motor2Enc, DcMotorSimple.Direction.FORWARD);
        configurator.SetCRServoHardwareMap(crservo1, DcMotorSimple.Direction.FORWARD);
        configurator.SetServoHardwareMap(servo1, 0, 0.8, true);
        configurator.SetPhoneCameraHardwareMap(camera);
        configurator.GetPhoneCamera(camera).EnableObjectDetection(assetFile, labels, VuforiaLocalizer.CameraDirection.BACK, 0.70f, 1);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            Gamepad gamepadPrimary = gamepad1;

            pm1 = 0;
            pm2 = 0;
            cr1 = 0;

            if (gamepadPrimary.left_stick_y > 0) {
                pm1 = gamepadPrimary.left_stick_y;
            }
            else if (gamepadPrimary.left_stick_y < 0) {
                pm2 = gamepadPrimary.left_stick_y;
            }

            if (gamepadPrimary.left_stick_x > 0) {
                configurator.GetServo(servo1).IncrementValue(-gamepadPrimary.left_stick_x);
            }
            else if (gamepadPrimary.left_stick_x < 0) {
                configurator.GetServo(servo1).DecrementValue(-gamepadPrimary.left_stick_x);
            }

            if (gamepadPrimary.x) {
                cr1 = 1;
            }
            else if (gamepadPrimary.y) {
                cr1 = -1;
            }

            if (gamepadPrimary.x) {
                configurator.GetPhoneCamera(camera).UseObjectDetection().StartVuforiaAndTensorFlow();
            }

            if (gamepadPrimary.a && gamepadPrimary.b) {
                configurator.TurnLogOn();
            }

            configurator.GetDcMotor(motor1).SetValue(pm1);
            configurator.GetDcMotorEncoder(motor2Enc).SetValue(pm2);
            configurator.GetCRServo(crservo1).SetValue(cr1);

            configurator.LogIfOn(true);
        }
    }
}