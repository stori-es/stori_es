package org.consumersunion.stories.server.rest.api.convio;

import java.util.HashMap;
import java.util.Map;

import org.consumersunion.stories.common.shared.service.GeneralException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * A singleton object which coordinates all synchronization between SYS Person and Collection membership data with the
 * Convio Constituent data set. The class tracks request rates on a per-Organization basis and determines whether any
 * given Organization is 'under load' or not. Incoming requests are queued based on priority and the load status.
 * <p/>
 * The current logic implements cooperative rate limits that 'backs off' sending requests on a per-{@link Organization}
 * basis using a linear back-off and increase that relatively slowly backs off and quickly speeds up when things clear
 * up. I don't remember the details of network coordination; there's probably a log or exponential involved in the
 * 'ideal' cooperative rate algorithms.
 */
@Component("convioSyncWorker")
public class ConvioDataSynchronizationWorker {
    final Map<Integer, Object> syncGates = new HashMap<Integer, Object>();

    static class ConvioLoadStatus {
        private static final Map<Integer, ConvioLoadStatus> loadStatti = new HashMap<Integer, ConvioLoadStatus>();

        public static ConvioLoadStatus getLoadStatusFor(int orgId) {
            if (!loadStatti.containsKey(orgId)) {
                loadStatti.put(orgId, new ConvioLoadStatus());
            }

            return loadStatti.get(orgId);
        }

        /**
         * We try and play nice with the Convio server which is documented to enforce strict rate limiting. So, if we
         * detect a channel becoming clogged, we 'back off' on our requests until things start to settle down.
         * Initially there's no back off.
         * <p/>
         * Note that in practice, we have been unable to detect this, but have so far only run tests against sand boxed
         * test systems.
         */
        long currentBackOffMs = 0;

        /**
         * Determines if a Organization is 'under load', which is defined by Convio as having sent more than 10
         * requests
         * in the last 10 seconds (or 10,000 ms). We add a quarter of second buffer, so we're slightly conservative
         * about load, helping to ensure that we avoid overload situations.
         */
        public boolean isUnderLoad() {
            return this.currentBackOffMs >= 1000;
        }

        /**
         * Recalculate the 'back off' for the next round.
         */
        public void recalcBackOff(ConvioSyncTask convioSyncRequest) {
            if (convioSyncRequest.isServiceSuccess()) {
                // When faced with success, we optimistically reduce the next back off dramatically
                if (!this.isUnderLoad()) { // If not under load, it's back to zero.
                    currentBackOffMs = 0;
                } else { // Reduce current back off by 70-90 ninety percent.
                    currentBackOffMs *= 0.1 + Math.random() * 0.2;
                }
            } else { // In failure, we're still optimistic.
                if (!this.isUnderLoad()) {
                    // If there's a service failure, we do immediately jump to an 'under load' status, which is set to
                    // back off 1000-1250ms because the Convio docs state (though we have been unable to detect) rate
                    // limiting to 1 request per second after an initial bursts of up to 10  requests.
                    currentBackOffMs = 1000l + (long) (Math.random() * 250.0);
                } else { // We're still optimistic, so we only increase the back off by 15-25 percent.
                    currentBackOffMs *= 1.15 + Math.random() * 0.1;
                }
            }
        }
    }

    /**
     * Synchronous request processing.
     */
    public ConvioSyncTask process(ConvioSyncTask convioSyncRequest) {
        final ConvioLoadStatus loadStatus = ConvioLoadStatus.getLoadStatusFor(convioSyncRequest.getOrgId());
        if (!loadStatus.isUnderLoad()) {
            // if the system is not-under load, then we issue the request immediately
            executeRequest(convioSyncRequest, loadStatus);
        }
        // else system is under load and we skip the synchronous requests

        return convioSyncRequest;
    }

    @Async
    public void processAsync(ConvioSyncTask convioSyncRequest) {
        final ConvioLoadStatus loadStatus = ConvioLoadStatus.getLoadStatusFor(convioSyncRequest.getOrgId());
        if (loadStatus.isUnderLoad()) {
            try {
                System.out.println("sleeping: " + loadStatus.currentBackOffMs);
                Thread.sleep(loadStatus.currentBackOffMs);
            } catch (InterruptedException e) {
                // We interpret an interruption as an unrecoverable runtime error.
                throw new GeneralException(e);
            }
        }
        try {
            // The calls to Convio need to be serialized on a per-org basis.
            System.out.println("synchronizing...");
            executeRequest(convioSyncRequest, loadStatus);
            convioSyncRequest.synchronize();
        } finally {
            // Positioning the markRequest() call here tends to over-estimate the effect of the call, intentionally
            // tipping us towards a more conservative call on the 'under load' status.
            loadStatus.recalcBackOff(convioSyncRequest);
        }
    }

    private void executeRequest(ConvioSyncTask convioSyncRequest, ConvioLoadStatus loadStatus) {
        try {
            Object syncGate = syncGates.get(convioSyncRequest.getOrgId());
            if (syncGate == null) {
                syncGate = new Object();
                syncGates.put(convioSyncRequest.getOrgId(), syncGate);
            }
            synchronized (syncGate) {
                convioSyncRequest.synchronize();
            }
        } finally {
            // Positioning the markRequest() call here tends to over-estimate the effect of the call, intentionally
            // tipping us towards a more conservative call on the 'under load' status.
            loadStatus.recalcBackOff(convioSyncRequest);
        }
    }
    /*
    @Scheduled(cron = "0 55 23 ? * *")
	public void scheduledProcess() throws Exception {
		scheduled.addAll(failed);
		failed.clear();
		for (ConvioSyncRequest convioSyncRequest : scheduled) {
			convioSyncRequest.synchronize();
		}
		scheduled.clear();
	}*/

    public boolean isOrgConnectionUnderLoad(int orgId) {
        return ConvioLoadStatus.getLoadStatusFor(orgId).isUnderLoad();
    }
}
