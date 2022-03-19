package org.mbatet.calories.controller;


import org.mbatet.calories.model.*;
import org.mbatet.calories.model.stats.Stats;
import org.mbatet.calories.model.stats.WeightLossStats;
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
	public String form(Model model, HttpServletRequest request){

		Form form = new Form();
		model.addAttribute(form);

		return "index";
	}


	@RequestMapping(value={"/parse"}, method = RequestMethod.POST)
	public String parse(Model model, @Validated Form form) throws IOException {

		log.info("[m:parse] " + form);

		if( form.isEmpty() )
		{
			log.error("[m:parse] No s'ha trobat fitxer ni text a parsejar...");
			model.addAttribute("message", "No s'ha trobat res a parsejar");
			return "index";

		}

		//Com a preferencia, agafem el que tenim al textarea
		String textCsv = form.getText();
		log.debug("[m:parse] textCsv: " + textCsv);



		List<Dia> dies = weightStatsService.parse(textCsv);
		List<Interval> intervals = weightStatsService.getIntervals(dies);
		Interval lastWeek = weightStatsService.getLastWeek(dies);
		Stats stats = weightStatsService.getStatsFromIntervals(intervals);

		//TODO: Posar a dins de stats i clacular a dins de getStatsFromIntervals
		Float calsLeft = weightStatsService.getCalsLeft( lastWeek, stats.getWeightLossStats());


		//model.addAttribute("message", "CSV parsejat");
		model.addAttribute("dies", dies);
		model.addAttribute("type", form.getType());
		model.addAttribute("intervals", intervals);
		model.addAttribute("lastWeek", lastWeek);
		model.addAttribute("calsLeft", calsLeft);


		//fer diferent, un objecte global amb les stats o
		model.addAttribute("stats", stats);


		String title =  Constants.CHART_TITTLE.get(form.getType());

		if( intervals.size() > 0 ) {

			//TODO: Aquest interval hauria d'anar al model de forma separada o anar directamment a dins de Stats
			Interval intervalGeneral = intervals.get(0);
			title += " " + Constants.FORMAT_DATE.format(intervalGeneral.getFirstDate()) + " to " + Constants.FORMAT_DATE.format(intervalGeneral.getLastDate());
			intervals.remove(intervalGeneral);
			intervals.add(intervalGeneral);
		}



		//TODO: AL tiol afegir dates... del tal al tal, en base a l'interval
		model.addAttribute("title", title);


		return "chart";
	}


	@ExceptionHandler(Exception.class)
	public String handleException (Model model, HttpServletRequest request, HttpServletResponse response, Exception e) {

		log.error("[m:handleException] Exception found: " + e.getMessage());
		model.addAttribute("message",e.getMessage());
		return "error";
	}


}
