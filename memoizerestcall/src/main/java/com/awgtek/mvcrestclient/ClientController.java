package com.awgtek.mvcrestclient;

import java.util.function.Function;

import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.awgtek.memoizer.Memoizer;
import com.awgtek.memoizerestcall.Params;
 

@Controller
public class ClientController {
	 
		@RequestMapping("notmemoized")
		public ModelAndView notMemoized() {
	 
			StopWatch timing = new StopWatch();
			timing.start();
			StringBuilder sb = new StringBuilder();
			String msg = "mo";
			String url = "http://localhost:8080/memoizerestcall/rest/slow/";
			for (int i = 0; i< 3; i++) {
				sb.append((new JerseyClient()).serverData(url, msg));
			}
			String message = "<br><div style='text-align:center;'>"
					+ "<h3>********** " + sb.toString()  + " **********</div><br><br>";
			timing.stop();
			return new ModelAndView("client", "message", message + timing.shortSummary());
		} 
		
		@RequestMapping("memoized")
		public ModelAndView clientMemoized() {
	 
			StopWatch timing = new StopWatch();
			timing.start();
			StringBuilder sb = new StringBuilder();
			Function<Params, String> ft = p -> (new JerseyClient()).serverData(p.getUrl(), p.getMsg());
			Function<Params, String> ftm = Memoizer.memoize(ft);


			for (int i = 0; i< 3; i++) {
				sb.append(ftm.apply(new Params("http://localhost:8080/memoizerestcall/rest/slow/","mo")));
			}
			String message = "<br><div style='text-align:center;'>"
					+ "<h3>********** " + sb.toString()  + " **********</div><br><br>";
			timing.stop();
			return new ModelAndView("client", "message", message + timing.shortSummary());
		}
}