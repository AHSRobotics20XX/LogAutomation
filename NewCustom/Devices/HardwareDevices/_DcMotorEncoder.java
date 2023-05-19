package org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Custom.BaseClasses.HardwareDevice;

public class _DcMotorEncoder extends HardwareDevice
{
    private DcMotor dcMotor = null;
    private DcMotorSimple.Direction direction;

    public _DcMotorEncoder(LinearOpMode Program, String Name, DcMotorSimple.Direction Direction) {
        super(Program, Name);
        direction = Direction;
    }

    @Override
    public void runOpMode() throws InterruptedException {}

    @Override
    public void SetHardwareMap() {
        dcMotor = program.hardwareMap.get(com.qualcomm.robotcore.hardware.DcMotor.class, getName());
        dcMotor.setDirection(direction);

        if (debugDevice) {
            Log.i(debugTag, "Device " + getName() + " has been created.");
        }

        if (!isStarted()) {
            dcMotor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            dcMotor.setMode(com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    @Override
    public void SetValue(double Value) {
        ElapsedTime timer = new ElapsedTime();
        double power = RoundDecimals(Value);

        previousValue = currentValue;
        currentValue = power;

        timer.reset();
        if (currentValue != previousValue) {
            dcMotor.setPower(currentValue);
        }
        actionTime = timer.milliseconds();

        if (debugDevice) {
            if (currentValue != previousValue) {
                Log.i(debugTag, "Setting " + getName() + " value to " + currentValue + ". This took " + actionTime + "milliseconds");
                Log.i(debugTag, "Device " + getName() + " encoder value is now " + GetCurrentPosition());
            }
        }
    }

//    @Override
//    public boolean ValueHasChanged() {
//        return (previousValue != currentValue);
//    }

    @Override
    public double GetValue() {
        return dcMotor.getPower();
    }

    public int GetCurrentPosition() {
        return dcMotor.getCurrentPosition();
    }

    public DcMotorSimple.Direction GetDirection() {
        return direction;
    }
}
