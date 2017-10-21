package Model.FileSystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** The {@code Model.FileSystem.FileReader} class represents a reader for files.
 */
public class FileReader implements Iterable<Collection<String>> {

    private String path;
    private String splitChar;
    private RandomAccessFile fileReader;

    /** Creates a {@code Model.FileSystem.FileReader} with the specified path for the data file and a specified split character.
     * @param path The data file's path.
     * @param splitChar The split character for splitting criteria.
     * @throws FileNotFoundException if the specified path is empty.
     */
    public FileReader(String path, String splitChar) throws FileNotFoundException {
        if(path == null) throw  new IllegalArgumentException("The path argument is null.");
        if(splitChar == null) throw new IllegalArgumentException("The split character argument is null.");
        this.path = path;
        this.fileReader = new RandomAccessFile(path, "r");
        this.splitChar = splitChar;
    }

    /** Creates a {@code Model.FileSystem.FileReader} with the specified path for the data file and a default split character: "#".
     * @param path The data file's path.
     * @throws FileNotFoundException if the specified path is empty.
     */
    public FileReader(String path) throws FileNotFoundException {
        this(path, "#");
    }

    /** Puts the file reader pointer to the beginning of the file.
     * @throws IOException if the pos parameter in seek method is less than 0 or if an I/O error occurs.
     */
    public void resetFilePointer() throws IOException {
        fileReader.seek(0L);
    }

    /**Returns a {@code Collection} with the values read from the line where file reader is pointing to.
     * @return a new {@code ArrayList<String>} with the string value of the values read from the line where file
     * reader is pointing to.
     * @throws IOException if an I/O error occurs.
     */
    public Collection<String> getLine() throws IOException {
        Collection<String> values = new ArrayList<String>();
        String line = fileReader.readLine();
        String[] valuesArray = line.split(splitChar);

        for (int i = 0; i < valuesArray.length; i++) {
            values.add(valuesArray[i]);
        }

        return values;
    }

    /**Returns a {@code Collection} with the values read from the specified line of the file.
     * The line is counted from the beginning of the file, number zero stands for the first line.
     * @param lineNumber the number of the line of the file.
     * @return a new {@code ArrayList<String>} with the string value of the values read from the specified line.
     * @throws IOException if an I/O error occurs.
     */
    public Collection<String> getLine(long lineNumber) throws IOException {
        if(lineNumber < 0) throw  new IndexOutOfBoundsException();
        Collection<String> values = new ArrayList<String>();
        while(--lineNumber != 0) {
            fileReader.readLine();
        }

        String line = fileReader.readLine();

        String[] valuesArray = line.split(splitChar);

        for (int i = 0; i < valuesArray.length; i++) {
            values.add(valuesArray[i]);
        }

        resetFilePointer();

        return values;
    }

    /**Returns false if there are more lines to read. Otherwise return true.
     * @return a boolean that represent if there are more lines to read (true) or not (false).
     * @throws IOException if an I/O error occurs.
     */
    public boolean feof() throws IOException {
        boolean isEof = true;
        long lastPointer = fileReader.getFilePointer();

        if(fileReader.readLine() != null)
            isEof = false;

        fileReader.seek(lastPointer);

        return isEof;
    }

    /**
     * Returns a {@code Model.FileSystem.FileReader} Custom Iterator, allows a {@code Model.FileSystem.FileReader} Object to be the target of
     * the "for-each loop" statement.
     * @return an {@code Iterator<Collection<String>>}.
     */
    @Override
    public Iterator<Collection<String>> iterator() {
        return new FileReaderIterator();
    }

    class FileReaderIterator implements Iterator<Collection<String>> {
        protected FileReaderIterator() {
            try {
                resetFilePointer();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        public boolean hasNext() {
            try {
                boolean hasNext = !feof();

                if(!hasNext)
                    resetFilePointer();

                return hasNext;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        public Collection<String> next() {
            try {
                return getLine();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
