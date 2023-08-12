import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Request {
    final char QUERY_DELIMITER = '?';
    private String method;
    private String path;
    private String version;
    private final Map<String, String> headers;
    private String body;

    public Request() {
        this.headers = new ConcurrentHashMap<>();
        this.method = "";
        this.path = "";
        this.version = "";
        this.body = "";
    }

    public void addHeader(String header) {
        String[] headerParts = header.split(":");
        if (headerParts.length == 2) {
            this.headers.put(headerParts[0], headerParts[1].replace(" ",""));
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\n").append(method).append(" ").append(path).append(" ").append(version).append("\n");
            for (Map.Entry<String, String> entry : headers.entrySet())
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            sb.append("\n");
            sb.append(body);
            return sb.toString();
        } catch (Exception e) {
            System.out.print("Ошибка: ");
            e.printStackTrace();
        }
        return "";
    }

    public Optional<String> extractHeader(String header) { //из списка строк мы вытаскиваем header, причем его значение
        return headers.entrySet()//преобразовываем его в стрим
                .stream()
                .filter(o -> o.getKey().equals(header))// оставляем только строки, которые начинаются с искомого header
                .map(Map.Entry::getValue)
                .findFirst();
    }

    public void setQueryParams() {
    }

    public List<NameValuePair> getBodyParams() {
        return URLEncodedUtils.parse(body, StandardCharsets.UTF_8);
    }

    public String getPathWithoutQueryParams() {
        int queryDelimiter = path.indexOf(QUERY_DELIMITER);
        if (queryDelimiter != -1) {
            path = path.substring(0, queryDelimiter);
            System.out.println(path);
        }
        return path;
    }

    public Optional<String> getHeaderValue(String header) {
        return headers.entrySet()
                .stream()
                .filter(o -> o.getKey().equals(header))
                .map(Map.Entry::getValue)
                .findFirst();
    }
}