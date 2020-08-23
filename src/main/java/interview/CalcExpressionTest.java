package interview;

import java.util.Random;
import java.util.Stack;

public class CalcExpressionTest {

    static char[] OPERATORS = new char[]{'+', '-', '*', '/'};

    /**
     * 产生表达式
     * @param num 操作数值个数
     * @return
     */
    public static String genExpression(int num){
        int[] numbers=new int[num];
        for(int i=0; i < num; i++){
            numbers[i] = new Random().nextInt(1000);
        }

        char[] operators=new char[num-1];
        for(int i=0; i < num-1; i++){
            operators[i] = OPERATORS[new Random().nextInt(4)];
        }

        StringBuffer expression = getExpression(numbers, operators);

        expression.append(" = ").append(evaluateExpression(numbers, operators));
        return expression.toString();
    }

    /**
     * 生成运算表达式字符串
     * @param numbers 数值数组
     * @param operators 操作符号数组
     * @return
     */
    private static StringBuffer getExpression(int[] numbers, char[] operators) {
        StringBuffer expression = new StringBuffer();
        int j = 0;
        for(int i=0; i < operators.length; i++){
            while(operators[i] == '('){
                expression.append(operators[i++]);
            }

            expression.append(numbers[j++]);

            while(i < operators.length && operators[i] == ')'){
                expression.append(operators[i++]);
            }

            if(i < operators.length){
                expression.append(" ").append(operators[i]).append(" ");
            }
        }

        if(j < numbers.length){
            expression.append(numbers[j]);
        }
        return expression;
    }

    /**
     * 计算表达式的结果
     * @param numbers 数值数组
     * @param operators 操作符数组
     * @return
     */
    private static Double evaluateExpression(int[] numbers, char[] operators) {
        Stack<Character> operatorStack = new Stack<>();
        Stack<Double> numberStack = new Stack<>();

        int j = 0;
        numberStack.push((double)numbers[j++]);
        for(int i=0; i < operators.length; i++){
            char currentOperator = operators[i];
            switch (currentOperator){
                case '(':
                    operatorStack.push(currentOperator);
                    break;

                case ')':
                    //在左边(之后的运算都处理掉
                    char lastOperator = operatorStack.pop();
                    while(lastOperator != '('){
                        evaluatePreNumber(lastOperator, numberStack);
                        lastOperator = operatorStack.pop();
                    }
                    break;

                case '*':
                case '/':
                    if(( operatorStack.isEmpty() || operatorStack.peek().equals('+') || operatorStack.peek().equals('-') )
                            && (i < operators.length-1 && operators[i+1]!='(') ){
                        numberStack.push(op(numberStack.pop(), numbers[j++], operators[i]));
                        break;
                    }

                case '+':
                case '-':
                    if(!operatorStack.isEmpty() && operatorStack.peek() != '(' ){
                        evaluatePreNumber(operatorStack.pop(), numberStack);

                        numberStack.push((double)numbers[j++]);
                        operatorStack.push(currentOperator);
                        break;
                    }

                default:
                     numberStack.push((double)numbers[j++]);
                     operatorStack.push(currentOperator);
            }
        }

        while (!operatorStack.isEmpty()){
            evaluatePreNumber(operatorStack.pop(), numberStack);
        }
        return numberStack.pop();
    }

    /**
     * 计算前一个表达式的运算结果
     * @param operator 操作符号
     * @param numberStack 数值栈
     */
    private static void evaluatePreNumber(char operator, Stack<Double> numberStack) {
        double b = numberStack.pop();
        numberStack.push(op(numberStack.pop(), b, operator));
    }

    /**
     * a operate b
     * @param a
     * @param b
     * @param operator 操作符
     * @return
     */
    public static double op(double a, double b, char operator){
        if(operator == '+'){
            return a + b;
        }
        if(operator == '-'){
            return a - b;
        }
        if(operator == '*'){
            return a * b;
        }
        if(operator == '/'){
            return a / b;
        }
        return 0d;
    }

    public static void main(String[] args) {
        int[] numbers = new int[]{11,45, 32, 17, 56};
        char[] operators = new char[]{'(', '+', '(', '+', ')', ')', '*', '(', '-', ')'};
        System.out.println(getExpression(numbers, operators));
        System.out.println(evaluateExpression(numbers, operators));
//        System.out.println(genExpression(4));
    }
}
