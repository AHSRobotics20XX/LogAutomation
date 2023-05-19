package org.firstinspires.ftc.teamcode.Custom.Devices.HardwareDevices;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Custom.BaseClasses.HardwareDevice;

public class _Servo extends HardwareDevice {
    private Servo servo = null;
    private double minValue;
    private double maxValue;
    private boolean roundValue;
    private double localValue;

    public _Servo(LinearOpMode Program, String Name, double MinValue, double MaxValue, boolean RoundValue) {
        super(Program, Name);
        minValue = MinValue;
        maxValue = MaxValue;
        roundValue = RoundValue;
    }

    @Override
    public void runOpMode() throws InterruptedException {}

    @Override
    public void SetHardwareMap() {
        servo = program.hardwareMap.get(Servo.class, getName());

        if (debugDevice) {
            Log.i(debugTag, "Device " + getName() + " has been created.");
        }
    }

    @Override
    public double GetValue() {
        return servo.getPosition();
    }

    @Override
    public void SetValue(double Value) {
        ElapsedTime timer = new ElapsedTime();
        localValue = 0;

        double power;
        if (roundValue) {
            power = RoundDecimals(Value);
        }
        else {
            power = Value;
        }

        previousValue = currentValue;
        currentValue = power;

        timer.reset();
        if (currentValue != previousValue) {
            servo.setPosition(currentValue);
        }
        actionTime = timer.milliseconds();

        if (debugDevice) {
            if (currentValue != previousValue) {
                Log.i(debugTag, "Setting " + getName() + " value to " + currentValue + ". This took " + actionTime + "milliseconds");
            }
        }
    }

    public void IncrementValue(double Increment) {
        localValue = CheckIncrementValue(Increment);
        SetPositionHelper(localValue);
    }
    public void DecrementValue(double Increment) {
        localValue = CheckDecrementValue(Increment);
        SetPositionHelper(localValue);
    }

    private double CheckIncrementValue(double Value) {
        double temp;
        if (Value > 0) {
            temp = Value;
        }
        else if (Value < 0) {
            temp = -Value;
        }
        else {
            temp = 0;
        }
        return temp;
    }
    private double CheckDecrementValue(double Value) {
        double temp;
        if (Value > 0) {
            temp = -Value;
        }
        else if (Value < 0) {
            temp = Value;
        }
        else {
            temp = 0;
        }
        return temp;
    }

    private void SetPositionHelper(double IncrementValue) {
        if (IncrementValue != 0) {
            double newPosition = currentValue + IncrementValue;

            if (newPosition > maxValue) {
                SetValue(maxValue);
            }
            else if (newPosition < minValue) {
                SetValue(minValue);
            }
            else {
                SetValue(newPosition);
            }
        }
    }
}
