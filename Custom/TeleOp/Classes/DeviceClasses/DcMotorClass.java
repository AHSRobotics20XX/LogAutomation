package org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Custom.HelperClasses.VariableHub;

import java.io.FileWriter;
import java.util.ArrayList;

@Disabled
public class DcMotorClass extends LinearOpMode {

    VariableHub variableHub = new VariableHub();

    public DcMotor dcMotor = null;
    public String motorName = null;
    public DcMotor.Direction direction = DcMotor.Direction.FORWARD;
    public double currentPower;
    public double previousPower;
    public boolean useEncoder = false;
    private boolean customMode = false;
    private int DecimalPlaces = variableHub.DcMotorDecimalPlaces;


    /**
     * Required for extension of LinearOpMode <p>
     */
    @Override
    public void runOpMode() {

    }

    /**
     * HardwareMap setter for any DcMotor <p>
     */
    public void SetHardwareMap(LinearOpMode program,String configName, DcMotor.Direction Direction, boolean UseEncoder)
    {
        dcMotor = program.hardwareMap.get(DcMotor.class, configName);
        motorName = configName;
        direction = Direction;
        dcMotor.setDirection(direction);
        useEncoder = UseEncoder;

        if (UseEncoder)
        {
            if (!isStarted())
            {
                if (!customMode)
                {
                    dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
            }
        }
    }

    /**
     * Sets power to specific DcMotor <p>
     * NOTE: Rounds power value to 3 decimals by default <p>
     */
    public void SetPower(double Power)
    {
        double power = RoundDecimals(Power, DecimalPlaces);

        previousPower = currentPower;
        currentPower = power;

        if (currentPower != previousPower)
        {
            dcMotor.setPower(currentPower);
        }
    }

    /**
     * Gets power for specific DcMotor <p>
     */
    public double GetPower()
    {
        return dcMotor.getPower();
    }

    /**
     * Gets position for specific DcMotor if using encoders <p>
     */
    public int GetCurrentPosition()
    {
        return dcMotor.getCurrentPosition();
    }

    /**
     * Sets a custom mode to specific DcMotor <p>
     * NOTE: Use if trying to set specific encoder modes <p>
     */
    public void SetMode(DcMotor.RunMode runMode)
    {
        dcMotor.setMode(runMode);
        customMode = true;
    }

    /**
     * Method to check if DcMotor power has changed <p>
     */
    public boolean PowerHasChanged()
    {
        return (previousPower != currentPower);
    }

    /**
     * Rounds Value by Decimal Places <p>
     */
    private double RoundDecimals(double Value, int DecimalPlaces)
    {
        String ReturnValue;

        double power = Value * Math.pow(10, DecimalPlaces);
        power = Math.round(power);
        power = power / Math.pow(10, DecimalPlaces);

        String format = "%." + String.valueOf(DecimalPlaces) + "f";
        ReturnValue = String.format(format, power);

        return Double.valueOf(ReturnValue);
    }
}