package controller;

import model.helperObjects.RatedMovie;
import model.primary.customer.AgeRange;
import model.primary.customer.CustomerInfo;
import model.primary.rating.RatingInfo;
import wrappers.MovieAgeRangeMap;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopRatedMoviesViewership {
    private int top;
    private int minViews;
    private RatingInfo ratingInfo;
    private CustomerInfo customerInfo;
    private MovieAgeRangeMap movieAgeRangeMap;


    public TopRatedMoviesViewership(int top,
                                    int minViews,
                                    RatingInfo ratingInfo,
                                    CustomerInfo customerInfo) {
        this.top = top;
        this.minViews = minViews;
        this.ratingInfo = ratingInfo;
        this.customerInfo = customerInfo;
    }

    public Map<Integer, Map> invoke() {

        movieAgeRangeMap = movieIdCustomerAgeRange(top, minViews);

        List<RatedMovie> ratedMovieList = new TopRatedMovies(top, minViews, ratingInfo).getMovieList();
        Map<Integer, Map> movieIdAgeRangeCount = new HashMap<>();

        for (RatedMovie movie : ratedMovieList) {
            Map<Integer, Integer> ageCategoryCount = ageCategoryCountMap(movie, movieAgeRangeMap);

            movieIdAgeRangeCount.put(movie.getId(), ageCategoryCount);
        }

        return movieIdAgeRangeCount;
    }

    private Map<Integer, Integer> ageCategoryCountMap(RatedMovie movie,
                                                      Map<Integer, EnumMap<AgeRange, Integer>> movieViewerAgeDistribution) {
        Map<Integer, Integer> ageCategoryCount = new HashMap<>();

        int movieID = movie.getId();
        int[] ageRange = new int[3];

        EnumMap<AgeRange, Integer> ageRanges = movieViewerAgeDistribution.get(movieID);

        ageRange[0] = ageRanges.getOrDefault(AgeRange.UNDER_EIGHTEEN, 0);
        ageRange[1] = ageRanges.getOrDefault(AgeRange.TWENTY_FIVE_TO_THIRTY_FOUR, 0);
        ageRange[2] = ageRanges.getOrDefault(AgeRange.FORTY_FIVE_TO_FORTY_NINE, 0);
        ageRange[2] = ageRange[2] + ageRanges.getOrDefault(AgeRange.FIFTY_TO_FIFTY_FIVE, 0);
        ageRange[2] = ageRange[2] + ageRanges.getOrDefault(AgeRange.FIFTY_SIX_AND_OVER, 0);


        ageCategoryCount.put(0, ageRange[0]);
        ageCategoryCount.put(1, ageRange[1]);
        ageCategoryCount.put(2, ageRange[2]);

        return ageCategoryCount;

    }

    private MovieAgeRangeMap movieIdCustomerAgeRange(int N, int minViews) {

       MovieAgeRangeMap movieIdAgeMap = new MovieAgeRangeMap();

        // go through the list , getThe age range of person and put it in the map for movie
        List<RatedMovie> ratedMovieList = new TopRatedMovies(N, minViews,ratingInfo).getMovieList();
        Map<Integer, EnumMap<AgeRange, Integer>> movieIdAgeRangeMap = ratingInfo.getMovieIdAgeRangeMap(customerInfo);
        ratingInfo.getMovieIdAgeRangeMap(customerInfo);

        // go through movie and for movie ID , fill the Emum Map
        for (RatedMovie movie : ratedMovieList) {
            movieIdAgeMap.put(movie.getId(), movieIdAgeRangeMap.get(movie.getId()));
        }

        return movieIdAgeMap;

    }
}