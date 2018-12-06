package gui;

import extractors.Extractor;
import org.jsoup.Jsoup;
import packer.Packer;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.IntStream;

public class MainFrame extends JFrame {

    private HashMap<Extractor.Element, Boolean> elements;
    private JList list;
    private String resourcePath = "src\\main\\resources";
    String dataPath = "src\\main\\data";

    public MainFrame(){
        super("Book Packing Application");
        elements = new HashMap<>();
        Box box = Box.createHorizontalBox();
        box.add(elementPanel());
        box.add(filePanel());
        box.add(indexButton());
        box.add(packingPanel());
        super.setSize(600,400);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.add(box);
        super.setVisible(true);
    }

    /**
     * Builds a panel with all the available possible elements to be extracted
     * @return a JPanel with checkboxes indicating possible elements to be extracted
     */
    private JPanel elementPanel(){
        JPanel panel = new JPanel();
        Box box = Box.createVerticalBox();
        EnumSet.allOf(Extractor.Element.class).stream()
                .map(this::checkBoxFromElement).forEach((box::add));
        panel.add(box);
        return panel;
    }

    /**
     * Creates a checkbox with a listener for an Extractor Element
     * @param element The element for which the checkbox is being built for
     * @return A constructed checkbox of the element
     */
    private JCheckBox checkBoxFromElement(Extractor.Element element){
        JCheckBox cb = new JCheckBox(element.name());
        elements.put(element,false);
        cb.addActionListener((event)->elements.put(element, cb.isSelected()));
        return cb;
    }

    /**
     * A panel with a list of the possible files that are accessible to be index
     * @return The constructed File panels
     */
    @SuppressWarnings("unchecked")
    private JPanel filePanel(){
        File dir = new File(resourcePath);
        String[] fileList = Arrays.stream(dir.listFiles()).map(File::getName).toArray(String[]::new);
        list = new JList(fileList);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JPanel panel = new JPanel();
        panel.add(list);
        return panel;
    }

    /**
     * @return Button that call the the indexing method
     */
    private JButton indexButton(){
        JButton button = new JButton("Index Files");
        button.addActionListener(e -> index());
        return button;
    }

    private JPanel packingPanel(){
        JPanel panel = new JPanel();
        Box box = Box.createVerticalBox();
        JLabel label = new JLabel("How many boxes?");
        JList<Integer> list = new JList(IntStream.rangeClosed(13,20).boxed().toArray());
        list.setSelectedIndex(0);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        JButton button = new JButton("Pack");
        button.addActionListener((e)-> pack(list));
        box.add(label);
        box.add(list);
        box.add(button);
        panel.add(box);
        return panel;
    }

    private void pack(JList<Integer> list){
        JFileChooser fc = new JFileChooser();
        fc.showDialog(new JFrame(), "Save");
        File f = fc.getSelectedFile();
        String target = f.getAbsolutePath();
        new Packer(dataPath + "\\data.csv", list.getSelectedValue(), target +".json");
    }

    /**
     * This method extracts the selected elements from the selected files and saves them into
     */
    @SuppressWarnings("unchecked")
    private void index(){
        ArrayList<Extractor.Element> elements = new ArrayList<>();
        ArrayList<Map<Extractor.Element, String>> indexed = new ArrayList<>();
        for(Extractor.Element element: EnumSet.allOf(Extractor.Element.class)){
            if (this.elements.get(element)){
                elements.add(element);
            }
        }
        Extractor.Element[] elements1 = new Extractor.Element[elements.size()];
        IntStream.range(0,elements1.length).forEach(i->elements1[i]=elements.get(1));
        String[] files = (String[]) list.getSelectedValuesList().stream().map(i-> resourcePath+"\\"+i).toArray(String[]::new);
        Arrays.stream(files)
                .map(this::getHTML)
                .map(html-> Extractor.getElements(elements1, html, Extractor.Source.Amazon))
                .forEach(indexed::add);
        indexed.forEach(map -> System.out.println(Arrays.toString(map.entrySet().toArray())));
        toCSV(indexed);
    }

    private void toCSV(ArrayList<Map<Extractor.Element, String>> indexed){
        Set<Extractor.Element> elements = EnumSet.allOf(Extractor.Element.class);
        String eol = System.getProperty("line.separator");


        try(Writer writer = new FileWriter(dataPath + "\\data.csv")) {
            for (Extractor.Element e : elements){
                writer.append(e.name()).append(";");
            }
            writer.append(eol);
            for (Map<Extractor.Element,String> map : indexed){
                elements.stream().map(map::get).forEach(s->{
                    try {
                        writer.append(s);
                        writer.append(";");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                writer.append(eol);
            }
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * @param file the file path
     * @return a String version of the html file
     */
    private String getHTML(String file){
        try {
            return Jsoup.parse(new File(file), "UTF-8").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
