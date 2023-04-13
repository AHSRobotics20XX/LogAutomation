package org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses.CRServoClass;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses.CameraClasses.CameraClass;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses.DcMotorClass;
import org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses.ServoClass;

import java.util.ArrayList;

@Disabled

/**
 * HardwareDeviceClass combines all sub-device classes into one public class <p>
 */
public class HardwareDeviceClass extends LinearOpMode {

    LinearOpMode program;


    /**
     * Required for extension of LinearOpMode <p>
     */
    @Override
    public void runOpMode() {
    }


    /**
     * NOTE: In order to pass any devices into the class, we also have to pass the "context" to which the main program resides (LinearOpMode) <p>
     * Example: "this" <p>
     */
    public HardwareDeviceClass(LinearOpMode CurrentClass)
    {
        program = CurrentClass;
    }

    //Array Lists to hold a variable number of devices needed for main program
    public ArrayList<DcMotorClass> lstDcMotors = new ArrayList<DcMotorClass>();
    public ArrayList<CRServoClass> lstCRServos = new ArrayList<CRServoClass>();
    public ArrayList<ServoClass> lstServos = new ArrayList<ServoClass>();
    public ArrayList<CameraClass> lstCameras = new ArrayList<CameraClass>();



    /**
     *  Setter method for DcMotor hardwareMap using the DcMotorClass <p>
     *  Declares and adds a DcMotor to the "lstDcMotors" <p>
     *  configName = name in RC/DS app <p>
     *  direction = DcMotor direction <p>
     *  UseEncoder = Is it going to use an encoder <p>
     */
    public void SetMotorHardwareMap(String configName, DcMotor.Direction direction, boolean UseEncoder)
    {
        DcMotorClass dcMotorClass = new DcMotorClass();

        dcMotorClass.SetHardwareMap(program, configName, direction, UseEncoder);

        lstDcMotors.add(dcMotorClass);
    }

    /**
     *  Setter method for CRServo hardwareMap using the CRServoClass <p>
     *  Declares and adds a CRServo to the "lstCRServos" <p>
     *  configName = name in RC/DS app <p>
     *  direction = CRServo direction <p>
     *  UseEncoder = Is it going to use an encoder <p>
     */
    public void SetCRServoHardwareMap(String configName, CRServo.Direction direction)
    {
        CRServoClass crServoClass = new CRServoClass();

        crServoClass.SetHardwareMap(program, configName, direction);

        lstCRServos.add(crServoClass);
    }

    /**
     *  Setter method for Servo hardwareMap using the ServoClass <p>
     *  Declares and adds a CRServo to the "lstServos" <p>
     *  configName = name in RC/DS app <p>
     *  MillisecondDelay = Amount of time before each action is called <p>
     */
    public void SetServoHardwareMap(String configName, double MillisecondDelay)
    {
        ServoClass servoClass = new ServoClass();

        servoClass.SetHardwareMap(program, configName, MillisecondDelay);

        lstServos.add(servoClass);
    }

    /**
     *  Setter method for Camera hardwareMap using the CameraClass <p>
     *  Declares and adds a Camera to the "lstCameras"
     *  @param configName name in RC/DS app
     *  @param Webcam webcam or a built-in camera
     */
    public void SetCameraHardwareMap(String configName, Boolean Webcam)
    {
        CameraClass cameraClass = new CameraClass();

        cameraClass.SetHardwareMap(program, configName, Webcam);

        lstCameras.add(cameraClass);
    }


    /**
     *  Getter method using specific DcMotor name
     */
    public DcMotorClass GetMotor(String configName)
    {
        int returnIndex = -1;

        for (int i = 0; i < lstDcMotors.size(); i++)
        {
            if (lstDcMotors.get(i).motorName == configName)
            {
                returnIndex = i;
            }
        }

        return lstDcMotors.get(returnIndex);
    }

    /**
     *  Getter method using specific CRServo name
     */
    public CRServoClass GetCRServo(String configName)
    {
        int returnIndex = -1;

        for (int i = 0; i < lstCRServos.size(); i++)
        {
            if (lstCRServos.get(i).crServoName == configName)
            {
                returnIndex = i;
            }
        }

        return lstCRServos.get(returnIndex);
    }

    /**
     *  Getter method using specific Servo name
     */
    public ServoClass GetServo(String configName)
    {
        int returnIndex = -1;

        for (int i = 0; i < lstServos.size(); i++)
        {
            if (lstServos.get(i).servoName == configName)
            {
                returnIndex = i;
            }
        }

        return lstServos.get(returnIndex);
    }

    /**
     *  Getter method using specific Camera name
     */
    public CameraClass GetCamera(String configName)
    {
        int returnIndex = -1;

        for (int i = 0; i < lstCameras.size(); i++)
        {
            if (lstCameras.get(i).configName == configName)
            {
                returnIndex = i;
            }
        }

        return lstCameras.get(returnIndex);
    }

    /**
     *  Method to check if any DcMotor/CRServo/Servo values have changed
     *  @return Boolean
     */
    public boolean HasDeviceValuesChanged()
    {
        boolean hasChanged = false;

        for (int i = 0; i < lstDcMotors.size(); i++)
        {
            hasChanged = hasChanged || lstDcMotors.get(i).PowerHasChanged();
        }
        for (int i = 0; i < lstCRServos.size(); i++)
        {
            hasChanged = hasChanged || lstCRServos.get(i).PowerHasChanged();
        }
        for (int i = 0; i < lstServos.size(); i++)
        {
            hasChanged = hasChanged || lstServos.get(i).PositionHasChanged();
        }

        return hasChanged;
    }
}