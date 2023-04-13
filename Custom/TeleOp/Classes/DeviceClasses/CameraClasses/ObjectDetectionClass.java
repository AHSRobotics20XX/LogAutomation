package org.firstinspires.ftc.teamcode.Custom.TeleOp.Classes.DeviceClasses.CameraClasses;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.Custom.HelperClasses.VariableHub;

import java.util.List;
import com.vuforia.CameraDevice;

@Disabled
public class ObjectDetectionClass extends LinearOpMode
{
    VariableHub variableHub = new VariableHub();
    LinearOpMode program;

    //Required
    private TFObjectDetector tensorFlow;
    private VuforiaLocalizer vuforia;
    private String VuforiaKey = variableHub.VuforiaKey;
    public String assetFile;
    public String[] labels;

    //Other Essential Variables
    public VuforiaLocalizer.CameraDirection cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    public float minConfidence = 0.75f;
    private String getLabel;
    private float getConfidence;
    public double zoom = 1;

    //Custom Variables
    ElapsedTime timer = new ElapsedTime();
    public String configName;
    public CameraName cameraName;
    private boolean isItWebcam = false;

    public double durationMilliseconds = 5000;
    public boolean useCustomAsset;
    public boolean flashlight = false;
    public int getLabelIndex;


    /**
     * Required for extension of LinearOpMode <p>
     */
    @Override
    public void runOpMode() {

    }

    /**
     * HardwareMap for using ObjectDetection <p>
     */
    public ObjectDetectionClass(LinearOpMode Program ,String ConfigName, CameraName CameraName, boolean CustomAsset, String AssetFileName, String[] LabelArray)
    {
        program = Program;
        configName = ConfigName;
        cameraName = CameraName;
        useCustomAsset = CustomAsset;

        if (CustomAsset)
        {
            assetFile = "/sdcard/FIRST/tflitemodels/" + AssetFileName;
        }
        else
        {
            assetFile = AssetFileName;
        }

        labels = LabelArray;

        if (cameraName != null)
        {
            isItWebcam = true;
        }
    }

    /**
     * Parameters that can be changed for a specific Camera <p>
     */
    public void SetParameters(VuforiaLocalizer.CameraDirection CameraDirection, float MinimumConfidence, boolean Flashlight, double SnapShotTime, double Zoom)
    {
        if (!isItWebcam)
        {
            cameraDirection = CameraDirection;

            if (CameraDirection == VuforiaLocalizer.CameraDirection.BACK)
            {
                flashlight = Flashlight;
            }
        }

        minConfidence = MinimumConfidence;
        durationMilliseconds = SnapShotTime;
        zoom = Zoom;
    }

    /**
     * Method to start the logic of ObjectDetection using a specific Camera <p>
     */
    public void UseVuforiaAndTensorFlow()
    {
        int tensorFlowMonitorViewId = program.hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", program.hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters();
        vuforiaParameters.vuforiaLicenseKey = VuforiaKey;

        if (isItWebcam)
        {
            vuforiaParameters.cameraName = cameraName;
        }
        else
        {
            vuforiaParameters.cameraDirection = cameraDirection;
        }

        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);

        TFObjectDetector.Parameters tensorFlowParameters = new TFObjectDetector.Parameters(tensorFlowMonitorViewId);
        tensorFlowParameters.minResultConfidence = minConfidence;
        tensorFlowParameters.isModelTensorFlow2 = true;
        tensorFlowParameters.inputSize = 300;
        tensorFlow = ClassFactory.getInstance().createTFObjectDetector(tensorFlowParameters, vuforia);

        if (useCustomAsset)
        {
            tensorFlow.loadModelFromFile(assetFile, labels);
        }
        else
        {
            tensorFlow.loadModelFromAsset(assetFile, labels);
        }

        if (tensorFlow != null)
        {
            tensorFlow.activate();
            tensorFlow.setZoom(zoom, 16.0/9.0);

            Flashlight(true);
            boolean notDetectedYet = true;
            getLabelIndex = 0;
            timer.reset();
            while (program.opModeIsActive() && (durationMilliseconds > timer.milliseconds()) && notDetectedYet)
            {
                List<Recognition> updatedRecognitions = tensorFlow.getUpdatedRecognitions();
                if (updatedRecognitions != null)
                {
                    for (Recognition recognition : updatedRecognitions)
                    {
                        getLabel = recognition.getLabel();
                        getLabelIndex = GetLabelIndex(getLabel) + 1;
                        getConfidence = recognition.getConfidence();
                        notDetectedYet = false;
                    }
                }
            }

            tensorFlow.shutdown();
            vuforia.close();
            Flashlight(false);
        }
    }

    /**
     * Getter for the found label <p>
     */
    public String GetLabel()
    {
        return getLabel;
    }

    /**
     * Getter for the logic confidence <p>
     */
    public float GetConfidence()
    {
        return getConfidence;
    }

    /**
     * Getter for the label index in the list <p>
     */
    public int GetLabelIndex(String labelValue)
    {
        int returnIndex = -1;

        for (int i = 0; i < labels.length; i++)
        {
            if (labels[i] == labelValue)
            {
                returnIndex = i;
            }
        }

        return returnIndex;
    }

    /**
     * Getter for the label separated by ~ <p>
     */
    public String GetLabelListDelimited()
    {
        String temp = "";

        for (int i = 0; i < labels.length; i++)
        {
            temp = temp + "~" + labels[i];
        }
        temp = temp.substring(1);

        return temp;
    }

    /**
     * Method to turn flashlight on or off if using flashlight <p>
     */
    private void Flashlight(Boolean OnOff)
    {
        if (flashlight)
        {
            CameraDevice.getInstance().setFlashTorchMode(OnOff);
        }
    }
}