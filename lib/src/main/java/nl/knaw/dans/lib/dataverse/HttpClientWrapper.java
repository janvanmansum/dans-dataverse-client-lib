/*
 * Copyright (C) 2021 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.lib.dataverse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.hc.client5.http.utils.Base64.encodeBase64String;
import static org.apache.hc.core5.http.HttpHeaders.AUTHORIZATION;

/**
 * Helper class that wraps an HttpClient, the configuration data and a Jackson object mapper. It implements generic methods for sending HTTP requests to the server and deserializing the responses
 * received.
 */
@Slf4j
class HttpClientWrapper implements MediaTypes {
    private static final String HEADER_X_DATAVERSE_KEY = "X-Dataverse-key";
    private static final String UNBLOCK_KEY = "unblock-key";

    @Getter
    private final DataverseClientConfig config;
    private org.apache.hc.client5.http.classic.HttpClient httpClient5;
    private final ObjectMapper mapper;

    // If false, it is sent through the X-Dataverse-key header
    private boolean sendApiTokenViaBasicAuth = false;

    HttpClientWrapper(DataverseClientConfig config, org.apache.hc.client5.http.classic.HttpClient httpClient5, ObjectMapper mapper) {
        this.config = config;
        this.httpClient5 = httpClient5;
        this.mapper = mapper;
    }

    public HttpClientWrapper sendApiTokenViaBasicAuth() {
        HttpClientWrapper wrapper = new HttpClientWrapper(getConfig(), httpClient5, mapper);
        wrapper.sendApiTokenViaBasicAuth = true;
        return wrapper;
    }

    public String writeValueAsString(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    /*
     * POST methods
     */
    public <D> DataverseHttpResponse<D> post2(Path subPath, org.apache.hc.core5.http.HttpEntity body, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        var post = new org.apache.hc.client5.http.classic.methods.HttpPost(buildURi(subPath, parameters));
        headers.forEach(post::setHeader);
        post.setEntity(body);
        return wrap(dispatch(post), c);
    }

    public <D> DataverseHttpResponse<D> postModelObjectAsJson(Path subPath, Object modelObject, Class<?>... c) throws IOException, DataverseException {
        return postModelObjectAsJson(subPath, modelObject, new HashMap<>(), new HashMap<>(), c);
    }

    public <D> DataverseHttpResponse<D> postModelObjectAsJson(Path subPath, Object modelObject, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        return postJsonString2(subPath, writeValueAsString(modelObject), parameters, headers, c);
    }

    public <D> DataverseHttpResponse<D> postJsonString2(Path subPath, String s, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        return wrap(postString2(subPath, s, APPLICATION_JSON, parameters, headers), c);
    }

    public <D> DataverseHttpResponse<D> postJsonLdString(Path subPath, String s, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        return wrap(postString2(subPath, s, APPLICATION_JSON_LD, parameters, headers), c);
    }

    private DispatchResult postString2(Path subPath, String s, String mediaType, Map<String, List<String>> parameters, Map<String, String> headers)
        throws IOException, DataverseException {
        var post = new org.apache.hc.client5.http.classic.methods.HttpPost(buildURi(subPath, parameters));
        post.setHeader(HttpHeaders.CONTENT_TYPE, mediaType);
        headers.forEach(post::setHeader);
        post.setEntity(new org.apache.hc.core5.http.io.entity.StringEntity(s, StandardCharsets.UTF_8));
        return dispatch(post);
    }

    /*
     * PUT methods
     */
    public <D> DataverseHttpResponse<D> putModelObjectAsJson(Path subPath, D modelObject, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        return putJsonString(subPath, mapper.writeValueAsString(modelObject), parameters, headers, c);
    }

    public <D> DataverseHttpResponse<D> putJsonString(Path subPath, String s, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        return wrap(putString2(subPath, s, APPLICATION_JSON, parameters, headers), c);
    }

    public <D> DataverseHttpResponse<D> putJsonLdString(Path subPath, String s, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        return wrap(putString2(subPath, s, APPLICATION_JSON_LD, parameters, headers), c);
    }

    public <D> DataverseHttpResponse<D> putTextString(Path subPath, String s, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... c)
        throws IOException, DataverseException {
        return wrap(putString2(subPath, s, TEXT_PLAIN, parameters, headers), c);
    }

    private DispatchResult putString2(Path subPath, String s, String mediaType, Map<String, List<String>> parameters, Map<String, String> headers)
        throws IOException, DataverseException {
        var put = new org.apache.hc.client5.http.classic.methods.HttpPut(buildURi(subPath, parameters));
        put.setHeader(HttpHeaders.CONTENT_TYPE, mediaType);
        headers.forEach(put::setHeader);
        put.setEntity(new org.apache.hc.core5.http.io.entity.StringEntity(s, StandardCharsets.UTF_8));
        return dispatch(put);
    }

    /*
     * GET methods
     */
    public <D> DataverseHttpResponse<D> get(Path subPath, Class<?>... outputClass) throws IOException, DataverseException {
        return get(subPath, new HashMap<>(), outputClass);
    }

    public <D> DataverseHttpResponse<D> get(Path subPath, Map<String, List<String>> parameters, Class<?>... outputClass) throws IOException, DataverseException {
        return get2(subPath, parameters, Collections.emptyMap(), outputClass);
    }

    public <D> DataverseHttpResponse<D> get2(Path subPath, Map<String, List<String>> parameters, Map<String, String> headers, Class<?>... outputClass)
        throws IOException, DataverseException {
        var get = new org.apache.hc.client5.http.classic.methods.HttpGet(buildURi(subPath, parameters));
        headers.forEach(get::setHeader);
        return wrap(dispatch(get), outputClass);
    }

    public <T> void get(Path subPath, Map<String, List<String>> parameters, Map<String, String> headers, HttpClientResponseHandler<T> handler) throws IOException, DataverseException {
        var get = new org.apache.hc.client5.http.classic.methods.HttpGet(buildURi(subPath, parameters));
        headers.forEach(get::setHeader);
        dispatch(get, handler);
    }

    /*
     * DELETE methods
     */
    public <D> DataverseHttpResponse<D> delete(Path subPath, Class<?>... outputClass) throws IOException, DataverseException {
        return delete(subPath, new HashMap<>(), outputClass);
    }

    public <D> DataverseHttpResponse<D> delete(Path subPath, Map<String, List<String>> parameters, Class<?>... outputClass) throws IOException, DataverseException {
        var delete = new org.apache.hc.client5.http.classic.methods.HttpDelete(buildURi(subPath, parameters));
        return wrap(dispatch(delete), outputClass);
    }

    public DataverseHttpResponse<Object> delete(Path subPath) throws IOException, DataverseException {
        return delete(subPath, new HashMap<>(), Object.class);
    }

    /*
     *  Helper methods.
     */
    private URI buildURi(Path subPath, Map<String, List<String>> parameters) {
        try {
            List<NameValuePair> nameValuePairs = parameters.entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(v -> new BasicNameValuePair(e.getKey(), v)))
                .collect(Collectors.toList());
            URIBuilder uriBuilder = new URIBuilder(config.getBaseUrl().resolve(subPath.toString()));
            uriBuilder.setParameters(nameValuePairs);
            Optional.ofNullable(config.getUnblockKey()).ifPresent(key -> uriBuilder.setParameter(UNBLOCK_KEY, key));
            URI uri = uriBuilder.build();
            log.debug("buildUri: {}", uri.toASCIIString());
            return uri;
        }
        catch (URISyntaxException e) {
            throw new IllegalStateException("Programming error? Constructed invalid URI internally", e);
        }
    }

    private <D> DataverseHttpResponse<D> wrap(DispatchResult result, Class<?>... dataClass) throws IOException {
        return new DataverseHttpResponse<>(result, mapper, dataClass);
    }

    private DispatchResult dispatch(org.apache.hc.core5.http.HttpRequest request) throws IOException, DataverseException {
        Optional.ofNullable(config.getApiToken()).ifPresent(token -> setApiTokenHeader(request, token));
        return httpClient5.execute((ClassicHttpRequest) request, response -> new DispatchResult(response, org.apache.hc.core5.http.io.entity.EntityUtils.toString(response.getEntity())));
    }

    private <T> void dispatch(org.apache.hc.core5.http.HttpRequest request, HttpClientResponseHandler<T> handler) throws IOException, DataverseException {
        Optional.ofNullable(config.getApiToken()).ifPresent(token -> setApiTokenHeader(request, token));
        httpClient5.execute((ClassicHttpRequest) request, handler);
    }

    private void setApiTokenHeader(org.apache.hc.core5.http.HttpRequest request, String apiToken) {
        if (sendApiTokenViaBasicAuth) {
            byte[] apiTokenBytes = (apiToken + ":").getBytes(StandardCharsets.UTF_8);
            request.setHeader(AUTHORIZATION, "Basic " + encodeBase64String(apiTokenBytes));
        }
        else
            request.setHeader(HEADER_X_DATAVERSE_KEY, apiToken);
    }
}
