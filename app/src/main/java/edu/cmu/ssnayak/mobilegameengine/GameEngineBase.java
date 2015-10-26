package edu.cmu.ssnayak.mobilegameengine;

import java.util.List;

import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;
import edu.cmu.ssnayak.mobilegameengine.event.XYEvent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

public class GameEngineBase extends GameEnginePreBase {

	public GameEngineBase(int xmlFileID, Context ctx){
		super(xmlFileID, ctx);
		sendInitMessages();
	}
	
	@Override
	protected List<GameCharacter> charactersUnder(RectF area) {
		return null;
	}
	
	
	@Override
	protected boolean dispatchPositionally(XYEvent evt) {
		return false;
	}
	
	@Override
	protected boolean dispatchPositionally(RectF inArea, FSMEvent evt) {
		return false;
	}
	
	@Override
	protected boolean dispatchDirect(int toChar, FSMEvent evt) {
		return false;
	}
	
	@Override
	protected boolean dispatchToAll(FSMEvent evt) {
		return false;
	}
	
	@Override
	protected boolean dispatchTryAll(FSMEvent evt) {
		return false;
	}
	
	
	@Override 
	protected boolean dispatchDragFocus(FSMEvent evt) {
		return false; 
	}

	@Override
	protected void onDraw(Canvas canv) {

	}

	@Override
	protected void buttonHit(int buttonNum) {
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		// Skeleton code provided below. Your job is to
		// implement the empty code blocks in the if/else
		// tree. Hint: you need to dispatch the correct
		// event using one of the dispatch methods you
		// implemented above, and by instantiating the
		// appropriate FSMEvent (e.g., TouchReleaseEvent).
		
		/*float x = evt.getX();
		float y = evt.getY();

		if (evt.getAction() == MotionEvent.ACTION_DOWN) {


		} else if (evt.getAction() == MotionEvent.ACTION_MOVE) {


		} else if (evt.getAction() == MotionEvent.ACTION_UP) {

		} else {
			// not an event we understand...
			return false;
		}*/
		
		return false;
	}
}
