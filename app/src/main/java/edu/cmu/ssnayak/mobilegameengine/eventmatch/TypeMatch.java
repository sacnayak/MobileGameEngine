package edu.cmu.ssnayak.mobilegameengine.eventmatch;

import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;

/**
 *
 */
public class TypeMatch implements FSMEventMatcher {

	protected int _typeToMatch = -1/* matches nothing */;
	public int getTypeToMatch() {return _typeToMatch;}
	
	@Override
	public boolean match(FSMEvent evt) {
		return evt != null && evt.getType() == _typeToMatch;
	}
	
	public TypeMatch(int typeToMatch) {
		_typeToMatch = typeToMatch;
	}
	
	public TypeMatch() {
		this(-1/* matches nothing */);
	}

}
