package com.ten.hanabi.core.exceptions;

import com.ten.hanabi.core.plays.Play;

public class InvalidPlayException extends Exception {

	public InvalidPlayException(Play p) {
		super(p.toString());
	}

	public InvalidPlayException(Play p, Throwable e) {
		super(p.toString(), e);
	}

	public InvalidPlayException(Play p, String message) {
		super(p.toString() + ": " + message);
	}

	public InvalidPlayException(Play p, String message, Throwable e) {
		super(p.toString() + ": " + message, e);
	}
}
