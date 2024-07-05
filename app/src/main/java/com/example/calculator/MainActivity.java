package com.example.calculator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.example.calculator.databinding.ActivityMainBinding;

public class  MainActivity extends AppCompatActivity {
    Context context;
    boolean isBlue;

    Drawable numberButtonDrawable;
    Drawable operatorButtonDrawable;
    Drawable changeColorButtonDrawable;
    LayerDrawable fractionButtonDrawable;
    LayerDrawable deleteButtonDrawable;
    LayerDrawable exponentButtonDrawable;

    Button[] numberButtons;
    Button[] operatorButtons;

    private ActivityMainBinding binding;

    Expression expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        expression = new Expression();

        isBlue = true;
        numberButtonDrawable = ContextCompat.getDrawable(context, R.drawable.number_button_icon);
        operatorButtonDrawable = ContextCompat.getDrawable(context, R.drawable.operator_button_icon);
        changeColorButtonDrawable = ContextCompat.getDrawable(context, R.drawable.colour_changer_icon);
        fractionButtonDrawable =  (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.fraction_button_drawable);
        exponentButtonDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.exponent_button_drawable);
        deleteButtonDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.delete_button_drawable);

        numberButtons = new Button[] {
                binding.oneButton,
                binding.twoButton,
                binding.threeButton,
                binding.fourButton,
                binding.fiveButton,
                binding.sixButton,
                binding.sevenButton,
                binding.eightButton,
                binding.nineButton,
                binding.zeroButton,
                binding.piButton,
                binding.eButton
        };


        operatorButtons = new Button[] {
                binding.addButton,
                binding.subtractButton,
                binding.multiplyButton,
                binding.divideButton,
                binding.decimalButton,
                binding.openBracketButton,
                binding.closeBracketButton,
                binding.equalButton
        };


        bindButtons();
    }
    public void bindButtons(){
        binding.changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchColor();
            }
        });
        View.OnClickListener defaultButtonListener = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                expression.add((String) ((Button)view).getText());
                binding.text.setText(expression.render());
            }
        };
        for (Button button:numberButtons) {
            button.setOnClickListener(defaultButtonListener);
        }
        for (Button button:operatorButtons) {
            button.setOnClickListener(defaultButtonListener);
        }
        binding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression.clear();
                binding.text.setText(expression.render());
            }
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression.remove();
                binding.text.setText(expression.render());
            }
        });
        binding.exponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expression.add("^");
                binding.text.setText(expression.render());
            }
        });
        binding.equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setText(expression.Calculate());
                    expression.clear();
                } catch (Exception e) {
                    expression.clear();
                    binding.text.setText("SYNTAX ERROR");
                }
            }
        });
    }
    public void setText(String number){
        if (number.length() > 10)
            binding.text.setText(number.substring(0,10));
        binding.text.setText(number);
    }
    public void switchColor(){
        if (isBlue){
            makePink();
        }
        else {
            makeBlue();
        }
        isBlue = !isBlue;
        binding.getRoot().invalidate();
    }

    void makePink(){
        Log.d("ColorHandler", "in makePink");

        setTint(numberButtonDrawable,R.color.pinkNumber);
        for(int i = 0; i < 12; i++){
            numberButtons[i].setBackground(numberButtonDrawable);
        }

        setTint(operatorButtonDrawable, R.color.pinkOperator);
        for(int i = 0; i < 8; i++){
            operatorButtons[i].setBackground(operatorButtonDrawable);
        }
        binding.clearButton.setBackground(operatorButtonDrawable);

        setTint(changeColorButtonDrawable, R.color.pinkOperator);
        binding.changeColorButton.setBackground(changeColorButtonDrawable);

        setTint(fractionButtonDrawable.findDrawableByLayerId(R.id.bottomLayerFraction),R.color.pinkOperator);
        binding.fractionButton.setBackground(fractionButtonDrawable);

        setTint(exponentButtonDrawable.findDrawableByLayerId(R.id.bottomLayerExponent), R.color.pinkOperator);
        binding.exponentButton.setBackground(exponentButtonDrawable);

        setTint(deleteButtonDrawable.findDrawableByLayerId(R.id.bottomLayerDelete), R.color.pinkOperator);
        binding.deleteButton.setBackground(deleteButtonDrawable);

        binding.MenuBar.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkBack));
        binding.TextArea.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        binding.ButtonArea.setBackgroundColor(ContextCompat.getColor(context, R.color.pinkBack));
    }

    void makeBlue(){
        String text = (String) binding.text.getText();
        recreate();
        binding.text.setText(text);
    }
    void setTint(Drawable drawable, int id){
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, id));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the text from the TextView
        outState.putString("textViewText", binding.text.getText().toString());
        outState.putString("expression", expression.expression);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the text to the TextView
        if (savedInstanceState != null) {
            String textViewText = savedInstanceState.getString("textViewText");
            binding.text.setText(textViewText);
            expression.expression = savedInstanceState.getString("expression");
        }
    }
}
