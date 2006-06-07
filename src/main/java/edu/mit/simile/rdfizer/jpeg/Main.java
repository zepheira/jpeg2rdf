package edu.mit.simile.rdfizer.jpeg;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

/**
 * @author Stefano Mazzocchi
 */

public class Main {

    static Logger logger;
        
    public static void main(String args[]) throws Exception {
        new Main().process(args);
    }

    public static void fatal(String msg) {
        System.err.println(msg);
        System.exit(1);
    }

    public static void fatal(String msg, Exception e) {
        System.err.println(msg + ": " + e.getMessage());
        e.printStackTrace(System.err);
        System.exit(1);
    }
        
    public void process(String[] args) throws Exception {

        File images = null;
        Writer output = null;

        Logger.getRootLogger().removeAllAppenders();
        PropertyConfigurator.configure(getClass().getResource("/log4j.properties"));
        logger = Logger.getLogger(Main.class);
        
        CommandLineParser parser = new PosixParser();

        Options options = new Options();
        options.addOption( "h", "help", false, "show this help screen" );
        
        try {
            CommandLine line = parser.parse(options, args);
            String[] clean_args = line.getArgs();
                                    
            if (line.hasOption("help") || clean_args.length < 2) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("jpeg2rdf image_folder output.n3", options);
                System.exit(1);
            }
            
            images = new File(clean_args[0]);
            if (!images.exists()) fatal("Image folder '" + images.getAbsolutePath() + "' doesn't exist");
            if (!images.canRead()) fatal("You don't have permision to read from the image folder '" + images.getAbsolutePath() + "'.");

            File outputFile = new File(clean_args[1]);
            output = new PrintWriter(new FileWriter(outputFile));
        } catch (Exception e) {
            fatal("Error found initializing", e);
        }		

        // ------------------------------------------------------------
        
        try {
            logger.info("Processing " + images);
            output.write("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n");
            output.write("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n");
            output.write("@prefix dc: <http://purl.org/dc/elements/1.1/> .\n");
            output.write("@prefix exif: <http://simile.mit.edu/2006/06/ontologies/exif#> .\n\n");
            processFolder(images, output);
            output.close();
         } catch (Exception e) {
            fatal("Error", e);
         }
    }
    
    void processFolder(File file, Writer writer) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                processFolder(files[i], writer);
            }
        } else {
            if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg")) {
                logger.info(" found jpeg file: '" + file.getAbsolutePath() + "'");
                try {
                    Metadata metadata = JpegMetadataReader.readMetadata(file);
                    writer.write("<" + file.toURI().toString() + ">\n");
                    Iterator i = metadata.getDirectoryIterator();
                    while (i.hasNext()) {
                        Directory directory = (Directory) i.next();
                        logger.info ("  found metadata group: '" + directory.getName() + "'");
                        Iterator j = directory.getTagIterator();
                        while (j.hasNext()) {
                            Tag tag = (Tag) j.next();
                            logger.info("   found metadata tag: '" + tag.getTagName() + "' -> '" + tag.getDescription() + "' [" + tag.getTagType() + "]");
                            String predicate = tag.getTagName().toLowerCase().replace(' ','_');
                            writer.write("  exif:" + predicate + " \"" + tag.getDescription() + "\" ;\n");
                        }
                    }
                    writer.write("  rdf:type exif:Image .\n\n");
                } catch (Exception e) {
                    logger.error("Error processing jpeg metadata for " + file.getAbsolutePath(), e);
                }
            }
        }
    }
}
