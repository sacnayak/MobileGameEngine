package edu.cmu.ssnayak.mobilegameengine;

import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;
import android.graphics.Bitmap;
import android.graphics.Canvas;

interface GameCharacter {
	GameEnginePreBase getOwner(); 
	int getCharacterIndex();
	float getX();
	void setX(float xv);
	float getY(); 
	void setY(float yv);
	float getW();
	void setW(float wv);
	float getH();
	void setH(float hv);
	Bitmap getImage();
	void setImage(Bitmap newImage);
	static final int START_STATE = 0;
	int getCurrentState();
	void resetState();
	boolean deliverEvent(FSMEvent event);  
	  // returns true if event was "consumed" (acted upon)
	void draw(Canvas canv);  
	//xx local coordinates will have been set up prior to calling this 
	//   method, so the top left will be at 0,0 (not x,y)
}
