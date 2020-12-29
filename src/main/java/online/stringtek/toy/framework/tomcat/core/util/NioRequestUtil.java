package online.stringtek.toy.framework.tomcat.core.util;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import lombok.extern.slf4j.Slf4j;
import online.stringtek.toy.framework.tomcat.core.http.NioRequest;
import online.stringtek.toy.framework.tomcat.core.http.eums.Header;
import online.stringtek.toy.framework.tomcat.core.http.eums.Method;
import online.stringtek.toy.framework.tomcat.core.http.eums.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class NioRequestUtil {
    //当前仅支持GET|POST
    private static final String firstLinePattern="^(GET|POST|PUT|PATCH|DELETE|COPY|HEAD|OPTIONS|LINK|UNLINK|PURGE|LOCK|UNLOCK|PROPFIND|VIEW) (.*) (.*)$";
    public static NioRequest build(byte[] bytes) throws IOException {
        log.info("building request...");
        try{
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new ByteInputStream(bytes,bytes.length), StandardCharsets.UTF_8));
            String line = reader.readLine();
            Pattern pattern=Pattern.compile(firstLinePattern);
            Matcher matcher = pattern.matcher(line);
            NioRequest request=new NioRequest();
            if(matcher.find()){
                request.setMethod(Method.parse(matcher.group(1)));
                request.setPath(matcher.group(2));
                request.setProtocol(Protocol.parse(matcher.group(3)));
                while(!StringUtils.isEmpty(line=reader.readLine())){
//                System.out.println(line);
                    String[] field = line.split(":");
                    request.getHeaders().put(Header.parse(StringUtils.trim(field[0])),StringUtils.trim(field[1]));
                }
            }else{
                //TODO
            }
            return request;
        }catch (Exception e){
            return null;
        }

    }
}
