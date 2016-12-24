package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BooleanArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
	// GDX Variables
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private BitmapFont font;

	// Music
	private Music rainMusic;
	// Images
	private Texture textboxImage;
	private Texture dateboxImage;
	private Texture leoStandImage;
	private Texture SavLoadImage;
	private Texture menuImage;

	// Temp
	private Rectangle bucket;
	private Rectangle menu;

	// Booleans
	private Boolean MouseRight;
	private Boolean MouseLeft;


	private Float TimeScroller;

	// Variables for text management
	private String DisplayText;
	private CharSequence text;
	private ArrayList<CharSequence> LinesOfText;
	private int DialogueLine;

	// Checks for displays
	boolean DisplaySave = false;
	boolean DisplayLoad = false;
	boolean DisplayStats = false;

	// Bound for menu positions
	float SaveTestX = 0;
	float SaveTestY = 0;
	// Refactor to a rectangle lmao
	int SaveUpperX = 1024;
	int SaveLowerX = 786;
	int SaveUpperY = 640;
	int SaveLowerY = 500;

	int LoadUpperX = 1024;
	int LoadLowerX = 786;
	int LoadUpperY = 500;
	int LoadLowerY = 400;


	// Class that deals with input script
	public class DialogueParser {
		// Text
		private CharSequence content;
		private ArrayList<CharSequence> lines;
		private CharSequence TempLines;
		private File dialogue;
		private String test;

		// Reads in from file
		public CharSequence readFile(String filename) {
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
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return content;
		}

		// Split my string into pieces, this is my last resort
		public ArrayList<CharSequence> parseFile(CharSequence script) {
			TempLines = "";
			lines = new ArrayList<>();
			for (int counter = 0; counter < script.length(); counter++) {
				// Makes a string of the current 2 characters
				test = "" + script.charAt(counter) + script.charAt(counter + 1);

				if ((test.equals("\r\n")) || (counter == script.length())) {
					lines.add(TempLines);
					TempLines = "";
					counter ++;

				} else {
					TempLines = "" + TempLines + script.charAt(counter);
				}

			}
			return lines;
		}
	}

	@Override
	public void create () {
		// Set the base value for Variables
		MouseLeft = Boolean.TRUE;
		MouseRight = Boolean.FALSE;
		TimeScroller = 0.0f;
		DialogueLine = 0;

		// font = new BitmapFont();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/basis33.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = (Color.BLACK);
		// font size 12 pixels
		font = generator.generateFont(parameter);
		// don't forget to dispose to avoid memory leaks!
		generator.dispose();


		batch = new SpriteBatch();

		// Stop continuous Rendering
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();

		// Sets up for text input
		String filename = "C:\\Users\\Dmitri\\Documents\\The actual Worst\\core\\assets\\dialogue.txt";
		text = new DialogueParser().readFile(filename);
		LinesOfText = new DialogueParser().parseFile(text);

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 640);

		// Load Images
		textboxImage = new Texture(Gdx.files.internal("Interface.png"));
		dateboxImage = new Texture(Gdx.files.internal("interface_date.png"));
		menuImage = new Texture(Gdx.files.internal("interface_menu.png"));
		SavLoadImage = new Texture(Gdx.files.internal("interface_save_load.png"));
		leoStandImage = new Texture(Gdx.files.internal("leo_stand.png"));

		// Load Audio
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// Start Music
		rainMusic.setLooping(true);
		rainMusic.play();

		// fuck if i know shapes? they bind things? i think uhhhh?
		bucket = new Rectangle();
		bucket.x = 800/2 - 64/2;
		bucket.y = 60;
		bucket.width = 64;
		bucket.height = 64;

		menu = new Rectangle();
		menu.width = 238;
		menu.height = 640;
		menu.x = 786;
		menu.y = 0;

	}


	@Override
	public void render () {
		// Set Background colour as white
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(leoStandImage, bucket.x, bucket.y);

		// Interface
		batch.draw(dateboxImage, 0, 470);
		batch.draw(textboxImage, 0, 0);

		// Code for Left Click to progress dialogue
		if((Gdx.input.isButtonPressed(Input.Buttons.LEFT)) && (DialogueLine != (LinesOfText.size()-1)) && (MouseRight == Boolean.FALSE) ) {
			DisplayText = "";
			DialogueLine = DialogueLine + 1;
			MouseLeft = Boolean.TRUE;
			TimeScroller = 0.0f;
		}

		// Add Scrolling to text or not sfuccckkkk fix this later
		 if (MouseLeft) {
			for (int counter = 0; counter < LinesOfText.get(DialogueLine).length(); counter++) {
				TimeScroller += Gdx.graphics.getDeltaTime();


				if (TimeScroller > 999f) {
					System.out.println(DisplayText);

					DisplayText = DisplayText + LinesOfText.get(DialogueLine).charAt(counter);
					font.draw(batch, DisplayText, 60, 100);
					TimeScroller = 0.0f;

				} else {
					if (counter != (LinesOfText.get(DialogueLine).length() - 1)) {
						counter--;
					} else  {
						MouseLeft = Boolean.FALSE;
					}
				}
			}
		}
		if (MouseLeft == Boolean.FALSE) {
			font.draw(batch, LinesOfText.get(DialogueLine), 60, 100);
		}

		// Code for right click to display a menu

		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			if (MouseRight == Boolean.TRUE) {
				MouseRight = Boolean.FALSE;
			} else {
				MouseRight = Boolean.TRUE;
			}
		}

		// Draw the right click menu and poll for input
		if(MouseRight == Boolean.TRUE) {
			batch.draw(menuImage, menu.x, menu.y);
			font.draw(batch, "Save", 870, 600);
			font.draw(batch, "Load", 870, 500);
			font.draw(batch, "Stats", 860, 400);
			font.draw(batch, "Menu", 870, 70);

			// Check if a "button" is clicked on
			if(Gdx.input.isTouched()) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				SaveTestX = touchPos.x - 64 / 2;
				SaveTestY = touchPos.y - 64 / 2;
				if ((SaveLowerX < SaveTestX)&&(SaveTestX < SaveUpperX)&&(SaveLowerY < SaveTestY)&&(SaveTestY < SaveUpperY)){
					System.out.print("save");
					DisplaySave = true;
				} else {
					if ((LoadLowerX < SaveTestX)&&(SaveTestX < LoadUpperX)&&(LoadLowerY < SaveTestY)&&(SaveTestY < LoadUpperY)) {
						System.out.print("load");
						DisplayLoad = true;
					}
				}
			}

		}
		if (DisplaySave) {
			batch.draw(SavLoadImage, 0, 0);
			font.draw(batch, "SAVE", 500, 500);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				DisplaySave = false;
			}
		}
		if (DisplayLoad) {
			batch.draw(SavLoadImage, 0, 0);
			font.draw(batch, "LOAD", 500, 500);
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
				DisplayLoad = false;
			}
		}


		batch.end();

		//This is useless, like me
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// Screen limits
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
