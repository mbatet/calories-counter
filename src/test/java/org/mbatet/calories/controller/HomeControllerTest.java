package org.mbatet.calories.controller;


import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Dia;
import org.mbatet.calories.model.Form;
import org.mbatet.calories.model.Interval;
import org.mbatet.calories.model.stats.TrackingChart;
import org.mbatet.calories.service.WeightStatsService;
import org.mbatet.calories.service.parser.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes={HomeController.class, WeightStatsService.class, CsvParser.class, ServletContext.class})
@ActiveProfiles("local_execution")
public class HomeControllerTest {

    private static final Logger log = LoggerFactory.getLogger(HomeControllerTest.class);


    //@Value("${camp.valor}") protected String login;

    @Autowired
    protected HomeController homeController;

    //String data = null;
    Form form = new Form();

    @BeforeAll
    public  void setUp() throws IOException {
        // Set up database

        String path = System.getProperties().getProperty("user.dir")+System.getProperties().getProperty("file.separator") + "sample.csv";

        System.out.println("Llegim fitxer de dades: "+path);
        FileInputStream inputStream = new FileInputStream(path);
        try {
            String data = IOUtils.toString(inputStream);
            form.setTextArea(data);
            form.setType(Constants.WHEIGHT_TRACKING_CHART_ADJUSTED_WEIGHTS);

        }
        catch(Exception e)
        {
            System.out.println("Error obtenint les dades: " + e.getMessage());
        }
        finally {
            inputStream.close();
        }



    }

    @Test
    @Order(1)
    public void testIndexAction() throws Exception{

        Model model = new ExtendedModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();


        //String view = homeController.homeNotAuthenticated(model, request, response);
        homeController.addAttributes(model, request);
        String view = homeController.index(model, request);


        log.info("[m:testChartAction] view: " + view);

        testHttpResponse200OK(response);

        Assert.assertEquals(Constants.VIEW_INDEX, view);

        Assert.assertNotNull(model.getAttribute("form"));

        Form form = (Form)model.getAttribute("form");

    }


    @Test
    @Order(2)
    public void testChartAction() throws Exception{



        Model model = new ExtendedModelMap();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        //request.addHeader("Uer-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");

        homeController.addAttributes(model, request);
        String view = homeController.chart(model, form);

        log.info("[m:testChartAction] view: " + view);

        //Assert.assertEquals(view, Constants.VIEW_INDEX);
        Assert.assertEquals(Constants.VIEW_CHART, view);

        testHttpResponse200OK(response);

        Assert.assertNotNull(model.getAttribute("dies"));
        Assert.assertNotNull(model.getAttribute("type"));
        Assert.assertNotNull(model.getAttribute("intervals"));
        Assert.assertNotNull(model.getAttribute("intervalGeneral"));
        Assert.assertNotNull(model.getAttribute("lastWeek"));
        Assert.assertNotNull(model.getAttribute("lastWeek"));
        Assert.assertNotNull(model.getAttribute("minWeight"));
        Assert.assertNotNull(model.getAttribute("maxWeight"));
        Assert.assertNotNull(model.getAttribute("currentWeight"));
        Assert.assertNotNull(model.getAttribute("calsLeft"));
        Assert.assertNotNull(model.getAttribute("title"));
        Assert.assertNotNull(model.getAttribute("calsLeft"));
        Assert.assertNotNull(model.getAttribute("stats"));

        List<Dia> dies = (List<Dia>)model.getAttribute("dies");
        List<Interval> intervals = (List<Interval>)model.getAttribute("intervals");
        Interval intervalGeneral = (Interval)model.getAttribute("intervalGeneral");
        Interval lastWeek = (Interval)model.getAttribute("lastWeek");
        TrackingChart stats = (TrackingChart ) model.getAttribute("stats");
        Float calsLeft = (Float)model.getAttribute("calsLeft");
        Float maxWeight = (Float)model.getAttribute("maxWeight");
        Float minWeight = (Float)model.getAttribute("minWeight");
        Float currentWeight = (Float)model.getAttribute("currentWeight");

        //TODO: fer comprovacions amb tot aixo



    }


    /** Classes de servei de tests del response code */
    protected void testHttpResponse200OK(MockHttpServletResponse response) throws UnsupportedEncodingException {
        log.info("[m:testHttpResponse200OK] response: " + response);

        Assert.assertNotNull(response);

        log.info("[m:testHttpResponse200OK] response.getStatus(): " + response.getStatus());
        log.info("[m:testHttpResponse200OK] response.getErrorMessage(): " + response.getErrorMessage());
        log.info("[m:testHttpResponse200OK] response.getContentType(): " + response.getContentType());
        log.info("[m:testHttpResponse200OK] response.getContentAsString(): " + response.getContentAsString());
        log.info("[m:testHttpResponse200OK] response.getContentLength(): " + response.getContentLength());


        Assert.assertNull(response.getErrorMessage());
        Assert.assertTrue(response.getStatus() == HttpServletResponse.SC_OK);

    }

}
