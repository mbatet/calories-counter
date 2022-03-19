package org.mbatet.calories.service.parser;

import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Dia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CsvParser implements Parser {

    private static final Logger log = LoggerFactory.getLogger(CsvParser.class);



    public Dia parse(String liniaCsv) {

        //FORMAT CODI_ANTIC;NOM_ANTIC;CODI_NOU;NOM_NOU;TIPUS_CANVI_TIPIFICAT;OBSERVACIONS

        if(liniaCsv==null || liniaCsv.trim().equals(""))
        {
            log.error("[m:getInstance] Linia en blanc");
            return null;
        }

        Dia dia = new Dia();
        dia.setLinia(liniaCsv);

        String[] fields = liniaCsv.split(";");

        if(fields.length < 3)
        {
            log.error("[m:getInstance] Error parsejant linia: " + liniaCsv + " - Ens falta algun camp");
            dia.setErrorDescription("Error parsejant linia: " + liniaCsv + " - Ens falta algun camp");
            //return dia;

        }




        try {

            dia.setDate(Constants.FORMAT_DATE.parse(fields[0]));
            if(!"".equals(fields[1])) dia.setWeight(Float.parseFloat(fields[1])); //QUe passa si no existeix aquest pes?
            if(!"".equals(fields[2])) dia.setConsumedCals(Integer.parseInt(fields[2]));
            if(!"".equals(fields[3])) dia.setActivityCals(Integer.parseInt(fields[3]));
            if(fields.length>4 ) dia.setObservacions(fields[4]);

            dia.validate();




        } catch (Exception e) {
            log.error("[m:getInstance] Error parsejant linia: " + liniaCsv + ": "+ e.getMessage());
            dia.setErrorDescription( "Error parsejant linia: " + liniaCsv + ": "+ e.getMessage());
        }



        return dia;
    }
}
