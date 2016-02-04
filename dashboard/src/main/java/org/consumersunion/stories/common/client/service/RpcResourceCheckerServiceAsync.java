package org.consumersunion.stories.common.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcResourceCheckerServiceAsync {
    void checkURL(String url, AsyncCallback<Boolean> async);

    void checkValidPDF(String url, AsyncCallback<Boolean> async);

    void checkValidImage(String url, AsyncCallback<Boolean> async);

    void checkValidVideo(String url, AsyncCallback<Boolean> async);

    void checkValidAudio(String url, AsyncCallback<Boolean> callback);
}
