package org.firstinspires.ftc.teamcode.Custom.BaseClasses;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Custom.EnvironmentConst;

public abstract class Device extends LinearOpMode
{
    public LinearOpMode program;

    private String name;
    public boolean debugDevice = false;
    public String debugTag;

    public Device(LinearOpMode Program, String Name) {
        program = Program;
        name = Name;
    }

    public void DebugMode(String Tag) {
        debugDevice = true;
        debugTag = Tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
