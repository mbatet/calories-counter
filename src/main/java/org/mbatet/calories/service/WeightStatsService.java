package org.mbatet.calories.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Dia;
import org.mbatet.calories.model.Interval;
import org.mbatet.calories.model.stats.*;
import org.mbatet.calories.service.parser.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class WeightStatsService {

    private static final Log log = LogFactory.getLog(WeightStatsService.class.getName());


    @Autowired
    CsvParser parser;


    //@Autowired
    //private ApplicationContext applicationContext;





    public GeneralStats getStatsFromData(List<Dia> dies)
    {


        List<Interval> intervals = getIntervals(dies);
        Interval intervalGeneral = getIntervalGeneral(dies);
        Interval lastWeek = getLastWeek(dies);


        GeneralStats stats = new GeneralStats(dies, intervals,  intervalGeneral, lastWeek);


        stats.calculate();
        //stats.validate();

        Float calsLeft = getCalsLeft( lastWeek, stats.getWeightLossStats());

        return stats;
    }



    //Els intervals no ens valen perque poden no començar en dilluns
    public Interval getLastWeek(List<Dia> dies)
    {
        Interval lastWeek = new Interval();
        //agafem un altre objecte per no revertir el objecte orgininal, no sigui que el necessitem
        List<Dia> diesReversed = new ArrayList<Dia>();
        diesReversed.addAll(dies);
        Collections.reverse(diesReversed);

        for(Dia dia: diesReversed){
            Calendar cal = Calendar.getInstance();
            cal.setTime(dia.getDate());

            lastWeek.addDia(dia);
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            {
                break;
            }

        }

        Collections.reverse(lastWeek.getDies());
        return lastWeek;
    }


    public  Interval getIntervalGeneral(List<Dia> dies)
    {

        Interval intervalGeneral = new Interval(dies);
        //intervalGeneral.setGeneral(true);

        return intervalGeneral;
    }

    public  List<Interval> getIntervals(List<Dia> dies)
    {
        List<Interval> intervals = new ArrayList<Interval>();
        //Interval intervalGeneral = new Interval(dies);
        //intervals.add(intervalGeneral);

        log.info("[m:getIntervals] dies: " + dies);

        if( dies == null ){
            log.error("[m:getIntervals] dies no pot ser null");
        }

        log.info("[m:getIntervals] dies.size: " + dies.size());

        int count = 0;
        Interval interval = new Interval();
        intervals.add(interval);


        for(Dia dia:dies){

            log.info("[m:getIntervals][" + count + "] dia: " + dia);
            interval.addDia(dia);
            count++;

            if( count == Constants.DEFAULT_INTERVAL_SIZE) {
                interval = new Interval();
                intervals.add(interval);
                count=0;
                log.info("[m:getIntervals][" + count + "] Obrim nou interval");

            }
        }

        Interval last = intervals.get(intervals.size()-1);

        if(last.getDies().size()< Constants.MINIMUM_INTERVAL_SIZE)
        {
            intervals.remove(last);
        }

       //intervals.forEach( (final Interval intrvl) -> intrvl.validate());

        intervals.forEach( (final Interval intrvl) -> log.debug("[m:getIntervals] interval:" + intrvl));


        return intervals;
    }





    public Float getCalsLeft(Interval lastWeek, IntervalStats weightLossStats){

        //TODO: canviar-ho per getCaloriesBelowMaintenance, o millor encara, agafar el menor dels dos valors
        Float recommendedCals = weightLossStats.getRecomendedCals();


        log.info("[m:getCalsLeft]  weightLossStats.getRecomendedCals: " + recommendedCals);

        if(lastWeek.getDies().size()==0)
        {
            log.info("[m:getCalsLeft] Last week no te dies. Retornem weightLossStats.getRecomendedCals: " + recommendedCals);
            return recommendedCals;
        }

        if(lastWeek.getDies().size()>=7)
        {
            log.info("[m:getCalsLeft] Last week te " + lastWeek.getDies().size() + " dies. Retornem 0");
            return 0F;
        }

        log.info("[m:getCalsLeft] Last week te " + lastWeek.getDies().size() + " dies. Calculem...");

        int sumaCals=0;
        int diesTotals=0;
        for(Dia dia:lastWeek.getDies()){
            //perque podria ser que algun dia no tingeussim dades
            log.info("[m:getCalsLeft] dia.getConsumedCals: " + dia.getConsumedCals());
            if(dia.getConsumedCals()!=null) {
                sumaCals += dia.getConsumedCals();
                diesTotals++;
            }
        }

        log.info("[m:getCalsLeft] sumaCals: " + sumaCals + " - diesTotals: " + diesTotals);

        //per si de cas tinguessim deis "buits" pero que ja han passat, no pendents de passar (no podem canviar els dies que ja han passat i no sabem que vam menjar)
        while( diesTotals < lastWeek.getDies().size() )
        {
            sumaCals += lastWeek.getAvgConsumedCals();
            diesTotals++;
        }

        log.info("[m:getCalsLeft] (despres de omplir els dies buits) sumaCals: " + sumaCals + " - diesTotals: " + diesTotals);



        Float calsLeft =  recommendedCals * 7 - sumaCals;
        log.info("[m:getCalsLeft] calsLeft: " + calsLeft);

        int daysLeft = 7 - diesTotals;
        log.info("[m:getCalsLeft] daysLeft: " + daysLeft);

        Float calsForDay = calsLeft/daysLeft;
        log.info("[m:getCalsLeft] calsLeft/daysLeft: " + calsForDay);

        if(calsLeft<0)
        {

            log.info("[m:getCalsLeft] Ens queden calories negatives... Retornem 0");
            return 0F;
        }

        return calsForDay;

    }

}
