package net.nooj4nlp.cmd.processing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;

import net.nooj4nlp.engine.Engine;
import net.nooj4nlp.engine.Ntext;
import net.nooj4nlp.engine.RefObject;

public final class LexicalAnalyzer implements NtextProcessor {
	private final Engine engine;

	public LexicalAnalyzer(Engine engine) {
		this.engine = checkNotNull(engine);
	}

	@Override
	public void process(Ntext nText) {
		RefObject<String> errorMessage = new RefObject<>("");
		
		nText.hUnknowns = new HashMap<>();
		boolean success = engine.tokenize(
				nText,
				nText.annotations,
				new HashMap<String, ArrayList<String>>(), 
				errorMessage);
		
		if (!success) {
			throw new LexicalAnalysisException(errorMessage.argvalue);
		}
	}
	
	@SuppressWarnings("serial")
	public static final class LexicalAnalysisException extends RuntimeException {
		private LexicalAnalysisException(String message) {
			super(message);
		}
	}
}
