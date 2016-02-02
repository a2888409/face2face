package gate.starter;
import gate.GateServer;
import org.apache.commons.cli.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created by Qzy on 2016/1/28.
 */

public class GateStarter {
    private static final Logger logger = LoggerFactory.getLogger(GateStarter.class);
    static File cfg = null;
    static File log = null;
    static int gateId = 0;
    static int port = 0;

    public static void main(String[] args) throws Exception {
        applyConfigurations(args);

        GateServer.startGateServer(port);
    }

    static void applyConfigurations(String[] args) throws ParseException {
        parseArgs(args);

        //init cfg
        try {
            DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            Document doc = builder.parse(cfg);
            Element rootElement = doc.getDocumentElement();

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = null;
            NodeList nodeList = null;
            Element element = null;

            xPathExpression  = xPath.compile("/gate/id");
            nodeList = (NodeList)xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element)nodeList.item(0);
            gateId = Integer.parseInt(element.getAttribute("value"));
            logger.info("gate id " + gateId);

            xPathExpression  = xPath.compile("/gate/gateserver");
            nodeList = (NodeList)xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element)nodeList.item(0);
            port = Integer.parseInt(element.getAttribute("port"));
            logger.info("gateserver port " + port);
        } catch (Exception e) {
            logger.error("init cfg error");
            e.printStackTrace();
        }
        //init log
    }

    static void parseArgs(String[] args) throws ParseException {
        // Create a Parser
        CommandLineParser parser = new BasicParser( );
        Options options = new Options( );
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("c", "cfg", true, "config Absolute Path");
        options.addOption("l", "log", true, "log configuration");

        // Parse the program arguments
        CommandLine commandLine = parser.parse( options, args );
        // Set the appropriate variables based on supplied options

        if( commandLine.hasOption('h') ) {
            printHelpMessage();
            System.exit(0);
        }
        if( commandLine.hasOption('c') ) {
            cfg = new File(commandLine.getOptionValue('c'));
        } else {
            printHelpMessage();
            System.exit(0);
        }
        if( commandLine.hasOption('l') ) {
            log = new File(commandLine.getOptionValue('l'));
        } else {
            printHelpMessage();
            System.exit(0);
        }
    }

    static void printHelpMessage() {
        System.out.println( "Change the xml File and Log.XML Path to right Absolute Path base on your project Location in your computor");
        System.out.println("Usage example: ");
        System.out.println( "java -cfg D:\\MyProject\\face2face\\gate\\src\\main\\resources\\gate.xml  -log D:\\MyProject\\face2face\\gate\\src\\main\\resources\\log.xml");
        System.exit(0);
    }

    public static int getGateId(){
        return gateId;
    }

}
