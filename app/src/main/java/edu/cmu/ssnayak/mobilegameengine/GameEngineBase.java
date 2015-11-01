package edu.cmu.ssnayak.mobilegameengine;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ssnayak.mobilegameengine.event.ButtonPressedEvent;
import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;
import edu.cmu.ssnayak.mobilegameengine.event.FSMEventType;
import edu.cmu.ssnayak.mobilegameengine.event.TouchMoveEvent;
import edu.cmu.ssnayak.mobilegameengine.event.TouchPressEvent;
import edu.cmu.ssnayak.mobilegameengine.event.TouchReleaseEvent;
import edu.cmu.ssnayak.mobilegameengine.event.XYEvent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

public class GameEngineBase extends GameEnginePreBase {

	public GameEngineBase(int xmlFileID, Context ctx){
		super(xmlFileID, ctx);
		sendInitMessages();
	}
	
	@Override
	protected List<GameCharacter> charactersUnder(RectF area) {
        List<GameCharacter> gameCharacters = new ArrayList<GameCharacter>();

        //trivial reject
        for(int i=0; i<this._characters.length;i++) {
            GameCharacter gameCharacter = this._characters[i];
            RectF characterBounds = new RectF(gameCharacter.getX(), gameCharacter.getY(), (gameCharacter.getX()+gameCharacter.getW()),
                                            (gameCharacter.getY()+gameCharacter.getH()));
            boolean isOverLap = RectF.intersects(characterBounds, area);
            if(isOverLap) {
                //add in reverse order
                gameCharacters.add(0, gameCharacter);
            }
        }
		return gameCharacters;
	}


	@Override
	protected boolean dispatchPositionally(XYEvent evt) {
        return dispatchPositionally(new RectF(evt.getX(), evt.getY(), evt.getX()+1, evt.getY()+1), evt);
	}
	
	@Override
	protected boolean dispatchPositionally(RectF inArea, FSMEvent evt) {
        List<GameCharacter> gameCharacters = charactersUnder(inArea);

        for(int i=0;i<gameCharacters.size();i++) {
            GameCharacter gameChar = gameCharacters.get(i);
            if(gameChar.deliverEvent(evt)) {
                return true;
            }
        }
        return false;
	}
	
	@Override
	protected boolean dispatchDirect(int toChar, FSMEvent evt) {
		GameCharacter gameChar = this._characters[toChar];
        return gameChar.deliverEvent(evt);
	}
	
	@Override
	protected boolean dispatchToAll(FSMEvent evt) {
        for(int i=this._characters.length-1;i>=0;i--) {
            GameCharacter gameChar = this._characters[i];
            gameChar.deliverEvent(evt);
        }
        return false;
	}
	
	@Override
	protected boolean dispatchTryAll(FSMEvent evt) {
        for(int i=this._characters.length-1;i>=0;i--) {
            GameCharacter gameChar = this._characters[i];
            if(gameChar.deliverEvent(evt)) {
                return true;
            }
        }
        return false;
	}
	
	
	@Override 
	protected boolean dispatchDragFocus(FSMEvent evt) {
		if(this._dragFocus == -1) {return false;}

        if(FSMEventType.isXYEvent(evt.getType())) {
            XYEvent xyEvent = (XYEvent) evt;
            xyEvent.offset(this._grabPointX, this._grabPointY);
            GameCharacter gameCharacter = this._characters[this._dragFocus];
            if(gameCharacter.deliverEvent(xyEvent)) {
                return true;
            }
        }
        return false;
	}

	@Override
	protected void onDraw(Canvas canv) {
        for (GameCharacter child : this._characters) {
            canv.save();
            canv.translate(child.getX(), child.getY());
            canv.clipRect(0, 0, child.getW(), child.getH());
            child.draw(canv);
            canv.restore();
        }
	}

	@Override
	protected void buttonHit(int buttonNum) {
		//Get Button Name from Id - dispatch appropriate event to all characters
        ButtonPressedEvent buttonPressedEvent = new ButtonPressedEvent(buttonNum);
        //FIXME deliver to all or try deliver to all?
        dispatchToAll(buttonPressedEvent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		// Skeleton code provided below. Your job is to
		// implement the empty code blocks in the if/else
		// tree. Hint: you need to dispatch the correct
		// event using one of the dispatch methods you
		// implemented above, and by instantiating the
		// appropriate FSMEvent (e.g., TouchReleaseEvent).
		
		float x = evt.getX();
		float y = evt.getY();

		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            TouchPressEvent touchPressEvent = new TouchPressEvent(x, y);
            dispatchPositionally(new RectF(x, y, x + 1, y + 1), touchPressEvent);
		} else if (evt.getAction() == MotionEvent.ACTION_MOVE) {
            TouchMoveEvent touchMoveEvent = new TouchMoveEvent(x, y);
            dispatchPositionally(new RectF(x, y, x + 1, y + 1), touchMoveEvent);
		} else if (evt.getAction() == MotionEvent.ACTION_UP) {
            TouchReleaseEvent touchReleaseEvent = new TouchReleaseEvent(x, y);
            dispatchPositionally(new RectF(x, y, x + 1, y + 1), touchReleaseEvent);
		} else {
			// not an event we understand...
			return false;
		}
		
		return false;
	}
}
