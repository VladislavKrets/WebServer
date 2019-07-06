package com.server.mappers;

import com.server.requests.GetRequest;
import com.server.requests.PostRequest;

public class PageMapperBuilder {
    private PageMapper pageMapper;

    public PageMapperBuilder() {
        this.pageMapper = new PageMapper();
    }

    public PageMapperBuilder setPath(String path) {
        pageMapper.path = path;
        return this;
    }

    public PageMapperBuilder setPageName(String pageName) {
        pageMapper.pageName = pageName;
        return this;
    }

    public PageMapperBuilder setGetRequest(GetRequest getRequest) {
        pageMapper.getRequest = getRequest;
        return this;
    }

    public PageMapperBuilder setPostRequest(PostRequest postRequest) {
        pageMapper.postRequest = postRequest;
        return this;
    }

    public PageMapperBuilder setHtml(String html) {
        pageMapper.html = html;
        return this;
    }

    public PageMapper getPageMapper() {
        return pageMapper;
    }

    public class PageMapper{
        private String path;
        private String pageName;
        private GetRequest getRequest;
        private PostRequest postRequest;
        private String html;

        public GetRequest getGetRequest() {
            return getRequest;
        }

        public void setGetRequest(GetRequest getRequest) {
            this.getRequest = getRequest;
        }

        private PageMapper(){}

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPageName() {
            return pageName;
        }

        public void setPageName(String pageName) {
            this.pageName = pageName;
        }

        public PostRequest getPostRequest() {
            return postRequest;
        }

        public void setPostRequest(PostRequest postRequest) {
            this.postRequest = postRequest;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }
    }
}
