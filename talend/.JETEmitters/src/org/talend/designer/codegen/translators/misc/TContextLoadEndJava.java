package org.talend.designer.codegen.translators.misc;

import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IContextParameter;
import java.util.List;

public class TContextLoadEndJava
{
  protected static String nl;
  public static synchronized TContextLoadEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TContextLoadEndJava result = new TContextLoadEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = "\t" + NL + "\tjava.util.Enumeration<?> enu_";
  protected final String TEXT_3 = " = context.propertyNames();" + NL + "    while(enu_";
  protected final String TEXT_4 = ".hasMoreElements())" + NL + "    {           " + NL + "    \tString key_";
  protected final String TEXT_5 = " = (String)enu_";
  protected final String TEXT_6 = ".nextElement();" + NL + "        if(!assignList_";
  protected final String TEXT_7 = ".contains(key_";
  protected final String TEXT_8 = ") && !newPropertyList_";
  protected final String TEXT_9 = "))" + NL + "        {" + NL + "            noAssignList_";
  protected final String TEXT_10 = ".add(key_";
  protected final String TEXT_11 = ");" + NL + "        }          " + NL + "    }";
  protected final String TEXT_12 = NL + "\tfor(Object obj_";
  protected final String TEXT_13 = " :newPropertyList_";
  protected final String TEXT_14 = "){" + NL + "\t\t";
  protected final String TEXT_15 = NL + "\t\t\tString newLog_";
  protected final String TEXT_16 = " = \"";
  protected final String TEXT_17 = ": Parameter \\\"\" + obj_";
  protected final String TEXT_18 = " + \"\\\" is a new parameter of ";
  protected final String TEXT_19 = "\";" + NL + "\t\t\t";
  protected final String TEXT_20 = NL + "\t\t\t\tlog.error(newLog_";
  protected final String TEXT_21 = ");" + NL + "\t\t\t";
  protected final String TEXT_22 = NL + "\t\t\t\tlog.warn(newLog_";
  protected final String TEXT_23 = "\t" + NL + "\t\t\t\tlog.info(newLog_";
  protected final String TEXT_24 = NL + "\t\t";
  protected final String TEXT_25 = NL + "\t\tSystem.";
  protected final String TEXT_26 = ".println(\"";
  protected final String TEXT_27 = "\");";
  protected final String TEXT_28 = NL + "\t\t  throw new RuntimeException(\"";
  protected final String TEXT_29 = "\");" + NL + "\t\t";
  protected final String TEXT_30 = "        " + NL + "\t}";
  protected final String TEXT_31 = " :noAssignList_";
  protected final String TEXT_32 = NL + "\t\t\tString oldLog_";
  protected final String TEXT_33 = " + \"\\\" has not been set by ";
  protected final String TEXT_34 = NL + "\t\t\t\tlog.error(oldLog_";
  protected final String TEXT_35 = NL + "\t\t\t\tlog.warn(oldLog_";
  protected final String TEXT_36 = "\t" + NL + "\t\t\t\tlog.info(oldLog_";
  protected final String TEXT_37 = NL + "\t}";
  protected final String TEXT_38 = " " + NL + "" + NL + "    String newPropertyStr_";
  protected final String TEXT_39 = " = newPropertyList_";
  protected final String TEXT_40 = ".toString();" + NL + "    String newProperty_";
  protected final String TEXT_41 = " = newPropertyStr_";
  protected final String TEXT_42 = ".substring(1, newPropertyStr_";
  protected final String TEXT_43 = ".length() - 1);" + NL + "    " + NL + "    String noAssignStr_";
  protected final String TEXT_44 = " = noAssignList_";
  protected final String TEXT_45 = ".toString();" + NL + "    String noAssign_";
  protected final String TEXT_46 = " = noAssignStr_";
  protected final String TEXT_47 = ".substring(1, noAssignStr_";
  protected final String TEXT_48 = ".length() - 1);" + NL + "    " + NL + "    globalMap.put(\"";
  protected final String TEXT_49 = "_KEY_NOT_INCONTEXT\", newProperty_";
  protected final String TEXT_50 = ");" + NL + "    globalMap.put(\"";
  protected final String TEXT_51 = "_KEY_NOT_LOADED\", noAssign_";
  protected final String TEXT_52 = ");" + NL + "" + NL + "    globalMap.put(\"";
  protected final String TEXT_53 = "_NB_LINE\",nb_line_";
  protected final String TEXT_54 = ");" + NL + "" + NL + "\tList<String> parametersToEncrypt_";
  protected final String TEXT_55 = " = new java.util.ArrayList<String>();" + NL + "\t";
  protected final String TEXT_56 = NL + "\t\tparametersToEncrypt_";
  protected final String TEXT_57 = ".add(\"";
  protected final String TEXT_58 = NL + "\t" + NL + "\tresumeUtil.addLog(\"NODE\", \"NODE:";
  protected final String TEXT_59 = "\", \"\", Thread.currentThread().getId() + \"\", \"\",\"\",\"\",\"\",resumeUtil.convertToJsonText(context,parametersToEncrypt_";
  protected final String TEXT_60 = "));    ";
  protected final String TEXT_61 = NL + "    \tlog.info(\"";
  protected final String TEXT_62 = " - Loaded contexts count: \" + nb_line_";
  protected final String TEXT_63 = " + \".\");";
  protected final String TEXT_64 = NL + "    ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
    boolean disError=ElementParameterParser.getValue(node, "__DISABLE_ERROR__").equals("true");
    boolean disWarning=ElementParameterParser.getValue(node, "__DISABLE_WARNINGS__").equals("true");
    boolean bDieOnError = "true".equals(ElementParameterParser.getValue(node, "__DIEONERROR__"));
    boolean disInfo=ElementParameterParser.getValue(node, "__DISABLE_INFO__").equals("true");
    String newVariable = ElementParameterParser.getValue(node, "__LOAD_NEW_VARIABLE__");
    String oldVariable = ElementParameterParser.getValue(node, "__NOT_LOAD_OLD_VARIABLE__");
	boolean isLog4jEnabled = ("true").equals(ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    
	if( (newVariable.equals("Error") && disError==false) 
		|| (newVariable.equals("Warning") && disWarning==false)
		|| (newVariable.equals("Info") && disInfo==false) ){

    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_14);
    if(isLog4jEnabled){
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    if(newVariable.equals("Error")){
    stringBuffer.append(TEXT_20);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    }else if(newVariable.equals("Warning")){
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    }else{
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_21);
    }
    stringBuffer.append(TEXT_24);
    }
    stringBuffer.append(TEXT_25);
    stringBuffer.append("Error".equals(newVariable)?"err":"out");
    stringBuffer.append(TEXT_26);
    stringBuffer.append(newVariable);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    if (bDieOnError && newVariable.equals("Error")){
    stringBuffer.append(TEXT_28);
    stringBuffer.append(newVariable);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    }
    stringBuffer.append(TEXT_30);
    
	}
	if( (oldVariable.equals("Error") && disError==false) 
		|| (oldVariable.equals("Warning") && disWarning==false)
		|| (oldVariable.equals("Info") && disInfo==false) ){

    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    if(isLog4jEnabled){
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    if(oldVariable.equals("Error")){
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    }else if(oldVariable.equals("Warning")){
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    }else{
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    }
    stringBuffer.append(TEXT_24);
    }
    stringBuffer.append(TEXT_25);
    stringBuffer.append(oldVariable.equals("Error")?"err":"out");
    stringBuffer.append(TEXT_26);
    stringBuffer.append(oldVariable);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    if (bDieOnError && oldVariable.equals("Error")){
    stringBuffer.append(TEXT_28);
    stringBuffer.append(oldVariable);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    }
    stringBuffer.append(TEXT_37);
    
	}

    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_55);
    
	List<IContextParameter> params = node.getProcess().getContextManager().getDefaultContext().getContextParameterList();
	for(IContextParameter ctxParam :params) {
    	if ("id_Password".equals(ctxParam.getType())) {
		
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_57);
    stringBuffer.append(ctxParam.getName());
    stringBuffer.append(TEXT_29);
    
    	}
    }
	
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    if(isLog4jEnabled){
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    }
    stringBuffer.append(TEXT_64);
    return stringBuffer.toString();
  }
}
