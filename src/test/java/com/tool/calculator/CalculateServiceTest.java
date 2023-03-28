package com.tool.calculator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.script.ScriptException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculateServiceTest {

    @Autowired
    private CalculatorStringVO calculatorStringVO;

    @Autowired
    private CalculateService calculateService;

    @MockBean
    private RandomFormulaStringGenerator randomFormulaStringGeneratorMock;

    @Autowired
    private RandomFormulaStringGenerator randomFormulaStringGenerator;

    @BeforeEach
    void setUp() {
        System.out.println("Setting it up!");
        assertNotNull(calculatorStringVO);
        assertNotNull(calculateService);
    }

    @Test
    void calculateAdditionTwoNumber() throws ScriptException {
        calculatorStringVO.setInputString("3+2");
        assertEquals(calculateService.calculate(calculatorStringVO), new BigDecimal(5));
    }

    @Test
    void calculateSubtractionTwoNumber() throws ScriptException {
        calculatorStringVO.setInputString("3-2");
        assertEquals(calculateService.calculate(calculatorStringVO), new BigDecimal(1));
    }

    @Test
    void calculateMultiplicationTwoNumber() throws ScriptException {
        calculatorStringVO.setInputString("3*2");
        assertEquals(calculateService.calculate(calculatorStringVO), new BigDecimal(6));
    }

    @Test
    void calculateDivisionTwoNumber() throws ScriptException {
        calculatorStringVO.setInputString("3/2");
        assertEquals(calculateService.calculate(calculatorStringVO), new BigDecimal("1.5"));
    }

    @Test
    void calculateWithRandomFormulaStringMock() throws ScriptException {
        Mockito.when(randomFormulaStringGeneratorMock.getFormulaString()).thenReturn("1+2-3*4/5+6-7*8/9");
        String randomFormulaString = randomFormulaStringGeneratorMock.getFormulaString();
        calculatorStringVO.setInputString(randomFormulaString);
        System.out.println(calculateService.calculate(calculatorStringVO));
        assertEquals(calculateService.calculate(calculatorStringVO), new BigDecimal("0.3777777777777773"));
    }

    //TODO :random如何測試
    @Test
    void calculateWithRandomFormulaString() throws ScriptException {
        String xxx = "64.09+37.1+110.1/136.1*136.1-129.1*138.1-63.1/82.1*156.1+156.1+20.1";
//        String randomFormulaString = randomFormulaStringGenerator.getFormulaString();
//        System.out.println(calculateService.calculate(xxx));
        calculatorStringVO.setInputString(xxx);
        assertEquals(calculateService.calculate(calculatorStringVO), new BigDecimal("-17561.19454323995"));
    }

    @Test
    void calculateErrorResult() {
        calculatorStringVO.setInputString("Apple");
        Throwable exception = assertThrows(ScriptException.class, () -> calculateService.calculate(calculatorStringVO));
        assertEquals("ReferenceError: \"Apple\" is not defined in <eval> at line number 1", exception.getMessage());
    }

    @Test
    void calculateInfinity() throws ScriptException {
        calculatorStringVO.setInputString("20/0");
        Throwable exception = assertThrows(ScriptException.class, () -> calculateService.calculate(calculatorStringVO));
        assertEquals("無法計算，請檢查察輸入字串數字與運算子是否有輸入錯誤，或是簡短算式。", exception.getMessage());
    }

    @Test
    void calculateMinusInfinity() throws ScriptException {
        calculatorStringVO.setInputString("-20/0");
        Throwable exception = assertThrows(ScriptException.class, () -> calculateService.calculate(calculatorStringVO));
        assertEquals("無法計算，請檢查察輸入字串數字與運算子是否有輸入錯誤，或是簡短算式。", exception.getMessage());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Running: tearDown");
        calculatorStringVO = null;
        calculateService = null;
        assertNull(calculatorStringVO);
        assertNull(calculateService);
    }
}