package edu.cmu.ssnayak.mobilegameengine.eventmatch;

import edu.cmu.ssnayak.mobilegameengine.event.ButtonPressedEvent;
import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;
import edu.cmu.ssnayak.mobilegameengine.event.FSMEventType;


/**
 *
 */
public class ButtonMatch extends TypeMatch {

	protected int _buttonToMatch = -1; /* matches nothing */
	public int getButtonToMatch() {return _buttonToMatch;}
	
	@Override
	public boolean match(FSMEvent evt) {
		if(evt != null && super.match(evt) && evt instanceof ButtonPressedEvent){
			ButtonPressedEvent bpe = (ButtonPressedEvent) evt;
			return bpe.getButtonNumber() == _buttonToMatch;
		}
		
		return false;
	}
	
	public ButtonMatch(int buttonToMatch) {
		super(FSMEventType.BUTTON_PRESSED);
		_buttonToMatch = buttonToMatch;
	}
	
	public ButtonMatch() {
		this(-1/* matches nothing */);
	}

}
