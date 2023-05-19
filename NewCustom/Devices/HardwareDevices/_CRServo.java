package org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Custom.BaseClasses.HardwareDevice;

public class _CRServo extends HardwareDevice
{
    private CRServo crServo = null;
    private CRServo.Direction direction;

    public _CRServo(LinearOpMode Program, String Name, CRServo.Direction Direction) {
        super(Program, Name);
        direction = Direction;
    }

    @Override
    public void runOpMode() throws InterruptedException {}

    @Override
    public void SetHardwareMap() {
        crServo = program.hardwareMap.get(CRServo.class, getName());
        crServo.setDirection(direction);

        if (debugDevice) {
            Log.i(debugTag, "Device " + getName() + " has been created.");
        }
    }

    @Override
    public double GetValue() {
        return crServo.getPower();
    }

    @Override
    public void SetValue(double Value) {
        ElapsedTime timer = new ElapsedTime();
        double power = RoundDecimals(Value);

        previousValue = currentValue;
        currentValue = power;

        timer.reset();
        if (currentValue != previousValue) {
            crServo.setPower(currentValue);
        }
        actionTime = timer.milliseconds();

        if (debugDevice) {
            if (currentValue != previousValue) {
                Log.i(debugTag, "Setting " + getName() + " value to " + currentValue + ". This took " + actionTime + "milliseconds");
            }
        }
    }

//    @Override
//    public boolean ValueHasChanged() {
//        return (previousValue != currentValue);
//    }

    public CRServo.Direction GetDirection() {
        return direction;
    }
}
