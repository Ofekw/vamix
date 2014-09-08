package model;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * A JTextField that accepts only integers. To be used in the Extract Pane.
 * 
 */
public class IntegerField extends JTextField {

	NumbersDocument doc;

	public IntegerField() {
		super();
	}

	public IntegerField(int cols) {
		super(cols);
	}

	@Override
	protected Document createDefaultModel() {
		doc = new NumbersDocument(2);
		return doc;
	}

	public void setNumberRestriction(boolean set) {
		NumbersDocument._inputRestriction = set;
	}

	static class NumbersDocument extends PlainDocument {
		public static boolean _inputRestriction = false;
		private int limit;

		NumbersDocument(int limit) {
			super();
			this.limit = limit;
		}

		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {

			if (str == null) {
				return;
			}

			if (str == null)
				return;

			char[] chars = str.toCharArray();
			boolean ok = true;

			for (int i = 0; i < chars.length; i++) {

				try {
					Integer.parseInt(String.valueOf(chars[i]));
				} catch (NumberFormatException exc) {
					ok = false;
					break;
				}

			}

			if (ok)
				if ((getLength() + str.length()) <= limit) {
					super.insertString(offs, new String(chars), a);
				}

		}
	}

}