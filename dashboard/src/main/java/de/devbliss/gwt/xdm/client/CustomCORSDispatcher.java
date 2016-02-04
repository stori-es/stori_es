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

package de.devbliss.gwt.xdm.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.cors.CORS;
import org.fusesource.restygwt.client.dispatcher.DefaultDispatcher;
import org.fusesource.restygwt.client.dispatcher.DispatcherFilter;
import org.fusesource.restygwt.client.dispatcher.FilterawareDispatcher;
import org.fusesource.restygwt.client.dispatcher.FilterawareRetryingDispatcher;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Timer;
import com.google.gwt.xhr.client.XMLHttpRequest;

import de.devbliss.gwt.xdm.client.impl.CustomEasyXDMTransportLayer;

/**
 * This Dispatcher is used to enable CORS functionality in your GWT Project.
 * <p/>
 * Instead of {@link FilterawareRetryingDispatcher} or {@link DefaultDispatcher} you need to use
 * this dispatcher in order to benefit from the {@link CORS} annotation.
 * <p/>
 * The only difference between the other two dispatcher is that this one merely checks if the
 * {@link RequestBuilder} specified is a {@link CORSRequestBuilder} and, in case, assigns the
 * RestyMethod.
 *
 * @author <a href="mailto:rstiller@vz.net">rstiller</a>
 */
public class CustomCORSDispatcher implements FilterawareDispatcher {

    /**
     * The counter for the request ids.
     */
    protected static int requestIds = 0;
    /**
     * stores all the requestIds to their corresponding request object.
     */
    protected static final Map<Integer, RequestDataContainer> requestMapping =
            new HashMap<Integer, RequestDataContainer>();

    /**
     * Strips protocol and host from the given url.
     */
    protected static String getContext(String url) {
        int index = url.indexOf("://");
        if (index > -1 && (index = url.indexOf('/', index + 3)) > -1) {
            return url.substring(index);
        }
        return url;
    }

    /**
     * Removes the request with the specified id from the {@link #requestMapping}.
     */
    protected static void deleteRequest(int id) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            container.getTimer().cancel();
            requestMapping.remove(id);
        }
    }

    /**
     * Callback method for {@link TransportLayer} implementations to signal that the connection has
     * been opened. This method needs to be fired for each request in order to set the
     * request-timeout.
     *
     * @param id  identifies the request.
     * @param xhr the request object.
     */
    public static void opened(final int id, XMLHttpRequest xhr) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            container.setTimer(new Timer() {

                @Override
                public void run() {
                    CustomCORSDispatcher.cancel(id);
                }
            });
            container.setXhr(xhr);
            container.getCallback().opened(id);
            if (container.getRequest().getTimeout() > 0) {
                container.getTimer().schedule(container.getRequest().getTimeout());
            }
        }
    }

    /**
     * Callback method for {@link TransportLayer} implementations to signal that the response headers
     * have been received. This method is optional for each request.
     *
     * @param id identifies the request.
     */
    public static void headersReceived(int id) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            container.getCallback().headersReceived();
        }
    }

    /**
     * Callback method for {@link TransportLayer} implementations to signal that the response is about
     * to be received. This method is optional for each request.
     *
     * @param id identifies the request.
     */
    public static void loading(int id) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            container.getCallback().loading();
        }
    }

    /**
     * Callback method for {@link TransportLayer} implementations to signal that the response has been
     * received. This method needs to be fired in order to finish the request.
     *
     * @param id              identifies the request.
     * @param status          the status code from the response.
     * @param statusText      the status text from the response.
     * @param responseText    the response data.
     * @param responseHeaders the response headers.
     */
    public static void done(int id, int status, String statusText, String responseText,
            String responseHeaders) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            container.getCallback().done(status, statusText, responseText, responseHeaders);
        }
    }

    /**
     * Callback method for {@link TransportLayer} implementations to signal any kind of errors. This
     * method is optional.
     *
     * @param id      identifies the request.
     * @param message the error message.
     */
    public static void exception(int id, String message) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            container.getCallback().exception(message);
        }
    }

    /**
     * Callback method for {@link TransportLayer} implementations to signal that the access to the
     * requested resource is not permitted. This method is optional.
     *
     * @param id identifies the request.
     */
    public static void accessDenied(int id) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            container.getCallback().accessDenied();
        }
    }

    /**
     * Cancels the request with specified id.
     *
     * @param id identifies the request.
     */
    public static void cancel(int id) {
        RequestDataContainer container = requestMapping.get(id);
        if (container != null) {
            if (container.getXhr() != null) {
                container.getXhr().abort();
            }
            deleteRequest(id);
        }
    }

    /**
     * Instances a new com.google.gwt.xhr.client.XMLHttpRequest.
     */
    public static XMLHttpRequest createXHR() {
        return XMLHttpRequest.create();
    }

    /**
     * Exports the callback methods to be available for non-gwt js-code. The method are named as
     * follows:
     * <p/>
     * <ul>
     * <li>gwt_cors_opened - {@link #opened(int, XMLHttpRequest)}</li>
     * <li>gwt_cors_headersReceived - {@link #headersReceived(int)}</li>
     * <li>gwt_cors_loading - {@link #loading(int)}</li>
     * <li>gwt_cors_done - {@link #done(int, int, String, String, String)}</li>
     * <li>gwt_cors_exception - {@link #exception(int, String)}</li>
     * <li>gwt_cors_accessDenied - {@link #accessDenied(int)}</li>
     * <li>gwt_cors_createXHR - {@link #createXHR()}</li>
     * </ul>
     */
    public static native void exportMethods()
  /*-{
      $wnd.gwt_cors_opened = $entry(@de.devbliss.gwt.xdm.client.CustomCORSDispatcher::opened(ILcom/google/gwt/xhr/client/XMLHttpRequest;));
      $wnd.gwt_cors_headersReceived = $entry(@de.devbliss.gwt.xdm.client.CustomCORSDispatcher::headersReceived(I));
      $wnd.gwt_cors_loading = $entry(@de.devbliss.gwt.xdm.client.CustomCORSDispatcher::loading(I));
      $wnd.gwt_cors_done = $entry(@de.devbliss.gwt.xdm.client.CustomCORSDispatcher::done(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;));
      $wnd.gwt_cors_exception = $entry(@de.devbliss.gwt.xdm.client.CustomCORSDispatcher::exception(ILjava/lang/String;));
      $wnd.gwt_cors_accessDenied = $entry(@de.devbliss.gwt.xdm.client.CustomCORSDispatcher::accessDenied(I));
      $wnd.gwt_cors_createXHR = $entry(@de.devbliss.gwt.xdm.client.CustomCORSDispatcher::createXHR());
  }-*/;

    /**
     * list of filters to be used when a request is about to get done.
     */
    protected final List<DispatcherFilter> filters = new ArrayList<DispatcherFilter>();
    /**
     * the transportLayer used.
     */
    protected final TransportLayer layer;

    /**
     * constructs a new CORSDispatcher and exports the callback methods.
     *
     * @param layer The TransportLayer to use.
     */
    public CustomCORSDispatcher(TransportLayer layer) {
        this.layer = layer;
        exportMethods();
    }

    public CustomCORSDispatcher() {
        this(GWT.<TransportLayer>create(CustomEasyXDMTransportLayer.class));
    }

    /**
     * Checks if the specified {@link RequestBuilder} is a {@link CORSRequestBuilder} and assigns the
     * specified method to it.
     * <p/>
     * But before performing anything the {@link DispatcherFilter}s are used to determine if the
     * request should be performed at all.
     *
     * @param method  The RestyMethod which is used to filter.
     * @param builder The {@link RequestBuilder} used to perform the request.
     */
    @Override
    public Request send(Method method, RequestBuilder builder) throws RequestException {
        // abort request if the first filter signals a veto
        for (DispatcherFilter filter : filters) {
            if (!filter.filter(method, builder)) {
                return null;
            }
        }

        // do a cross domain request if necessary.
        if (builder instanceof Method.MethodRequestBuilder) {
            return doCrossDomainRequest(method, (Method.MethodRequestBuilder) builder);
        }

        // ... or simple proceed as usual
        return builder.send();
    }

    /**
     * Adds the specified filter to the list of filters used to grant requests. Filters can be
     * assigned many times.
     *
     * @param filter The filter to request.
     */
    @Override
    public void addFilter(DispatcherFilter filter) {
        if (filter != null) {
            filters.add(filter);
        }
    }

    /**
     * This method first checks if a CORS Request is required. If so, a new {@link CORSRequest} and
     * {@link CORSCallback} is built and
     * {@link #performRequest(String, String, String, String, String, String, String, String, String, JavaScriptObject,
     * int, CORSCallback)}
     * is called.
     * <p/>
     * Before that, the values of the CORS annotation are checked if they are value expressions and if
     * so the JavaScript value will be assigned.
     * <p/>
     * If no CORS Request is required the requestBuilder's send method is called.
     *
     * @param method         The resty method.
     * @param requestBuilder The builder with the request information (url, etc.).
     */
    protected Request doCrossDomainRequest(Method method,
            final Method.MethodRequestBuilder requestBuilder) throws RequestException {
        final CORSRequest request;
        CORSCallback callback;
        String domain, port, protocol;
        TransportRequest transportRequest;
        Map<String, String> parameters;
        RequestDataContainer container;

        // is CORS necessary?
        if (isCORSNecessary(method)) {
            request = new CORSRequest();
            // the CORSCallback handles the resty-callback
            callback = new CORSCallback() {

                @Override
                public void done(int status, String statusText, String responseText, String headers) {
                    super.done(status, statusText, responseText, headers);
                    requestBuilder.getCallback().onResponseReceived(request,
                            new CORSResponse(status, statusText, responseText, headers));
                }

                @Override
                public void exception(String message) {
                    super.exception(message);
                    requestBuilder.getCallback().onError(request, new RuntimeException(message));
                }

                @Override
                public void accessDenied() {
                    super.accessDenied();
                    requestBuilder.getCallback().onError(request, new RuntimeException("Access Denied"));
                }
            };

            // get the value of the JS Variable if it is a variable expression
            domain = normalizeString(method.getData().get(CORS.DOMAIN));
            if (isJSVariable(domain)) {
                domain = getJSVariableName(domain);
                domain = getJSVarValue(domain);
            }

            protocol = normalizeString(method.getData().get(CORS.PROTOCOL));
            if (isJSVariable(protocol)) {
                protocol = getJSVariableName(protocol);
                protocol = getJSVarValue(protocol);
            }

            port = normalizeString(method.getData().get(CORS.PORT));
            if (isJSVariable(port)) {
                port = getJSVariableName(port);
                port = getJSVarValue(port);
            }

            parameters = new HashMap<String, String>();

            transportRequest = new TransportRequest();
            container = new RequestDataContainer();

            transportRequest.setData(requestBuilder.getRequestData());
            transportRequest.setHeaders(requestBuilder.getHeaders());
            transportRequest.setId(requestIds++);
            transportRequest.setMethod(requestBuilder.getHTTPMethod());
            transportRequest.setPassword(requestBuilder.getPassword());
            transportRequest.setUrl(getContext(requestBuilder.getUrl()));
            transportRequest.setUser(requestBuilder.getUser());
            transportRequest.setHost(domain);
            transportRequest.setPort(port);
            transportRequest.setProtocol(protocol);
            transportRequest.setTimeout(requestBuilder.getTimeoutMillis());

            container.setCallback(callback);
            container.setRequest(transportRequest);

            requestMapping.put(transportRequest.getId(), container);

            layer.call(transportRequest, parameters);

            return request;
        }

        return requestBuilder.send();
    }

    /**
     * Strips the leading and trailing bracket and quota.
     *
     * @param str The value to be stripped.
     */
    protected String normalizeString(String str) {
        return str.substring(2, str.length() - 2);
    }

    /**
     * Determines if the specified request needs to be executed via gwt_cors.request or not. It merely
     * checks if there is a field in the data attribute which key starts with "cors_".
     *
     * @param method The object used to determine if CORS is necessary.
     */
    protected boolean isCORSNecessary(Method method) {
        for (Map.Entry<String, String> entry : method.getData().entrySet()) {
            if (entry.getKey().startsWith("cors_")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the value is a real value or a variable expression.
     *
     * @param value The value to inspect.
     */
    protected boolean isJSVariable(String value) {
        return value != null && value.startsWith("${") && value.endsWith("}");
    }

    /**
     * Extracts the variable name from the expression string.
     *
     * @param value The value to get the JS variable name for.
     */
    protected String getJSVariableName(String value) {
        return value.substring(2).substring(0, value.length() - 3);
    }

    /**
     * Gets the value of the specified JS variable.
     *
     * @param varName The name of the JavaScript variable.
     */
    protected static native String getJSVarValue(String varName)/*-{
        return $wnd[varName];
    }-*/;
}
