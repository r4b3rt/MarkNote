package cn.iflyapi.demo.list.distinct;

import org.springframework.util.StopWatch;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qfwang
 * @date 2020-03-16 6:10 PM
 */
public class Test {

    /**
     * 随机生成长度为n的集合
     *
     * @return
     */
    private static List<Integer> randomList(int n) {
        Random random = new Random();
        ArrayList<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int i1 = random.nextInt(n);
            list.add(i1);
        }
        return list;
    }

    private static void forList(List<Integer> list) {
        List newList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (!newList.contains(list.get(i))) {
                newList.add(list.get(i));
            }
        }
    }

    private static void set(List<Integer> list) {
        Set set = new HashSet<Integer>(list);
        ArrayList<Integer> integers = new ArrayList<Integer>(set);
    }

    private static void stream(List<Integer> list) {
        List<Integer> collect = list.parallelStream().distinct().collect(Collectors.toList());
    }

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        List<Integer> integers = randomList(20000);
        stopWatch.start("stream");
        stream(integers);
        stopWatch.stop();
        stopWatch.start("for list");
        forList(integers);
        stopWatch.stop();
        stopWatch.start("set");
        set(integers);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
