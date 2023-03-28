package com.tool.calculator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CalculatorStringVO {
    private String inputString;

    public boolean validate(){
        return !(inputString.isBlank() || inputString.isEmpty());
    }

    public static void main(String[] args) {
        CalculatorStringVO calculatorStringVO = new CalculatorStringVO();
        calculatorStringVO.setInputString("");
        System.out.println(calculatorStringVO.validate());
    }


}
