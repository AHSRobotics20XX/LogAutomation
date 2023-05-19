package org.firstinspires.ftc.teamcode.Custom.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class HardwareDevice extends Device
{
    private int DecimalPlaces = 3;

    public double currentValue;
    public double previousValue;
    public double actionTime = 0;

    public HardwareDevice(LinearOpMode Program, String Name) {
        super(Program, Name);
    }

    public abstract void runOpMode() throws InterruptedException;
    public abstract void SetHardwareMap();
    public abstract double GetValue();
    public abstract void SetValue(double Value);

    public boolean ValueHasChanged() {
        return (previousValue != currentValue);
    }

    public double RoundDecimals(double Value) {
        String ReturnValue;

        double power = Value * Math.pow(10, DecimalPlaces);
        power = Math.round(power);
        power = power / Math.pow(10, DecimalPlaces);

        String format = "%." + String.valueOf(DecimalPlaces) + "f";
        ReturnValue = String.format(format, power);

        return Double.valueOf(ReturnValue);
    }
}
