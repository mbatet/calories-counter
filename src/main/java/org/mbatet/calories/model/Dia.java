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
        return "date:"+date +".pes:"   + weight +".calories:" + consumedCals + ".error:"+(errorDescription!=null?errorDescription:"");
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
            setErrorDescription("Parsing linie error: Missing weight.");
        }
        if(this.consumedCals==null)
        {
            setErrorDescription("Parsing linie error:  Missing calories");
        }
        if(this.date==null) {
            setErrorDescription("Parsing linie error:  Missing date");
        }
    }



    public static class SortDiesByDate implements Comparator<Dia> {
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

    /*

    public static class SortDiesByWeight implements Comparator<Dia> {
        @Override
        public int compare(Dia a, Dia b) {



            if( (a==null && b==null) || (a.getWeight() == null && b.getWeight() == null) )
            {
                log.debug("[m:compare] a i b son nulls retornem 0");
                return 0;
            }

            //a>b ==> 1
            //a<b ==> -11

            //a es null pero b no
            if(  b.getWeight() !=null && (a==null || a.getWeight() == null ))
            {
                log.debug("[m:compare] a es null i b no es null o a<b retornem -1");
                return -1;
            }

            if(  a.getWeight() !=null && (b==null || b.getWeight() == null ))
            {
                log.debug("[m:compare] a es null i b no es null o a<b retornem -1");
                return 1;
            }


            if( a.getWeight()<b.getWeight() )
            {
                log.debug("[m:compare] a es null i b no es null o a<b retornem -1");
                return -1;
            }


            if( a.getWeight()>b.getWeight() )
            {
                log.debug("[m:compare] a > b retornem 1");
                return 1;
            }


            return 0;

        }
    }*/
}


