package prik.lib.modules.org.psoup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import prik.lib.Function;
import prik.lib.MapValue;
import prik.lib.StringValue;
import prik.lib.Value;
import prik.lib.Variables;
import prik.lib.modules.Module;

/**
 *
 * @author Professional
 */
public final class pSoup implements Module {
    public static String docs = "";
    public static Document docum;
    public static Elements element;
    public static Element elem;

    @Override
    public void init() {
        MapValue psoup = new MapValue(3);
        psoup.set("parser", new pars());
        psoup.set("select", new select());
        psoup.set("body", new body());
        Variables.define("pSoup", psoup);
    }

    private static class body implements Function {
        @Override
        public Value execute(Value... args) {
            if (docum != null) {
                return new StringValue(docum.body().toString());
            } else {
                return new StringValue("Document not parsed yet");
            }
        }
    }

    private static class pars implements Function {
        @Override
        public Value execute(Value... args) {
            Document doc = null;
            try {
                doc = Jsoup.connect(args[0].toString()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            docs = doc.toString();
            Variables.set("document", new StringValue(Arrays.toString((docs).getBytes(StandardCharsets.UTF_8))));
            docum = doc;
            return new StringValue(docum.toString());


        }
    }


    private static class select implements Function {
        @Override
        public Value execute(Value... args) {
            Elements divs = docum.select(args[0].toString());
            Variables.set("elements", new StringValue(Arrays.toString((divs.toString()).getBytes(StandardCharsets.UTF_8))));
            element = divs;
            return new StringValue(element.toString());
        }
    }
}
