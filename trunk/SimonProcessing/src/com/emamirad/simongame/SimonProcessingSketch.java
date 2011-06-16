package com.emamirad.simongame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import TUIO.*;
import processing.core.*;

/**
 * This is an implementation of the Simon Game
 */
public class SimonProcessingSketch extends PApplet implements TuioListener {

	private static final String FONT_NAME = "Arial";

	private static final int FONT_SIZE = 18;

	private static final int FRAME_RATE = 30;

	private static final String WINDOW_TITLE = "MultiTouch Simon Game";

	TuioProcessing tuioClient;

	boolean unJoueur = false;
	boolean deuxJoueurs = false;

	boolean overUnJoueur = false;
	boolean overDeuxJoueurs = false;

	// this list contains all the pressed buttons.
	ArrayList<SimonColors> hoversList = new ArrayList<SimonColors>();
	ArrayList<SimonColors> hoversList2 = new ArrayList<SimonColors>();

	// hover pour le premier joueur
	SimonColors hover = SimonColors.NONE;

	// hover pour le deuxieme joueur
	SimonColors hover2 = SimonColors.NONE;

	// these are some helper variables which are used
	// to create scalable graphical feedback
	float cursor_size = 15;
	float object_size = 60;
	float table_size = 760;
	float scale_factor = 1;
	PFont font;
	int startTime;
	

	public void setup() {
		size(1024, 768);
		background(0, 0, 128);
		frameRate(FRAME_RATE);
		frame.setTitle(WINDOW_TITLE);

		loop();
		
		hint(ENABLE_NATIVE_FONTS);
		font = createFont(FONT_NAME, FONT_SIZE);
		scale_factor = height / table_size;

		// we create an instance of the TuioProcessing client
		// since we add "this" class as an argument the TuioProcessing class
		// expects
		// an implementation of the TUIO callback methods (see below)
		tuioClient = new TuioProcessing(this);
	}

	public void draw() {
		background(0, 0, 128);

		if (unJoueur == true) {
			drawUnJoueur();
			drawDashBoard();
		} else if (deuxJoueurs == true) {
			drawDeuxJoueurs();
			drawDashBoard();
		} else if (unJoueur == false && deuxJoueurs == false) {
			drawMenu();
		}

		// On dessine le tracé des mouvements qu'on effectue avec TUIO sur la
		// fenetre
		textFont(font, 18 * scale_factor);
		float obj_size = object_size * scale_factor;
		float cur_size = cursor_size * scale_factor;

		Vector<?> tuioObjectList = tuioClient.getTuioObjects();
		for (int i = 0; i < tuioObjectList.size(); i++) {
			TuioObject tobj = (TuioObject) tuioObjectList.elementAt(i);
			stroke(0);
			fill(0);
			pushMatrix();
			translate(tobj.getScreenX(width), tobj.getScreenY(height));
			rotate(tobj.getAngle());
			rect(-obj_size / 2, -obj_size / 2, obj_size, obj_size);
			popMatrix();
			fill(255);
			text("" + tobj.getSymbolID(), tobj.getScreenX(width), tobj
					.getScreenY(height));
		}

		Vector<?> tuioCursorList = tuioClient.getTuioCursors();
		for (int i = 0; i < tuioCursorList.size(); i++) {
			TuioCursor tcur = (TuioCursor) tuioCursorList.elementAt(i);
			Vector<?> pointList = tcur.getPath();

			if (pointList.size() > 0) {
				stroke(0, 0, 255);
				TuioPoint start_point = (TuioPoint) pointList.firstElement();

				for (int j = 0; j < pointList.size(); j++) {
					TuioPoint end_point = (TuioPoint) pointList.elementAt(j);
					line(start_point.getScreenX(width), start_point
							.getScreenY(height), end_point.getScreenX(width),
							end_point.getScreenY(height));
					start_point = end_point;
				}
				stroke(192, 192, 192);
				fill(192, 192, 192);
				ellipse(tcur.getScreenX(width), tcur.getScreenY(height),
						cur_size, cur_size);
				fill(0);
				text("" + tcur.getCursorID(), tcur.getScreenX(width) - 5, tcur
						.getScreenY(height) + 5);
			}
		}
	}

/*	public void mouseMoved() {
		checkButtons(mouseX, mouseY);
	}

	public void mouseDragged() {
		checkButtons(mouseX, mouseY);
	}

	public void mousePressed() {
		if (overUnJoueur == true) {
			unJoueur = true;
		}
		if (overDeuxJoueurs == true) {
			deuxJoueurs = true;
		}
	}*/

	void drawDashBoard() {
		int secondes = (millis() - startTime) / 1000;

		fill(255, 255, 255); // blanc
		text("Level:", 30, 650);
		text("Time:", 30, 700);
		text("High Score:", 250, 700);
		fill(128, 128, 128);
		text("01", 100, 650);
		text(Integer.toString(secondes), 100, 700);
		fill(0, 255, 0);
		text("30", 400, 700);
	}

	void drawMenu() {
		if (overUnJoueur == true) {
			fill(255, 255, 255, 255);
			stroke(255, 0, 0);
		} else {
			fill(255, 255, 255, 255);
			stroke(0, 0, 255);
		}
		rect(110, 310, 200, 100);

		if (overDeuxJoueurs == true) {
			fill(255, 255, 255, 255);
			stroke(255, 0, 0);
		} else {
			fill(255, 255, 255, 255);
			stroke(0, 0, 255);
		}
		rect(450, 310, 200, 100);

		PFont font = loadFont("BrowalliaUPC-Bold-32.vlw");
		textFont(font);
		fill(0, 0, 255);
		text("1 joueur", 120, 360);
		text("2 joueurs", 460, 360);
	}

	void drawUnJoueur() {
		fill(SimonColors.YELLOW.getR(), SimonColors.YELLOW.getG(),
				SimonColors.YELLOW.getB()); // yellow
		arc(512, 300, 460, 400, 0, PI / 2);
		fill(SimonColors.CYAN.getR(), SimonColors.CYAN.getG(), SimonColors.CYAN
				.getB()); // cyan
		arc(512, 300, 460, 400, PI / 2, PI);
		fill(SimonColors.GREEN.getR(), SimonColors.GREEN.getG(),
				SimonColors.GREEN.getB()); // green
		arc(512, 300, 460, 400, PI, TWO_PI - PI / 2);
		fill(SimonColors.RED.getR(), SimonColors.RED.getG(), SimonColors.RED
				.getB()); // red
		arc(512, 300, 460, 400, TWO_PI - PI / 2, TWO_PI);

		for (Iterator<SimonColors> iterator = hoversList.iterator(); iterator
				.hasNext();) {
			SimonColors color = iterator.next();

			switch (color) {
				case YELLOW :
					fill(SimonColors.HOVER_YELLOW.getR(),
							SimonColors.HOVER_YELLOW.getG(),
							SimonColors.HOVER_YELLOW.getB());
					arc(512, 300, 460, 400, 0, PI / 2);

					break;
				case CYAN :
					fill(SimonColors.HOVER_CYAN.getR(), SimonColors.HOVER_CYAN
							.getG(), SimonColors.HOVER_CYAN.getB());
					arc(512, 300, 460, 400, PI / 2, PI);

					break;
				case GREEN :
					fill(SimonColors.HOVER_GREEN.getR(),
							SimonColors.HOVER_GREEN.getG(),
							SimonColors.HOVER_GREEN.getB());
					arc(512, 300, 460, 400, PI, TWO_PI - PI / 2);

					break;
				case RED :
					fill(SimonColors.HOVER_RED.getR(), SimonColors.HOVER_RED
							.getG(), SimonColors.HOVER_RED.getB());
					arc(512, 300, 460, 400, TWO_PI - PI / 2, TWO_PI);
					break;

				default :
					break;
			}
		}

	}

	void drawDeuxJoueurs() {
		fill(SimonColors.YELLOW.getR(), SimonColors.YELLOW.getG(),
				SimonColors.YELLOW.getB()); // jaune
		arc(265, 300, 460, 400, 0, PI / 2);
		fill(SimonColors.CYAN.getR(), SimonColors.CYAN.getG(), SimonColors.CYAN
				.getB()); // cyan
		arc(265, 300, 460, 400, PI / 2, PI);
		fill(SimonColors.GREEN.getR(), SimonColors.GREEN.getG(),
				SimonColors.GREEN.getB()); // vert
		arc(265, 300, 460, 400, PI, TWO_PI - PI / 2);
		fill(SimonColors.RED.getR(), SimonColors.RED.getG(), SimonColors.RED
				.getB()); // rouge
		arc(265, 300, 460, 400, TWO_PI - PI / 2, TWO_PI);
		fill(SimonColors.YELLOW.getR(), SimonColors.YELLOW.getG(),
				SimonColors.YELLOW.getB()); // jaune
		arc(760, 300, 460, 400, 0, PI / 2);
		fill(SimonColors.CYAN.getR(), SimonColors.CYAN.getG(), SimonColors.CYAN
				.getB()); // cyan
		arc(760, 300, 460, 400, PI / 2, PI);
		fill(SimonColors.GREEN.getR(), SimonColors.GREEN.getG(),
				SimonColors.GREEN.getB()); // vert
		arc(760, 300, 460, 400, PI, TWO_PI - PI / 2);
		fill(SimonColors.RED.getR(), SimonColors.RED.getG(), SimonColors.RED
				.getB()); // rouge
		arc(760, 300, 460, 400, TWO_PI - PI / 2, TWO_PI);

		switch (hover) {
			case YELLOW :
				fill(SimonColors.HOVER_YELLOW.getR(),
						SimonColors.HOVER_YELLOW.getG(),
						SimonColors.HOVER_YELLOW.getB());
				arc(265, 300, 460, 400, 0, PI / 2);

				break;
			case CYAN :
				fill(SimonColors.HOVER_CYAN.getR(),
						SimonColors.HOVER_CYAN.getG(),
						SimonColors.HOVER_CYAN.getB());
				arc(265, 300, 460, 400, PI / 2, PI);

				break;
			case GREEN :
				fill(SimonColors.HOVER_GREEN.getR(),
						SimonColors.HOVER_GREEN.getG(),
						SimonColors.HOVER_GREEN.getB());
				arc(265, 300, 460, 400, PI, TWO_PI - PI / 2);

				break;
			case RED :
				fill(SimonColors.HOVER_RED.getR(),
						SimonColors.HOVER_RED.getG(),
						SimonColors.HOVER_RED.getB());
				arc(265, 300, 460, 400, TWO_PI - PI / 2, TWO_PI);
				break;

			default :
				break;
		}

		switch (hover2) {
			case YELLOW :
				fill(SimonColors.HOVER_YELLOW.getR(),
						SimonColors.HOVER_YELLOW.getG(),
						SimonColors.HOVER_YELLOW.getB());
				arc(760, 300, 460, 400, 0, PI / 2);

				break;
			case CYAN :
				fill(SimonColors.HOVER_CYAN.getR(),
						SimonColors.HOVER_CYAN.getG(),
						SimonColors.HOVER_CYAN.getB());
				arc(760, 300, 460, 400, PI / 2, PI);

				break;
			case GREEN :
				fill(SimonColors.HOVER_GREEN.getR(),
						SimonColors.HOVER_GREEN.getG(),
						SimonColors.HOVER_GREEN.getB());
				arc(760, 300, 460, 400, PI, TWO_PI - PI / 2);

				break;
			case RED :
				fill(SimonColors.HOVER_RED.getR(),
						SimonColors.HOVER_RED.getG(),
						SimonColors.HOVER_RED.getB());
				arc(760, 300, 460, 400, TWO_PI - PI / 2, TWO_PI);
				break;

			default :
				break;
		}
	}

	void checkButtons(float mouseX, float mouseY) {
		if (unJoueur == true) {
			hoversList.set(hoversList.size() - 1, hoveringColor(mouseX, mouseY));
		}

		if (deuxJoueurs == true) {
			if (mouseX > 265 && mouseX < 495 && mouseY > 300 && mouseY < 500) {
				hoversList.set(hoversList.size() - 1, SimonColors.YELLOW);
				hover = SimonColors.YELLOW;
			} else if (mouseX > 35 && mouseX < 265 && mouseY > 300
					&& mouseY < 500) {
				hoversList.set(hoversList.size() - 1, SimonColors.CYAN);
				hover = SimonColors.CYAN;
			} else if (mouseX > 35 && mouseX < 265 && mouseY > 100
					&& mouseY < 300) {
				hoversList.set(hoversList.size() - 1, SimonColors.GREEN);
				hover = SimonColors.GREEN;
			} else if (mouseX > 265 && mouseX < 495 && mouseY > 100
					&& mouseY < 300) {
				hoversList.set(hoversList.size() - 1, SimonColors.RED);
				hover = SimonColors.RED;
			} else if (mouseX > 760 && mouseX < 990 && mouseY > 300
					&& mouseY < 500) {
				hoversList2.set(hoversList2.size() - 1, SimonColors.YELLOW);
				hover2 = SimonColors.YELLOW;
			} else if (mouseX > 530 && mouseX < 760 && mouseY > 300
					&& mouseY < 500) {
				hoversList2.set(hoversList2.size() - 1, SimonColors.CYAN);
				hover2 = SimonColors.CYAN;
			} else if (mouseX > 530 && mouseX < 760 && mouseY > 100
					&& mouseY < 300) {
				hoversList2.set(hoversList2.size() - 1, SimonColors.GREEN);
				hover2 = SimonColors.GREEN;
			} else if (mouseX > 760 && mouseX < 990 && mouseY > 100
					&& mouseY < 300) {
				hoversList2.set(hoversList2.size() - 1, SimonColors.RED);
				hover2 = SimonColors.RED;
			} else {
				hover = hover2 = SimonColors.NONE;
			}
		}

		else {
			if (mouseX > 110 && mouseX < 310 && mouseY > 310 && mouseY < 410) {
				overUnJoueur = true;
			} else if (mouseX > 450 && mouseX < 550 && mouseY > 310
					&& mouseY < 410) {
				overDeuxJoueurs = true;
			} else {
				overUnJoueur = overDeuxJoueurs = false;
			}
		}
	}
	
	private SimonColors hoveringColor(float mouseX, float mouseY){
		if (mouseX > 512 && mouseX < 742 && mouseY > 100
				&& mouseY < 300) {
			return SimonColors.RED;
		}
		else if (mouseX > 282 && mouseX < 512 && mouseY > 100
				&& mouseY < 300) {
			return SimonColors.GREEN;
		}
		else if (mouseX > 282 && mouseX < 512 && mouseY > 300
				&& mouseY < 500) {
			return SimonColors.CYAN;
		}
		else if (mouseX > 512 && mouseX < 742 && mouseY > 300 && mouseY < 500) {
			return SimonColors.YELLOW;
		}
		else {
			return SimonColors.NONE;
		}		
	}

	public void keyPressed() {
		if (key == ESC) {
			key = 0;
		}
	}

	// called when an object is added to the scene
	public void addTuioObject(TuioObject tobj) {
		println("add object " + tobj.getSymbolID() + " (" + tobj.getSessionID()
				+ ") " + tobj.getX() + " " + tobj.getY() + " "
				+ tobj.getAngle());
	}

	// called when an object is removed from the scene
	public void removeTuioObject(TuioObject tobj) {
		println("remove object " + tobj.getSymbolID() + " ("
				+ tobj.getSessionID() + ")");
	}

	// called when an object is moved
	public void updateTuioObject(TuioObject tobj) {
		println("update object " + tobj.getSymbolID() + " ("
				+ tobj.getSessionID() + ") " + tobj.getX() + " " + tobj.getY()
				+ " " + tobj.getAngle() + " " + tobj.getMotionSpeed() + " "
				+ tobj.getRotationSpeed() + " " + tobj.getMotionAccel() + " "
				+ tobj.getRotationAccel());
	}

	// called when a cursor is added to the scene
	public void addTuioCursor(TuioCursor tcur) {
		println("add cursor " + tcur.getCursorID() + " (" + tcur.getSessionID()
				+ ") " + tcur.getX() + " " + tcur.getY());
		System.out
				.println("addTuioCursor getTuioState: " + tcur.getTuioState());
		hoversList.add(SimonColors.NONE);
	}

	// called when a cursor is moved
	public void updateTuioCursor(TuioCursor tcur) {
		println("update cursor " + tcur.getCursorID() + " ("
				+ tcur.getSessionID() + ") " + tcur.getX() + " " + tcur.getY()
				+ " " + tcur.getMotionSpeed() + " " + tcur.getMotionAccel());
		checkButtons(tcur.getX() * width, tcur.getY() * height);
		System.out.println("updateTuioCursor getTuioState: "
				+ tcur.getTuioState());
	}

	// called when a cursor is removed from the scene
	public void removeTuioCursor(TuioCursor tcur) {
		println("remove cursor " + tcur.getCursorID() + " ("
				+ tcur.getSessionID() + ")");
		if (overUnJoueur == true) {
			unJoueur = true;
			startTime = millis();
		}
		if (overDeuxJoueurs == true) {
			deuxJoueurs = true;
			startTime = millis();
		}
		hover = hover2 = SimonColors.NONE;
		hoversList.remove(tcur.getCursorID());
		if (tuioClient.getTuioCursors().size() == 0) {
			hoversList.clear();
		}
		System.out.println("removeTuioCursor getTuioState: "
				+ tcur.getTuioState());
		System.out.println("removeTuioCursor hoversList size: "
				+ hoversList.size());
	}

	@Override
	public void refresh(TuioTime bundleTime) {
		redraw();
	}

	public static void main(String args[]) {
		PApplet
				.main(new String[]{"com.emamirad.simongame.SimonProcessingSketch"});
	}

}
