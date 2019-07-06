package com.server.requests;

import java.util.Map;

public interface GetRequest {
    Map<String, String> execute(Map<String, String> params);
}
