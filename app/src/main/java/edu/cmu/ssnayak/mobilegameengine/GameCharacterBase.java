package edu.cmu.ssnayak.mobilegameengine;

import edu.cmu.ssnayak.mobilegameengine.action.ChangeImageAction;
import edu.cmu.ssnayak.mobilegameengine.action.DebugAction;
import edu.cmu.ssnayak.mobilegameengine.action.DropDragFocusAction;
import edu.cmu.ssnayak.mobilegameengine.action.FSMAction;
import edu.cmu.ssnayak.mobilegameengine.action.FSMActionType;
import edu.cmu.ssnayak.mobilegameengine.action.FollowEventAction;
import edu.cmu.ssnayak.mobilegameengine.action.GetDragFocusAction;
import edu.cmu.ssnayak.mobilegameengine.action.MoveIncAction;
import edu.cmu.ssnayak.mobilegameengine.action.MoveToAction;
import edu.cmu.ssnayak.mobilegameengine.action.RunAnimAction;
import edu.cmu.ssnayak.mobilegameengine.action.SendMessageAction;
import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;
import edu.cmu.ssnayak.mobilegameengine.event.MessageEvent;
import edu.cmu.ssnayak.mobilegameengine.event.XYEvent;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameCharacterBase extends GameCharacterPreBase {

	@Override
	public boolean deliverEvent(FSMEvent event) {
        // get state - loop through transitions - match event - if match - make FSM Transition
        // -  return true
        FSMState currState = this._FSMStateTable[this.getCurrentState()];

        for(int i=0; i<currState.getNumOfTransitions() ;i++) {
            FSMTransition transition = currState.getTransitionAt(i);
            if(transition.match(event)) {
                //make transition and return true
                makeFSMTransition(transition, event);
                return true;
            }
        }

		return false;
	}

	@Override
	protected void makeFSMTransition(FSMTransition transition, FSMEvent evt) {
		//take actions of transitions - catch action - do action
        GameEnginePreBase owner = this.getOwner();
        for(int i=0;i<transition.getAction().length;i++) {
            FSMAction action = transition._action[i];
            switch (action.getType()) {

                case FSMActionType.CHANGE_IMAGE:
                    //change bitmap to image in ChangeImageAction
                    ChangeImageAction changeImageAction = (ChangeImageAction) action;
                    Bitmap newImg = changeImageAction.getImage();
                    //set new image, bounds
                    this.setImage(newImg);
                    this.setW(newImg.getWidth());
                    this.setH(newImg.getHeight());
                    //invalidate for damage and redraw
                    owner.damageCharacter(this.getCharacterIndex());
                    break;

                case FSMActionType.MOVE_TO:
                    MoveToAction moveToAction = (MoveToAction) action;
                    this.setX(moveToAction.getX());
                    this.setY(moveToAction.getY());
                    //invalidate parent
                    owner.damageCharacter(this.getCharacterIndex());
                    break;

                case FSMActionType.MOVE_INC:
                    MoveIncAction moveIncAction = (MoveIncAction) action;
                    this.setX(getX() + moveIncAction.getX());
                    this.setY(getY() + moveIncAction.getY());
                    //invalidate parent
                    owner.damageCharacter(this.getCharacterIndex());
                    break;

                case FSMActionType.FOLLOW_EVENT_POSITION:
                    FollowEventAction followEventAction = (FollowEventAction) action;
                    XYEvent animEvent = (XYEvent) evt;
                    this.setX(animEvent.getX());
                    this.setY(animEvent.getY());
                    //invalidate parent
                    owner.damageCharacter(this.getCharacterIndex());
                    break;

                case FSMActionType.RUN_ANIM:
                    RunAnimAction runAnimAction = (RunAnimAction) action;
                    owner.startNewAnimation(runAnimAction);
                    break;

                case FSMActionType.GET_DRAG_FOCUS:
                    GetDragFocusAction getDragFocusAction = (GetDragFocusAction) action;
                    XYEvent touchPressEvent = (XYEvent) evt;
                    owner.requestDragFocus(getCharacterIndex(), touchPressEvent.getX(), touchPressEvent.getY());
                    break;
                case FSMActionType.DROP_DRAG_FOCUS:
                    DropDragFocusAction dropDragFocusAction = (DropDragFocusAction) action;
                    owner.releaseDragFocus();
                    break;

                case FSMActionType.SEND_MESSAGE:
                    SendMessageAction sendMessageAction = (SendMessageAction) action;
                    MessageEvent sendMessageEvent = (MessageEvent) evt;
                    owner.sendMessage(sendMessageAction.getTargetCharacter(), sendMessageEvent.getMessage());
                    break;

                case FSMActionType.DEBUG_MESSAGE:
                    DebugAction debugAction = (DebugAction) action;
                    MessageEvent debugMessageEvent = (MessageEvent) evt;
                    owner.sendMessage(getCharacterIndex(), debugMessageEvent.getMessage());
                    break;

                default: //No action event
                    break;
            };
        }

        //update target transition
        this._currentState = transition.getTargetState();

	}

	@Override
	public void draw(Canvas canv) {
        //draw current game character
        canv.drawBitmap(getImage(), 0, 0, null);
	}


	public GameCharacterBase(
			GameEnginePreBase owner,
			int index,
			int x, int y, 
			int w, int h, 
			FSMState[] states, 
			Bitmap img) 
	{
        this._owner = owner;
        this._characterIndex = index;
        this._FSMStateTable = states;
        setImage(img);
        setX(x);
        setY(y);
        if(img!=null) {
            //to make sure the bitmap isn't clipped
            setW(img.getWidth());
            setH(img.getHeight());
        } else {
            setW(w);
            setH(h);
        }
	}

}
