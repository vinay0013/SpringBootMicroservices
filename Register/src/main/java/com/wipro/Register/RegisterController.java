package com.wipro.Register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import javax.servlet.ServletResponse;


import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class RegisterController {
	Map<String,User> users=new HashMap<String,User>();
	
	@Autowired	
	private RestTemplate restTemplate;
	
	public RegisterController()
	{
		User uu=new User("savi","savi","savi@gmail.com");
		users.put("savi", uu);
	}
	
	@HystrixCommand(fallbackMethod="defaultCountries",
			commandProperties={
			@HystrixProperty(
			  name="circuitBreaker.errorThresholdPercentage",value="20"),
			@HystrixProperty(
			  name="circuitBreaker.sleepWindowInMilliseconds",value="7000")
	})		
	@RequestMapping(value = "/countries/all", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String displayCountries()
	{
Map<String,Object> aa=new HashMap<String,Object>();
		
		ResponseEntity<String> result = restTemplate.getForEntity("https://restcountries.eu/rest/v2/all",String.class,aa);
		if (result.getStatusCode() == HttpStatus.OK) {
			return result.getBody().toString();			
		}
		
		else {
		   return "";
		}
	}
	
	
	public String defaultCountries()
	{
		return "India:New Delhi:Asia";
	}
	
	@RequestMapping(value = "/users/register", method = RequestMethod.POST)
	@ResponseBody
	public String registerUser(@ModelAttribute("userid")String userid,@ModelAttribute("password") String password,@ModelAttribute("email")String email )
	{
		User u=new User(userid,password,email);
		users.put(userid, u);
		return "<html><body bgcolor='coral'>Registered Successfully"+"<a href='/index.html'>home to login</a>"+"</body></html>";
		
	}
	
	@RequestMapping(value = "/users/all", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,User> getAllRegisteredUsers()
	{
		return users;
	}
	
	@RequestMapping(value = "/users/{userid}", method = RequestMethod.GET)
	@ResponseBody
	public User getUser(@PathVariable("userid")String userid)
	{
		return users.get(userid);
	}
	
	
	
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@Autowired
	private LoadBalancerClient loadbalancerclient;

	@Value("${pivotal.tradeservice.name}")
	 protected String tradeService;
	
	
	@RequestMapping(value = "/users/login", method = RequestMethod.POST)
	@ResponseBody
	public String loginUser(@ModelAttribute("userid")String userid,@ModelAttribute("password") String password,HttpServletRequest request,HttpServletResponse response)
	{
		User uu=users.get(userid);
		request.getSession().setAttribute("user", uu);
		
		if (users.get(userid)!=null)
		{
			if(users.get(userid).getPassword().equals(password))
					{
						//use below 2 lines for regular eureka;
//				List<ServiceInstance> instances=  discoveryClient.getInstances(tradeService);
//				URI uri=instances.get(0).getUri();
				
				//use below 3 lines code for Ribbon
				ServiceInstance instance=  loadbalancerclient.choose(tradeService);
				URI uri=URI.create(String.format("http://%s:%s",
					instance.getHost(),instance.getPort()));
//				URI uri=URI.create(String.format("https://%s",
//				instance.getHost()));

				
//				List<ServiceInstance> instances=  discoveryClient.getInstances("EUREKAZUULSERVER");
//				URI uri=instances.get(0).getUri();
				
				
				System.out.println("Register-Service.loginUser .URI========="+uri);
				String url=uri.toString()+"/Trade.html";
				System.out.println("========================================");
				System.out.println("Register-Service.loginUser .URI========="+url);
				
				try
				{
				//request.getRequestDispatcher(url).include(request, response);
				//restTemplate.
					response.sendRedirect(url);
						}
				catch(Exception e)
				{
					System.out.println("Error in dispatching");
				}
						
			    //ResponseEntity result = restTemplate.getForEntity(uri.toString()
							//+ "/users/logincheck");
				
					return "";		
				
					}
			else
			{
				return "PasswordError";
			}
			
		}
		else
		{
			return "Sorry";
		}
		
		
	}
	
	
	@RequestMapping(value = "/reg/test", method = RequestMethod.GET)
	@ResponseBody
	public String getTest()
	{
		ServiceInstance instance=  loadbalancerclient.choose(tradeService);
		URI uri=URI.create(String.format("http://%s:%s"+"/tradeservice/trade/test",
			instance.getHost(),instance.getPort()));
		ResponseEntity<String> result = restTemplate.getForEntity(uri.toString(),String.class);
		if (result.getStatusCode() == HttpStatus.OK) {
			return result.getBody().toString();			
		}
		
		else {
		   return "Failed";
		}
	}
	

}