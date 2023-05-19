package org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses.CameraClasses;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;

import java.util.ArrayList;

@Disabled

public class CameraClass extends LinearOpMode {

    LinearOpMode program;
    public String configName = null;
    public CameraName webcamName;
    public boolean isItWebcam;

    public ArrayList<ObjectDetectionClass> lstObjectDetection = new ArrayList<ObjectDetectionClass>();

    /**
     * Required for extension of LinearOpMode <p>
     */
    @Override
    public void runOpMode() {

    }

    /**
     * HardwareMap setter for any Camera <p>
     */
    public void SetHardwareMap(LinearOpMode Program, String ConfigName, Boolean IsItWebcam)
    {
        configName = ConfigName;
        isItWebcam = IsItWebcam;
        program = Program;

        if (isItWebcam)
        {
            webcamName = Program.hardwareMap.get(CameraName.class, ConfigName);
        }
    }

    /**
     * Creates a instance of the ObjectDetectionClass to be used with specific Camera <p>
     */
    public void SetupObjectDetection(boolean CustomAsset, String AssetFileName, String[] LabelArray)
    {
        ObjectDetectionClass objectDetectionClass = new ObjectDetectionClass(program, configName, webcamName, CustomAsset, AssetFileName, LabelArray);
        lstObjectDetection.add(objectDetectionClass);
    }

    /**
     * Getter method to use/change ObjectDetection variables and methods using specific Camera <p>
     */
    public ObjectDetectionClass UseObjectDetectionClass()
    {
        int returnIndex = -1;

        for (int i = 0; i < lstObjectDetection.size(); i++)
        {
            if (lstObjectDetection.get(i).configName == configName)
            {
                returnIndex = i;
            }
        }

        return lstObjectDetection.get(returnIndex);
    }


    /**
     * Getter to see if its a webcam or a built-in camera <p>
     */
    public boolean IsWebCam()
    {
        return webcamName.isWebcam();
    }

    /**
     * Getter to see if its a webcam or a built-in camera <p>
     */
    public boolean IsPhoneCamera()
    {
        return webcamName.isCameraDirection();
    }

    /**
     * Getter to see if the camera is switchable <p>
     */
    public boolean IsSwitchable()
    {
        return webcamName.isSwitchable();
    }

    /**
     * Getter to see if the camera is unknown <p>
     */
    public boolean IsUnknown()
    {
        return webcamName.isUnknown();
    }

}
