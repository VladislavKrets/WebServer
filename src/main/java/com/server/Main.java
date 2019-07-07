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

        Server server = new Server();
        server.execute();
    }

}
