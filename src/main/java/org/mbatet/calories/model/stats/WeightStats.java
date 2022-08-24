package org.mbatet.calories.model.stats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Interval;
import org.mbatet.calories.service.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class WeightStats {

    int type;

    //Havent restat les calories del exercici
    Float activityCals = 0F;
    Float adjustedCals = 0F;
    Float consumedCals = 0F;

    Float recomendedCals;
    //Es igual que a consumedCals pero pot ser null, cosa que consumedCals pot ser 0, no se com eliminar-ho, pero potser cal dir-li diferent, consumedCalsOrNUll
    //TODO: ES EL MATEIX QUE CONSUMED CALS, CAL ELIMINAR

    boolean notEnoughData = false;



    List<Interval> intervals = new ArrayList<Interval>();


    private static final Log log = LogFactory.getLog(WeightStats.class.getName());

    public WeightStats(int type)
    {
        this.type=type;
    }



    //things that change
    public String getTitle() {
        return "Avg. consumed cals during " + Constants.INTERVAL_TYPE.get(this.type) + " interval";
    }


    //things that change
    public String toString()
    {

        String title = Constants.INTERVAL_TYPE.get(this.type).toUpperCase(Locale.ROOT);
        return "[" + title + "] activityCals:" + activityCals + " - consumedCals: " + consumedCals + " - adjustedCals: " + adjustedCals;
    }


    public boolean isNotEnoughData()
    {
        if( this.intervals.size() == 0 )
        {
            log.warn("[m:isNotEnoughData] We dont have intervals of the type " + Constants.INTERVAL_TYPE.get(this.type));
            return true;
        }

        return this.notEnoughData;
    }

    public void setNotEnoughData(boolean notEnough)
    {
        this.notEnoughData = notEnough;
    }


    public Float getActivityCals() {return Utils.roundToTens(activityCals);}

    public void setActivityCals(Float activityCals) {
        this.activityCals = activityCals;
    }

    public Float getAdjustedCals() {return Utils.roundToTens(adjustedCals);}

    public void setAdjustedCals(Float adjustedCals) {
        this.adjustedCals = adjustedCals;
    }


    public Float getConsumedCals() {return Utils.roundToTens(consumedCals);}

    public void setConsumedCals(Float consumedCals) {
        this.consumedCals = consumedCals;
    }


    public Float getRecomendedCals() {return Utils.roundToTens(recomendedCals);}

    public void setRecomendedCals(Float recomendedCals) {this.recomendedCals = recomendedCals;}




    public List<Interval> getIntervals() {return intervals;}

    public void setIntervals(List<Interval> intervals) {this.intervals = intervals;}

    public void addInterval(Interval interval) {this.intervals.add(interval);}


    public void calculate()
    {


        for(Interval interval: intervals) {
            this.consumedCals += interval.getAvgConsumedCals();
            this.activityCals += interval.getAvgActivityCals();
            this.adjustedCals += interval.getAvgAdjustedCals();
        }

        this.consumedCals = this.consumedCals/(float)intervals.size();
        this.activityCals = this.activityCals/(float)intervals.size();
        this.adjustedCals = this.adjustedCals/(float)intervals.size();

        //TODO: Hauria de ser ponderat, no una mitja de les calories consumides, exactament si o que al calcul hauria de tenri mes pes
        //els intervals en que el guany de pes es menor o major!

        //TODO: tb podria ser adjustedCals o  adjustedCals+(mitja d'esport que portem darrerament)
        this.recomendedCals =  this.consumedCals;

    }


    public void validate() throws Exception {
        //dont know how
        throw new Exception("Pendent de implementar");


    }


    public static class SortStats implements Comparator<WeightStats> {
        @Override
        public int compare(WeightStats a, WeightStats b) {

            //log.info("[m:compare] Comparem " + a.getRecomendedCals() + " (" + a.getAdjustedCals() + ") amb " + b.getRecomendedCals() + " (" + b.getAdjustedCals() + ")");

            //  if( comp.compare(weightLossStats, maintenanceStats) > 0
            //recomendedCals == consumedCals
            if( a.isNotEnoughData() || a.getRecomendedCals() < b.getRecomendedCals() )
            {
                /*
                if(a.getAdjustedCals() < b.getAdjustedCals()) {

                    log.info("[m:compare] Retornem -1 (a és menor que b) ===> " + a.getRecomendedCals()  + " < " + b.getRecomendedCals() + " (i " + a.getAdjustedCals()  + "<" + b.getAdjustedCals() + ")");
                    return -1;
                }

                log.info("[m:compare] Retornem 0 (a és igual que b) ===> " + a.getRecomendedCals()  + " < " + b.getRecomendedCals() + " (i " + a.getAdjustedCals()  + " > " +  b.getAdjustedCals() + ")");
                return 0;
                */

                log.info("[m:compare] Retornem -1 (a és menor que b) ===> " + a.getRecomendedCals()  + " < " + b.getRecomendedCals());
                return -1;
            }

            if( b.isNotEnoughData() || a.getRecomendedCals() > b.getRecomendedCals() )
            {
                /*
                if(a.getAdjustedCals()> b.getAdjustedCals()) {
                    log.info("[m:compare] Retornem 1 (a és més gran que b)===> " + a.getRecomendedCals()  + " > " + b.getRecomendedCals() + " (i " + a.getAdjustedCals()  + " > " +  b.getAdjustedCals() + ")");
                    return 1;
                }
                log.info("[m:compare] Retornem 0 (a es igual que b) ===> " + a.getRecomendedCals()  + " > " + b.getRecomendedCals() + " (i " + a.getAdjustedCals()  + " < " +  b.getAdjustedCals() + ")");
                return 0;*/

                log.info("[m:compare] Retornem 1 (a és més gran que b) ===> " + a.getRecomendedCals()  + " > " + b.getRecomendedCals());
                return 1;
            }


            log.info("[m:compare] Retornem 1 (son iguals) ===> " + a.getRecomendedCals()  + " = " + b.getRecomendedCals());

            return 0;

        }
    }


    final static WeightStats getWeightLossStatsInstance(){
        return new WeightStats(Constants.TYPE_WEIGHT_LOSS);
    }

    final static WeightStats getWeightGainStatsInstance(){
        return new  WeightStats(Constants.TYPE_WEIGHT_GAIN);
    }

    final static WeightStats getWeightMaintenanceStatsInstance(){return new  WeightStats(Constants.TYPE_WEIGHT_MAINTENANCE);}
}
