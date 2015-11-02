package edu.cmu.ssnayak.mobilegameengine;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.ssnayak.mobilegameengine.event.ButtonPressedEvent;
import edu.cmu.ssnayak.mobilegameengine.event.DragEndEvent;
import edu.cmu.ssnayak.mobilegameengine.event.DragMoveEvent;
import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;
import edu.cmu.ssnayak.mobilegameengine.event.FSMEventType;
import edu.cmu.ssnayak.mobilegameengine.event.TouchMoveEvent;
import edu.cmu.ssnayak.mobilegameengine.event.TouchPressEvent;
import edu.cmu.ssnayak.mobilegameengine.event.TouchReleaseEvent;
import edu.cmu.ssnayak.mobilegameengine.event.XYEvent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Child class of GameEnginePreBase that implements the dispatch
 * methods that help in animation and dispatch touch events
 */
public class GameEngineBase extends GameEnginePreBase {

    /**
     * Constructor for GameEngineBase
     * @param xmlFileID
     * @param ctx
     */
	public GameEngineBase(int xmlFileID, Context ctx){
		super(xmlFileID, ctx);
		//send initialize messages to all characters
        sendInitMessages();
	}

    /**
     * Check if a given characters bounds overlaps with a specified rectangular
     * area. Returns lists of characters which do so in reverse drawing order as
     * specified
     * @param area Rectangle we are testing for overlap against.
     * @return
     */
	@Override
	protected List<GameCharacter> charactersUnder(RectF area) {
        List<GameCharacter> gameCharacters = new ArrayList<GameCharacter>();

        //overlap check for each character
        for(int i=0; i<this._characters.length;i++) {
            GameCharacter gameCharacter = this._characters[i];
            //construct character bounds
            RectF characterBounds = new RectF(gameCharacter.getX(), gameCharacter.getY(), (gameCharacter.getX()+gameCharacter.getW()),
                                            (gameCharacter.getY()+gameCharacter.getH()));
            //use OOTB function in RectF that checks for overlap
            boolean isOverLap = RectF.intersects(characterBounds, area);
            if(isOverLap) {
                //add in reverse order
                gameCharacters.add(0, gameCharacter);
            }
        }
		return gameCharacters;
	}


    /**
     * Dispatch event to a character under a given x, y co-ordinate
     * Returns after the first character in drawing order that consumes event
     * @param evt the event to attempt to dispatch
     * @return
     */
	@Override
	protected boolean dispatchPositionally(XYEvent evt) {
        return dispatchPositionally(new RectF(evt.getX(), evt.getY(), evt.getX()+1, evt.getY()+1), evt);
	}

    /**
     * Dispatch a given event to a character under a given x, y position
     * Dispatched only to the first character which will consume the event
     * in drawing order
     * @param inArea minute pixel area for overlap check with character
     * @param evt the event to attempt to dispatch
     * @return
     */
	@Override
	protected boolean dispatchPositionally(RectF inArea, FSMEvent evt) {
        List<GameCharacter> gameCharacters = charactersUnder(inArea);
        if(inArea != null && gameCharacters != null) {
            for (int i = 0; i < gameCharacters.size(); i++) {
                GameCharacter gameChar = gameCharacters.get(i);
                if (gameChar.deliverEvent(evt)) {
                    return true;
                }
            }
        }
        return false;
	}

    /**
     * Delivers the event to a  character specified by function parameters
     * @param toChar
     * @param evt the event to attempt to dispatch
     * @return
     */
	@Override
	protected boolean dispatchDirect(int toChar, FSMEvent evt) {
		//validate if the character index passed in is correct
        if(isCharacter(toChar)) {
            GameCharacter gameChar = this._characters[toChar];
            return gameChar.deliverEvent(evt);
        }
        return false;
	}

    /**
     * Dispatches a given event to all characters in reverse
     * drawing order irrespective if
     * they consume it or not. Does not stop after a character
     * consumes event
     * @param evt the event to attempt to dispatch
     * @return
     */
	@Override
	protected boolean dispatchToAll(FSMEvent evt) {
        if(this._characters!=null) {
            for (int i = this._characters.length - 1; i >= 0; i--) {
                GameCharacter gameChar = this._characters[i];
                //do not care of the gameChar consumes event or not
                gameChar.deliverEvent(evt);
            }
        }
        return false;
	}

    /**
     * Dispatches an event iteratively to all characters in reverse
     * drawing order. If a character in the sequence consumes it, the
     * dispatch process stops
     * @param evt the event to attempt to dispatch
     * @return
     */
	@Override
	protected boolean dispatchTryAll(FSMEvent evt) {
        for(int i=this._characters.length-1;i>=0;i--) {
            GameCharacter gameChar = this._characters[i];
            //return after the first gameChar consumes the event
            if(gameChar.deliverEvent(evt)) {
                return true;
            }
        }
        return false;
	}

    /**
     * Dispatches an xy event to an object currently in
     * drag focus
     * @param evt the event being dispatched
     * @return
     */
	@Override 
	protected boolean dispatchDragFocus(FSMEvent evt) {
		if(this._dragFocus == -1) {return false;}
        // check if a given event is an XY event
        if(FSMEventType.isXYEvent(evt.getType())) {
            XYEvent xyEvent = (XYEvent) evt.copy();
            //offset the event x,y co-ordinates from the top left corner of the character
            xyEvent.offset(-(this._grabPointX - getX()), -(this._grabPointY - getY()));
            GameCharacter gameCharacter = this._characters[this._dragFocus];
            //return after the first gameChar consumes the event
            if(gameCharacter.deliverEvent(xyEvent)) {
                return true;
            }
        }
        return false;
	}

    /**
     * Draw the characters on canvas in drawing order
     * @param canv the Canvas object that drawing occurs on.
     */
	@Override
	protected void onDraw(Canvas canv) {
        if(this._characters != null) {
            for (GameCharacter child : this._characters) {
                canv.save();
                canv.translate(child.getX(), child.getY());
                canv.clipRect(0, 0, child.getW(), child.getH());
                child.draw(canv);
                canv.restore();
            }
        }
	}

    /**
     * Dispatches a button press event to all the characters in
     * the game
     * @param buttonNum the index of the game action button that was pressed.
     */
	@Override
	protected void buttonHit(int buttonNum) {
		//Get Button Name from Id - dispatch appropriate event to all characters
        ButtonPressedEvent buttonPressedEvent = new ButtonPressedEvent(buttonNum);
        dispatchToAll(buttonPressedEvent);
	}

    /**
     * Overriding the view onTouchEvent to
     * convert low level MotionEvents to desired
     * events for use in game engine
     * @param evt the motionEvent that has occured
     * @return
     */
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

        //if a character consumed one of the actions, return true to caller
        boolean actionDownDispatch = false;
        boolean actionMoveDispatch = false;
        boolean actionUpDispatch = false;

		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            //create a TouchPressEvent
            TouchPressEvent touchPressEvent = new TouchPressEvent(x, y);
            actionDownDispatch = dispatchPositionally(new RectF(x, y, x + 1, y + 1), touchPressEvent);

		} else if (evt.getAction() == MotionEvent.ACTION_MOVE) {
            //ActionMove is dragMove if a drag object is currently in focus
            //else it's a touch move
            if(this._dragFocus==-1) {
                TouchMoveEvent touchMoveEvent = new TouchMoveEvent(x, y);
                actionMoveDispatch = dispatchPositionally(new RectF(x, y, x + 1, y + 1), touchMoveEvent);
            } else {
                DragMoveEvent dragMoveEvent = new DragMoveEvent(x, y);
                actionMoveDispatch = dispatchDragFocus(dragMoveEvent);
            }
		} else if (evt.getAction() == MotionEvent.ACTION_UP) {
            //ActionMove is dragEnd if a drag object is currently in focus
            //else it's a touch release
            if(this._dragFocus==-1) {
                TouchReleaseEvent touchReleaseEvent = new TouchReleaseEvent(x, y);
                actionUpDispatch = dispatchPositionally(new RectF(x, y, x + 1, y + 1), touchReleaseEvent);
            } else {
                DragEndEvent dragEndEvent = new DragEndEvent(x, y);
                actionUpDispatch = dispatchDragFocus(dragEndEvent);
            }
		} else {
			// not an event we understand...
			return false;
		}
        //if dispatch is consumes, below will be true
        return (actionDownDispatch || actionMoveDispatch || actionUpDispatch);
	}
}
