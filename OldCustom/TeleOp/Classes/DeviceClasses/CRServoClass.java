package org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Custom.HelperClasses.VariableHub;

@Disabled
public class CRServoClass extends LinearOpMode {

    VariableHub variableHub = new VariableHub();
    public CRServo crServo = null;
    public String crServoName = null;
    public CRServo.Direction direction = CRServo.Direction.FORWARD;
    public double currentPower;
    public double previousPower;
    private int DecimalPlaces = variableHub.CRServoDecimalPlaces;
    public double actionTime = 0;

    /**
     * Required for extension of LinearOpMode <p>
     */
    @Override
    public void runOpMode() {

    }

    /**
     * HardwareMap setter for any CRServo <p>
     */
    public void SetHardwareMap(LinearOpMode program,String configName, CRServo.Direction Direction)
    {
        crServo = program.hardwareMap.get(CRServo.class, configName);
        crServoName = configName;
        direction = Direction;
        crServo.setDirection(direction);
    }

    /**
     * Sets power to specific CRServo <p>
     * NOTE: Rounds power value to 3 decimals by default <p>
     */
    public void SetPower(double Power)
    {
        ElapsedTime timer = new ElapsedTime();
        double power = RoundDecimals(Power, DecimalPlaces);
        previousPower = currentPower;
        currentPower = power;

        if (currentPower != previousPower)
        {
            crServo.setPower(currentPower);
        }
        actionTime = timer.milliseconds();
    }

    /**
     * Gets power for specific CRServo <p>
     */
    public double GetPower()
    {
        return crServo.getPower();
    }

    /**
     * Method to check if CRServo power has changed <p>
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