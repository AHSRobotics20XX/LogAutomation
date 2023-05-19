package org.firstinspires.ftc.teamcode.Custom.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Custom.Devices.CameraDevices.CameraAddons._ObjectDetection;
import org.firstinspires.ftc.teamcode.Custom.EnvironmentConst;
import org.firstinspires.ftc.teamcode.Custom.Helpers.Configurator;

public abstract class Camera extends Device {
    private EnvironmentConst environmentConst = EnvironmentConst.getInstance();
    private _ObjectDetection objDetectAddon;

    public String VuforiaKey = environmentConst.VuforiaKey;
    public CameraName cameraName = null;

    public Camera(LinearOpMode Program, String Name) {
        super(Program, Name);
    }

    public abstract void SetHardwareMap();

    public boolean IsWebCam() {
        return cameraName.isWebcam();
    }

    public boolean IsSwitchable() {
        return cameraName.isSwitchable();
    }

    public boolean IsUnknown() {
        return cameraName.isUnknown();
    }

    public void EnableObjectDetection(String AssetFileName, String[] Labels, VuforiaLocalizer.CameraDirection CameraDirection, float MinimumConfidence, double Zoom) {
        objDetectAddon = new _ObjectDetection(program, getName(), debugDevice, debugTag, VuforiaKey, cameraName, AssetFileName, Labels, CameraDirection, MinimumConfidence, Zoom);
    }

    public _ObjectDetection UseObjectDetection() {
        return objDetectAddon;
    }
}
