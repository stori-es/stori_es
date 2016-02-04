package org.consumersunion.stories.server.rest.api.convio;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.RestTemplate;

/**
 * Class to probe the Convio system in order to gauge rate limits. Initial tests indicate that, at least in the sand
 * boxed systems, the rate limiting is very different from the rate limits as stated in the Conveyor docs. This lead
 * to a re-implementation of the cooperative rate logic on our side to implement a generic back off algorithm.
 */
public class ConvioRateLimitProbe {
    private static String getUserResource;
    private static RestTemplate restTemplate;

    public static void main(String[] args) {
        if (args.length != 4) {
            showUsage();
        }
        final String convioUrlBase = args[0];
        final String loginName = args[1];
        final String loginPassword = args[2];
        final String apiKey = args[3];

        final String loginResource = convioUrlBase + "/SRConsAPI?method=login&response_format=json&v=1.0&login_name=" +
                loginName + "&login_password=" + loginPassword + "&api_key=" + apiKey + "&user_name=" + loginName +
                "&password=" + loginPassword;
        getUserResource = convioUrlBase + "/SRConsAPI?method=getUser&response_format=json&v=1.0&login_name=" +
                loginName + "&login_password=" + loginPassword + "&api_key=" + apiKey + "&cons_id=1008836";

        restTemplate = new RestTemplate();

        restTemplate.postForObject(loginResource, null, String.class);
        /*
        callSequence(3, 2000);
		callSequence(3, 1250);
		callSequence(3, 1000);
		callSequence(3, 500);
		System.out.print("Pausing for 10s to let rate limits time out... ");
		pause(10000);
		System.out.println("done.\n");
		System.out.println("Bursing 12 calls without pause...");
		callSequence(12, 0);
		System.out.print("Pausing for 10s to let rate limits time out... ");
		pause(10000);
		System.out.println("done.\n");
		System.out.println("Bursing 24 calls without pause...");
		callSequence(24, 0);*/

        ConvioDataSynchronizationWorker synchronizationWorker = new ConvioDataSynchronizationWorker();
        ConvioSyncTask convioSyncRequest = new PollConvioForConstituentData(1008836, 2);
        convioSyncRequest.restTemplate = restTemplate;
        convioSyncRequest.objectMapper = new ObjectMapper();

        int i;
        for (i = 0; i < 20 && !synchronizationWorker.isOrgConnectionUnderLoad(2); i += 1) {
            synchronizationWorker.process(convioSyncRequest);
        }
        if (i < 20) {
            System.out.println("Ran " + i + " asynchronous calls, got pushed into load.");
        } else {
            System.out.println("Ran 20 asynchronous calls, no load status detected.");
        }
    }

    private static void callSequence(int number, long waitTime) {
        System.out.println("Call every " + formatSeconds(waitTime) + " for " + number + " calls:");
        final long start = System.currentTimeMillis();
        for (int i = 0; i < number; i += 1) {
            makeCall();
            pause(waitTime);
        }
        System.out.println("Total execution time: " + formatSeconds(System.currentTimeMillis() - start) + "\n");
    }

    private static void pause(long waitTime) {
        if (waitTime > 0) {
            synchronized (restTemplate) {
                try {
                    restTemplate.wait(waitTime);
                } catch (InterruptedException e) {
                    System.out.println("ERROR: test was interrupted. Bailing out.");
                }
            }
        }
    }

    private static void makeCall() {
        long start = System.currentTimeMillis();

		/*ConvioConstituentResponse constituentResponse =
				restTemplate.postForObject(getUserResource, new ConvioConstituent(), ConvioConstituentResponse
				.class);*/
        String response =
                restTemplate.postForObject(getUserResource, null, String.class);

        System.out.println("\tCall took: " + formatSeconds(System.currentTimeMillis() - start));
    }

    private static String formatSeconds(long ms) {
        return String.format("%.3f", (ms * 1.0) / 1000.0) + "s";
    }

    private static void showUsage() {
        System.out.println(
                "java ... ConvioRateLimitProbe <convio url base> <convio login name> <convio password> <convio api " +
                        "key>");
    }
}
