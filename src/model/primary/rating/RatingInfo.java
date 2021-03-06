package model.primary.rating;

import model.helperObjects.Critic;
import model.primary.customer.CustomerInfo;
import model.primary.customer.AgeRange;
import model.primary.movie.RatingAndTime;
import wrappers.MovieAgeRangeMap;
import wrappers.RatingsMap;

import java.util.*;

public class RatingInfo {


    private final RatingsMap customerIdMovieIdRatingAndTimeMap;

    public RatingInfo(RatingsMap customerIdMovieIdRatingAndTimeMap) {
        this.customerIdMovieIdRatingAndTimeMap = customerIdMovieIdRatingAndTimeMap;
    }


    public Map<Integer, Integer> getMovieIdViewsCountMap() {
        Map<Integer, Integer> movieIdViewCount = new HashMap<>();

        for (Map<Integer, RatingAndTime> movieIdRatingAndTimeMap : customerIdMovieIdRatingAndTimeMap.values()) {
            Set<Integer> movieIdSet = movieIdRatingAndTimeMap.keySet();

            for (Integer movieID : movieIdSet) {
                int count = movieIdViewCount.getOrDefault(movieID, 0);
                movieIdViewCount.put(movieID, count + 1);
            }
        }
        return movieIdViewCount;
    }

    public Map<Integer, Integer> getMovieIdRatingsMap() {
        Map<Integer, Integer> movieIdRatings = new HashMap<>();

        for (Map<Integer, RatingAndTime> movieIdRatingAndTimeMap : customerIdMovieIdRatingAndTimeMap.values()) {

            for (int movieId : movieIdRatingAndTimeMap.keySet()) {
                RatingAndTime ratingAndTime = movieIdRatingAndTimeMap.get(movieId);

                int movieRating = ratingAndTime.getRating();
                int existingRatings = movieIdRatings.getOrDefault(movieId, 0);

                movieIdRatings.put(movieId, existingRatings + movieRating);

            }
        }
        return movieIdRatings;
    }


    public MovieAgeRangeMap getMovieIdAgeRangeMap(CustomerInfo customerInfo) {

        MovieAgeRangeMap movieIdAgeRangeMap = new MovieAgeRangeMap();

        for (Integer customerID : customerIdMovieIdRatingAndTimeMap.keySet()) {

            AgeRange ageRange = customerInfo.getAgeRange(customerID);

            Map<Integer, RatingAndTime> idRatingAndTimeMap = customerIdMovieIdRatingAndTimeMap.get(customerID);

            Set<Integer> movieIdSet = idRatingAndTimeMap.keySet();

            for (Integer movieID : movieIdSet) {

                EnumMap<AgeRange, Integer> ageRangeCountMap = movieIdAgeRangeMap.getOrDefault(movieID, new EnumMap<>(AgeRange.class));

                int presentAgeRangeCount = ageRangeCountMap.getOrDefault(ageRange, 0);

                ageRangeCountMap.put(ageRange, presentAgeRangeCount + 1);

                movieIdAgeRangeMap.put(movieID, ageRangeCountMap);
            }

        }
        return movieIdAgeRangeMap;
    }


    public Map<Integer, Long> getCustomerIdRatingMap() {
        Map<Integer, Long> customerIdRatingMap = new HashMap<>();

        List<Integer> customerIDs = getAllCustomerIds();

        for (int customerId : customerIDs) {
            Map<Integer, RatingAndTime> movieIdRatingAndTimeMap = customerIdMovieIdRatingAndTimeMap.get(customerId);

            Set<Integer> movieIds = movieIdRatingAndTimeMap.keySet();
            long ratingCount = 0;

            for (int movieId : movieIds) {
                RatingAndTime ratingAndTime = movieIdRatingAndTimeMap.get(movieId);
                ratingCount += ratingAndTime.getRating();
            }
            customerIdRatingMap.put(customerId, ratingCount);
        }
        return customerIdRatingMap;
    }

    public List<Integer> getAllCustomerIds() {
        List<Integer> customerIdList = new ArrayList<>(customerIdMovieIdRatingAndTimeMap.keySet());
        return customerIdList;
    }

    public Map<Integer, Integer> getCustomerIdMoviesSeenCountMap() {
        Map<Integer, Integer> customerIdViewershipMap = new HashMap<>();

        List<Integer> customerIdList = new ArrayList<>(customerIdMovieIdRatingAndTimeMap.keySet());

        for (int customerId : customerIdList) {

            Set<Integer> movieIdsSet = customerIdMovieIdRatingAndTimeMap.get(customerId).keySet();
            int moviesWatched = movieIdsSet.size();

            customerIdViewershipMap.put(customerId, moviesWatched);
        }

        return customerIdViewershipMap;
    }

    public void printCustomerMovieRatings(List<Critic> critics) {

        for (Critic critic : critics) {
            System.out.printf("The customer id is %d %n", critic.getId());
            System.out.printf("The average rating given" +
                    " by the customer is %.2f %n", critic.getRating());
            System.out.printf("The movie view count of " +
                    "the given customer is %d %n %n", critic.getViews());
        }
    }
}
