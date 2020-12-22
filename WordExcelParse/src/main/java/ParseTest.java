import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @Author zhangyugu
 * @Date 2020/12/18 5:57 下午
 * @Version 1.0
 */
public class ParseTest {

    public static void main(String[] args) throws IOException {
        extractInfo("/Users/zhangyugu/Documents/思政机考名单（给捷点）.xlsx");
    }

    @Data
    static class ClassInfo {
        private String name;
        private String major;
        private String institute;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getInstitute() {
            return institute;
        }

        public void setInstitute(String institute) {
            this.institute = institute;
        }
    }

    public static void extractInfo(String path) throws IOException {
        Workbook workbook = new XSSFWorkbook(path);
        Sheet sheet = workbook.getSheetAt(0);

        int TEST_TIME_COL = 0;
        int CLASS_ROOM_COL = 1;
        int SEAT_COL = 2;
        int CLASS_NAME_COL = 3;
        int COURSE_NAME_COL = 4;
        int COURSE_TEACHER_COL = 5;
        int STUDENT_NO_COL = 7;
        int STUDENT_NAME_COL = 8;
        int STUDENT_SEX_COL = 9;
        int INSTITUTE_COL = 10;
        int MAJOR_COL = 12;

        Map<String, ClassInfo> classInfoMap = new HashMap<>();
        Map<String, Set<String>> teacherClassInfos = new HashMap<>();
        Map<String,StringBuffer> testStudentsMap = new HashMap<>();

        for(int i = sheet.getFirstRowNum() + 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String testTime = row.getCell(TEST_TIME_COL).getStringCellValue();
            String classRoom = row.getCell(CLASS_ROOM_COL).getStringCellValue();
            String seat = row.getCell(SEAT_COL).getStringCellValue();
            String className = row.getCell(CLASS_NAME_COL).getStringCellValue();
            String courseName = row.getCell(COURSE_NAME_COL).getStringCellValue();
            String courseTeacher = row.getCell(COURSE_TEACHER_COL).getStringCellValue();
            String studentNo = row.getCell(STUDENT_NO_COL).getStringCellValue();
            String studentName = row.getCell(STUDENT_NAME_COL).getStringCellValue();
            String studentSex = row.getCell(STUDENT_SEX_COL).getStringCellValue();
            String institute = row.getCell(INSTITUTE_COL).getStringCellValue();
            String major = row.getCell(MAJOR_COL).getStringCellValue();

            if(!classInfoMap.containsKey(className)) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setInstitute(institute);
                classInfo.setMajor(major);
                classInfo.setName(className);
                classInfoMap.put(className, classInfo);
            }
            if(!teacherClassInfos.containsKey(courseTeacher)) {
                teacherClassInfos.put(courseTeacher, new HashSet<>());
            }
            teacherClassInfos.get(courseTeacher).add(String.format("'%s'", className));

            String testId = String.format("%s|%s|%s", testTime, courseName, courseTeacher);
            if(!testStudentsMap.containsKey(testId)) {
                testStudentsMap.put(testId, new StringBuffer());
            }
            testStudentsMap.get(testId).append(String.format("%s,%s", studentNo, studentName)).append("\n");
        }

        //学生信息excel
        //writeClassInfos(classInfoMap);

        //教师的负责班级列表
//        for(Map.Entry<String, Set<String>> entry: teacherClassInfos.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }

        //考试学生信息
        for(Map.Entry<String, StringBuffer> entry: testStudentsMap.entrySet()) {
            System.out.println(entry.getKey()+"============================");
            System.out.println(entry.getValue());
        }
    }

    /**
     * 将班级信息写入到excel中
     * @param classInfoMap
     * @throws IOException
     */
    private static void writeClassInfos(Map<String, ClassInfo> classInfoMap) throws IOException {
        Workbook toWriteBook = new XSSFWorkbook();
        Sheet classInfoSheet = toWriteBook.createSheet();
        classInfoSheet.setColumnWidth(0, 5000);
        classInfoSheet.setColumnWidth(1, 5000);
        classInfoSheet.setColumnWidth(2, 5000);
        Row row = classInfoSheet.createRow(0);
        createCell(row,0).setCellValue("班级名称");
        createCell(row,1).setCellValue("学院名称");
        createCell(row,2).setCellValue("专业名称");
        classInfoMap.values().stream().forEach(new Consumer<ClassInfo>() {
            int i = 1;
            @Override
            public void accept(ClassInfo classInfo) {
                Row row = classInfoSheet.createRow(i++);
                createCell(row, 0).setCellValue(classInfo.getName());
                createCell(row,1).setCellValue(classInfo.getInstitute());
                createCell(row,2).setCellValue(classInfo.getMajor());
            }
        });
        toWriteBook.write(new FileOutputStream("/Users/zhangyugu/Documents/classInfo.xlsx"));
    }

    private static Cell createCell(Row row, int cellNum) {
        Cell cell = row.createCell(cellNum);
        Workbook wb = cell.getSheet().getWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(false);
//        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
//
//        Font font = wb.createFont();
//        font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
//        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        return cell;
    }
}
