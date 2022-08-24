package org.mbatet.calories.model.stats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Interval;

import java.util.Comparator;

public class TrackingChart {

    /**
     * Només es una classe enbollcall per passar objecets a la vista
     * */

    WeightStats weightGainStats = WeightStats.getWeightGainStatsInstance();
    WeightStats weightLossStats = WeightStats.getWeightLossStatsInstance();
    WeightStats weightMaintenanceStats = WeightStats.getWeightMaintenanceStatsInstance();

    private static final Log log = LogFactory.getLog(TrackingChart.class.getName());


    public WeightStats getWeightGainStats() {
        return weightGainStats;
    }

    public void setWeightGainStats(WeightStats weightGainStats) {
        this.weightGainStats = weightGainStats;
    }

    public WeightStats getWeightLossStats() {
        return weightLossStats;
    }

    public void setWeightLossStats(WeightStats weightLossStats) {this.weightLossStats = weightLossStats;}

    public WeightStats getWeightMaintenanceStats() {return weightMaintenanceStats;}

    public void setWeightMaintenanceStats(WeightStats weightMaintenanceStats) {this.weightMaintenanceStats = weightMaintenanceStats;}

    public Double getMaintenanceCals()
    {
        if( this.weightMaintenanceStats.getRecomendedCals() != null )
        {
            //TODO: tb podria ser adjustedCals o  adjustedCals+(mitja d'esport que portem darrerament)
            return this.weightMaintenanceStats.getRecomendedCals()*1.0;
        }

        if(this.weightGainStats.getRecomendedCals()==null || this.weightLossStats.getRecomendedCals()==null)
        {
            return null;
        }

        return (this.weightGainStats.getRecomendedCals() + this.weightLossStats.getRecomendedCals())/2.0;

    }

    public Double getCaloriesBelowMaintenance()
    {

        return (this.getMaintenanceCals() - Constants.CALS_BELOW_MAINTENANCE_TO_LOSE_WEIGHT);

    }

    public void addInterval(Interval interval)
    {
        if( interval.getType() == Constants.TYPE_WEIGHT_LOSS ){
            this.weightLossStats.addInterval(interval);
            return;
        }

        if( interval.getType() == Constants.TYPE_WEIGHT_MAINTENANCE ){
            this.weightMaintenanceStats.addInterval(interval);
            return;
        }


        this.weightGainStats.addInterval(interval);

    }

    //TODO:this method should go to the service
    public void calculate()
    {
        weightLossStats.calculate();
        weightGainStats.calculate();
        weightMaintenanceStats.calculate();

        Comparator comp = new WeightStats.SortStats();


        /*
        log.info("[m:calculate] Comparem weightGainStats amb maintenanceStats i weightLossStats");
        //if(weightGainStats.getRecomendedCals()!=null && weightGainStats.getRecomendedCals()<maintenanceStats.getRecomendedCals())
        if(  comp.compare(weightGainStats, weightLossStats) < 1)
        {

            log.info("[m:calculate] No tenim prou dades per tenir estadistiques de weightGainStats" );
            weightGainStats.setNotEnoughData(true);
        }

        log.info("[m:calculate] Comparem weightLossStats amb maintenanceStats i weightGainStats");
        //if(weightLossStats.getRecomendedCals()!=null && weightLossStats.getRecomendedCals()>maintenanceStats.getRecomendedCals())
        if( comp.compare(weightLossStats, weightGainStats) > 0)
        {

            log.info("[m:calculate] No tenim prou dades per tenir estadistiques de weightLossStats" );
            weightLossStats.setNotEnoughData(true);
        }*/


    }




    /*
    public void validate()
    {
        //NO perque despres les dades no tenen cap tipsu de sentit
        //weightLossStats.changeRecomendedCalsIfSmaller(weightGainStats.getRecomendedCals());
        //weightLossStats.changeRecomendedCalsIfSmaller(maintenanceStats.getRecomendedCals());
        //weightGainStats.changeRecomendedCalsIfBigger(weightLossStats.getRecomendedCals());
        //weightGainStats.changeRecomendedCalsIfBigger(maintenanceStats.getRecomendedCals());
    }*/
}
