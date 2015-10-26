/**
 * 
 */
package edu.cmu.ssnayak.mobilegameengine.eventmatch;

import edu.cmu.ssnayak.mobilegameengine.event.FSMEvent;

/**
 *
 */
public interface FSMEventMatcher {
	public boolean match(FSMEvent evt);
}
