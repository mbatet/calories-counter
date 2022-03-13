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
        calculateAdjustedWeights(dies);

        log.info("[m:parse] pesos ponderats");


        return dies;

    }


    public Stats getStatsFromIntervals( List<Interval> intervals)
    {


        Stats stats = new Stats();

        for(Interval interval: intervals){
            stats.addInterval(interval);
        }

        stats.calculate();
        //stats.validate();

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

        for(Dia dia: diesReversed)
        {
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
            if(dia.getWeight()==null)
            {
                dia.setWeight( (dies.get(apuntador-1).getWeight() + dies.get(apuntador+1).getWeight())/2f);
            }
            apuntador++;
        }

    }


    private void calculateAdjustedWeights(List<Dia> dies)  {

        Float[] array = new Float[dies.size()];

        int i =0;
        for(Dia dia:dies){
            array[i++]=dia.getWeight();
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


    public Float getCalsLeft(Interval lastWeek, WeightLossStats weightLossStats){

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
