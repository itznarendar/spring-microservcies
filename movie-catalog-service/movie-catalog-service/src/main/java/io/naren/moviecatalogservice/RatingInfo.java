package io.naren.moviecatalogservice;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
@Service
public class RatingInfo {
	
	Logger log= LoggerFactory.getLogger(RatingInfo.class);


	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod="getFallbackRating")
	public UserRating  getUserRating(String userId )
	{
		log.info("RatingInfo    getUserRating  processng ");
		
		  return restTemplate.getForObject("http://rating-data-service/ratingsdata/user/"+userId, UserRating.class);
	
	}

	
	
	public UserRating getFallbackRating( String userId){
		return new UserRating("no ratings available", Arrays.asList(new Rating("00",0) ));
		
	}
	}
