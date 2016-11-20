package com.awgtek.mvcrestclient;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.awgtek.memoizer.Memoizer;
import com.awgtek.memoizerestcall.Params;

@Controller
@RequestMapping("{msg}")
public class ClientController {

	@Autowired
	private Memoizer<Params, String> memoizer;

	@Autowired
	private JerseyClient jerseyClient;

	@RequestMapping("notmemoized")
	public ModelAndView notMemoized(@PathVariable("msg") String msg) {

		StopWatch timing = new StopWatch();
		timing.start();
		StringBuilder sb = new StringBuilder();
		// String msg = "mo";
		String url = "http://localhost:8080/memoizerestcall/rest/slow/";
		for (int i = 0; i < 3; i++) {
			sb.append(jerseyClient.serverData(url, msg));
		}
		String message = "<br><div style='text-align:center;'>" + "<h3>********** " + sb.toString()
				+ " **********</div><br><br>";
		timing.stop();
		return new ModelAndView("client", "message", message + timing.shortSummary());
	}

	@RequestMapping("memoized")
	public ModelAndView clientMemoized(@PathVariable("msg") String msg) {

		StopWatch timing = new StopWatch();
		timing.start();
		StringBuilder sb = new StringBuilder();
		Function<Params, String> ft = p -> jerseyClient.serverData(p.getUrl(), p.getMsg());
		Function<Params, String> ftm = memoizer.memoize(ft);

		for (int i = 0; i < 3; i++) {
			sb.append(ftm.apply(new Params("http://localhost:8080/memoizerestcall/rest/slow/", msg)));
		}
		String message = "<br><div style='text-align:center;'>" + "<h3>********** " + sb.toString()
				+ " **********</div><br><br>";
		timing.stop();
		return new ModelAndView("client", "message", message + timing.shortSummary());
	}
}