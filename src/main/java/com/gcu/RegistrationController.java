package com.gcu;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gcu.business.RegistrationBusinessServiceInterface;
import com.gcu.business.SecurityServiceInterface;
import com.gcu.data.OrdersDataService;
import com.gcu.data.RegistrationDataService;
import com.gcu.model.OrderModel;
import com.gcu.model.LoginModel;
import com.gcu.model.RegistrationModel;

@Controller
@RequestMapping("/register")
public class RegistrationController{
	
	@Autowired
	SecurityServiceInterface securityService;
	@Autowired
	private OrdersDataService ordersService;

	@Autowired
	private RegistrationDataService regService;

	Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	
	@GetMapping("/")
	public String display(Model model) {
		model.addAttribute("title", "Registration Form");
		model.addAttribute("registrationModel", new RegistrationModel());
		logger.info("Registration page loaded");
		return "registration";
	}

	@PostMapping("/add")
    public RegistrationModel addUser(@RequestBody RegistrationModel addUser){
        regService.addOne(addUser);
		logger.info("Adding user");
		return addUser;
    }
	
	@PostMapping("/doRegistration")
	public String doRegistration(@Valid RegistrationModel registrationModel, BindingResult bindingResult, Model model) {
		LoginModel attemptReg = new LoginModel();
		attemptReg.setUsername(registrationModel.getUsername());
		attemptReg.setPassword(registrationModel.getPassword());



		if(bindingResult.hasErrors()) {
			model.addAttribute("title", "Registration Form");
			logger.error(bindingResult.getErrorCount()+" ERRORS FOUND");
			return "registration";
		}
		
		else if(securityService.isAuthenticated(attemptReg)){
			logger.warn("User already exists");
			return "registration";
	
		}
		else{
			model.addAttribute("model", registrationModel);
			List<OrderModel> orders = ordersService.getOrders();
			model.addAttribute("title","Products:");
			model.addAttribute("orders",orders);
			logger.info("User registered");

			return "orders";
		}
		
	}
}