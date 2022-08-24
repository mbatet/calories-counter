package org.mbatet.calories.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.service.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Interval {



    //private boolean general = false;


    private static final Log log = LogFactory.getLog(Interval.class.getName());

    List<Dia> dies = new ArrayList<Dia>();


    public Interval(){

    }

    public Interval( List<Dia> dies )
    {
        this.dies = dies;
    }


    public String toString()
    {
        return "dies:" + dies.size() + ".first:"+getFirstDate() + ".last:"+getLastDate() + ".avgCalories:" + getAvgConsumedCals() + ".weightDiff:" + getWeigthDiff();
    }

    /*
    public boolean isGeneral() {
        return general;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }*/

    public List<Dia> getDies() {
        return dies;
    }

    public void setDies(List<Dia> dies) {
        // Assegruar de ordenar el dies!!!! NO ho fem pq en principi ja els hem ordenat abans!
        //Collections.sort(dies, new Dia.SortDies());
        this.dies = dies;
    }

    public void addDia(Dia dia) {
        // Assegruar de ordenar el dies!!!! NO ho fem pq en principi ja els hem ordenat abans!
        //Collections.sort(dies, new Dia.SortDies());
        this.dies.add(dia);
    }


    public Dia getFirstDay()
    {
        if(this.dies.size()==0)
        {
            return null;
        }

        return this.dies.get(0);

    }

    public Dia getLastDay()
    {
        if(this.dies.size()==0)
        {
            return null;
        }

        return this.dies.get(this.dies.size()-1);
    }

    public Float getFirstPes()
    {
        if(this.dies.size()==0)
        {
            return null;
        }
        return this.dies.get(0).getWeight();

    }

    public Float getLastPes()
    {
        if(this.dies.size()==0)
        {
            return null;
        }

        return this.dies.get(this.dies.size()-1).getWeight();
    }

    public Float getFirstAdjustedWeight()
    {
        if(this.dies.size()==0)
        {
            return null;
        }

        float pes = this.dies.get(0).getAdjustedWeight();

        return Utils.round(pes);

    }

    public Float getLastAdjustedWeight()
    {
        if(this.dies.size()==0)
        {
            return null;
        }

        float pes = this.dies.get(this.dies.size()-1).getAdjustedWeight();

        return Utils.round(pes);
    }


    public Date getFirstDate()
    {
        if(this.dies.size()==0)
        {
            return null;
        }

        return this.dies.get(0).getDate();

    }


    public Date getLastDate()
    {
        if(this.dies.size()==0)
        {
            return null;
        }

        return this.dies.get(this.dies.size()-1).getDate();
    }


    public float getFirstWeight()
    {
        if(this.dies.size()==0)
        {
            return 0F;
        }

        float pes =  this.dies.get(0).getWeight();


        return Utils.round(pes);

    }


    public float getLastWeight()
    {
        if(this.dies.size()==0)
        {
            return 0F;
        }

        float pes =   this.dies.get(this.dies.size()-1).getWeight();

        return Utils.round(pes);
    }


    //tindria mes logica que fos amb el pes ponderat, crec que aportaria mes info
    public float getWeigthDiff()
    {

        if( this.dies.size()==0 )
        {
            return 0F;
        }

        //float weightDiff = getLastPes() - getFirstPes();
        float weightDiff = getLastAdjustedWeight() - getFirstAdjustedWeight();
        float rounded = Utils.round(weightDiff);

        return rounded;
    }


    public int getType()
    {


       // if(getWeigthDiff() <= 0 ) //hem perdut pes
        if( getWeigthDiff() < -Constants.MIN_AMMOUNT_WE_CONSIDER_IS_LOSING_WEIGHT )
        {
            //hem perdut pes
            log.info("[m:getType]  getWeigthDiff: " + getWeigthDiff()  + ": Retornem TYPE_WEIGHT_LOSS");
            return Constants.TYPE_WEIGHT_LOSS;

        }

        //	0.03 Kg
        //hem perdut entre 0 i MIN_AMMOUNT_WE_CONSIDER_IS_LOSING_WEIGHT
        if(getWeigthDiff() <= 0.0 ){

            log.info("[m:getType]  getWeigthDiff: " + getWeigthDiff()  + ": Retornem TYPE_WEIGHT_MAINTENANCE");
            return Constants.TYPE_WEIGHT_MAINTENANCE;
        }

        //HEM GUANYAT PES
        log.info("[m:getType]  getWeigthDiff: " + getWeigthDiff()  + ": Retornem TYPE_WEIGHT_GAIN");
        return Constants.TYPE_WEIGHT_GAIN;


    }

    //TODO: FER TEST PER ASSEGURAR QUE EL AVG DESCOMPTA BE ELS DIES NULLS
    public float getAvgConsumedCals()
    {

        if(this.dies.size()==0)
        {
            return 0;
        }

        int sumaCals=0;
        int diesTotals=0;

        for(Dia dia:dies){
            //perque podria ser que algun dia no tingeussim dades
            log.info("[m:getAvgConsumedCals] dia.getConsumedCals: " + dia.getConsumedCals());
            if(dia.getConsumedCals()!=null) {
                sumaCals += dia.getConsumedCals();
                diesTotals++;
            }

            //log.info("[m:getAvgConsumedCals][" + diesTotals + "] sumaCals: " + sumaCals);
        }

        log.info("[m:getAvgConsumedCals] diesTotals: " + diesTotals);
        return sumaCals/diesTotals;

    }

    public float getAvgActivityCals()
    {

        if(this.dies.size()==0)
        {
            return 0;
        }

        int sumaCals=0;
        int diesTotals=0;

        for(Dia dia:dies){
            //perque podria ser que algun dia no tingeussim dades
            if(dia.getActivityCals()!=null) {
                sumaCals += dia.getActivityCals();
                diesTotals++;
            }
        }

        return sumaCals/diesTotals;

    }

    public float getAvgAdjustedCals()
    {

        if(this.dies.size()==0)
        {
            return 0;
        }

        int sumaCals=0;
        int diesTotals=0;

        for(Dia dia:dies){
            //perque podria ser que algun dia no tingeussim dades
            if( dia.getConsumedCals()!=null && dia.getActivityCals()!=null) {
                sumaCals += (dia.getConsumedCals()-dia.getActivityCals());
                diesTotals++;
            }
        }

        return sumaCals/diesTotals;

    }

}
