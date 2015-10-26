package edu.cmu.ssnayak.mobilegameengine.event;



/**
 *
 */
public class TouchPressEvent extends XYEvent {

	/**
	 * @param x
	 * @param y
	 */
	public TouchPressEvent(float x, float y) {
		super(FSMEventType.TOUCH_PRESS, x, y);
	}

}
