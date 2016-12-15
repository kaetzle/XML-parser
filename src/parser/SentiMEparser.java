package parser;

//import java.io.BufferedWriter;

import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
//import java.io.Reader;
//import java.io.StringReader;
//import java.io.UnsupportedEncodingException;
//import java.io.Writer;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.Scanner;
import java.util.Set;
import java.util.Scanner;


import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.filefilter.TrueFileFilter;
//import org.apache.commons.lang3.StringEscapeUtils;
//import org.apache.commons.lang3.StringUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;


//import jdk.internal.org.xml.sax.InputSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;


//import weka.core.Instances;

class SentiMEparser {

    private Set<String> id_cache = new HashSet<String>();
    private boolean SCORE;
    private int FOLDER;
    private File result_storage;
    private PrintStream result_stream;


    protected Node getNode(String tagName, NodeList nodes) {
        for ( int x = 0; x < nodes.getLength(); x++ ) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }

    protected String getNodeAttr(String attrName, Node node ) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++ ) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue();
            }
        }
        return "";
    }


    //Creating .tsv file containing all the elements needed from .xml files (ESWC2016 challenge)
    public void createTrainingDataset(String path) throws IOException {
        File file = new File("ressource/XML/" + path + ".xml");
        String[] extensions = new String[]{"xml"};
        //List<File> files = (List<File>) FileUtils.listFiles(folder, extensions, true);
        //for (File file : files)
        //  if (file.isFile() && file.getName().endsWith(".xml")) {
        System.out.println("file has been opened: " + file.getCanonicalPath());
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            NodeList nList = doc.getElementsByTagName("sentence");
           // NodeList nSubList = doc.getElementsByTagName("opinion");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Element eElement = (Element) nList.item(temp);
                String str;
                //on recupere le tweet
                str = eElement.getElementsByTagName("text").item(0).getTextContent();
                str = str.replaceAll("(\\r|\\n|\\t)", "");

                if (eElement.getNodeType() == Node.ELEMENT_NODE) {

                    String pos1, pos2, polarity;

                    pos1 = eElement.getAttribute("to");
                    pos2 = eElement.getAttribute("from");
                    polarity = eElement.getAttribute("polarity");


               /* for (int tmp = 0; tmp < nSubList.getLength(); tmp++) {


*/
                    // on crée un nouveau fichier texte
                    File tsv = new File("ressource/TSV/" + path + ".tsv");
                    try {
                        if (tsv.exists() == false) {
                            System.out.println("We had to make a new file.");
                            tsv.createNewFile();
                        }
                        PrintWriter out = new PrintWriter(new FileWriter(tsv, true));
                        out.print("");

                        // add transformation of ID

                        out.append(eElement.getAttribute("id") + "\t" + eElement.getAttribute("id") + "\t" + pos1 + "\t" + pos2 + "\t" + polarity + "\t" + str + "\n");

                        out.close();
                    } catch (IOException e) {
                        System.out.println("COULD NOT LOG!!");
                    }
                }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] path) throws IOException {

        Scanner sc = new Scanner(System.in);
        SentiMEparser parser = new SentiMEparser();
        System.out.println("Enter the name of the file to convert: ");
        String name = sc.nextLine();
        File file = new File("ressource/TSV/" + name + ".tsv"); // General case
        parser.createTrainingDataset(name); //Creating the .tsv file from the folders of .xml files
        System.out.println("The tsv created from the xml file, opened " + file.getCanonicalPath());

    }
}

