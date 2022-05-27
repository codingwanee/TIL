package simple.sample;

import lombok.Data;


public class KeyboardDto {

    @Data
    public static class Keyboard {
        private String model;

        private String brand;

        private long price;
    }

}
