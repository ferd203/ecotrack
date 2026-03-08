package org.talend.designer.codegen.translators.databases.dbspecifics.postgresql;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.List;

public class TPostgresqlOutputBulkBeginJava
{
  protected static String nl;
  public static synchronized TPostgresqlOutputBulkBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TPostgresqlOutputBulkBeginJava result = new TPostgresqlOutputBulkBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + "\t\tint nb_line_";
  protected final String TEXT_3 = " = 0;" + NL + "\t\t" + NL + "\t\tfinal Character OUT_DELIM_";
  protected final String TEXT_4 = " = ";
  protected final String TEXT_5 = ".charAt(0); " + NL + "\t\t" + NL + "\t\tfinal String OUT_DELIM_ROWSEP_";
  protected final String TEXT_6 = ";" + NL + "\t\t" + NL + "\t\tjava.io.File file_";
  protected final String TEXT_7 = "=new java.io.File(";
  protected final String TEXT_8 = ");" + NL + "\t\tfinal java.io.BufferedWriter out";
  protected final String TEXT_9 = " = new java.io.BufferedWriter(new java.io.OutputStreamWriter(" + NL + "        \t\tnew java.io.FileOutputStream(file_";
  protected final String TEXT_10 = ", ";
  protected final String TEXT_11 = "),";
  protected final String TEXT_12 = "));" + NL + "       \tresourceMap.put(\"out";
  protected final String TEXT_13 = "\", out";
  protected final String TEXT_14 = "); ";
  protected final String TEXT_15 = NL + "        \tchar escapeChar_";
  protected final String TEXT_16 = ";" + NL + "" + NL + "       \t\tchar textEnclosure_";
  protected final String TEXT_17 = ";" + NL + "" + NL + "\t        int escape_mode_";
  protected final String TEXT_18 = " = 0;  // 0: double quotes; 1: backslash" + NL + "    \t    if(escapeChar_";
  protected final String TEXT_19 = "=='\\\\'){" + NL + "        \t    escape_mode_";
  protected final String TEXT_20 = " = 1;" + NL + "\t        }else if(escapeChar_";
  protected final String TEXT_21 = " == textEnclosure_";
  protected final String TEXT_22 = "){" + NL + "    \t        escape_mode_";
  protected final String TEXT_23 = " = 0;" + NL + "        \t}else {" + NL + "            \tthrow new RuntimeException(\"The escape mode only support the '\\\\' or double text enclosure.\");" + NL + "\t        }";
  protected final String TEXT_24 = NL + "\t\t\tif(file_";
  protected final String TEXT_25 = ".length()==0){";
  protected final String TEXT_26 = NL + "    \t\t\t\tout";
  protected final String TEXT_27 = ".write(\"";
  protected final String TEXT_28 = "\");" + NL + "    \t\t\t";
  protected final String TEXT_29 = "out";
  protected final String TEXT_30 = ".write(OUT_DELIM_";
  protected final String TEXT_31 = ".toString());";
  protected final String TEXT_32 = NL + "\t    \t\tout";
  protected final String TEXT_33 = ".write(OUT_DELIM_ROWSEP_";
  protected final String TEXT_34 = ");" + NL + "\t    \t}";
  protected final String TEXT_35 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
        String filename = ElementParameterParser.getValueWithUIFieldKey(node,"__FILENAME__","FILENAME");
        
        String fieldSeparator = ElementParameterParser.getValueWithUIFieldKey(node,"__FIELDSEPARATOR__","FIELDSEPARATOR");
        
        String rowSeparator = ElementParameterParser.getValueWithUIFieldKey(node,"__ROWSEPARATOR__","ROWSEPARATOR");
        
        String encoding = ElementParameterParser.getValue(node,"__ENCODING__");

        String fileType = ElementParameterParser.getValue(node, "__FILETYPE__");
                
        String escapeChar = ElementParameterParser.getValue(node, "__ESCAPE_CHAR__");
        
        String textEnclosure = ElementParameterParser.getValue(node, "__TEXT_ENCLOSURE__");
        
        boolean isAppend = ("true").equals(ElementParameterParser.getValue(node,"__APPEND__"));
        
        boolean isIncludeHeader = ("true").equals(ElementParameterParser.getValue(node,"__INCLUDEHEADER__"));

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(fieldSeparator );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(rowSeparator );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(filename );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    stringBuffer.append( isAppend);
    stringBuffer.append(TEXT_11);
    stringBuffer.append( encoding);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    
    	if("CSVFILE".equals(fileType)){	

    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(escapeChar.length()==0||("").equals(escapeChar)?"'\\\\'" : escapeChar + ".charAt(0)");
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(textEnclosure.length()==0||("").equals(textEnclosure)?"'\"'" : textEnclosure + ".charAt(0)");
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    
		if(isIncludeHeader){

    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
        			List<IMetadataColumn> columns = metadata.getListColumns();
    			int sizeColumns = columns.size();
    			for (int i = 0; i < sizeColumns; i++) {
    				IMetadataColumn column = columns.get(i);
	    		
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_28);
    
    				if(i != sizeColumns - 1) {
    					
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    
    				}
    			}
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    
			}
		}
    }
}

    stringBuffer.append(TEXT_35);
    return stringBuffer.toString();
  }
}
