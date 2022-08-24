package org.mbatet.calories.model.stats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Dia;
import org.mbatet.calories.model.Interval;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneralStats {

    /**
     * Només es una classe enbollcall per passar objecets a la vista
     * */




    //variables generals amb les dades raw
    List<Dia> dies = new ArrayList<Dia>();
    List<Interval> intervals = new ArrayList<Interval>();
    Interval intervalGeneral;
    Interval lastWeek;

    //TODO: Ompmplir al calculate i guardar com una variable per no calcularho cada cop
   //Float maxWeight;
    //Float minWeight;
    //Float currentWeight;

    //intervals filtrats segons el seu tipus
    IntervalStats weightGainStats = IntervalStats.getWeightGainStatsInstance();
    IntervalStats weightLossStats = IntervalStats.getWeightLossStatsInstance();
    IntervalStats weightMaintenanceStats = IntervalStats.getWeightMaintenanceStatsInstance();


    //variables obtingudes a partir dels intervals ordenats
    Float calsLeft;


    private static final Log log = LogFactory.getLog(GeneralStats.class.getName());


    public GeneralStats(List<Dia> dies, List<Interval> intervals,  Interval intervalGeneral, Interval lastWeek){

        this.dies=dies;
        this.intervals = intervals;
        this.intervalGeneral=intervalGeneral;
        this.lastWeek=lastWeek;
    }



    public List<Dia> getDies() {return dies;}

    public void setDies(List<Dia> dies) {this.dies = dies;}

    public List<Interval> getIntervals() {return intervals;}

    public void setIntervals(List<Interval> intervals) {this.intervals = intervals;}

    public Interval getIntervalGeneral() {return intervalGeneral;}

    public void setIntervalGeneral(Interval intervalGeneral) {this.intervalGeneral = intervalGeneral;}

    public Interval getLastWeek() {return lastWeek;}

    public void setLastWeek(Interval lastWeek) {this.lastWeek = lastWeek;}

    public IntervalStats getWeightGainStats() {return weightGainStats;}

    public void setWeightGainStats(IntervalStats weightGainStats) {this.weightGainStats = weightGainStats;}

    public IntervalStats getWeightLossStats() {return weightLossStats;}

    public void setWeightLossStats(IntervalStats weightLossStats) {this.weightLossStats = weightLossStats;}

    public IntervalStats getWeightMaintenanceStats() {return weightMaintenanceStats;}

    public void setWeightMaintenanceStats(IntervalStats weightMaintenanceStats) {this.weightMaintenanceStats = weightMaintenanceStats;}

    public Double getMaintenanceCals()
    {
        if( this.weightMaintenanceStats.getRecomendedCals() != null ){
            //TODO: tb podria ser adjustedCals o  adjustedCals+(mitja d'esport que portem darrerament)
            return this.weightMaintenanceStats.getRecomendedCals()*1.0;
        }

        if(this.weightGainStats.getRecomendedCals()==null || this.weightLossStats.getRecomendedCals()==null){
            return null;
        }

        return (this.weightGainStats.getRecomendedCals() + this.weightLossStats.getRecomendedCals())/2.0;

    }

    public Double getCaloriesBelowMaintenance(){

        return (this.getMaintenanceCals() - Constants.CALS_BELOW_MAINTENANCE_TO_LOSE_WEIGHT);

    }

    //TODO: this method should go to the service (things that change)
    public void filterInterval(Interval interval){
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

    //TODO: this method should go to the service (things that change)
    public void calculate(){


        for(Interval interval: this.intervals){
            filterInterval(interval);
        }


        weightLossStats.calculate();
        weightGainStats.calculate();
        weightMaintenanceStats.calculate();


        //TODO: calcular tb les variables i deixarles ja calculades
        //Float maxWeight = getMaxWeight( dies);
        //Float minWeight = getMinWeight( dies);
        //Float currentWeight = intervalGeneral.getLastDay().getWeight();


        Comparator comp = new IntervalStats.SortStats();


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


    //TODO: Ompmplir al calculate i guardar com una variable per no calcularho cada cop
    public Float getMaxWeight(){

        Float maxWeight = 0F;

        for(Dia dia:this.dies){
            if(dia.getWeight()!=null && dia.getWeight()>maxWeight) maxWeight=dia.getWeight();

        }

        log.info("[m:getMaxWeight] retornem " + maxWeight);
        return maxWeight;

    }

    //TODO: Ompmplir al calculate i guardar com una variable per no calcularho cada cop
    public Float getMinWeight(){
        Float minWeight = 10000F;

        for(Dia dia:this.dies){
            if(dia.getWeight()!=null && dia.getWeight()<minWeight) minWeight=dia.getWeight();

        }

        log.info("[m:getMinWeight] retornem " + minWeight);
        return minWeight;

    }

    //TODO: Ompmplir al calculate i guardar com una variable per no calcularho cada cop
    public Float getCurrentWeight(){
        return this.intervalGeneral.getLastDay().getWeight();
    }
}
