package com.ilikezhibo.ggzb.avsdk.badwordfilter;

import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.TextUtil;
import com.jack.utils.Trace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名称：WordFilterUtil
 * 类描述：
 * 创建人：linguo
 * 创建时间：2014-10-27 上午9:48:19
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */

public class WordFilterUtil {
   private static Node tree = new Node();

   static {
      //InputStream is=null;
      //
      //try {
      //   //InputStream is = WordFilterUtil.class.getResourceAsStream("/SensitiveWord.txt");
      //   is = AULiveApplication.mContext.getResources().getAssets().open("SensitiveWord.txt");
      //} catch (Exception e) {
      //   e.printStackTrace();
      //}
      //try {
      //   InputStreamReader reader = new InputStreamReader(is, "UTF-8");
      //   Properties prop = new Properties();
      //   prop.load(reader);
      //   Enumeration en = prop.propertyNames();
      //
      //   while (en.hasMoreElements()) {
      //      String word = (String) en.nextElement();
      //      insertWord(word, Integer.valueOf(prop.getProperty(word)).intValue());
      //   }
      //} catch (UnsupportedEncodingException e) {
      //   e.printStackTrace();
      //   if (is != null) {
      //      try {
      //         is.close();
      //      } catch (IOException e1) {
      //         e.printStackTrace();
      //      }
      //   }
      //} catch (IOException e) {
      //   e.printStackTrace();
      //   if (is != null) {
      //      try {
      //         is.close();
      //      } catch (IOException e2) {
      //         e.printStackTrace();
      //      }
      //   }
      //} finally {
      //   if (is != null) {
      //      try {
      //         is.close();
      //      } catch (IOException e) {
      //         e.printStackTrace();
      //      }
      //   }
      //}

      try {
         String info = SharedPreferenceTool.getInstance()
             .getString(BadWordDownloadHelper.BADWORDDOWNLOAD_KEY, "");
         if (TextUtil.isValidate(info)) {
            String[] words = info.split("，");
            Trace.d("words lengd:" + words.length);

            ArrayList<String> al_words = new ArrayList<String>();
            al_words.addAll(Arrays.asList(words));
            for (String word : al_words) {
               Trace.d("insertWord:" + word);
               insertWord(word, 4);
            }
         }
      }catch (Exception e){

      }
   }

   private static void insertWord(String word, int level) {
      Node node = tree;
      for (int i = 0; i < word.length(); i++) {
         node = node.addChar(word.charAt(i));
      }
      node.setEnd(true);
      node.setLevel(level);
   }

   private static boolean isPunctuationChar(String c) {
      String regex = "[\\p{P}\\p{Z}\\p{S}\\p{M}\\p{C}]";
      Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      Matcher m = p.matcher(c);
      return m.find();
   }

   private static PunctuationOrHtmlFilteredResult filterPunctation(String originalString) {
      StringBuffer filteredString = new StringBuffer();
      ArrayList<Integer> charOffsets = new ArrayList();
      for (int i = 0; i < originalString.length(); i++) {
         String c = String.valueOf(originalString.charAt(i));
         if (!isPunctuationChar(c)) {
            filteredString.append(c);
            charOffsets.add(Integer.valueOf(i));
         }
      }
      PunctuationOrHtmlFilteredResult result = new PunctuationOrHtmlFilteredResult();
      result.setOriginalString(originalString);
      result.setFilteredString(filteredString);
      result.setCharOffsets(charOffsets);
      return result;
   }

   private static PunctuationOrHtmlFilteredResult filterPunctationAndHtml(String originalString) {
      StringBuffer filteredString = new StringBuffer();
      ArrayList<Integer> charOffsets = new ArrayList();
      int i = 0;
      for (int k = 0; i < originalString.length(); i++) {
         String c = String.valueOf(originalString.charAt(i));
         if (originalString.charAt(i) == '<') {
            for (k = i + 1; k < originalString.length(); k++) {
               if (originalString.charAt(k) == '<') {
                  k = i;
               } else {
                  if (originalString.charAt(k) == '>') {
                     break;
                  }
               }
            }
            i = k;
         } else if (!isPunctuationChar(c)) {
            filteredString.append(c);
            charOffsets.add(Integer.valueOf(i));
         }
      }
      PunctuationOrHtmlFilteredResult result = new PunctuationOrHtmlFilteredResult();
      result.setOriginalString(originalString);
      result.setFilteredString(filteredString);
      result.setCharOffsets(charOffsets);
      return result;
   }

   private static FilteredResult filter(PunctuationOrHtmlFilteredResult pohResult,
       char replacement) {
      StringBuffer sentence = pohResult.getFilteredString();
      ArrayList<Integer> charOffsets = pohResult.getCharOffsets();
      StringBuffer resultString = new StringBuffer(pohResult.getOriginalString());
      StringBuffer badWords = new StringBuffer();
      int level = 0;
      Node node = tree;
      int start = 0;
      int end = 0;
      for (int i = 0; i < sentence.length(); i++) {
         start = i;
         end = i;
         node = tree;
         for (int j = i; j < sentence.length(); j++) {
            node = node.findChar(sentence.charAt(j));
            if (node == null) {
               break;
            }
            if (node.isEnd()) {
               end = j;
               level = node.getLevel();
            }
         }
         if (end > start) {
            for (int k = start; k <= end; k++) {
               resultString.setCharAt(((Integer) charOffsets.get(k)).intValue(), replacement);
            }
            if (badWords.length() > 0) {
               badWords.append(",");
            }
            badWords.append(sentence.substring(start, end + 1));
            i = end;
         }
      }
      FilteredResult result = new FilteredResult();
      result.setOriginalContent(pohResult.getOriginalString());
      result.setFilteredContent(resultString.toString());
      result.setBadWords(badWords.toString());
      result.setLevel(Integer.valueOf(level));
      return result;
   }

   public static String simpleFilter(String sentence, char replacement) {
      StringBuffer sb = new StringBuffer();
      Node node = tree;
      int start = 0;
      int end = 0;
      for (int i = 0; i < sentence.length(); i++) {
         start = i;
         end = i;
         node = tree;
         for (int j = i; j < sentence.length(); j++) {
            node = node.findChar(sentence.charAt(j));
            if (node == null) {
               break;
            }
            if (node.isEnd()) {
               end = j;
            }
         }
         if (end > start) {
            for (int k = start; k <= end; k++) {
               sb.append(replacement);
            }
            i = end;
         } else {
            sb.append(sentence.charAt(i));
         }
      }
      return sb.toString();
   }

   public static FilteredResult filterText(String originalString, char replacement) {
      return filter(filterPunctation(originalString), replacement);
   }

   public static FilteredResult filterHtml(String originalString, char replacement) {
      return filter(filterPunctationAndHtml(originalString), replacement);
   }

   private static class PunctuationOrHtmlFilteredResult {
      private String originalString;
      private StringBuffer filteredString;
      private ArrayList<Integer> charOffsets;

      public String getOriginalString() {
         return this.originalString;
      }

      public void setOriginalString(String originalString) {
         this.originalString = originalString;
      }

      public StringBuffer getFilteredString() {
         return this.filteredString;
      }

      public void setFilteredString(StringBuffer filteredString) {
         this.filteredString = filteredString;
      }

      public ArrayList<Integer> getCharOffsets() {
         return this.charOffsets;
      }

      public void setCharOffsets(ArrayList<Integer> charOffsets) {
         this.charOffsets = charOffsets;
      }
   }
}


