package no.ntnu.stud.avikeyb.backend.layouts;

import java.util.HashMap;

import no.ntnu.stud.avikeyb.backend.InputType;
import no.ntnu.stud.avikeyb.backend.Keyboard;

/**
 * Created by ingalill on 10/02/2017.
 */

public class AdaptiveLayout extends StepLayout {

    HashMap<String, String[]> adaptiveLayout;


    private Keyboard keyboard;

    public AdaptiveLayout(Keyboard keyboard) {
        fillAdaptiveLayout();
        this.keyboard = keyboard;
    }


    /**
     * Maps every letter a-z with the frequency of other letters.
     */
    public void fillAdaptiveLayout() {

        adaptiveLayout = new HashMap<>();
        adaptiveLayout.put(" ", new String[]{"taoisw", "cbphfm", "drelng", "uvyjkq", "xz "});
        adaptiveLayout.put("a", new String[]{"nrtlsc", " imdpg", "byukvw", "fhzxae", "joq"});
        adaptiveLayout.put("b", new String[]{"ealoru", "i sbyt", "jvcwpn", "mdzxqk", "hgf"});
        adaptiveLayout.put("c", new String[]{"oheakt", "irul c", "ysqmpn", "gdzxwv", "jfb"});
        adaptiveLayout.put("d", new String[]{" eiaos", "ruydlg", "nmwvjf", "ctphbq", "kzx"});
        adaptiveLayout.put("e", new String[]{" rsdna", "ltcemp", "vxygfw", "iobhqk", "zuj"});
        adaptiveLayout.put("f", new String[]{"ieoarf", "ul tys", "wcbzxv", "qpnmkj", "hgd"});
        adaptiveLayout.put("g", new String[]{" ehria", "ouslgn", "ytmfwd", "bzxvqp", "kjc"});
        adaptiveLayout.put("h", new String[]{"eao it", "uryhnl", "smbwfd", "qczxvp", "kjg"});
        adaptiveLayout.put("i", new String[]{"nstcoe", "ladrgv", "mpf bz", "kxquji", "ywh"});
        adaptiveLayout.put("j", new String[]{"eoaui ", "trzyxw", "vsqpnm", "lkjhgf", "dcb"});
        adaptiveLayout.put("k", new String[]{"e isya", "nlourt", "fmdwpg", "khzxvq", "jcb"});
        adaptiveLayout.put("l", new String[]{"ei lao", "ysudtf", "vkmcpb", "rnwgzx", "qjh"});
        adaptiveLayout.put("m", new String[]{"aeio p", "mbusyn", "cfrtlk", "zxwvqj", "hgd"});
        adaptiveLayout.put("n", new String[]{"g teds", "iaconk", "yvuflb", "jmhzrq", "pxw"});
        adaptiveLayout.put("o", new String[]{"nrulmo", "tsw pc", "dvbkai", "gfyehx", "zjq"});
        adaptiveLayout.put("p", new String[]{"eraoil", " puhst", "ymkdfc", "nbzxwv", "qjg"});
        adaptiveLayout.put("q", new String[]{"u zyxw", "vtsrqp", "onmlkj", "ihgfed", "cba"});
        adaptiveLayout.put("r", new String[]{"e aios", "trydun", "mglkcv", "bpfwhj", "zxq"});
        adaptiveLayout.put("s", new String[]{" teish", "ouapcl", "ymwknf", "qdgbrv", "jzx"});
        adaptiveLayout.put("t", new String[]{" eiahr", "ostuyl", "cmwnzf", "bdvgpx", "qkj"});
        adaptiveLayout.put("u", new String[]{"rnsltm", "cegiad", "pbf hz", "yxvoku", "qjw"});
        adaptiveLayout.put("v", new String[]{"eiao y", "vusrlz", "xwtqpn", "mkjhgf", "dcb"});
        adaptiveLayout.put("w", new String[]{"aei ho", "nsrlwd", "bykftm", "uzxvqp", "jgc"});
        adaptiveLayout.put("x", new String[]{"pi tea", "cyuosh", "wqzxvr", "nmlkjg", "fdb"});
        adaptiveLayout.put("y", new String[]{" seiao", "lcmdnt", "prwbug", "fzkhvy", "xqj"});
        adaptiveLayout.put("z", new String[]{"ea ioz", "yvuslg", "xwtrqp", "nmkjhf", "dcb"});
    }


    @Override
    protected void onStep(InputType input) {

    }
}
