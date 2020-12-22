import lombok.Data;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zhangyugu
 * @Date 2020/12/22 12:02 下午
 * @Version 1.0
 */
public class ParseWordJDSGY {

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
        readWord();
    }

    static Pattern QUESTION_PATTERN = Pattern.compile("^\\d+、");

    public static boolean readWord() throws IOException {
        InputStream inputStream = new FileInputStream("/Users/zhangyugu/Downloads/《中国近现代史纲要》期末考试复习题(上综-第七章) - 副本.docx");
//        WordExtractor wordExtractor = new WordExtractor(inputStream);
//        String fullText = wordExtractor.getText();
//        System.out.println(fullText);

        XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
        List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();

        final int JUDGE_CASE = 1, SINGLE_CASE = 2, MULTIPLE_CASE = 3,
        SINGLE_ANSWER=4, MULTIPLE_ANSWER=5;
        int nowCase = 1;
        List<Judge> judges = new ArrayList<>();
        List<AbstractChoice> singleChoices = new ArrayList<>();
        List<AbstractChoice> multipleChoices = new ArrayList<>();

        for(int i=0; i<paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            String text = paragraph.getText();
            if("一、 是非判断题：".equals(text)) {
                while(!(text = paragraphs.get(++i).getText()).equals("")) {
                    Judge judge = new Judge();
                    judge.setContent(text.replace("^\\d+、", ""));
                    judges.add(judge);
                }
            }else if("二、 单项选择题:".equals(text)) {
                i = addChoiceQuestions(paragraphs, singleChoices, i);
            }else if("三、多项选择题：".equals(text)) {
                nowCase = MULTIPLE_CASE;
            }
        }

//        String fullText = null;
//        byte[] space = new byte[]{(byte) 0xC2, (byte)0xA0};
//        fullText = fullText.replaceAll(new String(space, "UTF-8"),"");
//
//        System.out.println(fullText);
//        fullText = fullText.replaceAll("．",".").replaceAll("\\s+","")
//                .replaceAll("（","(").replaceAll("）", ")")
//                .replaceAll("　", "").replaceAll("PAGE\\d+","");
//
//        System.out.println(fullText);
//        int singleQuestionIndex = fullText.indexOf("单项选择题");
//        if(singleQuestionIndex < 0) {
//            singleQuestionIndex = fullText.indexOf("一、单选题：") + 6;
//        }else {
//            singleQuestionIndex =  singleQuestionIndex + 5;
//        }
//        int multipleQuestionsIndex = fullText.indexOf("二、多项选择题");
//        String singleQuestionTxt = fullText.substring( singleQuestionIndex, multipleQuestionsIndex);
//
//        String multipleQuestionTxt = fullText.substring(multipleQuestionsIndex + (fullText.indexOf("二、多项选择题：")<0?7:8));
//
//        List<ParseWordQuestions1.AbstractChoice> singleChoices = extractChoices(singleQuestionTxt);
//        List<ParseWordQuestions1.AbstractChoice> multipleChoices = extractChoices(multipleQuestionTxt);
//
//        for(ParseWordQuestions1.AbstractChoice choice: multipleChoices) {
//            System.out.println(choice.getSelections());
//        }
//        //writeExcel(singleChoices, true, "/Users/zhangyugu/Documents/马原习题excel/" + fileName + "单选题.xlsx");
//        //writeExcel(multipleChoices, false, "/Users/zhangyugu/Documents/马原习题excel/" + fileName + "多选题.xlsx");
//
        return true;
    }

    private static int addChoiceQuestions(List<XWPFParagraph> paragraphs, List<AbstractChoice> singleChoices, int i) {
        XWPFParagraph paragraph;
        AbstractChoice singleChoice = null;
        StringBuffer answers = null;
        String text = null;

        while(!(text = paragraphs.get(++i).getText()).equals("")) {
            Matcher questionMatcher = QUESTION_PATTERN.matcher(text);
            if(questionMatcher.find()) {
                if(answers!=null && answers.length() > 0) {
                    singleChoice.setSelections(str2Selections(answers));
                    singleChoices.add(singleChoice);
                }

                answers = new StringBuffer();
                singleChoice = new AbstractChoice();
                singleChoice.setContent(questionMatcher.replaceFirst(""));
            }else {
                answers.append(text);
            }
        }
        singleChoice.setSelections(str2Selections(answers));
        singleChoices.add(singleChoice);
        return i;
    }

    private static List<String> str2Selections(StringBuffer answers) {
        List<String> selections = new ArrayList<>();
        String[] answerArr = answers.toString().split("[A-Z]、");
        for(String answer: answerArr) {
            answer = answer.trim();
            if(answer.length() == 0) {
                continue;
            }
            selections.add(answer);
        }
        return selections;
    }

    private static void writeExcel(List<ParseWordQuestions1.AbstractChoice> choices, Boolean single, String fileName) throws IOException {
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
            ParseWordQuestions1.AbstractChoice choice = choices.get(i);
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

    private static List<ParseWordQuestions1.AbstractChoice> extractChoices(String singleQuestionTxt) {
        String[] singleQuestions = singleQuestionTxt.split("\\d+[、\\.]");
        Pattern questionPattern = Pattern.compile("(.+?)\\n?\\(([A-Z]+)\\)");
        Pattern answerPattern = Pattern.compile("[A-Z][\\.、]");

//        Matcher m = questionPattern.matcher("任何科学理论都不能穷尽真理，而只能在实践中不断地开辟认识真理的道路。这说明\n" +
//                "        （     C  ）");
//        System.out.println(m.find());
//        System.out.println(m.group(1));
//        System.out.println(m.group(3));
//        System.exit(0);

        List<ParseWordQuestions1.AbstractChoice> singleChoices = new ArrayList<>();
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

            ParseWordQuestions1.AbstractChoice choice = new ParseWordQuestions1.AbstractChoice();
            choice.setContent(content);
            choice.setAnswer(answer);
            choice.setSelections(selections);
            singleChoices.add(choice);
        }
        return singleChoices;
    }
}
