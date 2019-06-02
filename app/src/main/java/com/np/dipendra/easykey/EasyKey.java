package com.np.dipendra.easykey;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.Objects;

public class EasyKey extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private Keyboard numeric_keyboard;
    private Keyboard symbol_keyboard;

    private  Keyboard devanagiri_keyboard;
    private  Keyboard getDevanagiri_shifted_keyboard;
    private boolean isCaps = false;

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_design, null);
        keyboard = new Keyboard(this, R.xml.keyboard);
        numeric_keyboard=new Keyboard(this,R.xml.numeric);
        symbol_keyboard=new Keyboard(this, R.xml.symbols);

        devanagiri_keyboard=new Keyboard(this,R.xml.devanagiri);
        getDevanagiri_shifted_keyboard=new Keyboard(this, R.xml.devanagiri_shifted);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);

        return keyboardView;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = inputConnection.getSelectedText(0);

                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
                break;

            case Keyboard.KEYCODE_SHIFT:
                Keyboard current_keyboard= keyboardView.getKeyboard();
                if(current_keyboard==numeric_keyboard){
                    keyboardView.setKeyboard(symbol_keyboard);
                }else if(current_keyboard==symbol_keyboard){
                    keyboardView.setKeyboard(numeric_keyboard);
                }else {
                    isCaps = !isCaps;
                    keyboard.setShifted(isCaps);
                    keyboardView.invalidateAllKeys();
                }
                break;
            case Keyboard.KEYCODE_DONE:
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                keyboardView.setKeyboard(keyboard);
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
               Keyboard current=keyboardView.getKeyboard();
               if(current==numeric_keyboard|| current==symbol_keyboard){
keyboardView.setKeyboard(keyboard);
               }
               else {
                   keyboardView.setKeyboard(numeric_keyboard);
               }
               break;
            case Keyboard.KEYCODE_ALT:
                Keyboard currentView=keyboardView.getKeyboard();
                if(currentView==numeric_keyboard|| currentView==symbol_keyboard||currentView==keyboard) {
                    keyboardView.setKeyboard(devanagiri_keyboard);
                }
                else {
                    keyboardView.setKeyboard(keyboard);
                }
                break;


            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && isCaps)
                    code = Character.toUpperCase(code);
                inputConnection.commitText(String.valueOf(code), 1);

        }

    }


    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {


    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }


    private void playClick(int primaryCode) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (primaryCode) {
            case 32:
                if (audioManager != null) {
                    audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                }
                break;
            case 10:
                Objects.requireNonNull(audioManager).playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                if (audioManager != null) {
                    audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                }
                break;
            default:
                if (audioManager != null) {
                    audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
                }
                break;
        }
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
    }
}
