package com.wipro.Trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;



import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;



@Controller
public class TradeController {
	Map<String,Double> companies=new HashMap<String,Double>();
	
	Map<String,Trade> trades=new HashMap<String,Trade>();
	
	public TradeController(){
		companies.put("WIPRO", 298.45);
		companies.put("INFY", 949.95);
		companies.put("TCS", 2713.70);
		companies.put("TECHM", 485.85);	
		
	}
	
	
		@Autowired	
		private RestTemplate restTemplate;
		
		@Autowired
		private DiscoveryClient discoveryClient;

		@Value("${pivotal.registerservice.name}")
		 protected String registerService;
		
		User user;
	@RequestMapping(value = "/trade/do", method = RequestMethod.POST)
	@ResponseBody
	public String tradeDo(@ModelAttribute("ticker")String ticker,@ModelAttribute("qty") int qty,HttpServletRequest request)
	{
		Double price=companies.get(ticker);
		Trade u=new Trade(ticker,price,qty);
		double total=price*qty;
		u.setTotalcost(total);	
		
				
		trades.put(ticker, u);
		
		List<ServiceInstance> instances=  discoveryClient.getInstances(registerService);
		URI uri=instances.get(0).getUri();
		
//		URI uri=instances.get(0).getHost().toString();
		
		System.out.println("Trade-Service.tradedo() .URI========="+uri);
//		String url=uri.toString()+"/users/all";
		String url=instances.get(0).getHost().toString()+"/users/all";
		System.out.println("========================================");
		System.out.println("Trade-Service.tradedo()  .URI========="+url);
		
		Map<String,Object> aa=new HashMap<String,Object>();
		
		ResponseEntity<String> result = restTemplate.getForEntity(url,String.class,aa);
		
		if (result.getStatusCode() == HttpStatus.OK) {
			return result.getBody();
			//return repository.save(order);
		} else {
			
			return null;
		}
		//return "<html><body bgcolor='coral'>Traded Successfully "+"</body></html>";
		
		//return "<html><body bgcolor='coral'>Traded Successfully</body></html>";
		
	}
	
	@RequestMapping(value = "/trade/all", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Trade> getAllRegisteredUsers()
	{
		return trades;
	}
	
	@RequestMapping(value = "/trade/{ticker}", method = RequestMethod.GET)
	@ResponseBody
	public Trade getUser(@PathVariable("ticker")String ticker)
	{
		return trades.get(ticker);
	}
	
	@RequestMapping(value = "/trade/test", method = RequestMethod.GET)
	@ResponseBody
	public String getTest()
	{
		return "Test message from Trade.";
	}
	
	

}