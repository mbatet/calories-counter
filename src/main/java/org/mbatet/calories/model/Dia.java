package org.mbatet.calories.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Comparator;
import java.util.Date;

public class Dia {

    private static final Log log = LogFactory.getLog(Dia.class.getName());

    Date date;
    Float weight;
    Float adjustedWeight;
    Integer consumedCals;
    Integer activityCals;

    //Aquestes 3 podrien anar a una altra classe que representes un objecte Importable
    String linia;
    String observacions;
    String errorDescription;

    public Dia()
    {
    }

    public Dia(Date date, Float pes, Integer consumedCals)
    {
        this.date = date;
        this.weight = pes;
        this.consumedCals = consumedCals;
    }


    public String toString(){
        return "date:"+date +".pes:"   + weight +".calories:" + consumedCals + ".error:"+errorDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getConsumedCals() {return consumedCals;}

    public void setConsumedCals(Integer consumedCals) {
        this.consumedCals = consumedCals;
    }

    public String getLinia() {
        return linia;
    }

    public void setLinia(String linia) {
        this.linia = linia;
    }

    public String getObservacions() {
        return observacions;
    }

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Float getAdjustedWeight() {return adjustedWeight;}

    public void setAdjustedWeight(Float adjustedWeight) {this.adjustedWeight = adjustedWeight;}

    public Integer getActivityCals() {return activityCals;}

    public void setActivityCals(Integer activityCals) {this.activityCals = activityCals;}

    public void validate()
    {
        //do something

        if(this.weight ==null)
        {
            setErrorDescription("Error parsejant linia: " + linia + " - Ens falta el pes");
        }
        if(this.consumedCals==null)
        {
            setErrorDescription("Error parsejant linia: " + linia + " - Ens falten les calories consumides");
        }
        if(this.date==null) {
            setErrorDescription("Error parsejant linia: " + linia + " - Ens falten la data");
        }
    }


    public static class SortDies implements Comparator<Dia> {
        @Override
        public int compare(Dia a, Dia b) {



            if( a==null || a.getDate() == null || a.getDate().before(b.getDate()) )
            {
                return -1;
            }

            if( b==null || b.getDate()==null || a.getDate().after(b.getDate()) )
            {
                return 1;
            }


                return 0;

        }
    }
}


