package se.martenolsson.lah15.classes;

/**
 * Created by martenolsson on 15-12-22.
 */
public class CleanTextString {
    public String returnCleanTextString(String string) {
        return string
                .replace(" ","%20")
                .replace("A","%41")
                .replace("B","%42")
                .replace("C","%43")
                .replace("D","%44")
                .replace("E","%45")
                .replace("F","%46")
                .replace("G","%47")
                .replace("H","%48")
                .replace("I","%49")
                .replace("J","%4A")
                .replace("K","%4B")
                .replace("L","%4C")
                .replace("M","%4D")
                .replace("N","%4E")
                .replace("O","%4F")
                .replace("P","%50")
                .replace("Q","%51")
                .replace("R","%52")
                .replace("S","%53")
                .replace("T","%54")
                .replace("U","%55")
                .replace("V","%56")
                .replace("W","%57")
                .replace("X","%58")
                .replace("Y","%59")
                .replace("Z","%5A")
                .replace("å","%C3%A5")
                .replace("ä","%C3%A4")
                .replace("ö","%C3%B6")
                .replace("Å","%C3%85")
                .replace("Ä","%C3%84")
                .replace("Ö","%C3%96")
                .replace("(", "%28")
                .replace(")", "%29")
                .replace("'", "%27")
                .replace("‘", "%91")
                .replace("’", "%92");
    }
}
