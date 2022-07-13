package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamExample {

    // 스트림 생성 - 컬렉션
    List<String> list = Arrays.asList("apple", "banana", "cranberry");
    Stream<String> stream1 = list.stream();

    // 스트림 생성 - 배열
    String[] array = new String[]{"a", "b", "c"};
    Stream<String> stream2 = Arrays.stream(array);
    Stream<String> stream3 = Arrays.stream(array, 1, 3);

    // 스트림 생성 - Generator
    Stream<String> stream4 = Stream.generate(() -> "Hello").limit(5);
    // Stream.generate(() -> "Hello") 은 Hello를 무한대로 생성하는 람다식
    // limit()으로 5개만 찍어내도록 제한

    // 스트림 생성 - Iterator
    Stream<Integer> stream5 = Stream.iterate(100, n -> n + 10).limit(5);
    // 수열 형태의 데이터 생성
    // 초기값 100부터 10씩 증가하는 숫자를 생성하는 스트림


    // 스트림 생성 - Empty 스트림
    Stream<String> stream6 = Stream.empty();


    // 스트림 생성 - 기본 타입
    // 1. 오토박싱을 수행하지 않도록 하는 방법
    // int 변수를 Integer 클래스로 오토박싱해서 처리하는 경우가 있는데, 이 경우 오버헤드가 발생해서 성능저하가 있을 수 있다.
    IntStream intStream = IntStream.range(1, 10); // 1 ~ 9
    LongStream longStream = LongStream.range(1, 10000); // 1 ~ 10000
    // 2. 박싱을 해줘야 하는 경우
    Stream<Integer> steam7 = IntStream.range(1, 10).boxed();


    // https://hbase.tistory.com/171 에서 나중에 나머지도 공부


    public String print() {

        String str = stream2.toString();

        return str;
    }


}
