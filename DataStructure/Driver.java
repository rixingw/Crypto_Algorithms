/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stack;

/**
 *
 * @author Rixing
 */
public class Driver {
    
    public static void main(String[] args){
        Expression express = new Expression();
        int result = express.evaluate("10 + 2 * 6 * 3 / 2 + 2 + 2 / 3 + 5");
        System.out.print(result);
    }
}
