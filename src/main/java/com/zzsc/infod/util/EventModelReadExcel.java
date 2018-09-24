package com.zzsc.infod.util;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
public class EventModelReadExcel {
    // 开始读数据的行数
    private int beginRow;
    // 结束读数据的行数
    private int endRow;
    // 所有值列表
    private List<List<Object>> allValueList = new ArrayList<>();

    public EventModelReadExcel(int beginRow) {
        this.beginRow = beginRow;
    }

    public EventModelReadExcel(int beginRow, int rows) {
        this.beginRow = beginRow;
        this.endRow = this.beginRow + rows - 1;
    }

    public void processOneSheet(File file) throws Exception {
        InputStream sheet = null;
        try {
            OPCPackage pkg = OPCPackage.open(file);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst);
            sheet = r.getSheet("rId1");
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
        } catch (Exception e) {
            throw e;
        } finally {
            if (sheet != null) {
                sheet.close();
            }
        }
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    public List<List<Object>> getAllValueList() {
        return allValueList;
    }

    private class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean isString;
        private boolean validRow;
        // 一行的所有数据
        private List<Object> rowValueList;

        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }

        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if (name.equals("row") || name.equals("c")) {
                int column = getColumn(attributes);
                if (column < beginRow || (endRow > 0 && column > endRow)) {
                    validRow = false;
                } else {
                    validRow = true;
                    if (name.equals("row")) {
                        rowValueList = new ArrayList<>();
                        allValueList.add(rowValueList);
                    }
                    String cellType = attributes.getValue("t");
                    if (cellType != null && cellType.equals("s")) {
                        isString = true;
                    } else {
                        isString = false;
                    }
                }
            }
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name) throws SAXException {
            if (validRow) {
                if (isString) {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    isString = false;
                    validRow = false;
                    if (name.equals("v")) {
                        rowValueList.add(lastContents);
                    }
                } else {
                    if (name.equals("c")) {
                        rowValueList.add(lastContents);
                    }
                }
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            lastContents += new String(ch, start, length);
        }

        private int getColumn(Attributes attributes) {
            String row = attributes.getValue("r");
            int firstDigit = -1;
            for (int c = 0; c < row.length(); ++c) {
                if (Character.isDigit(row.charAt(c))) {
                    firstDigit = c;
                    break;
                }
            }
            return Integer.valueOf(row.substring(firstDigit, row.length()));
        }
    }
}
