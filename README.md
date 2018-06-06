# 雪花算法分布式ID生成器
该项目的目的是提供一个轻量级、高并发、高可用的生成唯一ID的服务，生成的ID是一个64位的
长整型，全局唯一，保持递增，相对有序。基于twitter的雪花算法来生成ID,用于取代UUID那种无序、128位的字符串形式的ID，提供
一种更加高效、人性化的全局唯一ID的生成方式，目前单机CPU4核、内存8G压测的并发数可以达到
**250万/每秒**，即每秒最多可以生成250万个唯一的ID，当然如果部署集群的话，这个数据可以
更高。
<br><br>
具体的教程可以参考：[基于twitter雪花算法的分布式ID —— Java篇](./SnowFlake-Java.md)

<br><br>

## 特点
* 基于twitter的雪花算法生成ID;
* 基于netty框架提供通信层接入；
* 提供HTTP和SDK两种方式接入；
* 轻量级、高并发、易伸缩；
* 部署简单，支持分布式部署；
  <br>

## 接入
服务器支持两种方式接入：**HTTP**和**SDK**，无论哪一种方式接入，对于同一台服务器来说，调用的是同
一个ID生成器，所以得到的ID都是递增、有序的。
<br><br>

### HTTP接入
HTTP的接入方式很简单，直接访问IP+端口即可，或者域名+端口，端口号固定为**16830**。如果你不喜欢这种带有端口号的方式，可以考虑配置Nginx来做代理转发，配置Nginx对于部署分布式ID集群也有好处，可以通过Nginx来做负载均衡。
<br><br>

### SDK接入
SDK接入前需要在自己的项目中加入SDK的jar包，或者自己写一个SDK来接入，语言不限。**DistributedID-SDK**提供了同步和异步两种请求方式，如果有高并发的要求，建议使用异步请求的方式，相同的环境下异步请求的性能会比同步请求的性能更高。
<br><br>

## 部署
部署之前需要把项目源码打包成jar包，或者使用项目打包好的jar包，把jar包上传到服务器，执行如下命令：
<br>
·java -jar distributedid.jar 1 2·
执行上面命令指定了两个参数1和2，前面的1代表数据中心标识，后面的2代表的是机器或进程标识.
<br><br>

如果不指定这两个参数，那么会使用默认的值1。如果只考虑部署单机服务器，那么可以不考虑这两个参数，**如果需要分布式集群来生成ID时，需要指定数据中心标识ID和机器进程标识ID，并且每一个服务器的数据中心标识ID和机器进程标识ID作为联合键全局唯一，这样才能保证集群生成的ID都是唯一的。**

<br>

目前已经集成docker化构建镜像；

构建步骤为：

1、首先执行maven编译命令 mvn clean install

2、进入本项目的根目录，执行 docker build 

3、查看是否生成了镜像，docker images

<br>

以上构建镜像对于人工操作比较繁琐，建议使用genkins 方式；当然也可以使用插件：

<plugin>

```java
                    <groupId>com.spotify</groupId>
                    <artifactId>docker-maven-plugin</artifactId>
                    <version>0.4.13</version>
                    <executions>
                        <execution>
                            <id>tag-image-version</id>
                            <phase>package</phase>
                            <goals>
                                <goal>tag</goal>
                            </goals>
                            <configuration>
                                <image>flowable/${project.build.finalName}</image>
                                <newName>IP:2375/flowable/${project.build.finalName}:${project.version}</newName>
                                <serverId>docker-hub</serverId>
                                <pushImage>true</pushImage>
                            </configuration>
                        </execution>
                        <execution>
                            <id>tag-image-latest</id>
                            <phase>package</phase>
                            <goals>
                                <goal>tag</goal>
                            </goals>
                            <configuration>
                                <image>flowable/${project.build.finalName}</image>
                                		<newName>IP:2375/flowable/${project.build.finalName}:latest</newName>
                                <serverId>docker-hub</serverId>
                                <pushImage>true</pushImage>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
```
注：该插件需要开启docker仓库远程api端口

<br>
<br>



## 待改进

一、目前SdkServer使用自定义协议方式，存在 tcp粘包问题，目前个人有两个小的解决方案：

1、使用java对象作为接受，那这自定义协议就体现在这对象中，所以可以使用默认属性接受，对于缺少字节的tcp包就有默认的数据；

2、在进行入站ChanelHandler时进行数据解码，只解析定长数据，其中可以自己实现FixedLengthFrameDecoder；

对于这些方案目前个人尚未测试过，有兴趣的小伙伴自己pull code 进行优化下，谢谢！！！

<br>