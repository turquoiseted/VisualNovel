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
import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	// Music
	private Music rainMusic;
	// Images
	private Texture textboxImage;
	private Texture dateboxImage;
	private Texture leoStandImage;
	private Texture menuImage;

	// Temp
	private Rectangle bucket;
	private Rectangle menu;

	// uhh
	private Boolean MouseRight;
	private Long MenuAccessed;
	private BitmapFont font;

	private CharSequence text;
	private ArrayList<CharSequence> LinesOfText;

	private String filename = "C:\\Users\\Dmitri\\Documents\\The actual Worst\\core\\assets\\dialogue.txt";
	private int DialogueLine;

	public class DialogueParser {
		// Text
		private CharSequence content;
		private ArrayList<CharSequence> lines;
		private CharSequence TempLines;
		private File dialogue;
		private int counter;
		private String test;

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
		// This is my last resort, splits shit into arrays of shit
		public ArrayList<CharSequence> parseFile(CharSequence script) {
			TempLines = "";
			lines = new ArrayList<CharSequence>();
			for (counter = 0; counter < script.length(); counter = counter + 1) {
				// Makes a string of the current 2 characters
				test = "" + script.charAt(counter) + script.charAt(counter + 1);

				if ((test.equals("\r\n")) || (counter == script.length())) {
					lines.add(TempLines);
					TempLines = "";
					counter = counter + 1;

				} else {
					TempLines = "" + TempLines + script.charAt(counter);
				}

			}
			return lines;
		}
	}

	@Override
	public void create () {
		//text???
		DialogueLine = 0;
		// font = new BitmapFont();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/basis33.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = (Color.BLACK);
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!


		// Stop continous Rendering
		Gdx.graphics.setContinuousRendering(false);
		Gdx.graphics.requestRendering();

		// Set any variables that need setting
		MenuAccessed = TimeUtils.nanoTime();

		text = new DialogueParser().readFile(filename);
		LinesOfText = new DialogueParser().parseFile(text);

		batch = new SpriteBatch();

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 640);

		// Load Images
		textboxImage = new Texture(Gdx.files.internal("Interface.png"));
		dateboxImage = new Texture(Gdx.files.internal("interface_date.png"));
		menuImage = new Texture(Gdx.files.internal("interface_menu.png"));
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
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(leoStandImage, bucket.x, bucket.y);

		// Interface
		batch.draw(dateboxImage, 0, 470);
		batch.draw(textboxImage, 0, 0);

		// Code for Left Click to progress the game
		if((Gdx.input.isButtonPressed(Input.Buttons.LEFT)) && (DialogueLine != (LinesOfText.size()-1) ) ) {
			DialogueLine = DialogueLine + 1;
		}

		font.draw(batch, LinesOfText.get(DialogueLine), 60, 100);

		// Code for right click to display a menu

		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			if (MouseRight == Boolean.TRUE) {
				MouseRight = Boolean.FALSE;
			} else {
				MouseRight = Boolean.TRUE;
				MenuAccessed = TimeUtils.nanoTime();
			}
		}

		if(MouseRight == Boolean.TRUE) {
			batch.draw(menuImage, menu.x, menu.y);
		}


		batch.end();
		// Code for item movement
		/*if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = (int) (touchPos.x - (64 / 2));
		}*/
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
