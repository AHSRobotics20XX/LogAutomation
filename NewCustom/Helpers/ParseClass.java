package org.firstinspires.ftc.teamcode.Custom.Helpers;

import java.util.ArrayList;

public class ParseClass {

    private static ArrayListClass arrayListClass = new ArrayListClass();
    private static FileIOClass fileIOClass = new FileIOClass();

    private static ArrayList<String> lstDcMotor = new ArrayList<>();
    private static ArrayList<String> lstCRServo = new ArrayList<>();
    private static ArrayList<String> lstServo = new ArrayList<>();
    private static ArrayList<String> lstCamera = new ArrayList<>();
    public static ArrayList<String> lstDetection = new ArrayList<>();
    private static String configTimeStamp;

    public static String StringAfterDelimiter(String Value, String Delimiter)
    {
        String ReturnValue = Value;
        int delimiterPos = Value.indexOf(Delimiter);

        if (delimiterPos >= 0)
        {
            ReturnValue = Value.substring(delimiterPos + Delimiter.length()).trim();
        }

        return ReturnValue;
    }
    public static String StringBeforeDelimiter(String Value, String Delimiter)
    {
        String ReturnValue = null;
        int DelimiterPos = Value.indexOf(Delimiter);
        if (DelimiterPos >= 0)
        {
            ReturnValue = Value.substring(0,DelimiterPos);
        }

        return ReturnValue;
    }

    public static String ArrayListJoinIntoString(ArrayList<String> ArrayList1, String Delimiter)
    {
        return ArrayListJoinIntoString(ArrayList1, Delimiter, 0, ArrayList1.size()-1);
    }
    public static String ArrayListJoinIntoString(ArrayList<String> ArrayList1, String Delimiter, int indexLow, int indexHigh)
    {
        String ReturnValue = "";
        for (int i = indexLow; i <= indexHigh; i++)
        {
            if (i != indexLow)
            {
                ReturnValue = ReturnValue + Delimiter;
            }
            ReturnValue = ReturnValue + ArrayList1.get(i);
        }
        return ReturnValue;
    }

    public static void ParseConfigIntoArrays(String RawConfig)
    {
        String config = RawConfig.replace(" CONFIG = ", "");
        configTimeStamp = StringBeforeDelimiter(RawConfig, " ");
        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(config, ":");

        lstDcMotor.clear();
        lstCRServo.clear();
        lstServo.clear();
        lstCamera.clear();
        lstDetection.clear();
        for (int i = 0; i < temp.size(); i++)
        {
            String configLine = temp.get(i);

            if (configLine.startsWith("DcMotor"))
            {
                lstDcMotor.add(configLine);
            }
            else if (configLine.startsWith("CRServo"))
            {
                lstCRServo.add(configLine);
            }
            else if (configLine.startsWith("Servo"))
            {
                lstServo.add(configLine);
            }
            else if (configLine.startsWith("Camera"))
            {
                lstCamera.add(configLine);
            }
            else if (configLine.startsWith("Detection"))
            {
                lstDetection.add(configLine);
            }
        }
    }

    public static int DcMotorCount()
    {
        return lstDcMotor.size();
    }
    public static int CRServoCount()
    {
        return lstCRServo.size();
    }
    public static int ServoCount()
    {
        return lstServo.size();
    }
    public static int CameraCount()
    {
        return lstCamera.size();
    }
    public static int DetectionCount()
    {
        return lstDetection.size();
    }

    public static int GetDcMotorNameIndex(String Name)
    {
        int ReturnValue = -1;

        for (int i = 0; i < DcMotorCount(); i++)
        {
            if (GetDcMotorName(i).equals(Name))
            {
                ReturnValue = i;
            }
        }

        return ReturnValue;
    }
    public static String GetDcMotorName(int Index)
    {
        String DcMotor = lstDcMotor.get(Index).replace("DcMotor(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(DcMotor, ",");

        return temp.get(0).trim();
    }
    public static String GetDcMotorDirection(int Index)
    {
        String DcMotor = lstDcMotor.get(Index).replace("DcMotor(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(DcMotor, ",");

        return temp.get(1).trim();
    }
    public static String GetDcMotorEncoder(int Index)
    {
        String DcMotor = lstDcMotor.get(Index).replace("DcMotor(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(DcMotor, ",");

        return temp.get(2).trim();
    }
    public static String GetCRServoName(int Index)
    {
        String CRServo = lstCRServo.get(Index).replace("CRServo(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(CRServo, ",");

        return temp.get(0).trim();
    }
    public static String GetCRServoDirection(int Index)
    {
        String CRServo = lstCRServo.get(Index).replace("CRServo(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(CRServo, ",");

        return temp.get(1).trim();
    }
    public static String GetServoName(int Index)
    {
        String Servo = lstServo.get(Index).replace("Servo(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Servo, ",");

        return temp.get(0).trim();
    }
    public static String GetCameraName(int Index)
    {
        String Camera = lstCamera.get(Index).replace("Camera(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Camera, ",");

        return temp.get(0).trim();
    }
    public static String GetCameraIsItWebcam(int Index)
    {
        String Camera = lstCamera.get(Index).replace("Camera(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Camera, ",");

        return temp.get(1).trim();
    }
    public static String GetDetectionCameraName(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(0).trim();
    }
    public static String GetDetectionAssetFile(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(1).trim();
    }
    public static String GetDetectionCustomAsset(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(2).trim();
    }
    public static String GetDetectionLabelsFromIndex(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(3).trim();
    }
    public static String GetDetectionCameraDirection(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(4).trim();
    }
    public static String GetDetectionFlashlight(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(5).trim();
    }
    public static String GetDetectionConfidence(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(6).trim();
    }
    public static String GetDetectionSnapshotTime(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(7).trim();
    }
    public static String GetDetectionZoom(int Index)
    {
        String Detection = lstDetection.get(Index).replace("Detection(", "").replace(")","");

        ArrayList<String> temp = arrayListClass.StringSplitIntoArrayList(Detection, ",");

        return temp.get(8).trim();
    }
    public static String GetDetectionLabelFromString(String TextLine)
    {
        String ReturnValue = "";
        ReturnValue = StringAfterDelimiter(TextLine, "DETECT ");
        ReturnValue = StringBeforeDelimiter(ReturnValue, " ");

        return ReturnValue;
    }
    public static String StringAfterLastDelimiterOccurrence(String Value, String Delimiter)
    {
        String ReturnValue = Value;

        int pos = ReturnValue.indexOf(Delimiter);

        while (pos > -1)
        {
            ReturnValue = ReturnValue.substring(pos + Delimiter.length());
            pos = ReturnValue.indexOf(Delimiter);
        }

        return ReturnValue;
    }
}
