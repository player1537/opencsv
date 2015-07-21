package au.com.bytecode.opencsv;

import java.io.IOException;
import java.util.Iterator;

public class CSVIterator implements Iterator<String[]> {
    private CSVReader reader;
    private String[] nextLine;

    /**
     * Create a new iterator to read through each row of a CSV file.
     *
     * @param reader The reader to iterate through
     * @throws IOException if there was an error reading the first line
     */
    public CSVIterator(CSVReader reader) throws IOException {
        this.reader = reader;
        nextLine = reader.readNext();
    }

    /**
     * Determine whether there is more data to read.
     *
     * @return true if there is more data
     * @see java.util.Iterator#hasNext
     */
    public boolean hasNext() {
        return nextLine != null;
    }

    /**
     * Get the next row of data.
     *
     * @return The next row of data
     * @see java.util.Iterator#next
     * @see CSVReader#readNext
     */
    public String[] next() {
        String[] temp = nextLine;
        try {
            nextLine = reader.readNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }

    /**
     * Unimplemented method.
     *
     * @see java.util.Iterator#remove
     */
    public void remove() {
        throw new UnsupportedOperationException("This is a read only iterator.");
    }
}
