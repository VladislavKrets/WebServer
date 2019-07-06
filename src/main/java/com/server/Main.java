package com.server;

import com.server.mappers.PageMapperBuilder;
import com.server.mappers.PageMapperContainer;

import java.io.*;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        PageMapperContainer pageMapperContainer = PageMapperContainer.getInstance();
        pageMapperContainer.addPage(new PageMapperBuilder()
                .setPath("/")
                .setPageName("web/html/index.html")
                .setGetRequest((params) -> {
                    return new HashMap<>();
                })
                .setPostRequest((getParams, postParams) -> {
                    return new HashMap<>();
                })
                .getPageMapper());

        Server server = new Server();
        server.execute();
    }

}
