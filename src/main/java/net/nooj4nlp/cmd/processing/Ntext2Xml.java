package net.nooj4nlp.cmd.processing;

import java.io.PrintWriter;
import java.util.List;

import net.nooj4nlp.engine.Language;
import net.nooj4nlp.engine.Ntext;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.common.collect.Lists;

public class Ntext2Xml {
	private final List<Object> annotations;
	private final List<String> xmlAnnotations;
	private final boolean filterOut;
	private final Language language;

	public Ntext2Xml(List<Object> annotations,
			List<String> xmlAnnotations,
			Language language,
			boolean filterOut) {
				this.annotations = annotations;
				this.xmlAnnotations = xmlAnnotations;
				this.language = language;
				this.filterOut = filterOut;
	}
	
	public String convert(Ntext nText) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		PrintWriter xmlWriter = new PrintWriter(byteStream);

		writeNtext(xmlWriter, nText);

		xmlWriter.close();

		return byteStream.toString();
	}

	private void writeNtext(PrintWriter xmlWriter, Ntext nText) {
		int currentLineStart = 0;
		for (int textUnit = 1; textUnit <= nText.nbOfTextUnits; textUnit++) {
			currentLineStart = writeLine(xmlWriter, nText, currentLineStart, textUnit);
		}
		
		if (!filterOut) {
			xmlWriter.write(nText.buffer.substring(currentLineStart));
		}
	}

	private int writeLine(PrintWriter xmlWriter, Ntext text, int currentAddress, int textUnit) {
		int lineStart = text.mft.tuAddresses[textUnit];
		int lineEnd = text.mft.tuLengths[textUnit] + lineStart;
		
		if (currentAddress < lineStart && !filterOut) {
				xmlWriter.write(text.buffer.substring(currentAddress, lineStart));
		}
		
		String currentLine = text.buffer.substring(lineStart, lineEnd);
		
		text.buildXmlTaggedText(currentLine,
				0,
				filterOut,
				xmlWriter,
				textUnit,
				Lists.newArrayList(annotations),
				xmlAnnotations.toArray(new String[0]),
				language,
				false);
		
		return lineEnd;
	}
}
