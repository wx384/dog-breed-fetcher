package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://dog.ceo/api/breed/%s/list";
    private static final String SUCCESS_MESSAGE = "success";
    private static final String MESSAGE = "message";

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // TODO Task 1: Complete this method based on its provided documentation
        //      and the documentation for the dog.ceo API. You may find it helpful
        //      to refer to the examples of using OkHttpClient from the last lab,
        //      as well as the code for parsing JSON responses.
        final Request request = new Request.Builder()
                .url(String.format(API_URL, breed))
                .method("GET", null)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            // string.equals()
            // int == int
            if (responseBody.getString("status").equals(SUCCESS_MESSAGE)) {
                final JSONArray subBreedsArray = responseBody.getJSONArray(MESSAGE);
                final List<String> subBreeds = new ArrayList<>();

                for (int i = 0; i < subBreedsArray.length(); i++) {
                    subBreeds.add(subBreedsArray.getString(i));
                };

                return subBreeds;
            }
            else {
                // But if the error is already a BreedNotFoundException,
                // rethrowing a new one just loses the stack trace and adds no value.
                throw new BreedNotFoundException(responseBody.getString(MESSAGE));
            }
        }
        // | BNFException no need
        catch (IOException | JSONException event) {
            throw new BreedNotFoundException(breed);
        }

    }
}

/*
learn:
@Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Build URL using the incoming breed
        String normalized = breed == null ? "" : breed.toLowerCase();
        String encoded = URLEncoder.encode(normalized, StandardCharsets.UTF_8);
        String url = String.format(API_URL_TEMPLATE, encoded);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException(breed);
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new BreedNotFoundException(breed);
            }

            JSONObject json = new JSONObject(body.string());

            // Expect: { "status": "success", "message": [ ... ] }
            if (!SUCCESS.equals(json.optString(STATUS))) {
                // Some error from API; turn into domain exception
                throw new BreedNotFoundException(breed);
            }

            JSONArray arr = json.getJSONArray(MESSAGE);
            List<String> subBreeds = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                subBreeds.add(arr.getString(i));
            }
            return subBreeds;

        } catch (IOException | JSONException e) {
            // Translate low-level failures into domain exception, keep cause
            throw new BreedNotFoundException(breed, e);
        }
    }
}
 */