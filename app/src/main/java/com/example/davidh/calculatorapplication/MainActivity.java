package com.example.davidh.calculatorapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends Activity {

    private TextView m_display;
    private Vibrator m_vibrator;
    private Boolean m_use_vibrator = false;

    List operands;

    SharedPreferences m_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operands = new ArrayList();

        m_display = (TextView)findViewById( R.id.display );
        m_vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        m_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        m_use_vibrator = m_sp.getBoolean("vibrate", false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        m_use_vibrator = m_sp.getBoolean("vibrate", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, CalcPreferenceActivity.class);
            startActivity( intent );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonPressed( View view ) {
        Button buttonView = (Button) view;
        String text = null;
        switch ( view.getId() ) {
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button0:
                m_display.append( buttonView.getText() );
                break;
            case R.id.buttonPlus:
                text = m_display.getText().toString();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

                if ( !(text.endsWith("+") || text.endsWith("-") || text.isEmpty())) {
                    m_display.append( buttonView.getText() );
                    operands.add('+');
                }
                break;
            case R.id.buttonMinus:
                text = m_display.getText().toString();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

                if ( !(text.endsWith("+") || text.endsWith("-") || text.isEmpty())) {
                    m_display.append( buttonView.getText() );
                    operands.add('-');
                }
                break;
            case R.id.buttonClear:
                m_display.setText("");
                break;
            case R.id.buttonBack:
                String result = back(m_display.getText().toString());
                m_display.setText(result);
                break;
            case R.id.buttonEquals:
                String res = evaluateExpression(m_display.getText().toString());
                m_display.setText(res);
                break;
        }
        if(m_use_vibrator) {
            m_vibrator.vibrate(300);
        }
    }

    String back( String expr )
    {
        String result = expr.substring(0, expr.length()-1);
        return result;
    }

    String evaluateExpression( String expr )
    {
        Integer result = 0;
        StringTokenizer st = new StringTokenizer( expr, "[+\\-]", true );
        String[] numbers = expr.split("[+\\-]");

        if(numbers.length > 0) {
            result = Integer.parseInt(numbers[0]);
        }
        for(int i = 1; i < numbers.length; i++) {
            result = operate(result, (char)operands.remove(0), Integer.parseInt(numbers[i]));
        }

        /*
        while ( st.hasMoreElements()) {
            String token = st.nextToken();
            if((token.equals("+"))) {
               result += Integer.parseInt(numbers[0]) + Integer.parseInt(numbers[1]);
               // numbers =
            }
        }*/
        return result.toString();
    }

    private int operate(int result, char operand, int number) {
        switch(operand){
            case '+':
                result += number;
                break;
            case '-':
                result -= number;
                break;
            default:
                break;
        }

        return result;
    }
}
