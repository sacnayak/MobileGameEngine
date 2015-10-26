package edu.cmu.ssnayak.mobilegameengine.event;



/**
 *
 */
public class AnimMoveEvent extends XYEvent {
	public AnimMoveEvent(float x, float y) {
		super(FSMEventType.ANIM_MOVE, x,y);
	}
}
