package com.awgtek.itemshop.itemstates.presentation.web;

import javax.inject.Inject;
import javax.jws.WebParam.Mode;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.awgtek.itemshop.itemstates.presentation.service.ItemStatesViewService;
import com.awgtek.itemstates.domain.ItemState;
import com.awgtek.itemstates.service.ItemStatesService;

@Controller
public class ItemStatesController {
	
	private ItemStatesViewService itemStatesViewService;
	private ItemStatesService itemStatesService;

	@Inject
	public ItemStatesController(ItemStatesViewService itemStatesViewService, ItemStatesService itemStatesService) {
		super();
		this.itemStatesViewService = itemStatesViewService;
		this.itemStatesService = itemStatesService;
	}
	
	@RequestMapping(value = "*", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {

		model.addAttribute("message", "from item view service " + itemStatesViewService.getDesc());
		model.put("title", itemStatesViewService.getTitle("from service " + itemStatesService.getDesc()));
		ItemState itemState = new ItemState();
//		itemState.setName("item state name");
//		itemState.setDescription("description");
		model.addAttribute("itemstate", itemState);
		model.addAttribute("itemstates", itemStatesService.list());
		return "itemstates";

	}
	
	@RequestMapping(value = "/saveItemState.do", method = RequestMethod.POST)
	public ModelAndView saveContact(@ModelAttribute ItemState contact) {
		itemStatesService.saveOrUpdate(contact);
	    return new ModelAndView("redirect:/states.do");
	}
	@RequestMapping(value = "/deleteItemState", method = RequestMethod.GET)
	public ModelAndView deleteContact(HttpServletRequest request) {
	    int contactId = Integer.parseInt(request.getParameter("id"));
	    itemStatesService.delete(contactId);
	    return new ModelAndView("redirect:/states.do");
	}
	@RequestMapping(value = "/editItemState", method = RequestMethod.GET)
	public ModelAndView editContact(HttpServletRequest request) {
	    int contactId = Integer.parseInt(request.getParameter("id"));
	    ItemState contact = itemStatesService.get(contactId);
	    ModelAndView model = new ModelAndView("ContactForm");
	    model.addObject("contact", contact);
	 
	    return model;
	}
	@RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
	public ModelAndView hello(@PathVariable("name") String name) {

		ModelAndView model = new ModelAndView();
		model.setViewName("itemstates");
		model.addObject("msg", name);

		return model;

	}
}
