package tools;
import java.util.*;
/**
 *
 * @author ciano
 */
public class ContentFilter {
    public static String doing(String message) {
        message = message.replaceAll("\n", "");
        int rIndex = -1;
        int rEndIndex = -1;
        List<String> rString = new ArrayList<String>();
        while ((rIndex = message.indexOf("[b]", rEndIndex)) >= 0) {
            rEndIndex = message.indexOf("[/b]", rIndex);
            try {
                rString.add(message.substring(rIndex, rEndIndex + 4));
            } catch (Exception e) {}
        }
        
        int qIndex = -1;
        int qEndIndex = -1;
        List<String> qString = new ArrayList<String>();
        while ((qIndex = message.indexOf("[quote]", qEndIndex)) >= 0) {
            qEndIndex = message.indexOf("[/quote]", qIndex) + 8;
            try {
                qString.add(message.substring(qIndex, qEndIndex));
            } catch (Exception e) {
            }
        }

        
        for (int i = 0; i < rString.size(); ++i) {
            String r = rString.get(i);
            message = message.replace(r, doContentFilter(r));
        }
        for (int i = 0; i < qString.size(); ++i) {
            String q = qString.get(i);
            message = message.replace(q, doQuoteFilter(q));
        }
        
        return message;
    }
    
    public static String doContentFilter(String message) {
        String result;
        try {
            StringBuilder info = new StringBuilder("\n»Ø¸´ ");
            int ptid = message.indexOf("ptid");
            int ptidEnd = message.indexOf("]", ptid) + 1;
            int urlBegin = message.indexOf("[/url]", ptidEnd);


            info.append(message.substring(ptidEnd, urlBegin));

            int iBeginIndex = message.indexOf("[i]") + 3;
            int iEndIndex   = message.indexOf("[/i]", iBeginIndex);


            info.append(" ");
            info.append(message.substring(iBeginIndex, iEndIndex) + "\n");
            
            result = info.toString();
        } catch (Exception e) {
            result = message;
        }
        return result;
    }
    
    public static String doQuoteFilter(String message) {
        String result;
        try {
            StringBuilder info = new StringBuilder("\nÒýÓÃ ");
            int quote = message.indexOf("[quote]") + 7;
            int size = message.indexOf("[size", quote);

            info.append(message.substring(quote, size) + "\n");
            info.append(" ");

            int color = message.indexOf("[color=#999999]") + 15;
            int endcolor = message.indexOf("[/color]", color);

            info.append(message.substring(color, endcolor) + "\n");
            result = info.toString();
        } catch (Exception e) {
            result = message;
        }
        return result;
    }
    
}
