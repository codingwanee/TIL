package simple.excel.test;


import simple.*;

public class MainClass {

    public static void main(String[] args) throws Exception {

        ExcelFileTest test = new ExcelFileTest();
        ExcelTestTwo two = new ExcelTestTwo();
        ExcelTestThree three = new ExcelTestThree();
        ExcelTestFour four = new ExcelTestFour();
        ExcelTestFive five = new ExcelTestFive();

        five.readAndCopyToNewFile();

    }
}
