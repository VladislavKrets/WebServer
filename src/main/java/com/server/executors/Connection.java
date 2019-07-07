package com.server.executors;

import com.server.logger.Logger;
import com.server.mappers.PageMapperBuilder;
import com.server.mappers.PageMapperContainer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Connection extends Thread{

    private Socket socket;
    private Logger logger;
    public Connection(Socket socket) {
        this.socket = socket;
        logger = new Logger();
    }

    @Override
    public void run() {

        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            Map<String, String> request = parseInputString(inputStream);
            Map<String, String> getParams = request.get("getParams") == null
                    ? new HashMap<>() : getUrlParameters(request.get("getParams"));
            Map<String, String> postParams = request.get("postParams") == null
                    ? new HashMap<>() : getUrlParameters(request.get("postParams"));
            logger.printConsoleLog(request.get("method") + " method on " + request.get("path"));
            String headers = request.get("http") + " 200 OK\r\n\r\n";
            outputStream.write(headers.getBytes());
            PageMapperBuilder.PageMapper pageMapper = PageMapperContainer.getInstance()
                    .getPage(request.get("path"));
            View view = new View(pageMapper, outputStream, request);
            if (request.get("method").equals("get"))
                view.getExecute(pageMapper.getGetRequest(), getParams);
            else if (request.get("method").equals("post"))
                view.postExecute(pageMapper.getPostRequest(), getParams, postParams);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Map<String, String> getUrlParameters(String parameters) {
        Map<String, String> params = new HashMap<>();
        String[] splitParameters = parameters.split("&");
        if (splitParameters.length == 1) return new HashMap<>();
        String[] splitVars;

        for (String parameter : splitParameters) {
            splitVars = parameter.split("=");
            if (splitVars.length == 1) {
                params.put(parameter, "");
            }
            else {
                params.put(splitVars[0], splitVars[1]);
            }
        }
        return params;
    }

    private Map<String, String> parseInputString(InputStream inputStream) throws IOException {
        Map<String, String> args = new HashMap<>();
        BufferedInputStream in = new BufferedInputStream(inputStream);
        String line = readLine(in);
        String[] split = line.split(" ");
        String[] params = split[1].split("\\?");
        if (split.length != 3) throw new IllegalArgumentException("wrong request: " + line);
        args.put("method", split[0].toLowerCase());
        if (params.length == 1) args.put("path", split[1]);
        else {
            args.put("path", params[0]);
            args.put("getParams", params[1]);
        }
        args.put("http", split[2]);
        Map<String, String> headers = new HashMap<>();
        do
        {
            line = readLine(in);
            if (line.trim().isEmpty()) break;
            headers.put(line.split(": ")[0], line.split(": ")[1]);
        }
        while (true);

        postHandler(args, in, headers);
        return args;
    }

    private void postHandler(Map<String, String> args,
                             BufferedInputStream in,
                             Map<String, String> headers) throws IOException {
        String line;
        if (args.get("method").equals("post")){
            if (headers.containsKey("Transfer-Encoding")
                    && !headers.get("Transfer-Encoding").equals("identity")){
                do
                {
                    line = readLine(in); // read chunk header
                    int size = line.length();
                    if (size == 0) break;
                    // use in.read() to read the specified
                    // number of bytes into message-body...
                    readLine(in); // skip trailing line break
                }
                while (true);

                // read trailing headers...
                line = readLine(in);
                while (!line.isEmpty())
                {
                    // store line in headers list, updating
                    // any existing header as needed...
                    System.out.println(line);
                }
            }
            else if (headers.containsKey("Content-Length"))
            {
                // use in.read() to read the specified
                // number of bytes into message-body...
                int length = Integer.parseInt(headers.get("Content-Length"));
                byte[] buffer = new byte[length];
                in.read(buffer);
                line = new String(buffer);
                args.put("postParams", line);
            }
            else if (headers.containsKey("Content-Type")
                    && headers.get("Content-Type").startsWith("multipart/"))
            {
                // use readLine(in) and in.read() as needed
                // to read/parse/decode MIME encoded data into
                // message-body until terminating MIME boundary
                // is reached...
                //todo
            }
            else
            {
                // fail the request...
                //todo
            }
        }
    }

    private String readLine(BufferedInputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        char c;
        while ((c = (char) in.read()) >= 0) {
            if (c == '\n') break;
            if (c == '\r') {
                c = (char) in.read();
                if ((c < 0) || (c == '\n')) break;
                sb.append('\r');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
