package org.mbatet.calories.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Dia;
import org.mbatet.calories.model.Interval;
import org.mbatet.calories.model.Stats;
import org.mbatet.calories.service.parser.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
public class WeightStatsService {

    private static final Log log = LogFactory.getLog(WeightStatsService.class.getName());


    @Autowired
    CsvParser parser;


    //@Autowired
    //private ApplicationContext applicationContext;



    public List<Dia> parse (String textToParse)
    {

        List<Dia> dies = new ArrayList<Dia>();


        //log.debug("[m:parse] textToParse: " + textToParse);

        /**
         * Dividim en linies i processem cada linia una a una amb el parseLine(linia)
         * El resultat de cada parseLine(linia) es un objecte de tipus CanviUnitat, validat,
         * del que podrem extreure un o varis sqls per mostrar per pantalla.
         * Un cop tenim totes les linies parsejades i obtenim un llistat de canvis, els ordenem
         * Ordenem canvis per mida de codiAntic, a mes llarg, primer, aixi ens assegurem de fer primer els canvis concrets
         * */
        List<String> llistaLinies = new ArrayList<String>(Arrays.asList(textToParse.split("\n")));

        llistaLinies.forEach( (final String linia) -> dies.add(parseLine(linia)));
        log.info("[m:parse] hem parsejat: " + dies.size()  + " dies");

        Collections.sort(dies, new Dia.SortDies());

        log.info("[m:parse] dies ordenats");

        dies.forEach( (final Dia dia) -> log.debug("[m:parse] dia ordenat:" + dia.getDate()));

        afegirDadesQueFalten(dies);
        calcularPesosPonderats(dies);

        log.info("[m:parse] pesos ponderats");


        return dies;

    }


    public Stats getStatsFromIntervals( List<Interval> intervals)
    {

        if( intervals==null || intervals.size()==0 )
        {
            return new Stats();
        }

        //TODO: fer una utilitat que faci aquest codi, que es repeteix en cada bloc
        //TODO: Arreglar noms de variables!!!
        Float weightLossAvrgCals  = 0F;
        Float weightGainAvrgCals  = 0F;
        Float maintenanceAvrgeCals = 0F;

        //les calories d'activitat que hem fet en peridoes de perdua/ganancia/manteniment
        Float weightLossAvgActivityCals  = 0F;
        Float weightGainAvgActivityCals  = 0F;
        Float maintenanceAvgActivityCals = 0F;

        //Havent restat les calories del exercici
        Float weightLossAvgAdjustedCals = 0F;
        Float weightGainAvgAdjustedCals = 0F;
        Float maintenanceAvgAdjustedCals = 0F;

        int weightLossIntervals  = 0;
        int weightGainIntervals  = 0;
        int maintenanceIntervals = 0;




        for(Interval interval: intervals)
        {

            //TODO: fer una utilitat que faci aquest codi, que es repeteix en cada bloc
            if( interval.getWeigthDiff() > Constants.MIN_AMMOUNT_WE_CONSIDER_IS_LOSING_WEIGHT)
            {

                //fer una utilitat que faci aquest codi, que es repeteix en cada bloc
                weightGainAvrgCals+=interval.getAvgConsumedCals();
                weightGainAvgActivityCals+=interval.getAvgActivityCals();
                weightGainAvgAdjustedCals+=interval.getAvgAdjustedCals();
                weightGainIntervals++;

            }
            else if( interval.getWeigthDiff() < -Constants.MIN_AMMOUNT_WE_CONSIDER_IS_LOSING_WEIGHT ) //hem perdut pes
            {
                weightLossAvrgCals+=interval.getAvgConsumedCals();
                weightLossAvgActivityCals+=interval.getAvgActivityCals();
                weightLossAvgAdjustedCals+=interval.getAvgAdjustedCals();
                weightLossIntervals++;
            }
            else
            {
                maintenanceAvrgeCals+=interval.getAvgConsumedCals();
                maintenanceAvgActivityCals+=interval.getAvgActivityCals();
                maintenanceAvgAdjustedCals+=interval.getAvgAdjustedCals();
                maintenanceIntervals++;
            }


        }


        Stats stats = new Stats();

       if( weightGainIntervals > 0 )
       {
           stats.setWeightGainAvgCals(weightGainAvrgCals/(float)weightGainIntervals);
           stats.setWeightGainActivityAvgCals(weightGainAvgActivityCals/(float)weightGainIntervals);
           stats.setWeightGainAvgAdjustedCals(weightGainAvgAdjustedCals/(float)weightGainIntervals);

       }

       if( maintenanceIntervals > 0 )
       {
           stats.setMaintenanceAvgCals(maintenanceAvrgeCals/(float)maintenanceIntervals);
           stats.setMaintenanceActivityAvgCals(maintenanceAvgActivityCals/(float)maintenanceIntervals);
           stats.setMaintenanceAvgAdjustedCals(maintenanceAvgAdjustedCals/(float)maintenanceIntervals);
       }

        if( weightLossIntervals > 0 )
        {
            stats.setWeightLossAvgCals(weightLossAvrgCals/(float)weightLossIntervals);
            stats.setWeightLossActivityAvgCals(weightLossAvgActivityCals/(float)weightLossIntervals);
            stats.setWeightLossAvgAdjustedCals(weightLossAvgAdjustedCals/(float)weightLossIntervals);
        }


        stats.validate();

        return stats;
    }

    public  List<Interval> getIntervals(List<Dia> dies)
    {
        List<Interval> intervals = new ArrayList<Interval>();
        Interval intervalGeneral = new Interval(dies);
        intervals.add(intervalGeneral);

        int count = 0;
        Interval interval = new Interval();
        intervals.add(interval);


        for(Dia dia:dies){

            interval.addDia(dia);
            count++;

            if( count == Constants.DEFAULT_MIDA_INTERVAL )
            {
                interval = new Interval();
                intervals.add(interval);
                count=0;

            }
        }

        Interval last = intervals.get(intervals.size()-1);

        if(last.getDies().size()< Constants.MIN_MIDA_INTERVAL)
        {
            intervals.remove(last);
        }

       //intervals.forEach( (final Interval intrvl) -> intrvl.validate());

        intervals.forEach( (final Interval intrvl) -> log.debug("[m:getIntervals] interval:" + intrvl));


        return intervals;
    }



    private Dia parseLine(String line)  {

        log.debug("[m:parseLine] line to parse: " + line);
        Dia dia = parser.parse(line);
        log.debug("[m:parseLine] dia: " + dia);



        return dia;

    }

    private void afegirDadesQueFalten(List<Dia> dies)  {

        //TODO: Que fem amb els dates que falten... haurien de posar els dies ni que estiguin buits...

        //TODO: MIrar tb que no hi hagin dades repetides!!

        //Pel moment, omplim els pesos que no tinguem, que es elq ue mes necessitem, sense la resta podem passar



        int apuntador = 0;
        for(Dia dia: dies)
        {
            if(dia.getPes()==null)
            {
                dia.setPes( (dies.get(apuntador-1).getPes() + dies.get(apuntador+1).getPes())/2f);
            }
            apuntador++;
        }

    }


    private void calcularPesosPonderats(List<Dia> dies)  {

        Float[] array = new Float[dies.size()];

        int i =0;
        for(Dia dia:dies){
            array[i++]=dia.getPes();
        }


        i =0;
        for(Dia dia:dies){

            int first = i - Constants.FINESTRA_DIES_ENDAVANT_I_ENDARRERA;
            int last = i + Constants.FINESTRA_DIES_ENDAVANT_I_ENDARRERA;

            first = ( first < 0 ) ? 0 : first;
            last = ( last > dies.size()-1) ? dies.size()-1 : last;


            Float sumaPesos = 0F;
            int numPesos = 0;
            for(int apuntador=first; apuntador<=last; apuntador++)
            {
                //TODO: Que fem quan el pes es nul??
                if(array[apuntador]!=null) {
                    sumaPesos += array[apuntador];
                    numPesos++; //ho fem aixi perque podriem tenir algun pes a null
                }
            }


            //Float pesPonderat = sumaPesos/(float)(last-first+1);
            Float pesPonderat = sumaPesos/(float)(numPesos);

            log.debug("i:" + i + " - first:" + first + " - last:" + last + " - pesPonderat =" + sumaPesos  + "/" + numPesos + " - pesPonderat: " + pesPonderat);


            dia.setPesPonderat(pesPonderat);

            i++;

        }
    }

}