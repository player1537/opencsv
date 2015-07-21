package au.com.bytecode.opencsv;

/**
 Copyright 2005 Bytecode Pty Ltd.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A very simple CSV reader released under a commercial-friendly license.
 *
 * @author Glen Smith
 */
public class CSVReader implements Closeable, Iterable<String[]> {

    protected final CSVParser parser;
    protected final int skipLines;
    private final BufferedReader br;
    private boolean hasNext = true;
    private boolean linesSkiped;

    /**
     * The default number of lines to skip when reading.
     */
    public static final int DEFAULT_SKIP_LINES = 0;

    /**
     * Constructs CSVReader using default parameters.
     *
     * @param reader the reader to an underlying CSV source.
     * @see CSVParser#CSVParser
     */
    public CSVReader(Reader reader) {
        this(reader, CSVParser.DEFAULT_SEPARATOR);
    }

    /**
     * Constructs CSVReader with supplied separator.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries.
     */
    public CSVReader(Reader reader, char separator) {
        this(reader, separator, CSVParser.DEFAULT_QUOTE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator and quote character.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quoteChar the character to use for quoted elements
     */
    public CSVReader(Reader reader, char separator, char quoteChar) {
        this(reader, separator, quoteChar, CSVParser.DEFAULT_ESCAPE_CHARACTER);
    }

    /**
     * Constructs CSVReader with supplied separator, quote character,
     * and strict quote behavior.
     *
     * @param reader       the reader to an underlying CSV source.
     * @param separator    the delimiter to use for separating entries
     * @param quoteChar    the character to use for quoted elements
     * @param strictQuotes sets if characters outside the quotes are ignored
     */
    public CSVReader(Reader reader, char separator, char quoteChar, boolean strictQuotes) {
        this(reader, separator, quoteChar, CSVParser.DEFAULT_ESCAPE_CHARACTER, DEFAULT_SKIP_LINES, strictQuotes);
    }

    /**
     * Constructs CSVReader with supplied separator, quote character, and escape
     * character.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quoteChar the character to use for quoted elements
     * @param escape    the character to use for escaping a separator or quote
     */

    public CSVReader(Reader reader, char separator, char quoteChar, char escape) {
        this(reader, separator, quoteChar, escape, DEFAULT_SKIP_LINES, CSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader with supplied separator, quote character, and number
     * of lines to skip.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quoteChar the character to use for quoted elements
     * @param line      the line number to skip for start reading
     */
    public CSVReader(Reader reader, char separator, char quoteChar, int line) {
        this(reader, separator, quoteChar, CSVParser.DEFAULT_ESCAPE_CHARACTER, line, CSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader with supplied separator, quote character, escape
     * character, and number of lines to skip.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param separator the delimiter to use for separating entries
     * @param quoteChar the character to use for quoted elements
     * @param escape    the character to use for escaping a separator or quote
     * @param line      the line number to skip for start reading
     */
    public CSVReader(Reader reader, char separator, char quoteChar, char escape, int line) {
        this(reader, separator, quoteChar, escape, line, CSVParser.DEFAULT_STRICT_QUOTES);
    }

    /**
     * Constructs CSVReader with supplied separator, quote character, escape
     * character, number of lines to skip, and strict quote behavior.
     *
     * @param reader       the reader to an underlying CSV source.
     * @param separator    the delimiter to use for separating entries
     * @param quoteChar    the character to use for quoted elements
     * @param escape       the character to use for escaping a separator or quote
     * @param line         the line number to skip for start reading
     * @param strictQuotes sets if characters outside the quotes are ignored
     */
    public CSVReader(Reader reader, char separator, char quoteChar, char escape, int line, boolean strictQuotes) {
        this(reader, separator, quoteChar, escape, line, strictQuotes, CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
    }

    /**
     * Constructs CSVReader with supplied separator, quote character, escape
     * character, number of lines to skip, strict quote behavior, and leading
     * whitespace behavior.
     *
     * @param reader                  the reader to an underlying CSV source.
     * @param separator               the delimiter to use for separating entries
     * @param quoteChar               the character to use for quoted elements
     * @param escape                  the character to use for escaping a separator or quote
     * @param line                    the line number to skip for start reading
     * @param strictQuotes            sets if characters outside the quotes are ignored
     * @param ignoreLeadingWhiteSpace it true, parser should ignore white space before a quote in a field
     */
    public CSVReader(Reader reader, char separator, char quoteChar, char escape, int line, boolean strictQuotes, boolean ignoreLeadingWhiteSpace) {
        this(reader,
             line,
             new CSVParser(separator, quoteChar, escape, strictQuotes, ignoreLeadingWhiteSpace));
    }

    /**
     * Constructs CSVReader with the supplied number of lines to skip and
     * {@link CSVParser} to use.
     *
     * @param reader    the reader to an underlying CSV source.
     * @param line      the line number to skip for start reading
     * @param csvParser the parser to use to parse input
     */
    public CSVReader(Reader reader, int line, CSVParser csvParser) {
        this.br =
                (reader instanceof BufferedReader ?
                        (BufferedReader) reader :
                        new BufferedReader(reader));
        this.skipLines = line;
        this.parser = csvParser;
    }

    /**
     * Reads the entire file into a List with each element being a String[] of
     * tokens.
     *
     * @return a List of String[], with each String[] representing a row of the
     *         file.
     * @throws IOException if bad things happen during the read
     */
    public List<String[]> readAll() throws IOException {

        List<String[]> allElements = new ArrayList<String[]>();
        while (hasNext) {
            String[] nextLineAsTokens = readNext();
            if (nextLineAsTokens != null)
                allElements.add(nextLineAsTokens);
        }
        return allElements;

    }

    /**
     * Reads the next line from the buffer and converts to a string array.
     *
     * @return a string array with each value from the row.
     * @throws IOException if bad things happen during the read
     */
    public String[] readNext() throws IOException {

        String[] result = null;
        do {
            String nextLine = getNextLine();
            if (!hasNext) {
                return result; // should throw if still pending?
            }
            String[] r = parser.parseLineMulti(nextLine);
            if (r.length > 0) {
                if (result == null) {
                    result = r;
                } else {
                    String[] t = new String[result.length + r.length];
                    System.arraycopy(result, 0, t, 0, result.length);
                    System.arraycopy(r, 0, t, result.length, r.length);
                    result = t;
                }
            }
        } while (parser.isPending());
        return result;
    }

    /**
     * Reads the next line from the file.
     *
     * @return the next line from the file without trailing newline
     * @throws IOException if bad things happen during the read
     */
    private String getNextLine() throws IOException {
        if (!this.linesSkiped) {
            for (int i = 0; i < skipLines; i++) {
                br.readLine();
            }
            this.linesSkiped = true;
        }
        String nextLine = br.readLine();
        if (nextLine == null) {
            hasNext = false;
        }
        return hasNext ? nextLine : null;
    }

    /**
     * Closes the underlying reader.
     *
     * @throws IOException if the close fails
     */
    public void close() throws IOException {
        br.close();
    }

    /**
     * Returns an iterator of rows.
     *
     * @see CSVIterator
     */
    public Iterator<String[]> iterator() {
        try {
            return new CSVIterator(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
