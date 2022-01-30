package org.mbatet.calories.model.stats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Interval;
import org.mbatet.calories.service.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class AvgCalStats {

    //TODO: hauriem de tenir 3 tipus de Stats, una de WeightLossIntervalsStats, una de WeightGainIntervalsStats, una de MaintenanceIntervalsStats
    //TODO: o a la inversa, hauriem de tenir 3 tipus de Stats, una de AverageCalsStats, una de AdjustedCalStats, una de ActivityStats
    //TODO: O varies interficie diferents i la classe que creui les due sinterficie

    //Havent restat les calories del exercici
    Float activityCals = 0F;
    Float adjustedCals = 0F;
    Float consumedCals = 0F;

    Float recomendedCals;

    boolean notAlloweed  = false;



    List<Interval> intervals = new ArrayList<Interval>();


    private static final Log log = LogFactory.getLog(AvgCalStats.class.getName());

    public AvgCalStats()
    {

    }

    public abstract String getTitle();

    public boolean isNotAllowed()
    {

        if(this.intervals.size()==0)
        {
            return true;
        }

        return this.notAlloweed;
    }

    public void setNotAllowed(boolean notAllowed)
    {
        this.notAlloweed = notAllowed;
    }


    public String toString()
    {

        return "activityCals:" + activityCals + " - consumedCals: " + consumedCals + " - adjustedCals: " + adjustedCals;
    }

    public Float getActivityCals() {return Utils.roundToHundreds(activityCals);}

    public void setActivityCals(Float activityCals) {
        this.activityCals = activityCals;
    }

    public Float getAdjustedCals() {return Utils.roundToHundreds(adjustedCals);}

    public void setAdjustedCals(Float adjustedCals) {
        this.adjustedCals = adjustedCals;
    }


    public Float getConsumedCals() {return Utils.roundToHundreds(consumedCals);}

    public void setConsumedCals(Float consumedCals) {
        this.consumedCals = consumedCals;
    }

    public Float getRecomendedCals() {return recomendedCals;}

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

        this.recomendedCals =  this.consumedCals;

    }


    public void validate() throws Exception {
        //dont know how
        throw new Exception("Pendent de implementar");


    }
}
