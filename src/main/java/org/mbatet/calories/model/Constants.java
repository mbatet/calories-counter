package org.mbatet.calories.model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Constants {



    static public int TYPE_WEIGHT_LOSS = 0;
    static public int TYPE_WEIGHT_GAIN = 1;

    static public String VIEW_INDEX = "index";
    static public String VIEW_CHART = "chart";


    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM/yyyy");
    //public static final SimpleDateFormat FORMAT_DATE_IN_TO_SHOW = new SimpleDateFormat("dd - MM - yyyyy");

    public final static int DAYS_WINDOW = 2;
    public final static int DEFAULT_INTERVAL_SIZE = 14;
    //public final static int DEFAULT_INTERVAL_SIZE = 30;
    public final static int MINIMUM_INTERVAL_SIZE = 4;

    //TODO: It should be configurable
    //public final static float MIN_AMMOUNT_WE_CONSIDER_IS_LOSING_WEIGHT = 0.15f;


    public final static String WHEIGHT_TRACKING_CHART_RAW_WEIGHTS = "WHEIGHT_TRACKING_CHART_RAW_WEIGHTS";
    public final static String WHEIGHT_TRACKING_CHART_ADJUSTED_WEIGHTS = "WHEIGHT_TRACKING_CHART_ADJUSTED_WEIGHTS";
    public final static String WHEIGHT_TRACKING_CHART_TWO_SERIES_RAW_AND_ADJUSTED = "WHEIGHT_TRACKING_CHART_TWO_SERIES_RAW_AND_ADJUSTED";

    //AFEGIR UN GRAFIC DE CALORIES COSNUMIDES

    public static Map<String, String> CHART_TITTLE =
            new HashMap<String, String>() {{
                put(WHEIGHT_TRACKING_CHART_RAW_WEIGHTS, "Weight tracking chart (raw data)");
                put(WHEIGHT_TRACKING_CHART_ADJUSTED_WEIGHTS, "Weight tracking chart (adjusted weight)");
                put(WHEIGHT_TRACKING_CHART_TWO_SERIES_RAW_AND_ADJUSTED, "Weight tracking chart (raw vs. adjusted weights)");

            }};

    /*
    final static public int TIPUS_CANVI_NOM=0;
    final static public int TIPUS_NOVA_UNITAT=1;
    final static public int TIPUS_CANVI_CODI=2;
    //es creen dues unitats noves, no cal fer res pq la mare es mante
    final static public int TIPUS_SUBDIVISIO=3;
    final static public int TIPUS_CANVI_CODI_I_NOM=4;
    //Desapareix per moviment persones
    final static public int TIPUS_UNITAT_DESAPAREIX=5;
    // Canvi de codi per canvi de nivell
    final static public int TIPUS_CANVI_CODI_PER_CANVI_NIVELL=6;
    //final static public int TIPUS_CANVI_NIVELL=7;


    public static Map<String, Integer> TIPUS =
            new HashMap<String, Integer>() {{
                put("Canvi de nom", TIPUS_CANVI_NOM);
                put("Nova unitat", TIPUS_NOVA_UNITAT);
                //put("", TIPUS_CANVI_NIVELL);
                put("Es creen dues unitats noves, no cal fer res pq la mare es manté", TIPUS_SUBDIVISIO);
                put("Canvi de codi", TIPUS_CANVI_CODI);
                put("Canvi de codi i de nom", TIPUS_CANVI_CODI_I_NOM);
                put("Desapareix per moviment persones", TIPUS_UNITAT_DESAPAREIX);
                put("Canvi de codi per canvi de nivell", TIPUS_CANVI_CODI_PER_CANVI_NIVELL);
            }};


    public static Map<Integer, String> TIPUS_COMMENT =
            new HashMap<Integer, String>() {{
                put(TIPUS_CANVI_NOM, "la unitat canvia de nom");
                put(TIPUS_NOVA_UNITAT, "és una nova unitat");
                //put("", TIPUS_CANVI_NIVELL);
                put(TIPUS_SUBDIVISIO, "es creen dues unitats noves, no cal fer res pq la mare es manté");
                put(TIPUS_CANVI_CODI, "la unitat canvia de codi");
                put(TIPUS_CANVI_CODI_I_NOM, "la unitat canvia de codi i de nom");
                put(TIPUS_UNITAT_DESAPAREIX, "la unitat desapareix per moviment persones");
                put(TIPUS_CANVI_CODI_PER_CANVI_NIVELL, "la unitat canvia de codi al canviar de nivell");
            }};


    public static final List<Class<? extends UpdateLine>> LIST_CLASSES_UPDATES = new ArrayList<Class<? extends UpdateLine>>(
            Arrays.asList(
                    UpdateLegacy.class,
                    UpdateAmbitGrup.class,
                    UpdatePermisClient.class,
                    UpdateOrigenAmbitGrup.class,
                    UpdateEscriptori.class,
                    UpdatePermisGrup.class
            ));


     */
}
