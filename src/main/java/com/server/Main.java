package com.server;

import com.server.mappers.PageMapperBuilder;
import com.server.mappers.PageMapperContainer;


public class Main {
    public static void main(String[] args){
        PageMapperContainer pageMapperContainer = PageMapperContainer.getInstance();
        pageMapperContainer.addPage(new PageMapperBuilder()
                .setPath("/")
                .setPageName("web/html/index.html")
                .getPageMapper());
        pageMapperContainer.addPage(new PageMapperBuilder()
                .setPath("/server")
                .setPageName("web/html/server.html")
                .getPageMapper());
        pageMapperContainer.addPage(new PageMapperBuilder()
                .setPath("/executors")
                .setPageName("web/html/executors.html")
                .getPageMapper());
        pageMapperContainer.addPage(new PageMapperBuilder()
                .setPath("/mappers")
                .setPageName("web/html/mappers.html")
                .getPageMapper());
        pageMapperContainer.addPage(new PageMapperBuilder()
                .setPath("/requests")
                .setPageName("web/html/requests.html")
                .getPageMapper());

        Server server = new Server();
        server.execute();
    }

}
