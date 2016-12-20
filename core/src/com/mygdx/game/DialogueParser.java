package com.mygdx.game;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DialogueParser {
    // Text
    private File dialogue;
    private String content;
    private String filename = "C:\\Users\\Dmitri\\Documents\\The actual Worst\\core\\assets\\dialogue.txt";

    public String readFile(String filename) {
        dialogue = new File(filename);
        FileReader reader = null;
        try {
            reader = new FileReader(dialogue);
            char[] chars = new char[(int) dialogue.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }

}
