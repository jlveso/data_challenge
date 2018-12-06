package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeightExtractor extends AbstractExtractor {

    /**
     * Regex Pattern to find the shipping weight in amazon source
     */
    private final Pattern amazonPattern = Pattern.compile("Weight:.*pounds");

    /**
     * @param document Document where extraction will occur
     * @param source   The source of the document in order to facilitate the extraction logic
     */
    WeightExtractor(String document, Extractor.Source source) {
        super(document, source);
    }

    /**
     * @return The type of extractor in Extractor.Element form
     */
    @Override
    protected Extractor.Element getElementType() {
        return Extractor.Element.Weight;
    }

    /**
     * Logic for extracting element from Amazon source
     *
     * @return Extracted element from source
     */
    @Override
    protected String amazonExtraction() {
        Document doc = Jsoup.parse(document);
        String text = doc.select("div#detail-bullets").first().text();
        //System.out.println(text);
        Matcher m = amazonPattern.matcher(text);
        String weight = "";
        if (m.find()){
            weight = m.group(0);
            weight = weight.replace("Weight: ","").replace(" pounds", "");
        }
        return weight;
    }

    /**
     * Logic for extracting element when source is unknown or there is not defined logic
     *
     * @return Extracted element from source
     */
    @Override
    protected String otherExtraction() {
        //TODO:
        return null;
    }
}
