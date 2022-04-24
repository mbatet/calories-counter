package org.mbatet.calories.model.stats;

public class WeightMaintenanceStats extends WeightStats {


    public String getTitle(){return "Estimated/recommended cals for maintenance";}


    @Override
    public String toString()
    {

        return "[MaintenanceStats]activityCals:" + activityCals + " - consumedCals: " + consumedCals + " - adjustedCals: " + adjustedCals;
    }
}
