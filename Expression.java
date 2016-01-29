
package stack;
/**
 *  Rixing Wu
 *  Lab 2
 * Expression Logic
 */
import java.util.Stack;
 
public class Expression
{
    
    public Expression(){ 
        System.out.println("Expression init alloc");
    }
    
    public int evaluate(String infixExpression)
    {  char[] array = infixExpression.toCharArray();
 
        Stack<Integer> operands =  new Stack<>();
        Stack<Character> operators = new Stack<>();
 
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] == ' ')
                continue;
 
            if (array[i] >= '0' && array[i] <= '9')
            {
                StringBuilder sMore = new StringBuilder();
                while (i < array.length && array[i] >= '0' && array[i] <= '9')
                    sMore.append(array[i++]);
                operands.push(Integer.parseInt(sMore.toString()));
            }
 
            else if (array[i] == '(')
                operators.push(array[i]);
 
            else if (array[i] == ')')
            {
                while (operators.peek() != '(')
                {
                  operands.push(operate(operators.pop(), operands.pop(), operands.pop()));
                }
                operators.pop();
            }
 
            else
            {
                while (!operators.empty() && hasGreaterPrecedence(array[i], operators.peek())){
                    operands.push(operate(operators.pop(), operands.pop(), operands.pop()));
                } 
                operators.push(array[i]);
            }
        }
 
    
        while (!operators.empty())
            operands.push(operate(operators.pop(), operands.pop(), operands.pop()));
 
      
        return operands.pop();
    }
    
   

    private int operate(char operator, int num1, int num2)
    {   
       //Handle devide by zero exception 
      if (operator == '/' && num1 == 0){ throw new IllegalArgumentException("Argument 'divisor' is 0"); }
       // Continue
      if (operator == '+')
            return num2 + num1;
      if (operator == '-')
            return num2 - num1;
      if (operator == '*')
            return num2 * num1;
      if (operator == '/')
            return num2 / num1;
      else
        return 0;
    }
 
    
    
    private boolean hasGreaterPrecedence (char operator1, char operator2)
    {   
        if ((operator1 == '(' || operator2 == ')') || ((operator1 == '*' || operator1 == '/') && (operator2 == '+' || operator2 == '-')))
                return false;
        else
              return true;
    }
 
}