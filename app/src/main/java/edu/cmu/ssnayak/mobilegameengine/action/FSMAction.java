
package edu.cmu.ssnayak.mobilegameengine.action;

/**
 * 
 *
 */
public abstract class FSMAction {
	protected int _type = FSMActionType.NO_ACTION;
	public int getType() {return _type;}
	
	public FSMAction(int typ) {
		_type = typ;
	}
	
	public FSMAction(){
		this(FSMActionType.NO_ACTION);
	}
}
