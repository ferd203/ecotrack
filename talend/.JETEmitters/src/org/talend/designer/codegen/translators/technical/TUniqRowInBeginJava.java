package org.talend.designer.codegen.translators.technical;

import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.List;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;

public class TUniqRowInBeginJava
{
  protected static String nl;
  public static synchronized TUniqRowInBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TUniqRowInBeginJava result = new TUniqRowInBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "            int bufferSizePerFile_";
  protected final String TEXT_2 = " = 10000;" + NL + "            " + NL + "            java.util.List<FileRowIterator_";
  protected final String TEXT_3 = "> rowFileList_1_";
  protected final String TEXT_4 = " = new java.util.ArrayList<FileRowIterator_";
  protected final String TEXT_5 = ">();" + NL + "            java.util.List<rowStruct_";
  protected final String TEXT_6 = "> rowList_1_";
  protected final String TEXT_7 = " = new java.util.ArrayList<rowStruct_";
  protected final String TEXT_8 = ">();" + NL + "" + NL + "            for (java.io.File file : files_1_";
  protected final String TEXT_9 = ") {" + NL + "                FileRowIterator_";
  protected final String TEXT_10 = " fri = new FileRowIterator_";
  protected final String TEXT_11 = "(file, bufferSizePerFile_";
  protected final String TEXT_12 = ");" + NL + "                rowFileList_1_";
  protected final String TEXT_13 = ".add(fri);" + NL + "                rowList_1_";
  protected final String TEXT_14 = ".add(fri.next());" + NL + "            }" + NL + "            " + NL + "            // comparator for second sort" + NL + "\t\t\tclass Comparator_2_";
  protected final String TEXT_15 = " implements Comparator<rowStruct_";
  protected final String TEXT_16 = "> {" + NL + "" + NL + "    \t\t\tpublic int compare(rowStruct_";
  protected final String TEXT_17 = " arg0, rowStruct_";
  protected final String TEXT_18 = " arg1) {" + NL + "        \t\t\treturn Long.compare(arg0.id_";
  protected final String TEXT_19 = ", arg1.id_";
  protected final String TEXT_20 = ");" + NL + "    \t\t\t}" + NL + "" + NL + "\t\t\t}" + NL + "\t\t\t" + NL + "            //For second sort init begin" + NL + "            int bufferSize_2_";
  protected final String TEXT_21 = " = bufferSize_";
  protected final String TEXT_22 = ";" + NL + "            rowStruct_";
  protected final String TEXT_23 = "[] buffer_2_";
  protected final String TEXT_24 = " = new rowStruct_";
  protected final String TEXT_25 = "[bufferSize_1_";
  protected final String TEXT_26 = "];" + NL + "            int rowsInBuffer_2_";
  protected final String TEXT_27 = " = 0;" + NL + "            Comparator<rowStruct_";
  protected final String TEXT_28 = "> comparator_2_";
  protected final String TEXT_29 = " = new Comparator_2_";
  protected final String TEXT_30 = "();" + NL + "" + NL + "            java.util.ArrayList<java.io.File> files_2_";
  protected final String TEXT_31 = " = new java.util.ArrayList<java.io.File>();" + NL + "            //For second sort init end" + NL + "" + NL + "            while (rowList_1_";
  protected final String TEXT_32 = ".size() > 0) {" + NL + "                int minIndex_";
  protected final String TEXT_33 = " = 0;" + NL + "                if (rowList_1_";
  protected final String TEXT_34 = ".size() > 1) {" + NL + "                    for (int i = 1; i < rowList_1_";
  protected final String TEXT_35 = ".size(); i++) {" + NL + "                        if (comparator_1_";
  protected final String TEXT_36 = ".compare(rowList_1_";
  protected final String TEXT_37 = ".get(minIndex_";
  protected final String TEXT_38 = "), rowList_1_";
  protected final String TEXT_39 = ".get(i)) > 0) {" + NL + "                            minIndex_";
  protected final String TEXT_40 = " = i;" + NL + "                        }" + NL + "                    }" + NL + "                }" + NL + "" + NL + "                // /////////////" + NL + "                if (rowsInBuffer_2_";
  protected final String TEXT_41 = " >= bufferSize_2_";
  protected final String TEXT_42 = ") {// buffer is full do sort and" + NL + "" + NL + "                    java.util.Arrays.<rowStruct_";
  protected final String TEXT_43 = "> sort(buffer_2_";
  protected final String TEXT_44 = ", 0, bufferSize_2_";
  protected final String TEXT_45 = ", comparator_2_";
  protected final String TEXT_46 = ");" + NL + "                    java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_47 = " + \"uniq_\" + files_2_";
  protected final String TEXT_48 = ".size());";
  protected final String TEXT_49 = NL + "\t\t\t\t\t\tlog.debug(\"";
  protected final String TEXT_50 = " - Invoke request to delete file: \"+file.getPath()+\" When VM exit.\");" + NL + "\t\t\t\t\t";
  protected final String TEXT_51 = NL + "                    file.deleteOnExit();" + NL + "                    java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                            new java.io.FileOutputStream(file)));" + NL + "                \t";
  protected final String TEXT_52 = " - Writing the data into: \"+file.getPath());" + NL + "\t\t\t\t\t";
  protected final String TEXT_53 = NL + "                    for (int i = 0; i < bufferSize_2_";
  protected final String TEXT_54 = "; i++) {" + NL + "                        buffer_2_";
  protected final String TEXT_55 = "[i].writeData(rw);" + NL + "                    }" + NL + "                    rw.close();" + NL + "\t\t\t\t\t";
  protected final String TEXT_56 = " - Wrote successfully.\");" + NL + "\t\t\t\t\t";
  protected final String TEXT_57 = NL + NL + "                    files_2_";
  protected final String TEXT_58 = ".add(file);" + NL + "" + NL + "                    rowsInBuffer_2_";
  protected final String TEXT_59 = " = 0;" + NL + "                }" + NL + "                rowStruct_";
  protected final String TEXT_60 = " minItem = rowList_1_";
  protected final String TEXT_61 = ");" + NL + "                buffer_2_";
  protected final String TEXT_62 = "[rowsInBuffer_2_";
  protected final String TEXT_63 = "++] = minItem;" + NL + "                FileRowIterator_";
  protected final String TEXT_64 = " fri = rowFileList_1_";
  protected final String TEXT_65 = ");" + NL + "                if (fri.hasNext()) {" + NL + "                    rowList_1_";
  protected final String TEXT_66 = ".set(minIndex_";
  protected final String TEXT_67 = ", fri.next());" + NL + "                } else {" + NL + "                    fri.close();" + NL + "                    rowFileList_1_";
  protected final String TEXT_68 = ".remove(minIndex_";
  protected final String TEXT_69 = ");" + NL + "                    rowList_1_";
  protected final String TEXT_70 = ");" + NL + "                }" + NL + "" + NL + "                //skip duplicates....begin-->this case is suitable for no duplicate connection" + NL + "                for (int i = 0; i < rowList_1_";
  protected final String TEXT_71 = ".size(); ) {" + NL + "                    if (rowList_1_";
  protected final String TEXT_72 = ".get(i).duplicateTo(minItem)) {" + NL + "                        rowStruct_";
  protected final String TEXT_73 = " noDuplicateItem = null;" + NL + "                        FileRowIterator_";
  protected final String TEXT_74 = " fri2 = rowFileList_1_";
  protected final String TEXT_75 = ".get(i);" + NL + "                        while (fri2.hasNext()) {" + NL + "                            rowStruct_";
  protected final String TEXT_76 = " current = fri2.next();" + NL + "                            if (!minItem.duplicateTo(current)) {" + NL + "                                noDuplicateItem = current;" + NL + "                                break;" + NL + "                            }" + NL + "                        }" + NL + "                        if (noDuplicateItem == null) {" + NL + "                            fri2.close();" + NL + "                            rowFileList_1_";
  protected final String TEXT_77 = ".remove(i);" + NL + "                            rowList_1_";
  protected final String TEXT_78 = ".remove(i);" + NL + "                        } else {" + NL + "                            rowList_1_";
  protected final String TEXT_79 = ".set(i, noDuplicateItem);" + NL + "                            i++;" + NL + "                        }" + NL + "                    }else{" + NL + "                        i++;" + NL + "                    }" + NL + "                }" + NL + "                //skip duplicates....end" + NL + "            }" + NL + "" + NL + "" + NL + "            if (rowsInBuffer_2_";
  protected final String TEXT_80 = " > 0) {" + NL + "                " + NL + "                java.util.Arrays.<rowStruct_";
  protected final String TEXT_81 = ", 0, rowsInBuffer_2_";
  protected final String TEXT_82 = ");" + NL + "                " + NL + "                java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_83 = ".size());" + NL + "                ";
  protected final String TEXT_84 = NL + "\t\t\t\t\tlog.debug(\"";
  protected final String TEXT_85 = " - Invoke request to delete file: \"+file.getPath()+\" When VM exit.\");" + NL + "\t\t\t\t";
  protected final String TEXT_86 = NL + "                " + NL + "                file.deleteOnExit();" + NL + "                " + NL + "                java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                        new java.io.FileOutputStream(file)));" + NL + "                        " + NL + "            \t";
  protected final String TEXT_87 = " - Writing the data into: \"+file.getPath());" + NL + "\t\t\t\t";
  protected final String TEXT_88 = NL + "                " + NL + "                for (int i = 0; i < rowsInBuffer_2_";
  protected final String TEXT_89 = "; i++) {" + NL + "                    buffer_2_";
  protected final String TEXT_90 = "[i].writeData(rw);" + NL + "                }" + NL + "                " + NL + "                rw.close();" + NL + "                " + NL + "             \t";
  protected final String TEXT_91 = " - Wrote successfully.\");" + NL + "\t\t\t\t";
  protected final String TEXT_92 = NL + NL + "                files_2_";
  protected final String TEXT_93 = ".add(file);" + NL + "" + NL + "                rowsInBuffer_2_";
  protected final String TEXT_94 = " = 0;" + NL + "            }" + NL + "            buffer_2_";
  protected final String TEXT_95 = " = null;" + NL + "            java.util.List<FileRowIterator_";
  protected final String TEXT_96 = "> rowFileList_2_";
  protected final String TEXT_97 = "> rowList_2_";
  protected final String TEXT_98 = ">();" + NL + "" + NL + "            for (java.io.File file : files_2_";
  protected final String TEXT_99 = ");" + NL + "                rowFileList_2_";
  protected final String TEXT_100 = ".add(fri);" + NL + "                rowList_2_";
  protected final String TEXT_101 = ".add(fri.next());" + NL + "            }" + NL + "" + NL + "            int nb_uniq_";
  protected final String TEXT_102 = " = 0;" + NL + "            while (rowList_2_";
  protected final String TEXT_103 = " = 0;" + NL + "                if (rowList_2_";
  protected final String TEXT_104 = ".size() > 1) {" + NL + "                    for (int i = 1; i < rowList_2_";
  protected final String TEXT_105 = ".size(); i++) {" + NL + "                        if (comparator_2_";
  protected final String TEXT_106 = ".compare(rowList_2_";
  protected final String TEXT_107 = "), rowList_2_";
  protected final String TEXT_108 = " = i;" + NL + "                        }" + NL + "                    }" + NL + "                }" + NL + "" + NL + "                // /////////////" + NL + "" + NL + "                rowStruct_";
  protected final String TEXT_109 = " current = rowList_2_";
  protected final String TEXT_110 = ");" + NL + "" + NL + "                // ////////////" + NL + "                FileRowIterator_";
  protected final String TEXT_111 = " fri = rowFileList_2_";
  protected final String TEXT_112 = ");" + NL + "                if (fri.hasNext()) {" + NL + "                    rowList_2_";
  protected final String TEXT_113 = ", fri.next());" + NL + "                } else {" + NL + "                    fri.close();" + NL + "                    rowFileList_2_";
  protected final String TEXT_114 = ");" + NL + "                    rowList_2_";
  protected final String TEXT_115 = ");" + NL + "                }" + NL + "\t";
  protected final String TEXT_116 = NL + "\t\tlog.trace(\"";
  protected final String TEXT_117 = " - Writing the unique record \" + (nb_uniq_";
  protected final String TEXT_118 = "+1) + \" into ";
  protected final String TEXT_119 = ".\");" + NL + "\t";
  protected final String TEXT_120 = NL + "\t\t\t\t";
  protected final String TEXT_121 = ".";
  protected final String TEXT_122 = " = current.";
  protected final String TEXT_123 = ";" + NL + "    \t";
  protected final String TEXT_124 = NL + "                " + NL + "                nb_uniq_";
  protected final String TEXT_125 = "++;";
  protected final String TEXT_126 = NL + "            int bufferSizePerFile_";
  protected final String TEXT_127 = " = 10000;" + NL + "" + NL + "            java.util.List<FileRowIterator_";
  protected final String TEXT_128 = "(file," + NL + "                        bufferSizePerFile_";
  protected final String TEXT_129 = ".add(fri.next());" + NL + "            }" + NL + "" + NL + "            // comparator for second sort" + NL + "            class Comparator_2_";
  protected final String TEXT_130 = "> {" + NL + "" + NL + "                public int compare(rowStruct_";
  protected final String TEXT_131 = " arg1) {" + NL + "                    return Long.compare(arg0.id_";
  protected final String TEXT_132 = ");" + NL + "                }" + NL + "" + NL + "            }" + NL + "" + NL + "            // For second sort init begin" + NL + "            int bufferSize_2_";
  protected final String TEXT_133 = "/2;" + NL + "            rowStruct_";
  protected final String TEXT_134 = " = new java.util.ArrayList<java.io.File>();" + NL + "            // For second sort init end" + NL + "" + NL + "            // For second sort duplicate init begin" + NL + "            int bufferSize_3_";
  protected final String TEXT_135 = "[] buffer_3_";
  protected final String TEXT_136 = "[bufferSize_3_";
  protected final String TEXT_137 = "];" + NL + "            int rowsInBuffer_3_";
  protected final String TEXT_138 = "> comparator_3_";
  protected final String TEXT_139 = "();" + NL + "" + NL + "            java.util.ArrayList<java.io.File> files_3_";
  protected final String TEXT_140 = " = new java.util.ArrayList<java.io.File>();" + NL + "            // For second sort duplicate init end" + NL + "" + NL + "            while (rowList_1_";
  protected final String TEXT_141 = ")," + NL + "                                rowList_1_";
  protected final String TEXT_142 = ") {" + NL + "" + NL + "                    java.util.Arrays.<rowStruct_";
  protected final String TEXT_143 = ", 0," + NL + "                            bufferSize_2_";
  protected final String TEXT_144 = " + \"uniq_\"" + NL + "                            + files_2_";
  protected final String TEXT_145 = ".size());" + NL + "                   \t";
  protected final String TEXT_146 = "[i].writeData(rw);" + NL + "                    }" + NL + "                    rw.close();" + NL + "\t             \t";
  protected final String TEXT_147 = ");" + NL + "                }" + NL + "" + NL + "                // get duplicates....begin" + NL + "                for (int i = 0; i < rowList_1_";
  protected final String TEXT_148 = ".size();) {" + NL + "                    rowStruct_";
  protected final String TEXT_149 = " current = rowList_1_";
  protected final String TEXT_150 = ".get(i);" + NL + "                    if (current.duplicateTo(minItem)) {" + NL + "                        // current is duplicate...." + NL + "                        if (rowsInBuffer_3_";
  protected final String TEXT_151 = " >= bufferSize_3_";
  protected final String TEXT_152 = ") {" + NL + "" + NL + "                            java.util.Arrays.<rowStruct_";
  protected final String TEXT_153 = "> sort(buffer_3_";
  protected final String TEXT_154 = ", 0," + NL + "                                    bufferSize_3_";
  protected final String TEXT_155 = ", comparator_3_";
  protected final String TEXT_156 = ");" + NL + "                            java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_157 = " + \"duplicate_\"" + NL + "                                    + files_3_";
  protected final String TEXT_158 = NL + "\t\t\t\t\t\t\t\tlog.debug(\"";
  protected final String TEXT_159 = " - Invoke request to delete file: \"+file.getPath()+\" When VM exit.\");" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_160 = NL + "                            file.deleteOnExit();" + NL + "                            java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                                    new java.io.FileOutputStream(file)));" + NL + "                        \t";
  protected final String TEXT_161 = " - Writing the data into: \"+file.getPath());" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_162 = NL + "                            for (int j = 0; j < bufferSize_3_";
  protected final String TEXT_163 = "; j++) {" + NL + "                                buffer_3_";
  protected final String TEXT_164 = "[j].writeData(rw);" + NL + "                            }" + NL + "                            rw.close();" + NL + "\t\t\t             \t";
  protected final String TEXT_165 = " - Wrote successfully.\");" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_166 = NL + NL + "                            files_3_";
  protected final String TEXT_167 = ".add(file);" + NL + "" + NL + "                            rowsInBuffer_3_";
  protected final String TEXT_168 = " = 0;" + NL + "                        }" + NL + "" + NL + "                        buffer_3_";
  protected final String TEXT_169 = "[rowsInBuffer_3_";
  protected final String TEXT_170 = "++] = current;" + NL + "                        rowStruct_";
  protected final String TEXT_171 = ".get(i);" + NL + "                        while (fri2.hasNext()) {" + NL + "                            current = fri2.next();" + NL + "                            if (!minItem.duplicateTo(current)) {" + NL + "                                noDuplicateItem = current;" + NL + "                                break;" + NL + "                            } else {" + NL + "                                // current is duplicate...." + NL + "                                if (rowsInBuffer_3_";
  protected final String TEXT_172 = ") {" + NL + "" + NL + "                                    java.util.Arrays.<rowStruct_";
  protected final String TEXT_173 = ", 0," + NL + "                                            bufferSize_3_";
  protected final String TEXT_174 = ");" + NL + "                                    java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_175 = " + \"duplicate_\"" + NL + "                                            + files_3_";
  protected final String TEXT_176 = NL + "\t\t\t\t\t\t\t\t\t\tlog.debug(\"";
  protected final String TEXT_177 = " - Invoke request to delete file: \"+file.getPath()+\" When VM exit.\");" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_178 = NL + "                                    file.deleteOnExit();" + NL + "                                    java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(" + NL + "                                            new java.io.BufferedOutputStream(new java.io.FileOutputStream(file)));" + NL + "\t\t                        \t";
  protected final String TEXT_179 = " - Writing the data into: \"+file.getPath());" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_180 = NL + "                                    for (int j = 0; j < bufferSize_3_";
  protected final String TEXT_181 = "; j++) {" + NL + "                                        buffer_3_";
  protected final String TEXT_182 = "[j].writeData(rw);" + NL + "                                    }" + NL + "                                    rw.close();" + NL + "\t\t\t\t\t             \t";
  protected final String TEXT_183 = " - Wrote successfully.\");" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_184 = NL + NL + "                                    files_3_";
  protected final String TEXT_185 = ".add(file);" + NL + "" + NL + "                                    rowsInBuffer_3_";
  protected final String TEXT_186 = " = 0;" + NL + "                                }" + NL + "" + NL + "                                buffer_3_";
  protected final String TEXT_187 = "++] = current;" + NL + "                            }" + NL + "                        }" + NL + "                        if (noDuplicateItem == null) {" + NL + "                            fri2.close();" + NL + "                            rowFileList_1_";
  protected final String TEXT_188 = ".set(i, noDuplicateItem);" + NL + "                            i++;" + NL + "                        }" + NL + "                    } else {" + NL + "                        i++;" + NL + "                    }" + NL + "                }" + NL + "                // get duplicates...." + NL + "            }" + NL + "" + NL + "            if (rowsInBuffer_2_";
  protected final String TEXT_189 = " > 0) {" + NL + "" + NL + "                java.util.Arrays.<rowStruct_";
  protected final String TEXT_190 = ", 0," + NL + "                        rowsInBuffer_2_";
  protected final String TEXT_191 = ");" + NL + "" + NL + "                java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_192 = " + \"uniq_\"" + NL + "                        + files_2_";
  protected final String TEXT_193 = ".size());" + NL + "" + NL + "\t\t\t\t";
  protected final String TEXT_194 = NL + "                file.deleteOnExit();" + NL + "" + NL + "                java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                        new java.io.FileOutputStream(file)));" + NL + "                        " + NL + "            \t";
  protected final String TEXT_195 = NL + NL + "                for (int i = 0; i < rowsInBuffer_2_";
  protected final String TEXT_196 = "[i].writeData(rw);" + NL + "                }" + NL + "" + NL + "                rw.close();" + NL + "\t\t\t\t";
  protected final String TEXT_197 = " = null;" + NL + "" + NL + "            // current is duplicate...." + NL + "            if (rowsInBuffer_3_";
  protected final String TEXT_198 = ", 0," + NL + "                        rowsInBuffer_3_";
  protected final String TEXT_199 = ");" + NL + "                java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_200 = " + \"duplicate_\"" + NL + "                        + files_3_";
  protected final String TEXT_201 = NL + "                file.deleteOnExit();" + NL + "                java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                        new java.io.FileOutputStream(file)));" + NL + "            \t";
  protected final String TEXT_202 = NL + "                for (int j = 0; j < rowsInBuffer_3_";
  protected final String TEXT_203 = "; j++) {" + NL + "                    buffer_3_";
  protected final String TEXT_204 = "[j].writeData(rw);" + NL + "                }" + NL + "                rw.close();" + NL + "\t\t\t\t";
  protected final String TEXT_205 = NL + "                files_3_";
  protected final String TEXT_206 = ".add(file);" + NL + "" + NL + "                rowsInBuffer_3_";
  protected final String TEXT_207 = " = 0;" + NL + "            }" + NL + "            buffer_3_";
  protected final String TEXT_208 = " = null;" + NL + "" + NL + "            java.util.List<FileRowIterator_";
  protected final String TEXT_209 = ".add(fri.next());" + NL + "            }" + NL + "" + NL + "            java.util.List<FileRowIterator_";
  protected final String TEXT_210 = "> rowFileList_3_";
  protected final String TEXT_211 = "> rowList_3_";
  protected final String TEXT_212 = ">();" + NL + "" + NL + "            for (java.io.File file : files_3_";
  protected final String TEXT_213 = ");" + NL + "                rowFileList_3_";
  protected final String TEXT_214 = ".add(fri);" + NL + "                rowList_3_";
  protected final String TEXT_215 = " = 0;" + NL + "            int nb_duplicate_";
  protected final String TEXT_216 = " = 0;" + NL + "            rowStruct_";
  protected final String TEXT_217 = " uniq_";
  protected final String TEXT_218 = " = null;" + NL + "            rowStruct_";
  protected final String TEXT_219 = " duplicate_";
  protected final String TEXT_220 = " = null;" + NL + "            int minIndex_";
  protected final String TEXT_221 = " = 0;" + NL + "            /////////////////////" + NL + "            if(rowList_2_";
  protected final String TEXT_222 = ".size() > 0){" + NL + "                minIndex_";
  protected final String TEXT_223 = ")," + NL + "                                rowList_2_";
  protected final String TEXT_224 = " = i;" + NL + "                        }" + NL + "                    }" + NL + "                }" + NL + "                uniq_";
  protected final String TEXT_225 = " = rowList_2_";
  protected final String TEXT_226 = ");" + NL + "                FileRowIterator_";
  protected final String TEXT_227 = ");" + NL + "                }" + NL + "            }" + NL + "            if(rowList_3_";
  protected final String TEXT_228 = " = 0;" + NL + "                if (rowList_3_";
  protected final String TEXT_229 = ".size() > 1) {" + NL + "                    for (int i = 1; i < rowList_3_";
  protected final String TEXT_230 = ".size(); i++) {" + NL + "                        if (comparator_3_";
  protected final String TEXT_231 = ".compare(rowList_3_";
  protected final String TEXT_232 = ")," + NL + "                                rowList_3_";
  protected final String TEXT_233 = " = i;" + NL + "                        }" + NL + "                    }" + NL + "                }" + NL + "                duplicate_";
  protected final String TEXT_234 = " = rowList_3_";
  protected final String TEXT_235 = " fri = rowFileList_3_";
  protected final String TEXT_236 = ");" + NL + "                if (fri.hasNext()) {" + NL + "                    rowList_3_";
  protected final String TEXT_237 = ", fri.next());" + NL + "                } else {" + NL + "                    fri.close();" + NL + "                    rowFileList_3_";
  protected final String TEXT_238 = ");" + NL + "                    rowList_3_";
  protected final String TEXT_239 = ");" + NL + "                }" + NL + "            }" + NL + "            while (true) {";
  protected final String TEXT_240 = NL + "                ";
  protected final String TEXT_241 = " = null;";
  protected final String TEXT_242 = " = null;" + NL + "                " + NL + "                if(uniq_";
  protected final String TEXT_243 = " == null){" + NL + "                    if(duplicate_";
  protected final String TEXT_244 = " == null){" + NL + "                        break;" + NL + "                    }else{";
  protected final String TEXT_245 = NL + "                        ";
  protected final String TEXT_246 = " = new ";
  protected final String TEXT_247 = "Struct();" + NL + "\t";
  protected final String TEXT_248 = " - Writing the duplicate record \" + (nb_duplicate_";
  protected final String TEXT_249 = " = duplicate_";
  protected final String TEXT_250 = NL + "                    \tnb_duplicate_";
  protected final String TEXT_251 = "++;" + NL + "                        duplicate_";
  protected final String TEXT_252 = " = null;" + NL + "                        if(rowList_3_";
  protected final String TEXT_253 = ".size() > 0){" + NL + "                            minIndex_";
  protected final String TEXT_254 = " = 0;" + NL + "                            if (rowList_3_";
  protected final String TEXT_255 = ".size() > 1) {" + NL + "                                for (int i = 1; i < rowList_3_";
  protected final String TEXT_256 = ".size(); i++) {" + NL + "                                    if (comparator_3_";
  protected final String TEXT_257 = ")," + NL + "                                            rowList_3_";
  protected final String TEXT_258 = ".get(i)) > 0) {" + NL + "                                        minIndex_";
  protected final String TEXT_259 = " = i;" + NL + "                                    }" + NL + "                                }" + NL + "                            }" + NL + "                            duplicate_";
  protected final String TEXT_260 = ");" + NL + "                            FileRowIterator_";
  protected final String TEXT_261 = ");" + NL + "                            if (fri.hasNext()) {" + NL + "                                rowList_3_";
  protected final String TEXT_262 = ", fri.next());" + NL + "                            } else {" + NL + "                                fri.close();" + NL + "                                rowFileList_3_";
  protected final String TEXT_263 = ");" + NL + "                                rowList_3_";
  protected final String TEXT_264 = ");" + NL + "                            }" + NL + "                        }" + NL + "                    }" + NL + "                }else{" + NL + "                    if(duplicate_";
  protected final String TEXT_265 = " == null){";
  protected final String TEXT_266 = "Struct();" + NL + "                        " + NL + "\t";
  protected final String TEXT_267 = " = uniq_";
  protected final String TEXT_268 = NL + "                        nb_uniq_";
  protected final String TEXT_269 = "++;" + NL + "                        " + NL + "                        uniq_";
  protected final String TEXT_270 = " = null;" + NL + "                        " + NL + "                        if(rowList_2_";
  protected final String TEXT_271 = " = 0;" + NL + "                            if (rowList_2_";
  protected final String TEXT_272 = ".size() > 1) {" + NL + "                                for (int i = 1; i < rowList_2_";
  protected final String TEXT_273 = ".size(); i++) {" + NL + "                                    if (comparator_2_";
  protected final String TEXT_274 = ")," + NL + "                                            rowList_2_";
  protected final String TEXT_275 = " = i;" + NL + "                                    }" + NL + "                                }" + NL + "                            }" + NL + "                            uniq_";
  protected final String TEXT_276 = ");" + NL + "                            if (fri.hasNext()) {" + NL + "                                rowList_2_";
  protected final String TEXT_277 = ", fri.next());" + NL + "                            } else {" + NL + "                                fri.close();" + NL + "                                rowFileList_2_";
  protected final String TEXT_278 = ");" + NL + "                                rowList_2_";
  protected final String TEXT_279 = ");" + NL + "                            }" + NL + "                        }" + NL + "                        " + NL + "                    }else{" + NL + "                        if(uniq_";
  protected final String TEXT_280 = ".id_";
  protected final String TEXT_281 = " < duplicate_";
  protected final String TEXT_282 = "){";
  protected final String TEXT_283 = NL + "                            " + NL + "                            nb_uniq_";
  protected final String TEXT_284 = "++;" + NL + "                            " + NL + "                            uniq_";
  protected final String TEXT_285 = " = null;" + NL + "                            " + NL + "                            if(rowList_2_";
  protected final String TEXT_286 = ".size() > 0){" + NL + "                                minIndex_";
  protected final String TEXT_287 = " = 0;" + NL + "                                if (rowList_2_";
  protected final String TEXT_288 = ".size() > 1) {" + NL + "                                    for (int i = 1; i < rowList_2_";
  protected final String TEXT_289 = ".size(); i++) {" + NL + "                                        if (comparator_2_";
  protected final String TEXT_290 = ")," + NL + "                                                rowList_2_";
  protected final String TEXT_291 = ".get(i)) > 0) {" + NL + "                                            minIndex_";
  protected final String TEXT_292 = " = i;" + NL + "                                        }" + NL + "                                    }" + NL + "                                }" + NL + "                                uniq_";
  protected final String TEXT_293 = ");" + NL + "                                FileRowIterator_";
  protected final String TEXT_294 = ");" + NL + "                                if (fri.hasNext()) {" + NL + "                                    rowList_2_";
  protected final String TEXT_295 = ", fri.next());" + NL + "                                } else {" + NL + "                                    fri.close();" + NL + "                                    rowFileList_2_";
  protected final String TEXT_296 = ");" + NL + "                                    rowList_2_";
  protected final String TEXT_297 = ");" + NL + "                                }" + NL + "                            }" + NL + "                        }else{";
  protected final String TEXT_298 = NL + "\t\t\t\t\t\t\tnb_duplicate_";
  protected final String TEXT_299 = "++;" + NL + "                            duplicate_";
  protected final String TEXT_300 = " = null;" + NL + "                            if(rowList_3_";
  protected final String TEXT_301 = " = 0;" + NL + "                                if (rowList_3_";
  protected final String TEXT_302 = ".size() > 1) {" + NL + "                                    for (int i = 1; i < rowList_3_";
  protected final String TEXT_303 = ".size(); i++) {" + NL + "                                        if (comparator_3_";
  protected final String TEXT_304 = ")," + NL + "                                                rowList_3_";
  protected final String TEXT_305 = " = i;" + NL + "                                        }" + NL + "                                    }" + NL + "                                }" + NL + "                                duplicate_";
  protected final String TEXT_306 = ");" + NL + "                                if (fri.hasNext()) {" + NL + "                                    rowList_3_";
  protected final String TEXT_307 = ", fri.next());" + NL + "                                } else {" + NL + "                                    fri.close();" + NL + "                                    rowFileList_3_";
  protected final String TEXT_308 = ");" + NL + "                                    rowList_3_";
  protected final String TEXT_309 = ");" + NL + "                                }" + NL + "                            }" + NL + "                        }" + NL + "                        " + NL + "                    }" + NL + "                }" + NL;
  protected final String TEXT_310 = " = new java.util.ArrayList<java.io.File>();" + NL + "            // For second sort duplicate init end" + NL + "            boolean forDuplicateFlag_";
  protected final String TEXT_311 = " = false;" + NL + "            rowStruct_";
  protected final String TEXT_312 = " minItem = null;" + NL + "            while (rowList_1_";
  protected final String TEXT_313 = " = i;" + NL + "                        }" + NL + "                    }" + NL + "                }" + NL + "" + NL + "                // /////////////" + NL + "                if(forDuplicateFlag_";
  protected final String TEXT_314 = "){" + NL + "                \tif(!rowList_1_";
  protected final String TEXT_315 = ").duplicateTo(minItem)){" + NL + "                \t\tif (rowsInBuffer_2_";
  protected final String TEXT_316 = ") {" + NL + "" + NL + "\t                        java.util.Arrays.<rowStruct_";
  protected final String TEXT_317 = ", 0," + NL + "\t                                bufferSize_2_";
  protected final String TEXT_318 = ");" + NL + "\t                        java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_319 = " + \"unique_\"" + NL + "\t                                + files_2_";
  protected final String TEXT_320 = ".size());" + NL + "\t                        ";
  protected final String TEXT_321 = NL + "\t                        file.deleteOnExit();" + NL + "\t                        java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "\t                                new java.io.FileOutputStream(file)));" + NL + "                        \t";
  protected final String TEXT_322 = NL + "\t                        for (int i = 0; i < bufferSize_2_";
  protected final String TEXT_323 = "; i++) {" + NL + "\t                            buffer_2_";
  protected final String TEXT_324 = "[i].writeData(rw);" + NL + "\t                        }" + NL + "\t                        rw.close();" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_325 = NL + "\t" + NL + "\t                        files_2_";
  protected final String TEXT_326 = ".add(file);" + NL + "\t" + NL + "\t                        rowsInBuffer_2_";
  protected final String TEXT_327 = " = 0;" + NL + "\t                    }" + NL + "\t                    minItem = rowList_1_";
  protected final String TEXT_328 = ");" + NL + "\t                    buffer_2_";
  protected final String TEXT_329 = "++] = minItem;" + NL + "\t                    FileRowIterator_";
  protected final String TEXT_330 = ");" + NL + "\t                    if (fri.hasNext()) {" + NL + "\t                        rowList_1_";
  protected final String TEXT_331 = ", fri.next());" + NL + "\t                    } else {" + NL + "\t                        fri.close();" + NL + "\t                        rowFileList_1_";
  protected final String TEXT_332 = ");" + NL + "\t                        rowList_1_";
  protected final String TEXT_333 = ");" + NL + "\t                    }\t" + NL + "                \t}else{" + NL + "\t                    if (rowsInBuffer_3_";
  protected final String TEXT_334 = ") {" + NL + "\t" + NL + "\t                        java.util.Arrays.<rowStruct_";
  protected final String TEXT_335 = ", 0," + NL + "\t                                bufferSize_3_";
  protected final String TEXT_336 = " + \"duplicate_\"" + NL + "\t                                + files_3_";
  protected final String TEXT_337 = NL + "\t                        file.deleteOnExit();" + NL + "\t                        " + NL + "\t                        java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "\t                                new java.io.FileOutputStream(file)));" + NL + "                        \t";
  protected final String TEXT_338 = NL + "\t                        for (int i = 0; i < bufferSize_3_";
  protected final String TEXT_339 = "; i++) {" + NL + "\t                            buffer_3_";
  protected final String TEXT_340 = NL + "\t" + NL + "\t                        files_3_";
  protected final String TEXT_341 = ".add(file);" + NL + "\t" + NL + "\t                        rowsInBuffer_3_";
  protected final String TEXT_342 = ");" + NL + "\t                    buffer_3_";
  protected final String TEXT_343 = ");" + NL + "\t                    }" + NL + "\t                    " + NL + "\t                    forDuplicateFlag_";
  protected final String TEXT_344 = " = false;" + NL + "\t                    " + NL + "\t                    // skip other duplicates....begin" + NL + "\t                    for (int i = 0; i < rowList_1_";
  protected final String TEXT_345 = ".size();) {" + NL + "\t                        if (rowList_1_";
  protected final String TEXT_346 = ".get(i).duplicateTo(minItem)) {" + NL + "\t                            rowStruct_";
  protected final String TEXT_347 = " noDuplicateItem = null;" + NL + "\t                            FileRowIterator_";
  protected final String TEXT_348 = ".get(i);" + NL + "\t                            while (fri2.hasNext()) {" + NL + "\t                                rowStruct_";
  protected final String TEXT_349 = " current = fri2.next();" + NL + "\t                                if (!minItem.duplicateTo(current)) {" + NL + "\t                                    noDuplicateItem = current;" + NL + "\t                                    break;" + NL + "\t                                }" + NL + "\t                            }" + NL + "\t                            if (noDuplicateItem == null) {" + NL + "\t                                fri2.close();" + NL + "\t                                rowFileList_1_";
  protected final String TEXT_350 = ".remove(i);" + NL + "\t                                rowList_1_";
  protected final String TEXT_351 = ".remove(i);" + NL + "\t                            } else {" + NL + "\t                                rowList_1_";
  protected final String TEXT_352 = ".set(i, noDuplicateItem);" + NL + "\t                                i++;" + NL + "\t                            }" + NL + "\t                        } else {" + NL + "\t                            i++;" + NL + "\t                        }" + NL + "\t                    }" + NL + "\t                    // skip duplicates....end" + NL + "                    }" + NL + "                }else{" + NL + "                    if (rowsInBuffer_2_";
  protected final String TEXT_353 = ") {" + NL + "" + NL + "                        java.util.Arrays.<rowStruct_";
  protected final String TEXT_354 = ", 0," + NL + "                                bufferSize_2_";
  protected final String TEXT_355 = ");" + NL + "                        java.io.File file = new java.io.File(temp_file_path_prefix_";
  protected final String TEXT_356 = " + \"unique_\"" + NL + "                                + files_2_";
  protected final String TEXT_357 = NL + "\t\t\t\t\t\t\tlog.debug(\"";
  protected final String TEXT_358 = " - Invoke request to delete file: \"+file.getPath()+\" When VM exit.\");" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_359 = NL + "                        file.deleteOnExit();" + NL + "                        java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                                new java.io.FileOutputStream(file)));" + NL + "                    \t";
  protected final String TEXT_360 = " - Writing the data into: \"+file.getPath());" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_361 = NL + "                        for (int i = 0; i < bufferSize_2_";
  protected final String TEXT_362 = "; i++) {" + NL + "                            buffer_2_";
  protected final String TEXT_363 = "[i].writeData(rw);" + NL + "                        }" + NL + "                        rw.close();" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_364 = " - Wrote successfully.\");" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_365 = NL + NL + "                        files_2_";
  protected final String TEXT_366 = ".add(file);" + NL + "" + NL + "                        rowsInBuffer_2_";
  protected final String TEXT_367 = " = 0;" + NL + "                    }" + NL + "                    minItem = rowList_1_";
  protected final String TEXT_368 = ");" + NL + "                    buffer_2_";
  protected final String TEXT_369 = "++] = minItem;" + NL + "                    FileRowIterator_";
  protected final String TEXT_370 = ");" + NL + "                    if (fri.hasNext()) {" + NL + "                        rowList_1_";
  protected final String TEXT_371 = ", fri.next());" + NL + "                    } else {" + NL + "                        fri.close();" + NL + "                        rowFileList_1_";
  protected final String TEXT_372 = ");" + NL + "                        rowList_1_";
  protected final String TEXT_373 = ");" + NL + "                    }" + NL + "                    " + NL + "                    forDuplicateFlag_";
  protected final String TEXT_374 = " = true;" + NL + "                }" + NL + "            }" + NL + "" + NL + "            if (rowsInBuffer_2_";
  protected final String TEXT_375 = ".size());" + NL + "                        ";
  protected final String TEXT_376 = NL + NL + "                file.deleteOnExit();" + NL + "" + NL + "                java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                        new java.io.FileOutputStream(file)));" + NL + "                        " + NL + "            \t";
  protected final String TEXT_377 = NL + NL + "                files_3_";
  protected final String TEXT_378 = ");" + NL + "                }" + NL + "" + NL + "            }" + NL + "" + NL + "            // For second sort duplicate init begin" + NL + "            int bufferSize_3_";
  protected final String TEXT_379 = " = i;" + NL + "                        }" + NL + "                    }" + NL + "                }" + NL + "" + NL + "                // /////////////" + NL + "                rowStruct_";
  protected final String TEXT_380 = "[j].writeData(rw);" + NL + "                            }" + NL + "                            rw.close();" + NL + "\t\t\t\t\t\t\t";
  protected final String TEXT_381 = NL + "                                    file.deleteOnExit();" + NL + "                                    java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(" + NL + "                                            new java.io.BufferedOutputStream(new java.io.FileOutputStream(file)));";
  protected final String TEXT_382 = "[j].writeData(rw);" + NL + "                                    }" + NL + "                                    rw.close();" + NL + "\t\t\t\t\t\t\t\t\t";
  protected final String TEXT_383 = ".set(i, noDuplicateItem);" + NL + "                            i++;" + NL + "                        }" + NL + "                    } else {" + NL + "                        i++;" + NL + "                    }" + NL + "                }" + NL + "                // get duplicates...." + NL + "            }" + NL + "" + NL + "            // current is duplicate...." + NL + "            if (rowsInBuffer_3_";
  protected final String TEXT_384 = NL + "                file.deleteOnExit();" + NL + "                java.io.ObjectOutputStream rw = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(" + NL + "                        new java.io.FileOutputStream(file)));" + NL + "              \t";
  protected final String TEXT_385 = ".add(fri.next());" + NL + "            }" + NL + "            int nb_uniq_";
  protected final String TEXT_386 = " = null;" + NL + "            " + NL + "            while (rowList_3_";
  protected final String TEXT_387 = " current = rowList_3_";
  protected final String TEXT_388 = NL + NL + "                nb_duplicate_";
  protected final String TEXT_389 = " = new java.util.ArrayList<java.io.File>();" + NL + "            // For second sort duplicate init end" + NL + "            " + NL + "            boolean forDuplicateFlag_";
  protected final String TEXT_390 = " minItem = null;" + NL + "            " + NL + "            while (rowList_1_";
  protected final String TEXT_391 = "){" + NL + "                \tif(!minItem.duplicateTo(rowList_1_";
  protected final String TEXT_392 = "))){" + NL + "                \t\tminItem = rowList_1_";
  protected final String TEXT_393 = ");" + NL + "\t                    FileRowIterator_";
  protected final String TEXT_394 = ");" + NL + "\t                    }" + NL + "                \t}else{" + NL + "                \t" + NL + "\t                    if (rowsInBuffer_3_";
  protected final String TEXT_395 = NL + "\t                        files_3_";
  protected final String TEXT_396 = ".set(i, noDuplicateItem);" + NL + "\t                                i++;" + NL + "\t                            }" + NL + "\t                        } else {" + NL + "\t                            i++;" + NL + "\t                        }" + NL + "\t                    }" + NL + "\t                    // skip duplicates....end" + NL + "                    }" + NL + "                }else{" + NL + "                    minItem = rowList_1_";
  protected final String TEXT_397 = ");" + NL + "                    FileRowIterator_";
  protected final String TEXT_398 = ");" + NL + "                    }" + NL + "                    forDuplicateFlag_";
  protected final String TEXT_399 = " = true;" + NL + "                }" + NL + "            }" + NL + "" + NL + "            // current is duplicate...." + NL + "            if (rowsInBuffer_3_";
  protected final String TEXT_400 = " = 0;" + NL + "" + NL + "            while (rowList_3_";
  protected final String TEXT_401 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = ElementParameterParser.getValue(node, "__CID__");

int UNIQUE = 1;
int UNIQUE_AND_DUPLICATE = 2;
int UNIQUE_AND_DUPLICATE_ONCE = 3;
int DUPLICATE = 4;
int DUPLICATE_ONCE = 5;

int mode = 0;
String connUniqName = null;
String connDuplicateName = null;
boolean onlyOnceEachDuplicatedKey = ("true").equals(ElementParameterParser.getValue(node, "__ONLY_ONCE_EACH_DUPLICATED_KEY__"));
List<? extends IConnection> connsUnique = node.getOutgoingConnections("UNIQUE");
List<? extends IConnection> connsDuplicate = node.getOutgoingConnections("DUPLICATE");
final boolean isLog4jEnabled = ("true").equals(ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));
if(connsUnique.size() > 0){
	connUniqName = connsUnique.get(0).getName();
	if(connsDuplicate.size() > 0){
		connDuplicateName = connsDuplicate.get(0).getName();
		if(onlyOnceEachDuplicatedKey){
			mode =3;
		}else{
			mode = 2;
		}
	}else{
		mode = 1;
	}
}else{
	if(connsDuplicate.size() > 0){
		connDuplicateName = connsDuplicate.get(0).getName();
		if(onlyOnceEachDuplicatedKey){
			mode =5;
		}else{
			mode = 4;
		}
	}
}

List<IMetadataTable> metadatas = node.getMetadataList();
IMetadataTable metadata = null;
List<IMetadataColumn> columnList = null;
if ((metadatas!=null)&&(metadatas.size()>0)) {
    metadata = metadatas.get(0);
    columnList = metadata.getListColumns();
}else{
	mode = 0;
}

if(mode == UNIQUE){//HSS_____0

    stringBuffer.append(TEXT_1);
    stringBuffer.append(cid );
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
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(cid );
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
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
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
    
					if (isLog4jEnabled) {
					
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    
					}
					
    stringBuffer.append(TEXT_51);
    
					if (isLog4jEnabled) {
					
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    
					}
					
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    
					if (isLog4jEnabled) {
					
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    
					}
					
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_83);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    
				}
				
    stringBuffer.append(TEXT_86);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    
				}
				
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_90);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
				}
				
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_102);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_124);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_125);
    
}else if(mode == UNIQUE_AND_DUPLICATE){//HSS_____0

    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_127);
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
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_134);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_142);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_144);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_145);
    
					if (isLog4jEnabled) {
					
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    
					}
					
    stringBuffer.append(TEXT_51);
    
					if (isLog4jEnabled) {
					
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_52);
    
					}
					
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_146);
    
					if (isLog4jEnabled) {
					
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_56);
    
					}
					
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_150);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_157);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_159);
    
							}
							
    stringBuffer.append(TEXT_160);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    
							}
							
    stringBuffer.append(TEXT_162);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_164);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    
							}
							
    stringBuffer.append(TEXT_166);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_168);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_170);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_171);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_172);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_173);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_174);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_175);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
									if (isLog4jEnabled) {
									
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_177);
    
									}
									
    stringBuffer.append(TEXT_178);
    
									if (isLog4jEnabled) {
									
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_179);
    
									}
									
    stringBuffer.append(TEXT_180);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_182);
    
									if (isLog4jEnabled) {
									
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_183);
    
									}
									
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_185);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_186);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_187);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_188);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_190);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_191);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_192);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_193);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    
				}
				
    stringBuffer.append(TEXT_194);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    
				}
				
    stringBuffer.append(TEXT_195);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_196);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
				}
				
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_197);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_198);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    
				}
				
    stringBuffer.append(TEXT_201);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    
				}
				
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_203);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_204);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
				}
				
    stringBuffer.append(TEXT_205);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_210);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_213);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_214);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_215);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_216);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_217);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_218);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_219);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_220);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_221);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_222);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_223);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_224);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_227);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_222);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_228);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_232);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_233);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_236);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_237);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_239);
    stringBuffer.append(TEXT_240);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_241);
    stringBuffer.append(TEXT_240);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_242);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_243);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_244);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_247);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_249);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_250);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_251);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_252);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_253);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_254);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_255);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_256);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_257);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_259);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_260);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_261);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_262);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_263);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_264);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_265);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_266);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_268);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_269);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_270);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_253);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_271);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_272);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_273);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_274);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_275);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_260);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_276);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_277);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_278);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_279);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_281);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_282);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_266);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_283);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_285);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_286);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_287);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_289);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_290);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_291);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_292);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_293);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_294);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_295);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_296);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_297);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_247);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_249);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_298);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_299);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_300);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_286);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_301);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_302);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_303);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_304);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_291);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_305);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_293);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_306);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_307);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_308);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_309);
    
}else if(mode == UNIQUE_AND_DUPLICATE_ONCE){//HSS_____0

    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_127);
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
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_28);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_134);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_310);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_311);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_312);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_313);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_314);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_315);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_316);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_317);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_318);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_319);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_320);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_159);
    
							}
							
    stringBuffer.append(TEXT_321);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    
							}
							
    stringBuffer.append(TEXT_322);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_323);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_324);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    
							}
							
    stringBuffer.append(TEXT_325);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_326);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_327);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_328);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_329);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_330);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_332);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_333);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_334);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_335);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_318);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_336);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_320);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_159);
    
							}
							
    stringBuffer.append(TEXT_337);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    
							}
							
    stringBuffer.append(TEXT_338);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_339);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_324);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    
							}
							
    stringBuffer.append(TEXT_340);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_341);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_327);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_342);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_329);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_330);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_332);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_343);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_344);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_345);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_346);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_347);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_348);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_349);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_350);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_351);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_352);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_353);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_354);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_355);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_356);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
						if (isLog4jEnabled) {
						
    stringBuffer.append(TEXT_357);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_358);
    
						}
						
    stringBuffer.append(TEXT_359);
    
						if (isLog4jEnabled) {
						
    stringBuffer.append(TEXT_357);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_360);
    
						}
						
    stringBuffer.append(TEXT_361);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_362);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_363);
    
						if (isLog4jEnabled) {
						
    stringBuffer.append(TEXT_357);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_364);
    
						}
						
    stringBuffer.append(TEXT_365);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_366);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_367);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_368);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_369);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_370);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_371);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_372);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_373);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_374);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_190);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_191);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_192);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_375);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    
				}
				
    stringBuffer.append(TEXT_376);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    
				}
				
    stringBuffer.append(TEXT_195);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_196);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
				}
				
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_197);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_198);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    
				}
				
    stringBuffer.append(TEXT_201);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    
				}
				
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_203);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_204);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
				}
				
    stringBuffer.append(TEXT_377);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_210);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_213);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_214);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_215);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_216);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_217);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_218);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_219);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_220);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_221);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_222);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_223);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_224);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_227);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_222);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_228);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_232);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_233);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_236);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_237);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_239);
    stringBuffer.append(TEXT_240);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_241);
    stringBuffer.append(TEXT_240);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_242);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_243);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_244);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_247);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_249);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_250);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_251);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_252);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_253);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_254);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_255);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_256);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_257);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_259);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_260);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_261);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_262);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_263);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_264);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_265);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_266);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_268);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_269);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_270);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_253);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_271);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_272);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_273);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_274);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_275);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_260);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_276);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_277);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_278);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_279);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_281);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_282);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_266);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connUniqName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_283);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_285);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_286);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_287);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_289);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_290);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_291);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_292);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_225);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_293);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_294);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_295);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_296);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_297);
    stringBuffer.append(TEXT_245);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_247);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_249);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_298);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_299);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_300);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_286);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_301);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_302);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_303);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_304);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_291);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_305);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_293);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_306);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_307);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_308);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_309);
    
}else if(mode == DUPLICATE){//HSS_____0

    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_127);
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
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_378);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_379);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_150);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_157);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_159);
    
							}
							
    stringBuffer.append(TEXT_160);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    
							}
							
    stringBuffer.append(TEXT_162);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_380);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    
							}
							
    stringBuffer.append(TEXT_166);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_168);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_170);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_171);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_172);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_173);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_174);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_175);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
									if (isLog4jEnabled) {
									
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_177);
    
									}
									
    stringBuffer.append(TEXT_381);
    
									if (isLog4jEnabled) {
									
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_179);
    
									}
									
    stringBuffer.append(TEXT_180);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_181);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_382);
    
									if (isLog4jEnabled) {
									
    stringBuffer.append(TEXT_176);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_183);
    
									}
									
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_185);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_186);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_187);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_383);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_198);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    
				}
				
    stringBuffer.append(TEXT_384);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    
				}
				
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_203);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_204);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
				}
				
    stringBuffer.append(TEXT_377);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_210);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_213);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_214);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_385);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_215);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_216);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_219);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_386);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_228);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_232);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_387);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_236);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_237);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_388);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_125);
    
}else if(mode == DUPLICATE_ONCE){//HSS_____0

    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_127);
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
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_378);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_138);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_389);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_311);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_390);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_313);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_391);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_392);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_393);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_330);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_332);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_394);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_334);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_335);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_318);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_336);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_320);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_159);
    
							}
							
    stringBuffer.append(TEXT_321);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    
							}
							
    stringBuffer.append(TEXT_338);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_339);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_324);
    
							if (isLog4jEnabled) {
							
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_165);
    
							}
							
    stringBuffer.append(TEXT_395);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_341);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_327);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_342);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_329);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_330);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_332);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_343);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_344);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_345);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_346);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_347);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_348);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_349);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_350);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_351);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_396);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_397);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_370);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_371);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_372);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_398);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_399);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_198);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    
				}
				
    stringBuffer.append(TEXT_201);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    
				}
				
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_203);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_204);
    
				if (isLog4jEnabled) {
				
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    
				}
				
    stringBuffer.append(TEXT_377);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_210);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_213);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_214);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_385);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_215);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_400);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_228);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_229);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_232);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_387);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_236);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_237);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    
	if(isLog4jEnabled){
	
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_248);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_119);
    
	}
    for(IMetadataColumn column : columnList){//HSS_____0_____1
    	
    stringBuffer.append(TEXT_120);
    stringBuffer.append(connDuplicateName );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_123);
    
    }//HSS_____0_____1

    stringBuffer.append(TEXT_388);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_125);
    
}//HSS_____0


    stringBuffer.append(TEXT_401);
    return stringBuffer.toString();
  }
}
