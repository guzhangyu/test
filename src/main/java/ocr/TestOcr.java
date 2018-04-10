//package ocr;
//
//import net.sourceforge.tess4j.Tesseract;
//import net.sourceforge.tess4j.TesseractException;
//
//import java.io.File;
//
///**
// * Created by guzy on 16/10/25.
// */
//public class TestOcr {
//
//    public static void main(String[] args) throws TesseractException {
//        File file=new File("/Users/guzy/Desktop/IMG_0168.JPG");
//        Tesseract instance=Tesseract.getInstance();
//        instance.setDatapath("/usr/local/share/tessdata");
//        instance.setLanguage("chi_sim");
//        String result=instance.doOCR(file);
//        System.out.println(result);
//    }
//}
