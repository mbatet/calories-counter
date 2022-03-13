package org.mbatet.calories.model.stats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Interval;

import java.util.Comparator;

public class Stats {

    /**
     * Només es una classe enbollcall per passar objecets a la vista
     * */

    WeightGainStats weightGainStats = new WeightGainStats();
    MaintenanceStats maintenanceStats = new MaintenanceStats();
    WeightLossStats weightLossStats = new WeightLossStats();

    private static final Log log = LogFactory.getLog(Stats.class.getName());


    public WeightGainStats getWeightGainStats() {
        return weightGainStats;
    }

    public void setWeightGainStats(WeightGainStats weightGainStats) {
        this.weightGainStats = weightGainStats;
    }

    public MaintenanceStats getMaintenanceStats() {
        return maintenanceStats;
    }

    public void setMaintenanceStats(MaintenanceStats maintenanceStats) {
        this.maintenanceStats = maintenanceStats;
    }

    public WeightLossStats getWeightLossStats() {
        return weightLossStats;
    }

    public void setWeightLossStats(WeightLossStats weightLossStats) {
        this.weightLossStats = weightLossStats;
    }

    public void addInterval(Interval interval)
    {
        if( interval.getType() == Interval.TYPE_WEIGHT_LOSS_INTERVAL )
        {
            this.weightLossStats.addInterval(interval);
            return;
        }

        if( interval.getType() == Interval.TYPE_WEIGHT_GAIN_INTERVAL )
        {
            this.weightGainStats.addInterval(interval);
            return;
        }

        //if(  interval.getType() == Interval.TYPE_MAINTENANCE_INTERVAL )

        //ens hem mantingut
        this.maintenanceStats.addInterval(interval);

    }

    public void calculate()
    {
        weightLossStats.calculate();
        maintenanceStats.calculate();
        weightGainStats.calculate();


        //necessitem mes dades

        //TODO: HEM DE FER SERVIR EL COMPARATOR!!!
        Comparator comp = new AverageCalStats.SortStats();


        log.info("[m:calculate] Comparem weightGainStats amb maintenanceStats i weightLossStats");
        //if(weightGainStats.getRecomendedCals()!=null && weightGainStats.getRecomendedCals()<maintenanceStats.getRecomendedCals())
        if( comp.compare(weightGainStats, maintenanceStats) < 1 || comp.compare(weightGainStats, weightLossStats) < 1)
        {

            log.info("[m:calculate] No tenim prou dades per tenir estadistiques de weightGainStats" );
            weightGainStats.setNotEnoughData(true);
        }

        log.info("[m:calculate] Comparem weightLossStats amb maintenanceStats i weightGainStats");
        //if(weightLossStats.getRecomendedCals()!=null && weightLossStats.getRecomendedCals()>maintenanceStats.getRecomendedCals())
        if( comp.compare(weightLossStats, maintenanceStats) >= 0 || comp.compare(weightLossStats, weightGainStats) >= 0)
        {

            log.info("[m:calculate] No tenim prou dades per tenir estadistiques de weightLossStats" );
            weightLossStats.setNotEnoughData(true);
        }


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
