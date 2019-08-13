package io.naren.moviecatalogservice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;



@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {
	Logger log= LoggerFactory.getLogger(MovieCatalogController.class);
	
	@Autowired
	private RestTemplate restTemplate;
	 @Autowired
	    WebClient.Builder webClientBuilder;
	 @Autowired
	 MovieInfo movieInfo;
	 
	 @Autowired
	 RatingInfo ratingInfo;
	@RequestMapping("/{userId}")
	@HystrixCommand(fallbackMethod="getFallbackCatalog")
	public List<CatalogItem> getCatalogitems(@PathVariable String userId){
	
/*		List<Rating> ratings=Arrays.asList(
                new Rating("100", 3),
                new Rating("200", 4)
        );*/
		log.info("MovieCatalogController    getCatalogitems  Begin ");
		UserRating ratings=ratingInfo.getUserRating(userId);
		return ratings.getRatings().stream().map(rating->{
			log.info("MovieCatalogController    getCatalogitems  processng ");
			return movieInfo.getCatalogItem(rating);
			}).collect(Collectors.toList()
				);
		
		
		//return Collections.singletonList(new CatalogItem("Avatar","hollywood  scifi"+userId,4) );
		/*
		Alternative WebClient way
		Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
		.retrieve().bodyToMono(Movie.class).block();
		*/
	}
	
	
	
	
	public List<CatalogItem> getFallbackCatalog(@PathVariable String userId){
		return Arrays.asList(new CatalogItem("no movie","",0) );
		
	}
	
	
	

	

}
