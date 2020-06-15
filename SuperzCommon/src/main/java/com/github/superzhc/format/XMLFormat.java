package com.github.superzhc.format;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * 2020年04月16日 superz add
 */
public class XMLFormat implements IFormat
{
    @Override
    public String format(String str) {
        byte[] data = null;
        StringReader stringReader = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            stringReader = new StringReader(str);
            InputSource is = new InputSource(stringReader);
            Document doc = db.parse(is);
            data = outPut(doc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (stringReader != null) {
                stringReader.close();
            }
        }
        return new String(data);
    }

    public static byte[] outPut(Node node) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        byte[] data = null;
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 3.0");
            LSSerializer serializer = impl.createLSSerializer();

            DOMConfiguration domConfiguration = serializer.getDomConfig();
            boolean isSupport = domConfiguration.canSetParameter("format-pretty-print", true);
            if (isSupport) {
                domConfiguration.setParameter("format-pretty-print", true);
            }

            LSOutput output = impl.createLSOutput();
            output.setEncoding("UTF-8");
            byteArrayOutputStream = new ByteArrayOutputStream();
            output.setByteStream(byteArrayOutputStream);
            serializer.write(node, output);
            data = byteArrayOutputStream.toByteArray();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}

