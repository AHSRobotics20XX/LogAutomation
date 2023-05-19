package org.firstinspires.ftc.teamcode.Custom.Devices.CameraDevices;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.teamcode.Custom.BaseClasses.Camera;

public class _PhoneCamera extends Camera {

    public _PhoneCamera(LinearOpMode Program, String Name) {
        super(Program, Name);
    }

    @Override
    public void runOpMode() throws InterruptedException {}

    @Override
    public void SetHardwareMap() {
        if (debugDevice) {
            Log.i(debugTag, "Device " + getName() + " has been created.");
        }
    }
}
