package org.firstinspires.ftc.teamcode.Custom.HelperClasses;

import java.util.ArrayList;
import java.util.Collections;

public class ArrayListClass {


    public static int FindStringLinNumInArray(ArrayList<String> List, String SearchString)
    {
        int ReturnValue = -1;

        for (int i = 0; i < List.size(); i++)
        {
            if (List.get(i).trim().equals(SearchString))
            {
                ReturnValue = i;
                break;
            }
        }

        if (ReturnValue == -1)
        {
            throw new RuntimeException("Could Not Find String " + SearchString);
        }

        return ReturnValue;
    }
    public static void AddStringBeforeSearchStringInArray(ArrayList<String> List, String SearchString, String StringToAdd)
    {
        List.add(FindStringLinNumInArray(List, SearchString), StringToAdd);
    }
    public static void AddArrayStringAfterSearchStringInArray(ArrayList<String> MainList, ArrayList<String> TempList, String SearchString)
    {
        Collections.reverse(TempList);
        for (int j = 0; j < TempList.size(); j++)
        {
            MainList.add(FindStringLinNumInArray(MainList, SearchString) + 1, TempList.get(j));
        }
    }
    public static void AddArrayStringBeforeSearchStringInArray(ArrayList<String> MainList, ArrayList<String> TempList, String SearchString)
    {
        for (int j = 0; j < TempList.size(); j++)
        {
            MainList.add(FindStringLinNumInArray(MainList, SearchString), TempList.get(j));
        }
    }
    public static void RemoveStringInArrayFromSearchString(ArrayList<String> List, String SearchString)
    {
        List.remove(FindStringLinNumInArray(List, SearchString));
    }


    public static String ArrayListItemContains(ArrayList<String> ArrayLst, String SearchString)
    {
        String ReturnValue = null;

        for (int i = 0; i < ArrayLst.size(); i++)
        {
            if (ArrayLst.get(i).contains(SearchString))
            {
                ReturnValue = ArrayLst.get(i);
            }
        }
        return ReturnValue;
    }
    public static int ArrayListItemEquals(ArrayList<String> ArrayList, String SearchString)
    {
        int ReturnValue = -1;

        for (int i = 0; i < ArrayList.size(); i++)
        {
            String line = ArrayList.get(i);
            if (line.trim().equals(SearchString))
            {
                ReturnValue = i;
            }
        }
        return ReturnValue;
    }
    public static ArrayList<String> StringSplitIntoArrayList(String StringToSplit, String Delimiter)
    {
        ArrayList<String> ReturnValue = new ArrayList<>();

        String[] split = StringToSplit.split(Delimiter);

        for (int i = 0; i < split.length; i++)
        {
            ReturnValue.add(split[i]);
        }

        return ReturnValue;
    }

    public static ArrayList<String> AddCharactersToArrayListString(ArrayList<String> Source, String CharacterBeforeElement, String CharacterAfterElement)
    {
        ArrayList<String> ReturnValue = new ArrayList<>();

        for (int i = 0; i < Source.size(); i++)
        {
            ReturnValue.add( CharacterBeforeElement + Source.get(i) + CharacterAfterElement);
        }

        return ReturnValue;
    }

    public static void ArrayListToFile(ArrayList<String> temp, String FilePath)
    {
        FileIOClass fileIOClass = new FileIOClass();

        for (int i = 0; i < temp.size(); i++)
        {
            fileIOClass.FileAppendLine(temp.get(i), FilePath);
        }
    }

    public static ArrayList<String> ArrayListAdjustAfterDelimiter(ArrayList<String> ArrayList, String Delimiter)
    {
        ParseClass parseClass = new ParseClass();

        ArrayList<String> ReturnValue = new ArrayList<>();

        for (int i = 0; i < ArrayList.size(); i++)
        {
            ReturnValue.add(parseClass.StringAfterDelimiter(ArrayList.get(i), Delimiter));
        }

        return ReturnValue;
    }

    public static void ArrayListAppendArrayList(ArrayList<String> ArrayList, ArrayList<String> AppendArrayList)
    {
        for (int i = 0; i < AppendArrayList.size(); i++)
        {
            ArrayList.add(AppendArrayList.get(i));
        }
    }
    public static void CheckIfConfigsEqual(ArrayList<String> ArrayList)
    {
        ParseClass parseClass = new ParseClass();
        FileIOClass fileIOClass = new FileIOClass();

        boolean ReturnValue = false;

        String masterConfig = "";
        String currentConfig = "";

        for (int i = 0; i < ArrayList.size(); i++)
        {
            String line = ArrayList.get(i);

            if (line.startsWith("Config: Master"))
            {
                masterConfig = line;
            }
            else if (line.startsWith("Config: Current"))
            {
                currentConfig = line;
            }
        }

        if (masterConfig.length() > 0 && currentConfig.length() > 0)
        {
            masterConfig = parseClass.StringAfterDelimiter(masterConfig, "~");
            fileIOClass.LogMessage(masterConfig);
            currentConfig = parseClass.StringAfterDelimiter(currentConfig, "~");
            fileIOClass.LogMessage(currentConfig);

            if (masterConfig.equals(currentConfig))
            {
                ReturnValue = true;
            }
        }

        if (!ReturnValue)
        {
            throw new RuntimeException("Mismatched Configs");
        }
    }
}
