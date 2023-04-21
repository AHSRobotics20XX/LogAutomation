package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.io.*;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import com.vuforia.CameraDevice;
import java.util.List;

@Autonomous(name="FirstTest", group="Linear Opmode")
//@Disabled
public class FirstTest extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private String lastAction = "";

    private DcMotor dcmotorL = null;
    private DcMotor dcmotorR = null;
    private DcMotor dcmotorl = null;
    private DcMotor dcmotorr = null;
    private CRServo crservoextender = null;
    private Servo servopinch = null;

    @Override
    public void runOpMode()
    {
       telemetry.addData("Status", "Initialized");
       telemetry.update();

       dcmotorL = hardwareMap.get(DcMotor.class, "L");
       dcmotorL.setDirection(DcMotor.Direction.FORWARD);
       dcmotorR = hardwareMap.get(DcMotor.class, "R");
       dcmotorR.setDirection(DcMotor.Direction.REVERSE);
       dcmotorl = hardwareMap.get(DcMotor.class, "l");
       dcmotorl.setDirection(DcMotor.Direction.FORWARD);
       dcmotorr = hardwareMap.get(DcMotor.class, "r");
       dcmotorr.setDirection(DcMotor.Direction.REVERSE);
       crservoextender = hardwareMap.get(CRServo.class, "extender");
       crservoextender.setDirection(CRServo.Direction.FORWARD);
       servopinch = hardwareMap.get(Servo.class, "pinch");

       waitForStart();


       if (opModeIsActive())
       {
           DeleteRunPattern();
           runtime.reset();

           Move01();
           //END-RunOpModeCalls
           WriteRunPattern();
       }
    }
    //END-RunOpMode
    public void Move(double durationMilliseconds,double LPwr,double RPwr,double lPwr,double rPwr,double extenderPwr,double pinchPos)
    {
        ElapsedTime durationTime = new ElapsedTime();
        Boolean stillHaveTime = true;

        dcmotorL.setPower(LPwr);

        dcmotorR.setPower(RPwr);

        dcmotorl.setPower(lPwr);

        dcmotorr.setPower(rPwr);

        crservoextender.setPower(extenderPwr);

        servopinch.setPosition(pinchPos);

        durationTime.reset();
        while (opModeIsActive() && stillHaveTime)
        {
            stillHaveTime = durationTime.milliseconds() < durationMilliseconds;
        }
    }
    public void Move01()
    {
        lastAction = "M01";
        Move(40,0.0,-0.007,0.0,-0.007,0.0,0.0);
        Move(27,-0.408,-0.349,-0.408,-0.349,0.0,0.0);
        Move(47,-0.958,-0.591,-0.958,-0.591,0.0,0.0);
        Move(38,-0.633,-0.149,-0.633,-0.149,0.0,0.0);
        Move(28,-0.015,0.0,-0.015,0.0,0.0,0.0);
        Move(398,0.0,0.0,0.0,0.0,0.0,0.0);
        Move(22,-0.373,0.373,0.373,-0.373,0.0,0.0);
        Move(33,-0.835,0.835,0.835,-0.835,0.0,0.0);
        Move(42,-0.925,0.925,0.925,-0.925,0.0,0.0);
        Move(44,-0.941,0.941,0.941,-0.941,0.0,0.0);
        Move(49,-0.957,0.957,0.957,-0.957,0.0,0.0);
        Move(24,-0.89,0.89,0.89,-0.89,0.0,0.0);
        Move(42,-0.737,0.737,0.737,-0.737,0.0,0.0);
        Move(38,-0.227,0.227,0.227,-0.227,0.0,0.0);
        Move(311,0.0,0.0,0.0,0.0,0.0,0.0);
        Move(21,-0.369,0.369,0.369,-0.369,0.0,0.0);
        Move(47,-0.871,0.871,0.871,-0.871,0.0,0.0);
        Move(27,-1.0,1.0,1.0,-1.0,0.0,0.0);
        Move(35,-0.718,0.718,0.718,-0.718,0.0,0.0);
        Move(34,-0.114,0.114,0.114,-0.114,0.0,0.0);
        Move(0,0.0,0.0,0.0,0.0,0.0,0.0);
        Move(20,-0.174,-0.091,-0.174,-0.091,0.0,0.0);
        Move(37,-0.483,-0.216,-0.483,-0.216,0.0,0.0);
        Move(41,-0.533,-0.249,-0.533,-0.249,0.0,0.0);
        Move(40,-0.391,-0.257,-0.391,-0.257,0.0,0.0);
        Move(35,-0.274,-0.307,-0.274,-0.307,0.0,0.0);
        Move(44,-0.274,-0.324,-0.274,-0.324,0.0,0.0);
        Move(39,-0.408,-0.358,-0.408,-0.358,0.0,0.0);
        Move(41,-0.257,-0.416,-0.257,-0.416,0.0,0.0);
        Move(33,0.0,-0.449,0.0,-0.449,0.0,0.0);
        Move(120,0.0,-0.458,0.0,-0.458,0.0,0.0);
        Move(52,0.0,-0.483,0.0,-0.483,0.0,0.0);
        Move(67,0.0,-0.499,0.0,-0.499,0.0,0.0);
        Move(40,0.0,-0.516,0.0,-0.516,0.0,0.0);
        Move(55,0.0,-0.541,0.0,-0.541,0.0,0.0);
        Move(22,-0.049,-0.549,-0.049,-0.549,0.0,0.0);
        Move(41,-0.074,-0.549,-0.074,-0.549,0.0,0.0);
        Move(121,-0.082,-0.549,-0.082,-0.549,0.0,0.0);
        Move(39,-0.082,-0.558,-0.082,-0.558,0.0,0.0);
        Move(40,-0.408,-0.558,-0.408,-0.558,0.0,0.0);
        Move(39,-0.908,-0.558,-0.908,-0.558,0.0,0.0);
        Move(216,-1.0,-0.558,-1.0,-0.558,0.0,0.0);
        Move(27,-1.0,-0.583,-1.0,-0.583,0.0,0.0);
        Move(78,-1.0,-0.608,-1.0,-0.608,0.0,0.0);
        Move(96,-1.0,-0.616,-1.0,-0.616,0.0,0.0);
        Move(31,-0.85,-0.625,-0.85,-0.625,0.0,0.0);
        Move(41,-0.65,-0.633,-0.65,-0.633,0.0,0.0);
        Move(43,-0.683,-0.641,-0.683,-0.641,0.0,0.0);
        Move(43,-0.933,-0.675,-0.933,-0.675,0.0,0.0);
        Move(37,-1.0,-0.7,-1.0,-0.7,0.0,0.0);
        Move(37,-0.933,-0.708,-0.933,-0.708,0.0,0.0);
        Move(38,-0.499,-0.716,-0.499,-0.716,0.0,0.0);
        Move(37,-0.199,-0.716,-0.199,-0.716,0.0,0.0);
        Move(42,-0.174,-0.716,-0.174,-0.716,0.0,0.0);
        Move(37,-0.149,-0.716,-0.149,-0.716,0.0,0.0);
        Move(38,-0.049,-0.716,-0.049,-0.716,0.0,0.0);
        Move(96,0.0,-0.716,0.0,-0.716,0.0,0.0);
        Move(67,0.0,-0.725,0.0,-0.725,0.0,0.0);
        Move(39,0.0,-0.741,0.0,-0.741,0.0,0.0);
        Move(58,0.0,-0.783,0.0,-0.783,0.0,0.0);
        Move(23,-0.116,-0.791,-0.116,-0.791,0.0,0.0);
        Move(36,-0.316,-0.791,-0.316,-0.791,0.0,0.0);
        Move(47,-0.332,-0.791,-0.332,-0.791,0.0,0.0);
        Move(40,-0.491,-0.8,-0.491,-0.8,0.0,0.0);
        Move(39,-0.608,-0.733,-0.608,-0.733,0.0,0.0);
        Move(42,-0.132,-0.374,-0.132,-0.374,0.0,0.0);
        Move(382,0.0,0.0,0.0,0.0,0.0,0.0);
        Move(31,-0.094,0.094,0.094,-0.094,0.0,0.0);
        Move(35,-0.227,0.227,0.227,-0.227,0.0,0.0);
        Move(36,-0.31,0.31,0.31,-0.31,0.0,0.0);
        Move(35,-0.4,0.4,0.4,-0.4,0.0,0.0);
        Move(43,-0.506,0.506,0.506,-0.506,0.0,0.0);
        Move(37,-0.565,0.565,0.565,-0.565,0.0,0.0);
        Move(80,-0.592,0.592,0.592,-0.592,0.0,0.0);
        Move(39,-0.627,0.627,0.627,-0.627,0.0,0.0);
        Move(42,-0.69,0.69,0.69,-0.69,0.0,0.0);
        Move(42,-0.71,0.71,0.71,-0.71,0.0,0.0);
        Move(37,-0.733,0.733,0.733,-0.733,0.0,0.0);
        Move(67,-0.753,0.753,0.753,-0.753,0.0,0.0);
        Move(17,-0.773,0.773,0.773,-0.773,0.0,0.0);
        Move(41,-0.792,0.792,0.792,-0.792,0.0,0.0);
        Move(275,-0.796,0.796,0.796,-0.796,0.0,0.0);
        Move(189,-0.8,0.8,0.8,-0.8,0.0,0.0);
        Move(290,-0.804,0.804,0.804,-0.804,0.0,0.0);
        Move(39,-0.831,0.831,0.831,-0.831,0.0,0.0);
        Move(123,-0.835,0.835,0.835,-0.835,0.0,0.0);
        Move(62,-0.8,0.8,0.8,-0.8,0.0,0.0);
        Move(16,-0.733,0.733,0.733,-0.733,0.0,0.0);
        Move(40,-0.616,0.616,0.616,-0.616,0.0,0.0);
        Move(39,-0.118,0.118,0.118,-0.118,0.0,0.0);
        Move(0,0.0,0.0,0.0,0.0,0.0,0.0);
    }//END-Move01
    public void DeleteRunPattern()
    {
        File file = new File("/storage/emulated/0/FIRST/CustomLog/ActionPattern/FirstTest_ActionPattern.txt");

        if (file.exists())
        {
            file.delete();
        }
    }
    public void WriteRunPattern()
    {
        if (lastAction != null)
        {
            try
            {
                FileWriter fileWriter = new FileWriter("/storage/emulated/0/FIRST/CustomLog/ActionPattern/FirstTest_ActionPattern.txt");
                fileWriter.write("RUNPATTERN " + lastAction);
                fileWriter.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could Not Write To File");
            }
        }
    }
}
