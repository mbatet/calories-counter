package org.mbatet.calories.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Dia;
import org.mbatet.calories.model.Interval;
import org.mbatet.calories.model.stats.GeneralStats;
import org.mbatet.calories.model.stats.IntervalStats;
import org.mbatet.calories.service.parser.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ParseService {

    private static final Log log = LogFactory.getLog(ParseService.class.getName());


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

        //dies.forEach( (final Dia dia) -> { if(dia==null) dies.remove(dia); } );
        while (dies.remove(null));


        log.info("[m:parse] hem parsejat: " + dies.size()  + " dies");

        Collections.sort(dies, new Dia.SortDiesByDate());

        log.info("[m:parse] dies ordenats");

        dies.forEach( (final Dia dia) -> log.debug("[m:parse] dia ordenat:" + dia.getDate()));

        fillInMissingWeights(dies);

        //TODO: AIxò hauria d'anar a WeightStatsService
        calculateAdjustedWeights(dies);

        log.info("[m:parse] pesos ponderats");


        return dies;

    }








    private Dia parseLine(String line)  {

        log.debug("[m:parseLine] line to parse: " + line);
        Dia dia = parser.parse(line);
        log.debug("[m:parseLine] dia: " + dia);



        return dia;

    }

    private void fillInMissingWeights(List<Dia> dies)  {

        //TODO: what do we do when days are missing? we should insert the dates even if there are void of data...
        //TODO:guarantee there are not duplicate date

        //Pel moment, omplim els pesos que no tinguem, que es elq ue mes necessitem, sense la resta podem passar


        int apuntador = 0;
        for(Dia dia: dies)
        {
            log.debug("[m:fillInMissingWeights][" + apuntador+"/"+ dies.size() + "] dia: " + dia);
            if( dia.getWeight()==null )
            {
                //if(apuntador-1 >= 0 && apuntador+1 <= dies.size()) {

                Float beforeWeight = dies.get(apuntador - 1).getWeight();// Aquest sempre esta ple
                log.debug("[m:fillInMissingWeights][" + apuntador+"/"+ dies.size() + "] beforeWeight: " + beforeWeight);

                Float afterWeight = null; //Pot estar tb buit!

                int nextDay = apuntador+1;
                while  ( afterWeight == null && nextDay < dies.size()){
                   // log.debug("[m:fillInMissingWeights][" + apuntador+"/"+ dies.size() + "] nextDay: " + nextDay);
                    afterWeight = dies.get(nextDay).getWeight();
                    nextDay++;
                }

                //log.debug("[m:fillInMissingWeights][" + apuntador+"/"+ dies.size() + "] afterWeight: " + afterWeight);

                //no sempre tenim afterWeight, si la última linia tampoc te el pes informat, el while no ha servit de res. Podem posar el pes del dia anterior o simplement descartar ek dua
                if( afterWeight == null )
                {
                    log.error("[m:fillInMissingWeights][" + apuntador+"/"+ dies.size() + "] Error linia " + apuntador + " -  dia: " + dia + " - No podem estimar el darrer pes. Descartem aquest dia.");
                    dies.remove(dia);
                    return;
                }


                log.error("[m:fillInMissingWeights][" + apuntador+"/"+ dies.size() + "] Omplim el pes del dia " + apuntador + " amb la mitja entre beforeWeight i afterWeight = ((" + beforeWeight + " + " + afterWeight + ")/2)");
                dia.setWeight( ((beforeWeight + afterWeight)) / 2f);


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

            int first = i - Constants.DAYS_WINDOW;
            int last = i + Constants.DAYS_WINDOW;

            first = ( first < 0 ) ? 0 : first;
            last = ( last > dies.size()-1) ? dies.size()-1 : last;


            Float sumaPesos = 0F;
            int numPesos = 0;
            for(int apuntador=first; apuntador<=last; apuntador++)
            {
                //TODO: what do we do if weigh is empty?
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
