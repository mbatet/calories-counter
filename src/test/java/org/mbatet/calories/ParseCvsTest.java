package org.mbatet.calories;

import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mbatet.calories.model.*;
import org.mbatet.calories.model.stats.AvgCalStats;
import org.mbatet.calories.model.stats.Stats;
import org.mbatet.calories.service.WeightStatsService;
import org.mbatet.calories.service.parser.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes={WeightStatsService.class, CsvParser.class})
@ActiveProfiles("local_execution")
//@ActiveProfiles("local_execution")
public class ParseCvsTest {


    @Autowired
    WeightStatsService weightStatsService;


/*
    final static String DATA = "01/01/2022;58.6;2051;\n" +
            "02/01/2022;58.6;2053;\n" +
            "03/01/2022;58.6;2457;\n" +
            "04/01/2022;58.6;2686;\n" +
            "05/01/2022;58.6;2050;\n" +
            "06/01/2022;57.9;1697;\n" +
            "08/01/2022;58;2013;\n" +
            "09/01/2022;58.4;1971;\n" +
            "10/01/2022;58.4;2245;\n" +
            "11/01/2022;58.5;2573;\n" +
            "12/01/2022;58.5;2312;\n" +
            "13/01/2022;57.5;2774;\n" +
            "14/01/2022;57.8;4351;\n" +
            "15/01/2022;58.2;2136;\n" +
            "16/01/2022;58.2;3390;\n" +
            "17/01/2022;58.6;2245;\n" +
            "18/01/2022;58.5;2708;\n" +
            "19/01/2022;58.1;2566;\n" +
            "21/01/2022;57.8;2986;\n" +
            "23/01/2022;57.8;2877;\n" +
            "24/01/2022;58.7;2485;\n" +
            "25/01/2022;57.3;2083;\n" +
            "26/01/2022;57.3;2485;\n" +
            "27/01/2022;57.2;1928;\n" +
            "28/01/2022;57.5;;\n";
*/

    String data = null;
    List<Dia> dies = null;
    List<Interval> intervals = null;
    Stats stats = null;


    @BeforeAll
    public  void setUp() throws IOException {
        // Set up database

        System.out.println("LLegim fitxer de dades: "+System.getProperties().getProperty("user.dir")+"\\sample.csv");
        FileInputStream inputStream = new FileInputStream(System.getProperties().getProperty("user.dir")+"\\sample.csv");
        try {
            data = IOUtils.toString(inputStream);
        }
        catch(Exception e)
        {
            System.out.println("Error obtenint les dades: " + e.getMessage());
        }
        finally {
            inputStream.close();
        }

    }

    @AfterEach
    public void tearDown() {
        // Cleanup codes
    }


    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testParseData () throws IOException {


        Form form = new Form();
        form.setTextArea(data);

        String text = form.getText();

        dies = weightStatsService.parse(text);

        Assert.assertNotNull(dies);

        Assert.assertTrue(dies.size()==(data.split("\n")).length);

        dies.forEach( (final Dia dia) -> {
            Assert.assertNotNull(dia);
            Assert.assertNotNull(dia.getLinia());
            //Pot ser que un dia no tinguem algun valor
            Assert.assertNotNull(dia.getPes()); //no pot ser null perque ens imaginem els valros qu eno tenim
            Assert.assertTrue(dia.getConsumedCals()!=null || dia.getErrorDescription()!=null);
            Assert.assertNotNull(dia.getPesPonderat());
            Assert.assertTrue(dia.getPes()>50);
            Assert.assertTrue(dia.getPes()<60);
            Assert.assertTrue(dia.getPesPonderat()>50);
            Assert.assertTrue(dia.getPesPonderat()<60);
            //System.out.println(dia);
        });

    }



    @Test
    @Order(2)
    public void testGetIntervals () throws IOException {


        intervals = weightStatsService.getIntervals(dies);
        Assert.assertNotNull(intervals);


        intervals.forEach( (final Interval interval) -> {
            Assert.assertNotNull(interval);
            Assert.assertTrue(interval.getDies().size() >= Constants.MIN_MIDA_INTERVAL);
            Assert.assertNotNull(interval.getFirstDay());
            Assert.assertNotNull(interval.getLastDay());
            Assert.assertNotNull(interval.getFirstDate());
            Assert.assertNotNull(interval.getLastDate());

            Assert.assertNotNull(interval.getAvgConsumedCals());
            Assert.assertNotNull(interval.getAvgActivityCals());
            Assert.assertNotNull(interval.getAvgAdjustedCals());


            Assert.assertTrue(interval.getAvgConsumedCals() !=0F );
            Assert.assertTrue(interval.getWeigthDiff()!=0F);
            Assert.assertTrue(interval.getAvgConsumedCals() > 1000F );
            Assert.assertTrue(interval.getAvgActivityCals() < 4000F );
            Assert.assertTrue(interval.getWeigthDiff()<3);
            Assert.assertTrue(interval.getWeigthDiff()>-3);
            Assert.assertTrue(interval.getAvgAdjustedCals() < 3000F );
            //System.out.println(dia);
        });

    }


    @Test
    @Order(3)
    public void testGetStats () throws IOException {


        stats = weightStatsService.getStatsFromIntervals(intervals);
        Assert.assertNotNull(stats);



        testStats (stats.getWeightLossStats());
        testStats (stats.getMaintenanceStats());
        testStats (stats.getWeightGainStats());


        if(stats.getWeightGainAvgCals()!=null) Assert.assertTrue(stats.getWeightLossAvgCals().floatValue()<=stats.getWeightGainAvgCals().floatValue());
        if(stats.getMaintenanceAvgCals()!=null) Assert.assertTrue(stats.getWeightLossAvgCals().floatValue()<=stats.getMaintenanceAvgCals().floatValue());
        if(stats.getWeightGainAvgCals()!=null && stats.getMaintenanceAvgCals()!=null) Assert.assertTrue(stats.getWeightGainAvgCals().floatValue()>=stats.getMaintenanceAvgCals().floatValue());


        if(stats.getWeightGainAvgAdjustedCals()!=null) Assert.assertTrue(stats.getWeightLossAvgAdjustedCals().floatValue()<=stats.getWeightGainAvgAdjustedCals().floatValue());
        if(stats.getMaintenanceAvgAdjustedCals()!=null) Assert.assertTrue(stats.getWeightLossAvgAdjustedCals().floatValue()<=stats.getMaintenanceAvgAdjustedCals().floatValue());
        if(stats.getWeightGainAvgAdjustedCals()!=null && stats.getMaintenanceAvgAdjustedCals()!=null) Assert.assertTrue(stats.getWeightGainAvgAdjustedCals().floatValue()>=stats.getMaintenanceAvgAdjustedCals().floatValue());



    }

    public void testStats (AvgCalStats avgCalStats) throws IOException {

        Assert.assertNotNull(avgCalStats.getConsumedCals());
        Assert.assertNotNull(avgCalStats.getActivityCals());
        Assert.assertNotNull(avgCalStats.getAdjustedCals());
        Assert.assertNotNull(avgCalStats.getTitle());
        Assert.assertNotNull(avgCalStats.getIntervals());
        Assert.assertTrue(avgCalStats.getIntervals().size()>0);

    }
}


