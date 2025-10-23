package dogapi;

import org.json.JSONException;

import java.io.IOException;
import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 *      * A cache is a temporary storage that keeps results you’ve already computed or fetched,
 *      * so you don’t have to fetch them again.
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // TODO Task 2: Complete this class
    private int callsMade = 0;

    // instance variables
    private Map cache = new HashMap();
    private BreedFetcher fetcher;

    // constructor
    public CachingBreedFetcher(BreedFetcher fetcher) {
        // fetcher can hold any object that implements the BreedFetcher interface like DogApiBreedFetcher
        // The CachingBreedFetcher wraps it and adds caching behavior
        //This pattern is called a decorator
            // — one object wraps another to extend its behavior without changing it.

        this.fetcher = fetcher;


    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // in cache, return list
        try {
            if (cache.containsKey(breed)) {
                return (List<String>) cache.get(breed);
            }
            else {
                List<String> subBreeds = fetcher.getSubBreeds(breed);
                cache.put(breed, subBreeds);
                callsMade++;
                return subBreeds;
            }
        }
        catch (BreedNotFoundException event) {
            callsMade++;
            throw new BreedNotFoundException(breed);
        }

        // not in cache
            // make a call to DogApi
            // callsMade++;
            // store list in cache
            // return
    }

    public int getCallsMade() {
        return callsMade;
    }
}