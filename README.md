# Handswork
Handswork是Java语言的架手架，封装常用的类和方法，后期将集成常用的web容器。

## usage
```
    <repositories>
        <repository>
            <id>me.duanyong</id>
            <url>https://raw.github.com/duanyong/handswork/snapshot</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
```

```
    <dependencies>
        <dependency>
            <groupId>me.duanyong</groupId>
            <artifactId>handswork</artifactId>
            <version>0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
```


## TODO
1. 通过java-doc生成文档；
2. 将jetty或者netty集成到此脚手架中；（集成jetty）
3. 将spring-mvc集成到脚手架中；
4. ...