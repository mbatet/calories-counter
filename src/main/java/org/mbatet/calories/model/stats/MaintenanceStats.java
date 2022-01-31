package org.mbatet.calories.model.stats;

public class MaintenanceStats extends AverageCalStats {


    public String getTitle(){return "Estimated/recommended cals for maintenance";}


    @Override
    public String toString()
    {

        return "[MaintenanceStats]activityCals:" + activityCals + " - consumedCals: " + consumedCals + " - adjustedCals: " + adjustedCals;
    }
}
