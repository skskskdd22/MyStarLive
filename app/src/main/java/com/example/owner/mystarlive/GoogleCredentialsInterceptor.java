package com.example.owner.mystarlive;

import com.google.auth.Credentials;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.StatusException;

/**
 * google cloud에 사용자 인증요청하는 클래스
 */

public class GoogleCredentialsInterceptor implements ClientInterceptor {

    private final Credentials mCredentials;

    private Metadata mCached;

    private Map<String, List<String>> mLastMetadata;

    GoogleCredentialsInterceptor(Credentials credentials) {
        mCredentials = credentials;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(final MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, final Channel next) {
        return new ClientInterceptors.CheckedForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {
            @Override
            protected void checkedStart(Listener<RespT> responseListener, Metadata headers)
                    throws StatusException {
                Metadata cachedSaved;
                URI uri = serviceUri(next, method);
                synchronized (this) {
                    Map<String, List<String>> latestMetadata = getRequestMetadata(uri);
                    if (mLastMetadata == null || mLastMetadata != latestMetadata) {
                        mLastMetadata = latestMetadata;
                        mCached = toHeaders(mLastMetadata);
                    }
                    cachedSaved = mCached;
                }
                headers.merge(cachedSaved);
                delegate().start(responseListener, headers);
            }
        };
    }

    /**
     * JWT 특정 서비스 URI를 생성
     * URI는 단순히 서비스가 JWT가 의도 한 것인지를 알 수있는 정보가 있는 식별자입니다.
     *
     * URI는 일반적으로 간단한 문자열 일치 검사로 확인
     */
    private URI serviceUri(Channel channel, MethodDescriptor<?, ?> method)
            throws StatusException {
        String authority = channel.authority();
        if (authority == null) {
            throw Status.UNAUTHENTICATED
                    .withDescription("권한이 없습니다.")
                    .asException();
        }
        // Always use HTTPS, by definition.
        final String scheme = "https";
        final int defaultPort = 443;
        String path = "/" + MethodDescriptor.extractFullServiceName(method.getFullMethodName());
        URI uri;
        try {
            uri = new URI(scheme, authority, path, null, null);
        } catch (URISyntaxException e) {
            throw Status.UNAUTHENTICATED
                    .withDescription("인증을위한 서비스 URI를 생성 할 수 없습니다.")
                    .withCause(e).asException();
        }
        // The default port must not be present. Alternative ports should be present.
        if (uri.getPort() == defaultPort) {
            uri = removePort(uri);
        }
        return uri;
    }

    private URI removePort(URI uri) throws StatusException {
        try {
            return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), -1 /* port */,
                    uri.getPath(), uri.getQuery(), uri.getFragment());
        } catch (URISyntaxException e) {
            throw Status.UNAUTHENTICATED
                    .withDescription("포트를 제거한 후 서비스 URI를 생성 할 수 없습니다.")
                    .withCause(e).asException();
        }
    }

    private Map<String, List<String>> getRequestMetadata(URI uri) throws StatusException {
        try {
            return mCredentials.getRequestMetadata(uri);
        } catch (IOException e) {
            throw Status.UNAUTHENTICATED.withCause(e).asException();
        }
    }

    private static Metadata toHeaders(Map<String, List<String>> metadata) {
        Metadata headers = new Metadata();
        if (metadata != null) {
            for (String key : metadata.keySet()) {
                Metadata.Key<String> headerKey = Metadata.Key.of(
                        key, Metadata.ASCII_STRING_MARSHALLER);
                for (String value : metadata.get(key)) {
                    headers.put(headerKey, value);
                }
            }
        }
        return headers;
    }

}
