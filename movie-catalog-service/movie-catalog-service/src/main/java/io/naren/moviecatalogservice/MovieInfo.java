package io.naren.moviecatalogservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
@Service
public class MovieInfo {

	@Autowired
	private RestTemplate restTemplate;
	@HystrixCommand(fallbackMethod="getFallbackCatalogItem",commandProperties= { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
		       @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="60"), @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),
		       @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000")})
	

	public CatalogItem getCatalogItem(Rating rating)
	{
		Movie movie=	restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
		
		return	new CatalogItem(movie.getName(),movie.getDescription(),rating.getRating());
	}

	public CatalogItem getFallbackCatalogItem(Rating rating){
		return new CatalogItem("no movie from @@@","",0);
		
	}
	
	
	
}
