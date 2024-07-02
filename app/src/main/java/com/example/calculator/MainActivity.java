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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                binding.clearButton,
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
        for(int i = 0; i < 9; i++){
            operatorButtons[i].setBackground(operatorButtonDrawable);
        }

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
        recreate();
    }
    void setTint(Drawable drawable, int id){
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, id));
    }
}
