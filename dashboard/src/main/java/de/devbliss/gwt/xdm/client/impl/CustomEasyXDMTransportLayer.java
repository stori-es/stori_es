/**
 * Copyright 2012 devbliss GmbH
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.devbliss.gwt.xdm.client.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import de.devbliss.gwt.xdm.client.CORSDispatcher;
import de.devbliss.gwt.xdm.client.TransportLayer;
import de.devbliss.gwt.xdm.client.TransportRequest;

/**
 * This is an implementation of a {@link TransportLayer} which uses the <a
 * href="http://easyxdm.net">EasyXDM framework</a> for browsers which don't support CORS.
 *
 * @author <a href="mailto:hbilges@vz.net">hbilges</a>
 */
public class CustomEasyXDMTransportLayer extends AbstractTransportLayer {

    /**
     * Maps the easyXDM rpc object to the corresponding domains (to avoid unnecessary object creation).
     */
    private static final HashMap<String, JavaScriptObject> RPCS = new HashMap<String, JavaScriptObject>();

    /**
     * Transforms all the request data needed to perform a request with easyXDM and actually performs the request.
     *
     * @param request    The request object with all the information needed
     * @param parameters additional parameters - unused
     */
    @Override
    public void call(TransportRequest request, Map<String, String> parameters) {
        String port = request.getPort();
        String corsUri;
        JavaScriptObject rpc;
        JSONObject headersJson;
        String requestData;

        // builds url, port and protocol
        port = port == null || port.trim().length() == 0 ? "" : ":" + port;
        corsUri = Window.Location.getProtocol() + "//" + request.getHost() + port + "/cors/index.html";
        // gets the easyXDM RPC object
        rpc = getRpcForUri(corsUri);

        // builds the headers as JSON string
        headersJson = new JSONObject();
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            headersJson.put(entry.getKey(), new JSONString(entry.getValue()));
        }

        // assigns the request parameters
        requestData = request.getData() != null ? request.getData() : "{}";

        // called for the timeout assignment
        CORSDispatcher.opened(request.getId(), null);

        // performs the request
        CustomEasyXDMTransportLayer.request(rpc, request.getId(), request.getUrl(), request.getMethod(),
                requestData, headersJson.toString());
    }

    /**
     * Gets the easyXDM RPC object - by cache or a new one.
     */
    private static JavaScriptObject getRpcForUri(String uri) {
        JavaScriptObject rpc = RPCS.get(uri);
        if (rpc == null) {
            rpc = getNativeRpc(uri);
            RPCS.put(uri, rpc);
        }
        return rpc;
    }

    /**
     * Performs the request with the given RPC object.
     * This method calls the {@link CORSDispatcher} callback methods.
     */
    private static native void request(JavaScriptObject rpc, int id, String domain,
            String requestMethod, String requestData, String requestHeaders)
    /*-{
        rpc.request({
                url: domain,
                method: requestMethod,
                headers: $wnd.JSON.parse(requestHeaders),
                data: requestData
            }, function (response) {
                var headers = $wnd.JSON.stringify(response.headers);
                headers = headers.replace(/\{|\}/g, '')
                    .replace(/\",\"/g, '\n')
                    .replace(/\\\"/g, '@#*!')
                    .replace(/\"/g, '')
                    .replace(/@#\*!/g, '\"');
                @de.devbliss.gwt.xdm.client.CustomCORSDispatcher::done(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id, response.status, null, response.data, headers);
            }, function (error) {
                @de.devbliss.gwt.xdm.client.CustomCORSDispatcher::exception(ILjava/lang/String;)(id, error.message);
            }
        );
    }-*/;

    /**
     * Generates the easyXDM RPC object of the given domain.
     */
    private static native JavaScriptObject getNativeRpc(String corsURI)
    /*-{
        var rpc = new $wnd.easyXDM.Rpc({
                remote: corsURI
            }, {
                remote: {
                    request: {}
                }
            }
        );
        return rpc;
    }-*/;
}
