package org.firstinspires.ftc.teamcode.Custom.Devices.CameraDevices.CameraAddons;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Custom.Helpers.FileIOClass;
import org.firstinspires.ftc.teamcode.Custom.Helpers.Logger;

import java.util.List;

public class _ObjectDetection {
    Logger logger = Logger.getInstance();
    FileIOClass fileIOClass = new FileIOClass();
    private LinearOpMode program;
    private boolean debugMode;
    private String debugTag;
    private String deviceName;
    private CameraName camera;
    private String vuforiaKey;
    private TFObjectDetector tensorFlow;
    private VuforiaLocalizer vuforia;

    private String assetFile;
    private String[] labels;
    private VuforiaLocalizer.CameraDirection cameraDirection;
    private float minConfidence;
    private double zoom;
    private boolean isCustomAsset = false;
    private double durationMilliseconds = 5000;
    private String label;
    private float confidence;
    private boolean localHasDetected;
    public boolean logHasDetected;
    public int labelIndex;

    public _ObjectDetection(LinearOpMode Program, String DeviceName, boolean Debug, String DebugTag, String VuforiaKey, CameraName Camera, String AssetFileName, String[] Labels,
                            VuforiaLocalizer.CameraDirection CameraDirection, float MinimumConfidence, double Zoom) {
        program = Program;
        camera = Camera;
        debugMode = Debug;
        debugTag = DebugTag;
        deviceName = DeviceName;

        if (VuforiaKey.equals("")) {
            throw new RuntimeException("No Vuforia Key Given");
        }
        else {
            vuforiaKey = VuforiaKey;
            String customAssetFilePath = "/sdcard/FIRST/tflitemodels/" + AssetFileName;

            if (fileIOClass.FileExists(customAssetFilePath)) {
                assetFile = customAssetFilePath;
                isCustomAsset = true;
            }
            else {
                assetFile = AssetFileName;
            }

            labels = Labels;
            cameraDirection = CameraDirection;
            minConfidence = MinimumConfidence;
            zoom = Zoom;

            if (debugMode) {
                Log.i(debugTag, "ObjectDetection has been created for " + deviceName);
            }
        }
    }

    public void StartVuforiaAndTensorFlow() {
        if (debugMode) {
            Log.i(debugTag, "ObjectDetection has started for " + deviceName);
        }

        localHasDetected = false;
        int tensorFlowMonitorViewId = program.hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", program.hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters();
        vuforiaParameters.vuforiaLicenseKey = vuforiaKey;

        if (camera != null) {
            vuforiaParameters.cameraName = camera;
        }
        else {
            vuforiaParameters.cameraDirection = cameraDirection;
        }

        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);

        TFObjectDetector.Parameters tensorFlowParameters = new TFObjectDetector.Parameters(tensorFlowMonitorViewId);
        tensorFlowParameters.minResultConfidence = minConfidence;
        tensorFlowParameters.isModelTensorFlow2 = true;
        tensorFlowParameters.inputSize = 300;
        tensorFlow = ClassFactory.getInstance().createTFObjectDetector(tensorFlowParameters, vuforia);

        if (isCustomAsset) {
            tensorFlow.loadModelFromFile(assetFile, labels);
        }
        else {
            tensorFlow.loadModelFromAsset(assetFile, labels);
        }

        if (tensorFlow != null) {
            tensorFlow.activate();
            tensorFlow.setZoom(zoom, 16.0/9.0);
            ElapsedTime timer = new ElapsedTime();

            timer.reset();
            while (program.opModeIsActive() && (durationMilliseconds > timer.milliseconds()) && !localHasDetected) {
                List<Recognition> updatedRecognitions = tensorFlow.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    for (Recognition recognition : updatedRecognitions) {
                        label = recognition.getLabel();
                        labelIndex = GetLabelIndex(label) + 1;
                        confidence = recognition.getConfidence();
                        localHasDetected = true;

                        if (logger.logStarted) {
                            logHasDetected = true;
                        }
                    }
                }
            }

            if (!localHasDetected) {
                //If nothing is found;
                label = "null";
                labelIndex = 0;
                localHasDetected = true;

                if (logger.logStarted) {
                    logHasDetected = true;
                }
            }

            tensorFlow.shutdown();
            vuforia.close();

            if (debugMode) {
                Log.i(debugTag, "ObjectDetection for " + deviceName + " has found " + label + ". The index of this label is " + labelIndex);
            }
        }
    }

    public int GetLabelIndex(String labelValue)
    {
        int returnIndex = -1;
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] == labelValue) {
                returnIndex = i;
            }
        }
        return returnIndex;
    }

    public String GetFoundLabel() {
        return label;
    }

    public float GetConfidence() {
        return confidence;
    }

    public String GetAssetFile() {
        return assetFile;
    }

    public boolean GetCustomAsset() {
        return isCustomAsset;
    }

    public String GetLabelDelimited() {
        String temp = "";

        for (int i = 0; i < labels.length; i++) {
            temp = temp + "~" + labels[i];
        }
        temp = temp.substring(1);

        return temp;
    }

    public VuforiaLocalizer.CameraDirection GetCameraDirection() {
        return cameraDirection;
    }

    public double GetDuration() {
        return durationMilliseconds;
    }

    public double GetZoom() {
        return zoom;
    }
}
