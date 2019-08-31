---
title: CPU使用率过高怎么办
date: 2019-08-30 20:15:00
---

工作中遇到CPU使用率100%，不要慌，一起来找出原因并fix掉它。

<!-- more -->

记住这里大致流程，当线上突然遇到时，也不必手足无措。

## 总体流程

1. 找出CPU比较高的进程PID, `top`命令

2. 打印该进程下线程的CPU占用比较高的tid, `top -Hp {PID}`

3. 将该tid进行16进制转换id, `printf "%x\n" {tid}`

4. 打印线程的堆栈信息, `jstack {PID} |grep {id} -A 100`

当然这四步的执行需要时间，但我们可以将这几步写成shell脚本来执行。

服务器上安装的OpenJDK ,是否有常用的命令行工具？

## 安装JDK命令行工具

### 验证是否安装
```shell
[root@op-system ~]$ jstack -h
Usage:
    jstack [-l] <pid>
        (to connect to running process)
    jstack -F [-m] [-l] <pid>
        (to connect to a hung process)
    jstack [-m] [-l] <executable> <core>
        (to connect to a core file)
    jstack [-m] [-l] [server_id@]<remote server IP or hostname>
        (to connect to a remote debug server)

Options:
    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)
    -m  to print both java and native frames (mixed mode)
    -l  long listing. Prints additional information about locks
    -h or -help to print this help message

```

如果输出如上内容表明，已经拥有工具，倘若没有，那么继续下面。

### 安装

- 查看JDK版本

```shell
[root@op-system ~]$ java -version
openjdk version "1.8.0_201"
OpenJDK Runtime Environment (build 1.8.0_201-b09)
OpenJDK 64-Bit Server VM (build 25.201-b09, mixed mode)
```

- 看openJDK有jstack的yum源

```shell
[root@op-system ~]$ yum whatprovides '*/jstack'
1:java-1.8.0-openjdk-devel-debug-1.8.0.201.b09-2.el7_6.x86_64 : OpenJDK Development Environment 8 with full debug on
Repo        : @updates
Matched from:
Filename    : /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.201.b09-2.el7_6.x86_64-debug/bin/jstack
```

找到和JDK版本对应的yum源。

- 安装

```shell
[root@op-system ~]$ sudo yum install java-1.8.0-openjdk-devel-debug-1.8.0.201.b09-2.el7_6.x86_64
```

## 寻找问题所在

```shell
[root@op-system ~]$ top

top - 15:45:43 up 2 days,  1:56,  6 users,  load average: 3.91, 2.87, 2.19
Tasks: 167 total,   5 running, 161 sleeping,   1 stopped,   0 zombie
%Cpu(s): 96.2 us,  3.5 sy,  0.0 ni,  0.0 id,  0.0 wa,  0.0 hi,  0.3 si,  0.0 st
KiB Mem :  8155280 total,   155948 free,  5900832 used,  2098500 buff/cache
KiB Swap:        0 total,        0 free,        0 used.  1839192 avail Mem 

  PID USER     PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND                                                                               
  4494 root    20   0 1669724 879872   4244 S 181.4 10.8 194:40.37 java                                                                                
 49831 root    20   0 4818136 936428   7324 S   6.0 11.5   1:07.91 java                                                                                  
 51859 mysql   20   0 4858288 957160   7656 S   2.7 11.7   4:41.22 mysqld
```

- 查看进程中线程情况

```shell
[root@op-system ~]$ top -p 4494 -H
   PID USER      PR  NI    VIRT    RES    SHR S %CPU %MEM     TIME+ COMMAND                                                                                
 62984 root   20   0 4864612 845780   7852 S  50 10.4   0:05.11 java                                                                                   
 51971 root   20   0 4864612 845780   7852 S  0.3 10.4   0:04.31 java                                                                                   
 51973 root   20   0 4864612 845780   7852 S  0.3 10.4   0:16.17 java  
```
发现 62984 这个线程特别消耗CPU，那么我们来看看这个线程到底是做什么的。

- 十进制id转十六进制

```shell
[root@op-system ~]$ printf "%x\n" 62984
f608
```

- 使用jstack打印出线程的堆栈信息

```shell
[root@op-system ~]$ jstack 4494 |grep f608 -A 100

"http-nio-8000-exec-12" #82 daemon prio=5 os_prio=0 tid=0x00007f205487a800 nid=0xf608 waiting for monitor entry [0x00007f202b3b7000]
   java.lang.Thread.State: RUNNABLE (on object monitor)
	at java.io.PrintStream.println(PrintStream.java:805)
	- waiting to lock <0x000000008397f940> (a java.io.PrintStream)
	at com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.list(ServiceImpl.java:272)
	at com.DeviceAlarmServiceImpl.getSystem(DeviceAlarmServiceImpl.java:244)
	at com.DeviceAlarmServiceImpl.autoProcess(DeviceAlarmServiceImpl.java:213)
	at com.DeviceAlarmServiceImpl$$FastClassBySpringCGLIB$$a90af92f.invoke(<generated>)
```

从打印出来的信息中发现了很熟悉的代码，对，就是这里。

## 解决问题

那么既然找到了问题，我们就只能通过，数据库索引、SQL优化、算法优化、快速返回等方法来最这段代码优化了。