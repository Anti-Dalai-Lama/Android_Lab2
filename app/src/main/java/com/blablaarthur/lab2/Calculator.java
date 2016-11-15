package com.blablaarthur.lab2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


/**
 * Created by Артур on 04.10.2016.
 */

public class Calculator extends AppCompatActivity {

    Button num1, num2,num3,num4,num5,num6,num7,num8,num9,num0;
    Button clear, delete;
    Button plus, minus, divide, mult;
    Button point, evaluate;
    TextView calcfield;
    Integer zerocount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        num0 = (Button) findViewById(R.id.button0);
        num1 = (Button) findViewById(R.id.button1);
        num2 = (Button) findViewById(R.id.button2);
        num3 = (Button) findViewById(R.id.button3);
        num4 = (Button) findViewById(R.id.button4);
        num5 = (Button) findViewById(R.id.button5);
        num6 = (Button) findViewById(R.id.button6);
        num7 = (Button) findViewById(R.id.button7);
        num8 = (Button) findViewById(R.id.button8);
        num9 = (Button) findViewById(R.id.button9);
        clear = (Button) findViewById(R.id.buttonc);
        delete = (Button) findViewById(R.id.buttonb);
        plus = (Button) findViewById(R.id.buttonplus);
        minus = (Button) findViewById(R.id.buttonminus);
        divide = (Button) findViewById(R.id.buttondiv);
        mult = (Button) findViewById(R.id.buttonmult);
        point = (Button) findViewById(R.id.buttonpoint);
        evaluate = (Button) findViewById(R.id.buttoneq);
        calcfield = (TextView) findViewById(R.id.calcView);

    }

    public void ClearSpace(View target){
        calcfield.setText("0");
    }

    public void DeleteCharacter(View target){
        String text = calcfield.getText().toString();
        if(text.length() > 1)
            calcfield.setText(text.subSequence(0,text.length()-1));
        else
            calcfield.setText("0");
    }

    public void AddNum(View target){
        String text = calcfield.getText().toString();
        if(((Button)target).getText().toString().equals("0") && !text.equals("0"))
            if((text.charAt(text.length() - 1) == '0' && !Character.isDigit(text.charAt(text.length() - 2)) && text.charAt(text.length() - 2) != '.')){}

            else
                text  += "0";
        else {
            if (!calcfield.getText().toString().equals("0"))
                text += ((Button) target).getText().toString();
            else
                text = ((Button) target).getText().toString();
        }
        calcfield.setText(text);
    }

    public void AddSign(View target){
        String text = calcfield.getText().toString();
        if(Character.isDigit(text.charAt(text.length() - 1))){
            String ch = ((Button) target).getText().toString();
            if(ch.equals("-") && text.equals("0"))
                text = "-";
            else if(ch.equals("+") && text.equals("0")){

            }
            else{
                text += ((Button) target).getText().toString();
            }
            calcfield.setText(text);
        }
        zerocount = 0;
    }

    public void Evaluate(View target){
        double result;
        try {
            Expression expr = new ExpressionBuilder(calcfield.getText().toString()).build();
            result = expr.evaluate();
            calcfield.setText(Double.toString(result));
        }
        catch (Exception e){
        }
    }
}
