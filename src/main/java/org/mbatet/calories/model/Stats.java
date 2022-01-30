package org.mbatet.calories.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mbatet.calories.service.Utils;

public class Stats {

    //TODO: hauriem de tenir 3 tipus de Stats, una de WeightLossIntervalsStats, una de WeightGainIntervalsStats, una de MaintenanceIntervalsStats
    //TODO: o a la inversa, hauriem de tenir 3 tipus de Stats, una de AverageCalsStats, una de AdjustedCalStats, una de ActivityStats
    //TODO: O varies interficie diferents i la classe que creui les due sinterficie

    //Havent restat les calories del exercici
    Float weightLossAvgCals;
    Float weightGainAvgCals;
    Float maintenanceAvgCals;

    //TODO: Arreglar noms de variables!!!

    Float weightLossActivityAvgCals;
    Float weightGainActivityAvgCals;
    Float maintenanceActivityAvgCals;

    //Havent restat les calories del exercici
    Float weightLossAvgAdjustedCals;
    Float weightGainAvgAdjustedCals;
    Float maintenanceAvgAdjustedCals;


    private static final Log log = LogFactory.getLog(Stats.class.getName());

    public Stats()
    {

    }


    public String toString()
    {

        return "weightLossAvgCals:" + weightLossAvgCals + " - maintenanceAvgCals: " + maintenanceAvgCals + " - weightGainAvgCals: " + weightGainAvgCals;
    }

    public Float getWeightLossAvgCals() {

        return Utils.roundToHundreds(weightLossAvgCals);
    }

    public void setWeightLossAvgCals(Float weightLossAvgCals) {
        this.weightLossAvgCals = weightLossAvgCals;
    }

    public Float getWeightGainAvgCals() {

        return Utils.roundToHundreds(weightGainAvgCals);
    }

    public void setWeightGainAvgCals(Float weightGainAvgCals) {
        this.weightGainAvgCals = weightGainAvgCals;
    }


    public Float getMaintenanceAvgCals() {

        return Utils.roundToHundreds(maintenanceAvgCals);
    }

    public void setMaintenanceAvgCals(Float maintenanceAvgCals) {
        this.maintenanceAvgCals = maintenanceAvgCals;
    }


    public Float getWeightLossAvgAdjustedCals() {

        return Utils.roundToHundreds(weightLossAvgAdjustedCals);
    }

    public void setWeightLossAvgAdjustedCals(Float weightLossAvgAdjustedCals) {
        this.weightLossAvgAdjustedCals = weightLossAvgAdjustedCals;
    }

    public Float getWeightGainAvgAdjustedCals() {

        return Utils.roundToHundreds(weightGainAvgAdjustedCals);
    }

    public void setWeightGainAvgAdjustedCals(Float weightGainAvgAdjustedCals) {
        this.weightGainAvgAdjustedCals = weightGainAvgAdjustedCals;
    }


    public Float getMaintenanceAvgAdjustedCals() {

        return Utils.roundToHundreds(maintenanceAvgAdjustedCals);
    }

    public void setMaintenanceAvgAdjustedCals(Float maintenanceAvgAdjustedCals) {
        this.maintenanceAvgAdjustedCals = maintenanceAvgAdjustedCals;
    }

    public Float getWeightLossActivityAvgCals() {
        return Utils.roundToHundreds(weightLossActivityAvgCals);
    }

    public void setWeightLossActivityAvgCals(Float weightLossActivityAvgCals) {
        this.weightLossActivityAvgCals = weightLossActivityAvgCals;
    }

    public Float getWeightGainActivityAvgCals() {
        return Utils.roundToHundreds(weightGainActivityAvgCals);
    }

    public void setWeightGainActivityAvgCals(Float weightGainActivityAvgCals) {
        this.weightGainActivityAvgCals = weightGainActivityAvgCals;
    }

    public Float getMaintenanceActivityAvgCals() {
        return Utils.roundToHundreds(maintenanceActivityAvgCals);
    }

    public void setMaintenanceActivityAvgCals(Float maintenanceActivityAvgCals) {
        this.maintenanceActivityAvgCals = maintenanceActivityAvgCals;
    }

    public void validate()
    {

        //TODO: falta valdiar les caloreis de activitat en intervals de loss,gain, maintenance, es a dir, falta un blco mes de comprovacions
        //TODO: els valors de maintenance ara mateix no tenen sentit
        //TODO: cal validar d'una altra manera, amb una funcio a banda, que no es repetieixi codi

        log.info("[m:validate] weightLossAvgCals: " + weightLossAvgCals);
        log.info("[m:validate] maintenanceAvgCals: " + maintenanceAvgCals);
        log.info("[m:validate] weightGainAvgCals: " + weightGainAvgCals);

        //TODO: fer una utilitat que faci aquest codi, que es repeteix en cada bloc
        if(weightGainAvgCals !=null && maintenanceAvgCals !=null && maintenanceAvgCals > weightGainAvgCals)
        {
            log.info("[m:validate] Les calories de manteniment no haurien de superar les calories de guany de pes");
            maintenanceAvgCals = weightGainAvgCals;
        }

        if(weightLossAvgCals !=null && maintenanceAvgCals !=null && maintenanceAvgCals < weightLossAvgCals)
        {
            log.info("[m:validate] Les calories de perdua de pes no haurien de superar les calories de manteniment");
            weightLossAvgCals = maintenanceAvgCals;
        }

        if(weightLossAvgCals !=null && weightGainAvgCals !=null && weightGainAvgCals < weightLossAvgCals)
        {
            log.info("[m:validate] Les calories de perdua de pes no haurien de superar les calories de guany de pes");
            weightLossAvgCals = weightGainAvgCals;
        }


        if( maintenanceAvgCals==null && weightLossAvgCals !=null && weightGainAvgCals !=null )
        {
            //log.info("[m:validate] Posem les calories de manteniment a la meitad de les de perdua i guany");
            //maintenanceAvgCals = weightLossAvgCals + (weightGainAvgCals-weightLossAvgCals)/2;
        }

        if(weightGainAvgAdjustedCals !=null && maintenanceAvgAdjustedCals !=null && maintenanceAvgAdjustedCals > weightGainAvgAdjustedCals)
        {
            log.info("[m:validate] Les calories de manteniment no haurien de superar les calories de guany de pes");
            maintenanceAvgAdjustedCals = weightGainAvgAdjustedCals;
        }

        if(weightLossAvgAdjustedCals !=null && maintenanceAvgAdjustedCals !=null && maintenanceAvgAdjustedCals < weightLossAvgAdjustedCals)
        {
            log.info("[m:validate] Les calories de perdua de pes no haurien de superar les calories de manteniment");
            weightLossAvgAdjustedCals = maintenanceAvgAdjustedCals;
        }

        if(weightLossAvgAdjustedCals !=null && weightGainAvgAdjustedCals !=null && weightGainAvgAdjustedCals < weightLossAvgAdjustedCals)
        {
            log.info("[m:validate] Les calories de perdua de pes no haurien de superar les calories de guany de pes");
            weightLossAvgAdjustedCals = weightGainAvgAdjustedCals;
        }


        if( maintenanceAvgAdjustedCals==null && weightLossAvgAdjustedCals !=null && weightGainAvgAdjustedCals !=null )
        {
            //log.info("[m:validate] Posem les calories de manteniment a la meitad de les de perdua i guany");
            //maintenanceAvgAdjustedCals = weightLossAvgAdjustedCals + (weightGainAvgAdjustedCals-weightLossAvgAdjustedCals)/2;
        }

    }
}
