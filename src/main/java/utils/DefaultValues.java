package utils;

// Default values for various stuff around the program

public class DefaultValues {

    ////////////
    // AGENTS //
    ////////////

    public static double agentBaseSpeedGuard = 0.2;
    public static double agentSprintSpeedGuard = 0.4;
    public static double agentBaseSpeedIntruder = 0.2;
    public static double agentSprintSpeedIntruder = 0.4;

    public static double agentMaxStamina = 100;
    public static double agentSprintConsumption = 8;
    public static double agentStaminaRegeneration = 5;

    // Initial FOV values for each agent
    public static double agentFovAngle = 60;
    public static double agentFovDepth = 20;
    public static double agentFovNumber = 20;
    //////////////
    // AREA FOV //
    //////////////

    public static double areaShadedFovAngleFactor = .5;
    public static double areaShadedFovDepthFactor = .5;

    public static double areaSentryTowerFovAngleFactor = 1.5;
    public static double areaSentryTowerFovDepthFactor = 1.5;

    ////////////////////////
    // AREA SPEED & SOUND //
    ////////////////////////

    public static double areaSpeedFactor = 1;
    public static double areaSoundVolume = 1;

    public static double areaSentryTowerSpeedFactor = .5;

    public static double areaDoorSpeedFactor = .7;
}
