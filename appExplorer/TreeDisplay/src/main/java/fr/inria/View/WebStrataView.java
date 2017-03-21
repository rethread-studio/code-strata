package fr.inria.View;

import fr.inria.DataStructure.Execution;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by nharrand on 21/03/17.
 */
public class WebStrataView {

    public Execution e;
    public int strataNb;
    public Map<Integer, File> strataImages;
    public Map<Integer, String> strataDesc;
    public String htmlTemplate = "strata.html";
    public String cssTemplate = "style.css";

    public WebStrataView(Execution e, int strataNb, Map<Integer, File> strataImages, Map<Integer, String> strataDesc) {
        this.e = e;
        this.strataNb = strataNb;
        this.strataDesc = strataDesc;
        this.strataImages = strataImages;
    }


    public void generateWeb() {
        String html = getTemplateByID(htmlTemplate);

        String stratumImgCode = "\t\t\t\t\t<div id=\"cell_ID\">\n" +
                "\t\t\t\t\t\t<span onclick=\"openNav()\"><img src=\"img/IMG\" /></span>\n" +
                "\t\t\t\t\t</div>";
        String strataImgCode = "";

        String stratumNavCode = "\t\t\t\t\t<img src=\"img/IMG\" id=\"img_ID\" />";
        String strataNavCode = "";

        for(Integer i : strataImages.keySet()) {
            strataImgCode += "\n" + stratumImgCode.replace("ID", i.toString()).replace("IMG", strataImages.get(i).getName());
            strataNavCode += "\n" + stratumNavCode.replace("ID", i.toString()).replace("IMG", strataImages.get(i).getName());
        }

        html = html.replace("<!-- Strata_img -->", strataImgCode);
        html = html.replace("<!-- Strata_nav -->", strataNavCode);

        html = html.replace("/*Strata_nb*/", "" + strataNb);
        html = html.replace("/*Strata_max*/", "" + (strataNb-1));

        String stratumMiniCode = "\t\t\t\t\t\t<div id=\"mini_ID\">\n" +
                "\t\t\t\t\t\t\t<img src=\"img/mini/mini_ID.png\" />\n" +
                "\t\t\t\t\t\t</div>";
        String stataMiniCode = "";

        for(int i = 0; i < strataNb; i++) {
            stataMiniCode += "\n" + stratumMiniCode.replace("ID", "" + i);
        }
        html = html.replace("<!-- Strata_mini -->", stataMiniCode);

        String stratumDescCode = "\t\t\t\t\t\t<div id=\"desc_ID\">\n" +
                "\t\t\t\t\t\t\tDESC\n" +
                "\t\t\t\t\t\t</div>";
        String strataDescCode = "";

        for(Integer i : strataDesc.keySet()) {
            strataDescCode += "\n" + stratumDescCode.replace("ID", i.toString()).replace("DESC", strataDesc.get(i));
        }

        html = html.replace("<!-- Strata_desc -->", strataDescCode);



        writeFile(new File(e.outputDir, "strata.html"), html);
        writeFile(new File(e.outputDir, "style.css"), getTemplateByID(cssTemplate));
    }

    public static String getTemplateByID(String template_id) {
        InputStream input = WebStrataView.class.getClassLoader().getResourceAsStream(template_id);
        String result = null;
        try {
            if (input != null) {
                result = org.apache.commons.io.IOUtils.toString(input, java.nio.charset.Charset.forName("UTF-8"));
                input.close();
            } else {
                System.out.println("[Error] Template not found: " + template_id);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null; // the template was not found
        }
        return result;
    }

    public static void writeFile(File output, String content) {
        try {
            PrintWriter w = new PrintWriter(output);
            w.print(content);
            w.close();
        } catch (Exception ex) {
            System.err.println("Problem writing config file");
            ex.printStackTrace();
        }
    }
}
