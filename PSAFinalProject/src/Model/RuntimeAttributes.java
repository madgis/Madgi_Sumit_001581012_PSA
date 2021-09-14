/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author aditya
 */
import java.util.ArrayList;
import java.util.HashMap;

public class RuntimeAttributes {

    public static HashMap<String, ArrayList<Double>> fitnessTable = new HashMap<>();
    public static  HashMap<String, Double> virusvariantLog = new HashMap<>();
    public static int recoveredCount = 0;
    public static int naiveCount=999;
    public static int infectedCount=1;
    public static double humanThreshold = 2000;
    public static double naiveHumanThreshold = 5000;
    public static int deathCount=0;
    public static int k_factor;
    public static int naiveDeathCount = 0;
    public static int recoveredDeathCount = 0;
    public static int vaccinatedDeathCount = 0;

}
