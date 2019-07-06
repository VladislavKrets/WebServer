package com.server.requests;

import java.util.Map;

public interface PostRequest {
    Map<String, String> execute(Map<String, String> getParams, Map<String, String> postParams);
}
