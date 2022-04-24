package org.mbatet.calories.model.stats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Interval;
import org.mbatet.calories.service.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class WeightStats {

    //Havent restat les calories del exercici
    Float activityCals = 0F;
    Float adjustedCals = 0F;
    Float consumedCals = 0F;

    //Es igual que a consumedCals pero pot ser null, cosa que consumedCals semrpe hot enim, com a mínim, a 0
    Float recomendedCals;

    boolean notEnoughData = false;



    List<Interval> intervals = new ArrayList<Interval>();


    private static final Log log = LogFactory.getLog(WeightStats.class.getName());

    public WeightStats()
    {

    }

    public abstract String getTitle();

    public boolean isNotEnoughData()
    {
        if(this.intervals.size()==0)
        {
            return true;
        }

        return this.notEnoughData;
    }

    public void setNotEnoughData(boolean notEnough)
    {
        this.notEnoughData = notEnough;
    }


    public String toString()
    {

        return "activityCals:" + activityCals + " - consumedCals: " + consumedCals + " - adjustedCals: " + adjustedCals;
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
}
