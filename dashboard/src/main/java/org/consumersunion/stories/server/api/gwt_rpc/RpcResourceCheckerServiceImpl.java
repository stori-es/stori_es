package org.consumersunion.stories.server.api.gwt_rpc;

import org.consumersunion.stories.common.client.service.RpcResourceCheckerService;
import org.consumersunion.stories.server.util.URLChecker;

public class RpcResourceCheckerServiceImpl extends RpcBaseServiceImpl implements RpcResourceCheckerService {
    @Override
    public Boolean checkURL(String url) {
        return URLChecker.verifyURL(url);
    }

    @Override
    public Boolean checkValidPDF(String url) {
        return URLChecker.verifyPDFDocument(url);
    }

    @Override
    public Boolean checkValidImage(String url) {
        return URLChecker.verifyImage(url);
    }

    @Override
    public Boolean checkValidVideo(String url) {
        return URLChecker.verifyVideo(url);
    }

    @Override
    public Boolean checkValidAudio(String url) {
        return URLChecker.verifyAudio(url);
    }
}
