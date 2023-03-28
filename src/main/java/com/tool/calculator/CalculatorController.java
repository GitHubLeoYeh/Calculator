package com.tool.calculator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import java.math.BigDecimal;
import java.util.Arrays;

@Controller
public class CalculatorController {

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private RandomFormulaStringGenerator randomFormulaStringGenerator;

    @MessageMapping("/tool")
    @SendTo("/topic/calculator")
    public CalculatorDTO send(CalculatorStringVO stringVO) {
        String answer = "無法計算，請檢查察輸入字串數字與運算子是否有輸入錯誤，或是簡短算式。";;
        try {
            System.out.println("------get message------");
            System.out.println(stringVO);
            BigDecimal result = calculateService.calculate(stringVO);
            answer = result.toString();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println(Arrays.toString(e.getStackTrace()));
            System.err.println(e.getMessage());
        }
        return new CalculatorDTO(HtmlUtils.htmlEscape(answer));
    }

    @MessageMapping("/generate_formula")
    @SendTo("/topic/random_formula")
    public String generateFormula() {
        String randomFormulaStr = "1+1";
        try {
            randomFormulaStr = randomFormulaStringGenerator.getFormulaString();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return HtmlUtils.htmlEscape(randomFormulaStr);
    }
}