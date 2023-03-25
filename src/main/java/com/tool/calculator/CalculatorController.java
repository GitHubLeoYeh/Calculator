package com.tool.calculator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import javax.script.ScriptException;
import java.math.BigDecimal;

@Controller
public class CalculatorController {

    @Autowired
    private CalculateService calculateService;

    @MessageMapping("/tool")
    @SendTo("/topic/calculator")
    public CalculatorDTO greet(CalculatorStringVO stringVO) {
        String answer = "無法計算，請檢查察輸入字串數字與運算子是否有輸入錯誤，或是簡短算式。";
        try {
            System.out.println("------get message------");
            System.out.println(stringVO);
            BigDecimal result = calculateService.calculate(stringVO.getInputString());
            answer = result.toString();
            Thread.sleep(2000);
        } catch (NullPointerException | ScriptException | NumberFormatException e) {
            System.err.println(e.getMessage());
            System.out.println(answer);
        } catch (Exception e) {
            answer = "系統錯誤...";
            System.err.println(e.getMessage());
        }
        return new CalculatorDTO(HtmlUtils.htmlEscape(answer));
    }
}