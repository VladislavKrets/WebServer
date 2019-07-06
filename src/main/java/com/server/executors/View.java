package com.server.executors;

import com.server.mappers.PageMapperBuilder;
import com.server.mappers.PageMapperContainer;
import com.server.requests.GetRequest;
import com.server.requests.PostRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class View {
    private PageMapperBuilder.PageMapper pageMapper;
    private OutputStream outputStream;
    private Map<String, String> request;

    public View(PageMapperBuilder.PageMapper pageMapper, OutputStream outputStream, Map<String, String> request) {
        this.pageMapper = pageMapper;
        this.outputStream = outputStream;
        this.request = request;
    }


    public void getExecute(GetRequest getRequest, Map<String, String> getParams){
        String fileText = null;
        try {
            PageMapperContainer pageMapperContainer = PageMapperContainer.getInstance();
            if (pageMapper.getHtml() != null) {
                outputStream.write(pageMapper.getHtml().getBytes());
            }
            else if (isFiletypesCompatible()) {
                fileText = HTMLFile(pageMapper
                        .getPageName());

                Map<String, String> variables = getRequest.execute(getParams);
                fileText = replaceVariables(fileText, variables);
                //mapCss(fileText, pageMapperContainer);
                mapSrc(fileText, pageMapperContainer);

                outputStream.write(fileText.getBytes());
            }
            else {
                binaryFile();
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void postExecute(PostRequest postRequest, Map<String, String> getParams, Map<String, String> postParams){
        String fileText = null;
        try {
            PageMapperContainer pageMapperContainer = PageMapperContainer.getInstance();
            if (isFiletypesCompatible()) {
                fileText = HTMLFile(pageMapper
                        .getPageName());

                Map<String, String> variables = postRequest.execute(getParams, postParams);
                fileText = replaceVariables(fileText, variables);
                //mapCss(fileText, pageMapperContainer);
                mapSrc(fileText, pageMapperContainer);

                outputStream.write(fileText.getBytes());
            }
            else {
                binaryFile();
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFiletypesCompatible() {
        return pageMapper.getPageName().endsWith(".html")
                || pageMapper.getPageName().endsWith(".css")
                || pageMapper.getPageName().endsWith(".js");
    }

    private String replaceVariables(String fileText, Map<String, String> variables) {
        for (Map.Entry<String, String> entity : variables.entrySet()) {
            fileText = fileText.replaceAll("\\$\\{" + entity.getKey() + "\\}", entity.getValue());
        }
        return fileText;
    }

    private void binaryFile() throws IOException {
        FileInputStream inputStream = new FileInputStream(pageMapper.getPageName());
        byte[] buffer = new byte[1024];
        int len = inputStream.read(buffer);
        while (len != -1) {
            outputStream.write(buffer, 0, len);
            len = inputStream.read(buffer);
        }
        inputStream.close();
    }

    private void mapSrc(String fileText, PageMapperContainer pageMapperContainer) {
        Elements elements = Jsoup.parse(fileText).select("[${src}]");
        StringBuilder path;
        String name;
        List<String> paths;
        String[] nameSplit;
        int dotIndex;
        for (Element element : elements) {
            path = new StringBuilder(element.attr("${src}"));
            name = path.toString();
            nameSplit = name.split("\\.\\./");
            if (pageMapper.getPageName().contains("/")) {
                path.insert(0, pageMapper
                        .getPageName()
                        .substring(0, pageMapper.getPageName()
                                .lastIndexOf("/") + 1));
                paths = new ArrayList<>(Arrays.asList(path.toString().split("/")));
                while (paths.contains("..")) {
                    dotIndex = paths.indexOf("..");
                    if (dotIndex == 0) break;
                    paths.remove(dotIndex - 1);
                    paths.remove(dotIndex - 1);
                }
                path = new StringBuilder();
                for (String s : paths) path.append(s).append("/");
                path = new StringBuilder(path.substring(0, path.length() - 1));
            }
            pageMapperContainer.addPage(new PageMapperBuilder()
                    .setPath("/" + (nameSplit.length == 1 ? name : nameSplit[1]))
                    .setPageName(path.toString())
                    .setGetRequest(params1 -> {return new HashMap<>();
                    }).getPageMapper());
        }
    }
    private void mapCss(String fileText, PageMapperContainer pageMapperContainer) {
        String path = Jsoup.parse(fileText).head().select("[type=text/css]").attr("href");
        if (pageMapper.getPageName().contains("/"))
            path = pageMapper
                    .getPageName()
                    .substring(0, pageMapper.getPath()
                            .lastIndexOf("/") + 1) + path;

        pageMapperContainer.addPage(new PageMapperBuilder()
                    .setPath("/" + path)
                    .setPageName(path)
                    .setGetRequest(params1 -> {return new HashMap<>();
                    }).getPageMapper());
    }

    private String HTMLFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder fileText = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            fileText.append(line).append("\n");
        }
        reader.close();
        return fileText.toString();
    }
}
