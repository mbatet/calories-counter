package org.mbatet.calories;

import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mbatet.calories.model.*;
import org.mbatet.calories.model.stats.AverageCalStats;
import org.mbatet.calories.model.stats.Stats;
import org.mbatet.calories.service.WeightStatsService;
import org.mbatet.calories.service.parser.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
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


    String data = null;
    List<Dia> dies = null;
    List<Interval> intervals = null;
    Stats stats = null;


    @BeforeAll
    public  void setUp() throws IOException {
        // Set up database

        String path = System.getProperties().getProperty("user.dir")+System.getProperties().getProperty("file.separator") + "sample.csv";

        System.out.println("Llegim fitxer de dades: "+path);
        FileInputStream inputStream = new FileInputStream(path);
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
            Assert.assertNotNull(dia.getWeight()); //no pot ser null perque ens imaginem els valros qu eno tenim
            Assert.assertTrue(dia.getConsumedCals()!=null || dia.getErrorDescription()!=null);
            Assert.assertNotNull(dia.getAdjustedWeight());
            Assert.assertTrue(dia.getWeight()>50);
            Assert.assertTrue(dia.getWeight()<60);
            Assert.assertTrue(dia.getAdjustedWeight()>50);
            Assert.assertTrue(dia.getAdjustedWeight()<60);
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

        Comparator comp = new AverageCalStats.SortStats();

        stats = weightStatsService.getStatsFromIntervals(intervals);
        Assert.assertNotNull(stats);



        testStats (stats.getWeightLossStats());
        testStats (stats.getMaintenanceStats());
        //testStats (stats.getWeightGainStats());

        Assert.assertTrue(stats.getMaintenanceStats().isNotEnoughData() || comp.compare(stats.getWeightLossStats(), stats.getMaintenanceStats()) < 0);
        Assert.assertTrue(stats.getWeightGainStats().isNotEnoughData() || comp.compare(stats.getWeightLossStats(), stats.getWeightGainStats()) < 0);

        Assert.assertTrue(stats.getWeightGainStats().isNotEnoughData() || comp.compare(stats.getWeightGainStats(), stats.getWeightLossStats()) > 0);
        Assert.assertTrue(stats.getWeightGainStats().isNotEnoughData() || comp.compare(stats.getWeightGainStats(), stats.getMaintenanceStats()) > 0);




    }

    public void testStats (AverageCalStats avgCalStats) throws IOException {

        Assert.assertNotNull(avgCalStats.getConsumedCals());
        Assert.assertNotNull(avgCalStats.getActivityCals());
        Assert.assertNotNull(avgCalStats.getAdjustedCals());
        Assert.assertNotNull(avgCalStats.getTitle());
        Assert.assertNotNull(avgCalStats.getIntervals());
        Assert.assertTrue(avgCalStats.getIntervals().size()>0);

    }
}


