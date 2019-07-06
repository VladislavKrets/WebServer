package com.server.mappers;

import java.util.HashMap;
import java.util.Map;

public class PageMapperContainer {
    private Map<String, PageMapperBuilder.PageMapper> pages;
    private static PageMapperContainer pageMapperContainer;

    private PageMapperContainer() {
        pages = new HashMap<>();

    }

    public static PageMapperContainer getInstance() {
        if (pageMapperContainer == null) pageMapperContainer = new PageMapperContainer();
        return pageMapperContainer;
    }

    public synchronized void addPage(PageMapperBuilder.PageMapper pageMapper){
        pages.put(pageMapper.getPath(), pageMapper);
    }

    public PageMapperBuilder.PageMapper getPage(String path){
        return pages.get(path) == null ? pages.get("404") : pages.get(path);
    }

    public void deletePage(String path){
        pages.remove(path);
    }
}
