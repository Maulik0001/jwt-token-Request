package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableWebSecurity
@RequestMapping(value="/rest")
public class RequestController {
	
	@Value("${path.callUrl}")
	private String callUrl; 
	
	@RequestMapping(value = "/jwtSecurityRequest", method = RequestMethod.POST)
	public String jwtSecurityRequest() {
		System.out.println("JWT Request Called ::: ");
	
		String jsonObjString = ApiCall.callAPI(callUrl);
		
		if(jsonObjString!=null) {
			System.out.println("response ::;"+jsonObjString);
			return "success";
		}
		return "fail";
		
		
		
		/*HashMap<String, String> resultmap= new HashMap<String, String>();
		resultmap.put("abc", "abc1");
		resultmap.put("abcd", "abcd1");
		
		Gson gson=new Gson();
	
		return gson.toJson(resultmap);*/
	}
	
}
