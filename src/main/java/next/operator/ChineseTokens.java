package next.operator;

import com.aliasi.lm.NGramProcessLM;
import com.aliasi.spell.CompiledSpellChecker;
import com.aliasi.spell.FixedWeightEditDistance;
import com.aliasi.spell.TrainSpellChecker;
import com.aliasi.spell.WeightedEditDistance;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ChineseTokens {

	CompiledSpellChecker mSpellChecker;

	Set<Character> mTrainingCharSet = new HashSet<Character>();
	Set<String> mTrainingTokenSet = new HashSet<String>();

	// parameter values
	public ChineseTokens(InputStream trainingInput, String mCharEncoding,
	                     int mMaxNGram,
	                     double mLambdaFactor,
	                     int mNumChars,
	                     int mMaxNBest,
	                     double mContinueWeight,
	                     double mBreakWeight) throws IOException, ClassNotFoundException {
		// System.out.println("CHINESE TOKENS");
		// System.out.println("    Data Directory=" + mDataDir);
		// System.out.println("    Train Corpus Name=" + mTrainingCorpusName);
		// System.out.println("    Test Corpus Name=" + mTestCorpusName);
		// System.out.println("    Char Encoding=" + mCharEncoding);
		// System.out.println("    Max N-gram=" + mMaxNGram);
		// System.out.println("    Lambda factor=" + mLambdaFactor);
		// System.out.println("    Num chars=" + mNumChars);
		// System.out.println("    Max n-best=" + mMaxNBest);
		// System.out.println("    Continue weight=" + mContinueWeight);
		// System.out.println("    Break weight=" + mBreakWeight);

		NGramProcessLM lm
				= new NGramProcessLM(mMaxNGram, mNumChars, mLambdaFactor);
		WeightedEditDistance distance
				= new ChineseTokenizing(mContinueWeight, mBreakWeight);
		TrainSpellChecker trainer
				= new TrainSpellChecker(lm, distance, null);
		// System.out.println("Training Zip File=" + trainingFile);
		try (ZipInputStream zipIn = new ZipInputStream(trainingInput)) {
			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
				String name = entry.getName();
				String[] lines = extractLines(zipIn, mTrainingCharSet, mTrainingTokenSet, mCharEncoding);
				for (int i = 0; i < lines.length; ++i)
					trainer.handle(lines[i]);
			}
		}

		// System.out.println("Compiling Spell Checker");
		mSpellChecker = (CompiledSpellChecker) AbstractExternalizable.compile(trainer);

		mSpellChecker.setAllowInsert(true);
		mSpellChecker.setAllowMatch(true);
		mSpellChecker.setAllowDelete(false);
		mSpellChecker.setAllowSubstitute(false);
		mSpellChecker.setAllowTranspose(false);
		mSpellChecker.setNumConsecutiveInsertionsAllowed(1);
		mSpellChecker.setNBest(mMaxNBest);
	}

	public String[] run(String input) {
		return testSpellChecker(input).split(" ");
	}

	String testSpellChecker(String reference) {
		return mSpellChecker.didYouMean(reference.replaceAll(" ", ""));
	}

	static void addTokChars(Set<Character> charSet,
	                        Set<String> tokSet,
	                        String line) {
		if (line.contains("  ")) {
			String msg = "Illegal double space.\n"
					+ "    line=/" + line + "/";
			throw new RuntimeException(msg);
		}
		String[] toks = line.split("\\s+");
		for (String tok : toks) {
			if (tok.length() == 0) {
				String msg = "Illegal token length= 0\n"
						+ "    line=/" + line + "/";
				throw new RuntimeException(msg);
			}
			tokSet.add(tok);
			for (int j = 0; j < tok.length(); ++j) {
				charSet.add(tok.charAt(j));
			}
		}
	}

	static String[] extractLines(InputStream in, Set<Character> charSet, Set<String> tokenSet,
	                             String encoding)
			throws IOException {

		ArrayList<String> lineList = new ArrayList<>();
		InputStreamReader reader = new InputStreamReader(in, encoding);
		BufferedReader bufReader = new BufferedReader(reader);
		String refLine;
		while ((refLine = bufReader.readLine()) != null) {
			String trimmedLine = refLine.trim() + " ";
			String normalizedLine = trimmedLine.replaceAll("\\s+", " ");
			lineList.add(normalizedLine);
			addTokChars(charSet, tokenSet, normalizedLine);
		}
		return lineList.toArray(new String[0]);
	}

	public static final class ChineseTokenizing
			extends FixedWeightEditDistance
			implements Compilable {

		static final long serialVersionUID = -756371L;

		private final double mBreakWeight;
		private final double mContinueWeight;

		public ChineseTokenizing(double breakWeight, double continueWeight) {
			mBreakWeight = breakWeight;
			mContinueWeight = continueWeight;
		}

		public double insertWeight(char cInserted) {
			return cInserted == ' ' ? mBreakWeight : Double.NEGATIVE_INFINITY;
		}

		public double matchWeight(char cMatched) {
			return mContinueWeight;
		}

		public void compileTo(ObjectOutput objOut) throws IOException {
			objOut.writeObject(new Externalizable(this));
		}

		private static class Externalizable extends AbstractExternalizable {
			static final long serialVersionUID = -756373L;
			final ChineseTokenizing mDistance;

			public Externalizable() {
				this(null);
			}

			public Externalizable(ChineseTokenizing distance) {
				mDistance = distance;
			}

			public void writeExternal(ObjectOutput objOut) throws IOException {
				objOut.writeDouble(mDistance.mBreakWeight);
				objOut.writeDouble(mDistance.mContinueWeight);
			}

			public Object read(ObjectInput objIn)
					throws IOException, ClassNotFoundException {

				double breakWeight = objIn.readDouble();
				double continueWeight = objIn.readDouble();
				return new ChineseTokenizing(breakWeight, continueWeight);
			}
		}
	}

}
