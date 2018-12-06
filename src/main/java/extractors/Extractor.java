package extractors;


import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class containing the elements to handle the extraction task
 */
public class Extractor {

    /**
     * Possible elements to be extracted
     */
    public enum Element{
        Author, Title, ISBN_10, Price, Weight,
    }

    /**
     * Possible sources where extraction can occur
     */
    public enum Source{
        Amazon, Other
    }

    /**
     * Ensure no instance of this call can be initiated
     */
    private Extractor(){}

    /**
     * Performs extraction of elements on a given document, taking into consideration the source
     * @param elements Elements to be extracted
     * @param document Document where extraction will occur
     * @param source The source of the document to handle the logic of the extraction
     * @return A map of the Element to the extracted String
     */
    public static Map<Element, String> getElements(Extractor.Element[] elements, String document, Source source){
        return Arrays.stream(elements).map(element -> getExtractor(element, document, source))
                .collect(Collectors.toMap(AbstractExtractor::getElementType, AbstractExtractor::getElement));
    }

    /**
     *
     * @param element element to be extracted from document
     * @param document document where extraction will be performed
     * @param source source of the document
     * @return Concrete extractor for element
     */
    private static AbstractExtractor getExtractor(Extractor.Element element, String document, Source source){
        switch (element) {
            case Author:
                return new AuthorExtractor(document, source);
            case Title:
                return new TitleExtractor(document, source);
            case Price:
                return new PriceExtractor(document, source);
            case Weight:
                return new WeightExtractor(document, source);
            case ISBN_10:
                return new ISBN10Extractor(document, source);
        }
        return new AbstractExtractor(document, source) {
            @Override
            protected Element getElementType() {
                return null;
            }

            @Override
            protected String amazonExtraction() {
                return null;
            }

            @Override
            protected String otherExtraction() {
                return null;
            }
        };
    }


}
