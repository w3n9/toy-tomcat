package online.stringtek.toy.framework.tomcat.core.component;

import lombok.Data;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;

@Data
public class MappedHost extends MappedElement<MappedContext>{
    private String appBase;
    public static MappedHost build(String appBase) throws Exception {
        MappedHost mappedHost=new MappedHost();
        mappedHost.setAppBase(appBase);
        mappedHost.setObjectMap(new HashMap<>());
        File appBaseDir=new File(appBase);
        if(appBaseDir.exists()&&appBaseDir.isDirectory()){
            //给根目录注册一个context
            mappedHost.getObjectMap().put("",MappedContext.build(appBase));
            File[] contextDirArr = appBaseDir.listFiles(File::isDirectory);
            if(contextDirArr!=null){
                //所有的目录都视为Context
                for (File contextDir : contextDirArr)
                    mappedHost.getObjectMap().put(contextDir.getName(),MappedContext.build(contextDir.getAbsolutePath()));
            }
        }else{
            //TODO 错误处理
        }
        return mappedHost;
    }
}
