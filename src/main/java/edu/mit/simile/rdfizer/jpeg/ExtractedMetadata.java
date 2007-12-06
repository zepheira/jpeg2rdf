package edu.mit.simile.rdfizer.jpeg;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ExtractedMetadata {
	private HashMap<String, List> _metadata;
	
	public ExtractedMetadata() {
		_metadata = new HashMap<String, List>();
	}
	
	public void addPropertyValue(String prop, String val) {
		List<String> vals = getValues(prop);
		if (null == vals) {
			vals = new Vector<String>();
			vals.add(val);
			_metadata.put(prop, vals);
		} else {
			vals.add(val);
			_metadata.put(prop, vals);
		}
	}
	
	public Set<String> getProperties() {
		return _metadata.keySet();
	}
	
	public List<String> getValues(String prop) {
		return _metadata.get(prop);
	}
}
