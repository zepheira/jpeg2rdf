package edu.mit.simile.rdfizer.jpeg;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import org.apache.log4j.Logger;

/**
 * @author Ryan Lee
 */

public class Extractor {
	public static String EXIF_NS = "http://simile.mit.edu/2006/06/ontologies/exif#";
	public static String RDF_NS  = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	public static ExtractedMetadata extractFromFile(File file) throws Exception {
    	return extractFromFile(file, null);
	}

	public static ExtractedMetadata extractFromFile(File file, Logger logger) throws Exception {
    	String uri = file.toURI().toString();
    	Metadata metadata = JpegMetadataReader.readMetadata(file);
    	return extractMetadata(metadata, uri, logger);
	}

	public static ExtractedMetadata extractFromStream(InputStream stream, String uri) throws Exception {
		return extractFromStream(stream, uri, null);
	}
	
	public static ExtractedMetadata extractFromStream(InputStream stream, String uri, Logger logger) throws Exception {
		Metadata metadata = JpegMetadataReader.readMetadata(stream);
		return extractMetadata(metadata, uri, logger);
	}
	
	static ExtractedMetadata extractMetadata(Metadata metadata, String uri) throws Exception {
		return extractMetadata(metadata, uri, (Logger) null);
	}
	
	static ExtractedMetadata extractMetadata(Metadata metadata, String uri, Logger logger) throws Exception {
		ExtractedMetadata propsVals = new ExtractedMetadata();
        for (Directory directory : metadata.getDirectories()) {
            if (null != logger) logger.info ("  found metadata group: '" + directory.getName() + "'");
            for (Tag tag : directory.getTags()) {
                if (null != logger) logger.info("   found metadata tag: '" + tag.getTagName() + "' -> '" + tag.getDescription() + "' [" + tag.getTagType() + "]");
                String predicate = EXIF_NS + tag.getTagName().toLowerCase().replaceAll("[ /()&<>]","_");
                propsVals.addPropertyValue(predicate, tag.getDescription());
            }
        }
        
        return propsVals;
	}
}
