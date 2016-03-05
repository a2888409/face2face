package logic.starter;

/**
 * Created by Dell on 2016/2/2.
 */

import logic.LogicServer;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import thirdparty.redis.utils.RedisPoolManager;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;



/**
 * Created by Qzy on 2016/1/28.
 */

public class LogicStarter {
    private static final Logger logger = LoggerFactory.getLogger(LogicStarter.class);
    private static File cfg = null;
    private static File log = null;
    public static RedisPoolManager _redisPoolManager;
    public static int workNum = 1;

    public static void main(String[] args) throws Exception {

        configureAndStart(args);

    }

    static void configureAndStart(String[] args) throws ParseException {
        parseArgs(args);

        try {
            //parse xml File and apply it
            DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            Document doc = builder.parse(cfg);
            Element rootElement = doc.getDocumentElement();

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = null;
            NodeList nodeList = null;
            Element element = null;


            xPathExpression  = xPath.compile("/logic/logicserver");
            nodeList = (NodeList)xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element)nodeList.item(0);
            int logicListenPort = Integer.parseInt(element.getAttribute("port"));
            logger.info("Logicserver logicListenPort " + logicListenPort);
            workNum = Integer.parseInt(element.getAttribute("workNum"));
            logic.Worker.startWorker(workNum);

            xPathExpression  = xPath.compile("/logic/redis");
            nodeList = (NodeList)xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element)nodeList.item(0);
            _redisPoolManager = new RedisPoolManager();
            _redisPoolManager.REDIS_SERVER = element.getAttribute("ip");
            _redisPoolManager.REDIS_PORT = Integer.parseInt(element.getAttribute("port"));

            _redisPoolManager.returnJedis(_redisPoolManager.getJedis());
            logger.info("Redis init successed");

            //Now Start Servers
            new Thread(() -> LogicServer.startLogicServer(logicListenPort)).start();

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
        System.out.println( "Change the xml File and Log.XML Path to the right Absolute Path base on your project Location in your computor");
        System.out.println("Usage example: ");
        System.out.println( "java -cfg D:\\MyProject\\face2face\\logic\\src\\main\\resources\\logic.xml  -log D:\\MyProject\\face2face\\logic\\src\\main\\resources\\log.xml");
        System.exit(0);
    }

}
