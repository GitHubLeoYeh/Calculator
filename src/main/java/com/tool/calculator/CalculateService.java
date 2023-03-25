package com.tool.calculator;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Pattern;

@NoArgsConstructor
@Component
public class CalculateService {
    public BigDecimal calculate(String calculateString) throws ScriptException, NumberFormatException {
        // 創建一個ScriptEngineManager對象
        ScriptEngineManager mgr = new ScriptEngineManager();
        // 使用JavaScript解析器
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        BigDecimal answer;
        // 計算表達式
        String result = Objects.toString(engine.eval(calculateString), "NaN");
        System.out.println(calculateString + " = " + result);
        System.out.println("result.getClass() = " + result.getClass());        if (result.equals("NaN") || result.equals("Infinity") || result.equals("-Infinity")){ // Infinity 算是Double類別
            throw new ScriptException("");
        }else{
            answer = new BigDecimal(result);
        }
        return answer;
    }

    public static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }


    public static void main(String[] args) throws ScriptException {
//        String toCalculateStr = "3+9+8*4/5.5";
        String toCalculateStr = "Gorden";
//        String toCalculateStr = "3+9+8*4/0";
        System.out.println(new CalculateService().calculate(toCalculateStr));
//        BigDecimal xxx;
//        System.out.println(xxx);
    }
}
