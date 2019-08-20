package cycles.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SAXHandler extends DefaultHandler {
    private Map<String, Set<String>> packages;
    private Map<String, Set<String>> classes;
    private Map<String, String> class_pack;
    private String currentPackage;
    private String currentClass;

    public SAXHandler() {
        super();
        packages = new HashMap<String, Set<String>>();
        classes = new HashMap<String, Set<String>>();
        class_pack = new HashMap<String, String>();
    }

    public Map<String, Set<String>> getPackages() {
        return packages;
    }

    public Map<String, Set<String>> getClasses() {
        return classes;
    }

    public Map<String, String> getClass_pack() {
        return class_pack;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("namespace")){
            processPackage(attributes);

        } else if (qName.equalsIgnoreCase("type")){
            processClass(attributes);

        } else if (qName.equalsIgnoreCase("depends-on")){
            //if (attributes.getValue("classification") == "uses")
                processDependency(attributes);
        }
        super.startElement(uri, localName, qName, attributes);
    }

    private void processPackage(Attributes attributes){
        currentPackage = attributes.getValue("name");
        packages.put(currentPackage, new HashSet<String>());
    }

    private void processClass(Attributes attributes){
        String className = attributes.getValue("name");

        if (className.contains("$"))
            currentClass = className.substring(0,className.indexOf('$'));
        else currentClass = className;

        packages.get(currentPackage).add(currentClass);
        if (!classes.containsKey(currentClass)) {
            classes.put(currentClass, new HashSet<String>());
            class_pack.put(currentClass, currentPackage);
        }

    }

    private void processDependency(Attributes attributes){
        String className = attributes.getValue("name");
            if (className.contains("$"))
                className = className.substring(0, className.indexOf('$'));

            classes.get(currentClass).add(className);

    }
}
