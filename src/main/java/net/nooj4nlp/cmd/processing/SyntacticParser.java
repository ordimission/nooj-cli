package net.nooj4nlp.cmd.processing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

public final class SyntacticParser implements NtextProcessor {
	private final Engine engine;

	public SyntacticParser(Engine engine) {
		this.engine = checkNotNull(engine);
	}
	
	@Override
	public void process(Ntext nText) {
		RefObject<String> errorMessage = new RefObject<>("");
		
		if (nText.XmlNodes == null) {
			nText.hPhrases = new HashMap<>();
		}

		applySyntax(0, nText, errorMessage);
		nText.cleanupBadAnnotations(nText.annotations);
		applySyntax(1, nText, errorMessage);
	}
	
	private void applySyntax(int startingPoint, Ntext nText, RefObject<String> errorMessage) {
		if (engine.synGrms == null || engine.synGrms.size() <= 0) {
			return;
		}
		
		boolean success = false;
		try {
			success = engine.applyAllGrammars(
					nText,
					nText.annotations,
					startingPoint,
					errorMessage);
		} catch (ClassNotFoundException | IOException e) {
			throw new GrammarException(errorMessage.argvalue);
		}
		
		if (!success && errorMessage != null) {
			throw new SyntaxParsingException(errorMessage.argvalue);
		}
		
		if (!success) {
			nText.hPhrases = null;
		}
	}
	
	@SuppressWarnings("serial")
	public static final class GrammarException extends RuntimeException {
		private GrammarException(String message) {
			super(message);
		}
	}
	
	@SuppressWarnings("serial")
	public static final class SyntaxParsingException extends RuntimeException {
		private SyntaxParsingException(String message) {
			super(message);
		}
	}

}
