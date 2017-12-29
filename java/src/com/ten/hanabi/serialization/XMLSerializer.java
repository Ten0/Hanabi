package com.ten.hanabi.serialization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.ten.hanabi.core.*;
import com.ten.hanabi.core.clues.*;
import com.ten.hanabi.core.exceptions.*;
import com.ten.hanabi.core.plays.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.security.*;

public class XMLSerializer {

	private static XStream xstream = null;

	private static void init() {
		if(xstream != null)
			return;
		xstream = new XStream();

		xstream.addPermission(NoTypePermission.NONE);
		xstream.addPermission(NullPermission.NULL);
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);

		Class<?>[] allowedClasses = new Class[] { Hanabi.class, Player.class, Deck.class, Card.class, RuleSet.class,
				Variant.class, NumberClue.class, ColorClue.class, CluePlay.class, DiscardPlay.class, PlacePlay.class };
		xstream.allowTypes(allowedClasses);
		for(Class<?> c : allowedClasses) {
			xstream.alias(c.getSimpleName(), c);
		}

		// Ommit situation DP
		xstream.omitField(Variant.class, "situationDP");
	}

	public static void toXML(Object obj, String filename) throws IOException {
		init();
		String s = xstream.toXML(obj);
		Files.write(Paths.get(filename), s.getBytes());
	}

	public static Hanabi loadHanabi(String filename)
			throws XStreamException, InvalidDeckException, InvalidPlayException {
		return loadHanabi(new File(filename));
	}

	public static Hanabi loadHanabi(File file) throws XStreamException, InvalidDeckException, InvalidPlayException {
		init();
		Object o = xstream.fromXML(file);
		Hanabi h = (Hanabi) o;
		Deck d = h.getDeck();
		if(d.isLocked())
			d.checkCoherence();
		h.getVariant().getSituation(); // To check for invalid plays
		return h;
	}
}
