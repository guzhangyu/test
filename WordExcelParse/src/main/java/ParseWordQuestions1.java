import lombok.Data;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhangyugu
 * @Date 2020/12/19 11:27 上午
 * @Version 1.0
 */
public class ParseWordQuestions1 {

    @Data
    static class Judge {
        String content;
        String answer;
    }

    @Data
    static class AbstractChoice {
        String content;
        String answer;
        List<String> selections;
    }

    public static void main(String[] args) throws IOException {
        readWord("第四章");
    }

    public static boolean readWord(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream("/Users/zhangyugu/Documents/马原习题/" + fileName + ".doc");
        WordExtractor wordExtractor = new WordExtractor(inputStream);
        String fullText = wordExtractor.getText();
        byte[] space = new byte[]{(byte) 0xC2, (byte)0xA0};
        fullText = fullText.replaceAll(new String(space, "UTF-8"),"");

        System.out.println(fullText);
        fullText = fullText.replaceAll("．",".").replaceAll("\\s+","")
                .replaceAll("（","(").replaceAll("）", ")")
                .replaceAll("　", "").replaceAll("PAGE\\d+","");

        System.out.println(fullText);
        int singleQuestionIndex = fullText.indexOf("单项选择题");
        if(singleQuestionIndex < 0) {
            singleQuestionIndex = fullText.indexOf("一、单选题：") + 6;
        }else {
            singleQuestionIndex =  singleQuestionIndex + 5;
        }
        int multipleQuestionsIndex = fullText.indexOf("二、多项选择题");
        String singleQuestionTxt = fullText.substring( singleQuestionIndex, multipleQuestionsIndex);

        String multipleQuestionTxt = fullText.substring(multipleQuestionsIndex + (fullText.indexOf("二、多项选择题：")<0?7:8));

        List<AbstractChoice> singleChoices = extractChoices(singleQuestionTxt);
        List<AbstractChoice> multipleChoices = extractChoices(multipleQuestionTxt);

        for(AbstractChoice choice: multipleChoices) {
            System.out.println(choice.getSelections());
        }
        //writeExcel(singleChoices, true, "/Users/zhangyugu/Documents/马原习题excel/" + fileName + "单选题.xlsx");
        //writeExcel(multipleChoices, false, "/Users/zhangyugu/Documents/马原习题excel/" + fileName + "多选题.xlsx");

        return true;
    }

    private static void writeExcel(List<AbstractChoice> choices, Boolean single, String fileName) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("题目");
        row.createCell(1).setCellValue("选项A");
        row.createCell(2).setCellValue("选项B");
        row.createCell(3).setCellValue("选项C");
        row.createCell(4).setCellValue("选项D");
        if(single) {
            row.createCell(5).setCellValue("难度");
            row.createCell(6).setCellValue("答案");
        }else {
            row.createCell(5).setCellValue("选项E");
            row.createCell(6).setCellValue("难度");
            row.createCell(7).setCellValue("答案");
        }

        for(int i=0; i<choices.size(); i++) {
            AbstractChoice choice = choices.get(i);
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(choice.getContent());
            if(single) {
                for(int j=0; j<4; j++) {
                    row.createCell(j+1).setCellValue(choice.getSelections().get(j));
                }
                row.createCell(5).setCellValue("一般");
                row.createCell(6).setCellValue(choice.getAnswer());
            }else {
                for(int j=0; j<choice.getSelections().size(); j++) {
                    row.createCell(j+1).setCellValue(choice.getSelections().get(j));
                }
                row.createCell(6).setCellValue("一般");
                row.createCell(7).setCellValue(choice.getAnswer());
            }
        }
        wb.write(new FileOutputStream(fileName));
    }

    static Pattern notDotAnswerPattern = Pattern.compile("[A-Z]");

    private static List<AbstractChoice> extractChoices(String singleQuestionTxt) {
        String[] singleQuestions = singleQuestionTxt.split("\\d+[、\\.]");
        Pattern questionPattern = Pattern.compile("(.+?)\\n?\\(([A-Z]+)\\)");
        Pattern answerPattern = Pattern.compile("[A-Z][\\.、]");

//        Matcher m = questionPattern.matcher("任何科学理论都不能穷尽真理，而只能在实践中不断地开辟认识真理的道路。这说明\n" +
//                "        （     C  ）");
//        System.out.println(m.find());
//        System.out.println(m.group(1));
//        System.out.println(m.group(3));
//        System.exit(0);

        List<AbstractChoice> singleChoices = new ArrayList<>();
        for (String singleQuestion: singleQuestions) {
            if(singleQuestion.length()==0) {
                continue;
            }

//            System.out.println(singleQuestion);
            Matcher questionMatcher = questionPattern.matcher(singleQuestion);
            questionMatcher.find();
            String content = questionMatcher.group(1);
            String answer = questionMatcher.group(2);

            String answerTxt = singleQuestion.substring(questionMatcher.end());
            String[] answers = answerPattern.split(answerTxt);
            List<String> selections = new ArrayList<>();
            if(answers.length < 2) {
                answers = notDotAnswerPattern.split(answerTxt);
            }
            for(String answerItem: answers) {
                if(answerItem.trim().length()==0) {
                    continue;
                }
                selections.add(answerItem.trim());
            }

            AbstractChoice choice = new AbstractChoice();
            choice.setContent(content);
            choice.setAnswer(answer);
            choice.setSelections(selections);
            singleChoices.add(choice);
        }
        return singleChoices;
    }
}
