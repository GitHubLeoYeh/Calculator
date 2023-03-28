package com.tool.calculator;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@NoArgsConstructor
@Component
public class RandomFormulaStringGenerator {
    public String getFormulaString(){
        StringBuilder resultStr = new StringBuilder();
        int digit_count = (int)(Math.random()*20)+2;  //2~20個數字
        for(int i=0; i<digit_count; i++){
            Random random = new Random(System.currentTimeMillis() + (int)(Math.random()*20));
            double double_digit = random.nextDouble();
            int integer_digit = random.nextInt(200);
            DecimalFormat df = new DecimalFormat("#.##");
            String ran_digit = df.format(integer_digit + double_digit); // 產生數字 整數+兩位小數
            if(i!=0){ // 第一位前方不用產生亂數運算子
                ran_digit = getRandomFormula() + ran_digit;
            }
            resultStr.append(ran_digit);
        }
        return resultStr.toString();
    }

    private String getRandomFormula(){
        Map<Integer, String> formulaMap = new HashMap<>();
        formulaMap.put(0, "+");
        formulaMap.put(1, "-");
        formulaMap.put(2, "*");
        formulaMap.put(3, "/");
        int randomNum = (int)(Math.random()*4);  //0~3的亂數
//        System.out.println("randomNum:" + randomNum);
        return formulaMap.get(randomNum);
    }

    public static void main(String[] args) {
        RandomFormulaStringGenerator randomFormulaStringGenerator = new RandomFormulaStringGenerator();
        System.out.println(randomFormulaStringGenerator.getFormulaString());
//        for(int i=0; i<10; i++) {
//            System.out.println(randomFormulaStringGenerator.getRandomFormula());
//        }
    }
}
