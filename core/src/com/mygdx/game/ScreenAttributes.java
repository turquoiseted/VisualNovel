package com.mygdx.game;

import java.util.ArrayList;

public class ScreenAttributes {
    ArrayList<CharSequence> MainBodyText;
    ArrayList<CharSequence> SpeakingCharacter;
    ArrayList<String> CharacterPortrait;

    public void ScreenAttributes(String filename) {

    }

    public CharSequence GetLine(int line) {
        return MainBodyText.get(line);
    }
}
