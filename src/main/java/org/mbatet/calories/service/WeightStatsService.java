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


        Stats stats = new Stats();

        for(Interval interval: intervals)
        {

            if( interval.getType() == Interval.TYPE_WEIGHT_LOSS_INTERVAL )
            {
                stats.getWeightLossStats().addInterval(interval);

            }
            else if( interval.getType() == Interval.TYPE_WEIGHT_GAIN_INTERVAL )
            {
                stats.getWeightGainStats().addInterval(interval);

            }
            else //if(  interval.getType() == Interval.TYPE_MAINTENANCE_INTERVAL )
            {
                //ens hem mantingut
                stats.getMaintenanceStats().addInterval(interval);
            }
          

        }

        stats.calculate();
        //stats.validate();

        return stats;
    }

    public  Interval getLastWeek(List<Dia> dies)
    {

        return null;
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


            dia.setAdjustedWeight(pesPonderat);

            i++;

        }
    }

}
