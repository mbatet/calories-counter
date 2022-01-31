package org.mbatet.calories.model.stats;

public class Stats {

    /**
     * Només es una classe enbollcall per passar objecets a la vista
     * */

    WeightGainStats weightGainStats = new WeightGainStats();
    MaintenanceStats maintenanceStats = new MaintenanceStats();
    WeightLossStats weightLossStats = new WeightLossStats();



    public WeightGainStats getWeightGainStats() {
        return weightGainStats;
    }

    public void setWeightGainStats(WeightGainStats weightGainStats) {
        this.weightGainStats = weightGainStats;
    }

    public MaintenanceStats getMaintenanceStats() {
        return maintenanceStats;
    }

    public void setMaintenanceStats(MaintenanceStats maintenanceStats) {
        this.maintenanceStats = maintenanceStats;
    }

    public WeightLossStats getWeightLossStats() {
        return weightLossStats;
    }

    public void setWeightLossStats(WeightLossStats weightLossStats) {
        this.weightLossStats = weightLossStats;
    }

    public void calculate()
    {
        weightLossStats.calculate();
        maintenanceStats.calculate();
        weightGainStats.calculate();


        //necessitem mes dades
        if(weightLossStats.getRecomendedCals()!=null && weightLossStats.getRecomendedCals()>maintenanceStats.getRecomendedCals())
        {
            maintenanceStats.setNotEnoughData(true);
        }

        if(weightGainStats.getRecomendedCals()!=null && weightGainStats.getRecomendedCals()<maintenanceStats.getRecomendedCals())
        {
            weightGainStats.setNotEnoughData(true);
        }
    }


    /*
    public void validate()
    {
        //NO perque despres les dades no tenen cap tipsu de sentit
        //weightLossStats.changeRecomendedCalsIfSmaller(weightGainStats.getRecomendedCals());
        //weightLossStats.changeRecomendedCalsIfSmaller(maintenanceStats.getRecomendedCals());
        //weightGainStats.changeRecomendedCalsIfBigger(weightLossStats.getRecomendedCals());
        //weightGainStats.changeRecomendedCalsIfBigger(maintenanceStats.getRecomendedCals());
    }*/
}
