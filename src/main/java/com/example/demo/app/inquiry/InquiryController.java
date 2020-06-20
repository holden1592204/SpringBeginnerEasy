package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
//import com.example.demo.service.InquiryNotFoundException;
import com.example.demo.service.InquiryServiceImpl;


//import com.example.demo.service.InquiryNotFoundException;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
	
 	private final InquiryServiceImpl inquiryService; //型の名前はインターフェイス名
	
 	@Autowired
 	public InquiryController(InquiryServiceImpl inquiryService){
 		this.inquiryService = inquiryService;
 	}
	
	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();
		
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "Inquiry Index");
		return "inquiry/index_boot"; //inquiryのファイルに入っているindex.htmlファイルを指定している
	}
	
	@GetMapping("/form")
	public String form(InquiryForm InquiryForm,
			Model model,
			@ModelAttribute("complete") String complete)  {
		model.addAttribute("title", "InquiryForm");
		return "inquiry/form_boot";
	}
	
	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "InquiryForm");
		return "inquiry/form_boot";
	}
	
	
	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm,
			BindingResult result, Model model) {

		//hands-on
		if(result.hasErrors()) {
			model.addAttribute("title ", "Inquiry Form");
			return "inquiry/form_boot";
		}
					model.addAttribute("title", " Confirm Page");
					return "inquiry/confirm_boot";
	}
	
	@PostMapping("/complete")
	public String complete(@Validated InquiryForm inquiryForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			model.addAttribute("title", "InquiryForm");
			return "inquiry/form_boot";
		}
		
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getEmail());
		inquiry.setCreated(LocalDateTime.now());
		
		inquiryService.save(inquiry);
		redirectAttributes.addFlashAttribute("complete", "Registered");
		return "redirect:/inquiry/form";
	}
	
//	@ExceptionHandler(InquiryNotFoundException.class)
//	public String handleException(InquiryNotFoundException e, Model model) {
//		model.addAttribute("message", e);
//		return "error/CustomPage";
//	}
	
	
	
}
