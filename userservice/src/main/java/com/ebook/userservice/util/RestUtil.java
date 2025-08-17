package com.ebook.userservice.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for handling HTTP requests and responses in a Spring Boot application.
 * Provides convenient methods for accessing request information and creating standardized error responses.
 *
 * @author furkanasik
 */
public class RestUtil {
    private static final String X_REAL_IP_HEADER = "X-Real-IP";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String USER_AGENT = "User-Agent";
    private static final String ERROR_URI_PREFIX = "http://mumbled.co/errors/";

    /**
     * Private constructor to prevent instantiation.
     */
    private RestUtil() {
        // Utility class, no instantiation
    }

    /**
     * Gets the current servlet request attributes.
     *
     * @return ServletRequestAttributes for the current request context
     */
    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * Gets the current HTTP servlet request.
     *
     * @return HttpServletRequest for the current request
     * @throws IllegalStateException if request context is not available
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes reqAttrs = getRequestAttributes();
        if (reqAttrs == null) {
            throw new IllegalStateException("Request context is not available");
        }
        return reqAttrs.getRequest();
    }

    /**
     * Gets the current HTTP servlet response.
     *
     * @return HttpServletResponse for the current request
     * @throws IllegalStateException if request context is not available
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes reqAttrs = getRequestAttributes();
        if (reqAttrs == null) {
            throw new IllegalStateException("Request context is not available");
        }
        return reqAttrs.getResponse();
    }

    /**
     * Gets the client IP address by checking request headers and remote address.
     * First tries X-Real-IP header, then X-Forwarded-For, and finally falls back to remote address.
     *
     * @return The client's IP address as a string
     */
    public static String getIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader(X_REAL_IP_HEADER);
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader(X_FORWARDED_FOR);
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Gets the User-Agent header from the current request.
     *
     * @return The User-Agent string or null if not present
     */
    public static String getUserAgent() {
        return getRequest().getHeader(USER_AGENT);
    }

    /**
     * Gets the complete request URL including query parameters.
     *
     * @return The full request URL as a string
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getRequest();
        String queryString = request.getQueryString();
        String requestUri = request.getRequestURI();

        return queryString == null ? requestUri : requestUri + "?" + queryString;
    }

    /**
     * Constructs the base URL of the application including scheme, server name, port (if non-standard),
     * and context path.
     *
     * @return The base URL of the application as a string
     */
    public static String getBaseUrl() {
        HttpServletRequest request = getRequest();
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath);
        return url.toString();
    }

    /**
     * Gets the value of a specified HTTP header from the current request.
     *
     * @param headerName The name of the header to retrieve
     * @return The header value as a string, or null if the header is not present
     */
    public static String getHeader(String headerName) {
        return getRequest().getHeader(headerName);
    }

    /**
     * Creates a standardized problem detail response with custom data.
     *
     * @param status HTTP status code for the response
     * @param code   Application-specific error code
     * @param detail Error description message
     * @param data   Additional data to include in the response
     * @return Formatted ProblemDetail object ready to be returned to the client
     */
    public static ProblemDetail createProblemDetail(HttpStatusCode status, String code, String detail, Object data) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        HttpServletRequest request = getRequest();

        problemDetail.setTitle(code);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setType(URI.create(ERROR_URI_PREFIX + code));

        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("data", data);
        problemDetail.setProperty("error", code);
        problemDetail.setProperty("message", detail);
        problemDetail.setProperty("path", request.getRequestURI());
        problemDetail.setProperty("ip", getIp());

        return problemDetail;
    }

    /**
     * Creates a standardized problem detail response without additional data.
     *
     * @param status HTTP status code for the response
     * @param code   Application-specific error code
     * @param detail Error description message
     * @return Formatted ProblemDetail object ready to be returned to the client
     */
    public static ProblemDetail createProblemDetail(HttpStatusCode status, String code, String detail) {
        return createProblemDetail(status, code, detail, null);
    }

    /**
     * Determines if the current request is an AJAX request by checking the X-Requested-With header.
     *
     * @return true if the request is an AJAX request, false otherwise
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * Gets all HTTP headers from the current request as a Map.
     *
     * @return Map containing all HTTP header names and their values
     */
    public static Map<String, String> getRequestHeaders() {
        HttpServletRequest request = getRequest();
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        return headers;
    }
}