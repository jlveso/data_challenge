package extractors;

/**
 * An interface to wrap all extractors and add more to Extractor if necessary
 */
public abstract class AbstractExtractor {

    protected String document;
    protected Extractor.Source source;

    /**
     * @param document Document where extraction will occur
     * @param source The source of the document in order to facilitate the extraction logic
     */
    AbstractExtractor(String document, Extractor.Source source){
        this.source = source;
        this.document = document;
    }

    /**
     * Method handles the processing of the document depending on its source
     * @return return the element as a String
     */
    public String getElement() {
        String element="";
        switch (source){
            case Amazon: element = amazonExtraction();
                break;
            case Other: element = otherExtraction();
        }
        return element;
    }

    /**
     * @return The source of the website where the extraction is occurring
     */
    public Extractor.Source getSource(){
        return source;
    }

    /**
     * @return The type of extractor in Extractor.Element form
     */
    protected abstract Extractor.Element getElementType();

    /**
     * Logic for extracting element from Amazon source
     * @return Extracted element from source
     */
    protected abstract String amazonExtraction();

    /**
     * Logic for extracting element when source is unknown or there is not defined logic
     * @return Extracted element from source
     */
    protected abstract String otherExtraction();

}
