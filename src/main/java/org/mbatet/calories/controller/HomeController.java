package org.mbatet.calories.controller;


import org.mbatet.calories.model.*;
import org.mbatet.calories.model.stats.GeneralStats;
import org.mbatet.calories.service.ParseService;
import org.mbatet.calories.service.WeightStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class HomeController {


	@Autowired
	private WeightStatsService weightStatsService;

	@Autowired
	private ParseService parseService;

	/*@Value("${serveis.urlApp}")
	String serveisUrlApp;*/

	@Autowired
	ServletContext context;

	/**
	 * http://localhost:8080/calories/
	 * http://dev01.intranet.dtgna:8080/calories/
	 */


	private static final Logger log = LoggerFactory.getLogger(HomeController.class);


	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {

		log.debug("[m:addAttributes] ...");
		//model.addAttribute("serveisUrlApp", serveisUrlApp);
		//model.addAttribute("userName", (String) SecurityContextHolder.getContext().getAuthentication().getName());
	}


	@RequestMapping(value={"","/", "/index", "/form"}, method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request){

		Form form = new Form();
		model.addAttribute(form);

		return Constants.VIEW_INDEX;
	}


	@RequestMapping(value={"/chart"}, method = RequestMethod.POST)
	public String chart(Model model, @Validated Form form) throws IOException {

		log.info("[m:parse] " + form);

		if( form.isEmpty() )
		{
			log.error("[m:parse] No s'ha trobat fitxer ni text a parsejar...");
			model.addAttribute("message", "No s'ha trobat res a parsejar");
			return Constants.VIEW_INDEX;

		}

		//Com a preferencia, agafem el que tenim al textarea
		String textCsv = form.getText();
		log.debug("[m:parse] textCsv: " + textCsv);


		List<Dia> dies = parseService.parse(textCsv);
		GeneralStats stats = weightStatsService.getStatsFromData(dies);


		//TODO: No utilitzar aquestes variabels si no que al frontal ho afagarem directament de stats que tb ho tenim
		List<Interval> intervals = stats.getIntervals();
		Interval intervalGeneral = stats.getIntervalGeneral();
		Interval lastWeek = stats.getLastWeek();


		//TODO: TOT aixo haurien de ser variabels de stats i posar a dins de TrackingChart/Stats i clacular a dins de getStatsFromIntervals
		Float calsLeft = weightStatsService.getCalsLeft( lastWeek, stats.getWeightLossStats());
		Float maxWeight = stats.getMaxWeight( );
		Float minWeight = stats.getMinWeight( );
		Float currentWeight = stats.getCurrentWeight();


		//Float maxWeight = 0F;
		//Float minWeight = 0F;


		//model.addAttribute("message", "CSV parsejat");
		//TODO: fer diferent, un objecte global amb les stats o
		model.addAttribute("stats", stats);
		model.addAttribute("type", form.getType());

		//TODO: No utilitzar aquestes variabels si no que al frontal ho afagarem directament de stats que tb ho tenim
		model.addAttribute("dies", dies);
		model.addAttribute("intervals", intervals);
		model.addAttribute("intervalGeneral", intervalGeneral);


		model.addAttribute("lastWeek", lastWeek);
		model.addAttribute("calsLeft", calsLeft);
		model.addAttribute("maxWeight", maxWeight);
		model.addAttribute("minWeight", minWeight);
		model.addAttribute("currentWeight", currentWeight);







		String title =  Constants.CHART_TITLE.get(form.getType());

		/*
		if( intervals.size() > 0 ) {

			//TODO: Aquest interval hauria d'anar al model de forma separada o anar directamment a dins de TrackingChart/Stats
			Interval intervalGeneral = intervals.get(0);
			title += " " + Constants.FORMAT_DATE.format(intervalGeneral.getFirstDate()) + " to " + Constants.FORMAT_DATE.format(intervalGeneral.getLastDate());
			intervals.remove(intervalGeneral);
			intervals.add(intervalGeneral);
		}*/

		title += " " + Constants.FORMAT_DATE.format(intervalGeneral.getFirstDate()) + " to " + Constants.FORMAT_DATE.format(intervalGeneral.getLastDate());


		//TODO: AL titol afegir dates... del tal al tal, en base a l'interval
		model.addAttribute("title", title);


		return Constants.VIEW_CHART;
	}


	@ExceptionHandler(Exception.class)
	public String handleException (Model model, HttpServletRequest request, HttpServletResponse response, Exception e) {

		log.error("[m:handleException] Exception found: " + e.getMessage());
		model.addAttribute("message",e.getMessage());
		return "error";
	}


}
