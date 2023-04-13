package org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
public class ServoClass extends LinearOpMode {

    public Servo servo = null;
    public String servoName = null;
    public double currentPosition;
    public double previousPosition;

    private ElapsedTime timer = new ElapsedTime();
    private double pauseMilliseconds = 0;

    /**
     * Required for extension of LinearOpMode <p>
     */
    @Override
    public void runOpMode() {

    }

    /**
     * HardwareMap setter for any Servo <p>
     */
    public void SetHardwareMap(LinearOpMode program,String configName, double PauseMilliseconds)
    {
        servo = program.hardwareMap.get(Servo.class, configName);
        servoName = configName;
        pauseMilliseconds = PauseMilliseconds;
    }

    /**
     * Sets position to specific Servo if the millisecond delay is up <p>
     */
    public void SetPosition(double position)
    {
        if (ReadyToSetNewPosition())
        {
            previousPosition = currentPosition;
            currentPosition = position;

            if (currentPosition != previousPosition)
            {
                servo.setPosition(currentPosition);
            }
        }
    }

    /**
     * Helper function to keep the position within the bounds of Min and Max position <p>
     */
    public void SetPositionHelper(double IncrementValue, double MinPosition, double MaxPosition)
    {
        double newPosition = currentPosition;

        if (IncrementValue != 0)
        {
            newPosition = currentPosition + IncrementValue;

            if (newPosition > MaxPosition)
            {
                SetPosition(MaxPosition);
            }
            else if (newPosition < MinPosition)
            {
                SetPosition(MinPosition);
            }
            else
            {
                SetPosition(newPosition);
            }
        }
    }

    /**
     * Gets position for specific servo <p>
     */
    public double GetPosition()
    {
        return servo.getPosition();
    }

    /**
     * Method to check if Servo position has changed <p>
     */
    public boolean PositionHasChanged()
    {
        return (previousPosition != currentPosition);
    }

    /**
     * Checks to see if the time delay is up
     */
    public boolean ReadyToSetNewPosition()
    {
        if (timer.milliseconds() > pauseMilliseconds)
        {
            timer.reset();
            return true;
        }
        else
        {
            return false;
        }
    }
}