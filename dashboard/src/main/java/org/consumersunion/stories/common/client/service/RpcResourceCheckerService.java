package org.consumersunion.stories.common.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/resource")
public interface RpcResourceCheckerService extends RemoteService {
    Boolean checkURL(String url);

    Boolean checkValidPDF(String url);

    Boolean checkValidImage(String url);

    Boolean checkValidVideo(String url);

    Boolean checkValidAudio(String url);
}
