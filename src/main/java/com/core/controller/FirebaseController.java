package com.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.core.util.FirebaseDynamicLinksUtil;

@Controller
@RequestMapping("firebase")
public class FirebaseController {
	
	
	@RequestMapping("/dynamicLinksUtil")
	public String index(Model model) {
		model.addAttribute("title", "Firebase Dynamic Links Util");
		return "firebase/index";
	}
	
	@RequestMapping("/dynamicLinksUtil/build")
	public String build(@RequestParam(value="domain", required=false) String domain,
						@RequestParam(value="url", required=false) String url, Model model){
		model.addAttribute("shortLink", FirebaseDynamicLinksUtil.buildDynamicLinkByFirebase(domain, url));
		// 若在不同層級回傳字串也需提供 path
		return "firebase/build";
	}
	
	@RequestMapping("/dynamicLinksUtil/analytics")
	public String analytics(@RequestParam(value="shortLink", required=false) String shortLink,
							@RequestParam(value="durationDays", required=false) String durationDays, Model model){
		model.addAttribute("analyticsData", FirebaseDynamicLinksUtil.analyticsFirebaseDynamicLink(shortLink, durationDays));
		// 若在不同層級回傳字串也需提供 path
		return "firebase/analytics";
	}
	
}
