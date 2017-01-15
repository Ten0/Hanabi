package com.ten.hanabi.core.exceptions;

import com.ten.hanabi.core.clues.Clue;

public class InvalidClueException extends RuntimeException {

	public InvalidClueException(Clue c) { super(c.toString()); }
	public InvalidClueException(Clue c, Throwable e) { super(c.toString(), e); }
	public InvalidClueException(Clue c, String message) { super(c.toString()+": "+message); }
	public InvalidClueException(Clue c, String message, Throwable e) { super(c.toString()+": "+message, e); }
}
