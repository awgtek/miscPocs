package com.awgtek.dynamicspringmodel.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.awgtek.dynamicspringmodel.annotations.DynamicModelAttribute;
import com.awgtek.dynamicspringmodel.entities.BasicPojo;
import com.awgtek.dynamicspringmodel.entities.SomeType;
@Controller
@RequestMapping("/{bean}/")
public class BasicPojoController {

    private static final Logger log = LoggerFactory.getLogger(BasicPojoController.class);

    
    public BasicPojoController() {
        super();
    }
    
    @ModelAttribute("allTypes")
    public SomeType[] populateTypes() {
        return new SomeType[] { SomeType.TYPE1, SomeType.TYPE2 };
    }

    
    @RequestMapping({"/edit"})
    public String showBean(@PathVariable("bean") String bean, @ModelAttribute("basicpojo") final BasicPojo basicPojo) {
        return "editbean";
    }  
        
    @RequestMapping(value="/edit", params={"save"})
    public String save(@PathVariable("bean") String bean,
    		@DynamicModelAttribute("basicpojo") @Valid final Object basicpojo,
    		final BindingResult bindingResult, final ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "editbean";
        }
        log.info("JUST ADDED SUBSCRIPTION: " + basicpojo);
        model.clear();
        return "redirect:/" + bean + "/edit";
    }

}
